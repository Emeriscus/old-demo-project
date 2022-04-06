package catalog;

import catalog.classes.Audio;
import catalog.classes.Book;
import catalog.classes.LibraryItem;
import catalog.repository.AudioRepository;
import catalog.repository.BookRepository;
import catalog.repository.LibraryItemRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {

    Flyway flyway;
    AudioRepository audioRepository;
    BookRepository bookRepository;
    LibraryItemRepository libraryItemRepository;
    LibraryService libraryService;
    Audio audio;
    Book book;

    @BeforeEach
    void init() {

        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/library-catalog-test?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("root");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!", sqle);
        }

        flyway = Flyway.configure()/*.locations("src/test/resources/db/migration")*/.dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        audioRepository = new AudioRepository(dataSource);
        bookRepository = new BookRepository(dataSource);
        libraryItemRepository = new LibraryItemRepository(dataSource);

        libraryService = new LibraryService(bookRepository, audioRepository, libraryItemRepository);

        libraryService.addLibraryItem(
                (new Book("Conan a barbár", List.of("Robert E. Howard"), 1932, 3)));
        libraryService.addLibraryItem(
                (new Audio("Illegális bál", List.of("Aurora"), List.of("Vigi, Pusztai Zoltán"), 1997, 4)));
        libraryService.addLibraryItem(
                (new Book("A tűzhegy varázslója", List.of("Steve Jackson", "Ian Livingstone"), 1982, 6)));
        libraryService.addLibraryItem(
                (new Audio("Viperagarzon", List.of("Másfél"), List.of("Másfél"), 1996, 1)));
    }

    @Test
    void addAudioTest() {
        audio = new Audio("Viperagarzon", List.of("Másfél"), List.of("Bácsi Attila", "Hegedűs János"), 1996, 1);

        long libraryItemId = libraryService.addLibraryItem(audio);
        Audio expected = (Audio) libraryService.getLibraryItemById(libraryItemId).get();

        assertEquals("Viperagarzon", expected.getTitle());
        assertEquals(List.of("Másfél"), expected.getPerformers());
        assertEquals(List.of("Bácsi Attila", "Hegedűs János"), expected.getComposers());
        assertEquals(1996, expected.getYearOfPublication());
        assertEquals(1, expected.getQuantity());
        assertEquals(5, libraryService.getAllLibraryItem().size());
    }

    @Test
    void addBookTest() {
        book = new Book("A tűzhegy varázslója", List.of("Steve Jackson", "Ian Livingstone"), 1982, 6);

        long libraryItemId = libraryService.addLibraryItem(book);
        Book expected = (Book) libraryService.getLibraryItemById(libraryItemId).get();

        assertEquals("A tűzhegy varázslója", expected.getTitle());
        assertEquals(List.of("Steve Jackson", "Ian Livingstone"), expected.getContributors());
        assertEquals(1982, expected.getYearOfPublication());
        assertEquals(6, expected.getQuantity());
        assertEquals(5, libraryService.getAllLibraryItem().size());
    }

    @Test
    public void addNullItemTest() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> libraryService.addLibraryItem(null));

        assertEquals("Book or Audio cannot be null!", exception.getMessage());
    }

    @Test
    void getLibraryItemByTitleTest() {
        assertEquals("Conan a barbár", libraryService.getLibraryItemByTitle("Conan a barbár").get().getTitle());
        assertEquals(List.of("Robert E. Howard"), libraryService.getLibraryItemByTitle("Conan a barbár").get().getContributors());
        assertEquals(1932, libraryService.getLibraryItemByTitle("Conan a barbár").get().getYearOfPublication());
        assertEquals(3, libraryService.getLibraryItemByTitle("Conan a barbár").get().getQuantity());
    }

    @Test
    void getLibraryItemByIdTest() {
        libraryService.addLibraryItem(
                (new Book("Conan a barbár", List.of("Robert E. Howard"), 1932, 3)));
        long id = libraryService.addLibraryItem(
                (new Audio("Illegális bál", List.of("Aurora"), List.of("Vigi, Pusztai Zoltán"), 1997, 4)));

        assertEquals("Illegális bál", libraryService.getLibraryItemById(id).get().getTitle());
        assertEquals(1997, libraryService.getLibraryItemById(id).get().getYearOfPublication());
        assertEquals(4, libraryService.getLibraryItemById(id).get().getQuantity());
    }

    @Test
    void getAllLibraryItemTest() {
        List<LibraryItem> expected = libraryService.getAllLibraryItem();

        assertEquals(4, expected.size());
    }

    @Test
    void getAllLibraryItemByTitleFragmentTest() {
        assertEquals(2, libraryService.getAllLibraryItemByTitleFragment("bá").size());
    }

    @Test
    void deleteLibraryItemByTitleTest() {
        assertEquals(4, libraryService.getAllLibraryItem().size());
        libraryService.deleteLibraryItemByTitle("Viperagarzon");
        assertEquals(3, libraryService.getAllLibraryItem().size());
    }

    @Test
    void deleteLibraryItemByIdTest() {
        long id = libraryService.addLibraryItem(
                (new Book("Dimension of Miracles", List.of("Robert Sheckley"), 1968, 5)));

        assertEquals(5, libraryService.getAllLibraryItem().size());
        libraryService.deleteLibraryItemById(id);
        assertEquals(4, libraryService.getAllLibraryItem().size());
    }

    @Test
    void borrowLibraryItemByTitleTest() {
        assertEquals(3, libraryService.getLibraryItemByTitle("Conan a barbár").get().getQuantity());
        libraryService.borrowLibraryItemByTitle("Conan a barbár");
        assertEquals(2, libraryService.getLibraryItemByTitle("Conan a barbár").get().getQuantity());
    }

    @Test
    void borrowLibraryItemByTitleNotAvailableItemTest() {
        assertEquals(1, libraryService.getLibraryItemByTitle("Viperagarzon").get().getQuantity());
        libraryService.borrowLibraryItemByTitle("Viperagarzon");
        assertEquals(0, libraryService.getLibraryItemByTitle("Viperagarzon").get().getQuantity());
    }
}