<%@page import="com.yuanluesoft.jeaf.util.Encoder"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String applicatonName = "edu/onlineclass";
	String payReason = "报名";
	long providerId = 1000;
	String providerName = "李旭";
	float money = 10;
	float providerMoney = 10;
	String recirectAfterPrepay = "http://www.9191edu.com/charge/payment/example.shtml";
	String url = "https://passport.9191edu.com/charge/payment/payment.shtml" +
	  			 "?applicationName=" + applicatonName +
	  			 "&payReason=" + Encoder.getInstance().URLEncode(payReason) +
	  			 "&providerId=" + providerId +
				 "&providerName=" + Encoder.getInstance().URLEncode(providerName) +
				 "&money=" + money +
				 "&providerMoney=" + providerMoney +
	  			 "&redirect=" + Encoder.getInstance().URLEncode(recirectAfterPrepay);
	response.sendRedirect(url);
%>