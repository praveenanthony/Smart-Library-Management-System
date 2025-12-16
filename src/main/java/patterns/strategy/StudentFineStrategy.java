package patterns.strategy;

import models.BorrowedBook;

public class StudentFineStrategy implements FineStrategy {
    private static final double FINE_PER_DAY = 50;
    
    @Override
    public double calculateFine(BorrowedBook borrowedBook) {
        long overdueDays = borrowedBook.getOverdueDays();
        return overdueDays * FINE_PER_DAY;
    }
}
