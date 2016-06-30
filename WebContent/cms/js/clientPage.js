function doHtmlDialogAction(action, parameter) {
	if(!window.frameElement) {
		location.href = "app://" + action + "/" + (parameter ? "?" + parameter : "");
	}
}

function alert(msg) {
	doHtmlDialogAction("ALERT", "message=" + msg);
}

var onClientPageLoaded = function() {
	/*document.body.oncontextmenu = function() { //禁用右键菜单
		var obj = event.srcElement;
		return obj.tagName.toLowerCase()=="input" && (obj.type.toLowerCase()=="text" || obj.type.toLowerCase()=="password");
	};*/
	document.body.onhelp = function() { //禁止帮助
		return false;
	};
	document.body.ondragstart = function() {
		return false;
	};
	document.body.onselectstart = function() { //禁止选择
		if(!isSelectable()) {
			return false;
		}
	}; 
	document.body.onmouseup = function() {
		return false;
	};
	document.body.onmousedown = function() {
		if(isMovable()) {
			doHtmlDialogAction('MOVEWINDOW');
		}
	};
	document.onkeydown = function() {
		switch(event.keyCode) {
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
	};
};
EventUtils.addEvent(window, "load", onClientPageLoaded);

function isSelectable() { //是否允许选择
	var obj = event.srcElement;
	var tagName = obj.tagName.toLowerCase();
	return "input,font,".indexOf(tagName + ",")!=-1;
}

function isMovable() { //是否允许移动
	var obj = event.srcElement;
	if(obj.onclick) { //有单击事件
		return false;
	}
	var tagName = obj.tagName.toLowerCase();
	return "input,div,span,a,font,img,label,".indexOf(tagName + ",")==-1;
}