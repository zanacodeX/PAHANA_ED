package dao;

import dto.AdminSummary;
import model.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminDashboardDAOTest {

    private AdminDashboardDAO dao;

    @BeforeEach
    void setUp() {
        dao = new AdminDashboardDAO();
    }

    @Test
    void testGetDashboardSummary() throws Exception {
        // Mock the JDBC objects
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Mock the static DBConnection.getConnection() method
        try (MockedStatic<DBConnection> mockedDB = mockStatic(DBConnection.class)) {
            mockedDB.when(DBConnection::getConnection).thenReturn(mockConnection);

            // Mock connection.prepareStatement()
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            // Mock the ResultSet returned values
            when(mockResultSet.next()).thenReturn(true); // first call returns true
            when(mockResultSet.getDouble("totalEarnings")).thenReturn(1000.50);
            when(mockResultSet.getInt("pendingOrders")).thenReturn(5);
            when(mockResultSet.getInt("confirmedOrders")).thenReturn(8);
            when(mockResultSet.getInt("completedOrders")).thenReturn(10);
            when(mockResultSet.getInt("declinedOrders")).thenReturn(2);
            when(mockResultSet.getInt("totalCustomers")).thenReturn(50);

            // Call the method
            AdminSummary summary = dao.getDashboardSummary();

            // Verify results
            assertEquals(1000.50, summary.getTotalEarnings());
            assertEquals(5, summary.getPendingOrders());
            assertEquals(8, summary.getConfirmedOrders());
            assertEquals(10, summary.getCompletedOrders());
            assertEquals(2, summary.getDeclinedOrders());
            assertEquals(50, summary.getTotalCustomers());

            // Verify that JDBC methods were called
            verify(mockConnection).prepareStatement(anyString());
            verify(mockStatement).executeQuery();
            verify(mockResultSet).next();
        }
    }
}
