package dao;

import dto.Book;
import model.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookDAOTest {

    private BookDAO dao;

    @BeforeEach
    void setUp() {
        dao = new BookDAO();
    }

    @Test
    void testGetAllBooks() throws Exception {
        // Mock JDBC objects
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Mock static DBConnection.getConnection()
        try (MockedStatic<DBConnection> mockedDB = mockStatic(DBConnection.class)) {
            mockedDB.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            // Mock ResultSet
            when(mockResultSet.next()).thenReturn(true, true, false); // two books
            when(mockResultSet.getInt("id")).thenReturn(1, 2);
            when(mockResultSet.getString("name")).thenReturn("Book One", "Book Two");
            when(mockResultSet.getString("author")).thenReturn("Author A", "Author B");
            when(mockResultSet.getString("category")).thenReturn("Fiction", "Non-Fiction");
            when(mockResultSet.getDouble("price")).thenReturn(10.0, 20.0);
            when(mockResultSet.getString("image_path")).thenReturn("img1.jpg", "img2.jpg");

            // Call method
            List<Book> books = dao.getAllBooks();

            // Assertions
            assertEquals(2, books.size());
            assertEquals("Book One", books.get(0).getName());
            assertEquals("Book Two", books.get(1).getName());

            // Verify JDBC calls
            verify(mockConnection).prepareStatement(anyString());
            verify(mockStatement).executeQuery();
            verify(mockResultSet, times(3)).next();
        }
    }

    @Test
    void testGetBookByID() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        try (MockedStatic<DBConnection> mockedDB = mockStatic(DBConnection.class)) {
            mockedDB.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getString("name")).thenReturn("Book One");
            when(mockResultSet.getString("author")).thenReturn("Author A");
            when(mockResultSet.getString("category")).thenReturn("Fiction");
            when(mockResultSet.getDouble("price")).thenReturn(15.0);
            when(mockResultSet.getString("image_path")).thenReturn("img1.jpg");

            Book book = dao.getBookByID(1);

            assertNotNull(book);
            assertEquals(1, book.getId());
            assertEquals("Book One", book.getName());
        }
    }

    @Test
    void testInsertBook() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        try (MockedStatic<DBConnection> mockedDB = mockStatic(DBConnection.class)) {
            mockedDB.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

            Book book = new Book(0, "Book X", "Author X", "Sci-Fi", 25.0, "imgX.jpg");
            dao.insertBook(book);

            verify(mockConnection).prepareStatement(anyString());
            verify(mockStatement).setString(1, "Book X");
            verify(mockStatement).setString(2, "Author X");
            verify(mockStatement).setString(3, "Sci-Fi");
            verify(mockStatement).setDouble(4, 25.0);
            verify(mockStatement).setString(5, "imgX.jpg");
            verify(mockStatement).executeUpdate();
        }
    }
}
