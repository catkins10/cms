<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/unitAppraise">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">单位名称：</td>
			<td><ext:field property="unitName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">单位分类：</td>
			<td><ext:field property="unitCategory"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">总投票数：</td>
			<td><ext:field property="voteTotal"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">短信投票数：</td>
			<td><ext:field property="smsVoteTotal"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">网络投票数：</td>
			<td><ext:field property="internetVoteTotal"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">得分：</td>
			<td><ext:field property="scoreComprehensive"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">累计得分：</td>
			<td><ext:field property="yearScoreComprehensive"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">选项统计：</td>
			<td>
				<ext:iterate id="optionVote" property="optionVotes">
					<ext:notEqual value="1" name="optionVote" property="abstain"> 
						<span style="<ext:equal value="0" name="optionVote" property="optionType">color:#0000CD</ext:equal>"><ext:field writeonly="true" name="optionVote" property="option"/>:<ext:field writeonly="true" name="optionVote" property="voteTotal"/></span>&nbsp;&nbsp;
					</ext:notEqual>
				</ext:iterate>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">发起时间：</td>
			<td><ext:field property="created"/></td>
		</tr>
	</table>
</ext:form>