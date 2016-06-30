<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/examTestReport">
	<div style="padding:3px 0px 3px 0px; font-weight:bold">考试情况：</div>
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr align="center">
			<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
			<td class="tdtitle" nowrap="nowrap">用户名</td>
			<td class="tdtitle" nowrap="nowrap" width="120px">完成时间</td>
			<td class="tdtitle" nowrap="nowrap" width="120px">得分</td>
		</tr>
		<ext:iterate id="test" indexId="testIndex" property="tested">
			<tr align="center" style="cursor:pointer" valign="top" onclick="PageUtils.editrecord('enterprise/exam', 'examTest', '<ext:write name="test" property="id"/>', 'mode=dialog,width=80%,height=80%', '', 'check=true')">
				<td class="tdcontent"><ext:writeNumber name="testIndex" plus="1"/></td>
				<td class="tdcontent" align="left"><ext:write name="test" property="personName"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="test" property="endTime"/></td>
				<td class="tdcontent"><ext:write name="test" property="scoreAsText"/></td>
			</tr>
		</ext:iterate>
	</table>
	
	<ext:notEmpty property="notTestPersons">
		<div style="padding:6px 0px 6px 0px; font-weight:bold">未参加人员：</div>
		<ext:iterate id="person" indexId="personIndex" property="notTestPersons"><ext:notEqual name="personIndex" value="0">、</ext:notEqual><ext:write name="person" property="name"/></ext:iterate>
	</ext:notEmpty>
</ext:form>