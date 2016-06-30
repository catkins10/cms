<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newMaterial() {
		if(document.getElementsByName("name")[0].value=="") {
			alert("办理事项名称不能为空");
			return;
		}
		DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemMaterial.shtml?id=<ext:write property="id"/>', 500, 260);
	}
	function openMaterial(materialId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemMaterial.shtml?id=<ext:write property="id"/>&material.id=' + materialId, 500, 260);
	}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加材料" style="width:80px" onclick="newMaterial()">
		<input type="button" class="button" value="调整优先级" onclick="DialogUtils.adjustPriority('cms/onlineservice', 'admin/onlineServiceMaterial', '申报材料', 600, 380, 'itemId=<ext:write property="id"/>')">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="100%">材料名称</td>
	</tr>
	<ext:iterate id="material" indexId="materialIndex" property="materials">
		<tr style="cursor:pointer;" align="center" valign="top">
			<td class="tdcontent" onclick="openMaterial('<ext:write name="material" property="id"/>')" align="center"><ext:writeNumber name="materialIndex" plus="1"/></td>
			<td class="tdcontent" onclick="openMaterial('<ext:write name="material" property="id"/>')" align="left"><ext:write name="material" property="title"/></td>
		</tr>
	</ext:iterate>
</table>