//类:WEBIM对话窗口
WebimChat = function(chatId) {
	this.chatId = chatId;
	this.loading = true;
	window.webimChat = this;
	this.webim = parent.frames['frameWebim'].webim;
	new WebimChatEditor(this.webim, this); //加载录入对话框
	//绑定事件
	if(document.all) {
		window.attachEvent('onresize', window.webimChat.onresize);
	}
	else {
		window.addEventListener("resize", window.webimChat.onresize, false);
	}
	var onChatWindowLoaded = function() {
		window.webimChat.loading = false;
		//创建发送文件FRAME
		var sendFileAction = document.getElementById("sendFileAction");
		if(sendFileAction) {
			var url = "../attachmentEditor.shtml?act=create&id=" + chatId + "&attachmentSelector.field=attachments&attachmentSelector.type=attachments";
			var attachmentUploader = new AttachmentUploader();
			attachmentUploader.createUploadFrame(sendFileAction, url, "发送文件");
			attachmentUploader.onUploaded = function(attachmentNames, attachments) { //事件:文件上传完成
				window.webimChat.fileUploaded("attachments", attachments);
			};
			attachmentUploader.onUploading = function(filePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime, percent) {
				window.webimChat.showUploading();
			};
			attachmentUploader.onError =  function(errorDescription) { //事件:文件上传错误
				window.webimChat.showUploadError(errorDescription);
			};
		}
		//创建发送图片FRAME
		var sendImageAction = document.getElementById("sendImageAction");
		if(sendImageAction) {
			var url = "../attachmentEditor.shtml?act=create&id=" + chatId + "&attachmentSelector.field=images&attachmentSelector.type=images";
			var attachmentUploader = new AttachmentUploader();
			attachmentUploader.createUploadFrame(sendImageAction, url, "发送图片");
			attachmentUploader.onUploaded = function(attachmentNames, attachments) { //事件:文件上传完成
				window.webimChat.fileUploaded("images", attachments);
			};
			attachmentUploader.onUploading = function(filePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime, percent) {
				window.webimChat.showUploading();
			};
			attachmentUploader.onError =  function(errorDescription) { //事件:文件上传错误
				window.webimChat.showUploadError(errorDescription);
			};
		}
	};
	EventUtils.addEvent(window, "load", onChatWindowLoaded);
	//设置字体
	var fontName = document.getElementsByName("fontName")[0];
	if(fontName) {
		fontName.value = this.webim.webimSetting.fontName;
	}
	var fontSize = document.getElementsByName("fontSize")[0];
	if(fontSize) {
		DropdownField.setValue("fontSize", this.webim.webimSetting.getFontSize());
	}
	var fontColor = document.getElementsByName("fontColor")[0];
	if(fontColor) {
		fontColor.value = this.webim.webimSetting.fontColor;
	}
	//给firefox创建div用来显示列表
	if(!document.all) {
		var talks = document.getElementById("talks");
		var div = document.createElement("div");
		div.style.margin = "5px";
		div.id = "talksDiv";
		div.appendChild(talks.cloneNode(true));
		talks.parentNode.replaceChild(div, talks);
	}
	this.onresize();
};

//设置发送文件和图片的IFRAME位置
WebimChat.prototype.onresize = function() {
	AttachmentUploader.adjustUploadFramePosition();
	if(document.all) {
		return;
	}
	var talksDiv = document.getElementById("talksDiv");
	if(talksDiv) {
		talksDiv.style.width = (talksDiv.parentNode.clientWidth - 10) + "px";
	}
};

//输出收到的发言
WebimChat.prototype.writeTalk = function(talkHtml) {
	var talks = document.getElementById("talks");
	//记录上级滚动区域的尺寸
	var size = null;
	for(var parentNode = talks.parentNode; parentNode.tagName.toLowerCase()!="body"; parentNode=parentNode.parentNode) {
		if(parentNode.scrollHeight && parentNode.clientHeight && parentNode.clientHeight>0 && parentNode.scrollHeight>parentNode.clientHeight) {
			size = {node: parentNode, clientHeight: parentNode.clientHeight, scrollHeight: parentNode.scrollHeight, scrollTop: parentNode.scrollTop};
			break;
		}
	}
	//调整上级滚动区域的滚动位置
	var webimChat = this;
	var onAfterWriteTalk = function() {
		for(var parentNode = talks.parentNode; parentNode.tagName.toLowerCase()!="body"; parentNode=parentNode.parentNode) {
			if(parentNode.scrollHeight && parentNode.clientHeight && parentNode.clientHeight>0 && parentNode.scrollHeight>parentNode.clientHeight) {
				if(size==null || size.node!=parentNode || size.scrollHeight-size.scrollTop-size.clientHeight<=3) {
					parentNode.scrollTop = parentNode.scrollHeight - parentNode.clientHeight;
				}
				break;
			}
		}
		webimChat.onresize();
		frames["content"].document.body.focus();
	};
   	//输出发言
   	var span = document.createElement("span");
	span.innerHTML = talkHtml;
	for(var node=span.childNodes[0].firstChild; node!=null;) {
		var nextSibling = node.nextSibling;
		node = talks.appendChild(node);
		//给img添加onload事件处理
		if(node.tagName) {
			if(node.tagName.toLowerCase()=="img") {
				node.onload = function() {
					onAfterWriteTalk.call();
					this.onload = function() { //清除事件处理,避免重复调用
					
					};
				};
			}
			else {
				var images = node.getElementsByTagName("img");
				for(var i=0; i<images.length; i++) {
					images[i].onload = function() {
						onAfterWriteTalk.call();
						this.onload = function() { //清除事件处理,避免重复调用
						
						};
					};
				}
			}
		}
		node = nextSibling;
	}
	onAfterWriteTalk.call();
};

