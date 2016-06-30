<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">使用者姓名</td>
		<td class="tdcontent"><html:text property="name" styleClass="field required"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">工号</td>
		<td class="tdcontent"><html:text property="code" styleClass="field required"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">PDA号码</td>
		<td class="tdcontent"><html:text property="pdaNumber" styleClass="field required"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在单位</td>
		<td class="tdcontent">
			<html:hidden property="orgId"/>
			<ext:field selectOnly="true" property="orgName" styleClass="field required" onSelect="DialogUtils.selectOrg(600, 400, false, 'orgId{id},orgName{fullName}', '', 'unit,unitDepartment')"/>
		</td>
	</tr>
</table>