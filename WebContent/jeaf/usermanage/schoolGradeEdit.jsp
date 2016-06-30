<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">年级名称</td>
		<td class="tdcontent"><ext:field property="gradeName" itemsProperty="gradeNames" styleClass="field required"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">入学年份</td>
		<td class="tdcontent"><html:text property="enrollTime" styleClass="field required" value=""/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">学制</td>
		<td class="tdcontent"><html:text property="lengthOfSchooling" styleClass="field required" value=""/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">班级数量</td>
		<td class="tdcontent"><html:text property="classCount" styleClass="field required" value=""/></td>
	</tr>
</table>
<html:hidden property="parentOrgId"/>