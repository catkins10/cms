<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#2D5C7A">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">专业</td>
		<td class="tdcontent"><ext:field property="speciality"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">期数</td>
		<td class="tdcontent"><ext:field property="period"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">试卷代码</td>
		<td class="tdcontent"><ext:field property="examPaperCode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">考试时间</td>
		<td class="tdcontent"><ext:field property="examTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">考试地点</td>
		<td class="tdcontent"><ext:field property="examAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>