//插入表情
WebimChat.prototype.insertExpressionImage = function(event) {
	if(!event) {
		event = window.event;
	}
	if(event.srcElement.tagName.toLowerCase()=="img") { //图片
		window.webimChatEditor.insertImage(event.srcElement.src, event.srcElement.alt); //输出图片
		this.showBox("expressionBox", true); //隐藏表情框
	}
};
//显示文件上传进度
WebimChat.prototype.showUploading = function() {
	var fileUploadingBox = document.getElementById("fileUploadingBox");
	if(fileUploadingBox) {
		fileUploadingBox.style.display = "";
	}
};
//文件上传完成
WebimChat.prototype.fileUploaded = function(type, attachments) {
	var fileUploadingBox = document.getElementById("fileUploadingBox");
	if(fileUploadingBox) {
		fileUploadingBox.style.display = "none";
	}
	for(var i = 0; i < attachments.length; i++) {
		if(type=="images") { //发送图片
			window.webimChatEditor.insertImage(attachments[i].urlInline, attachments[i].name);
		}
		else { //发送文件
			this.webim.submitTalk(this.chatId, '发送文件：<a href="' + attachments[i].urlAttachment + '" target="_blank">' + attachments[i].name + '(' + attachments[i].sizeAsText + ')</a>');
		}
	}
};
//显示文件上传错误
WebimChat.prototype.showUploadError = function(error) {
	var fileUploadingBox = document.getElementById("fileUploadingBox");
	if(fileUploadingBox) {
		fileUploadingBox.style.display = "none";
	}
	alert(error);
};
//显示或隐藏字体/表情框
WebimChat.prototype.showBox = function(boxId, hide) {
	var boxIds = ['expressionBox', 'fontSettingBox'];
	for(var i = 0; i < boxIds.length; i++) {
		var box = document.getElementById(boxIds[i]);
		box.style.display = boxIds[i]!=boxId || hide ? 'none' : (box.style.display=='none' ? '' : 'none');
		if(box.tagName=='TD') {
			box.parentNode.style.display = box.style.display;
		}
	}
};
//设置字体名称
WebimChat.prototype.setChatFontName = function(fontName) {
	this.webim.webimSetting.setPropertyValue("fontName", fontName);
	window.webimChatEditor.editorDocument.body.style.fontFamily = fontName;
	window.webimChatEditor.resizeEditor();
};

//设置字体大小
WebimChat.prototype.setChatFontSize = function(fontSize) {
	this.webim.webimSetting.setPropertyValue("fontSize", fontSize);
	window.webimChatEditor.editorDocument.body.style.fontSize = this.webim.webimSetting.getFontSize() + "px";
	window.webimChatEditor.resizeEditor();
};

//设置字体颜色
WebimChat.prototype.setChatFontColor = function(fontColor) {
	this.webim.webimSetting.setPropertyValue("fontColor", fontColor);
	window.webimChatEditor.editorDocument.body.style.color = fontColor;
};

//添加对话用户
WebimChat.prototype.addChatPerson = function(fontColor) {
	var frameSelect = document.createElement('iframe');
	frameSelect.frameBorder = 0;
	frameSelect.id = frameSelect.name = "selectPersonFrame";
	frameSelect.scrolling = "no";
	if(document.all) {
		frameSelect.style.left = ((document.body.offsetWidth - document.body.clientWidth) / -2) + "px";
		frameSelect.style.top = ((document.body.offsetHeight - document.body.clientHeight) / -2) + "px";
		frameSelect.style.width = document.body.offsetWidth + "px";
		frameSelect.style.height = document.body.offsetHeight + "px";
	}
	else {
		frameSelect.style.left = "0px";
		frameSelect.style.top = "0px";
		frameSelect.style.width = "100%";
		frameSelect.style.height = "100%";
	}
	frameSelect.style.position = "absolute";
	frameSelect.allowTransparency = "true";
	frameSelect.src = "selectPerson.shtml?script=" + StringUtils.utf8Encode("window.webimChat.webim.createGroupChat('" + this.chatId + "', '{PERSONIDS}')");
	document.body.appendChild(frameSelect);
};

//设置发言内容
WebimChat.prototype.setTalkContent = function(talkContent) {
	window.webimChatEditor.editorDocument.body.innerHTML = talkContent;
	window.webimChatEditor.editorDocument.body.focus();
};

