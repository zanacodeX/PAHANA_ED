package servlet;

import controller.OrderController;
import dto.Customer;
import dto.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

//@WebServlet("/placeOrder")  // Servlet mapping
public class PlaceOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderController orderController;

    @Override
    public void init() throws ServletException {
        orderController = new OrderController();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("customer") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            //  Get Customer from Session
            Customer customer = (Customer) session.getAttribute("customer");
            int customerId = customer.getCustomerId();

            //  Get Book Details from Form
            int bookId = Integer.parseInt(request.getParameter("book_id"));
            String bookName = request.getParameter("book_name");
            String author = request.getParameter("author");
            double unitPrice = Double.parseDouble(request.getParameter("unit_price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            double total = unitPrice * quantity;

            //  Set Order DTO
            Order order = new Order();
            order.setCustomerId(customerId);
            order.setBookId(bookId);
            order.setBookName(bookName);
            order.setAuthor(author);
            order.setUnitPrice(unitPrice);
            order.setQuantity(quantity);
            order.setTotal(total);

            //  Call Controller
            boolean success = orderController.placeOrder(order);

            if (success) {
                request.setAttribute("message", "Order placed successfully!");
                request.getRequestDispatcher("customerDashboard.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Order failed. Please try again.");
                request.getRequestDispatcher("placeOrder.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Order failed: " + e.getMessage());
            request.getRequestDispatcher("placeOrder.jsp").forward(request, response);
        }
    }
}
