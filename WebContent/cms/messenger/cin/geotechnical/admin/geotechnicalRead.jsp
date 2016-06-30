<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">受理编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sn"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设单位：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="constructionUnit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">勘察单位：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="surveyUnit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设地点：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="constructionPlace"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">拟进场时间：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="approachTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent"><ext:field writeonly="true" property="attachment"/></td>
	</tr>
</table>