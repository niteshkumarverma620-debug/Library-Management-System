
import java.util.*;

// Book Class
class Book {
    int id;
    String name;
    String author;
    boolean available;
    String issuedTo;

    Book(int id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.available = true;
        this.issuedTo = "";
    }

    void display() {
        System.out.println("--------------------------------");
        System.out.println("Book ID   : " + id);
        System.out.println("Book Name : " + name);
        System.out.println("Author    : " + author);
        System.out.println("Status    : " +
                (available ? "Available" : "Issued"));

        if (!available) {
            System.out.println("Issued To : " + issuedTo);
        }
    }
}

// Linked List Node
class Node {
    Book book;
    Node next;

    Node(Book book) {
        this.book = book;
        this.next = null;
    }
}

// Custom Linked List
class BookLinkedList {
    Node head;

    void add(Book book) {
        Node newNode = new Node(book);

        if (head == null) {
            head = newNode;
            return;
        }

        Node temp = head;

        while (temp.next != null) {
            temp = temp.next;
        }

        temp.next = newNode;
    }

    boolean delete(int id) {
        if (head == null) {
            return false;
        }

        if (head.book.id == id) {
            head = head.next;
            return true;
        }

        Node temp = head;

        while (temp.next != null) {
            if (temp.next.book.id == id) {
                temp.next = temp.next.next;
                return true;
            }

            temp = temp.next;
        }

        return false;
    }

    void display() {
        if (head == null) {
            System.out.println("No books available!");
            return;
        }

        Node temp = head;

        while (temp != null) {
            temp.book.display();
            temp = temp.next;
        }
    }

    // Linear Search by Book Name
    Book searchByName(String name) {
        Node temp = head;

        while (temp != null) {
            if (temp.book.name.equalsIgnoreCase(name)) {
                return temp.book;
            }

            temp = temp.next;
        }

        return null;
    }

    // Bubble Sort by Book ID
    void sortById() {
        if (head == null || head.next == null) {
            return;
        }

        boolean swapped;

        do {
            swapped = false;
            Node temp = head;

            while (temp.next != null) {
                if (temp.book.id > temp.next.book.id) {
                    Book t = temp.book;
                    temp.book = temp.next.book;
                    temp.next.book = t;

                    swapped = true;
                }

                temp = temp.next;
            }

        } while (swapped);
    }

    // Bubble Sort by Book Name
    void sortByName() {
        if (head == null || head.next == null) {
            return;
        }

        boolean swapped;

        do {
            swapped = false;
            Node temp = head;

            while (temp.next != null) {
                if (temp.book.name.compareToIgnoreCase(
                        temp.next.book.name) > 0) {

                    Book t = temp.book;
                    temp.book = temp.next.book;
                    temp.next.book = t;

                    swapped = true;
                }

                temp = temp.next;
            }

        } while (swapped);
    }
}

// Issue Request Class
class IssueRequest {
    int bookId;
    String studentName;

    IssueRequest(int bookId, String studentName) {
        this.bookId = bookId;
        this.studentName = studentName;
    }
}

// Main Library Management Class
public class Library_Management {

    static Scanner sc = new Scanner(System.in);

    // Linked List
    static BookLinkedList library = new BookLinkedList();

    // HashMap for Hashing
    static HashMap<Integer, Book> bookMap = new HashMap<>();

    // Queue for Issue Requests
    static Queue<IssueRequest> issueQueue = new LinkedList<>();

    // Add Book
    static void addBook() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (bookMap.containsKey(id)) {
            System.out.println("Book ID already exists!");
            return;
        }

        System.out.print("Enter Book Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Author Name: ");
        String author = sc.nextLine();

        Book book = new Book(id, name, author);

        library.add(book);
        bookMap.put(id, book);

