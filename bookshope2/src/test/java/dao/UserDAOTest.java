package dao;

import dto.User;
import model.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    public void setup() {
        userDAO = new UserDAO();
    }

    @Test
    public void testValidateUser_ReturnsUser() throws Exception {
        // Mock ResultSet
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("user_id")).thenReturn(1);
        when(rs.getString("email")).thenReturn("test@example.com");
        when(rs.getString("password")).thenReturn("pass123");
        when(rs.getString("role")).thenReturn("admin");

        // Mock PreparedStatement
        PreparedStatement ps = mock(PreparedStatement.class);
        when(ps.executeQuery()).thenReturn(rs);

        // Mock Connection
        Connection con = mock(Connection.class);
        when(con.prepareStatement(anyString())).thenReturn(ps);

        // Mock static DBConnection.getConnection()
        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(con);

            User user = userDAO.validateUser("test@example.com", "pass123");

            assertNotNull(user);
            assertEquals(1, user.getUserId());
            assertEquals("admin", user.getRole());
        }
    }

    @Test
    public void testValidateUser_ReturnsNull() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(false);

        PreparedStatement ps = mock(PreparedStatement.class);
        when(ps.executeQuery()).thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(anyString())).thenReturn(ps);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(con);

            User user = userDAO.validateUser("wrong@example.com", "wrongpass");

            assertNull(user);
        }
    }
}
