<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td valign="top" nowrap="nowrap">标题</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">副标题</td>
		<td class="tdcontent"><ext:field writeonly="true" property="subhead"/></td>
		<td nowrap="nowrap">匿名访问级别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="anonymousLevel"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">所属栏目</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="columnFullName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">所属其它栏目</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="otherColumnNames"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">来源</td>
		<td class="tdcontent">
			<ext:field writeonly="true" property="source"/>
			<ext:notEmpty property="sourceRecordUrl">
				<ext:notEmpty property="source">，</ext:notEmpty><a href="<ext:write property="sourceRecordUrl" filter="false"/>" target="_blank">查看源记录</a>
			</ext:notEmpty>
		</td>
		<td nowrap="nowrap">作者</td>
		<td class="tdcontent"><ext:field writeonly="true" property="author"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">关键字</td>
		<td class="tdcontent"><ext:field writeonly="true" property="keyword"/></td>
		<td nowrap="nowrap">标题颜色</td>
		<td class="tdcontent"><ext:field writeonly="true" property="subjectColor"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td nowrap="nowrap">发布时间</td>
		<td class="tdcontent">
			<ext:field writeonly="true" property="issueTime"/>
			<ext:notEmpty property="issueEndTime">&nbsp;至&nbsp;<ext:field writeonly="true" property="issueEndTime"/></ext:notEmpty>
		</td>
	</tr>
	<ext:notEmpty property="readers.visitorNames">
		<tr>
			<td nowrap="nowrap">访问者</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="readers.visitorNames"/></td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td valign="top" nowrap="nowrap">内容</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="body"/></td>
	</tr>
</table>