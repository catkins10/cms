<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveMarkStandard">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">适用的地区：</td>
			<td><ext:field property="areaNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">适用的单位类别：</td>
			<td><ext:field property="categories"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">服务对象评议所占比例：</td>
			<td><ext:field property="recipientRatio"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">专家评议所占比例：</td>
			<td><ext:field property="expertRatio"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">二级单位所占比例：</td>
			<td><ext:field property="secondaryRatio"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">下级单位所占比例：</td>
			<td><ext:field property="subordinateRatio"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">扣分上限：</td>
			<td><ext:field property="deductLimit"/></td>
		</tr>
	</table>
</ext:form>