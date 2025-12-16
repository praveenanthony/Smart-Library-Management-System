package patterns.command;
import models.Book;
import models.User;
import services.LibrarySystem;
import java.time.LocalDate;

public class BorrowCommand implements Command {

    private final LibrarySystem library;
    private final User user;
    private final Book book;
    private final LocalDate borrowDate;

    public BorrowCommand(LibrarySystem library, User user, 
                        Book book, LocalDate borrowDate) {
        this.library = library;
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate; }

    @Override
    public void execute() {
        library.borrowBook(user, book, borrowDate); }

    @Override
    public void undo() {
        library.returnBook(user, book, borrowDate);
        System.out.println("Undo successful: Borrow reverted / book returned."); }
}
