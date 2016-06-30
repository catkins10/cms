<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:iterate id="popedomConfig" property="popedomConfigs">
	<tr>
		<td nowrap="nowrap" class="tdtitle"><ext:write name="popedomConfig" property="popedomTitle"/></td>
		<td class="tdcontent" <%=(request.getAttribute("colspan")==null ? "" : "colspan=\"" + request.getAttribute("colspan") + "\"")%>>
			<html:hidden name="popedomConfig" property="popedomName"/>
			<input type="hidden" name="popedomUserIds_<ext:write name="popedomConfig" property="popedomName"/>" value="<ext:write name="popedomConfig" property="userIds"/>">
			<script>
				new SelectField('<input type="text" name="popedomUserNames_<ext:write name="popedomConfig" property="popedomName"/>" value="<ext:write name="popedomConfig" property="userNames"/>" readonly="readonly"/>',
								"DialogUtils.selectUser(this, 600, 400, true, 'popedomUserIds_<ext:write name="popedomConfig" property="popedomName"/>{id},popedomUserNames_<ext:write name="popedomConfig" property="popedomName"/>{name|<ext:write name="popedomConfig" property="popedomTitle"/>|100%}');",
								"field");
			</script>
		</td>
		<ext:notEmpty name="popedomConfig" property="inheritUserNames">
			<tr>
				<td class="tdtitle" nowrap="nowrap"><ext:write name="popedomConfig" property="popedomTitle"/>(上级继承)</td>
				<td class="tdcontent" <%=(request.getAttribute("colspan")==null ? "" : "colspan=\"" + request.getAttribute("colspan") + "\"")%>><ext:write name="popedomConfig" property="inheritUserNames"/></td>
			</tr>
		</ext:notEmpty>
	</tr>
</ext:iterate>