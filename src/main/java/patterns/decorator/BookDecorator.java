package patterns.decorator;

import models.Book;

public abstract class BookDecorator extends Book {
    protected Book book;

    public BookDecorator(Book book) {
        super(book.getBookId(), book.getTitle(), book.getAuthor(),
              book.getCategory(), book.getISBN());
        this.book = book;
        this.setState(book.getState());
        this.getBorrowedHistory().addAll(book.getBorrowedHistory());
    }

    @Override
    public String getDescription() {
        return book.getDescription(); //wrapped book
    }

    @Override
    public String toString() {
        return getDescription() + " | Status: " + (getState() != null 
                ? getState().getStateName() : "Unknown") +
               " | Borrowed History: " + (getBorrowedHistory().isEmpty() 
                ? "None" : String.join(", ", getBorrowedHistory()));
    }
}
