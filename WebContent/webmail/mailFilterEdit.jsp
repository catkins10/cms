<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:hidden property="viewPackage.searchConditions"/>
<html:hidden property="viewPackage.searchConditionsDescribe"/>
<table valign="middle" width="100%" style="table-layout:fixed;border-collapse:collapse" border="0" cellpadding="0" cellspacing="3" bordercolor="#2D5C7A">
	<tr>
		<td>启用规则:&nbsp;<html:radio property="enable" value="1" styleClass="radio" styleId="ruleEnable"/>&nbsp;<label for="ruleEnable">启用</label>&nbsp;&nbsp;<html:radio property="enable" value="0" styleClass="radio" styleId="ruleDisable"/>&nbsp;<label for="ruleDisable">禁用</label></td>
	</tr>
	<tr>
		<td style="padding-top:8px">过滤条件:
			<jsp:include page="/jeaf/view/viewSearchConditions.jsp" />
		</td>
	</tr>
	<tr>
		<td style="padding-top:8px">执行操作:
			<table border="0" width="100%">
				<tr>
					<td width="163px">
						<html:select property="actionType" onchange="document.getElementById('tdMailbox').style.display=(selectedIndex==0 ? 'none':'')">
							<html:option value="delete">直接删除</html:option>
							<html:option value="move">发送到邮箱</html:option>
						</html:select>
					</td>
					<td id="tdMailbox" style="<ext:equal value="delete" property="actionType">display:none</ext:equal>">
						<html:select property="selectedMailboxId">
						<html:optionsCollection property="receiveMailboxes" label="mailboxName" value="id"/>
						</html:select>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<script>
function formOnSubmit() {
	generateSearchConditions("viewPackage"); //生成搜索条件
	return true;
}
</script>