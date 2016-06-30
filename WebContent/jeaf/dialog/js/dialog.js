var move = false;
var cursorX, cursorY;
var originalDialogWidth = -1, originalDialogHeight = -1;
document.onmousedown = function(event) { //start drag
	if(!event) {
		event = window.event;
	}
	if(document.all) { //IE
		document.body.setCapture();
	}
	else if(window.captureEvents) {
		window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
	}
	cursorX = event.clientX;
	cursorY = event.clientY;
	move=true;
};
document.onmousemove = function(event) { //drag
	if(!move) {
		return;
	}
	if(!event) {
		event = window.event;
	}
	var dialogFrame = window.frameElement;
	dialogFrame.style.left = (dialogFrame.offsetLeft + event.clientX - cursorX) + "px";
	dialogFrame.style.top = (dialogFrame.offsetTop + event.clientY - cursorY) + "px";
};
document.onmouseup = function(event) { //stop drap
	if(document.all) { //IE
		document.body.releaseCapture();
	}
	else {
		window.releaseEvents(Event.MOUSEMOVE|Event.MOUSEUP);
	}
	move = false;
};
function onDialogLoad() {
	var iframe = document.getElementById("dialogBody");
	if(window.frameElement.dialogUrl) {
		var siteId = StringUtils.getPropertyValue(window.location.href, "siteId", "0");
		iframe.src = window.frameElement.dialogUrl + (window.frameElement.dialogUrl.indexOf('?')==-1 ? '?' : '&') + 'displayMode=dialog' +
					 (siteId && siteId!="" && siteId!="0" && window.frameElement.dialogUrl.indexOf("siteId=")==-1 ? "&siteId=" + siteId : "");
	}
	if(window.frameElement.onDialogLoad) {
		window.frameElement.onDialogLoad.call(null, iframe);
	}
}
function onDialogBodyLoad() { //对话框完成加载
	var iframe = document.getElementById("dialogBody");
	if(iframe.contentWindow.location=='about:blank') {
		return;
	}
	window.setTimeout(function() {
		try {
			var doc = window.frames[0].document;
			var dialogTitle = (!doc.title || doc.title=='' ? '' : doc.title); //对话框标题
			var index = location.search.indexOf("dialogTitle=");
			if(index!=-1) {
				dialogTitle =  StringUtils.utf8Decode(location.search.substring(index + "dialogTitle=".length));
			}
			if(dialogTitle) {
				document.getElementById('dialogTitle').innerHTML = dialogTitle;
			}
			adjustDialogSize(); //调整对话框尺寸
		}
		catch(e) {
			
		}
		window.frameElement.allowTransparency = false;
		document.body.style.backgroundColor = "#ffffff";
		window.frameElement.style.visibility = "visible";
		try {
			//var range = DomSelection.createRange(doc, doc.body);
			//range.collapse(true);
			//DomSelection.selectRange(iframe.contentWindow, range);
			doc.body.focus();
			doc.body.onkeydown = function(event) {
				if(!event) {
					event = window.event;
				}
				if(event.keyCode==27) { //ESC
					DialogUtils.closeDialog();
				}
			};
		}
		catch(e) {
			
		}
		if(window.frameElement.onDialogBodyLoad) {
			window.frameElement.onDialogBodyLoad.call(null, iframe);
		}
	}, 200);
}
function adjustDialogSize() { //调整对话框尺寸
	try {
		var doc = window.frames[0].document;
		var iframe = document.getElementById("dialogBody");
		var dialogFrame = window.frameElement;
		var isOriginalPage = iframe.getAttribute("src", 2).indexOf(window.frames[0].location.pathname)==0; //是否原始页面
		if(originalDialogWidth==-1) {
			originalDialogWidth = iframe.offsetWidth;
			originalDialogHeight = iframe.offsetHeight;
		}
		else if(isOriginalPage) {
			var currentWidth = iframe.offsetWidth, currentHeight = iframe.offsetHeight;
			dialogFrame.style.width = (dialogFrame.offsetWidth + (originalDialogWidth - currentWidth)) + "px";
			dialogFrame.style.height = (dialogFrame.offsetHeight + (originalDialogHeight - currentHeight)) + "px";
			dialogFrame.style.left = (dialogFrame.offsetLeft - (originalDialogWidth - currentWidth) / 2) + "px";
			dialogFrame.style.top = (dialogFrame.offsetTop - (originalDialogHeight - currentHeight) / 2) + "px";
			iframe.style.width = originalDialogWidth + "px";
			iframe.style.height = originalDialogHeight + "px";
		}
		//调整对话框尺寸
		var dialogWidthCheck = doc.getElementById("dialogWidthCheck");
		var dialogHeightCheck = doc.getElementById("dialogHeightCheck");
		var dialogButtonBar = doc.getElementById("dialogButtonBar");
		var newWidth = -1, newHeight = -1;
		if(dialogWidthCheck && dialogHeightCheck && dialogButtonBar) {
			//检查对话框内容是否按百分比显示
			for(var i=0; i<dialogWidthCheck.childNodes.length; i++) {
				if(dialogWidthCheck.childNodes[i].tagName && ",table,div,span,".indexOf("," + dialogWidthCheck.childNodes[i].tagName.toLowerCase() + ",")!=-1) {
					if((dialogWidthCheck.childNodes[i].width && dialogWidthCheck.childNodes[i].width.indexOf("%")!=-1) ||
					   (dialogWidthCheck.childNodes[i].style.width && dialogWidthCheck.childNodes[i].style.width.indexOf("%")!=-1)) {
						dialogWidthCheck.style.float = "none";
					}
				}
			}
			var dialogContent = dialogWidthCheck.offsetParent;
			//获取对话框内容的宽度和高度
			dialogHeightCheck.style.display = 'block';
			newWidth = dialogWidthCheck.offsetWidth + DomUtils.getSpacing(dialogContent, "left") + DomUtils.getSpacing(dialogContent, "right") + DomUtils.getSpacing(doc.body, "left") + DomUtils.getSpacing(doc.body, "right");
			newHeight = dialogHeightCheck.offsetTop + dialogButtonBar.offsetHeight + DomUtils.getSpacing(dialogContent, "top") + DomUtils.getSpacing(dialogContent, "bottom") + DomUtils.getSpacing(doc.body, "top") + DomUtils.getSpacing(doc.body, "bottom");
		}
		else if(doc.body.style.overflow!='auto' && doc.body.style.overflow!='scroll') {
			var childNodes = doc.body.childNodes;
			for(var i=0; i<childNodes.length; i++) {
				if(childNodes[i].offsetWidth && childNodes[i].offsetWidth>0) {
					newWidth = Math.max(newWidth, childNodes[i].offsetLeft + childNodes[i].offsetWidth);
					newHeight = Math.max(newHeight, childNodes[i].offsetTop + childNodes[i].offsetHeight);
				}
			}
			newWidth += DomUtils.getSpacing(doc.body, "right");
			newHeight += DomUtils.getSpacing(doc.body, "bottom");
		}
		if(newWidth!=-1 && newHeight!=-1) {
			newWidth = Math.min(DomUtils.getClientWidth(top.document) - 30, Math.max(320, newWidth));
			newHeight = Math.min(DomUtils.getClientHeight(top.document) - 30, newHeight);
			//获取当前的宽度和高度
			iframe.style.display = "none";
			var currentWidth = iframe.parentElement.offsetWidth;
			var currentHeight = iframe.parentElement.offsetHeight;
			iframe.style.display = "";
	
			//重置尺寸和位置
			if(dialogHeightCheck) {
				dialogHeightCheck.style.display = "none";
			}
			iframe.style.width = newWidth + "px";
			iframe.style.height = newHeight + "px";
			dialogFrame.style.width = (dialogFrame.offsetWidth + (newWidth - currentWidth)) + "px";
			dialogFrame.style.height = (dialogFrame.offsetHeight + (newHeight - currentHeight)) + "px";
			dialogFrame.style.left = (dialogFrame.offsetLeft - (newWidth - currentWidth) / 2) + "px";
			dialogFrame.style.top = (dialogFrame.offsetTop - (newHeight - currentHeight) / 2) + "px";
		}
		if(isOriginalPage) { //原始页面
			originalDialogWidth = iframe.offsetWidth;
			originalDialogHeight = iframe.offsetHeight;
		}
	}
	catch(e) {
		
	}
}