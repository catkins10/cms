<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">姓名</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">工作单位或住址</td>
		<td class="tdtitle" nowrap="nowrap" width="180px">身份类别</td>
		<td class="tdtitle" nowrap="nowrap" width="180px">居住地类型</td>
		<td class="tdtitle" nowrap="nowrap" width="90px">移动电话号码</td>
		<td class="tdtitle" nowrap="nowrap" width="180px">提供服务或管理单位</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">服务或管理的项目、内容</td>
	</tr>
	<ext:iterate id="recipient" indexId="recipientIndex" property="recipients">
		<tr valign="top">
			<td class="tdcontent" align="center"><ext:writeNumber name="recipientIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="recipient" property="name"/></td>
			<td class="tdcontent"><ext:write name="recipient" property="unit"/></td>
			<td class="tdcontent"><ext:write name="recipient" property="job"/></td>
			<td class="tdcontent"><ext:write name="recipient" property="areaType"/></td>
			<td class="tdcontent"><ext:write name="recipient" property="mobileNumber"/></td>
			<td class="tdcontent"><ext:write name="recipient" property="serviceUnit"/></td>
			<td class="tdcontent"><ext:write name="recipient" property="serviceContent"/></td>
		</tr>
	</ext:iterate>
</table>