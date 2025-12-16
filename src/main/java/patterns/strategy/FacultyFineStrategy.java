package patterns.strategy;

import models.BorrowedBook;

public class FacultyFineStrategy implements FineStrategy {
    private static final double FINE_PER_DAY = 20;

    @Override
    public double calculateFine(BorrowedBook borrowedBook) {
        long overdueDays = borrowedBook.getOverdueDays();
        return overdueDays * FINE_PER_DAY;
    }
}
