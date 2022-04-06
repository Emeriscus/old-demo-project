package catalog.classes;

import catalog.utils.Validators;

import java.util.ArrayList;
import java.util.List;

public class Book implements LibraryItem {

    private long id;
    private String title;
    private List<String> authors;
    private int yearOfPublication;
    private int quantity;

    public Book(String title, List<String> authors, int yearOfPublication, int quantity) {
        if (Validators.isValidBook(title, authors, yearOfPublication, quantity)) {
            this.title = title;
            this.authors = authors;
            this.yearOfPublication = yearOfPublication;
            this.quantity = quantity;
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getContributors() {
        return new ArrayList<>(authors);
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", yearOfPublication=" + yearOfPublication +
                ", quantity=" + quantity +
                '}';
    }
}
