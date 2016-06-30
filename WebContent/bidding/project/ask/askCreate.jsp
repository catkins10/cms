<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="0" cellspacing="1" bordercolor="black" class="contentTable">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" class="tdtitle">项目名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle" valign="top">提问内容</td>
		<td class="tdcontent"><ext:field property="question"/></td>
	</tr>
</table>
<br>
<center>
	<ext:iterate id="formAction" property="formActions">
		<ext:button nameName="formAction" propertyName="title" nameOnclick="formAction" propertyOnclick="execute"/>
	</ext:iterate>
</center>
<script>
	function formOnSubmit() {
		if(document.getElementsByName('question')[0].value=='') {
			alert('没有输入内容，不能提交。');
			return false;
		}
		return true;
	}
</script>