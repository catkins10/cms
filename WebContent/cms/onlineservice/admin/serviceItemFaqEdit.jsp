<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="5px">
	<col>
	<col width="100%">
	<tr>
		<td nowrap="nowrap" align="right">问题：</td>
		<td><ext:field property="faq.question"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right" height="300px">解答：</td>
		<td><ext:field property="faq.answer"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">登记人：</td>
		<td><ext:field property="faq.creator"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">登记时间：</td>
		<td><ext:field property="faq.created"/></td>
	</tr>
</table>