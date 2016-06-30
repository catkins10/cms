<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="120px" class="tdtitle">
	<col valign="middle" width="100%" class="tdcontent">
	<tr>
		<td class="tdtitle" nowrap="nowrap">地区名称</td>
		<td class="tdcontent"><ext:write property="area" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">区号</td>
		<td class="tdcontent"><ext:write property="areaCode" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">起始号码</td>
		<td class="tdcontent"><ext:write property="beginNumber" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">结束号码</td>
		<td class="tdcontent">
			<ext:write property="endNumber" />
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:write property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd"/></td>
	</tr>
</table>