<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveParameterConfig">
   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col width="50%">
		<col>
		<col width="50%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">适用的项目分类</td>
			<td class="tdcontent" colspan="3"><ext:field property="categoryArray"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">适用的招标内容</td>
			<td class="tdcontent" colspan="3"><ext:field property="procedureArray"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">适用的地区</td>
			<td class="tdcontent" colspan="3"><ext:field property="cityArray"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">招标编号规则</td>
			<td class="tdcontent"><ext:field property="biddingNumberFormat"/></td>
			<td class="tdtitle" nowrap="nowrap">报名号规则</td>
			<td class="tdcontent"><ext:field property="signUpNumberFormat"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">中标通知书编号规则</td>
			<td class="tdcontent"><ext:field property="noticeNumberFormat"/></td>
			<td class="tdtitle" nowrap="nowrap">报建受理编号规则</td>
			<td class="tdcontent"><ext:field property="declareReceiveNumberFormat"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">报建编号规则</td>
			<td class="tdcontent"><ext:field property="declareNumberFormat"/></td>
			<td class="tdtitle" nowrap="nowrap">标书价格(元)</td>
			<td class="tdcontent"><ext:field property="biddingDocumentsPrice"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">代理抽签公告有效期</td>
			<td class="tdcontent"><ext:field property="agentDrawDays"/></td>
			<td class="tdtitle" nowrap="nowrap">中选代理公示有效期</td>
			<td class="tdcontent"><ext:field property="agentResultDays"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">提问有效期(天)</td>
			<td class="tdcontent"><ext:field property="askDays"/></td>
			<td class="tdtitle" nowrap="nowrap">报名有效期(天)</td>
			<td class="tdcontent"><ext:field property="signUpDays"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">收标有效期(天)</td>
			<td class="tdcontent"><ext:field property="receveDays"/></td>
			<td class="tdtitle" nowrap="nowrap">开标公示有效期(天)</td>
			<td class="tdcontent"><ext:field property="bidopeningDays"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">中标公示有效期(天)</td>
			<td class="tdcontent"><ext:field property="pitchonDays"/></td>
			<td class="tdtitle" nowrap="nowrap">预审公示有效期(天)</td>
			<td class="tdcontent"><ext:field property="preapprovalDays"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">中标通知时间(天)</td>
			<td class="tdcontent"><ext:field property="noticeDays"/></td>
			<td class="tdtitle" nowrap="nowrap">书面报告备案时间(天)</td>
			<td class="tdcontent"><ext:field property="archiveDays"/></td>
		</tr>
	</table>
</ext:form>