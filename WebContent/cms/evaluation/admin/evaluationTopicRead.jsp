<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">描述</td>
		<td class="tdcontent"><ext:field writeonly="true" property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">测评类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="anonymous" onclick="document.getElementById('trEvaluatePersons').style.display=document.getElementsByName('anonymous')[0].checked ? 'none':'';"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">截止日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">被测评人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="targetPersons.visitorNames" title="被测评人"/></td>
	</tr>
	<tr id="trEvaluatePersons" style="<ext:equal value="1" property="anonymous">display:none</ext:equal>">
		<td class="tdtitle" nowrap="nowrap">测评人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="evaluatePersons.visitorNames" title="测评人"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="issueTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
</table>