package catalog;

import catalog.classes.Audio;
import catalog.classes.Book;
import catalog.utils.StringListConverters;
import catalog.utils.Validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private int choice = 0;
    private LibraryService libraryService;

    public Menu(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void runMenu() {
        do {
            printMenu();
            switch (choice) {
                case 1:
                    runMenuItemOne();
                    break;
                case 2:
                    runMenuItemTwo();
                    break;
                case 3:
                    runMenuItemThree();
                    break;
                case 4:
                    runMenuItemFour();
                    break;
                case 5:
                    runMenuItemFive();
                    break;
                case 6:
                    runMenuItemSix();
                    break;
                case 7:
                    runMenuItemSeven();
                    break;
                case 8:
                    runMenuItemEight();
                    break;
                case 9:
                default:
                    break;
            }
        } while (choice != 9);
    }

    public void printMenu() {
        System.out.println("1. Add library item");
        System.out.println("2. Find library item by title");
        System.out.println("3. Find library item by ID");
        System.out.println("4. Delete library item by title");
        System.out.println("5. Delete library item by ID");
        System.out.println("6. List all library item");
        System.out.println("7. List all library items by title fragment");
        System.out.println("8. Borrowing a library item");
        System.out.println("9. Exit");
        System.out.print("Please choose a number and press Enter: ");
        String input = scanner.nextLine();
        if (Validators.isNumber(input)) {
            choice = Integer.parseInt(input);
        } else {
            System.out.println("Not a number! Please press Enter and choose a number!");
            scanner.nextLine();
        }
    }

    private void runMenuItemOne() {
        System.out.println("What type of library item would you like add? (Book, Audio) ");
        String input = scanner.nextLine();
        if (Validators.isBlank(input)) {
            System.out.println("The type cannot be empty! Please press Enter and try again!");
            scanner.nextLine();
            return;
        }
        if (!input.equals("Book") && !input.equals("Audio")) {
            System.out.println("The type must be Book or Audio! Please press Enter and try again!");
            scanner.nextLine();
        }
        addLibraryItem(input);
    }

    private void runMenuItemTwo() {
        System.out.print("Please type the title to search for: ");
        System.out.println();
        String input = scanner.nextLine();
        if (libraryService.getLibraryItemByTitle(input).isPresent()) {
            System.out.println(libraryService.getLibraryItemByTitle(input).get());
        }
        choice = 0;
        System.out.println();
        System.out.println("Continue with Enter");
        scanner.nextLine();
    }

    private void runMenuItemThree() {
        System.out.print("Please type the ID to search for: ");
        System.out.println();
        String input = scanner.nextLine();
        if (!Validators.isNumber(input)) {
            System.out.println("Not a number! Please press Enter and choose a number!");
        }
        if (Validators.isNumber(input) && libraryService.getLibraryItemById(Integer.parseInt(input)).isPresent()) {
            System.out.println(libraryService.getLibraryItemById(Integer.parseInt(input)).get());
        }
        choice = 0;
        System.out.println();
        System.out.println("Continue with Enter");
        scanner.nextLine();
    }

    private void runMenuItemFour() {
        System.out.print("Please type the title: ");
        System.out.println();
        String input = scanner.nextLine();
        if (libraryService.getLibraryItemByTitle(input).isPresent()) {
            libraryService.deleteLibraryItemByTitle(input);
            System.out.println();
            System.out.println("The delete was successful");
        }
        choice = 0;
        System.out.println("Continue with Enter");
        scanner.nextLine();
    }

    private void runMenuItemFive() {
        System.out.print("Please type the ID: ");
        System.out.println();
        String input = scanner.nextLine();
        if (!Validators.isNumber(input)) {
            System.out.println("Not a number! Please press Enter and try again!");
        }
        if (Validators.isNumber(input) && libraryService.getLibraryItemById(Integer.parseInt(input)).isPresent()) {
            libraryService.deleteLibraryItemById(Integer.parseInt(input));
            System.out.println("The delete was successful");
            System.out.println("Continue with Enter");
            scanner.nextLine();
        } else {
            choice = 0;
            System.out.println();
            System.out.println("Continue with Enter");
            scanner.nextLine();
        }
    }

    private void runMenuItemSix() {
        System.out.println();
        System.out.println(libraryService.getAllLibraryItem());
        choice = 0;
        System.out.println("Continue with Enter");
        scanner.nextLine();
    }

    private void runMenuItemSeven() {
        System.out.print("Please type the title: ");
        System.out.println();
        System.out.println(libraryService.getAllLibraryItemByTitleFragment(scanner.nextLine()));
        choice = 0;
        System.out.println("Continue with Enter");
        scanner.nextLine();
    }

    private void runMenuItemEight() {
        System.out.println("Please type the title: ");
        String input = scanner.nextLine();
        if (libraryService.getLibraryItemByTitle(input).isPresent() &&
                libraryService.hasAvailableLibraryItemQuantitybyId(libraryService.getLibraryItemIdByTitle(input))) {
            libraryService.borrowLibraryItemByTitle(input);
            System.out.println();
            System.out.println("The borrow was successful");
            System.out.println("Continue with Enter");
            scanner.nextLine();
        } else {
            choice = 0;
            System.out.println(("There is not available item!"));
            System.out.println("Continue with Enter");
            scanner.nextLine();
        }
    }

    private void addLibraryItem(String itemType) {
        if (itemType.equals("Book")) {
            addBook();
        }
        if (itemType.equals("Audio")) {
            addAudio();
        }
    }

    private void addBook() {
        System.out.println("title ");
        String title = scanner.nextLine();
        if (Validators.isBlank(title)) {
            throw new IllegalArgumentException("The title cannot be empty");
        }
        System.out.println("authors separated ', ' ");
        List<String> authors = new ArrayList<>(StringListConverters.stringToList(scanner.nextLine()));
        System.out.println("year ");
        int yearOfProduction = scanner.nextInt();
        if (!Validators.isValidYear(yearOfProduction)) {
            throw new IllegalArgumentException("The year must be between 1000 and the current year");
        }
        System.out.println("quantity ");
        int quantity = scanner.nextInt();
        if (!Validators.isValidQuantity(quantity)) {
            throw new IllegalArgumentException("The quantity must not above 0");
        }
        scanner.nextLine();
        Book book = new Book(title, authors, yearOfProduction, quantity);
        libraryService.addLibraryItem(book);
        System.out.println("Add item was successful!");
        System.out.println("Continue with Enter");
        scanner.nextLine();
    }

    private void addAudio() {
        System.out.println("title ");
        String title = scanner.nextLine();
        System.out.println("performers separated ', ' ");
        List<String> performers = new ArrayList<>(StringListConverters.stringToList(scanner.nextLine()));
        System.out.println("composers separated ', ' ");
        List<String> composers = new ArrayList<>(StringListConverters.stringToList(scanner.nextLine()));
        System.out.println("year ");
        int yearOfProduction = scanner.nextInt();
        if (!Validators.isValidYear(yearOfProduction)) {
            throw new IllegalArgumentException("The year must be between 1000 and the current year");
        }
        System.out.println("quantity ");
        int quantity = scanner.nextInt();
        if (!Validators.isValidQuantity(quantity)) {
            throw new IllegalArgumentException("The quantity must not above 0");
        }
        scanner.nextLine();
        Audio audio = new Audio(title, performers, composers, yearOfProduction, quantity);
        libraryService.addLibraryItem(audio);
        System.out.println("Add item was successful!");
        System.out.println("Continue with Enter");
        scanner.nextLine();
    }


}
