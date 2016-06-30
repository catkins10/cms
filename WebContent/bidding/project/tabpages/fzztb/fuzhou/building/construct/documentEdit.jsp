<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布邀请书时间</td>
		<td class="tdcontent"><ext:field property="plan.inviteDate"/></td>
		<td class="tdtitle" nowrap="nowrap">邀请书媒体</td>
		<td class="tdcontent"><ext:field property="plan.inviteMedia"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">质疑时限</td>
		<td class="tdcontent"><ext:field property="plan.askEnd"/></td>
		<td class="tdtitle" nowrap="nowrap">质疑媒体</td>
		<td class="tdcontent"><ext:field property="plan.askMedia"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开评标时间</td>
		<td class="tdcontent"><ext:field property="plan.bidopeningTime"/></td>
		<td class="tdtitle" nowrap="nowrap">开评标地点</td>
		<td class="tdcontent"><ext:field property="plan.bidopeningAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">确定中标人</td>
		<td class="tdcontent"><ext:field property="plan.pitchonDate"/></td>
		<td class="tdtitle" nowrap="nowrap">确定中标人地点</td>
		<td class="tdcontent"><ext:field property="plan.pitchonAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标结果公示时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>开标后&nbsp;</td>
					<td><ext:field property="plan.publicPitchonDays" style="width:30px;text-align:center"/></td>
					<td>&nbsp;天</td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">中标通知时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>开标后&nbsp;</td>
					<td><ext:field property="plan.noticeDays" style="width:30px;text-align:center"/></td>
					<td>&nbsp;天</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">书面报告备案时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>开标后&nbsp;</td>
					<td><ext:field property="plan.archiveDays" style="width:30px;text-align:center"/></td>
					<td>&nbsp;天</td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">招标控制价(元)</td>
		<td class="tdcontent"><ext:field property="tender.controlPrice"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" nowrap="nowrap">招标文件(GEF)上传</td>
		<td class="tdcontent" colspan="3"><ext:field property="biddingDocuments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">图纸上传</td>
		<td class="tdcontent" colspan="3"><ext:field property="biddingDrawing"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">其他招标文件上传</td>
		<td class="tdcontent" colspan="3"><ext:field property="otherBiddingDocuments"/></td>
	</tr>
</table>
<br>
<center>
	<ext:button name="提交" onclick="submitBiddingDocuments()"/>
</center>

<script>
function submitBiddingDocuments() {
	if(FormUtils.getAttachmentCount('biddingDocuments')==0) {
		alert('尚未完成招标文件上传');
		return false;
	}
	if(FormUtils.getAttachmentCount('otherBiddingDocuments')==0) {
		alert('尚未完成加密工具上传');
		return false;
	}
	var controlPrice = Number(document.getElementsByName("tender.controlPrice")[0].value);
	if(controlPrice+""=="NaN" || controlPrice==0) {
		alert('投标控制价未输入');
		return;
	}
	if(!confirm('是否确定提交招标文件？')) {
		return false;
	}
	FormUtils.doAction('uploadBiddingDocuments');
}
</script>