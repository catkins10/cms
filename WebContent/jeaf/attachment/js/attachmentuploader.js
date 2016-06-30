//附件上传类
AttachmentUploader = function(overwrite) {
	this.isUploading = false;
	this.overwrite = overwrite ? true : false;
};
//事件:文件开始上传,返回false时,取消上传,由调用者实现
AttachmentUploader.prototype.onUploadStart = function(filePath) {
	return true;
};
//事件:文件上传中
AttachmentUploader.prototype.onUploading = function(filePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime, percent) {
	
};
//事件:文件上传完成,attachments=[{name:[name], urlAttachment:[urlAttachment], urlInline:[urlInline], size:[size], sizeAsText:[sizeAsText]}, ...]
AttachmentUploader.prototype.onUploaded = function(attachmentNames, attachments) {
	
};
//事件:文件停止上传
AttachmentUploader.prototype.onStopped = function() {
	
};
//事件:文件上传错误
AttachmentUploader.prototype.onError = function(errorDescription) {

};
//创建文件上传帧
AttachmentUploader.prototype.createUploadFrame = function(attachmentUploadButton, attachmentUrl, attachmentTitle) {
	try {
		this.uploadFrame = document.createElement(document.all ? '<iframe onload="this.attachmentUploader._onUploadFrameLoad();"></iframe>' : 'iframe');
	}
	catch(e) {
		this.uploadFrame = document.createElement('iframe');
	}
	this.attachmentUrl = attachmentUrl;
	this.uploadFrame.uploadButton = attachmentUploadButton;
	this.uploadFrame.attachmentUploader = this;
	this.uploadFrame.src = attachmentUrl + (attachmentUrl.lastIndexOf("?")==-1 ? "?" : "&") + "attachmentSelector.uploadFrame=true";
	this.uploadFrame.title = attachmentTitle;
	this.uploadFrame.style.cssText = "position:absolute; width:0px; height:0px; border-style:none; filter:alpha(opacity=0); opacity:0;";
	this.uploadFrame.frameBorder = "0";
	this.uploadFrame.scrolling = "no";
	this.uploadFrame.setAttribute("allowtransparency", "true");
	this.uploadFrame.onload = function() {
		this.attachmentUploader._onUploadFrameLoad(this);
	};
	this.uploadFrame.onUploadAttachmentsProcessed = function(attachmentNames, attachments) { //已完成上传文件处理
		this.attachmentUploader.onUploaded(attachmentNames, attachments); //事件:文件上传完成
	};
	this.uploadFrame.onProcessUploadAttachmentsError = function(errorDescription) { //处理已上传文件时出错
		this.attachmentUploader._onProcessUploadAttachmentsError(errorDescription);
	};
	document.body.appendChild(this.uploadFrame);
	if(!AttachmentUploader.uploaders) {
		AttachmentUploader.uploaders = [];
		EventUtils.addEvent(window, "resize", function() {
			AttachmentUploader.adjustUploadFramePosition();
		});
		EventUtils.addEvent(document.body, "scroll", function() {
			AttachmentUploader.adjustUploadFramePosition();
		});
	}
	AttachmentUploader.uploaders.push(this); //添加列表
	this._doAdjustUploadFramePosition();
};
//上传附件
AttachmentUploader.prototype.uploadAttachment = function() {
	if(this.isUploading) { //正在上传
		return true;
	}
	this.isCanceled = false;
	this._loadFileUploadClient();
	var uploadPassportUrl = this.attachmentUrl + (this.attachmentUrl.indexOf('?')==-1 ? '?' : '&') + 'attachmentSelector.action=passport';
 	var filter = this.uploadFrame.contentWindow.document.getElementsByName("attachmentSelector.fileExtension")[0].value;
 	return this.fileUploadClient.selectFileToUpload('选取文件', filter, true, uploadPassportUrl, this.overwrite);
};
//是否可以取消上传
AttachmentUploader.prototype.isCancelable = function() {
	return this.fileUploadClient && this.fileUploadClient.isCancelable();
};
//取消上传
AttachmentUploader.prototype.cancelUpload = function() {
	this.isCanceled = true;
	this.isUploading = false;
	this.fileUploadClient.cancelUpload();
};
//上传帧加载完成
AttachmentUploader.prototype._onUploadFrameLoad = function() {
	var uploadFrame = this.uploadFrame;
	var body = uploadFrame.contentWindow.document.body;
	body.title = uploadFrame.title;
	body.style.borderStyle = "none";
	body.style.margin = 0;
	body.style.overflow = "hidden";
	body.style.backgroundColor = "transparent";
	body.style.padding = 0;
	try { body.style.filter = "alpha(opacity=0)"; } catch(e) { }
	try { body.style.opacity = "0"; } catch(e) { }
	body.onscroll = function() {
		return false;
	};
	body.onmouseover = function() {
		try{uploadFrame.uploadButton.onmouseover();}catch(e){}
	};
	body.onmouseout = function() {
		try{uploadFrame.uploadButton.onmouseout();}catch(e){}
	};
};
//调整上传帧位置
AttachmentUploader.adjustUploadFramePosition = function() {
	for(var i=0; i < (AttachmentUploader.uploaders ? AttachmentUploader.uploaders.length : 0); i++) {
		try { AttachmentUploader.uploaders[i]._doAdjustUploadFramePosition(); } catch(e) { }
	}
};
//执行调整上传帧位置
AttachmentUploader.prototype._doAdjustUploadFramePosition = function() {
	if(this.uploadFrame.uploadButton.offsetWidth==0 || this.uploadFrame.uploadButton.offsetHeight==0) {
		window.setTimeout(AttachmentUploader.adjustUploadFramePosition, 100);
		return;
	}
	var position = DomUtils.getAbsolutePosition(this.uploadFrame.uploadButton);
	this.uploadFrame.style.left = position.left + "px";
	this.uploadFrame.style.top = position.top + "px";
	this.uploadFrame.style.width = this.uploadFrame.uploadButton.offsetWidth + "px";
	this.uploadFrame.style.height = (this.uploadFrame.uploadButton.offsetHeight + 3) + "px";
};
AttachmentUploader.prototype._loadFileUploadClient = function() { //加载文件上传客户端
	if(this.fileUploadClient) {
		return this.fileUploadClient;
	}
	var attachmentUploader = this;
	this.fileUploadClient = new FileUploadClient(this.uploadFrame);
	this.fileUploadClient.onFileUploadStart = function(filePath) { //文件上传事件处理:文件校验,返回true才继续上传
		return attachmentUploader._validateFileType(filePath) && attachmentUploader.onUploadStart(filePath);
	};
	this.fileUploadClient.onFileUploaded = function(remoteFileNames) { //文件上传事件处理:上传完毕
		attachmentUploader.isUploading = false;
		attachmentUploader.remoteFileNames = remoteFileNames;
		attachmentUploader._processUploadAttachments();
	};
	this.fileUploadClient.onFileUploading = function(filePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime) { //文件上传控件事件处理:上传中
		if(attachmentUploader.isCanceled) {
			return;
		}
		attachmentUploader.isUploading = true;
		var percent = totalSize==-1 ? null : Math.floor(complete / totalSize * 1000) / 10;
		attachmentUploader.onUploading(filePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime, percent);
	};
	this.fileUploadClient.onFileUploadStopped = function() { //事件:停止上传
		attachmentUploader.isUploading = false;
		attachmentUploader.onStopped();
	};
	this.fileUploadClient.onFileUploadError = function(errorDescription) { //文件上传事件处理:上传错误
		attachmentUploader.isUploading = false;
		attachmentUploader.onError(errorDescription);
	};
};
AttachmentUploader.prototype._validateFileType = function(filePath) { //校验文件类型
	var fileExtension = this.uploadFrame.contentWindow.document.getElementsByName("attachmentSelector.fileExtension")[0].value;
	if(fileExtension=="" || fileExtension.indexOf("|*.*|")!=-1) {
		return true;
	}
	var index = filePath.lastIndexOf(".");
	if(index==-1) {
		alert("您选择的文件类型不正确，不允许上传。");
		return false;
	}
	var found = false;
	var extensions = fileExtension.split("|");
	var fileExt = "*" + filePath.substring(index).toLowerCase() + ";";
	for(var i=1; i<extensions.length && !found; i+=2) {
		found = (extensions[i] + ";").toLowerCase().indexOf(fileExt)!=-1;
	}
	if(!found) {
		alert("您选择的文件类型不正确，不允许上传。");
		return false;
	}
	return true;
};
AttachmentUploader.prototype._processUploadAttachments = function() { //处理上传的附件
	var uploadDocument = this.uploadFrame.contentWindow.document;
	uploadDocument.getElementsByName("attachmentSelector.lastUploadFiles")[0].value = this.remoteFileNames;
	uploadDocument.getElementsByName("attachmentSelector.action")[0].value = "processUploadFiles";
	uploadDocument.forms[0].action += this.uploadFrame.contentWindow.location.search;
	uploadDocument.forms[0].submit();
};
AttachmentUploader.prototype._onProcessUploadAttachmentsError = function(errorDescription) { //处理已上传文件时出错
	if("会话无效"!=errorDescription) {
		alert(errorDescription);
		return;
	}
	var attachmentUploader = this;
	LoginUtils.openLoginDialog(function() { //重新登录
		attachmentUploader._processUploadAttachments(); //重新处理上传的文件
	});
};