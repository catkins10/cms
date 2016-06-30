<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function onDateChanged() {
		if(document.getElementsByName('debriefYear')[0].value!=<ext:field writeonly="true" property="debriefYear"/> ||
		   document.getElementsByName('debriefMonth')[0].value!=<ext:field writeonly="true" property="debriefMonth"/>) {
			FormUtils.doAction('project');
		}
	}
</script>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目全称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">年度</td>
		<td class="tdcontent"><ext:field property="debriefYear" onchange="onDateChanged()"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">月份</td>
		<td class="tdcontent"><ext:field property="debriefMonth" onchange="onDateChanged()"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">当月计划完成投资(万元)</td>
		<td class="tdcontent"><ext:field property="debriefInvestPlan"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">当月存在的问题</td>
		<td class="tdcontent"><ext:field property="debriefProblem"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">旬</td>
		<td class="tdcontent"><ext:field property="debriefTenDay" onclick="FormUtils.doAction('project')"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">本旬完成投资(万元)</td>
		<td class="tdcontent"><ext:field property="debriefInvestComplete"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">本旬形象进度目标</td>
		<td class="tdcontent"><ext:field property="debriefProgress"/></td>
	</tr>
</table>