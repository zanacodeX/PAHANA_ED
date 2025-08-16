package servlet;

import controller.OrderController;
import dto.Order;
import dto.User;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class CustomerPendingOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private OrderController orderController;

    @Override
    public void init() {
        orderController = new OrderController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            User user = (User) session.getAttribute("user");
            List<Order> pendingOrders = orderController.getPendingOrdersForUser(user);

            request.setAttribute("pendingOrders", pendingOrders);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/customerOrderStatus.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error fetching pending orders.");
        }
    }
}
