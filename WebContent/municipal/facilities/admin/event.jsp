<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveEvent">
	<script language="Javascript1.1" charset="utf-8" src="<%=request.getContextPath()%>/municipal/facilities/js/map.js"></script>
	<script>
		function pdaValidate() {
			DialogUtils.openSelectDialog('municipal/facilities', 'admin/selectPdaUser', 600, 400, false, 'pdaValidateUserId{id},pdaValidateUserName{name}', 'FormUtils.doAction("pdaValidate")');
		}
	</script>
   	<ext:tab/>
   	<html:hidden property="pdaValidateUserId"/>
   	<html:hidden property="pdaValidateUserName"/>
</ext:form>