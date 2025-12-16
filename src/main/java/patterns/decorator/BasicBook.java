package patterns.decorator;

import models.Book;

public class BasicBook {

    private final Book book;

    public BasicBook(Book book) {
        this.book = book;
    }

    public String getDescription() {
        return book.getDescription();
    }

    public Book getBook() {
        return book;
    }
}
