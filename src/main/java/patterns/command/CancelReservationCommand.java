package patterns.command;

import models.Book;
import models.User;
import services.LibrarySystem;

public class CancelReservationCommand implements Command {
    private final LibrarySystem library;
    private final User user;
    private final Book book;

    public CancelReservationCommand(LibrarySystem library, 
                                    User user, Book book) {
        this.library = library;
        this.user = user;
        this.book = book;
    }

    @Override
    public void execute() {
        library.cancelReservation(user, book);
    }

    @Override
    public void undo() {
        library.reserveBook(user, book);
        System.out.println("Undo: Cancel reservation reverted.");
    }
}
