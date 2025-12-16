package patterns.state;

import models.Book;

public class ReservedState implements BookState {
    
    private final Book book;
    public ReservedState(Book book) { this.book = book;
    }

    @Override
    public void handle() {
        if (book != null)
            System.out.println("State changed: '" + 
                                book.getTitle() + "' is now RESERVED.");
    }

    @Override
    public String getStateName() { return "Reserved";
    }
}
