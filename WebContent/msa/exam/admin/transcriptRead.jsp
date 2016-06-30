<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#2D5C7A">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">身份证号码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="identityCard"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">准考证号码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="permit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申考等级</td>
		<td class="tdcontent"><ext:field property="examLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申考职务</td>
		<td class="tdcontent"><ext:field property="job"/></td>
	</tr>
	<ext:iterate id="score" property="scores">
		<tr>
			<td class="tdtitle" nowrap="nowrap"><ext:write name="score" property="subject"/></td>
			<td class="tdcontent"><ext:write name="score" property="score"/></td>
		</tr>
	</ext:iterate>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总成绩</td>
		<td class="tdcontent"><ext:field writeonly="true" property="totalScore"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否通过</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pass"/></td>
	</tr>
</table>