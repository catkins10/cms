<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/evaluation">
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col align="right">
		<col width="50%">
		<col align="right">
		<col width="50%">
		<tr>
			<td nowrap="nowrap">姓名：</td>
			<td><ext:field property="personName"/></td>
			<td nowrap="nowrap">部门：</td>
			<td><ext:field property="departmentName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">岗位：</td>
			<td><ext:field property="postName"/></td>
			<td nowrap="nowrap">考核月份：</td>
			<td><ext:field property="year"/>年<ext:field property="month"/>月</td>
		</tr>
		<tr>
			<td nowrap="nowrap">工作量：</td>
			<td><ext:field property="workload"/></td>
			<td nowrap="nowrap">部门权重：</td>
			<td><ext:field property="departmentWeight"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">需要完成的考试题量：</td>
			<td><ext:field property="monthTestQuestionNumber"/></td>
			<td nowrap="nowrap">实际完成的考试题量：</td>
			<td><ext:field property="testedQuestionNumber"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">考试总分：</td>
			<td><ext:field property="testScore"/></td>
			<td nowrap="nowrap">考试名次：</td>
			<td><ext:field property="testPosition"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">本岗位考试最高分：</td>
			<td><ext:field property="testMaxScore"/></td>
			<td nowrap="nowrap">本岗位考试最低分：</td>
			<td><ext:field property="testMinScore"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">本岗位考试平均分：</td>
			<td><ext:field property="testAverageScore"/></td>
			<td nowrap="nowrap">考试奖励/处罚：</td>
			<td><ext:field property="testReward"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">互评靠前数：</td>
			<td><ext:field property="mutualEvaluationHighNumber"/></td>
			<td nowrap="nowrap">互评靠后数：</td>
			<td><ext:field property="mutualEvaluationLowNumber"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">互评投票数：</td>
			<td><ext:field property="mutualEvaluationVoteNumber"/></td>
			<td nowrap="nowrap">互评奖励/处罚：</td>
			<td><ext:field property="mutualEvaluationReward"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">铁路局问题数：</td>
			<td>
				A：<ext:field property="eventLevelANumber"/>&nbsp;
				B：<ext:field property="eventLevelBNumber"/>&nbsp;
				C：<ext:field property="eventLevelCNumber"/>&nbsp;
				D：<ext:field property="eventLevelDNumber"/>
			</td>
			<td nowrap="nowrap">铁路局问题处罚：</td>
			<td><ext:field property="eventPunish"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">综合评价成绩：</td>
			<td><ext:field property="score"/></td>
			<td nowrap="nowrap"></td>
			<td></td>
		</tr>
	</table>
</ext:form>