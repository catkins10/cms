<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="70px">开始时间</td>
		<td class="tdtitle" nowrap="nowrap" width="70px">结束时间</td>
		<td class="tdtitle" nowrap="nowrap" width="30%">公司</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">行业</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">公司规模</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">职位</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">月薪</td>
		<td class="tdtitle" nowrap="nowrap" width="30%">离职原因</td>
	</tr>
	<ext:iterate id="career" property="careers">
		<tr align="center">
			<td class="tdcontent"><ext:field writeonly="true" name="career" property="startDate"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="career" property="endDate"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="career" property="company"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="career" property="industry"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="career" property="scale"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="career" property="job"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="career" property="monthlyPay"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="career" property="leaveReason"/></td>
		</tr>
	</ext:iterate>
</table>