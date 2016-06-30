<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="margin-bottom: 3px">
	<input value="查看正文" onclick="DocumentUtils.openRemoteDocument('viewDocument', '', null, 'editBody')" type="button" class="button" style="width:72px">
</div>
<ext:field property="htmlBody" readonly="true" style="height: 430px"/>
<script>
	document.getElementsByName('htmlBody')[0].value = "";
</script>