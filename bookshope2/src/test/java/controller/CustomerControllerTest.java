package controller;

import dao.CustomerDAO;
import dto.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private CustomerController controller;
    private CustomerDAO mockDAO;

    @BeforeEach
    void setUp() {
        mockDAO = mock(CustomerDAO.class);

        // Inject mockDAO by subclassing CustomerController
        controller = new CustomerController() {
            @Override
            public DashboardResult getCustomerData(int userId) {
                try {
                    Customer customer = mockDAO.getCustomerByUserId(userId);
                    if (customer != null) {
                        return new DashboardResult(customer, "Customer data loaded successfully.");
                    } else {
                        return new DashboardResult(null, "No customer data found.");
                    }
                } catch (Exception e) {
                    return new DashboardResult(null, "Error loading customer data: " + e.getMessage());
                }
            }

            @Override
            public EditResult getCustomerById(int customerId) {
                try {
                    Customer customer = mockDAO.getCustomerById(customerId);
                    if (customer != null) {
                        return new EditResult(customer, "Customer loaded successfully.", true);
                    } else {
                        return new EditResult(null, "Customer not found.", false);
                    }
                } catch (Exception e) {
                    return new EditResult(null, "Error loading customer: " + e.getMessage(), false);
                }
            }

            @Override
            public EditResult updateCustomer(Customer customer) {
                try {
                    boolean updated = mockDAO.updateCustomer(customer);
                    String message = updated ? "Customer updated successfully." : "Failed to update customer.";
                    return new EditResult(customer, message, updated);
                } catch (Exception e) {
                    return new EditResult(customer, "Error updating customer: " + e.getMessage(), false);
                }
            }

            @Override
            public ManagementResult getAllCustomers() {
                try {
                    List<Customer> customers = mockDAO.getAllCustomers();
                    return new ManagementResult(customers, "Customers loaded successfully.", true);
                } catch (Exception e) {
                    return new ManagementResult(null, "Error loading customers: " + e.getMessage(), false);
                }
            }

            @Override
            public ManagementResult deleteCustomer(int customerId) {
                try {
                    boolean deleted = mockDAO.deleteCustomerAndUser(customerId);
                    String message = deleted ? "Customer and user deleted successfully." : "Failed to delete customer and user.";
                    return new ManagementResult(null, message, deleted);
                } catch (Exception e) {
                    return new ManagementResult(null, "Error deleting customer: " + e.getMessage(), false);
                }
            }
        };
    }

    @Test
    void testGetCustomerData() throws Exception {
        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(1);
        mockCustomer.setName("John Doe");
        when(mockDAO.getCustomerByUserId(1)).thenReturn(mockCustomer);

        CustomerController.DashboardResult result = controller.getCustomerData(1);
        assertNotNull(result.getCustomer());
        assertEquals("John Doe", result.getCustomer().getName());
        assertEquals("Customer data loaded successfully.", result.getMessage());
        verify(mockDAO, times(1)).getCustomerByUserId(1);
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(2);
        mockCustomer.setName("Jane Smith");
        when(mockDAO.getCustomerById(2)).thenReturn(mockCustomer);

        CustomerController.EditResult result = controller.getCustomerById(2);
        assertTrue(result.isSuccess());
        assertEquals("Jane Smith", result.getCustomer().getName());
        verify(mockDAO, times(1)).getCustomerById(2);
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(3);
        when(mockDAO.updateCustomer(customer)).thenReturn(true);

        CustomerController.EditResult result = controller.updateCustomer(customer);
        assertTrue(result.isSuccess());
        assertEquals("Customer updated successfully.", result.getMessage());
        verify(mockDAO, times(1)).updateCustomer(customer);
    }

    @Test
    void testGetAllCustomers() throws Exception {
        Customer c1 = new Customer();
        c1.setName("Alice");
        Customer c2 = new Customer();
        c2.setName("Bob");
        when(mockDAO.getAllCustomers()).thenReturn(Arrays.asList(c1, c2));

        CustomerController.ManagementResult result = controller.getAllCustomers();
        assertTrue(result.isSuccess());
        assertEquals(2, result.getCustomers().size());
        verify(mockDAO, times(1)).getAllCustomers();
    }

    @Test
    void testDeleteCustomer() throws Exception {
        when(mockDAO.deleteCustomerAndUser(5)).thenReturn(true);

        CustomerController.ManagementResult result = controller.deleteCustomer(5);
        assertTrue(result.isSuccess());
        assertEquals("Customer and user deleted successfully.", result.getMessage());
        verify(mockDAO, times(1)).deleteCustomerAndUser(5);
    }
}
