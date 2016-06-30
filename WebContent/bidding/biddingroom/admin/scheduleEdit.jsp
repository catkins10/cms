<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field property="projectName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开标/评标</td>
		<td class="tdcontent"><ext:field property="roomType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">使用时间</td>
		<td class="tdcontent">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="50%"><ext:field property="beginTime"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td width="50%"><ext:field property="endTime"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开/评标室</td>
		<td class="tdcontent"><ext:field property="roomName"/></td>
	</tr>
</table>