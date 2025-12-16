package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowedBook {
    private final Book book;
    private final User user;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate actualReturnDate;
    private boolean returned;

    public BorrowedBook(Book book, User user, LocalDate borrowDate, LocalDate dueDate) {
        this.book = book;
        this.user = user;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returned = false;
        this.actualReturnDate = null;
    }

    public long getOverdueDays() {
        if (actualReturnDate == null) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(dueDate, actualReturnDate);
        return Math.max(0, days);
    }

    public Book getBook() { return book; }
    public User getUser() { return user; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    
    public boolean isReturned() { return returned; }
    
    public void setReturned(boolean returned) {
        this.returned = returned;
    }
    
    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
        this.returned = true;
    }
}