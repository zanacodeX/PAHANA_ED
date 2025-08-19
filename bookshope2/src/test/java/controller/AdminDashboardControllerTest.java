package controller;

import dao.AdminDashboardDAO;
import dto.AdminSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminDashboardControllerTest {

    private AdminDashboardController controller;
    private AdminDashboardDAO mockDAO;

    @BeforeEach
    void setUp() {
        mockDAO = mock(AdminDashboardDAO.class);
        controller = new AdminDashboardController() {
            // override DAO to use mock
            @Override
            public AdminSummary getDashboardSummary() throws Exception {
                return mockDAO.getDashboardSummary();
            }
        };
    }

    @Test
    void testGetDashboardSummary() throws Exception {
        // Prepare mock data
        AdminSummary mockSummary = new AdminSummary();
        mockSummary.setTotalEarnings(1000.0);
        mockSummary.setPendingOrders(5);
        mockSummary.setConfirmedOrders(10);
        mockSummary.setCompletedOrders(15);
        mockSummary.setDeclinedOrders(2);
        mockSummary.setTotalCustomers(50);

        // Set up DAO mock
        when(mockDAO.getDashboardSummary()).thenReturn(mockSummary);

        // Call controller
        AdminSummary result = controller.getDashboardSummary();

        // Verify results
        assertNotNull(result);
        assertEquals(1000.0, result.getTotalEarnings());
        assertEquals(5, result.getPendingOrders());
        assertEquals(10, result.getConfirmedOrders());
        assertEquals(15, result.getCompletedOrders());
        assertEquals(2, result.getDeclinedOrders());
        assertEquals(50, result.getTotalCustomers());

        // Verify DAO method was called
        verify(mockDAO, times(1)).getDashboardSummary();
    }
}
