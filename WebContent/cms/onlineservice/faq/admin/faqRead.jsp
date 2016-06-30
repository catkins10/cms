<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" valign="top">问题</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="question"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属目录</td>
		<td class="tdcontent"><ext:field writeonly="true" property="directoryName"/></td>
		<td class="tdtitle" nowrap="nowrap">所属其它目录</td>
		<td class="tdcontent"><ext:field writeonly="true" property="otherDirectoryNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属办理事项</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="itemNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">解答</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="answer"/></td>
	</tr>
</table>