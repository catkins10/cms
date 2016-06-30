<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<link href="<%=request.getContextPath()%>/jeaf/tree/css/tree.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
<script src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
<script>
	MAX_TREE_NODES = 20000;
	//添加条目
	function appendItem(tree, nodeId, directoryId, directoryName, importDataName) {
		var item = document.createElement("div");
		item.id = directoryId;
		item.innerHTML = directoryName;
		item.style.cursor = "pointer";
		item.title = "双击取消关联";
		item.ondblclick = function() {
			updateTargetDirectory(tree, nodeId, directoryId, directoryName, importDataName, false);
			item.parentNode.removeChild(item);
		};
		document.getElementById("targetDirectory_" + importDataName).appendChild(item);
		item.focus();
	}
	//更新或删除目标目录
	function updateTargetDirectory(tree, nodeId, directoryId, directoryName, importDataName, isAppend) {
		var idsField = getTargetDirectoryField(nodeId, "targetDirectoryIds", false);
		var namesField = getTargetDirectoryField(nodeId, "targetDirectoryNames", false);
		if(!idsField) {
			var node = tree.getNodeById(nodeId);
			idsField = getTargetDirectoryField(nodeId, "targetDirectoryIds", true);
			namesField = getTargetDirectoryField(nodeId, "targetDirectoryNames", true);
			idsField.value = tree.getNodeAttribute(node, "targetDirectoryIds") || "";
			namesField.value = tree.getNodeAttribute(node, "targetDirectoryNames") || "";
			getTargetDirectoryField(nodeId, "copyChildDirectory", true).value = tree.getNodeAttribute(node, "copyChildDirectory");
		}
		var targetDirectoryIds = idsField.value;
		var targetDirectoryNames = namesField.value;
		if(isAppend) { //添加新的目标目录
			if(("," + targetDirectoryIds + ",").indexOf("," + directoryId + ",")!=-1) {
				return;
			}
			targetDirectoryIds = (targetDirectoryIds && targetDirectoryIds!="" ? targetDirectoryIds + "," : "") + directoryId;
			targetDirectoryNames = (targetDirectoryNames && targetDirectoryNames!="" ? targetDirectoryNames + "," : "") + directoryName;
			appendItem(tree, nodeId, directoryId, directoryName, importDataName);
		}
		else { //删除目标目录
			targetDirectoryIds = ("," + targetDirectoryIds + ",").replace(directoryId + ",", "");
			targetDirectoryNames = ("," + targetDirectoryNames + ",").replace(directoryName + ",", "");
			targetDirectoryIds = targetDirectoryIds.substring(1);
			targetDirectoryNames = targetDirectoryNames.substring(1);
			if(targetDirectoryIds.substring(targetDirectoryIds.length - 1)==",") {
				targetDirectoryIds = targetDirectoryIds.substring(0, targetDirectoryIds.length - 1);
				targetDirectoryNames = targetDirectoryNames.substring(0, targetDirectoryNames.length - 1);
			}
		}
		idsField.value = targetDirectoryIds;
		namesField.value = targetDirectoryNames;
	}
	//设置是否需要拷贝子目录
	function setCopyChildDirectory(importDataIndex, copy, importDataName) {
		var directoryId = document.getElementById('sourceDirectoryName_' + importDataName).title;
		if(directoryId && directoryId!="") {
			var copyChildDirectoryField = getTargetDirectoryField(directoryId, "copyChildDirectory", false);
			if(!copyChildDirectoryField) {
				var node = window.trees[importDataIndex*2].getNodeById(directoryId); //获取源目录树节点
				getTargetDirectoryField(directoryId, "targetDirectoryIds", true).value = window.trees[importDataIndex*2].getNodeAttribute(node, "targetDirectoryIds");
				var namesField = getTargetDirectoryField(directoryId, "targetDirectoryNames", true).value = window.trees[importDataIndex*2].getNodeAttribute(node, "targetDirectoryNames");
				copyChildDirectoryField = getTargetDirectoryField(directoryId, "copyChildDirectory", true);
			}
			copyChildDirectoryField.value = copy;
		}
	}
	//获取字段
	function getTargetDirectoryField(nodeId, fieldName, createIfNotExist) {
		var field = document.getElementsByName(fieldName + "_" + nodeId)[0];
		if(!field && createIfNotExist) {
			try {
				field = document.createElement('<input type="hidden" name="' + fieldName + "_" + nodeId + '"/>');
			}
			catch(e) {
				field = document.createElement('input');
				field.type = "hidden";
				field.name = fieldName + "_" + nodeId;
			}
			document.forms[0].appendChild(field);
		}
		return field;
	}
	function getTreeInfo(tree) {
		var values = tree.parentElement.id.split("_");
		return {importDataName: values[1], isSourceTree: values[0]=="sourceTree"};
	}
