<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">开始时间</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">结束时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">培训机构</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">培训课程</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">获得证书</td>
	</tr>
	<ext:iterate id="training" property="trainings">
		<tr align="center">
			<td class="tdcontent"><ext:field writeonly="true" name="training" property="startDate"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="training" property="endDate"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="training" property="organization"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="training" property="course"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="training" property="certificate"/></td>
		</tr>
	</ext:iterate>
</table>