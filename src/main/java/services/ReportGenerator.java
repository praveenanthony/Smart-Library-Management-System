package services;

import models.Book;
import models.BorrowedBook;
import models.User;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class ReportGenerator {

    public void generateMostBorrowedBooks(List<Book> books) {
        System.out.println("Most Borrowed Books (top 5):");
        books.stream()
                .sorted(Comparator.comparingInt(b -> -b.getBorrowedHistory().size()))
                .limit(5)
                .forEach(b -> System.out.println(b.getTitle() + " - borrowed " + 
                         b.getBorrowedHistory().size() + " times"));
        System.out.println();
    }

    public void generateActiveBorrowers(List<User> users) {
    System.out.println("Active Borrowers: ");
    users.stream()
            .filter(u -> u.getBorrowedBooksHistory().stream()
                    .anyMatch(bb -> !bb.isReturned()))
            .forEach(u -> {
                long count = u.getBorrowedBooksHistory().stream()
                        .filter(bb -> !bb.isReturned()).count();                
                String membershipType = u.getClass().getSimpleName();

                System.out.println(u.getName() + " (" + membershipType + ") - currently borrowed: " + count);
            });
    System.out.println();
    }


    public void generateOverdueBooks(List<User> users) {
    System.out.println("Overdue Books:");

    boolean found = false;

    for (User u : users) {
        for (BorrowedBook bb : u.getBorrowedBooksHistory()) {

            if (!bb.isReturned() && bb.getDueDate().isBefore(LocalDate.now())) {
                found = true;

                long overdueDays =
                        java.time.temporal.ChronoUnit.DAYS.between(
                                bb.getDueDate(), LocalDate.now());

                System.out.println(
                        "Book     : " + bb.getBook().getTitle() +
                        " [" + bb.getBook().getBookId() + "]\n" +
                        "Borrower : " + u.getName() +
                        " [" + u.getUserId() + "]\n" +
                        "Due Date : " + bb.getDueDate() + "\n" +
                        "Overdue  : " + overdueDays + " days\n"
                );
            }
        }
    }

    if (!found) {
        System.out.println("No overdue books found.");
    }

    System.out.println();
    }
}