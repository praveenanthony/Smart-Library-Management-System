package models;

import patterns.strategy.FineStrategy;
import patterns.observer.Observer;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

public abstract class User implements Observer {
    protected String userId;
    protected String name;
    protected String email;
    protected String contactNumber;
    protected List<BorrowedBook> borrowedBooksHistory;
    protected FineStrategy fineStrategy;

    public User(String userId, String name, String email, 
                String contactNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.borrowedBooksHistory = new ArrayList<>();
    }

    @Override
    public void update(String message) {
        System.out.println("Notification for " + name + ": " + message);
    }

    public abstract int getBorrowLimit();
    public abstract int getBorrowDays();

    public double calculateFine(BorrowedBook borrowedBook) {
        if (fineStrategy != null)
            return fineStrategy.calculateFine(borrowedBook);
        return 0;
    }

    public long getOverdueDays(BorrowedBook borrowedBook) {
        long overdue = ChronoUnit.DAYS.between(borrowedBook.getDueDate(), LocalDate.now());
        return overdue > 0 ? overdue : 0;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
    public List<BorrowedBook> getBorrowedBooksHistory() { return borrowedBooksHistory; }
    public FineStrategy getFineStrategy() { return fineStrategy; }

    public void setFineStrategy(FineStrategy fineStrategy) {
        this.fineStrategy = fineStrategy;
    }
    
    @Override
    public String toString() {
        return String.format(
                "User ID: %s | Name: %s | Type: %s | Email: %s | Contact: %s",
                userId, name, this.getClass().getSimpleName(), email, contactNumber,borrowedBooksHistory.size()
        );
    }
}