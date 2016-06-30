<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col width="50%">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap">工作内容</td>
		<td class="tdtitle" nowrap="nowrap">时间</td>
		<td class="tdtitle" nowrap="nowrap">地点或媒体</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布招标邀请书</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.inviteDate"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.inviteMedia"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发售招标文件</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.buyDocumentBegin"/> 至 <ext:field writeonly="true" property="plan.buyDocumentEnd"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.buyDocumentAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标文件售价(元)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tender.documentPrice"/></td>
		<td class="tdcontent">&nbsp;</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标图纸售价(元)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tender.drawingPrice"/></td>
		<td class="tdcontent">&nbsp;</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金金额(元)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tender.pledgeMoney"/></td>
		<td class="tdcontent">&nbsp;</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金提交</td>
		<td class="tdcontent"><ext:field writeonly="true" property="tender.pledgeTime"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="tender.pledgeMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标文件质疑时限</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.askEnd"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.askMedia"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">递交投标文件</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.submitTime"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.submitAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开标、评标</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.bidopeningTime"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.bidopeningAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">确定中标人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.pitchonDate"/></td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.pitchonAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标结果公示</td>
		<td class="tdcontent">开标后<ext:field writeonly="true" property="plan.publicPitchonDays"/>天内</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.publicPitchonMedia"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发放中标通知书</td>
		<td class="tdcontent">开标后<ext:field writeonly="true" property="plan.noticeDays"/>天内</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.noticeAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招投标情况书面报告备案</td>
		<td class="tdcontent">开标后<ext:field writeonly="true" property="plan.archiveDays"/>天内</td>
		<td class="tdcontent"><ext:field writeonly="true" property="plan.archiveAddress"/></td>
	</tr>
</table>