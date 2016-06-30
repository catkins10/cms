<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<script>
	function resetDialog() {
		var showNotPublicType = document.getElementsByName('approvalResult')[0].value=='不公开';
		var showNotPublicReason = document.getElementsByName('notPublicType')[0].value=='其他原因';
		document.getElementsByName('notPublicType')[0].disabled = !showNotPublicType;
		document.getElementById('trNotPublicReason').style.display = showNotPublicType && showNotPublicReason ? '' : 'none';
	}
	function doApprovalAndSend() {
		if(document.getElementsByName('approvalResult')[0].value=='') {
			alert('审批结果不能为空');
			return;
		}
		if(document.getElementsByName('approvalResult')[0].value=='不公开') {
			if(document.getElementsByName('notPublicType')[0].value=='') {
				alert('不公开原因不能为空');
				return;
			}
		}
		else {
			if(document.getElementsByName('doneMedium')[0].value=='') {
				alert('实际的提供介质不能为空');
				return;
			}
			if(document.getElementsByName('doneReceiveMode')[0].value=='') {
				alert('实际的获取方不能为空');
				return;
			}
		}
		FormUtils.doAction('doApprovalAndSend');
	}
	window.onload = resetDialog;
</script>
<table valign="middle" width="600px" border="1" cellpadding="0" cellspacing="0" class="table">
	<col align="right">
	<col width="50%">
	<col align="right">
	<col width="50%">
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">内容描述</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">用途</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="purpose"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否公开</td>
		<td class="tdcontent"><ext:field property="approvalResult" onchange="resetDialog()"/></td>
		<td class="tdtitle" nowrap="nowrap">不公开原因</td>
		<td class="tdcontent"><ext:field property="notPublicType" disabled="true" onchange="resetDialog()"/></td>
	</tr>
	<tr style="display:none" id="trNotPublicReason">
		<td class="tdtitle" nowrap="nowrap">其他原因说明</td>
		<td colspan="3" class="tdcontent"><ext:field property="notPublicReason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">期望的提供介质</td>
		<td class="tdcontent"><ext:field writeonly="true" property="medium"/></td>
		<td class="tdtitle" nowrap="nowrap">期望的获取方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="receiveMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">实际的提供介质</td>
		<td class="tdcontent"><ext:field property="doneMedium"/></td>
		<td class="tdtitle" nowrap="nowrap">实际的获取方式</td>
		<td class="tdcontent"><ext:field property="doneReceiveMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提供时间</td>
		<td class="tdcontent"><ext:field property="approvalTime"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否发布到网站</td>
		<td colspan="3" class="tdcontent"><ext:field property="publicPass"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布申请内容</td>
		<td class="tdcontent"><ext:field property="publicBody"/></td>
		<td class="tdtitle" nowrap="nowrap">发布办理过程</td>
		<td class="tdcontent"><ext:field property="publicWorkflow"/></td>
	</tr>
	<ext:equal value="1" property="proposerType">
		<tr>
			<td class="tdtitle" nowrap="nowrap">申请人姓名</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">工作单位</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="creatorUnit"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系地址</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="creatorAddress"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系人电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
			<td class="tdtitle" nowrap="nowrap">传真</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorFax"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
			<td class="tdtitle" nowrap="nowrap">邮政编码</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorPostalcode"/></td>
		</tr>
	</ext:equal>
	<ext:equal value="2" property="proposerType">
		<tr>
			<td class="tdtitle" nowrap="nowrap">申请企业</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="creatorUnit"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系人姓名</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系人电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
			<td class="tdtitle" nowrap="nowrap">传真</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorFax"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系地址</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="creatorAddress"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
			<td class="tdtitle" nowrap="nowrap">邮政编码</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorPostalcode"/></td>
		</tr>
	</ext:equal>
</table>