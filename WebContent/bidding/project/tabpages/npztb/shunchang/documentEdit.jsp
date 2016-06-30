<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
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
		<td class="tdtitle" nowrap="nowrap">发售招标文件</td>
		<td class="tdcontent">
			<html:hidden property="tender.buyDocumentBegin"/>
			<html:hidden property="tender.buyDocumentEnd"/>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><ext:field property="plan.buyDocumentBegin" onchange="document.getElementsByName('tender.buyDocumentBegin')[0].value=value"/></td>
					<td>&nbsp;至&nbsp;</td>
					<td><ext:field property="plan.buyDocumentEnd" onchange="document.getElementsByName('tender.buyDocumentEnd')[0].value=value"/></td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">发售招标文件地点</td>
		<td class="tdcontent">
			<html:hidden property="tender.buyDocumentAddress"/>
			<ext:field property="plan.buyDocumentAddress" onchange="document.getElementsByName('tender.buyDocumentAddress')[0].value=value"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手续费(元)</td>
		<td class="tdcontent"><ext:field property="tender.documentPrice"/></td>
		<td class="tdtitle" nowrap="nowrap">招标图纸售价(元)</td>
		<td class="tdcontent"><ext:field property="tender.drawingPrice"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标文件质疑时限</td>
		<td class="tdcontent"><ext:field property="plan.askEnd"/></td>
		<td class="tdtitle" nowrap="nowrap">质疑媒体</td>
		<td class="tdcontent"><ext:field property="plan.askMedia"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金提交时间</td>
		<td class="tdcontent"><ext:field property="tender.pledgeTime"/></td>
		<td class="tdtitle" nowrap="nowrap">保证金提交的方式</td>
		<td class="tdcontent"><ext:field property="tender.pledgeMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金金额(元)</td>
		<td class="tdcontent"><ext:field property="tender.pledgeMoney"/></td>
		<td class="tdtitle" nowrap="nowrap">招标控制价(元)</td>
		<td class="tdcontent"><ext:field property="tender.controlPrice"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">递交投标文件时间</td>
		<td class="tdcontent"><ext:field property="plan.submitTime"/></td>
		<td class="tdtitle" nowrap="nowrap">递交投标文件地点</td>
		<td class="tdcontent"><ext:field property="plan.submitAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开评标时间</td>
		<td class="tdcontent"><ext:field property="plan.bidopeningTime"/></td>
		<td class="tdtitle" nowrap="nowrap">开评标地点</td>
		<td class="tdcontent"><ext:field property="plan.bidopeningAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">确定中标人时间</td>
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
					<td style="width:30px"><ext:field property="plan.publicPitchonDays" style="text-align:center"/></td>
					<td>&nbsp;天</td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">中标结果公示媒体</td>
		<td class="tdcontent"><ext:field property="plan.publicPitchonMedia"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标通知时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>开标后&nbsp;</td>
					<td style="width:30px"><ext:field property="plan.noticeDays" style="text-align:center"/></td>
					<td>&nbsp;天</td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">发放中标通知书地点</td>
		<td class="tdcontent"><ext:field property="plan.noticeAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">书面报告备案时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>开标后&nbsp;</td>
					<td style="width:30px"><ext:field property="plan.archiveDays" style="text-align:center"/></td>
					<td>&nbsp;天</td>
				</tr>
			</table>
		</td>
		<td class="tdtitle" nowrap="nowrap">书面报告备案地点</td>
		<td class="tdcontent"><ext:field property="plan.archiveAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" nowrap="nowrap">招标文件上传</td>
		<td class="tdcontent"><ext:field property="biddingDocuments"/></td>
		<td class="tdtitle" nowrap="nowrap">图纸上传</td>
		<td class="tdcontent"><ext:field property="biddingDrawing"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">招标公告</td>
		<td class="tdcontent" colspan="3" height="280px"><ext:field property="tender.body"/></td>
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
	if(HtmlEditor.getHtmlContent('tender.body')=='') {
		alert('尚未输入招标公告');
		return false;
	}
	var controlPrice = Number(document.getElementsByName("tender.controlPrice")[0].value);
	if(controlPrice+""=="NaN" || controlPrice==0) {
		alert('投标控制价未输入');
		return;
	}
	if(document.getElementsByName("plan.buyDocumentBegin")[0].value=="" || document.getElementsByName("plan.buyDocumentEnd")[0].value=="") {
		alert('发售招标文件时间未输入');
		return;
	}
	if(document.getElementsByName("plan.submitTime")[0].value=="") {
		alert('递交投标文件时间未输入');
		return;
	}
	if(document.getElementsByName("tender.pledgeTime")[0].value=="") {
		alert('保证金提交时间未输入');
		return;
	}
	if(document.getElementsByName("plan.bidopeningTime")[0].value=="") {
		alert('开评标时间未输入');
		return;
	}
	var documentPrice = Number(document.getElementsByName("tender.documentPrice")[0].value);
	var drawingPrice = Number(document.getElementsByName("tender.drawingPrice")[0].value);
	var pledgeMoney = Number(document.getElementsByName("tender.pledgeMoney")[0].value);
	if(documentPrice+"" == "NaN" || documentPrice<=0) {
		alert('标书价格必须大于0');
		return false;
	}
	if(pledgeMoney+"" == "NaN" || pledgeMoney<=0) {
		alert('保证金必须大于0');
		return false;
	}
	if(drawingPrice+"" == "NaN" || drawingPrice<=0) {
		if(!confirm('图纸价格未输入,是否确认没有图纸？')) {
			return false;
		}
	}
	if(!confirm('是否确定提交招标公告？')) {
		return false;
	}
	FormUtils.doAction('uploadBiddingDocuments');
}
</script>