package catalog.utils;

import java.time.LocalDate;
import java.util.List;

public class Validators {

    public static boolean isBlank(String text) {
        return text == null || text.isBlank();
    }

    public static boolean isEmpty(List<String> stringList) {
        return stringList == null || stringList.isEmpty();
    }

    public static boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isValidYear(int year) {
        return year >= 1000 && year <= LocalDate.now().getYear();
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }

    public static boolean isValidAudio
            (String title, List<String> performers, List<String> composers, int yearOfPublication, int quantity) {
        if (isBlank(title)) {
            throw new IllegalArgumentException("title cannot be empty");
        }
        if (isEmpty(performers) || isEmpty(composers)) {
            throw new IllegalArgumentException("performers and composers cannot be empty");
        }
        if (!isValidYear(yearOfPublication)) {
            throw new IllegalArgumentException("The year must be between 1000 and the current year");
        }
        if (!isValidQuantity(quantity)) {
            throw new IllegalArgumentException("The quantity must not above 0");
        }
        return true;
    }

    public static boolean isValidBook(String title, List<String> authors, int yearOfPublication, int quantity) {
        if (isBlank(title)) {
            throw new IllegalArgumentException("title cannot be empty");
        }
        if (isEmpty(authors)) {
            throw new IllegalArgumentException("The authors cannot be empty");
        }
        if (!isValidYear(yearOfPublication)) {
            throw new IllegalArgumentException("The year must be between 1000 and the current year");
        }
        if (!isValidQuantity(quantity)) {
            throw new IllegalArgumentException("The quantity must not above 0");
        }
        return true;
    }
}
