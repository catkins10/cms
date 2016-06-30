<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
	function openChange(changeId) {
	}
</script>
<ext:equal value="true" name="editabled">
	<script>
		function newChange() {
			DialogUtils.openDialog('<%=request.getContextPath()%>/traffic/busline/admin/change.shtml?id=<ext:write property="id"/>', 500, 300);
		}
		function openChange(changeId) {
			DialogUtils.openDialog('<%=request.getContextPath()%>/traffic/busline/admin/change.shtml?id=<ext:write property="id"/>&change.id=' + changeId, 600, 370);
		}
	</script>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加线路变更通知" onclick="newChange()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td nowrap="nowrap" class="tdtitle" width="50px">序号</td>
		<td nowrap="nowrap" class="tdtitle" width="100%">变更说明</td>
		<td nowrap="nowrap" class="tdtitle" width="100px">变更时间</td>
	</tr>
	<ext:iterate id="change" indexId="changeIndex" property="changes">
		<tr style="cursor:pointer;" align="center" valign="top" onclick="openChange('<ext:write name="change" property="id"/>')">
			<td class="tdcontent"><ext:writeNumber name="changeIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="change" property="content"/></td>
			<td class="tdcontent"><ext:write name="change" property="beginDate" format="yyyy-MM-dd"/></td>
		</tr>
	</ext:iterate>
</table>