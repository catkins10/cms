<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="90px" class="tdtitle">
	<col valign="middle" width="50%" class="tdcontent">
	<col valign="middle" width="80px" class="tdtitle">
	<col valign="middle" width="50%" class="tdcontent">
	<tr>
		<td>服务名称</td>
		<td colspan="3"><html:text property="serviceName" styleClass="field required"/></td>
	</tr>
	<tr>
		<td>服务面向的用户</td>
		<td colspan="3">
			<html:checkbox styleClass="checkbox" property="studentEnabled" value="1" styleId="studentEnabledBox"/>&nbsp;<label for="studentEnabledBox">学生</label>&nbsp;
			<html:checkbox styleClass="checkbox" property="teacherEnabled" value="1" styleId="teacherEnabledBox"/>&nbsp;<label for="teacherEnabledBox">教师</label>&nbsp;
			<html:checkbox styleClass="checkbox" property="genearchEnabled" value="1" styleId="genearchEnabledBox"/>&nbsp;<label for="genearchEnabledBox">家长</label>&nbsp;
			<html:checkbox styleClass="checkbox" property="employeeEnabled" value="1" styleId="employeeEnabledBox"/>&nbsp;<label for="employeeEnabledBox">普通用户</label>&nbsp;
		</td>
	</tr>
	<tr>
		<td>是否停用</td>
		<td colspan="3">
			<html:radio styleClass="radio" property="halt" value="1" styleId="haltBox"/>&nbsp;<label for="haltBox">是</label>&nbsp;
			<html:radio styleClass="radio" property="halt" value="0" styleId="notHaltBox"/>&nbsp;<label for="notHaltBox">否</label>&nbsp;
		</td>
	</tr>
	<tr>
		<td>服务内容</td>
		<td colspan="3">
			<html:hidden property="allServiceItems"/>
			<ext:field property="textServiceItems" selectOnly="true" onSelect="DialogUtils.openListDialog('选择服务', 'allServiceItems', 550, 320, true, 'textServiceItems{title},textServiceItems{title|服务|100%}');" styleClass="field required"/>
		</td>
	</tr>
	<tr>
		<td>服务报价</td>
		<td colspan="3">
			<ext:iterate id="servicePrice" indexId="servicePriceIndex" property="servicePrices">
				<ext:notEqual value="0" name="servicePriceIndex">、</ext:notEqual><a style="cursor:pointer" onclick="PageUtils.editrecord('charge/servicemanage','admin/servicePrice','<ext:write name="servicePrice" property="id"/>','width=720,height=480')"><ext:write name="servicePrice" property="price"/></a>
			</ext:iterate>
		</td>
	</tr>
	<tr>
		<td>创建人</td>
		<td><ext:write property="creator"/></td>
		<td>创建时间</td>
		<td><ext:write property="created" format="yyyy-MM-dd"/></td>  
	</tr>	
	<tr>
		<td>备注</td>
		<td colspan="3"><html:text property="remark"/></td>
	</tr>
</table>