//类:WEBIM对话编辑器
WebimChatEditor = function(webim, webimChat) {
	window.webimChatEditor = this;
	this.webim = webim;
	this.webimChat = webimChat;
	this.editorDocument = frames["content"].document;
	this.load();
};

//加载编辑器
WebimChatEditor.prototype.load = function() {
	var html = '<html>' +
			   '	<head>' +
			   '		<style type="text/css">' +
			   '			.editor {' +
			   '				font-family:' + this.webim.webimSetting.fontName + ';' +
			   '				font-size:' + this.webim.webimSetting.getFontSize() + 'px;' +
			   '				color:' + this.webim.webimSetting.fontColor + ';' +
			   '			}' +
			   '		</style>' +
			   '		<link href="css/webimChatEditor.css" type="text/css" rel="stylesheet">' +
			   '	</head>' +
			   '	<body contentEditable="true" class="editor" onload="parent.webimChatEditor.onLoad()" onresize="parent.webimChatEditor.resizeEditor()" onkeydown="parent.webimChatEditor.resizeEditor()" onkeyup="parent.webimChatEditor.resizeEditor()" onkeypress="return parent.webimChatEditor.onKeyPress(event)" onpaste="parent.webimChatEditor.resizeEditor()" oncut="parent.webimChatEditor.resizeEditor()">' +
			   '	</body>' +
			   '</html>';
	this.editorDocument.open();
	this.editorDocument.write(html);
	this.editorDocument.close();
};

//加载
WebimChatEditor.prototype.onLoad = function() {
	this.resizeEditor();
	this.editorDocument.body.focus();
};

//按键处理
WebimChatEditor.prototype.onKeyPress = function(event) {
  	if(!event) {
   		event = window.event;
   	}
   	var keyCode = event.which ? event.which : event.keyCode;
   	if(event.ctrlKey && keyCode==13) { //firefox
   		keyCode = 10;
   	}
   	if((!event.charCode || event.charCode!=100) && !event.shiftKey && keyCode==(this.webim.webimSetting.ctrlSend ? 10 : 13)) {
   		this.submitTalk()
   		return false;
   	}
   	if(keyCode==10) { //CTRL+回车,转换为回车
    	if(event.which) {
	    	try {
	   			var evt = this.editorDocument.createEvent("KeyboardEvent");
				evt.initKeyEvent("keypress", true, true, null, false, false, false, false, 13, 100);
				this.editorDocument.body.dispatchEvent(evt);
			}
			catch(e) { //不支持initEvent
				var range = this.editorDocument.getSelection().getRangeAt(0);
	    		range.surroundContents(this.editorDocument.createElement("br"));
			}
	    }
	   	else {
	    	event.keyCode = 13;
	   	}
    }
   	this.resizeEditor();
   	return true;
};

//发送发言
WebimChatEditor.prototype.submitTalk = function() {
   	var content = this.editorDocument.body.innerHTML;
   	if(this.editorDocument.getElementsByTagName("img").length==0 && content.replace(/<.*?>/g, "").replace(/&nbsp;/g, "").replace(/[\r\n\t ]/g, "")=="") {
   		return;
   	}
   	if(this.webim.submitTalk(this.webimChat.chatId, content)) {
   		this.load(); //重新加载输入框
   	}
};

//设置编辑器大小
WebimChatEditor.prototype.resizeEditor = function() {
	var fontSize = this.webim.webimSetting.getFontSize();
	var html;
	var height = (!document.all && ((html = this.editorDocument.body.innerHTML.trim().toLowerCase())=="" || html=="<br>" || html=="<br/>") ? 0 : this.editorDocument.body.scrollHeight);
	height = Math.max(height, fontSize + 6);
	var fullHeight = document.body.clientHeight;
	document.getElementById("content").style.height = Math.min(height, fullHeight * 0.25) + "px";
	this.editorDocument.body.style.overflow = (height > fullHeight * 0.25 ? "auto" : "hidden");
	this.webimChat.onresize(); //调整附件和图片IFRAME位置
	this.webim.setActive(); //通知WEBIM,当前正在输入
};

//插入图片
WebimChatEditor.prototype.insertImage = function(imgSrc, imgAlt) {
	var index = imgSrc.indexOf("://");
	if(index!=-1) {
		imgSrc = imgSrc.substring(imgSrc.indexOf("/", index + 3));
	}
	this.editorDocument.body.focus();
	this.editorDocument.execCommand("InsertImage", false, imgSrc + "#tempflag");
	var imgs = this.editorDocument.getElementsByTagName("img");
	var img;
	for(var i=0; i<imgs.length; i++) {
		if(imgs[i].src.indexOf("tempflag")!=-1) {
			img = imgs[i];
			break;
		}
	}
	img.onload = function() {
		if(img.width>220) {
			img.width = "180";
			img.title = '点击放大';
			img.setAttribute("onclick", "this.removeAttribute('width')");
		}
		window.webimChatEditor.resizeEditor();
	};
	img.src = imgSrc;
	img.title = img.alt = imgAlt;
	this.editorDocument.body.focus();
};