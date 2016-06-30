<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">征集截止日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="endDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否发布</td>
		<td class="tdcontent"><ext:field writeonly="true" property="issue"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">内容</td>
		<td style="height:300px" class="tdcontent"><ext:field writeonly="true" property="body"/></td>
	</tr>
</table>