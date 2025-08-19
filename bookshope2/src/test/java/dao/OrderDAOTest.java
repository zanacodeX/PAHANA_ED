package dao;

import dto.Order;
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

class OrderDAOTest {

    private OrderDAO dao;

    @BeforeEach
    void setUp() {
        dao = new OrderDAO();
    }

    @Test
    void testPlaceOrder() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeUpdate()).thenReturn(1);

            Order order = new Order();
            order.setCustomerId(1);
            order.setBookId(101);
            order.setBookName("Book A");
            order.setAuthor("Author A");
            order.setUnitPrice(100.0);
            order.setQuantity(2);
            order.setTotal(200.0);

            boolean result = dao.placeOrder(order);

            assertTrue(result);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testGetCompletedOrdersByCustomerId() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);

            when(mockRs.next()).thenReturn(true, false);
            when(mockRs.getInt("id")).thenReturn(1);
            when(mockRs.getInt("customer_id")).thenReturn(1);
            when(mockRs.getInt("book_id")).thenReturn(101);
            when(mockRs.getString("book_name")).thenReturn("Book A");
            when(mockRs.getString("author")).thenReturn("Author A");
            when(mockRs.getDouble("unit_price")).thenReturn(100.0);
            when(mockRs.getInt("quantity")).thenReturn(2);
            when(mockRs.getDouble("total")).thenReturn(200.0);
            when(mockRs.getString("order_status")).thenReturn("Completed");
            when(mockRs.getString("payment_status")).thenReturn("Paid");

            List<Order> orders = dao.getCompletedOrdersByCustomerId(1);

            assertEquals(1, orders.size());
            assertEquals("Book A", orders.get(0).getBookName());
        }
    }

    @Test
    void testGetAllPaidOrders() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);

            when(mockRs.next()).thenReturn(true, false);
            when(mockRs.getInt("id")).thenReturn(1);
            when(mockRs.getString("book_name")).thenReturn("Book A");
            when(mockRs.getString("payment_status")).thenReturn("Paid");

            List<Order> orders = dao.getAllPaidOrders();

            assertEquals(1, orders.size());
            assertEquals("Book A", orders.get(0).getBookName());
        }
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

            dao.updateOrderStatus(1, "Confirmed");

            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testGetOrderById() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);

            when(mockRs.next()).thenReturn(true);
            when(mockRs.getInt("id")).thenReturn(1);
            when(mockRs.getInt("customer_id")).thenReturn(1);
            when(mockRs.getString("book_name")).thenReturn("Book A");
            when(mockRs.getString("author")).thenReturn("Author A");
            when(mockRs.getDouble("unit_price")).thenReturn(100.0);
            when(mockRs.getInt("quantity")).thenReturn(2);
            when(mockRs.getDouble("total")).thenReturn(200.0);
            when(mockRs.getString("order_status")).thenReturn("Pending");
            when(mockRs.getString("payment_status")).thenReturn("Pending");
            when(mockRs.getString("customer_name")).thenReturn("Alice");
            when(mockRs.getString("email")).thenReturn("alice@test.com");

            Order order = dao.getOrderById(1);

            assertNotNull(order);
            assertEquals("Book A", order.getBookName());
            assertEquals("Alice", order.getCustomerName());
        }
    }

    @Test
    void testUpdatePaymentStatus() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

            dao.updatePaymentStatus(1, "Paid");

            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testGetPendingOrdersByCustomerId() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);

            when(mockRs.next()).thenReturn(true, false);
            when(mockRs.getInt("id")).thenReturn(1);
            when(mockRs.getInt("customer_id")).thenReturn(1);
            when(mockRs.getString("book_name")).thenReturn("Book A");
            when(mockRs.getString("author")).thenReturn("Author A");
            when(mockRs.getDouble("unit_price")).thenReturn(100.0);
            when(mockRs.getInt("quantity")).thenReturn(2);
            when(mockRs.getDouble("total")).thenReturn(200.0);
            when(mockRs.getString("order_status")).thenReturn("Pending");
            when(mockRs.getString("payment_status")).thenReturn("Pending");

            List<Order> orders = dao.getPendingOrdersByCustomerId(1);

            assertEquals(1, orders.size());
            assertEquals("Book A", orders.get(0).getBookName());
        }
    }
}
