<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="90px" class="tdtitle">
	<col valign="middle" width="100%" class="tdcontent">
	<tr>
		<td>服务项目</td>
		<td><ext:field property="serviceItem" selectOnly="true" itemsProperty="serviceItems" styleClass="field required"/></td>
	</tr>
	<tr>
		<td>策略面向的用户</td>
		<td>
			<html:checkbox styleClass="checkbox" property="studentEnabled" value="1" styleId="studentEnabledBox"/>&nbsp;<label for="studentEnabledBox">学生</label>&nbsp;
			<html:checkbox styleClass="checkbox" property="teacherEnabled" value="1" styleId="teacherEnabledBox"/>&nbsp;<label for="teacherEnabledBox">教师</label>&nbsp;
			<html:checkbox styleClass="checkbox" property="genearchEnabled" value="1" styleId="genearchEnabledBox"/>&nbsp;<label for="genearchEnabledBox">家长</label>&nbsp;
			<html:checkbox styleClass="checkbox" property="employeeEnabled" value="1" styleId="employeeEnabledBox"/>&nbsp;<label for="employeeEnabledBox">普通用户</label>&nbsp;
			<html:checkbox styleClass="checkbox" property="anonymousEnabled" value="1" styleId="anonymousEnabledBox"/>&nbsp;<label for="anonymousEnabledBox">匿名用户</label>
		</td>
	</tr>
	<tr>
		<td>策略</td>
		<td>
			<html:radio onclick="onPolicyChanged(value)" styleClass="radio" property="policy" value="1" styleId="policyFree"/>&nbsp;<label for="policyFree">免费</label>&nbsp;
			<html:radio onclick="onPolicyChanged(value)" styleClass="radio" property="policy" value="2" styleId="policyByTime"/>&nbsp;<label for="policyByTime">指定试用时间段</label>&nbsp;
			<html:radio onclick="onPolicyChanged(value)" styleClass="radio" property="policy" value="3" styleId="policyByUseDays"/>&nbsp;<label for="policyByUseDays">试用指定天数</label>&nbsp;
		</td>
	</tr>
	<tr id="tryDate" style="display:none">
		<td>试用时间</td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="50%"><ext:field property="beginDate" styleClass="field required"/></td>
					<td nowrap>&nbsp;至&nbsp;</td>
					<td width="50%"><ext:field property="endDate" styleClass="field required"/></td>
				<tr>
			</table>
		</td>
	</tr>
	<tr id="tryDays">
		<td>试用天数</td>
		<td><html:text property="days" styleClass="field required" maxlength="3"/></td>
	</tr>
	<tr>
		<td>创建人</td>
		<td><ext:write property="creator"/></td>
	</tr>
	<tr>
		<td>创建时间</td>
		<td><ext:write property="created" format="yyyy-MM-dd"/></td>  
	</tr>	
	<tr>
		<td>备注</td>
		<td><html:text property="remark"/></td>
	</tr>
</table>
<script>
function onPolicyChanged(policy) {
	document.getElementById("tryDate").style.display = (policy=="2" ? "" : "none");
	document.getElementById("tryDays").style.display = (policy=="3" ? "" : "none");
}
onPolicyChanged(<ext:write property="policy"/>);
</script>