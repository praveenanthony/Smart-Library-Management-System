package patterns.state;

import models.Book;

public class AvailableState implements BookState {

    private final Book book;
    public AvailableState(Book book) { this.book = book;
    }

    @Override
    public void handle() {
        if (book != null)
            System.out.println("State changed: '" + 
                                book.getTitle() + "' is now AVAILABLE.");
    }

    @Override
    public String getStateName() { return "Available";
    }
}
