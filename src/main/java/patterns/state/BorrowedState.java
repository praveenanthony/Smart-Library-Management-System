package patterns.state;

import models.Book;

public class BorrowedState implements BookState {

    private final Book book;
    public BorrowedState(Book book) { this.book = book;
    }

    @Override
    public void handle() {
        if (book != null)
            System.out.println("State changed: '" + 
                                book.getTitle() + "' is now BORROWED.");
    }

    @Override
    public String getStateName() { return "Borrowed";
    }
}
