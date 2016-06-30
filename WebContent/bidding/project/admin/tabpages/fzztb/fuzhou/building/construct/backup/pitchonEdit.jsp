<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<b>中标结果</b>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目编号</td>
		<td class="tdcontent"><ext:field property="pitchon.projectNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field property="pitchon.projectName"/></td>
		<td class="tdtitle" nowrap="nowrap">开标时间</td>
		<td class="tdcontent"><ext:field property="pitchon.bidopeningTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标方式</td>
		<td class="tdcontent"><ext:field property="pitchon.biddingMode"/></td>
		<td class="tdtitle" nowrap="nowrap">中标人</td>
		<td class="tdcontent"><ext:field property="pitchon.pitchonEnterprise"/></td>
		<td class="tdtitle" nowrap="nowrap">项目经理</td>
		<td class="tdcontent"><ext:field property="pitchon.manager"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标金额</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pitchon.pitchonMoney"/></td>
		<td class="tdtitle" nowrap="nowrap">业主评委</td>
		<td class="tdcontent"><ext:field property="pitchon.ownerRater"/></td>
		<td class="tdtitle" nowrap="nowrap">评标专家</td>
		<td class="tdcontent"><ext:field property="pitchon.raters"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">联系方式</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标人</td>
		<td class="tdcontent"><ext:field property="pitchon.tenderee"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="pitchon.tendereeAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">邮编</td>
		<td class="tdcontent"><ext:field property="pitchon.tendereePostalCode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="pitchon.tendereeTel"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="pitchon.tendereeFax"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮件</td>
		<td class="tdcontent"><ext:field property="pitchon.tendereeMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="pitchon.tendereeLinkman"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标代理机构</td>
		<td class="tdcontent"><ext:field property="pitchon.agent"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="pitchon.agentAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">邮编</td>
		<td class="tdcontent"><ext:field property="pitchon.agentPostalCode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="pitchon.agentTel"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="pitchon.agentFax"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮件</td>
		<td class="tdcontent"><ext:field property="pitchon.agentMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="pitchon.agentLinkman"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">招投标监督机构</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="pitchon.supervision"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="pitchon.supervisionAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">监督电话</td>
		<td class="tdcontent"><ext:field property="pitchon.supervisionTel"/></td>
	</tr>
	
	<tr style="display:none">
		<td class="tdtitle" colspan="6" style="font-weight:bold">KC值</td>
	</tr>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">K1</td>
		<td class="tdcontent"><ext:field property="kc.k1"/></td>
		<td class="tdtitle" nowrap="nowrap">K2</td>
		<td class="tdcontent"><ext:field property="kc.k2"/></td>
		<td class="tdtitle" nowrap="nowrap">Q</td>
		<td class="tdcontent"><ext:field property="kc.q"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">公示期限</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">公示期限(天)</td>
		<td class="tdcontent"><html:text property="pitchon.publicLimit"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
</table>
<br>
<b>资格审查不合格或被确定为无效标、废标的投标人名称及原因、理由与依据</b>
<table valign="middle" width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col width="36px">
	<col width="100%">
	<col width="80px">
	<col width="80px">
	<col width="80px">
	<col width="80px">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap">序号</td>
		<td class="tdtitle" nowrap="nowrap">投标人名称</td>
		<td class="tdtitle" nowrap="nowrap">评审结果</td>
		<td class="tdtitle" nowrap="nowrap">原因</td>
		<td class="tdtitle" nowrap="nowrap">依据及理由</td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
	</tr>
	<ext:iterate indexId="pitchoutIndex" id="pitchout" property="pitchouts">
		<tr align="center">
			<td class="tdcontent"><ext:writeNumber name="pitchoutIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="pitchout" property="enterpriseName"/></td>
			<td class="tdcontent"><ext:write name="pitchout" property="result"/></td>
			<td class="tdcontent"><ext:write name="pitchout" property="reason"/></td>
			<td class="tdcontent"><ext:write name="pitchout" property="basis"/></td>
			<td class="tdcontent"><ext:write name="pitchout" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>