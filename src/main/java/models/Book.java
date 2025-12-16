package models;

import patterns.state.BookState;
import patterns.state.AvailableState;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Book {
    private final String bookId;
    private String title;
    private String author;
    private String category;
    private String ISBN;
    private BookState state;
    private List<String> borrowedHistory;

    public Book(String bookId, String title, String author,
                String category, String ISBN) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.ISBN = ISBN;
        this.borrowedHistory = new ArrayList<>();
        this.state = new AvailableState(this);
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getISBN() { return ISBN; }
    public BookState getState() { return state; }
    public List<String> getBorrowedHistory() { return borrowedHistory; }

    public void setState(BookState state) {
        this.state = state;
        if (this.state != null) {
            this.state.handle(); // triggers action when state changes
        }
    }

    public void addBorrowHistory(String userId) {
        borrowedHistory.add(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book b = (Book) o;
        return Objects.equals(bookId, b.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }

    @Override
    public String toString() {
        return getDescription() + " | Status: " + (state != null ? state.getStateName() : "Unknown") +
               " | Borrowed History: " + (borrowedHistory.isEmpty() ? "None" : String.join(", ", borrowedHistory));
    }

    public String getDescription() {
        return String.format("Book ID: %s | Title: %s | Author: %s | Category: %s | ISBN: %s",
                bookId, title, author, category, ISBN);
    }
}
