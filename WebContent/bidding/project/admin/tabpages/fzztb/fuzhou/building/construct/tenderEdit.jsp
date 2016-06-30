<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">招标条件</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field property="tender.tenderProjectName"/></td>
		<td class="tdtitle" nowrap="nowrap">项目审批机关</td>
		<td class="tdcontent"><ext:field property="tender.approvalUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">批名称及编号</td>
		<td class="tdcontent"><ext:field property="tender.projectSn"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目业主</td>
		<td class="tdcontent"><ext:field property="tender.owner"/></td>
		<td class="tdtitle" nowrap="nowrap">资金来源</td>
		<td class="tdcontent"><ext:field property="tender.investSource"/></td>
		<td class="tdtitle" nowrap="nowrap">招标人</td>
		<td class="tdcontent"><ext:field property="tender.tenderee"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标代理</td>
		<td class="tdcontent"><ext:field property="tender.agent"/></td>
		<td class="tdtitle" nowrap="nowrap">招标方式</td>
		<td class="tdcontent"><ext:field property="tender.biddingMode"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">项目概况及招标范围</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设地点</td>
		<td class="tdcontent"><ext:field property="tender.projectAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">建设规模</td>
		<td class="tdcontent"><ext:field property="tender.projectScale"/></td>
		<td class="tdtitle" nowrap="nowrap">招标范围内容</td>
		<td class="tdcontent"><ext:field property="tender.biddingContent"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总工期</td>
		<td class="tdcontent"><ext:field property="tender.timeLimit"/></td>
		<td class="tdtitle" nowrap="nowrap">关键节点工期</td>
		<td class="tdcontent"><ext:field property="tender.keysTimeLimit"/></td>
		<td class="tdtitle" nowrap="nowrap">工程质量要求</td>
		<td class="tdcontent"><ext:field property="tender.quality"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">咨询单位</td>
		<td class="tdcontent"><ext:field property="tender.consultationUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">设计单位</td>
		<td class="tdcontent"><ext:field property="tender.designUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">代建单位</td>
		<td class="tdcontent"><ext:field property="tender.fillinUnit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">监理单位</td>
		<td class="tdcontent"><ext:field property="tender.supervisorUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">招标控制价</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tender.controlPrice"/>元</td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">投标人资格及审查办法</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资质类别等级</td>
		<td class="tdcontent"><ext:field property="tender.bidderLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">项目经理等级</td>
		<td class="tdcontent"><ext:field property="tender.managerLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">项目经理专业</td>
		<td class="tdcontent"><ext:field property="tender.managerSubject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联合体投标</td>
		<td class="tdcontent"><ext:field property="tender.unionBid"/></td>
		<td class="tdtitle" nowrap="nowrap">联合体主办方</td>
		<td class="tdcontent"><ext:field property="tender.majorBidder"/></td>
		<td class="tdtitle" nowrap="nowrap">类似工程业绩</td>
		<td class="tdcontent"><ext:field property="tender.managerAchievement"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">类似工程业绩</td>
		<td class="tdcontent"><ext:field property="tender.similarityProject"/></td>
		<td class="tdtitle" nowrap="nowrap">资格审查方式</td>
		<td class="tdcontent"><ext:field property="tender.approvalMode"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">邀请函</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.invitation"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">招标文件的获取</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">每份售价</td>
		<td class="tdcontent"><ext:field property="tender.documentPrice"/></td>
		<td class="tdtitle" nowrap="nowrap">图纸每份售价</td>
		<td class="tdcontent"><ext:field property="tender.drawingPrice"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">评标办法、发布公告的媒体</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评标办法</td>
		<td class="tdcontent"><ext:field property="tender.evaluateMethod"/></td>
		<td class="tdtitle" nowrap="nowrap">媒体名称</td>
		<td class="tdcontent"><ext:field property="tender.media"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
		
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">投标保证金的提交</td>
	</tr>
	<tr>	
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:field property="tender.pledgeTime"/></td>
		<td class="tdtitle" nowrap="nowrap">提交的方式</td>
		<td class="tdcontent"><ext:field property="tender.pledgeMode"/></td>
		<td class="tdtitle" nowrap="nowrap">提交的金额</td>
		<td class="tdcontent"><ext:field property="tender.pledgeMoneyText"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">联系方式</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tender.tenderee"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="tender.tendereeAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">邮编</td>
		<td class="tdcontent"><ext:field property="tender.tendereePostalCode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="tender.tendereeTel"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="tender.tendereeFax"/></td>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="tender.tendereeLinkman"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标代理机构</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tender.agent"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="tender.agentAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">邮编</td>
		<td class="tdcontent"><ext:field property="tender.agentPostalCode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="tender.agentTel"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="tender.agentFax"/></td>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="tender.agentLinkman"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" colspan="6" style="font-weight:bold">投标保证金银行帐号</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开户银行</td>
		<td class="tdcontent"><ext:field property="tender.bank"/></td>
		<td class="tdtitle" nowrap="nowrap">帐户名称</td>
		<td class="tdcontent"><ext:field property="tender.accountName"/></td>
		<td class="tdtitle" nowrap="nowrap">帐号</td>
		<td class="tdcontent"><ext:field property="tender.accounts"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">交易中心</td>
		<td class="tdcontent"><ext:field property="tender.tradingName"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="tender.tradingAddress"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注3.1</td>
		<td class="tdcontent" colspan="5"><ext:field writeonly="true" property="tender.remark31"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注3.2</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark32"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注3.7</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark1"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注3.8</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark2"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注3.9</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark3"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注3.10</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark4"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注3.11</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark311"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注3.12</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark312"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注4.3</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark43"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注6.2</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark62"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">备注7.1</td>
		<td class="tdcontent" colspan="5"><ext:field property="tender.remark71"/></td>
	</tr>
</table>