<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">任务名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">地区名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议员类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="appraiserType" onchange="onAppraiserTypeChanged()"/></td>
		<td class="tdtitle" nowrap="nowrap">评议发起方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="appraiseMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">短信评分比例(百分比)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="smsRatio"/></td>
		<td class="tdtitle" nowrap="nowrap">网络投票评分比例(百分比)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="internetRatio"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议开始日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="appraiseBeginDay"/></td>
		<td class="tdtitle" nowrap="nowrap">评议有效期(天)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="appraiseDays"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议发起时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="appraiseTime"/></td>
		<td class="tdtitle" nowrap="nowrap">是否启用</td>
		<td class="tdcontent"><ext:field writeonly="true" property="enabled"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议月份</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="months"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">基础库评议员身份类别</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="appraiserJobs"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">服务对象身份类别</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="recipientJobs"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议代表</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="delegateAttend"/></td>
	</tr>
	<ext:equal value="1" property="isSpecial">
		<tr>
			<td class="tdtitle" nowrap="nowrap">参评单位</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="specialUnitNames"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">邀请短信格式</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="inviteSms"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">电信联通邀请短信格式</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="otherCarrierInviteSms"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">评议代表邀请短信格式</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="delegateInviteSms"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">答谢短信格式</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="applauseSms"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">最后修改人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="lastModifier"/></td>
		<td class="tdtitle" nowrap="nowrap">最后修改时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="lastModified"/></td>
	</tr>
</table>