package dao;

import dto.User;
import model.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    void testValidateUser_Success() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);

            // simulate result set having a row
            when(mockRs.next()).thenReturn(true);
            when(mockRs.getInt("user_id")).thenReturn(10);
            when(mockRs.getString("email")).thenReturn("test@example.com");
            when(mockRs.getString("password")).thenReturn("pass123");
            when(mockRs.getString("role")).thenReturn("Admin");

            User user = userDAO.validateUser("test@example.com", "pass123");

            assertNotNull(user);
            assertEquals(10, user.getUserId());
            assertEquals("test@example.com", user.getEmail());
            assertEquals("Admin", user.getRole());
        }
    }

    @Test
    void testValidateUser_NotFound() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);

            when(mockRs.next()).thenReturn(false);

            User user = userDAO.validateUser("notfound@example.com", "wrongpass");
            assertNull(user);
        }
    }

    @Test
    void testInsertUser_Success() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
            when(mockStmt.executeUpdate()).thenReturn(1);
            when(mockStmt.getGeneratedKeys()).thenReturn(mockRs);

            when(mockRs.next()).thenReturn(true);
            when(mockRs.getInt(1)).thenReturn(100);

            User user = new User();
            user.setEmail("newuser@example.com");
            user.setPassword("pass123");
            user.setRole("Customer");

            int generatedId = userDAO.insertUser(user);

            assertEquals(100, generatedId);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testInsertUser_NoGeneratedKey() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
            when(mockStmt.executeUpdate()).thenReturn(1);
            when(mockStmt.getGeneratedKeys()).thenReturn(mockRs);

            when(mockRs.next()).thenReturn(false);

            User user = new User();
            user.setEmail("nogenerate@example.com");
            user.setPassword("pass123");
            user.setRole("Customer");

            int generatedId = userDAO.insertUser(user);

            assertEquals(-1, generatedId);
        }
    }
}
