<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function completeDesign() {
		if(FieldValidator.validateDateField(document.getElementsByName("projectTeam.completionDate")[0], true, "完成时间")=="NaN") {
			return;
		}
		if(document.getElementsByName("projectTeam.completionDescription")[0].value=="") {
			alert("完成情况不能为空");
			return;
		}
		FormUtils.doAction('doCompleteDesign');
	}
</script>
<div style="width:500px">
	<table valign="middle" width="100%" style="table-layout:fixed" border="0" cellpadding="0" cellspacing="5">
		<col width="70px" align="right">
		<col>
		<tr>
			<td>完成时间：</td>
			<td>
				<ext:field property="projectTeam.completionDate"/>
			</td>
		</tr>
		<tr>
			<td valign="top">完成情况：</td>
			<td><ext:field property="projectTeam.completionDescription"/></td>
		</tr>
	</table>
</div>