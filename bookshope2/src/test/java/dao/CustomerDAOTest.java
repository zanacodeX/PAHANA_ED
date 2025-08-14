package dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.mockito.*;

import dto.Customer;
import model.DBConnection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private CustomerDAO customerDAO;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock DBConnection.getConnection()
        mockStatic(DBConnection.class);
        when(DBConnection.getConnection()).thenReturn(mockConnection);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    @Order(1)
    public void testInsertCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setUserId(1);
        customer.setAccountNumber("ACC100");
        customer.setName("Test Name");
        customer.setAddress("Test Address");
        customer.setPhone("0771234567");
        customer.setUnitsConsumed(50);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> customerDAO.insertCustomer(customer));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    @Order(2)
    public void testGetCustomerById() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("customer_id")).thenReturn(1);
        when(mockResultSet.getInt("user_id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Name");
        when(mockResultSet.getString("account_number")).thenReturn("ACC100");
        when(mockResultSet.getString("address")).thenReturn("Test Address");
        when(mockResultSet.getString("phone")).thenReturn("0771234567");
        when(mockResultSet.getInt("units_consumed")).thenReturn(50);

        Customer c = customerDAO.getCustomerById(1);
        assertNotNull(c);
        assertEquals("Test Name", c.getName());
        assertEquals("ACC100", c.getAccountNumber());
    }

    @Test
    @Order(3)
    public void testGetCustomerByUserId() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("customer_id")).thenReturn(1);
        when(mockResultSet.getInt("user_id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Name");

        Customer c = customerDAO.getCustomerByUserId(1);
        assertNotNull(c);
        assertEquals("Test Name", c.getName());
    }

    @Test
    @Order(4)
    public void testUpdateCustomer() throws Exception {
        Customer c = new Customer();
        c.setCustomerId(1);
        c.setName("Updated Name");
        c.setAccountNumber("ACC200");
        c.setAddress("New Address");
        c.setPhone("0770000000");
        c.setUnitsConsumed(100);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        assertTrue(customerDAO.updateCustomer(c));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    @Order(5)
    public void testGetAllCustomers() throws Exception {
        when(mockResultSet.next()).thenReturn(true, true, false); // 2 rows
        when(mockResultSet.getInt("customer_id")).thenReturn(1, 2);
        when(mockResultSet.getInt("user_id")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("Name1", "Name2");
        when(mockResultSet.getString("account_number")).thenReturn("ACC1", "ACC2");
        when(mockResultSet.getString("address")).thenReturn("Addr1", "Addr2");
        when(mockResultSet.getString("phone")).thenReturn("077111", "077222");
        when(mockResultSet.getInt("units_consumed")).thenReturn(10, 20);

        List<Customer> customers = customerDAO.getAllCustomers();
        assertEquals(2, customers.size());
        assertEquals("Name1", customers.get(0).getName());
        assertEquals("Name2", customers.get(1).getName());
    }

    @Test
    @Order(6)
    public void testDeleteCustomerAndUser() throws Exception {
        when(mockResultSet.next()).thenReturn(true); // Customer exists
        when(mockResultSet.getInt("user_id")).thenReturn(1);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> {
            boolean deleted = customerDAO.deleteCustomerAndUser(1);
            assertTrue(deleted);
        });
    }

    @Test
    @Order(7)
    public void testGetCustomerIdByUserId() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("customer_id")).thenReturn(5);

        int customerId = customerDAO.getCustomerIdByUserId(1);
        assertEquals(5, customerId);
    }
}
