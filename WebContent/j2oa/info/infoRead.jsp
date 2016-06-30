<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">标题</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">刊物名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="magazineName"/></td>
		<td class="tdtitle" nowrap="nowrap">采用级别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="level"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
		<td class="tdcontent" colspan="3" style="font-size:14px; line-height: 20px; text-indent:28px;"><ext:field writeonly="true" property="body"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.attachments"/></td>
		<td class="tdtitle" nowrap="nowrap">状态</td>
		<td class="tdcontent"><ext:field writeonly="true" property="status"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.keywords"/></td>
		<td class="tdtitle" nowrap="nowrap">密级</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.secretLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否简讯</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isBrief"/></td>
		<td class="tdtitle" nowrap="nowrap">是否核实</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isVerified"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否通报</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isCircular"/></td>
		<td class="tdtitle" nowrap="nowrap">是否多条合一</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isCombined"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">责任编辑</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.editor"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.editorTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">签发领导</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.signer"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.signerTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">来稿单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.fromUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">信息来源</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.source"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">投稿时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.contributeTime"/></td>
		<td class="tdtitle" nowrap="nowrap">筛选时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.filterTime"/></td>
	</tr>
	<ext:notEmpty property="infoReceive.filterOpinion">
		<tr>
			<td class="tdtitle" nowrap="nowrap">筛选意见</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="infoReceive.filterOpinion"/></td>
		</tr>
	</ext:notEmpty>
	<ext:notEmpty property="issueTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">定版时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="issueTime"/></td>
			<td class="tdtitle" nowrap="nowrap">刊物期数</td>
			<td class="tdcontent"><ext:field writeonly="true" property="magazineSN"/></td>
		</tr>
	</ext:notEmpty>
	<ext:notEmpty property="supplementTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">补录人</td>
			<td class="tdcontent"><ext:field writeonly="true" property="supplementPerson"/></td>
			<td class="tdtitle" nowrap="nowrap">补录时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="supplementTime"/></td>
		</tr>
	</ext:notEmpty>
</table>