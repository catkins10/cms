<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveCityConfig">
   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col width="50%">
		<col>
		<col width="50%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">地区名称</td>
			<td class="tdcontent"><ext:field property="name"/></td>
			<td class="tdtitle" nowrap="nowrap">地区简称</td>
			<td class="tdcontent"><ext:field property="shortName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">交易中心名称</td>
			<td class="tdcontent"><ext:field property="trading"/></td>
			<td class="tdtitle" nowrap="nowrap">交易中心地址</td>
			<td class="tdcontent"><ext:field property="tradingAddress"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">代理抽签地址</td>
			<td class="tdcontent"><ext:field property="drawAddress"/></td>
			<td class="tdtitle" nowrap="nowrap">发布招标邀请书媒体</td>
			<td class="tdcontent"><ext:field property="inviteMedia"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">购买招标文件地址</td>
			<td class="tdcontent"><ext:field property="buyDocumentAddress"/></td>
			<td class="tdtitle" nowrap="nowrap">招标文件质疑地点</td>
			<td class="tdcontent"><ext:field property="askMedia"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">投标文件的递交地点</td>
			<td class="tdcontent"><ext:field property="submitAddress"/></td>
			<td class="tdtitle" nowrap="nowrap">开标、评标地点</td>
			<td class="tdcontent"><ext:field property="bidopeningAddress"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">确定中标人地点</td>
			<td class="tdcontent"><ext:field property="pitchonAddress"/></td>
			<td class="tdtitle" nowrap="nowrap">中标结果公示媒体</td>
			<td class="tdcontent"><ext:field property="publicPitchonMedia"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">发放中标通知书地点</td>
			<td class="tdcontent"><ext:field property="noticeAddress"/></td>
			<td class="tdtitle" nowrap="nowrap">书面报告备案地点</td>
			<td class="tdcontent"><ext:field property="archiveAddress"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目报建地点</td>
			<td class="tdcontent"><ext:field property="declareAddress"/></td>
			<td class="tdtitle" nowrap="nowrap">施工许可证发放地点</td>
			<td class="tdcontent"><ext:field property="licenceAddress"/></td>
		</tr>
		<tr>
			<td class="tdtitle" valign="top">代理取费标准</td>
			<td class="tdcontent" colspan="3"><ext:field property="agentChargeStandard"/></td>
		</tr>
		<tr>
			<td class="tdtitle" valign="top">代理抽签说明</td>
			<td class="tdcontent" colspan="3"><ext:field property="agentDrawRemark"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">上午上班时间</td>
			<td class="tdcontent"><ext:field property="workBeginAm"/></td>
			<td class="tdtitle" nowrap="nowrap">上午下班时间</td>
			<td class="tdcontent"><ext:field property="workEndAm"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">下午上班时间</td>
			<td class="tdcontent"><ext:field property="workBeginPm"/></td>
			<td class="tdtitle" nowrap="nowrap">下午下班时间</td>
			<td class="tdcontent"><ext:field property="workEndPm"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">标书网银商户</td>
			<td class="tdcontent" colspan="3"><ext:field property="merchantNames"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">保证金网银商户</td>
			<td class="tdcontent" colspan="3"><ext:field property="paymentMerchantNames"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">标书和图纸强制B2C支付</td>
			<td class="tdcontent"><ext:field property="documentsB2CPayment"/></td>
			<td class="tdtitle" nowrap="nowrap">保证金强制B2B支付</td>
			<td class="tdcontent"><ext:field property="pledgeB2BPayment"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">标书统一定价</td>
			<td class="tdcontent"><ext:field property="documentsUniformPrice"/></td>
			<td class="tdtitle" nowrap="nowrap">代理结算手续费</td>
			<td class="tdcontent"><ext:field property="agentSettleFee"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">网络支付保证金</td>
			<td class="tdcontent" colspan="3"><ext:field property="pledgeInternetPayment"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">保证金返还利率(%)</td>
			<td class="tdcontent"><ext:field property="pledgeReturnRate"/></td>
			<td class="tdtitle" nowrap="nowrap">保证金利息所得税率(%)</td>
			<td class="tdcontent"><ext:field property="pledgeReturnTax"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">管理员</td>
			<td class="tdcontent"><ext:field property="managers.visitorNames" title="管理员"/></td>
			<td class="tdtitle" nowrap="nowrap">报表查看人员</td>
			<td class="tdcontent"><ext:field property="reportVisitors.visitorNames" title="报表查看人员"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目登记人员</td>
			<td class="tdcontent"><ext:field property="projectCreators.visitorNames" title="项目登记人员"/></td>
			<td class="tdtitle" nowrap="nowrap">项目审核人员</td>
			<td class="tdcontent"><ext:field property="projectApprovers.visitorNames" title="项目审核人员"/></td>
		</tr>
	</table>
</ext:form>