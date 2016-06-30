<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<link href="<%=request.getContextPath()%>/jeaf/tree/css/tree.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
<div style="padding-bottom:5px">
	<input type="button" class="button" value="添加分组" onclick="newItem(0)">
	<input type="button" class="button" value="添加链接" onclick="newItem(1)">
	<input type="button" class="button" value="添加事件" onclick="newItem(2)">
</div>
<div id="divTree" align="left" style="overflow:auto; height:300px; width:100%; border: #cccccc 1px solid; background-color: #ffffff; padding: 5px 5px 5px 5px"></div>
<ext:tree property="menuTree" parentElementId="divTree"/>
<script>
	//事件:节点被双击
	window.tree.onDblClickNode = function(nodeId, nodeText, nodeType, leafNode) {
		if(nodeId!="0") {
			PageUtils.editrecord('wechat', 'menuItem', '<ext:write property="id"/>', 'mode=dialog,width=360,height=200', '', '&menuItem.id=' + nodeId);
		}
	};
	function newItem(itemType) {
		var node = window.tree.getSelectedNode();
		var nodeType = node ? window.tree.getNodeAttribute(node, "type") : "root";
		if(nodeType=="link" || nodeType=="event") {
			return;
		}
		if(itemType==0 && nodeType=="folder") {
			return; //不支持两级菜单
		}
		PageUtils.newrecord('wechat', 'menuItem', 'mode=dialog,width=360,height=200', 'id=<ext:write property="id"/>&menuItem.type=' + itemType + '&menuItem.parentItemId=' + (node ? window.tree.getNodeAttribute(node, "id") : 0));
	}
</script>