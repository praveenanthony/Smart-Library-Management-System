package patterns.decorator;

import models.Book;

public class FeaturedBook extends BookDecorator {

    public FeaturedBook(Book book) {
        super(book);
    }

    @Override
    public String getDescription() {
        return book.getDescription() + " [Featured]";
    }
}
