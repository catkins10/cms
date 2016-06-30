<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件字名称</td>
		<td class="tdcontent"><ext:field property="docWord"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联合编号的文件字</td>
		<td class="tdcontent"><ext:field property="unionDocWords"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">文件字格式</td>
		<td class="tdcontent">
			<div style="padding-bottom:3px">
				<input type="button" class="button" value="插入文件字" style="width:72px" onclick="FormUtils.pasteText('format', '&lt;文件字&gt;')">
				<input type="button" class="button" value="插入年度" style="width:72px" onclick="FormUtils.pasteText('format', '&lt;年度&gt;')">
				<input type="button" class="button" value="插入序号" style="width:72px" onclick="FormUtils.pasteText('format', '&lt;序号&gt;')">
			</div>
			<ext:field property="format"/>
		</td>
	</tr>
</table>