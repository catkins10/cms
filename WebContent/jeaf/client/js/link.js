Client.prototype.openLink = function(srcElement) { //打开链接
	var url = srcElement.getAttribute('url');
	if(!url) {
		return;
	}
	if(url.indexOf('?client.modified=')!=-1) {
		var imageTypes = ['jpg', 'jpeg', 'jpe', 'gif', 'bmp', 'png', 'tif', 'tiff', 'pcx', 'tga', 'exif', 'fpx', 'svg', 'psd', 'cdr', 'pcd', 'dxf', 'ufo', 'eps', 'ai', 'raw'];
		var i = imageTypes.length - 1;
		for(; i>=0 && url.toLowerCase().indexOf('.' + imageTypes[i])==-1; i--);
		if(i>=0) {
			this.openImage(url);
		}
		else {
			this.openFile(url, srcElement.target=='_self');
		}
		return;
	}
	url += (url.indexOf('?')==-1 ? '?' : '&') + 'client.imageWidth=' + (this.clientWidth-16) + '&client.density=' + this.density;
	if(srcElement.target!='_self') {
		this.openPage(url, win);
	}
	else {
		var win = (srcElement.ownerDocument.parentWindow ? srcElement.ownerDocument.parentWindow : srcElement.ownerDocument.defaultView)
		win.location.replace(url);
	}
};
Client.prototype.openPage = function(url, opener) { //打开页面
	var client = this, pageFrame, popupPageId;
	var now = new Date().valueOf();
	if(client.lastOpenPageTime && (client.pageLoading || now - client.lastOpenPageTime < 800) && now - client.lastOpenPageTime < 2000) {
		return;
	}
	client.pageLoading = true;
	client.lastOpenPageTime = now;
	var promptTimer = window.setTimeout(function() { //显示提示信息
		client.showToast('正在加载,请稍候...', 30);
		popupPageId = client.registPopupPage(function() {
			client.showToast('');
			client.closePopupFrame(pageFrame);
		});
	}, 300);
	
	//打开URL
	var onload = function(popupFrame) {
		if(!popupPageId) {
			window.clearTimeout(promptTimer);
		}
		else {
			client.showToast('');
			client.unregistPopupPage(popupPageId);
		}
		popupFrame.contentWindow.opener = opener;
		var doc = popupFrame.contentWindow.document;
		var contentArea = doc.getElementById("contentArea");
		if(contentArea) {
			doc.scroller = new Scroller(contentArea, false, false, true, false, false);
			new Updater(doc.scroller, false, false);
		}
		client.resetForm(doc); //重置表单
		client.pageLoading = false;
	};
	pageFrame = this.createPopupFrame(url, onload, null, true, true);
	pageFrame.opener = opener;
};
Client.prototype.openImage = function(url) { //打开图片
	var client = this;
	var div = document.createElement('div');
	div.onclick = function() {
		client.closeImageWindow();
	};
	div.imageUrl = url;
	div.id = 'divImage';
	div.style.zIndex = this.getZIndex();
	div.style.position = 'absolute';
	div.style.borderStyle = 'none';
	div.style.backgroundColor = "#000000";
	div.style.left = '0px';
	div.style.top = '0px';
	div.style.width = (this.clientWidth - 6) + "px";
	div.style.height = (this.clientHeight - 6) + "px";
	div.style.padding = "3px";
	div.style.textAlign = 'center';
	div.style.overflow = 'hidden';
	document.body.insertBefore(div, document.body.childNodes[0]);
	
	//下载相关回调
	var imageProgressBar;
	var imageProgressBarTable;
	var onFileDownloadStart = function() {
		imageProgressBar = new CircleProgressBar(); //创建进度条
		imageProgressBarTable = document.createElement('table');
		imageProgressBarTable.border = 0;
		imageProgressBarTable.cellPadding = 0;
		imageProgressBarTable.cellSpacing = 0;
		imageProgressBarTable.style.width = imageProgressBarTable.style.height = '100%';
		var td = imageProgressBarTable.insertRow(-1).insertCell(-1);
		td.align = 'center';
		td.vAlign = 'middle';
		div.appendChild(imageProgressBarTable);
	   	td.appendChild(imageProgressBar.create(document));
   	};
	var onFileDownloading = function(fileSize, completed, percentage) {
		imageProgressBar.showProgress(percentage);
	};
	var onFileDownloadComplete = function() {
		if(imageProgressBarTable) {
			imageProgressBarTable.parentNode.removeChild(imageProgressBarTable);
		}
	};
	this.registFileDowloadTask(url, onFileDownloadStart, onFileDownloading, onFileDownloadComplete); //登记文件下载任务,onFileDownloadStart = function(), onFileDownloading = function(fileSize, completed, percentage), onFileDownloadComplete = function()
	
	//加载图片
	var img = document.createElement('img');
	img.onload = function() {
		this.width = Math.round(this.width / client.density);
		this.height = Math.round(this.height / client.density);
		this.style.marginTop = Math.max(0, (client.clientHeight - this.height) / 2) + "px";
		this.style.display = '';
		new Scroller(div, false, false, false, false, false); //创建滚动条
	};
	img.style.display = 'none';
	img.src = url;
	div.appendChild(img);
	
	div.popupPageId = this.registPopupPage(function() {
		client.closeImageWindow();
	});
};
Client.prototype.closeImageWindow = function() { //关闭图片
	var div = document.getElementById('divImage');
	this.cancelFileDowloadTask(div.imageUrl);
	this.unregistPopupPage(div.popupPageId);
	div.parentNode.removeChild(div);
};
Client.prototype.openFile = function(url, openWithoutConfirm) { //打开文件
	if(openWithoutConfirm) {
		this._downloadFile(url);
		return true;
	}
	var client = this;
	var index = url.lastIndexOf('/') + 1;
	var indexEnd = url.indexOf('?', index);
	var fileName = StringUtils.utf8Decode(url.substring(index, indexEnd==-1 ? url.length : indexEnd));
	var fileSize = StringUtils.getPropertyValue(url, "fileSize", "");
	var dialog = new Dialog('文件', '<div style="padding: 6px;">打开或者保存' + fileName + (fileSize!="" ? '(' + StringUtils.formatBytes(Number(fileSize)) + ')' : '') + '?</div>', false, false, '打开', '取消', '保存');
	dialog.onOK = function(clientDialogBody) {
		client._downloadFile(url);
		return true;
	};
	dialog.onOther = function(clientDialogBody) {
		window.top.fileSystem.onSelectFile = function(filePath) {
			client._downloadFile(url, filePath);
		};
		window.top.fileSystem.saveFile("", fileName, fileSize!="" ? Number(fileSize) : 0);
		return true;
	};
	dialog.open();
};
Client.prototype._downloadFile = function(url, savePath) { //打开文件
	var client = this;
	url += (savePath ? "&client.savePath=" + StringUtils.utf8Encode(savePath) : "&client.openFile=true") + "&seq=" + Math.random();
	this.downloadProgressDialog = null;
	var onFileDownloadStart = function(fileSize) {
		client._createFileDownloadProgressBar(url, fileSize);
	};
	var onFileDownloading = function(fileSize, completed, percentage) {
		client.downloadedSpan.innerHTML = completed;
		client.downloadProgressBar.showProgress(percentage);
	};
	var onFileDownloadComplete = function() {
		if(client.downloadProgressDialog) {
			client.downloadProgressDialog.close();
		}
		if(savePath) {
			client.showToast("已保存", 2);
		}
	};
	this.registFileDowloadTask(url, onFileDownloadStart, onFileDownloading, onFileDownloadComplete); //登记文件下载任务,onFileDownloadStart = function(), onFileDownloading = function(fileSize, completed, percentage), onFileDownloadComplete = function()
	ScriptUtils.appendJsFile(document, url, "openFileJs");
};
Client.prototype._createFileDownloadProgressBar = function(url, fileSize) { //创建文件下载进度条
	var client = this;
	var html = '<div style="padding: 6px;">已下载<span id="downloaded">0KB</span>, 共' + fileSize + '</div>' +
			   '<div style="padding: 3px 6px 10px 6px;" id="downloadProgressBar"><div>';
	this.downloadProgressDialog = new Dialog('文件下载', html, true, false);
	client.downloadedSpan = DomUtils.getElement(this.downloadProgressDialog.clientDialogBody, 'span', 'downloaded');
	client.downloadProgressBar = new LinearProgressBar(); //创建进度条
   	var progressBarElement = client.downloadProgressBar.create(document);
   	DomUtils.getElement(this.downloadProgressDialog.clientDialogBody, 'div', 'downloadProgressBar').appendChild(progressBarElement);
	this.downloadProgressDialog.onCancel = function(clientDialogBody) {
		client.cancelFileDowloadTask(url);
		return true;
	};
	this.downloadProgressDialog.open();
};