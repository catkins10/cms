<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent" colspan="3"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">内容</td>
		<td class="tdcontent" colspan="3"><ext:field property="body"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交人姓名</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><ext:field property="creatorMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field property="creatorMobile"/></td>
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理时间</td>
		<td class="tdcontent"><ext:field property="transactTime"/></td>
		<td class="tdtitle" nowrap="nowrap">办理人</td>
		<td class="tdcontent"><ext:field property="transactor"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理意见</td>
		<td class="tdcontent" colspan="3"><ext:field property="opinion"/></td>
	</tr>
</table>