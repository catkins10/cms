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
		<td class="tdtitle" nowrap="nowrap">接收单位</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="receiveUnitNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent"><ext:field writeonly="true" property="keywords"/></td>
		<td class="tdtitle" nowrap="nowrap">密级</td>
		<td class="tdcontent"><ext:field writeonly="true" property="secretLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
		<td class="tdcontent" colspan="3" style="font-size:14px; line-height: 20px; text-indent:28px;"><ext:field writeonly="true" property="body"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">责任编辑</td>
		<td class="tdcontent"><ext:field writeonly="true" property="editor"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="editorTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">签发领导</td>
		<td class="tdcontent"><ext:field writeonly="true" property="signer"/></td>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="signerTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">预选刊型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="presetMagazineIds"/></td>
		<ext:notEmpty property="supplementTime">
			<td class="tdtitle" nowrap="nowrap">补录时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="supplementTime"/></td>
		</ext:notEmpty>
		<ext:empty property="supplementTime">
			<td class="tdtitle" nowrap="nowrap">投稿时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="contributeTime"/></td>
		</ext:empty>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">来稿单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="fromUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">信息来源</td>
		<td class="tdcontent"><ext:field writeonly="true" property="source"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
</table>