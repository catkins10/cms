<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table  width="100%" border="0" cellpadding="0" cellspacing="3">
	<col valign="middle" align="right">
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" valign="top">必备条件：</td>
		<td>
			<ext:equal value="0" property="evaluationPrerequisites.sourceRecordId">
				<ext:field property="evaluationPrerequisites.prerequisites"/>
			</ext:equal>
			<ext:notEqual value="0" property="evaluationPrerequisites.sourceRecordId">
				<ext:field readonly="true" property="evaluationPrerequisites.prerequisites"/>
			</ext:notEqual>
		</td>
	</tr>
	<ext:iterate id="score" indexId="scoreIndex" property="evaluationPrerequisites.scores">
		<tr>
			<td nowrap="nowrap">[<ext:write name="score" property="level"/>]的考评结果：</td>
			<td>
				<input type="hidden" name="levelId" value="<ext:write name="score" property="levelId"/>"/>
				<input type="text" name="score" value="<ext:write name="score" property="score"/>" class="field"/>
			</td>
		</tr>
	</ext:iterate>
	<tr>
		<td nowrap="nowrap">备注：</td>
		<td><ext:field property="evaluationPrerequisites.remark"/></td>
	</tr>
</table>