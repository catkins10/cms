<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:iterate id="popedomConfig" property="popedomConfigs">
	<tr>
		<td nowrap="nowrap" class="tdtitle"><ext:write name="popedomConfig" property="popedomTitle"/></td>
		<td class="tdcontent">
			<html:hidden name="popedomConfig" property="popedomName"/>
			<input type="hidden" name="popedomUserIds_<ext:write name="popedomConfig" property="popedomName"/>" value="<ext:write name="popedomConfig" property="userIds"/>">
			<table border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed;padding-left:0;padding-right:0;padding-top:0;padding-bottom:0" class="field"><tr>
				<td><input type="text" name="popedomUserNames_<ext:write name="popedomConfig" property="popedomName"/>" value="<ext:write name="popedomConfig" property="userNames"/>" readonly="readonly" style="background:''; border-style:none; height:100%; width:100%"></td>
				<td class="selectButton" onclick="selectBbsUsers('系统用户\0部门\0角色\0网上注册用户', 'popedomUserIds_<ext:write name="popedomConfig" property="popedomName"/>', 'popedomUserNames_<ext:write name="popedomConfig" property="popedomName"/>', '<ext:write name="popedomConfig" property="popedomTitle"/>', this)">&nbsp;</td>
			</tr></table>
		</td>
	</tr>
</ext:iterate>