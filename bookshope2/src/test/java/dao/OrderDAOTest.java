package dao;

import dto.Order;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDAOTest {

    private static OrderDAO orderDAO;
    private static int testOrderId ;
    private static final int testCustomerId = 2; // replace with a valid customer_id
    private static final int testBookId = 4;     // replace with a valid book_id

    @BeforeAll
    public static void setup() {
        orderDAO = new OrderDAO();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void testPlaceOrder() {
        Order order = new Order();
        order.setCustomerId(testCustomerId);
        order.setBookId(testBookId);
        order.setBookName("JUnit Test Book");
        order.setAuthor("JUnit Author");
        order.setUnitPrice(150.0);
        order.setQuantity(2);
        order.setTotal(300.0);

        assertDoesNotThrow(() -> orderDAO.placeOrder(order));

        // Fetch last inserted pending order for this customer
        try {
            List<Order> pendingOrders = orderDAO.getPendingOrdersByCustomerId(testCustomerId);
            assertFalse(pendingOrders.isEmpty(), "Pending orders list should not be empty");
            testOrderId = pendingOrders.get(pendingOrders.size() - 1).getId();
            System.out.println("Inserted testOrderId = " + testOrderId);
        } catch (Exception e) {
            fail("Exception fetching inserted order: " + e.getMessage());
        }
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void testGetPendingOrdersByCustomerId() {
        assertDoesNotThrow(() -> {
            List<Order> pendingOrders = orderDAO.getPendingOrdersByCustomerId(testCustomerId);
            assertNotNull(pendingOrders, "Pending orders should not be null");
            assertTrue(pendingOrders.size() > 0, "There should be at least one pending order");
        });
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void testUpdatePaymentStatusToPaid() {
        assertDoesNotThrow(() -> orderDAO.updatePaymentStatusToPaid(testOrderId));

        try {
            List<Order> completedOrders = orderDAO.getCompletedOrdersByCustomerId(testCustomerId);
            boolean found = completedOrders.stream().anyMatch(o -> o.getId() == testOrderId);
            assertTrue(found, "Order payment status should be updated to Paid");
        } catch (Exception e) {
            fail("Exception fetching completed orders: " + e.getMessage());
        }
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void testGetCompletedOrdersByCustomerId() {
        assertDoesNotThrow(() -> {
            List<Order> completedOrders = orderDAO.getCompletedOrdersByCustomerId(testCustomerId);
            assertNotNull(completedOrders, "Completed orders should not be null");
            assertTrue(completedOrders.size() > 0, "There should be at least one completed order");
        });
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void testGetAllPaidOrders() {
        assertDoesNotThrow(() -> {
            List<Order> paidOrders = orderDAO.getAllPaidOrders();
            assertNotNull(paidOrders, "Paid orders list should not be null");
        });
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void testGetAllOrders() {
        assertDoesNotThrow(() -> {
            List<Order> allOrders = orderDAO.getAllOrders();
            assertNotNull(allOrders, "All orders list should not be null");
            assertTrue(allOrders.size() > 0, "There should be at least one order");
        });
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    public void testUpdateOrderStatus() {
        assertDoesNotThrow(() -> orderDAO.updateOrderStatus(testOrderId, "Confirmed"));

        try {
            List<Order> orders = orderDAO.getOrdersByCustomerId(testCustomerId);
            boolean confirmed = orders.stream()
                    .anyMatch(o -> o.getId() == testOrderId && "Confirmed".equalsIgnoreCase(o.getOrderStatus()));
            assertTrue(confirmed, "Order status should be Confirmed");
        } catch (Exception e) {
            fail("Exception fetching orders: " + e.getMessage());
        }
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    public void testGetPendingOrders() {
        assertDoesNotThrow(() -> {
            List<Order> pendingOrders = orderDAO.getPendingOrders();
            assertNotNull(pendingOrders, "Pending orders should not be null");
        });
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    public void testGetOrdersByCustomerId() {
        List<Order> orders = orderDAO.getOrdersByCustomerId(testCustomerId);
        assertNotNull(orders, "Orders by customer should not be null");
        assertTrue(orders.size() > 0, "There should be at least one order for the customer");
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    public void testGetOrderById() {
        Order order = null;
        try {
            order = orderDAO.getOrderById(testOrderId);
        } catch (Exception e) {
            fail("Exception fetching order by ID: " + e.getMessage());
        }

        assertNotNull(order, "Order should not be null");
        assertEquals(testOrderId, order.getId(), "Order ID should match testOrderId");
    }

}

   