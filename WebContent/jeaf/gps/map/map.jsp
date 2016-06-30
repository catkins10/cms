<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<jsp:include flush="true" page="<%=((com.yuanluesoft.jeaf.gps.forms.Map)request.getAttribute("map")).getSubForm()%>"/>