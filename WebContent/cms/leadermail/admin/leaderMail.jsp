<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveLeaderMail">
	<ext:tab/>
	<script>
		var content;
		document.body.onbeforeprint = function() {
			window.tabLists[0].selectTab("basic");
			content = document.body.innerHTML;
			document.body.innerHTML = document.getElementById("tabbasic").outerHTML;
		};
		document.body.onafterprint = function() {
			document.body.innerHTML = content;
		};
	</script>
</ext:form>