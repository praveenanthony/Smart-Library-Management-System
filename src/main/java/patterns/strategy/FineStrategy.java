package patterns.strategy;

import models.BorrowedBook;

public interface FineStrategy {
    double calculateFine(BorrowedBook borrowedBook);
}
