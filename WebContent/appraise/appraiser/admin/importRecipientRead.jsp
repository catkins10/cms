<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">地区名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="areaName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="unitName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">数据文件</td>
		<td class="tdcontent"><ext:field writeonly="true" property="data"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">经办人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">导入时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>