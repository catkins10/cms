<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:iterate id="popedomConfig" property="popedomConfigs">
	<tr>
		<td nowrap="nowrap" class="tdcontent"><ext:write name="popedomConfig" property="popedomTitle"/></td>
		<td class="tdcontent" <%=(request.getAttribute("colspan")==null ? "" : "colspan=\"" + request.getAttribute("colspan") + "\"")%>><ext:write name="popedomConfig" property="userNames"/></td>
	</tr>
	<ext:notEmpty name="popedomConfig" property="inheritUserNames">
		<tr>
			<td nowrap="nowrap" class="tdtitle"><ext:write name="popedomConfig" property="popedomTitle"/>(上级继承)</td>
			<td class="tdcontent" <%=(request.getAttribute("colspan")==null ? "" : "colspan=\"" + request.getAttribute("colspan") + "\"")%>><ext:write name="popedomConfig" property="inheritUserNames"/></td>
		</tr>
	</ext:notEmpty>
</ext:iterate>