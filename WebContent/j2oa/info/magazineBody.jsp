<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="margin-bottom: 3px">
	<ext:equal value="true" name="editabled">
		<input value="编辑正文" onclick="DocumentUtils.openRemoteDocument('editDocument', '', 'FormUtils.doAction(&quot;saveMagazineBody&quot;)', 'editMagazineBody')" type="button" class="button" style="width:72px">
	</ext:equal>
	<ext:notEqual value="true" name="editabled">
		<input value="查看正文" onclick="DocumentUtils.openRemoteDocument('viewDocument', '', null, 'editMagazineBody')" type="button" class="button" style="width:72px">
	</ext:notEqual>
</div>
<ext:field property="htmlBody" readonly="true" style="height: 430px"/>
<script>
	document.getElementsByName('htmlBody')[0].value = "";
</script>