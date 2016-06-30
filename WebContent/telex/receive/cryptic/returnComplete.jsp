<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/signTelegrams">
	<table border="0" cellspacing="1" width="350px" cellpadding="2" style="table-layout:fixed">
		<tr>
			<td style="padding-left:15px;padding-top:10px; font-size:14px">
				已经完成对下列电报的清退:<br>
				<ext:iterate id="telegramSign" indexId="signIndex" name="sign" property="returnTelegrams">
					<ext:writeNumber name="signIndex" plus="1"/>、<ext:write name="telegramSign" property="telegram.subject"/><br>
				</ext:iterate>
			</td>
		</tr>
	</table>
</ext:form>