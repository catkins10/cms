<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

	<tr>
		<td class="tdtitle" nowrap="nowrap">我的分管领导</td>
		<td class="tdcontent"><ext:field property="supervisorNames"/></td>
		<td class="tdtitle" nowrap="nowrap">我分管的用户</td>
		<td class="tdcontent"><ext:field property="supervisePersonNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">我任领导的部门</td>
		<td class="tdcontent"><ext:field property="leadOrgNames"/></td>
		<td class="tdtitle" nowrap="nowrap">我分管的部门</td>
		<td class="tdcontent"><ext:field property="superviseOrgNames"/></td>
	</tr>