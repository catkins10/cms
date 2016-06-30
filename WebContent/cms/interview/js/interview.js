/*********** 重置发言表单,发言提交后调用 ***********/
function resetSpeakForm(oldSpeakId, newSpeakId) {
	var editor = HtmlEditor.getEditor('content');
	//清空正文
	editor.editorDocument.body.innerHTML = "";
	//修改图片、附件等的选择对话框链接
	editor.attachmentSelectorURL = editor.attachmentSelectorURL.replace('id=' + oldSpeakId, 'id=' + newSpeakId);
	//重置action
	DomUtils.getElement(document.forms['cms/interview/interviewSpeak'], "input", "id").value = newSpeakId;
	//切换校验码图片
	if(document.getElementById('validateCodeImage')) {
		document.getElementById('validateCodeImage').src = document.getElementById('validateCodeImage').src.split('?')[0] + '?reload=true&amp;seq=' + (new Date().getTime());
	}
	if(document.getElementsByName('validateCode')[0]) {
		document.getElementsByName('validateCode')[0].value = "";
	}
}

/*********** 更新发言列表 ***********/
function updateSpeaks() {
	if(!document.getElementById("updateSpeaksFrame")) {
		return;
	}
	//调用静态页面获取新的发言列表
	var pageName = document.getElementsByName("interviewPageName")[0].value;
	if(pageName=="interviewLive") { //匿名用户
		pageName = document.getElementsByName("liveSpeaksUpdateUrl")[0].value + Math.floor(Math.random() * 30) + "?seq=" + (new Date().getTime());
	}
	else {
		pageName = "liveUpdate.shtml?target=interviewLiveSpeaks&id=" + document.getElementsByName("subjectId")[0].value + "&siteId=" + document.getElementsByName("siteId")[0].value + "&pageName=" + pageName + "&seq=" + (new Date().getTime());
	}
	updateData(pageName, "updateSpeaksFrame");
}
function updateData(url, frameName) {
	var ajax = new Ajax();
	ajax.openRequest("GET", url, "", true, function() {
		var doc = window.frames[frameName].document;
		doc.open();
		doc.write(ajax.getResponseText());
		doc.close();
	});
}
function doUpdateSpeaks(previousSpeakId, newSpeaksCount, maxDisplay) {
	if(newSpeaksCount==0) {
		return;
	}
	var firstNewSpeak = getFirstRecord(frames['updateSpeaksFrame'].document, "speak"); //获取第一个新发言
	if(document.getElementById(firstNewSpeak.id)) { //检查记录是否已经在发言列表中
		return;
	}
	if(newSpeaksCount<maxDisplay && previousSpeakId!='0' && !document.getElementById("speak_" + previousSpeakId)) { //新的发言数量小于最大显示记录数,且最后的发言记录ID不在当前发言列表中
		//调用动态态页面获取新的发言列表
		var url = "liveUpdate.shtml?target=interviewLiveSpeaks&id=" + document.getElementsByName("subjectId")[0].value + "&siteId=" + document.getElementsByName("siteId")[0].value + "&pageName=" + document.getElementsByName("interviewPageName")[0].value + "&seq=" + (new Date().getTime());
		updateData(url, "updateSpeaksFrame");
		return;
	}
	//开始更新发言列表
	var firstSpeak = getFirstRecord(document, "speak"); //获取第一个发言
	var parentElement = document.getElementById("interviewLiveSpeaks");
	newSpeaksCount = 0;
	while(firstNewSpeak && (!firstNewSpeak.id || firstNewSpeak.id.indexOf("speak")==-1 || !document.getElementById(firstNewSpeak.id))) {
		copyElement(firstNewSpeak, firstSpeak, parentElement);
		if(firstNewSpeak.id && firstNewSpeak.id.indexOf("speak")!=-1) {
			newSpeaksCount++;
		}
		firstNewSpeak = firstNewSpeak.nextSibling;
	}
	//删除超出最大显示记录数的发言
	for(var i=0; i<maxDisplay-newSpeaksCount && firstSpeak; i++) {
		if(!firstSpeak.id || firstSpeak.id.indexOf("speak")==-1) {
			i--;
		}
		firstSpeak = firstSpeak.nextSibling;
	}
	while(firstSpeak) {
		var tempSpeak = firstSpeak.nextSibling;
		firstSpeak.parentNode.removeChild(firstSpeak);
		firstSpeak = tempSpeak;
	}
}
//查找第一条记录
function getFirstRecord(doc, type) {
	//获取span列表,检查是否存在指定类型的记录
	var spans = doc.getElementsByTagName("span");
	for(var i=0; i<spans.length; i++) {
		if(spans[i].id && spans[i].id.indexOf(type)==0) {
			return spans[i];
		}
	}
	var trs = doc.getElementsByTagName("tr");
	for(var i=0; i<trs.length; i++) {
		if(trs[i].id && trs[i].id.indexOf(type)==0) {
			return trs[i];
		}
	}
	return null;
}
function copyElement(newElement, oldElement, parentElement) { //拷贝对象
	if(newElement.tagName.toLowerCase()=="tr") { //表格
		parentElement = parentElement.getElementsByTagName("table")[0];
		var row = parentElement.insertRow(oldElement ? oldElement.rowIndex : 0);
		//拷贝属性
		copyAttributes(newElement, row);
		//拷贝单元格
		for(var i=0; i<newElement.cells.length; i++) {
			var cell = row.insertCell(i);
			copyAttributes(newElement.cells[i], cell);
			cell.innerHTML = newElement.cells[i].innerHTML;
		}
	}
	else {
		var obj = parentElement.ownerDocument.createElement(newElement.tagName);
		copyAttributes(newElement, obj);
		obj.innerHTML = newElement.innerHTML;
		if(!oldElement) {
			parentElement.appendChild(obj);
		}
		else {
			parentElement.insertBefore(obj, oldElement);
		}
	}
}
function copyAttributes(from, to) { //拷贝属性
	if(from.style.cssText) {
		to.style.cssText = from.style.cssText;
	}
	if(!from.attributes || from.attributes.length==0) {
		return;
	}
	for(var i= 0; i<from.attributes.length; i++) {
		var attribute = from.attributes[i];
		if(attribute.nodeValue) {
			to.setAttribute(attribute.nodeName, attribute.nodeValue);
		}
	}
}

