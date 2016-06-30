<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveSmsBusiness">
   	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col>
		<col width="100%">
		<tr>
			<td nowrap="nowrap" align="right">业务名称：</td>
			<td><ext:field property="name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">计费方式：</td>
			<td><ext:field property="chargeMode"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">价格(元)：</td>
			<td><ext:field property="price"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">折扣：</td>
			<td><ext:field property="discount"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">附加信息：</td>
			<td><ext:field property="postfix"/></td>
		</tr>
	</table>
</ext:form>