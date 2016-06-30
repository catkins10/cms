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
		<td class="tdtitle" valign="top">会议内容</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="content"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开始时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="beginTime"/></td>
		<td class="tdtitle" nowrap="nowrap">结束时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">地点</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">与会人员</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="attendees.visitorNames"/></td>
	</tr>
	<ext:notEmpty property="accessVisitors">
		<tr>
			<td class="tdtitle" nowrap="nowrap">已阅人员</td>
			<td class="tdcontent" colspan="3">
				<ext:iterate id="visitor" indexId="visitorIndex" property="accessVisitors"><ext:notEqual value="0" name="visitorIndex">,</ext:notEqual><ext:write name="visitor" property="userName"/></ext:iterate>
			</td>
		</tr>
	</ext:notEmpty>
	<ext:notEmpty property="issueTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">状态</td>
			<td class="tdcontent"><ext:equal value="1" property="issued">已发布</ext:equal><ext:notEqual value="1" property="issued">撤销发布</ext:notEqual></td>
			<td class="tdtitle" nowrap="nowrap">发布时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="issueTime"/></td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td class="tdtitle" nowrap="nowrap">经办人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="handlerName"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>