<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveServiceItem">
	<script language="JavaScript" charset="utf-8" src="../js/onlineservice.js"></script>
	<script>
		function preview() {
			window.open('../serviceItem.shtml?id=' + document.getElementsByName("id")[0].value + "&siteId=<ext:write property="siteId"/>");
		}
	</script>
   	<ext:subForm/>
</ext:form>