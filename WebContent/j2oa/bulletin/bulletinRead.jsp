<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">类别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
		<td class="tdtitle" nowrap="nowrap">有效时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="beginDate"/>&nbsp;至&nbsp;<ext:field writeonly="true" property="endDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布范围</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="issueRange.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">已阅人员</td>
		<td class="tdcontent" colspan="3">
			<ext:iterate id="visitor" indexId="visitorIndex" property="accessVisitors"><ext:notEqual value="0" name="visitorIndex">,</ext:notEqual><ext:write name="visitor" property="userName"/></ext:iterate>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="attachments"/></td>
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
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">内容</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="content"/></td>
	</tr>
</table>