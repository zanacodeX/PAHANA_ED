<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Customer" %>
<%
    Customer customer = (Customer) request.getAttribute("customer");
    if (customer == null) {
        response.sendRedirect("manageCustomers.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Customer</title>
<style>
    form {
        width: 400px;
        margin: auto;
        padding: 20px;
        border: 1px solid #ccc;
        background-color: #f9f9f9;
        margin-top: 50px;
    }
    label {
        display: block;
        margin-top: 10px;
    }
    input[type="text"], input[type="number"] {
        width: 100%;
        padding: 8px;
        margin-top: 5px;
    }
    input[type="submit"] {
        margin-top: 15px;
        background-color: #0074D9;
        color: white;
        border: none;
        padding: 10px;
        cursor: pointer;
    }
    input[type="submit"]:hover {
        background-color: #005fa3;
    }
</style>
</head>
<body>

<h2 style="text-align:center;">Edit Customer</h2>

<form action="EditCustomerServlet" method="post">
    <input type="hidden" name="customerId" value="<%= customer.getCustomerId() %>">

    <label>Account Number:</label>
    <input type="text" name="accountNumber" value="<%= customer.getAccountNumber() %>" required>

    <label>Name:</label>
    <input type="text" name="name" value="<%= customer.getName() %>" required>

    <label>Address:</label>
    <input type="text" name="address" value="<%= customer.getAddress() %>" required>

    <label>Phone:</label>
    <input type="text" name="phone" value="<%= customer.getPhone() %>" required>

    <label>Units Consumed:</label>
    <input type="number" name="unitsConsumed" value="<%= customer.getUnitsConsumed() %>" required>

    <br><br>
<div style="display:flex; justify-content:space-between; max-width:450px; margin:0 auto;">
    <button type="submit">Update</button>
    <button type="button" onclick="window.location.href='manageCustomers.jsp';">Cancel</button>
    <button type="button" onclick="window.location.href='manageCustomers.jsp';"
            style="padding:10px 20px; font-size:16px; border-radius:6px; 
                   background-color:#3498db; color:white; border:none; cursor:pointer;">
        ⬅ Back
    </button>
</div>

    <% if (request.getAttribute("message") != null) { %>
    <div style="margin:20px auto; padding:10px; max-width:600px;
                background:#d4edda; color:#155724; border:1px solid #c3e6cb;
                border-radius:6px; text-align:center;">
        ✅ <%= request.getAttribute("message") %>
    </div>
<% } %>

<% if (request.getAttribute("error") != null) { %>
    <div style="margin:20px auto; padding:10px; max-width:600px;
                background:#f8d7da; color:#721c24; border:1px solid #f5c6cb;
                border-radius:6px; text-align:center;">
        ❌ <%= request.getAttribute("error") %>
    </div>
<% } %>
</form>

</body>
</html>