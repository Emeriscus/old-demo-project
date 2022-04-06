package catalog;

import catalog.repository.AudioRepository;
import catalog.repository.BookRepository;
import catalog.repository.LibraryItemRepository;
import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

public class LibraryMain {

    public static void main(String[] args) {

        MariaDbDataSource dataSource = new MariaDbDataSource();

        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/library-catalog?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("root");
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot reach database", se);
        }

        Flyway flyway = Flyway.configure().locations("db/migration").dataSource(dataSource).load();
        flyway.migrate();

        AudioRepository audioRepository = new AudioRepository(dataSource);
        BookRepository bookRepository = new BookRepository(dataSource);
        LibraryItemRepository libraryItemRepository = new LibraryItemRepository(dataSource);

        LibraryService libraryService = new LibraryService(bookRepository, audioRepository, libraryItemRepository);
        Menu menu = new Menu(libraryService);

        menu.runMenu();
    }
}
