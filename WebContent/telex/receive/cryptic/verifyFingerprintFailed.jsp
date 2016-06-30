<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/submitFingerprint">
	<table border="0" cellspacing="0" cellpadding="0" width="350px" height="60px">
		<tr> 
			<td style="font-size:14px;">&nbsp;<img src="<%=request.getContextPath()%>/jeaf/form/img/warn.gif" align="middle">&nbsp;系统中没有匹配的指纹。</td>
		</tr>
	</table>
</ext:form>