        System.out.println("Book added successfully!");
    }

    // Display Books
    static void displayBooks() {
        library.display();
    }

    // Delete Book
    static void deleteBook() {
        System.out.print("Enter Book ID to delete: ");
        int id = sc.nextInt();

        Book book = bookMap.get(id);

        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        if (!book.available) {
            System.out.println("Cannot delete an issued book!");
            return;
        }

        if (hasPendingRequest(id)) {
            System.out.println("Cannot delete: pending issue requests exist!");
            return;
        }

        library.delete(id);
        bookMap.remove(id);

        System.out.println("Book deleted successfully!");
    }

    // Search Book by ID using HashMap
    static void searchById() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();

        Book book = bookMap.get(id);

        if (book != null) {
            book.display();
        } else {
            System.out.println("Book not found!");
        }
    }

    // Search Book by Name using Linear Search
    static void searchByName() {
        sc.nextLine();

        System.out.print("Enter Book Name: ");
        String name = sc.nextLine();

        Book book = library.searchByName(name);

        if (book != null) {
            book.display();
        } else {
            System.out.println("Book not found!");
        }
    }

    // Check Pending Requests
    static boolean hasPendingRequest(int bookId) {
        for (IssueRequest request : issueQueue) {
            if (request.bookId == bookId) {
                return true;
            }
        }

        return false;
    }

    // Issue Book
    static void issueBook() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (!bookMap.containsKey(id)) {
            System.out.println("Book not found!");
            return;
        }

        System.out.print("Enter Student Name: ");
        String student = sc.nextLine();

        issueQueue.offer(new IssueRequest(id, student));

        System.out.println("Issue request added to queue!");

        processIssueQueue();
    }

    // Process Queue using FIFO
    static void processIssueQueue() {
        int size = issueQueue.size();

        for (int i = 0; i < size; i++) {
            IssueRequest request = issueQueue.poll();

            Book book = bookMap.get(request.bookId);

            if (book == null) {
                System.out.println("Request cancelled: Book not found!");
            } 
            else if (book.available) {
                book.available = false;
                book.issuedTo = request.studentName;

                System.out.println(
                    "Book ID " + book.id +
                    " issued successfully to " +
                    request.studentName
                );
            } 
            else {
                issueQueue.offer(request);
            }
        }
    }

    // Return Book
    static void returnBook() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();

        Book book = bookMap.get(id);

        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        if (book.available) {
            System.out.println("This book is not issued!");
            return;
        }

        book.available = true;
        book.issuedTo = "";

        System.out.println("Book returned successfully!");

        // Process Pending Requests
        processIssueQueue();
    }

    // Display Pending Requests
    static void displayQueue() {
        if (issueQueue.isEmpty()) {
            System.out.println("No pending requests!");
            return;
        }

        System.out.println("\nPending Issue Requests:");

        for (IssueRequest request : issueQueue) {
            System.out.println(
                "Book ID: " + request.bookId +
                " | Student: " + request.studentName
            );
        }
    }

    // Sort Books by ID
    static void sortById() {
        library.sortById();

        System.out.println("Books sorted by ID!");
        displayBooks();
    }

    // Sort Books by Name
    static void sortByName() {
        library.sortByName();

        System.out.println("Books sorted by Name!");
        displayBooks();
    }

    // Main Method
    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("    LIBRARY MANAGEMENT SYSTEM");
            System.out.println("================================");

            System.out.println("1. Add Book");
            System.out.println("2. Display All Books");
            System.out.println("3. Delete Book");
            System.out.println("4. Search Book By ID");
            System.out.println("5. Search Book By Name");
            System.out.println("6. Issue Book");
            System.out.println("7. Return Book");
            System.out.println("8. Sort Books By ID");
            System.out.println("9. Sort Books By Name");
            System.out.println("10. Display Pending Requests");
            System.out.println("0. Exit");

            System.out.print("\nEnter Your Choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    addBook();
                    break;

                case 2:
                    displayBooks();
                    break;

                case 3:
                    deleteBook();
                    break;

                case 4:
                    searchById();
                    break;

                case 5:
                    searchByName();
                    break;

                case 6:
                    issueBook();
                    break;

                case 7:
                    returnBook();
                    break;

                case 8:
                    sortById();
                    break;

                case 9:
                    sortByName();
                    break;

                case 10:
                    displayQueue();
                    break;

                case 0:
                    System.out.println(
                        "Thank you for using Library Management System!"
                    );
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 0);

        sc.close();
    }
}
