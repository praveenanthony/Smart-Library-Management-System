import models.*;
import patterns.builder.BookBuilder;
import patterns.command.*;
import services.LibrarySystem;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static final LibrarySystem library = new LibrarySystem();
    private static final Invoker invoker = new Invoker();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedSampleData();
        showMenu();
    }

    private static void seedSampleData() {

        User u1 = new Student("U01", "Alice", "alice@example.com", "0771111111");
        User u2 = new Faculty("U02", "Bob", "bob@example.com", "0772222222");
        User u3 = new Guest("U03", "Charlie", "charlie@example.com", "0773333333");

        library.addUser(u1);
        library.addUser(u2);
        library.addUser(u3);

        System.out.println();  

        Book b1 = new BookBuilder()
                .setBookId("B01")
                .setTitle("Java Programming")
                .setAuthor("Sasini")
                .setCategory("Programming")
                .setISBN("978-1111")
                .build();

        Book b2 = new BookBuilder()
                .setBookId("B02")
                .setTitle("Data Structures")
                .setAuthor("Francis")
                .setCategory("CS")
                .setISBN("978-2222")
                .build();

        Book b3 = new BookBuilder()
                .setBookId("B03")
                .setTitle("Design Patterns")
                .setAuthor("Gamma")
                .setCategory("SE")
                .setISBN("978-3333")
                .build();

        library.addBook(b1);
        library.addBook(b2);
        library.addBook(b3);
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\n=== Smart Library Management System ===");
            System.out.println("1. List Books");
            System.out.println("2. Add Book");
            System.out.println("3. Update Book");
            System.out.println("4. Remove Book");
            System.out.println("5. List Users");
            System.out.println("6. Add User");
            System.out.println("7. Borrow Book");
            System.out.println("8. Return Book");
            System.out.println("9. Reserve Book");
            System.out.println("10. Cancel Reservation");
            System.out.println("11. Show Reservations");
            System.out.println("12. Generate Reports");
            System.out.println("13. Undo Last Command");
            System.out.println("14. Redo Last Command");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> library.listBooks();
                case "2" -> addBook();
                case "3" -> updateBook();
                case "4" -> removeBook();
                case "5" -> library.listUsers();
                case "6" -> addUser();
                case "7" -> borrowBook();
                case "8" -> returnBook();
                case "9" -> reserveBook();
                case "10" -> cancelReservation();
                case "11" -> library.showReservations();
                case "12" -> library.generateReports();
                case "13" -> invoker.undo();
                case "14" -> invoker.redo();
                case "0" -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void borrowBook() {
        User u = getUserInput();
        Book b = getBookInput();
        if (u != null && b != null) {
            LocalDate borrowDate = LocalDate.now();
            Command cmd = new BorrowCommand(library, u, b, borrowDate);
            invoker.executeCommand(cmd);
        }
    }

    private static void returnBook() {
        User u = getUserInput();
        Book b = getBookInput();

        if (u != null && b != null) {

            BorrowedBook borrowed = u.getBorrowedBooksHistory().stream()
                    .filter(bb -> bb.getBook().equals(b) && !bb.isReturned())
                    .findFirst()
                    .orElse(null);

            if (borrowed == null) {
                System.out.println("This book is not borrowed by the user.");
                return;
            }

            System.out.println("Borrowed Date: " + borrowed.getBorrowDate());
            System.out.print("Enter Actual Return Date (YYYY-MM-DD): ");

            try {
                LocalDate actualReturnDate = LocalDate.parse(scanner.nextLine().trim());
                Command cmd = new ReturnCommand(library, u, b, actualReturnDate);
                invoker.executeCommand(cmd);
            } catch (Exception e) {
                System.out.println("Invalid date format.");
            }
        }
    }

    private static void reserveBook() {
        User u = getUserInput();
        Book b = getBookInput();
        if (u != null && b != null) {
            Command cmd = new ReserveCommand(library, u, b);
            invoker.executeCommand(cmd);
        }
    }

    private static void cancelReservation() {
        User u = getUserInput();
        Book b = getBookInput();
        if (u != null && b != null) {
            Command cmd = new CancelReservationCommand(library, u, b);
            invoker.executeCommand(cmd);
        }
    }

    private static void addBook() {
        System.out.print("Book ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Category: ");
        String category = scanner.nextLine().trim();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();
        
        if (id.isEmpty() || title.isEmpty() || author.isEmpty() || category.isEmpty() || isbn.isEmpty()) {
        System.out.println("All fields are required. Book not added.");
        return;
        }
        
        Book b = new BookBuilder()
                .setBookId(id)
                .setTitle(title)
                .setAuthor(author)
                .setCategory(category)
                .setISBN(isbn)
                .build();

        library.addBook(b);
    }

    private static void updateBook() {
        System.out.print("Book ID to update: ");
        String id = scanner.nextLine().trim();

        Book book = library.findBookById(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        // Update basic details
        System.out.print("New Title (leave blank to keep current): ");
        String title = scanner.nextLine().trim();
        System.out.print("New Author (leave blank to keep current): ");
        String author = scanner.nextLine().trim();
        System.out.print("New Category (leave blank to keep current): ");
        String category = scanner.nextLine().trim();
        System.out.print("New ISBN (leave blank to keep current): ");
        String isbn = scanner.nextLine().trim();

        library.updateBook(id,
                title.isEmpty() ? book.getTitle() : title,
                author.isEmpty() ? book.getAuthor() : author,
                category.isEmpty() ? book.getCategory() : category,
                isbn.isEmpty() ? book.getISBN() : isbn
        );

        System.out.print("Add Featured? (y/n): ");
        boolean featured = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Add Recommended? (y/n): ");
        boolean recommended = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Add Special Edition? (y/n): ");
        boolean specialEdition = scanner.nextLine().trim().equalsIgnoreCase("y");

        //decorators
        library.decorateBook(id, featured, recommended, specialEdition);
    }

    private static void removeBook() {
        System.out.print("Book ID: ");
        String id = scanner.nextLine().trim();
        library.removeBook(id);
    }

    private static void addUser() {
        System.out.print("User ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Contact: ");
        String contact = scanner.nextLine().trim();
        System.out.print("Membership (Student/Faculty/Guest): ");
        String type = scanner.nextLine().trim().toLowerCase();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || contact.isEmpty() || type.isEmpty()) {
        System.out.println("All fields are required. User not added.");
        return;
        }
        
        User u = switch (type) {
            case "student" -> new Student(id, name, email, contact);
            case "faculty" -> new Faculty(id, name, email, contact);
            default -> new Guest(id, name, email, contact);
        };

        library.addUser(u);
    }

    private static User getUserInput() {
        System.out.print("User ID: ");
        String uid = scanner.nextLine().trim();
        User u = library.findUserById(uid);
        if (u == null) System.out.println("User not found.");
        return u;
    }

    private static Book getBookInput() {
        System.out.print("Book ID: ");
        String bid = scanner.nextLine().trim();
        Book b = library.findBookById(bid);
        if (b == null) System.out.println("Book not found.");
        return b;
    }
}
