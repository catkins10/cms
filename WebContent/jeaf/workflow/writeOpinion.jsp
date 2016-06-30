<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<table width="500px" border="0" cellpadding="3" cellspacing="0">
	<tr>
		<td nowrap width="60">常用意见:</td>
		<td width="100%">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="100%" id="tdSelectedOftenUseOpinion">
						<ext:field property="opinionPackage.selectedOftenUseOpinion"/>
					</td>
					<td nowrap="nowrap">
						<input class="button" style="margin-left:5px;" type="button" value="删除" onclick="if(document.getElementsByName('opinionPackage.selectedOftenUseOpinion')[0].value!='')FormUtils.doAction('<ext:write property="currentAction"/>', 'workflowAction=<ext:write property="workflowAction"/>&opinionPackage.opinionAction=deleteOftenUseOpinion')">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td nowrap colspan="2">
			填写意见: 
			<input type="button" class="button" value="加为常用意见" onclick="if(document.getElementsByName('opinionPackage.opinion')[0].value!='')FormUtils.doAction('<ext:write property="currentAction"/>', 'workflowAction=<ext:write property="workflowAction"/>&opinionPackage.opinionAction=appendOftenUseOpinion')">
		</td>
	</tr>
	<tr>
		<td colspan="2"><ext:field property="opinionPackage.opinion"/></td>
	</tr>
</table>