/*********** 审核发言,更新需要审批的发言列表 ***********/
var isApprovalSpeak = false;
function approvalSpeak(speakId, pass) { //审核发言
	isApprovalSpeak = true;
	var reload = (location.href.indexOf("interviewLive.shtml")==-1); //不是直播页面
	var url = "approvalSpeak.shtml?id=" + speakId + "&pass=" + pass + "&subjectId=" + document.getElementsByName("subjectId")[0].value + "&siteId=" + document.getElementsByName("siteId")[0].value + "&pageName=" + document.getElementsByName("interviewPageName")[0].value + (reload ? "&reload=true" : "") + "&seq=" + (new Date().getTime());
	updateData(url, "approvalSpeaksFrame");
}
function updateToApprovalSpeaks() { //更新待审核发言
	if(isApprovalSpeak) { //正在审批发言
		return;
	}
	if(location.href.indexOf("interviewLive.shtml")==-1) {
		return;
	}
	if(lastMouseMoveTime!=-1 && (new Date().getTime() - lastMouseMoveTime)<20000) { //20秒钟内移动过
		return;
	}
	var iframe = document.getElementById("approvalSpeaksFrame");
	if(!iframe) {
		return;
	}
	var url = "liveUpdate.shtml?target=toApprovalSpeaks&id=" + document.getElementsByName("subjectId")[0].value + "&siteId=" + document.getElementsByName("siteId")[0].value + "&pageName=" + document.getElementsByName("interviewPageName")[0].value + "&seq=" + (new Date().getTime());
	updateData(url, "approvalSpeaksFrame");
}
function doUpdateToApprovalSpeaks() {
	isApprovalSpeak = false;
	document.getElementById("toApprovalSpeakList").innerHTML = frames["approvalSpeaksFrame"].document.body.innerHTML;
}
var lastMouseMoveTime = -1;
var lastMouseX = -1;
var lastMouseY = -1;
function onMouseMoveApprovalSpeaks(event) {
	if(lastMouseX==-1 || (Math.abs(event.clientX - lastMouseX)>2 && Math.abs(event.clientY - lastMouseY)>2)) {
		lastMouseMoveTime = new Date().getTime();
	}
	lastMouseX = event.clientX;
	lastMouseY = event.clientY;
}
function onMouseOutApprovalSpeaks(event) {
	lastMouseMoveTime = -1;
	lastMouseX = -1;
	lastMouseY = -1;
}

/*********** 更新访谈图片列表 ***********/
var timerCount = 0;
function updateImages() {
	timerCount++;
	if(timerCount % 5 != 0) { //3个周期更新一次
		return;
	}
	var iframe = document.getElementById("updateImagesFrame");
	if(!iframe) {
		return;
	}
	var pageName = document.getElementsByName("interviewPageName")[0].value;
	if(pageName=="interviewLive") { //匿名用户
		pageName = document.getElementsByName("liveImagesUpdateUrl")[0].value + "?seq=" + (new Date().getTime());
	}
	else {
		pageName = "liveUpdate.shtml?target=interviewImages&id=" + document.getElementsByName("subjectId")[0].value + "&siteId=" + document.getElementsByName("siteId")[0].value + "&pageName=" + pageName + "&seq=" + (new Date().getTime());
	}
	updateData(pageName, "updateImagesFrame");
}
function doUpdateImages() {
	document.getElementById("interviewImageList").innerHTML = frames["updateImagesFrame"].document.body.innerHTML;
}

window.setInterval('updateSpeaks();updateToApprovalSpeaks();updateImages();', 5000, 'javascript'); //5秒更新一次