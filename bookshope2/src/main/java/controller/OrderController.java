package controller;

import dao.CustomerDAO;
import dao.OrderDAO;
import dto.Order;
import dto.User;

import java.util.List;

public class OrderController {

    private OrderDAO orderDAO;
    private CustomerDAO customerDAO;

    public OrderController() {
    	this.customerDAO = new CustomerDAO();
        this.orderDAO = new OrderDAO();//sscsfsc
    }

    
    public boolean placeOrder(Order order) throws Exception {
        return orderDAO.placeOrder(order);
    }
    
    public List<Order> getPendingOrdersForUser(User user) throws Exception {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        int customerId = customerDAO.getCustomerIdByUserId(user.getUserId());
        return orderDAO.getPendingOrdersByCustomerId(customerId);
    }
}

