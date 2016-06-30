<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/j2oa/document/js/keyword.js"></script>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">标题</td>
		<td class="tdcontent" colspan="3"><ext:field property="subject" onchange="parseKeyword(value, 'keywords')"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">接收单位</td>
		<td class="tdcontent" colspan="3"><ext:field property="receiveUnitNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent"><ext:field property="keywords"/></td>
		<td class="tdtitle" nowrap="nowrap">密级</td>
		<td class="tdcontent"><ext:field property="secretLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
		<td class="tdcontent" colspan="3"><ext:field property="body" style="font-size:14px; line-height:20px; text-indent:28px; display:block;"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent" colspan="3"><ext:field property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">责任编辑</td>
		<td class="tdcontent"><ext:field property="editor"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="editorTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">签发领导</td>
		<td class="tdcontent"><ext:field property="signer"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="signerTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">预选刊型</td>
		<td class="tdcontent" colspan="3"><ext:field property="presetMagazineIds"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">来稿单位</td>
		<td class="tdcontent"><ext:field property="fromUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">信息来源</td>
		<td class="tdcontent"><ext:field property="source"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
</table>