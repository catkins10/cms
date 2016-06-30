<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newStation() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/traffic/busline/admin/station.shtml?id=<ext:write property="id"/>', 400, 200);
}
function openStation(stationId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/traffic/busline/admin/station.shtml?id=<ext:write property="id"/>&station.id=' + stationId, 400, 200);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加站点" style="width:80px" onclick="newStation()">&nbsp;
		<input type="button" class="button" value="调整站点顺序" style="width:80px" onclick="DialogUtils.adjustPriority('traffic/busline', 'station', '站点顺序', 600, 400, 'busLineId=<ext:write property="id"/>')">&nbsp;
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="50px">序号</td>
		<td align="center" nowrap="nowrap" class="tdtitle"l width="100%">站点</td>
	</tr>
	<ext:iterate id="station" indexId="stationIndex" property="stations">
		<tr style="cursor:pointer;" align="center" valign="top" onclick="openStation('<ext:write name="station" property="id"/>')">
			<td align="center" class="tdcontent"><ext:writeNumber name="stationIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="station" property="fullName"/></td>
		</tr>
	</ext:iterate>
</table>