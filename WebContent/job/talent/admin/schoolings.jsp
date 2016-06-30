<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">开始时间</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">结束时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">学校</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">专业</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">班级</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">学历</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">学位</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">考生号</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">学号</td>
	</tr>
	<ext:iterate id="schooling" property="schoolings">
		<tr align="center">
			<td class="tdcontent"><ext:field writeonly="true" name="schooling" property="startDate"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="schooling" property="endDate"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="schooling" property="school"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="schooling" property="specialty"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="schooling" property="schoolClass"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="schooling" property="qualification"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="schooling" property="degree"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="schooling" property="candidateNumber"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="schooling" property="studentNumber"/></td>
		</tr>
	</ext:iterate>
</table>