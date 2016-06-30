<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function onAppraiserTypeChanged() {
		if(document.getElementsByName('act')[0].value!='create') {
			return;
		}
		if(document.getElementsByName('appraiserType')[0].value=='-1') {
			return;
		}
		if(document.getElementsByName('inviteSms')[0].value=='') {
			var sms = '您好，请您对<参评单位名称>的政风行风做出评议，直接短信回复<评议选项列表>。';
			if(document.getElementsByName('isSpecial')[0].value!='1' && document.getElementsByName('appraiserType')[0].value=='0') { //不是专题,且是基础库评议员
				sms = '您好，欢迎您登录xxx网址，评议验证码为<评议验证码>，参与公众满意度行风测评活动';
			}
			document.getElementsByName('inviteSms')[0].value = sms;
		}
		if(document.getElementsByName('otherCarrierInviteSms')[0].value=='') {
			var sms = '您好，请您打开 xxx网址<WAP评议链接> 对<参评单位名称>的政风行风做出评议。';
			if(document.getElementsByName('isSpecial')[0].value!='1' && document.getElementsByName('appraiserType')[0].value=='0') { //不是专题,且是基础库评议员
				sms = '您好，欢迎您登录xxx网址，评议验证码为<评议验证码>，参与公众满意度行风测评活动';
			}
			document.getElementsByName('otherCarrierInviteSms')[0].value = sms;
		}
		if(document.getElementsByName('delegateInviteSms')[0].value=='') {
			sms = '您好，欢迎您登录xxx网址，评议验证码为<评议验证码>，参与公众满意度行风测评活动';
			document.getElementsByName('delegateInviteSms')[0].value = sms;
		}
		if(document.getElementsByName('applauseSms')[0].value=='') {
			var sms = '感谢您的参与！欢迎登录xxx网址继续参与其他单位的行风测评。评议验证码为<评议验证码>。';
			if(document.getElementsByName('isSpecial')[0].value=='1') { //专题
				sms = '感谢您的参与';
			}
			else if(document.getElementsByName('appraiserType')[0].value=='0') { //不是专题,且是基础库评议员
				sms = '';
			}
			document.getElementsByName('applauseSms')[0].value = sms;
		}
	}
</script>