</script>
<ext:iterate id="dataDirectoryTree" property="dataDirectoryTrees" indexId="dataDirectoryTreeIndex">
<%	com.yuanluesoft.jeaf.dataimport.model.DataDirectoryTree dataDirectoryTree = (com.yuanluesoft.jeaf.dataimport.model.DataDirectoryTree)pageContext.getAttribute("dataDirectoryTree");%>
	<div style="float:left; padding-right: 20px">
		<b><ext:write name="dataDirectoryTree" property="name"/>导入配置：</b>
		<table border="0" cellpadding="3" cellspacing="0" height="390px">
			<tr>
				<td rowspan="2">
					<div id="sourceTree_<ext:write name="dataDirectoryTree" property="name"/>" style="overflow:auto; width:260px; height:100%; border:gray 1 solid"></div>
				</td>
				<td style="height:80px" valign="top">
					目标目录<span id="sourceDirectoryName_<ext:write name="dataDirectoryTree" property="name"/>"></span>：
					<div id="targetDirectory_<ext:write name="dataDirectoryTree" property="name"/>" style="margin-bottom:3px;overflow:auto; width:260px; height:65px; border:gray 1 solid; padding:3px"></div>
					<input onclick="setCopyChildDirectory(<ext:write name="dataDirectoryTreeIndex"/>, checked, name.split('_')[1])" name="copyChildDirectory_<ext:write name="dataDirectoryTree" property="name"/>" type="checkbox" class="checkbox">&nbsp;拷贝子目录
				</td>
			</tr>
			<tr>
				<td>
					<div id="targetTree_<ext:write name="dataDirectoryTree" property="name"/>" style="overflow:auto; width:260px; height:100%; border:gray 1 solid"></div>
				</td>
			</tr>
		</table>
	</div>
	<ext:tree name="dataDirectoryTree" property="sourceTree" parentElementId="<%="sourceTree_" + dataDirectoryTree.getName()%>"/>
	<ext:tree name="dataDirectoryTree" property="targetTree" parentElementId="<%="targetTree_" + dataDirectoryTree.getName()%>"/>
</ext:iterate>
<script>
	for(var k=0; k<window.trees.length; k++) {
		//事件:节点被选中
		window.trees[k].onNodeSeleced = function(nodeId, nodeText, nodeType, leafNode) {
			var treeInfo = getTreeInfo(this);
			if(!treeInfo.isSourceTree) {
				return;
			}
			var span = document.getElementById('sourceDirectoryName_' + treeInfo.importDataName);
			span.innerHTML = "(" + this.getSelectedNodeFullText("/") + ")";
			span.title = nodeId;
			var targetDirectoryIds = this.getNodeAttribute(this.getSelectedNode(), "targetDirectoryIds");
			var targetDirectoryNames = this.getNodeAttribute(this.getSelectedNode(), "targetDirectoryNames");
			var copyChildDirectory = this.getNodeAttribute(this.getSelectedNode(), "copyChildDirectory");
			var idsField = getTargetDirectoryField(nodeId, "targetDirectoryIds", false);
			if(idsField) {
				targetDirectoryIds = idsField.value;
				targetDirectoryNames = getTargetDirectoryField(nodeId, "targetDirectoryNames", false).value;
				copyChildDirectory = getTargetDirectoryField(nodeId, "copyChildDirectory", false).value;
			}
			//设置是否拷贝子目录
			document.getElementsByName("copyChildDirectory_" + treeInfo.importDataName)[0].checked = ("true"==copyChildDirectory);
			//设置目标目录列表
			var div = document.getElementById("targetDirectory_" + treeInfo.importDataName);
			div.innerHTML = "";
			if(!targetDirectoryIds || targetDirectoryIds=="") {
				return;
			}
			var ids = targetDirectoryIds.split(",");
			var names = targetDirectoryNames.split(",");
			for(var i=0; i<ids.length; i++) {
				appendItem(window.trees[this.treeIndex], nodeId, ids[i], names[i], treeInfo.importDataName);
			}
		};
		//事件:节点被双击
		window.trees[k].onDblClickNode = function(nodeId, nodeText, nodeType, leafNode) {
			var treeInfo = getTreeInfo(this);
			if(treeInfo.isSourceTree) {
				return;
			}
			var directoryId = document.getElementById('sourceDirectoryName_' + treeInfo.importDataName).title;
			if(directoryId && directoryId!="") {
				updateTargetDirectory(window.trees[this.treeIndex - 1], directoryId, nodeId, this.getSelectedNodeFullText("/"), treeInfo.importDataName, true);
			}
		};
		//事件:获取URL,以得到子节点列表
		window.trees[k].onGetUrlForListChildNodes = function(nodeId, nodeType) {
			var treeInfo = getTreeInfo(this);
			return 'listChildNodes.shtml?parentNodeId=' + StringUtils.utf8Encode(nodeId) + "&importDataName=" + StringUtils.utf8Encode(treeInfo.importDataName) + "&sourceTree=" + (treeInfo.isSourceTree) + "&targetSiteId=" + document.getElementsByName('targetSiteId')[0].value + "&dataImportServiceClass=" + document.getElementsByName('dataImportServiceClass')[0].value;
		};
	}
</script>
<div style="width:100%; float:left">
	<br><br>
	<input class="button" onclick="submit()" value="导入" type="button">
	<input class="button" onclick="FormUtils.doAction('saveDataTree')" value="保存导入设置" type="button">
</div>