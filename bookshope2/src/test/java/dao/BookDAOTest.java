package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;

import dto.Book;

public class BookDAOTest {

    private static BookDAO bookDAO;

    @BeforeAll
    public static void setupclass() {
        bookDAO = new BookDAO();
    }

    @Test
    public void testAddAndGetBookById() throws Exception {
        Book newBook = new Book(0, "JUnit in Action", "Sam Brannen", "Testing", 29.99, "image.jpg");
        bookDAO.insertBook(newBook);

        List<Book> books = bookDAO.searchBooks("JUnit in Action");
        assertFalse(books.isEmpty(), "Book should be found after insertion");

        Book fetchedBook = bookDAO.getBookById(books.get(0).getId());
        assertNotNull(fetchedBook, "Fetched book should not be null");
        assertEquals("JUnit in Action", fetchedBook.getName());
    }

    @Test
    public void testGetAllBooks() throws Exception {
        List<Book> books = bookDAO.getAllBooks();
        assertNotNull(books);
        assertTrue(books.size() >= 0); // At least empty list
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book newBook = new Book(0, "Temp Book", "Author A", "Category A", 10.0, "temp.jpg");
        bookDAO.insertBook(newBook);

        List<Book> books = bookDAO.searchBooks("Temp Book");
        assertFalse(books.isEmpty());

        Book bookToUpdate = books.get(0);
        bookToUpdate.setName("Updated Book Name");

        boolean updated = bookDAO.updateBook(bookToUpdate);
        assertTrue(updated);

        Book updatedBook = bookDAO.getBookById(bookToUpdate.getId());
        assertEquals("Updated Book Name", updatedBook.getName());
    }

    @Test
    public void testDeleteBook() throws Exception {
        Book book = new Book(0, "To Delete", "Author B", "Category B", 15.0, "delete.jpg");
        bookDAO.insertBook(book);

        List<Book> books = bookDAO.searchBooks("To Delete");
        assertFalse(books.isEmpty());

        int idToDelete = books.get(0).getId();
        bookDAO.deleteBook(idToDelete);

        Book deletedBook = bookDAO.getBookById(idToDelete);
        assertNull(deletedBook);
    }

    @Test
    public void testGetAllCategories() throws Exception {
        List<String> categories = bookDAO.getAllCategories();
        assertNotNull(categories);
    }
}
