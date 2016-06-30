<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<ext:iterate id="prophaseFile" indexId="prophaseFileIndex" property="prophaseFiles">
		<tr>
			<td class="tdtitle" nowrap="nowrap"><ext:write name="prophaseFile" property="name"/></td>
			<td class="tdcontent" width="100%">
				<html:multibox property="prophase.submittedFiles" disabled="true" styleClass="checkbox"><ext:write name="prophaseFile" property="name"/></html:multibox>&nbsp;已提交
			</td>
		</tr>
	</ext:iterate>
</table>