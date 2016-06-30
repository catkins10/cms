<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveLink">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
	<script>
		function setAsHeadline() {
			setHeadline('<ext:write name="article" property="siteIds"/>', '<ext:write name="article" property="subjectTrim"/>', '<ext:write name="article" property="link" filter="false"/>', ' ');
		}
		
		function setTop() {
			DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/setTop.shtml?resourceId=<ext:write name="article" property="id"/>&columnIds=<ext:write property="columnId"/>,<ext:write property="otherColumnIds"/>', 430, 200);
		}
	</script>
	<ext:tab/>
</ext:form>