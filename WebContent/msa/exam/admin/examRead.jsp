<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#2D5C7A">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">专业</td>
		<td class="tdcontent"><ext:field writeonly="true" property="speciality"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">期数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="period"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">试卷代码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="examPaperCode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">考试时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="examTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">考试地点</td>
		<td class="tdcontent"><ext:field writeonly="true" property="examAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>