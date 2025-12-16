package models;

import services.LibrarySystem;

public class Librarian {

    private final String librarianId;
    private final String name;
    private final String email;

    public Librarian(String librarianId, String name, String email) {
        this.librarianId = librarianId;
        this.name = name;
        this.email = email;
    }

    public void addBook(LibrarySystem system, Book book) {
        system.addBook(book);
    }
    public void updateBook(LibrarySystem system, Book book) {
        system.updateBook(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getISBN()
        );
    }
    public void removeBook(LibrarySystem system, String bookId) {
        system.removeBook(bookId);
    }
    public void generateReports(LibrarySystem system) {
        system.generateReports();
    }

    public String getLibrarianId() { return librarianId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Librarian{" +
                "ID='" + librarianId + '\'' +
                ", Name='" + name + '\'' +
                ", Email='" + email + '\'' +
                '}';
    }
}