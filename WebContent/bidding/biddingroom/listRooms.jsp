<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table style="table-layout:fixed" class="view" width="100%" border="0" cellpadding="0" cellspacing="1">
	<col class="viewColEven"/>
	<tr align="center" bordercolor="E1E8F5">
		<td align="center" class="viewHeader" id="name">名称</td>
	</tr>
	<tr>
		<td class="viewData"><b>空闲<ext:write property="roomType"/>室:</b></td>
	</tr>
	<ext:iterate id="room" property="freeRooms">
		<tr id="<ext:write name="room" property="id"/>" onclick="onSelectData(this)" ondblclick="onDblClickData(this)">
			<td style="padding-left:10px" class="viewData"><ext:write name="room" property="name"/></td>
		</tr>
	</ext:iterate>
	<ext:equal value="false" property="freeRoomOnly">
		<tr>
			<td class="viewData"><b>全部<ext:write property="roomType"/>室:</b></td>
		</tr>
		<ext:iterate id="room" property="allRooms">
			<tr id="<ext:write name="room" property="id"/>" style="cursor:pointer" onclick="onSelectData(this)" ondblclick="onDblClickData(this)">
				<td style="padding-left:10px" class="viewData"><ext:write name="room" property="name"/></td>
			</tr>
		</ext:iterate>
	</ext:equal>
</table>