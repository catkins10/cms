<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:empty property="innerDialog">
	<table width="100%" border="0" cellpadding="3" cellspacing="0" bordercolor="black" bgcolor="">
		<tr>
			<td nowrap>填写意见:</td>
			<td nowrap><ext:field writeonly="true" property="opinionPackage.opinionType"/>意见</td>
		</tr>
		<tr>
			<td nowrap>常用意见:</td>
			<td style="width:100%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="100%" id="tdSelectedOftenUseOpinion">
							<ext:field property="opinionPackage.selectedOftenUseOpinion"/>
						</td>
						<td style="padding:1px">
							<input class="button" type="button" value="删除" onclick="if(document.getElementsByName('opinionPackage.selectedOftenUseOpinion')[0].value!='')FormUtils.doAction('<ext:write property="opinionPackage.writeOpinionActionName"/>', 'opinionPackage.opinionAction=deleteOftenUseOpinion')">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr valign="top">
			<td colspan="2"><ext:field property="opinionPackage.opinion"/></td>
		</tr>
		<tr valign="top">
			<td colspan="5" align="right" style="padding-right:20px">
				 <input onclick="FormUtils.doAction('<ext:write property="opinionPackage.writeOpinionActionName"/>', 'opinionPackage.opinionAction=appendOftenUseOpinion')" type="button" class="button" value="添加到常用意见列表">
				 <input onclick="FormUtils.doAction('<ext:write property="opinionPackage.writeOpinionActionName"/>')" type="button" class="button" value="提交">
			</td>
		</tr>
	</table>
</ext:empty>