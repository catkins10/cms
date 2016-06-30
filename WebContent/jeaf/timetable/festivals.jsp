<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newFestival() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/timetable/festival.shtml?id=<ext:write property="id"/>', 500, 290);
	}
	function openFestival(festivalId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/timetable/festival.shtml?id=<ext:write property="id"/>&festival.id=' + festivalId, 500, 290);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加节日" style="width:80px" onclick="newFestival()">
	</div>
<%}%>
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td nowrap="nowrap" class="tdtitle">节日名称</td>
		<td nowrap="nowrap" class="tdtitle">放假时间</td>
		<td nowrap="nowrap" class="tdtitle">法定放假时间</td>
	</tr>
	<ext:iterate id="festival" indexId="festivalIndex" property="festivals">
		<tr onclick="openFestival('<ext:write name="festival" property="id"/>')" align="center">
			<td class="tdcontent" align="left"><ext:write name="festival" property="name"/></td>
			<td class="tdcontent"><ext:write name="festival" property="beginTime" format="yyyy-MM-dd HH:mm"/> ~ <ext:write name="festival" property="endTime" format="yyyy-MM-dd HH:mm"/></td>
			<td class="tdcontent"><ext:write name="festival" property="legalBeginTime" format="yyyy-MM-dd HH:mm"/> ~ <ext:write name="festival" property="legalEndTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</ext:iterate>
</table>