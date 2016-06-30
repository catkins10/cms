<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col width="50%">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap">工作内容</td>
		<td class="tdtitle" nowrap="nowrap">时间/金额</td>
		<td class="tdtitle" nowrap="nowrap">地点或媒体</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布招标邀请书</td>
		<td class="tdcontent"><ext:field property="plan.inviteDate"/></td>
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
		<td class="tdcontent">
			<html:hidden property="tender.buyDocumentAddress"/>
			<ext:field property="plan.buyDocumentAddress" onchange="document.getElementsByName('tender.buyDocumentAddress')[0].value=value"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标文件售价(元)</td>
		<td class="tdcontent"><ext:field property="tender.documentPrice"/></td>
		<td class="tdcontent">&nbsp;</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标图纸售价(元)</td>
		<td class="tdcontent"><ext:field property="tender.drawingPrice"/></td>
		<td class="tdcontent">&nbsp;</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金金额(元)</td>
		<td class="tdcontent"><ext:field property="tender.pledgeMoney"/></td>
		<td class="tdcontent">&nbsp;</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金提交</td>
		<td class="tdcontent"><ext:field property="tender.pledgeTime"/></td>
		<td class="tdcontent"><ext:field property="tender.pledgeMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标文件质疑时限</td>
		<td class="tdcontent"><ext:field property="plan.askEnd"/></td>
		<td class="tdcontent"><ext:field property="plan.askMedia"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">递交投标文件</td>
		<td class="tdcontent">
			<html:hidden property="tender.submitTime"/>
			<ext:field property="plan.submitTime" onchange="document.getElementsByName('tender.submitTime')[0].value=value"/>
		</td>
		<td class="tdcontent">
			<html:hidden property="tender.submitAddress"/>
			<ext:field property="plan.submitAddress" onchange="document.getElementsByName('tender.submitAddress')[0].value=value"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开标、评标</td>
		<td class="tdcontent"><ext:field property="plan.bidopeningTime"/></td>
		<td class="tdcontent"><ext:field property="plan.bidopeningAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">确定中标人</td>
		<td class="tdcontent"><ext:field property="plan.pitchonDate"/></td>
		<td class="tdcontent"><ext:field property="plan.pitchonAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标结果公示</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>开标后&nbsp;</td>
					<td width="30px"><ext:field property="plan.publicPitchonDays" style="text-align:center"/></td>
					<td>&nbsp;天</td>
				</tr>
			</table>
		</td>
		<td class="tdcontent"><ext:field property="plan.publicPitchonMedia"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发放中标通知书</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>开标后&nbsp;</td>
					<td width="30px"><ext:field property="plan.noticeDays" style="text-align:center"/></td>
					<td>&nbsp;天</td>
				</tr>
			</table>
		</td>
		<td class="tdcontent"><ext:field property="plan.noticeAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招投标情况书面报告备案</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>开标后&nbsp;</td>
					<td width="30px"><ext:field property="plan.archiveDays" style="text-align:center"/></td>
					<td>&nbsp;天</td>
				</tr>
			</table>
		</td>
		<td class="tdcontent"><ext:field property="plan.archiveAddress"/></td>
	</tr>
</table>