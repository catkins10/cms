<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveEvaluationParameter">
	<table width="100%" border="0" cellpadding="2" cellspacing="0">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">铁路局问题级别A扣分：</td>
			<td><ext:field property="eventLevelADeduct"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">铁路局问题级别B扣分：</td>
			<td><ext:field property="eventLevelBDeduct"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">铁路局问题级别C扣分：</td>
			<td><ext:field property="eventLevelCDeduct"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">铁路局问题级别D扣分：</td>
			<td><ext:field property="eventLevelDDeduct"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">铁路局问题扣分上限：</td>
			<td><ext:field property="eventDeductLimit"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">互评靠前加分：</td>
			<td><ext:field property="mutualEvaluationRaise"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">互评靠后减分：</td>
			<td><ext:field property="mutualEvaluationDeduct"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">考试题量未完成扣分：</td>
			<td><ext:field property="testLackDeduct"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">考分靠前加分：</td>
			<td><ext:field property="testRaise"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">考分靠后减分：</td>
			<td><ext:field property="testDeduct"/></td>
		</tr>
	</table>
</ext:form>