<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	int rowNum = 0; 
%>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">姓名</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">人员类别</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">等级</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">资质证书编号</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">企业名称</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">报名号</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">解锁时间</td>
	</tr>
	<ext:iterate id="signUp" property="signUps">
		<ext:iterate id="jobholder" name="signUp" property="jobholders">
			<tr valign="top" align="center">
				<td class="tdcontent"><%=++rowNum%></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="jobholder" property="jobholderName"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="jobholder" property="jobholderCategory"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="jobholder" property="qualification"/></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="jobholder" property="certificateNumber"/></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseName"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="signUpNo"/></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="jobholder" property="unlockTime"/></td>
			</tr>
		</ext:iterate>
	</ext:iterate>
</table>