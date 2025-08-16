package controller;

import dao.OrderDAO;
import dto.Order;

public class OrderController {

    private OrderDAO orderDAO;

    public OrderController() {
        this.orderDAO = new OrderDAO();
    }

    /**
     * Place an order by passing an Order DTO.
     */
    public boolean placeOrder(Order order) throws Exception {
        return orderDAO.placeOrder(order);
    }
}
