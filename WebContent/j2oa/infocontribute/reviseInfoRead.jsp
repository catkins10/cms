<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">标题</td>
		<td class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
		<td class="tdcontent" style="font-size:14px; line-height:20px; text-indent:28px; display:block;"><ext:field writeonly="true" property="newBody"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">退改稿意见</td>
		<td class="tdcontent"><ext:field property="reviseOpinion"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">退改稿人</td>
		<td class="tdcontent"><ext:field property="revisePerson"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">退改稿人电话</td>
		<td class="tdcontent"><ext:field property="revisePersonTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">退改稿时间</td>
		<td class="tdcontent"><ext:field property="reviseTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">修改人</td>
		<td class="tdcontent"><ext:field property="editor"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">修改时间</td>
		<td class="tdcontent"><ext:field property="editTime"/></td>
	</tr>
</table>