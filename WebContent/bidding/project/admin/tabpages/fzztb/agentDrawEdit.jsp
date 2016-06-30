<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">委托代理内容</td>
		<td class="tdcontent"><ext:field property="agentDraw.content"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">代理资格条件</td>
		<td class="tdcontent"><ext:field property="agentDraw.agentLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">代理申请方式</td>
		<td class="tdcontent"><ext:field property="agentDraw.drawMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抽选时间</td>
		<td class="tdcontent"><ext:field property="agentDraw.drawTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抽选地点</td>
		<td class="tdcontent"><ext:field property="agentDraw.drawAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">代理取费标准</td>
		<td class="tdcontent"><ext:field property="agentDraw.money"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="agentDraw.remark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">公告期限(天)</td>
		<td class="tdcontent"><ext:field property="agentDraw.publicLimit"/></td>
	</tr>
</table>