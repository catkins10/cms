<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function completeDesign() {
		if(document.getElementsByName("projectTeam.designQuality")[0].value=="") {
			alert("设计质量未设置");
			return;
		}
		FormUtils.doAction('doApprovalDesign', 'workflowAction=completeApproval');
	}
</script>
<div style="width:500px">
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<tr>
			<td nowrap width="60">常用意见:</td>
			<td width="100%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="100%" id="tdSelectedOftenUseOpinion">
							<ext:field property="opinionPackage.selectedOftenUseOpinion"/>
						</td>
						<td>
							&nbsp;<input class="button" style="width:32px" type="button" value="删除" onclick="if(document.getElementsByName('opinionPackage.selectedOftenUseOpinion')[0].value!='')FormUtils.doAction('runProject', 'workflowAction=deleteOftenUseOpinion')">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap colspan="2">
				填写审核意见: 
				<input type="button" style="width:80px" class="button" value="加为常用意见" onclick="if(document.getElementsByName('opinionPackage.opinion')[0].value!='')FormUtils.doAction('runProject', 'workflowAction=appendOftenUseOpinion')">
			</td>
		</tr>
		<tr>
			<td colspan="2"><ext:field property="opinionPackage.opinion"/></td>
		</tr>
		<tr>
			<td>设计质量:</td>
			<td nowrap="nowrap">
				<ext:field property="projectTeam.designQuality"/>
			</td>
		</tr>
	</table>
</div>