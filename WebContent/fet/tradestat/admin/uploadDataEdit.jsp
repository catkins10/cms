<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">数据年度</td>
		<td class="tdcontent"><html:text property="dataYear" styleClass="field required"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">数据月份</td>
		<td class="tdcontent"><html:text property="dataMonth" styleClass="field required"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">数据类型</td>
		<td class="tdcontent">
			<html:radio property="isExport" value="1" styleClass="radio" styleId="isExport"/><label for="isExport">&nbsp;出口</label>
			<html:radio property="isExport" value="0" styleClass="radio" styleId="isImport"/><label for="isImport">&nbsp;进口</label>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">经办人</td>
		<td class="tdcontent"><ext:write property="transactor"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上传时间</td>
		<td class="tdcontent"><ext:write property="uploadTime" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">数据文件</td>
		<td class="tdcontent">
			<ext:iterateAttachment id="dataFile" applicationName="fet/tradestat" propertyRecordId="id" attachmentType="data">
				<a href="<ext:write name="dataFile" property="urlAttachment"/>"><ext:write name="dataFile" property="name"/></a>
			</ext:iterateAttachment>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent"><html:file property="upload" styleClass="field required"/></td>
	</tr>
</table>