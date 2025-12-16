package patterns.decorator;

import models.Book;

public class SpecialEditionBook extends BookDecorator {

    public SpecialEditionBook(Book book) {
        super(book);
    }

    @Override
    public String getDescription() {
        return book.getDescription() + " [Special Edition]";
    }
}
