/*
//类型校验,由继承者实现
function validTargetType(node);
*/

//初始化数据,从dialog.js继承
function initData() {
	
}
//获取当前记录,从dialog.js继承
function getCurrentData() {
	return window.tree.getSelectedNode();
}
//获取选中的记录列表,从dialog.js继承
function getSelectedData() {
	return null;
}
//获取全部的记录列表,全选时调用,从dialog.js继承
function getAllData() {
	//获取当前节点的子节点
	return window.tree.getChildNodes(window.tree.getSelectedNodeId());
}
//获取字段值,从dialog.js继承
function getDataFieldValue(data, fieldName) {
	var value = window.tree.getNodeAttribute(data, fieldName);
	if(value && value!="") {
		return value;
	}
	else if(fieldName=="name") {
		return window.tree.getNodeAttribute(data, "text");
	}
	else if(fieldName.indexOf("fullName")==0) {
		return window.tree.getNodeAttribute(data, "fullText" + fieldName.substring("fullName".length));
	}
	return "";
}
//焦点移动到下一个记录,从dialog.js继承
function moveToNext() {
	var nextNode = window.tree.getNextSibling();
	if(nextNode) {
		window.tree.selectNodeById(nextNode.id);
		nextNode.focus();
	}
}
//重置记录列表,和getSelectedData对应,从dialog.js继承
function resetData() {

}
//脚本搜索树形对话框内容
function query(){
	var key = document.getElementsByName('key')[0].value;
	if(key==''){ 
		return false; 
	} 
	if(/^[a-z]+$/.test(key)){   //纯字母比较节点拼音
		//TODO
	}
	if(!window.tree.getSelectedNode()) {
		window.tree.selectNode(window.tree.getRootNode());
	}
	window.tree.searchTreeNodeByKey(key);
	return false;
}