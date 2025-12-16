package patterns.command;

import models.Book;
import models.User;
import services.LibrarySystem;

public class ReserveCommand implements Command {
    private final LibrarySystem library;
    private final User user;
    private final Book book;

    public ReserveCommand(LibrarySystem library, 
                            User user, Book book) {
        this.library = library;
        this.user = user;
        this.book = book;
    }

    @Override
    public void execute() {
        library.reserveBook(user, book);
    }

    @Override
    public void undo() {
        library.cancelReservation(user, book);
        System.out.println("Undo: Reservation cancelled.");
    }
}
