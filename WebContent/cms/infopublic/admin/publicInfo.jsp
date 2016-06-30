<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/savePublicInfo" onsubmit="return formOnSubmit();">
	<ext:tab/>
	<script>
		function preview() {
			window.open('../publicInfo.shtml?id=' + document.getElementsByName("id")[0].value + "&siteId=<ext:write property="siteId"/>");
		}
	</script>
</ext:form>