package dao;

import dto.Customer;
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

class CustomerDAOTest {

    private CustomerDAO dao;

    @BeforeEach
    void setUp() {
        dao = new CustomerDAO();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            // Simulate 2 customers
            when(mockResultSet.next()).thenReturn(true, true, false);
            when(mockResultSet.getInt("customer_id")).thenReturn(1, 2);
            when(mockResultSet.getInt("user_id")).thenReturn(101, 102);
            when(mockResultSet.getString("account_number")).thenReturn("ACC001", "ACC002");
            when(mockResultSet.getString("name")).thenReturn("Alice", "Bob");
            when(mockResultSet.getString("address")).thenReturn("Street A", "Street B");
            when(mockResultSet.getString("phone")).thenReturn("111111", "222222");
            when(mockResultSet.getInt("units_consumed")).thenReturn(10, 20);

            List<Customer> customers = dao.getAllCustomers();

            assertEquals(2, customers.size());
            assertEquals("Alice", customers.get(0).getName());
            assertEquals("Bob", customers.get(1).getName());
        }
    }

    @Test
    void testGetCustomerById() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("customer_id")).thenReturn(1);
            when(mockResultSet.getInt("user_id")).thenReturn(101);
            when(mockResultSet.getString("account_number")).thenReturn("ACC001");
            when(mockResultSet.getString("name")).thenReturn("Alice");
            when(mockResultSet.getString("address")).thenReturn("Street A");
            when(mockResultSet.getString("phone")).thenReturn("111111");
            when(mockResultSet.getInt("units_consumed")).thenReturn(10);

            Customer customer = dao.getCustomerById(1);

            assertNotNull(customer);
            assertEquals("Alice", customer.getName());
            assertEquals("ACC001", customer.getAccountNumber());
        }
    }

    @Test
    void testInsertCustomer() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

            Customer customer = new Customer();
            customer.setUserId(101);
            customer.setAccountNumber("ACC001");
            customer.setName("Alice");
            customer.setAddress("Street A");
            customer.setPhone("111111");
            customer.setUnitsConsumed(10);

            dao.insertCustomer(customer);

            verify(mockStatement).setInt(1, 101);
            verify(mockStatement).setString(2, "ACC001");
            verify(mockStatement).setString(3, "Alice");
            verify(mockStatement).setString(4, "Street A");
            verify(mockStatement).setString(5, "111111");
            verify(mockStatement).setInt(6, 10);
            verify(mockStatement).executeUpdate();
        }
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(1);

            Customer customer = new Customer();
            customer.setCustomerId(1);
            customer.setAccountNumber("ACC002");
            customer.setName("Bob");
            customer.setAddress("Street B");
            customer.setPhone("222222");
            customer.setUnitsConsumed(20);

            boolean result = dao.updateCustomer(customer);

            assertTrue(result);
            verify(mockStatement).executeUpdate();
        }
    }

    @Test
    void testDeleteCustomerAndUser() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockGetUserId = mock(PreparedStatement.class);
        PreparedStatement mockDeleteCustomer = mock(PreparedStatement.class);
        PreparedStatement mockDeleteUser = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(contains("SELECT user_id"))).thenReturn(mockGetUserId);
            when(mockConnection.prepareStatement(contains("DELETE FROM customers"))).thenReturn(mockDeleteCustomer);
            when(mockConnection.prepareStatement(contains("DELETE FROM users"))).thenReturn(mockDeleteUser);

            when(mockGetUserId.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("user_id")).thenReturn(101);

            boolean result = dao.deleteCustomerAndUser(1);

            assertTrue(result);
            verify(mockGetUserId).executeQuery();
            verify(mockDeleteCustomer).executeUpdate();
            verify(mockDeleteUser).executeUpdate();
        }
    }

    @Test
    void testGetCustomerByUserId() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("customer_id")).thenReturn(1);
            when(mockResultSet.getInt("user_id")).thenReturn(101);
            when(mockResultSet.getString("account_number")).thenReturn("ACC001");
            when(mockResultSet.getString("name")).thenReturn("Alice");
            when(mockResultSet.getString("address")).thenReturn("Street A");
            when(mockResultSet.getString("phone")).thenReturn("111111");

            Customer customer = dao.getCustomerByUserId(101);

            assertNotNull(customer);
            assertEquals("Alice", customer.getName());
            assertEquals(101, customer.getUserId());
        }
    }

    @Test
    void testGetCustomerIdByUserId() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("customer_id")).thenReturn(1);

            int customerId = dao.getCustomerIdByUserId(101);

            assertEquals(1, customerId);
        }
    }
}
