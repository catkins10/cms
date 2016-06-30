//文件系统类
//文件选择事件:window.top.fileSystem.onSelectFile = function(filePath);
//文件读取事件:window.top.fileSystem.onReadLineFromFile = function(line),返回true继续读取,返回false放弃,line==null表示文件读取完成
//获取需要写入的行:window.top.fileSystem.onReadLineToWriteFile = function(),返回null表示已经没有需要写入的行
FileSystem = function() {

};
FileSystem.prototype.saveFile = function(path, fileName, fileSize, filter) { //打开文件保存对话框, filter: *.txt,*.doc
	var index;
	if(fileName && !filter && (index = fileName.lastIndexOf('.'))!=-1) {
		filter = '*' + fileName.substring(index);
	}
	this._createFileFrame(true, path, fileName, fileSize, filter, false, false);
};
FileSystem.prototype.selectFile = function(path, filter, multiSelect, selectFolder) { //打开文件选择对话框, filter:*.txt,*.doc
	this._createFileFrame(false, path, null, 0, filter, multiSelect, selectFolder);
};
FileSystem.prototype._createFileFrame = function(isSaveFile, path, fileName, fileSize, filter, multiSelect, selectFolder) { //打开文件选择对话框, filter:文本文件|*.txt|所有文件|*.*|
	this.isSaveFile = isSaveFile;
	this.path = path;
	this.fileName = fileName;
	this.fileSize = fileSize;
	this.filter = filter;
	this.multiSelect = multiSelect;
	this.selectFolder = selectFolder;
	if(path && path!='') {
		this._doCreateFileFrame();
	}
	else {
		window.client.callNativeMethod("getWorkDirectory()", function(returnValue) {
			window.top.fileSystem.path = returnValue;
			window.top.fileSystem._doCreateFileFrame();
		});
	}
};
FileSystem.prototype._doCreateFileFrame = function() {
	var fileSystem = this;
	var url = RequestUtils.getContextPath() + "/jeaf/client/" + (this.isSaveFile ? "saveFile.shtml" : "selectFile.shtml") + (window.top.client.siteId=="" ? "" : "?siteId=" + window.top.client.siteId);
	var onload = function(popupFrame) {
		fileSystem._loadFileFrame(popupFrame);
	};
	this.fileFrame = window.client.createPopupFrame(url, onload, null, true, true);
};
FileSystem.prototype._loadFileFrame = function(iframe) {
	var win = iframe.contentWindow;
	this.document = win.document;
	
	//重置文件列表区域
	var fileListArea = this.document.getElementById('fileListArea');
	fileListArea.style.overflow = "hidden";
	fileListArea.style.width = fileListArea.parentNode.offsetWidth + "px";
	fileListArea.style.height = fileListArea.parentNode.offsetHeight + "px";
	this.scroller = new Scroller(fileListArea, false, false, true, false, false);
	win.close = win.closeWindow = function() {
		window.top.client.closePopupFrame(iframe);
	};
	if(this.isSaveFile) {
		var textBox = this.document.getElementsByName('fileName')[0];
		textBox.value = this.fileName;
		window.top.client.resetTextBox(textBox); //重置文本框
	}
	this._writeFolderName(); //输出目录名称
	this.fileOffset = 0;
	this._listFiles(0, 20); //获取文件列表
	//设置更新器
	var fileSystem = this;
	this.document.loadMoreEnabled = true;
	this.document.loadMore = function() {
		fileSystem._listFiles(fileSystem.fileOffset, 20);
	};
	new Updater(this.scroller, false, true);
};
FileSystem.prototype._writeFolderName = function() { //输出目录名称
	var folderName = this.document.getElementById('folderName');
	if(!folderName) {
		return;
	}
	var path = this.path;
	if(path!="/") {
		path = path.substring(path.lastIndexOf('/', path.length-2) + 1, path.length-1);
	}
	folderName.innerHTML = path;
	if(!folderName.onclick) {
		folderName.onclick = function() {
			window.top.fileSystem.openFolder("..");
		};
	}
};
FileSystem.prototype._clearFileList = function() { //清空列表
	var fileRecord = this.document.getElementById('fileRecord');
	var childNodes = fileRecord.parentNode.childNodes;
	for(var i=childNodes.length-1; i>=0; i--) {
		if(childNodes[i]!=fileRecord) {
			childNodes[i].parentNode.removeChild(childNodes[i]);
		}
	}
	this.fileOffset = 0;
};
FileSystem.prototype._listFiles = function(offset, limit) { //获取文件列表
	var callback;
	if(window.top.androidClient) { //安卓
		callback = function(returnValue) {
			window.top.fileSystem._writeFiles(returnValue);
		};
	}
	else { //IOS
		callback = function(returnValue) {
			window.top.fileSystem._appendFile(returnValue, offset, count);
		};
	}
	window.client.callNativeMethod("listFiles('" + this.path + "', '" + (this.filter ? this.filter : "") + "', " + (this.selectFolder ? true : false) + "," + offset + "," + limit + ")", callback);
};
FileSystem.prototype._appendFile = function(file, offset, count) { //追加文件(IOS调用),file格式:{name:'文件名称', isFolder:true/false, size:'文件大小', lastModified:'最后修改时间', icon:'图标'}
	//TODO
};
FileSystem.prototype._writeFiles = function(files) { //输出文件列表,files格式:[{name:'文件名称', isFolder:true/false, size:'文件大小', lastModified:'最后修改时间', icon:'图标'}]
	try {
		eval("files=" + files + ";");
	}
	catch(e) {
		return;
	}
	this.fileOffset += files.length;
	var fileRecord = this.document.getElementById('fileRecord');
	for(var i=0; i<files.length; i++) {
		var newFileRecord = fileRecord.cloneNode(true);
		newFileRecord.removeAttribute("id");
		newFileRecord.style.display = "";
		//设置文件名称
		var fileName = DomUtils.getElement(newFileRecord, "a", "fileName");
		if(fileName) {
			fileName.innerHTML = files[i].name;
		}
		//设置图标
		var fileIcon = DomUtils.getElement(newFileRecord, "a", "fileIcon");
		if(fileIcon) {
			var img = this.document.createElement("img");
			img.src = files[i].icon;
			fileIcon.parentNode.replaceChild(img, fileIcon);
		}
		var fileSize = DomUtils.getElement(newFileRecord, "a", "fileSize");
		if(fileSize) {
			if(files[i].isFolder) {
				fileSize.parentNode.removeChild(fileSize);
			}
			else {
				fileSize.innerHTML = files[i].size;
			}
		}
		var fileModified = DomUtils.getElement(newFileRecord, "a", "fileModified");
		if(fileModified) {
			if(files[i].isFolder) {
				fileModified.parentNode.removeChild(fileModified);
			}
			else {
				fileModified.innerHTML = files[i].lastModified;
			}
		}
		var fileSelectBox = DomUtils.getElement(newFileRecord, "a", "fileSelectBox");
		if(fileSelectBox) {
			if(files[i].name==".." || (files[i].isFolder && !this.selectFolder) || this.isSaveFile) {
				fileSelectBox.parentNode.removeChild(fileSelectBox);
			}
			else {
				var selectBox = this.document.createElement("input");
				selectBox.type = this.multiSelect ? "checkbox" : "radio";
				selectBox.name = "select";
				selectBox.value = files[i].name;
				fileSelectBox.parentNode.replaceChild(selectBox, fileSelectBox);
			}
		}
		fileRecord.parentNode.insertBefore(newFileRecord, fileRecord);
		newFileRecord.isFolder = files[i].isFolder;
		newFileRecord.onclick = function() {
			var name = DomUtils.getElement(this, "a", "fileName").innerHTML;
			if(this.isFolder) {
				window.top.fileSystem.openFolder(name); //打开目录
			}
			else if(window.top.fileSystem.isSaveFile) {
				window.top.client.setTextValue(name, window.top.fileSystem.document.getElementsByName("fileName")[0]);
			}
			else {
				var selectBox = this.getElementsByTagName("input")[0];
				if(selectBox) {
					selectBox.checked = selectBox.type=="radio" ? true : !selectBox.checked;
				}
			}
		};
	}
	this.scroller.showBar();
};
FileSystem.prototype.openFolder = function(folderName) { //打开目录
	if(".."!=folderName) {
		this.path += folderName + "/";
	}
	else if(this.path=="/") {
		return;
	}
	else {
		this.path = this.path.substring(0, this.path.lastIndexOf('/', this.path.length-2) + 1);
	}
	this._writeFolderName(); //输出目录名称
	this.scroller.scrollTo(0, 0); //重新显示滚动条
	this._clearFileList(); //清空列表
	this._listFiles(0, 20); //获取目录列表
};
FileSystem.prototype.createNewFolder = function() { //新建文件夹
	var dialog = window.top.client.openInputDialog('新建文件夹', [{name:"文件夹名称", value:"", dataType:"string", maxLength:100}], false, false);
	dialog.onOK = function(clientDialogBody) {
		var folderName = clientDialogBody.getValue("文件夹名称");
		if(folderName=="") {
			return true;
		}
		if(folderName.search(/[<>\"\'?\*:/\\\|]/gi)!=-1) {
			alert("文件夹名称不能包含<>\"'?*:/\\|");
			return;
		}
		window.client.callNativeMethod("createNewFolder('" + window.top.fileSystem.path + folderName + "')", function(returnValue) {
			if(returnValue=='true') {
				window.top.fileSystem.openFolder(folderName);
			}
			else {
				alert('文件夹创建失败');
			}
		});
		return true;
	};
};
FileSystem.prototype.onOK = function() {
	if(this.isSaveFile) {
		var fileName = this.document.getElementsByName("fileName")[0].value;
		if(fileName=="") {
			alert("文件名不能为空");
			return;
		}
		if(fileName.search(/[<>\"\'?\*:/\\\|]/gi)!=-1) {
			alert("文件名不能包含<>\"'?*:/\\|");
			return;
		}
		if(this.filter && this.filter!="" && this.filter.indexOf(',')==-1 && this.filter.indexOf("*.")==0 && fileName.indexOf(".")==-1) { //仅有一个扩展名过滤规则,且没有指定扩展名
			fileName += this.filter.substring(1);
		}
		fileName = this.path + fileName;
		window.client.callNativeMethod("validateToSaveFile('" + fileName + "')", function(returnValue) { //校验需要保存的文件, 返回格式: {size:当前存在的文件大小, freeSpace:剩余空间大小, canWrite:目录是否可写}
			window.top.fileSystem._afterValidateToSaveFile(returnValue, fileName);
		});
	}
	else {
		var selects = this.document.getElementsByName("select");
		var filePaths = new Array();
		for(var i=0; i<(selects ? selects.length : 0); i++) {
			if(selects[i].checked) {
				filePaths[filePaths.length] = this.path + selects[i].value;
			}
		}
		if(filePaths.length==0) {
			alert("未选择文件");
			return;
		}
		this.doOK(this.multiSelect ? filePaths : filePaths[0]);
	}
};
FileSystem.prototype._afterValidateToSaveFile = function(validateResult, filePath) { //validateResult: {size:当前存在的文件大小, freeSpace:剩余空间大小, canWrite:目录是否可写}
	eval("validateResult=" + validateResult);
	if(!validateResult.canWrite) {
		alert("您没有权限在此位置保存文件");
		return;
	}
	if(validateResult.size==0) { //文件不存在,或者大小为0
		this._checkFreeSpace(validateResult.freeSpace, filePath);
		return;
	}
	var confirm = window.client.openConfirmDialog("保存", "文件已经存在,是否覆盖?");
	confirm.onOK = function(clientDialogBody) {
		window.top.fileSystem._checkFreeSpace(validateResult.freeSpace + validateResult.size, filePath);
		return true;
	};
};
FileSystem.prototype._checkFreeSpace = function(freeSpace, filePath) { //检查存储空间是否足够存放文件
	if(this.fileSize>0 && freeSpace < this.fileSize) {
		alert('剩余空间不足');
	}
	else {
		this.doOK(filePath);
	}
};
FileSystem.prototype.doOK = function(filePath) {
	this.onSelectFile.call(null, filePath);
	this.onSelectFile = null;
	window.client.closePopupFrame(this.fileFrame);
	window.client.callNativeMethod("setWorkDirectory('" + filePath + "')");
};
FileSystem.prototype.readTextFromFile = function(filePath) { //读取文本文件
	window.client.callNativeMethod("readLineFromFile('" + filePath + "')", function(returnValue) { //返回值#END#表示读取完成
		if(returnValue=='#ERROR#') {
			alert('读文件时出错');
			return;
		}
		if(window.top.fileSystem.onReadLineFromFile.call(null, returnValue=='#END#' ? null : returnValue) && returnValue!='#END#') {
			window.top.fileSystem.readTextFromFile(filePath);
		}
	});
};
FileSystem.prototype.writeTextToFile = function(filePath) { //写文本文件
	var line = this.onReadLineToWriteFile.call(null);
	if(line==null) {
		window.client.callNativeMethod("completeWriteFile()");
	}
	else {
		var callback = function(returnValue) {
			if(returnValue=='true') {
				window.top.fileSystem.writeTextToFile(filePath);
			}
			else {
				alert('写文件时出错');
			}
		};
		window.client.callNativeMethod("writeLineToFile('" + filePath + "', '" + line.replace(/\"/gi, "\\\"").replace(/'/gi, "\\'").replace(/\r/gi, "\\r").replace(/\n/gi, "\\n") + "')", callback);
	}
};