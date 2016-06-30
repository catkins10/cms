<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:page applicationName="<%=(request.getParameter("forApplication")==null || request.getParameter("forApplication").isEmpty() ? "cms/messageboard" : request.getParameter("forApplication"))%>" pageName="messageSubmitted"/>