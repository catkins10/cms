<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%	
	com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationRule rule = (com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationRule)request.getAttribute("rule");
%>
<ext:tab name="ruleDetailTabs">
	<ext:tabBody tabId="<%="provision_" + rule.getId()%>">
		<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col valign="middle" width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">标准分</td>
				<td class="tdcontent"><ext:write name="rule" property="score"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">评分规定</td>
				<td class="tdcontent"><ext:write name="rule" property="provision"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="<%="details_" + rule.getId()%>">
		<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
			<tr height="23px" valign="bottom" align="center">
				<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
				<td class="tdtitle" nowrap="nowrap" width="33%">目标要求</td>
				<td class="tdtitle" nowrap="nowrap" width="33%">评价方法</td>
				<td class="tdtitle" nowrap="nowrap" width="33%">扣分条款</td>
				<td class="tdtitle" nowrap="nowrap" width="200px">备注</td>
			</tr>
			<ext:iterate id="ruleDetail" indexId="ruleDetailIndex" name="rule" property="details">
				<tr valign="top" align="left">
					<td class="tdcontent" align="center"><ext:writeNumber name="ruleDetailIndex" plus="1"/></td>
					<td class="tdcontent"><ext:write name="ruleDetail" property="objective"/></td>
					<td class="tdcontent"><ext:write name="ruleDetail" property="method"/></td>
					<td class="tdcontent"><ext:write name="ruleDetail" property="deduct"/></td>
					<td class="tdcontent"><ext:write name="ruleDetail" property="remark"/></td>
				</tr>
			</ext:iterate>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="<%="scores_" + rule.getId()%>">
		<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
			<tr height="23px" valign="bottom" align="center">
				<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
				<td class="tdtitle" nowrap="nowrap" width="200px">评价等级</td>
				<td class="tdtitle" nowrap="nowrap" width="100%">分数范围</td>
			</tr>
			<ext:iterate id="score" indexId="scoreIndex" name="rule" property="scores">
				<tr align="left">
					<td class="tdcontent" align="center"><ext:writeNumber name="scoreIndex" plus="1"/></td>
					<td class="tdcontent" align="center"><ext:write name="score" property="level"/></td>
					<td class="tdcontent">
						<ext:write name="score" property="minScore"/> ~ <ext:write name="score" property="maxScore"/>
					</td>
				</tr>
			</ext:iterate>
		</table>
	</ext:tabBody>
</ext:tab>