package patterns.decorator;

import models.Book;

public class RecommendedBook extends BookDecorator {

    public RecommendedBook(Book book) {
        super(book);
    }

    @Override
    public String getDescription() {
        return book.getDescription() + " [Recommended]";
    }
}
