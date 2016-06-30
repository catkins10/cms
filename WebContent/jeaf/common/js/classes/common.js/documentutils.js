DocumentUtils = function() {

};
//打开远程文档,onUploaded:回调函数,文本或者function(){}
DocumentUtils.openRemoteDocument = function(documentCommand, documentCommandParameter, onUploaded, actionName, actionParameter, formName) {
	var iframe = document.getElementById('iframeRemoteDocument');
	if(!iframe) {
		try {
			iframe = document.createElement('<iframe id="iframeRemoteDocument" name="iframeRemoteDocument"/>');
		}
		catch(e) {
			iframe = document.createElement('iframe');
			iframe.id = iframe.name = 'iframeRemoteDocument';
		}
		iframe.style.display = 'none';
		document.body.insertBefore(iframe, document.body.childNodes[0]);
	}
	window.lastRemoteDocument = {documentCommand:documentCommand, documentCommandParameter:documentCommandParameter, onUploaded:onUploaded, actionName:actionName, actionParameter:actionParameter, formName:formName};
	window.onRemoteDocumentReady = function(taskId, downloadDocumentURL, uploadPassportURL, uploadServerURL, documentApplicationName) { //远程文档准备完成
		DocumentUtils._onRemoteDocumentReady(taskId, downloadDocumentURL, uploadPassportURL, uploadServerURL, documentApplicationName);
	};
	window.onRemoteDocumentProcessError = function(error) { //打开远程文档时出错
		DocumentUtils._onRemoteDocumentProcessError(error);
	};
	window.onRemoteDocumentDownloadStart = function() {
		DocumentUtils._removeExtensionNotRunWarnTimer();
		DocumentUtils._checkStatus(300); //状态检查
	};
	window.onRemoteDocumentDownloading = function(totalSize, complete) { //文档下载中
		DocumentUtils._removeExtensionNotRunWarnTimer();
		var percent = Math.floor(complete / totalSize * 1000) / 10;
		PageUtils.showToast('正在下载, 已完成' + percent + '%...');
	};
	window.onRemoteDocumentOpening = function() { //文档打开中
		DocumentUtils._removeExtensionNotRunWarnTimer();
		PageUtils.showToast('正在打开文档, 请稍候...');
	};
	window.onRemoteDocumentOpened = function() { //文档已经打开
		DocumentUtils._removeExtensionNotRunWarnTimer();
		DocumentUtils._onRemoteDocumentOpened();
	};
	window.onRemoteDocumentClosed = function() { //文档已经关闭
		DocumentUtils._onRemoteDocumentClosed();
	};
	window.onfocus = function() { //窗口获取焦点后,检查是否上传完毕
		DocumentUtils._doCheckStatus();
	};
	window.onRemoteDocumentUploaded = function() { //文档上传完成
		DocumentUtils._onRemoteDocumentUploadCompleted();
	};
	var parameter = (actionParameter ? actionParameter + "&" : "") + "documentCommand=" + documentCommand + (documentCommandParameter ? "&documentCommandParameter=" + StringUtils.utf8Encode(documentCommandParameter) : "") ;
	if(FormUtils.doAction(actionName, parameter, true, formName, 'iframeRemoteDocument')) {
		PageUtils.showToast('正在下载, 请稍候...');
	}
};
//事件处理:远程文档准备完成
DocumentUtils._onRemoteDocumentReady = function(taskId, downloadDocumentURL, uploadPassportURL, uploadServerURL, documentApplicationName) {
	window.lastRemoteDocument.taskId = taskId;
	//下载文档
	var url = "http://127.0.0.1:50718/document?action=open" +
			  "&taskId=" + taskId +
			  "&downloadDocumentURL=" + StringUtils.utf8Encode(downloadDocumentURL) +
			  "&uploadPassportURL=" + StringUtils.utf8Encode(uploadPassportURL) +
			  "&uploadServerURL=" + StringUtils.utf8Encode(uploadServerURL) +
			  "&documentApplicationName=" + documentApplicationName +
			  "&seq=" + Math.random();
	ScriptUtils.appendJsFile(document, url, "scriptBorwserExtension");
	window.extensionNotRunWarnTimer = window.setTimeout(function() {
		PageUtils.showToast('');
		alert('下载文档时出错,请检查插件是否正确安装和运行');
	}, 5000);
};
//事件处理:打开远程文档时出错
DocumentUtils._onRemoteDocumentProcessError = function(error) {
	PageUtils.showToast('');
	DocumentUtils._stopCheckStatus();
	if(error=="NOSESSIONINFO") {
		LoginUtils.openLoginDialog(function() {
			DocumentUtils.openRemoteDocument(window.lastRemoteDocument.documentCommand, window.lastRemoteDocument.documentCommandParameter, window.lastRemoteDocument.onUploaded, window.lastRemoteDocument.actionName, window.lastRemoteDocument.actionParameter, window.lastRemoteDocument.formName);
		});
	}
	else if(error=="NOPRIVILEGE") {
		alert("没有权限");
	}
	else if(error=="ERROR") {
		alert("系统错误,请联系管理员");
	}
	else {
		alert(error);
	}
};
//事件处理:文档已经打开
DocumentUtils._onRemoteDocumentOpened = function() {
	PageUtils.showToast('');
	DocumentUtils._checkStatus(3000); //每隔3秒检查是否上传完成
};
//事件处理:文档已经关闭
DocumentUtils._onRemoteDocumentClosed = function() {
	PageUtils.showToast('');
	DocumentUtils._stopCheckStatus();
	window.lastRemoteDocument = null;
};
//事件处理:文档上传完成
DocumentUtils._onRemoteDocumentUploadCompleted = function() {
	DocumentUtils._stopCheckStatus();
	document.cookie = "RemoteDocumentTask=" + window.lastRemoteDocument.taskId + ";path=/";
	if(!window.lastRemoteDocument.onUploaded) {
		window.lastRemoteDocument = null;
		return;
	}
	if(typeof window.lastRemoteDocument.onUploaded == 'function') {
		window.lastRemoteDocument.onUploaded.call(null);
	}
	else {
		window.setTimeout(window.lastRemoteDocument.onUploaded, 1);
	}
	window.lastRemoteDocument = null;
};
//检查状态
DocumentUtils._checkStatus = function(interval) {
	DocumentUtils._stopCheckStatus();
	if(!window.lastRemoteDocument) {
		return;
	}
	window.documentTimer = window.setInterval(function() {
		DocumentUtils._doCheckStatus();
	}, interval);
};
//执行检查状态
DocumentUtils._doCheckStatus = function() {
	if(window.lastRemoteDocument) {
		ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/document?action=status&taskId=' + window.lastRemoteDocument.taskId + '&seq=' + Math.random(), "scriptBorwserExtension");	
	}
};
//停止检查状态
DocumentUtils._stopCheckStatus = function() {
	if(window.documentTimer) {
		window.clearInterval(window.documentTimer);
		window.documentTimer = null;
	}
};
//移除插件未运行警告
DocumentUtils._removeExtensionNotRunWarnTimer = function() {
	if(window.extensionNotRunWarnTimer) {
		window.clearTimeout(window.extensionNotRunWarnTimer);
		window.extensionNotRunWarnTimer = null;
	}
};