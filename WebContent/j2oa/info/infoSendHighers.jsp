<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newSendHigher() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/info/sendHigher.shtml?id=<ext:write property="id"/>', 450, 300);
}
function openSendHigher(sendHigherId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/info/sendHigher.shtml?id=<ext:write property="id"/>&sendHigher.id=' + sendHigherId, 450, 300);
}
</script>
<ext:equal value="true" property="magazineEditor">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="报送登记" onclick="newSendHigher()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">报送级别</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">报送时间</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">报送人</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">采用时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">采用刊物名称</td>
	</tr>
	<ext:iterate id="sendHigher" indexId="sendHigherIndex" property="sendHighers">
		<tr valign="top" align="center" onclick="openSendHigher('<ext:write name="sendHigher" property="id"/>')" style="cursor: pointer">
			<td class="tdcontent"><ext:writeNumber name="sendHigherIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="sendHigher" property="level"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="sendHigher" property="sendTime"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="sendHigher" property="sender"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="sendHigher" property="useTime"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="sendHigher" property="useMagazine"/></td>
		</tr>
	</ext:iterate>
</table>