<%request.setAttribute("editabled", "true");%>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">任务名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">地区名称</td>
		<td class="tdcontent"><ext:field property="area"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议员类型</td>
		<td class="tdcontent"><ext:field property="appraiserType" onchange="onAppraiserTypeChanged()"/></td>
		<td class="tdtitle" nowrap="nowrap">评议发起方式</td>
		<td class="tdcontent"><ext:field property="appraiseMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">短信评分比例(百分比)</td>
		<td class="tdcontent"><ext:field property="smsRatio"/></td>
		<td class="tdtitle" nowrap="nowrap">网络投票评分比例(百分比)</td>
		<td class="tdcontent"><ext:field property="internetRatio"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议开始日期</td>
		<td class="tdcontent"><ext:field property="appraiseBeginDay"/></td>
		<td class="tdtitle" nowrap="nowrap">评议有效期(天)</td>
		<td class="tdcontent"><ext:field property="appraiseDays"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议发起时间</td>
		<td class="tdcontent"><ext:field property="appraiseTime"/></td>
		<td class="tdtitle" nowrap="nowrap">是否启用</td>
		<td class="tdcontent"><ext:field property="enabled"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议月份</td>
		<td class="tdcontent" colspan="3"><ext:field property="months"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">基础评议员身份类别</td>
		<td class="tdcontent" colspan="3">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="55px">
						<ext:field property="allAppraiserJobs" onclick="document.getElementById('tdAppraiserJobs').style.display=(checked ? 'none' : '');"/>
					</td>
					<td id="tdAppraiserJobs" style="<ext:equal value="true" property="allAppraiserJobs">display:none</ext:equal>">
						<ext:field property="selectedAppraiserJobs"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">服务对象身份类别</td>
		<td class="tdcontent" colspan="3">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="55px">
						<ext:field property="allRecipientJobs" onclick="document.getElementById('tdRecipientJobs').style.display=(checked ? 'none' : '');"/>
					</td>
					<td id="tdRecipientJobs" style="<ext:equal value="true" property="allRecipientJobs">display:none</ext:equal>">
						<ext:field property="selectedRecipientJobs"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议代表</td>
		<td class="tdcontent" colspan="3"><ext:field property="delegateAttend"/></td>
	</tr>
	<ext:equal value="1" property="isSpecial">
		<tr>
			<td class="tdtitle" nowrap="nowrap">参评单位</td>
			<td class="tdcontent" colspan="3"><ext:field property="specialUnitNames"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">邀请短信格式</td>
		<td class="tdcontent" colspan="3">
			<div style="padding-bottom: 3px;">
				<input type="button" class="button" value="插入评议员姓名" style="width:100px" onclick="FormUtils.pasteText('inviteSms', '&lt;评议员姓名&gt;')">
				<input type="button" class="button" value="插入参评单位名称" style="width:100px" onclick="FormUtils.pasteText('inviteSms', '&lt;参评单位名称&gt;')">
				<input type="button" class="button" value="插入评议验证码" style="width:100px" onclick="FormUtils.pasteText('inviteSms', '&lt;评议验证码&gt;')">
				<input type="button" class="button" value="插入评议选项列表" style="width:100px" onclick="FormUtils.pasteText('inviteSms', '&lt;评议选项列表&gt;')">
				<input type="button" class="button" value="插入截止时间" style="width:100px" onclick="FormUtils.pasteText('inviteSms', '&lt;截止时间&gt;')">
			</div>
			<ext:field property="inviteSms"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">电信联通邀请短信格式</td>
		<td class="tdcontent" colspan="3">
			<div style="padding-bottom: 3px;">
				<input type="button" class="button" value="插入评议员姓名" style="width:100px" onclick="FormUtils.pasteText('otherCarrierInviteSms', '&lt;评议员姓名&gt;')">
				<input type="button" class="button" value="插入参评单位名称" style="width:100px" onclick="FormUtils.pasteText('otherCarrierInviteSms', '&lt;参评单位名称&gt;')">
				<input type="button" class="button" value="插入评议验证码" style="width:100px" onclick="FormUtils.pasteText('otherCarrierInviteSms', '&lt;评议验证码&gt;')">
				<input type="button" class="button" value="插入WAP评议链接" style="width:100px" onclick="FormUtils.pasteText('otherCarrierInviteSms', '&lt;WAP评议链接&gt;')">
				<input type="button" class="button" value="插入截止时间" style="width:100px" onclick="FormUtils.pasteText('otherCarrierInviteSms', '&lt;截止时间&gt;')">
			</div>
			<ext:field property="otherCarrierInviteSms"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">评议代表邀请短信格式</td>
		<td class="tdcontent" colspan="3">
			<div style="padding-bottom: 3px;">
				<input type="button" class="button" value="插入评议员姓名" style="width:100px" onclick="FormUtils.pasteText('delegateInviteSms', '&lt;评议员姓名&gt;')">
				<input type="button" class="button" value="插入评议验证码" style="width:100px" onclick="FormUtils.pasteText('delegateInviteSms', '&lt;评议验证码&gt;')">
				<input type="button" class="button" value="插入截止时间" style="width:100px" onclick="FormUtils.pasteText('delegateInviteSms', '&lt;截止时间&gt;')">
			</div>
			<ext:field property="delegateInviteSms"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">答谢短信格式</td>
		<td class="tdcontent" colspan="3">
			<div style="padding-bottom: 3px;">
				<input type="button" class="button" value="插入评议员姓名" style="width:100px" onclick="FormUtils.pasteText('applauseSms', '&lt;评议员姓名&gt;')">
				<input type="button" class="button" value="插入评议验证码" style="width:100px" onclick="FormUtils.pasteText('applauseSms', '&lt;评议验证码&gt;')">
				<input type="button" class="button" value="插入截止时间" style="width:100px" onclick="FormUtils.pasteText('applauseSms', '&lt;截止时间&gt;')">
			</div>
			<ext:field property="applauseSms"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">最后修改人</td>
		<td class="tdcontent"><ext:field property="lastModifier"/></td>
		<td class="tdtitle" nowrap="nowrap">最后修改时间</td>
		<td class="tdcontent"><ext:field property="lastModified"/></td>
	</tr>
</table>