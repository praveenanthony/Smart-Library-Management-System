package services;

import models.*;
import patterns.decorator.*;
import patterns.state.*;
import patterns.observer.Observer;
import patterns.observer.Subject;

import java.time.LocalDate;
import java.util.*;

public class LibrarySystem implements Subject {

    private final List<Book> books = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final Map<String, Queue<Reservation>> reservationQueueByBookId = new HashMap<>();
    private final List<Observer> observers = new ArrayList<>();
    private final ReportGenerator reportGenerator = new ReportGenerator();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        observers.forEach(o -> o.update(message));
    }

    //BOOK
    public void addBook(Book book) {
        boolean exists = books.stream()
                .anyMatch(b -> b.getBookId().equals(book.getBookId()));

        if (exists) {
            System.out.println("Book ID already exists. Enter a unique ID.");
            return;
        }

        book.setState(new AvailableState(book));
        books.add(book);
        System.out.println("Book added successfully: " + book);
    }

    public void updateBook(String bookId, String title, String author,
                           String category, String isbn) {

        for (int i = 0; i < books.size(); i++) {
            Book old = books.get(i);

            if (old.getBookId().equals(bookId)) {
                Book updated = new Book(bookId, title, author, category, isbn);
                updated.setState(old.getState());
                updated.getBorrowedHistory().addAll(old.getBorrowedHistory());
                books.set(i, updated);
                System.out.println("Book updated successfully: " + updated);
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void removeBook(String bookId) {
        Book book = findBookById(bookId);
        if (book != null) {
            if (book.getState() instanceof BorrowedState) {
                System.out.println("Cannot remove book. It is currently borrowed.");
                return;
            }

            Queue<Reservation> queue = reservationQueueByBookId.get(bookId);
            if (queue != null && !queue.isEmpty()) {
                System.out.println("Cannot remove book. There are active reservations.");
                return;
            }

            books.remove(book);
            reservationQueueByBookId.remove(bookId);
            System.out.println("Book removed successfully: " + bookId);
        } else {
            System.out.println("Book not found.");
        }
    }

    public Book findBookById(String bookId) {
        return books.stream()
                .filter(b -> b.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    public void listBooks() {
        books.forEach(System.out::println);
    }

    //USER
    public void addUser(User user) {
        if (findUserById(user.getUserId()) != null) {
            System.out.println("User ID already exists. Cannot add.");
            return;
        }
        users.add(user);
        addObserver(user);
        System.out.println("User added successfully: " + user);
    }

    public User findUserById(String userId) {
        return users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void listUsers() {
    users.forEach(System.out::println);
    }

    public void borrowBook(User user, Book book, LocalDate borrowDate) {
        if (!(book.getState() instanceof AvailableState)) {
            System.out.println("Book is not available.");
            return;
        }
        long activeBorrows = user.getBorrowedBooksHistory().stream()
                .filter(b -> !b.isReturned()).count();

        if (activeBorrows >= user.getBorrowLimit()) {
            System.out.println("Borrow limit exceeded.");
            return;
        }
        LocalDate dueDate = borrowDate.plusDays(user.getBorrowDays());
        BorrowedBook borrowedBook = new BorrowedBook(book, user, borrowDate, dueDate);

        user.getBorrowedBooksHistory().add(borrowedBook);
        book.getBorrowedHistory().add(user.getUserId());
        book.setState(new BorrowedState(book));

        notifyObservers("Book '" + book.getTitle() + "' borrowed by " + user.getName() +
                ". Due date: " + dueDate);
    }

    public void returnBook(User user, Book book, LocalDate actualReturnDate) {
        BorrowedBook borrowed = user.getBorrowedBooksHistory().stream()
                .filter(b -> b.getBook().equals(book) && !b.isReturned())
                .findFirst().orElse(null);

        if (borrowed == null) {
            System.out.println("User did not borrow this book.");
            return;
        }

        borrowed.setActualReturnDate(actualReturnDate);
        borrowed.setReturned(true);
        double fine = user.calculateFine(borrowed);

        System.out.println("Borrowed Date: " + borrowed.getBorrowDate());
        System.out.println("Due Date     : " + borrowed.getDueDate());
        System.out.println("Returned Date: " + actualReturnDate);

        if (fine > 0) {
            System.out.println("Late Fine: LKR " + (long) fine);
            user.update("Late fine charged: " + (long) fine);
        } else {
            System.out.println("Returned on time. No fine.");
        }

        // Handle reservation queue
        Queue<Reservation> queue = reservationQueueByBookId.get(book.getBookId());
        if (queue != null && !queue.isEmpty()) {
            Reservation next = queue.poll();
            book.setState(new ReservedState(book));
            notifyObservers("Book '" + book.getTitle() + "' available for reservation pickup by " + next.getUser().getName());
        } else {
            book.setState(new AvailableState(book));
        }
    }

    public void reserveBook(User user, Book book) {
        boolean alreadyReserved = reservations.stream()
                .anyMatch(r -> r.getBook().equals(book) && r.getUser().equals(user));

        if (alreadyReserved) {
            System.out.println("You have already reserved this book.");
            return;
        }

        String reservationId = "R" + (reservations.size() + 1);
        Reservation reservation = new Reservation(reservationId, book, user);

        reservations.add(reservation);
        reservationQueueByBookId.computeIfAbsent(book.getBookId(), k -> new LinkedList<>()).add(reservation);

        if (!(book.getState() instanceof BorrowedState)) {
            book.setState(new ReservedState(book));
        }

        notifyObservers(user.getName() + " reserved book '" + book.getTitle() + "'");
    }

    public void cancelReservation(User user, Book book) {
        Reservation res = reservations.stream()
                .filter(r -> r.getUser().equals(user) && r.getBook().equals(book))
                .findFirst()
                .orElse(null);

        if (res == null) {
            System.out.println("No reservation found.");
            return;
        }

        reservations.remove(res);
        Queue<Reservation> queue = reservationQueueByBookId.get(book.getBookId());
        if (queue != null) {
            queue.remove(res);
            if (queue.isEmpty() && book.getState() instanceof ReservedState) {
                book.setState(new AvailableState(book));
            }
        }

        System.out.println("Reservation cancelled successfully.");
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    public void generateReports() {
        reportGenerator.generateMostBorrowedBooks(books);
        reportGenerator.generateActiveBorrowers(users);
        reportGenerator.generateOverdueBooks(users);
    }

    public Book decorateBook(String bookId, boolean featured, boolean recommended, boolean specialEdition) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return null;
        }

        if (featured) book = new FeaturedBook(book);
        if (recommended) book = new RecommendedBook(book);
        if (specialEdition) book = new SpecialEditionBook(book);

        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId().equals(bookId)) {
                books.set(i, book);
                break;
            }
        }

        System.out.println("Book updated with features: " + book.getDescription());
        return book;
    }

    public void showReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations.");
            return;
        }
        reservations.forEach(System.out::println);
    }
}
