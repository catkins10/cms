<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">标题</td>
		<td class="tdcontent" colspan="3"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent" colspan="3"><ext:field property="infoReceive.keywords"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">刊物名称</td>
		<td class="tdcontent"><ext:field property="magazineName"/></td>
		<td class="tdtitle" nowrap="nowrap">刊物期数</td>
		<td class="tdcontent"><ext:field property="magazineSN"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">来稿单位</td>
		<td class="tdcontent"><ext:field property="infoReceive.fromUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">信息来源</td>
		<td class="tdcontent"><ext:field property="infoReceive.source"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
		<td class="tdcontent" colspan="3"><ext:field property="body" style="font-size:14px; line-height: 20px; text-indent:28px; display:block"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">密级</td>
		<td class="tdcontent"><ext:field property="infoReceive.secretLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">是否简讯</td>
		<td class="tdcontent"><ext:field property="isBrief"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否核实</td>
		<td class="tdcontent"><ext:field property="isVerified"/></td>
		<td class="tdtitle" nowrap="nowrap">是否通报</td>
		<td class="tdcontent"><ext:field property="isCircular"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">责任编辑</td>
		<td class="tdcontent"><ext:field property="infoReceive.editor"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="infoReceive.editorTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">签发领导</td>
		<td class="tdcontent"><ext:field property="infoReceive.signer"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="infoReceive.signerTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">补录人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="supplementPerson"/></td>
		<td class="tdtitle" nowrap="nowrap">补录时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="supplementTime"/></td>
	</tr>
</table>