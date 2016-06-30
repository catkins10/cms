<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业名称</td>
		<td class="tdcontent" colspan="3"><ext:field property="enterpriseName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">贷款项目</td>
		<td class="tdcontent" colspan="3"><ext:field property="productName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">贷款金额（万元）</td>
		<td class="tdcontent" ><ext:field property="mony"/></td>
		<td class="tdtitle" nowrap="nowrap">贷款发放时间</td>
		<td class="tdcontent" ><ext:field property="time"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<ext:equal property="creator" value="金融办">
			<td class="tdcontent"><ext:field property="creator"/></td>
		</ext:equal>
		<ext:notEqual property="creator" value="金融办">
			<td class="tdcontent"><ext:write property="creator"/></td>
		</ext:notEqual>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>