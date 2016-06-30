<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" align="center">
		<td class="tdtitle" nowrap="nowrap" width="32px" rowspan="2">序号</td>
		<td class="tdtitle" nowrap="nowrap" rowspan="2">单位名称</td>
		<td class="tdtitle" nowrap="nowrap" rowspan="2">单位分类</td>
		<td class="tdtitle" nowrap="nowrap" width="50px" rowspan="2">总投票数</td>
		<td class="tdtitle" nowrap="nowrap" width="50px" rowspan="2">短信投票</td>
		<td class="tdtitle" nowrap="nowrap" width="50px" rowspan="2">网络投票</td>
		<td class="tdtitle" nowrap="nowrap" width="50px" rowspan="2">满意度</td>
		<td class="tdtitle" nowrap="nowrap" width="60px" rowspan="2" style="display:none">累计满意度</td>
		<td class="tdtitle" nowrap="nowrap" colspan="<ext:writeNumber property="optionVotes"/>">选项统计</td>
		<td class="tdtitle" nowrap="nowrap" width="120px" rowspan="2">&nbsp;</td>
	</tr>
	<tr height="23px" align="center">
		<ext:iterate id="optionVote" property="optionVotes">
			<ext:notEqual value="1" name="optionVote"> 
				<td class="tdtitle" nowrap="nowrap" width="50px" style="<ext:equal value="0" name="optionVote" property="optionType">color:#0000CD</ext:equal>"><ext:field writeonly="true" name="optionVote" property="option"/></td>
			</ext:notEqual>
		</ext:iterate>
	</tr>
	<ext:iterate id="unitAppraise" indexId="unitAppraiseIndex" property="unitAppraises">
		<tr valign="top" align="center" onclick="openOption('<ext:write name="unitAppraise" property="id"/>')">
			<td class="tdcontent"><ext:writeNumber name="unitAppraiseIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="unitAppraise" property="unitName"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="unitAppraise" property="unitCategory"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="unitAppraise" property="voteTotal"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="unitAppraise" property="smsVoteTotal"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="unitAppraise" property="internetVoteTotal"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="unitAppraise" property="scoreComprehensive"/></td>
			<td class="tdcontent" style="display:none"><ext:field writeonly="true" name="unitAppraise" property="yearScoreComprehensive"/></td>
			<ext:iterate id="optionVote" name="unitAppraise" property="optionVotes">
				<ext:notEqual value="1" name="optionVote"> 
					<td class="tdcontent" style="<ext:equal value="0" name="optionVote" property="optionType">color:#0000CD</ext:equal>"><ext:field writeonly="true" name="optionVote" property="voteTotal"/></td>
				</ext:notEqual>
			</ext:iterate>
			<td class="tdcontent" align="center">
				<a style="color:#3333dd" href="javascript:DialogUtils.openSelectDialog('appraise', 'admin/vote', '80%', '80%', false, '', '', '', '', '', true, 'unitAppraiseId=<ext:write name="unitAppraise" property="id"/>')">投票明细</a>
				<a style="color:#3333dd" href="javascript:DialogUtils.openSelectDialog('appraise', 'admin/propose', '80%', '80%', false, '', '', '', '', '', true, 'unitAppraiseId=<ext:write name="unitAppraise" property="id"/>')">意见或建议</a>
			</td>
		</tr>
	</ext:iterate>
	<tr valign="top" align="center" onclick="openOption('<ext:write name="unitAppraise" property="id"/>')">
		<td class="tdcontent"></td>
		<td class="tdcontent" align="left">合计</td>
		<td class="tdcontent" align="left"></td>
		<td class="tdcontent"><ext:field writeonly="true" property="voteTotal"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="smsVoteTotal"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="internetVoteTotal"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="scoreComprehensive"/></td>
		<td class="tdcontent" style="display:none"><ext:field writeonly="true" property="yearScoreComprehensive"/></td>
		<ext:iterate id="optionVote" property="optionVotes">
			<ext:notEqual value="1" name="optionVote"> 
				<td class="tdcontent" style="<ext:equal value="0" name="optionVote" property="optionType">color:#0000CD</ext:equal>"><ext:field writeonly="true" name="optionVote" property="voteTotal"/></td>
			</ext:notEqual>
		</ext:iterate>
		<td class="tdcontent" align="center"></td>
	</tr>
</table>