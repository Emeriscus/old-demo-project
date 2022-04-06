package catalog;

import catalog.classes.Book;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {


    Book book;

    @Test
    public void createBookWithValidValuesTest() {
        book = new Book("Az", List.of("Stephen King"), 1982, 13);

        assertEquals("Az", book.getTitle());
        assertEquals(List.of("Stephen King"), book.getContributors());
        assertEquals(1982, book.getYearOfPublication());
        assertEquals(13, book.getQuantity());
    }

    @Test
    public void createBookWithInvalidValuesTest() {

        IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () ->
                new Book("", List.of("Stephen King"), 1982, 13));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Book(null, List.of("Stephen King"), 1982, 13));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Book("Az", null, 1982, 13));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Book("Az", List.of("Stephen King"), 999, 13));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Book("Az", List.of("Stephen King"), LocalDate.now().getYear() + 1, 13));
        assertEquals(IllegalArgumentException.class, expected.getClass());

        expected = assertThrows(IllegalArgumentException.class, () ->
                new Book("Az", List.of("Stephen King"), 1982, -1));
        assertEquals(IllegalArgumentException.class, expected.getClass());
    }
}
