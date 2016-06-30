<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveVehicle">
	<ext:notEmpty property="linkmanTel">
		<script charset="utf-8" src="<%=request.getContextPath()%>/jeaf/gps/js/gps.js"></script>
		<script language="javascript">gpsLocation('<ext:write property="linkmanTel"/>', '<ext:write property="id"/>')</script>
	</ext:notEmpty>
	<ext:subForm/>
</ext:form>