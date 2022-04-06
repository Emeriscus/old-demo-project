package catalog.classes;

import java.util.List;

public interface LibraryItem {

    long getId();

    String getTitle();

    int getYearOfPublication();

    List<String> getContributors();

    int getQuantity();
}
