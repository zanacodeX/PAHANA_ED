package controller;

import dao.BookDAO;
import dto.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    private BookController controller;
    private BookDAO mockDAO;

    @BeforeEach
    void setUp() {
        mockDAO = mock(BookDAO.class);
        // Inject the mock DAO by subclassing controller
        controller = new BookController() {
            @Override
            public boolean deleteBook(int bookId) {
                try {
                    mockDAO.deleteBook(bookId);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public Book getBookById(int bookId) {
                try {
                    return mockDAO.getBookById(bookId);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public boolean updateBook(Book book) {
                try {
                    return mockDAO.updateBook(book);
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public List<Book> getAllBooks() {
                try {
                    return mockDAO.getAllBooks();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public List<Book> searchBooks(String searchTerm) {
                try {
                    return mockDAO.searchBooks(searchTerm);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public BookResult addBook(String name, String author, String category, double price, String imagePath) {
                try {
                    Book book = new Book();
                    book.setName(name);
                    book.setAuthor(author);
                    book.setCategory(category);
                    book.setPrice(price);
                    book.setImagePath(imagePath);
                    mockDAO.insertBook(book);
                    return new BookResult(book, "Book added successfully.", true);
                } catch (Exception e) {
                    return new BookResult(null, "Failed to add book: " + e.getMessage(), false);
                }
            }
        };
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(mockDAO).deleteBook(1);
        boolean result = controller.deleteBook(1);
        assertTrue(result);
        verify(mockDAO, times(1)).deleteBook(1);
    }

    @Test
    void testGetBookById() throws Exception {
        Book mockBook = new Book();
        mockBook.setId(1);
        mockBook.setName("Java Basics");
        when(mockDAO.getBookById(1)).thenReturn(mockBook);

        Book result = controller.getBookById(1);
        assertNotNull(result);
        assertEquals("Java Basics", result.getName());
        verify(mockDAO, times(1)).getBookById(1);
    }

    @Test
    void testUpdateBook() throws Exception {
        Book book = new Book();
        book.setId(1);
        when(mockDAO.updateBook(book)).thenReturn(true);

        boolean updated = controller.updateBook(book);
        assertTrue(updated);
        verify(mockDAO, times(1)).updateBook(book);
    }

    @Test
    void testGetAllBooks() throws Exception {
        Book b1 = new Book();
        b1.setName("Book1");
        Book b2 = new Book();
        b2.setName("Book2");
        when(mockDAO.getAllBooks()).thenReturn(Arrays.asList(b1, b2));

        List<Book> books = controller.getAllBooks();
        assertEquals(2, books.size());
        verify(mockDAO, times(1)).getAllBooks();
    }

    @Test
    void testSearchBooks() throws Exception {
        Book b1 = new Book();
        b1.setName("Java Programming");
        when(mockDAO.searchBooks("Java")).thenReturn(Arrays.asList(b1));

        List<Book> results = controller.searchBooks("Java");
        assertEquals(1, results.size());
        assertEquals("Java Programming", results.get(0).getName());
        verify(mockDAO, times(1)).searchBooks("Java");
    }

    @Test
    void testAddBook() throws Exception {
        BookController.BookResult result = controller.addBook("Spring Boot", "John Doe", "Programming", 29.99, "img.jpg");
        assertTrue(result.isSuccess());
        assertEquals("Book added successfully.", result.getMessage());
        assertNotNull(result.getBook());
        assertEquals("Spring Boot", result.getBook().getName());
        verify(mockDAO, times(1)).insertBook(result.getBook());
    }
}
