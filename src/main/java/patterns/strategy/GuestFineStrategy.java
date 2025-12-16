package patterns.strategy;

import models.BorrowedBook;

public class GuestFineStrategy implements FineStrategy {
    private static final double FINE_PER_DAY = 100;

    @Override
    public double calculateFine(BorrowedBook borrowedBook) {
        long overdueDays = borrowedBook.getOverdueDays();
        return overdueDays * FINE_PER_DAY;
    }
}
