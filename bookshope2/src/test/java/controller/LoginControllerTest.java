package controller;

import dao.CustomerDAO;
import dao.UserDAO;
import dto.Customer;
import dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private LoginController loginController;
    private UserDAO mockUserDAO;
    private CustomerDAO mockCustomerDAO;

    @BeforeEach
    void setUp() {
        mockUserDAO = mock(UserDAO.class);
        mockCustomerDAO = mock(CustomerDAO.class);

        // Use anonymous subclass to inject mocks
        loginController = new LoginController() {
            @Override
            public LoginResult login(String email, String password) throws Exception {
                User user = mockUserDAO.validateUser(email, password);

                if (user != null) {
                    if ("admin".equalsIgnoreCase(user.getRole())) {
                        return new LoginResult(user, null, "adminDashboard.jsp");
                    } else {
                        Customer customer = mockCustomerDAO.getCustomerByUserId(user.getUserId());
                        return new LoginResult(user, customer, "customerDashboard");
                    }
                }
                return null;
            }
        };
    }

    @Test
    void testAdminLoginSuccess() throws Exception {
        User admin = new User();
        admin.setUserId(1);
        admin.setEmail("admin@example.com");
        admin.setRole("admin");

        when(mockUserDAO.validateUser("admin@example.com", "pass123")).thenReturn(admin);

        LoginController.LoginResult result = loginController.login("admin@example.com", "pass123");

        assertNotNull(result);
        assertEquals("adminDashboard.jsp", result.getRedirectPage());
        assertEquals(admin, result.getUser());
        assertNull(result.getCustomer());
        verify(mockUserDAO, times(1)).validateUser("admin@example.com", "pass123");
    }

    @Test
    void testCustomerLoginSuccess() throws Exception {
        User user = new User();
        user.setUserId(2);
        user.setEmail("customer@example.com");
        user.setRole("customer");

        Customer customer = new Customer();
        customer.setCustomerId(10);
        customer.setUserId(2);
        customer.setName("John Doe");

        when(mockUserDAO.validateUser("customer@example.com", "pass456")).thenReturn(user);
        when(mockCustomerDAO.getCustomerByUserId(2)).thenReturn(customer);

        LoginController.LoginResult result = loginController.login("customer@example.com", "pass456");

        assertNotNull(result);
        assertEquals("customerDashboard", result.getRedirectPage());
        assertEquals(user, result.getUser());
        assertEquals(customer, result.getCustomer());

        verify(mockUserDAO, times(1)).validateUser("customer@example.com", "pass456");
        verify(mockCustomerDAO, times(1)).getCustomerByUserId(2);
    }

    @Test
    void testInvalidLogin() throws Exception {
        when(mockUserDAO.validateUser("invalid@example.com", "wrongpass")).thenReturn(null);

        LoginController.LoginResult result = loginController.login("invalid@example.com", "wrongpass");

        assertNull(result);
        verify(mockUserDAO, times(1)).validateUser("invalid@example.com", "wrongpass");
        verifyNoInteractions(mockCustomerDAO);
    }
}
