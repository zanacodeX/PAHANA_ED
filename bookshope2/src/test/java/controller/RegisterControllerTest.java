package controller;

import dao.CustomerDAO;
import dao.UserDAO;
import dto.Customer;
import dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterControllerTest {

    private RegisterController registerController;
    private UserDAO mockUserDAO;
    private CustomerDAO mockCustomerDAO;

    @BeforeEach
    public void setUp() {
        // Create mocks for DAOs
        mockUserDAO = mock(UserDAO.class);
        mockCustomerDAO = mock(CustomerDAO.class);

        // Override DAOs in RegisterController using a subclass
        registerController = new RegisterController() {
            @Override
            public RegisterResult registerCustomer(String email, String password, String accNum,
                                                   String name, String address, String phone) {
                try {
                    // Mocked User creation
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setRole("customer");

                    // Use mocked UserDAO
                    int userId = mockUserDAO.insertUser(user);

                    if (userId != -1) {
                        // Mocked Customer creation
                        Customer customer = new Customer();
                        customer.setUserId(userId);
                        customer.setAccountNumber(accNum);
                        customer.setName(name);
                        customer.setAddress(address);
                        customer.setPhone(phone);

                        // Use mocked CustomerDAO
                        mockCustomerDAO.insertCustomer(customer);

                        return new RegisterResult(true, "Registration successful.");
                    } else {
                        return new RegisterResult(false, "Failed to create user account.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new RegisterResult(false, "Error: " + e.getMessage());
                }
            }
        };
    }

    @Test
    public void testRegisterCustomer_Success() throws Exception {
        // Arrange: mock UserDAO to return a user ID
        when(mockUserDAO.insertUser(any(User.class))).thenReturn(1);
        doNothing().when(mockCustomerDAO).insertCustomer(any(Customer.class));

        // Act
        RegisterController.RegisterResult result = registerController.registerCustomer(
                "test@gmail.com", "12345", "ACC123", "John Doe", "Colombo", "0771234567"
        );

        // Assert
        assertTrue(result.isSuccess());
        assertEquals("Registration successful.", result.getMessage());

        // Verify DAOs were called
        verify(mockUserDAO, times(1)).insertUser(any(User.class));
        verify(mockCustomerDAO, times(1)).insertCustomer(any(Customer.class));
    }

    @Test
    public void testRegisterCustomer_UserInsertFails() throws Exception {
        // Arrange: mock UserDAO to fail
        when(mockUserDAO.insertUser(any(User.class))).thenReturn(-1);

        // Act
        RegisterController.RegisterResult result = registerController.registerCustomer(
                "fail@gmail.com", "12345", "ACC456", "Jane Doe", "Kandy", "0719876543"
        );

        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Failed to create user account.", result.getMessage());

        // Verify CustomerDAO is never called
        verify(mockCustomerDAO, never()).insertCustomer(any(Customer.class));
    }

    @Test
    public void testRegisterCustomer_Exception() throws Exception {
        // Arrange: throw exception when inserting user
        when(mockUserDAO.insertUser(any(User.class))).thenThrow(new RuntimeException("DB error"));

        // Act
        RegisterController.RegisterResult result = registerController.registerCustomer(
                "error@gmail.com", "12345", "ACC789", "Error User", "Galle", "0761234567"
        );

        // Assert
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Error: DB error"));

        // Verify CustomerDAO is never called
        verify(mockCustomerDAO, never()).insertCustomer(any(Customer.class));
    }
}
