<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent" colspan="3"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">类别</td>
		<td class="tdcontent"><ext:field property="category"/></td>
		<td class="tdtitle" nowrap="nowrap">有效时间</td>
		<td class="tdcontent">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed"><tr>
				<td width="50%"><ext:field property="beginDate"/></td>
				<td width="30px" align="center">至</td>
				<td width="50%"><ext:field property="endDate"/></td>
			</tr></table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布范围</td>
		<td class="tdcontent" colspan="3"><ext:field property="issueRange.visitorNames" title="发布范围"/></td>
	</tr>
	<ext:notEmpty property="accessVisitors">
		<tr>
			<td class="tdtitle" nowrap="nowrap">已阅人员</td>
			<td class="tdcontent" colspan="3">
				<ext:iterate id="visitor" indexId="visitorIndex" property="accessVisitors"><ext:notEqual value="0" name="visitorIndex">,</ext:notEqual><ext:write name="visitor" property="userName"/></ext:iterate>
			</td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent" colspan="3"><ext:field property="attachments"/></td>
	</tr>
	<ext:notEmpty property="issueTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">状态</td>
			<td class="tdcontent"><ext:equal value="1" property="issued">已发布</ext:equal><ext:notEqual value="1" property="issued">撤销发布</ext:notEqual></td>
			<td class="tdtitle" nowrap="nowrap">发布时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="issueTime"/></td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">内容</td>
		<td colspan="3" class="tdcontent"><ext:field property="content"/></td>
	</tr>
</table>