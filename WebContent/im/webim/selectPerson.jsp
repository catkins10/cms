<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>

<ext:form action="/selectPerson" applicationName="im/webim" pageName="personSelect">
	<jsp:include page="/jeaf/tree/tree.jsp" />
	<html:hidden property="script"/>
	<script>
		function doOK() {
			var personIds = "";
			var selectBoxes = document.getElementsByName("select");
			for(var i=0; i<selectBoxes.length; i++) {
				if(selectBoxes[i].checked) {
					personIds = (personIds=="" ? "" : personIds + ",") + selectBoxes[i].value;
				}
			}
			var script = document.getElementsByName("script")[0].value.replace("{PERSONIDS}", personIds);
			parent.setTimeout(script, 1);
			frameElement.parentNode.removeChild(frameElement); //销毁对话框
		}
	</script>
</ext:form>