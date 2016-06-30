<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveMonitorParameter">
	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col valign="middle" width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">机构名称</td>
			<td class="tdcontent"><ext:field property="orgName"/></td>
		</tr>
		<ext:iterate id="contentParameter" property="contentParameters">
			<tr style="<ext:equal value="false" name="contentParameter" property="timeoutSupport">display:none</ext:equal>">
				<td class="tdtitle" nowrap="nowrap"><ext:write name="contentParameter" property="contentName"/>时限</td>
				<td class="tdcontent"><ext:field name="contentParameter" property="timeout"/></td>
			</tr>
		</ext:iterate>
	</table>
</ext:form>