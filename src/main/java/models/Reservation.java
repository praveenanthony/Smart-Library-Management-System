package models;

import java.time.LocalDate;

public class Reservation {
    private final String reservationId;
    private final Book book;
    private final User user;
    private final LocalDate reservationDate;

    public Reservation(String reservationId, Book book, User user) {
        this.reservationId = reservationId;
        this.book = book;
        this.user = user;
        this.reservationDate = LocalDate.now();
    }

    public String getReservationId() { return reservationId; }
    public Book getBook() { return book; }
    public User getUser() { return user; }
    public LocalDate getReservationDate() { return reservationDate; }

    @Override
    public String toString() {
        return String.format("Reservation %s: %s[%s] reserved by %s[%s] on %s",
                reservationId, book.getTitle(), book.getBookId(), user.getName(), user.getUserId(),
                reservationDate);
    }
}