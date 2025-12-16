package patterns.command;

import models.Book;
import models.User;
import services.LibrarySystem;
import java.time.LocalDate;

public class ReturnCommand implements Command {

    private final LibrarySystem library;
    private final User user;
    private final Book book;
    private final LocalDate actualReturnDate;

    public ReturnCommand(LibrarySystem library, User user, Book book, 
                        LocalDate actualReturnDate) {
        this.library = library;
        this.user = user;
        this.book = book;
        this.actualReturnDate = actualReturnDate; }

    @Override
    public void execute() {
        library.returnBook(user, book, actualReturnDate); }

    @Override
    public void undo() {
        System.out.println("Undo not supported for return operation."); }
}
