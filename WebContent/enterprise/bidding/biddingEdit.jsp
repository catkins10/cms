<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field property="projectName"/></td>
		<td class="tdtitle" nowrap="nowrap">招标编号</td>
		<td class="tdcontent"><ext:field property="biddingNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">招标内容</td>
		<td colspan="3" class="tdcontent"><ext:field property="content"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">标的</td>
		<td colspan="3" class="tdcontent"><ext:field property="bidTarget"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">技术要求</td>
		<td colspan="3" class="tdcontent"><ext:field property="technicalRequirement"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">时间要求</td>
		<td class="tdcontent"><ext:field property="timeRequirement"/></td>
		<td class="tdtitle" nowrap="nowrap">招标时间</td>
		<td class="tdcontent"><ext:field property="tenderDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资格审查</td>
		<td class="tdcontent"><ext:field property="qualificationExamination"/></td>
		<td class="tdtitle" nowrap="nowrap">招标方式</td>
		<td class="tdcontent"><ext:field property="tenderingPractice"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">业主单位</td>
		<td class="tdcontent"><ext:field property="owner"/></td>
		<td class="tdtitle" nowrap="nowrap">招标代理单位</td>
		<td class="tdcontent"><ext:field property="agency"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">代理联系电话</td>
		<td class="tdcontent"><ext:field property="agencyTel"/></td>
		<td class="tdtitle" nowrap="nowrap">代理传真号码</td>
		<td class="tdcontent"><ext:field property="agencyFax"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">代理联系地址</td>
		<td class="tdcontent"><ext:field property="agencyAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">代理联系人</td>
		<td class="tdcontent"><ext:field property="agencyLinkman"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金开户银行</td>
		<td class="tdcontent"><ext:field property="depositBank"/></td>
		<td class="tdtitle" nowrap="nowrap">保证金帐户名称</td>
		<td class="tdcontent"><ext:field property="depositAccountName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金帐号</td>
		<td class="tdcontent"><ext:field property="depositAccount"/></td>
		<td class="tdtitle" nowrap="nowrap">保证金额(元)</td>
		<td class="tdcontent"><ext:field property="depositAmount"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否已缴纳</td>
		<td class="tdcontent"><ext:field property="depositPaid"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent" nowrap="nowrap"></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">交易中心名称</td>
		<td class="tdcontent"><ext:field property="tradingCenter"/></td>
		<td class="tdtitle" nowrap="nowrap">交易中心地址</td>
		<td class="tdcontent"><ext:field property="tradingCenterAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td colspan="3" class="tdcontent"><ext:field property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="3" class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>