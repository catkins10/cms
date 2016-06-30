<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/learnMissionReport">
	<div style="padding:3px 0px 3px 0px; font-weight:bold">学习情况：</div>
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr height="23px" valign="bottom" align="center">
			<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
			<td class="tdtitle" nowrap="nowrap">用户名</td>
			<td class="tdtitle" nowrap="nowrap" width="120px">开始时间</td>
			<td class="tdtitle" nowrap="nowrap" width="120px">最后学习时间</td>
			<td class="tdtitle" nowrap="nowrap" width="120px">时长(分钟)</td>
		</tr>
		<ext:iterate id="learn" indexId="learnIndex" property="learned">
			<tr align="center" valign="top">
				<td class="tdcontent"><ext:writeNumber name="learnIndex" plus="1"/></td>
				<td class="tdcontent" align="left"><ext:write name="learn" property="personName"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="learn" property="beginTime"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="learn" property="lastTime"/></td>
				<td class="tdcontent"><ext:write name="learn" property="learnMinutes"/></td>
			</tr>
		</ext:iterate>
	</table>
	
	<ext:notEmpty property="notLearnPersons">
		<div style="padding:6px 0px 6px 0px; font-weight:bold">未学习人员：</div>
		<ext:iterate id="person" indexId="personIndex" property="notLearnPersons"><ext:notEqual name="personIndex" value="0">、</ext:notEqual><ext:write name="person" property="name"/></ext:iterate>
	</ext:notEmpty>
</ext:form>