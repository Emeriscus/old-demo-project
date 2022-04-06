package catalog;

import catalog.classes.Audio;
import catalog.classes.Book;
import catalog.classes.LibraryItem;
import catalog.repository.AudioRepository;
import catalog.repository.BookRepository;
import catalog.repository.LibraryItemRepository;
import catalog.utils.Validators;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryService {

    private BookRepository bookRepository;
    private AudioRepository audioRepository;
    private LibraryItemRepository libraryItemRepository;

    public LibraryService(BookRepository bookRepository, AudioRepository audioRepository, LibraryItemRepository libraryItemRepository) {
        this.bookRepository = bookRepository;
        this.audioRepository = audioRepository;
        this.libraryItemRepository = libraryItemRepository;
    }

    public long addLibraryItem(LibraryItem libraryItem) {
        long libraryItemId = libraryItemRepository.saveLibraryItem(libraryItem);
        if (libraryItem instanceof Book) {
            bookRepository.saveBookAndGetId(libraryItemId, (Book) libraryItem);
        }
        if (libraryItem instanceof Audio) {
            audioRepository.saveAudioAndGetId(libraryItemId, (Audio) libraryItem);
        }
        return libraryItemId;
    }

    public long getLibraryItemIdByTitle(String title) {
        return libraryItemRepository.getLibraryItemIdByTitle(title);
    }

    public Optional<LibraryItem> getLibraryItemByTitle(String title) {
        if (Validators.isBlank(title)) {
            System.out.println("The title cannot be empty! ! Please press Enter and try again!");
            return Optional.empty();
        }
        try {
            long libraryItemId = libraryItemRepository.getLibraryItemIdByTitle(title);
            if (isBook(libraryItemId)) {
                return Optional.of(bookRepository.getBookByLibraryItemsId(libraryItemId));
            } else {
                return Optional.of(audioRepository.getAudioByLibraryItemsId(libraryItemId));
            }
        } catch (EmptyResultDataAccessException erdae) {
            System.out.println("No result with this title: " + title);
            return Optional.empty();
        }
    }

    public Optional<LibraryItem> getLibraryItemById(long libraryItemId) {
        try {
            if (isBook(libraryItemId)) {
                return Optional.of(bookRepository.getBookByLibraryItemsId(libraryItemId));
            } else {
                return Optional.of(audioRepository.getAudioByLibraryItemsId(libraryItemId));
            }
        } catch (EmptyResultDataAccessException erdae) {
            System.out.println("No result with this ID: " + libraryItemId);
            return Optional.empty();
        }
    }

    public List<LibraryItem> getAllLibraryItem() {
        List<LibraryItem> result = new ArrayList<>();
        result.addAll(audioRepository.getAllAudioItem());
        result.addAll(bookRepository.getAllBookItem());
        return result;
    }

    public List<LibraryItem> getAllLibraryItemByTitleFragment(String fragment) {
        List<LibraryItem> result = new ArrayList<>();
        result.addAll(audioRepository.getAllAudioItemByTitleFragment(fragment));
        result.addAll(bookRepository.getAllBookItemByTitleFragment(fragment));
        return result;
    }

    public boolean deleteLibraryItemByTitle(String title) {
        if (Validators.isBlank(title)) {
            System.out.println("The title cannot be empty! ! Please press Enter and try again!");
        }
        try {
            long libraryItemId = libraryItemRepository.getLibraryItemIdByTitle(title);
            if (isBook(libraryItemId)) {
                bookRepository.deleteBookByLibraryItemsId(libraryItemId);
            } else {
                audioRepository.deleteAudioByLibraryItemsId(libraryItemId);
            }
            libraryItemRepository.deleteLibraryItemById(libraryItemId);
            return true;
        } catch (EmptyResultDataAccessException erdae) {
            System.out.println("No result with this title: " + title);
            return false;
        }
    }

    public boolean deleteLibraryItemById(long libraryItemId) {
        try {
            if (isBook(libraryItemId)) {
                bookRepository.deleteBookByLibraryItemsId(libraryItemId);
            } else {
                audioRepository.deleteAudioByLibraryItemsId(libraryItemId);
            }
            libraryItemRepository.deleteLibraryItemById(libraryItemId);
            return true;
        } catch (EmptyResultDataAccessException erdae) {
            System.out.println("No result with this ID: " + libraryItemId);
            return false;
        }
    }

    public void borrowLibraryItemByTitle(String title) {
        if (Validators.isBlank(title)) {
            System.out.println("The title cannot be empty! Please press Enter and try again!");
        }
        try {
            long libraryItemId = libraryItemRepository.getLibraryItemIdByTitle(title);

            libraryItemRepository.borrowLibraryItemById(libraryItemId);

        } catch (EmptyResultDataAccessException erdae) {
            System.out.println("No result with this ID: " + title);
        }
    }

    private boolean isBook(long libraryItemId) {
        return libraryItemRepository.getLibraryItemTypeById(libraryItemId).equals(Book.class.toString());
    }

    public boolean hasAvailableLibraryItemQuantitybyId(long libraryItemId) {
        return libraryItemRepository.hasAvailableLibraryItemQuantitybyId(libraryItemId);
    }
}
