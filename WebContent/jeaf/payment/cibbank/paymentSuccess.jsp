<%@ page contentType="text/html; charset=utf-8" %>

<%response.sendRedirect(request.getContextPath() + "/jeaf/payment/completePayment.shtml?paymentId=" + Long.parseLong(request.getParameter("orderID"), 32));%>