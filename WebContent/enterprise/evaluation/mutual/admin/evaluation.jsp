<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveEvaluation">
	<table border="0" cellpadding="1px" cellspacing="0">
		<tr>
			<td nowrap="nowrap">互评月份：</td>
			<td nowrap="nowrap"><ext:field property="evaluationYear"/>年<ext:field property="evaluationMonth"/>月&nbsp;&nbsp;</td>
			<td nowrap="nowrap">互评人：</td>
			<td width="100%"><ext:field property="creator"/></td>
			<td nowrap="nowrap">互评时间：</td>
			<td nowrap="nowrap"><ext:field property="created"/></td>
		</tr>
	</table>
	<table id="evaluationTable" class="evaluationTable" border="1" cellpadding="3px" cellspacing="0" style="border-collapse:collapse; margin-top:2px;" bordercolor="#000000">
		<tr align="center" style="font-weight: bold">
			<td class="tdtitle" nowrap="nowrap">序号</td>
			<td class="tdtitle" nowrap="nowrap">被评价人员</td>
			<td class="tdtitle" nowrap="nowrap" width="100%">评价等级</td>
		</tr>
		<ext:iterate id="result" indexId="resultIndex" property="results">
			<tr align="center">
				<td class="tdcontent" nowrap="nowrap"><ext:writeNumber name="resultIndex" plus="1"/></td>
				<td class="tdcontent" nowrap="nowrap"><ext:write name="result" property="personName"/></td>
				<td class="tdcontent" nowrap="nowrap" align="left">
					<input disabled="true" <ext:equal value="2" name="result" property="evaluateLevel">checked="true"</ext:equal> type="checkbox" name="evaluateLevel_<ext:write name="result" property="personId"/>" value="2" id="evaluateLevel_<ext:write name="result" property="personId"/>_high"><label for="evaluateLevel_<ext:write name="result" property="personId"/>_high">前<ext:write property="voteNumber"/>名</label>&nbsp;
					<input disabled="true" <ext:equal value="0" name="result" property="evaluateLevel">checked="true"</ext:equal> type="checkbox" name="evaluateLevel_<ext:write name="result" property="personId"/>" value="0" id="evaluateLevel_<ext:write name="result" property="personId"/>_low"><label for="evaluateLevel_<ext:write name="result" property="personId"/>_low">后<ext:write property="voteNumber"/>名</label>
				</td>
			</tr>
		</ext:iterate>
	</table>
</ext:form>