//类:WEBIM对话窗口
Chat = function(chatId) {
	this.chatId = chatId;
	this.loading = true;
	window.chat = this;
	new ChatEditor(this); //加载录入对话框
	//绑定事件
	if(document.all) {
		window.attachEvent('onresize', window.chat.onresize);
	}
	else {
		window.addEventListener("resize", window.chat.onresize, false);
	}
	window.onload = function() {
		window.chat.loading = false;
	};
	/*/设置字体
	var fontName = document.getElementsByName("fontName")[0];
	if(fontName) {
		fontName.value = this.webim.webimSetting.fontName;
	}
	var fontSize = document.getElementsByName("fontSize")[0];
	if(fontSize) {
		fontSize.value = this.webim.webimSetting.fontSize;
	}
	var fontColor = document.getElementsByName("fontColor")[0];
	if(fontColor) {
		fontColor.value = this.webim.webimSetting.fontColor;
	}
	*/
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
Chat.prototype.onresize = function() {
	if(!document.all) {
		var talksDiv = document.getElementById("talksDiv");
		talksDiv.style.width = (talksDiv.parentNode.clientWidth - 10) + "px";
	}
};

//输出收到的发言
Chat.prototype.writeTalk = function(talkHtml) {
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
	var chat = this;
	var onAfterWriteTalk = function() {
		for(var parentNode = talks.parentNode; parentNode.tagName.toLowerCase()!="body"; parentNode=parentNode.parentNode) {
			if(parentNode.scrollHeight && parentNode.clientHeight && parentNode.clientHeight>0 && parentNode.scrollHeight>parentNode.clientHeight) {
				if(size==null || size.node!=parentNode || size.scrollHeight-size.scrollTop-size.clientHeight<=3) {
					parentNode.scrollTop = parentNode.scrollHeight - parentNode.clientHeight;
				}
				break;
			}
		}
		chat.onresize();
		//frames["content"].document.body.focus();
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
Chat.prototype.insertExpressionImage = function(event) {
	if(!event) {
		event = window.event;
	}
	if(event.srcElement.tagName.toLowerCase()=="img") { //图片
		//输出图片
		window.chatEditor.insertImage(event.srcElement.src, event.srcElement.alt);
		//隐藏表情框
		document.getElementById("expressionBox").style.display = "none";
	}
};

//显示字体/表情框
Chat.prototype.showBox = function(boxId, hide) {
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
Chat.prototype.setChatFontName = function(fontName) {
	//this.webim.webimSetting.setPropertyValue("fontName", fontName);
	window.chatEditor.editorDocument.body.style.fontFamily = fontName;
	window.chatEditor.resizeEditor();
};

//设置字体大小
Chat.prototype.setChatFontSize = function(fontSize) {
	//this.webim.webimSetting.setPropertyValue("fontSize", fontSize);
	//window.chatEditor.editorDocument.body.style.fontSize = this.webim.webimSetting.getFontSizePixel() + "px";
	window.chatEditor.resizeEditor();
};

//设置字体颜色
Chat.prototype.setChatFontColor = function(fontColor) {
	//this.webim.webimSetting.setPropertyValue("fontColor", fontColor);
	window.chatEditor.editorDocument.body.style.color = fontColor;
};

//设置发言内容
Chat.prototype.setTalkContent = function(talkContent) {
	window.chatEditor.editorDocument.body.innerHTML = talkContent;
	window.chatEditor.editorDocument.body.focus();
};

//提交发言
Chat.prototype.submitTalk = function() {
   	var content = window.chatEditor.editorDocument.body.innerHTML;
   	if(window.chatEditor.editorDocument.getElementsByTagName("img").length==0 && content.replace(/<.*?>/g, "").replace(/&nbsp;/g, "").replace(/[\r\n\t ]/g, "")=="") {
   		return;
   	}
   	content = '<font style="font-family:' + 'simsun' + ';' + //this.webimSetting.fontName
			  ' font-size:' + 12 + 'px;' + //this.webimSetting.getFontSizePixel()
			  ' color:' + '#000000' + ';' + //this.webimSetting.fontColor
			  ' line-height:' + Math.ceil(12 * 1.2) + 'px;' + //this.webimSetting.getFontSizePixel()
			  '">' + content + '</font>';
	if(content.length>2000) {
		alert('您输入的内容过长，不允许提交。');
		return false;
	}
	//写入hidden域
	var talkContent = document.getElementById("talkContent");
	if(!talkContent) {
		talkContent = document.createElement('<input type="hidden" id="talkContent"/>');
		document.body.insertBefore(talkContent, document.body.childNodes[0]);
	}
	talkContent.value = content;
	doHtmlDialogAction('SUBMITTALK');
	window.chatEditor.load(); //重新加载输入框
};

//显示文件接收进度
Chat.prototype.showFileReceiveProgress = function(fileId, receivedSize, fileSize, speed) {
	var percent = (Math.floor(receivedSize / fileSize * 1000) / 10)
	document.getElementById("transferProgress_" + fileId).style.width = percent + "%";
	document.getElementById("percent_" + fileId).innerHTML = percent + "%";
	if(speed>0) {
		document.getElementById("speed_" + fileId).innerHTML = convertBytes(speed) + "/秒";
	}
	if(document.getElementById("receiveFile_" + fileId).style.display!="none") { //开始接收
		//隐藏"接收"、"拒绝接收"
		document.getElementById("receiveFile_" + fileId).style.display = "none";
		document.getElementById("refuseFile_" + fileId).style.display = "none";
		//显示"取消"
		document.getElementById("cancelFile_" + fileId).style.display = "";
	}
	if(receivedSize==fileSize) { //完成接收
		//隐藏"取消"
		document.getElementById("cancelFile_" + fileId).style.display = "none";
		//显示"打开文件"、"打开所在文件夹"
		document.getElementById("openFile_" + fileId).style.display = "";
		document.getElementById("openFolder_" + fileId).style.display = "";
	}
};

//显示文件发送进度
Chat.prototype.showFileSendProgress = function(fileId, sendSize, fileSize, speed, percent) {
	document.getElementById("transferProgress_" + fileId).style.width = percent + "%";
	document.getElementById("percent_" + fileId).innerHTML = percent + "%";
	if(speed>0) {
		document.getElementById("speed_" + fileId).innerHTML = convertBytes(speed) + "/秒";
	}
	if(document.getElementById("offlineSendFile_" + fileId).style.display!="none") { //开始发送
		document.getElementById("offlineSendFile_" + fileId).style.display = "none"; //隐藏"发送离线文件"
	}
	if(sendSize==fileSize) { //完成发送
		document.getElementById("cancelFile_" + fileId).style.display = "none"; //隐藏"取消"
	}
};

//发送离线文件
Chat.prototype.offlineSendFile = function(fileId, filePath) {
	var chat = this;
	var fileUploader = new FileUploader();
	fileUploader.onUploading = function(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumer, currentFileSize, currentFileComplete, threads, complete, speed, usedTime, percent) {
		chat.showFileSendProgress(fileId, complete, totalSize, speed, percent);
	};
	fileUploader.onUploadComplete = function(localFileNames, remoteFileNames) { //上传完成
		doHtmlDialogAction("OFFLINESENDFILECOMPlETE", "fileId=" + fileId + "&remoteFileName=" + remoteFileNames);
	};
	var uploadPassportUrl = "attachmentEditor.shtml?act=create&id=" + this.chatId + "&attachmentSelector.field=attachments&attachmentSelector.type=attachments&attachmentSelector.action=passport";
	fileUploader.uploadFile(filePath, uploadPassportUrl, true);
};

//类:IM对话编辑器
ChatEditor = function(chat) {
	window.chatEditor = this;
	this.chat = chat;
	this.editorDocument = frames["content"].document;
	this.load();
	this.defaultHeight = document.getElementById("content").offsetHeight;
};

//加载编辑器
ChatEditor.prototype.load = function() {
	var html = '<html>' +
			   '	<head>' +
			   '		<style type="text/css">' +
			   '			.editor {' +
			   '				font-family:' + 'simsun' + ';' + //this.webim.webimSetting.fontName
			   '				font-size:' + 12 + 'px;' + //this.webim.webimSetting.getFontSizePixel()
			   '				color:' + '#000000' + ';' + //this.webim.webimSetting.fontColor
			   '			}' +
			   '		</style>' +
			   '		<link href="css/chatEditor.css" type="text/css" rel="stylesheet">' +
			   '	</head>' +
			   '	<body contentEditable="true"' +
			   '		  class="editor"' +
			   '		  onload="parent.chatEditor.onLoad()"' +
			   '		  onresize="parent.chatEditor.resizeEditor()"' +
			   '		  onkeydown="return parent.chatEditor.onKeyDown(event)"' +
			   '		  onkeyup="return parent.chatEditor.onKeyUp(event)"' +
			   '		  onkeypress="parent.chatEditor.resizeEditor()"' +
			   '		  onpaste="parent.chatEditor.resizeEditor()"' +
			   '		  oncut="parent.chatEditor.resizeEditor()"' +
			   '		  onhelp="return false;"' +
			   '		  ondragstart="return false;">' +
			   '	</body>' +
			   '</html>';
	this.editorDocument.open();
	this.editorDocument.write(html);
	this.editorDocument.close();
	//设置滚动条样式
	var iframe = document.getElementById("content");
	this.editorDocument.body.style.scrollbarTrackColor = iframe.style.scrollbarTrackColor;
	this.editorDocument.body.style.scrollbarShadowColor = iframe.style.scrollbarShadowColor;
	this.editorDocument.body.style.scrollbarHighlightColor = iframe.style.scrollbarHighlightColor;
	this.editorDocument.body.style.scrollbarFaceColor = iframe.style.scrollbarFaceColor;
	this.editorDocument.body.style.scrollbarDarkShadowColor = iframe.style.scrollbarDarkShadowColor;
	this.editorDocument.body.style.scrollbarBaseColor = iframe.style.scrollbarBaseColor;
	this.editorDocument.body.style.scrollbarArrowColor = iframe.style.scrollbarArrowColor;
	this.editorDocument.body.style.scrollbar3dLightColor = iframe.style.scrollbar3dLightColor;
};

//加载
ChatEditor.prototype.onLoad = function() {
	this.resizeEditor();
	this.editorDocument.body.focus();
};

//按键处理
ChatEditor.prototype.onKeyDown = function(event) {
   	if(!event) {
   		event = window.event;
   	}
   	var keyCode = event.which ? event.which : event.keyCode;
   	if(event.ctrlKey && keyCode==13) { //firefox
   		keyCode = 10;
   	}
   	if((!event.charCode || event.charCode!=100) && !event.shiftKey && keyCode==(false ? 10 : 13)) { //this.webim.webimSetting.ctrlSend
   		window.chat.submitTalk()
   		return false;
   	}
   	switch(keyCode) {
	case 13:
	case 10: //回车
		this.editorDocument.execCommand('InsertInputButton', '', 'tempButton');
		this.editorDocument.getElementById('tempButton').outerHTML = '<br/>';
		break;
		
	case 70: //CTRL + F, 查找
	case 76: //CTRL + L, 打开链接
	case 80: //CRRL + P, 打印
	case 79: //CTRL + O, 打开链接
	case 78: //CTRL + N, 创建新窗口
		if(event.ctrlKey) {
			event.keyCode = 0;
			return false;
		}
		break;

	case 116: //F5
		event.keyCode = 0;
		return false;
	}
	this.resizeEditor();
};

//按键处理
ChatEditor.prototype.onKeyUp = function(event) {
	this.resizeEditor();
};

//设置编辑器大小
ChatEditor.prototype.resizeEditor = function() {
	var fontSize = 12; //this.webim.webimSetting.getFontSizePixel();
	var height = (!document.all && this.editorDocument.body.innerHTML.trim()=="" ? 0 : this.editorDocument.body.scrollHeight);
	height = Math.max(height, fontSize + 6);
	var editorHeight = Math.min(Math.max(this.defaultHeight, height), Math.max(this.defaultHeight, document.body.clientHeight * 0.25));
	document.getElementById("content").style.height =  editorHeight;
	this.editorDocument.body.style.overflow = (height > editorHeight ? "auto" : "hidden");
	//通知WEBIM,当前正在输入
	//this.webim.setActive();
};

//插入图片
ChatEditor.prototype.insertImage = function(imgSrc, imgAlt) {
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
		window.chatEditor.resizeEditor();
	};
	img.src = imgSrc;
	img.title = img.alt = imgAlt;
	this.editorDocument.body.focus();
};

function isSelectable() { //是否允许选择
	var obj = event.srcElement;
	//检查是否对话记录列表
	var talks = document.getElementById("talks");
	if(obj==talks || talks.contains(obj)) {
		return true;
	}
	var tagName = obj.tagName.toLowerCase();
	return "input,font,".indexOf(tagName + ",")!=-1;
}