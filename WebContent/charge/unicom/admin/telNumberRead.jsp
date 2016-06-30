<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="100px" class="tdtitle">
	<col valign="middle" width="100%" class="tdcontent">
	<tr>
		<td>地区</td>
		<td>
			<html:hidden property="areaId"/>
			<ext:write property="area"/>
		</td>
	</tr>
	<tr>
		<td>区号</td>
		<td>
			<ext:write property="areaCode"/>
		</td>
	</tr>
	<tr>
		<td>起始号码</td>
		<td>
			<ext:write property="beginNumber"/>
		</td>
	</tr>
	<tr>
		<td>结束号码</td>
		<td>
			<ext:write property="endNumber"/>
		</td>
	</tr>
	<tr>
		<td>最后使用的号码</td>
		<td>
			<ext:write property="lastUseNumber"/>
		</td>
	</tr>
</table>