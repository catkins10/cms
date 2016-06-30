PageUtils = function() {

};
//新建流程,选择流程
PageUtils.createWorkflowInstnace = function(workflowEntriyList, recordUrl, openFeatues) {
   var workflowEntriy;
   if(workflowEntriyList.collection.length == 1 && (workflowEntriy=ListUtils.findObjectByProperty(workflowEntriyList.objects, 'uuid', workflowEntriyList.collection[0].uuid)).activityEntries.length==1) {
      PageUtils._newWorkflowInstnace(recordUrl, workflowEntriy.workflowId + "." + ListUtils.findObjectByProperty(workflowEntriyList.objects, 'uuid', workflowEntriy.activityEntries[0].uuid).id, openFeatues);
      return;
   }
   var menuDefinition = [];
   for(var i = 0; i < workflowEntriyList.collection.length; i++) {
   	  workflowEntriy = ListUtils.findObjectByProperty(workflowEntriyList.objects, 'uuid', workflowEntriyList.collection[i].uuid);
      menuDefinition[i] = {id: workflowEntriy.workflowId, title: workflowEntriy.workflowName};
      if(workflowEntriy.activityEntries.length==1) {
          menuDefinition[i].id += "." + ListUtils.findObjectByProperty(workflowEntriyList.objects, 'uuid', workflowEntriy.activityEntries[0].uuid).id;
	  	  continue;
	  }
  	  menuDefinition[i].subItems = [];
      for(var k = 0; k < workflowEntriy.activityEntries.length; k++) {
      	 var activityEntry = ListUtils.findObjectByProperty(workflowEntriyList.objects, 'uuid', workflowEntriy.activityEntries[k].uuid);
      	 menuDefinition[i].subItems[k] = {id:workflowEntriy.workflowId + "." + activityEntry.id, title: activityEntry.name};
   	  }
   }
   PopupMenu.popupMenu(menuDefinition, function(menuItemId, menuItemTitle) {
		PageUtils._newWorkflowInstnace(recordUrl, menuItemId, openFeatues);
   }, event.srcElement);
};

//创建流程实例
PageUtils._newWorkflowInstnace = function(recordUrl, workflowId, openFeatues) {
   PageUtils.openurl(recordUrl + (recordUrl.indexOf('?')==-1 ? "?" : "&") + "workflowId=" + workflowId.split(".")[0] + "&activityId=" + workflowId.split(".")[1] + "&seq=" + Math.random(), openFeatues, recordUrl.replace(new RegExp('[=/\\?\\.&]', 'g'), ""));
};

//创建新记录
PageUtils.newrecord = function(applicationName, formName, openFeatues, param) {
   PageUtils.openurl(RequestUtils.getContextPath() + "/" + applicationName + "/" + formName + ".shtml?act=create" + (param ? "&" + param : "") + "&seq=" + Math.random(), openFeatues, applicationName + formName);
};

//打开记录
PageUtils.openrecord = function(applicationName, formName, id, openFeatues, param) {
   PageUtils.openurl(RequestUtils.getContextPath() + "/" + applicationName + "/" + formName + ".shtml?act=open&id=" + id + "&seq=" + Math.random(), openFeatues, id);
};

//编辑记录
PageUtils.editrecord = function(applicationName, formName, id, openFeatues, workItemId, param) {
   PageUtils.openurl(RequestUtils.getContextPath() + "/" + applicationName + "/" + formName + ".shtml?act=edit" + (param ? "&" + param : "") +  "&id=" + id + (workItemId && workItemId>0 ? "&workItemId=" + workItemId : "") + "&seq=" + Math.random(), openFeatues, id);
};

//打开URL
PageUtils.openurl = function(url, openFeatues, name) {
   var fullScreen = false;
   if(!openFeatues) {
      openFeatues = "";
   }
   else {
      var mode = PageUtils._getOpenFeatue(openFeatues, "mode");
      var width = PageUtils._getOpenFeatue(openFeatues, "width");
      var height = PageUtils._getOpenFeatue(openFeatues, "height");
      var sWidth = screen.availWidth;
      var sHeight = screen.availHeight;
      if(mode=="dialog") {
      	  DialogUtils.openDialog(url, width, height);
      	  return;
      }
      else if(mode=="fullscreen") {
         fullScreen = true;
         openFeatues = "left=0,top=0,width=" +(sWidth - 8) + ",height=" +(sHeight - 24);
      }
      else if(mode=="center") {
         if(width != null && height != null) {
            openFeatues = "left=" +((sWidth - width) /2)+",top="+((sHeight-height)/ 2) + ",width=" + width + ",height=" + height;
         }
      }
      else {
         openFeatues = width == null ? "" : "width=" + width;
         openFeatues += height == null ? "" :(openFeatues == "" ? "" : ",") + "height=" + height;
      }
      openFeatues += ",";
   }
   name = (name ? ("" + name).replace(new RegExp("[-./]", "g"), "") : "");
   var win = window.open(url, name, (!BrowserInfo.isIE() || BrowserInfo.getIEVersion()>=7 ? "" : openFeatues + "scrollbars=yes,status=no,resizable=yes,toolbar=no,menubar=no,location=no"), false);
   if(fullScreen) {
   		try {
	   		win.resizeTo(sWidth, sHeight);
	   	}
	   	catch(e) {
	   		
	   	}
   }
   try {
   		win.focus();
   }
   catch(e) {
   		
   }
};

//发送邮件
PageUtils.sendMail = function(mailAddress, name) {
	PageUtils.openurl(RequestUtils.getContextPath() + "/webmail/mail.shtml?mailTo=" + StringUtils.utf8Encode(name ? "\"" + name + "\" &lt;" + mailAddress + "&gt;" : mailAddress), "width=760,height=520", "mail");
};

//获取页面打开参数
PageUtils._getOpenFeatue = function(openFeatues, name) {
	var index = openFeatues.indexOf(name + "=");
	if(index==-1) {
		return null;
	}
	index += name.length + 1;
	var indexNext = openFeatues.indexOf(",", index);
	return indexNext==-1 ? openFeatues.substring(index) : openFeatues.substring(index, indexNext);
};

//显示提示信息,showSecnonds==0时一直显示
PageUtils.showToast = function(message, showSecnonds) {
	if(!window.toast) {
		window.toastCover = PageUtils.createCover(window, 0, true);
		window.toast = document.createElement("div");
		window.toast.style.cssText = "float:right; border:#aaaaaa 1px solid; background:#ffff66; padding:3px; margin-top:2px; margin-right:2px; font-family:宋体; font-size:12px";
		window.toastCover.appendChild(window.toast);
	}
	if(!message || message=='') {
		PageUtils.removeToast();
		return;
	}
	window.toast.innerHTML = message;
	if(window.toastTimer) {
		window.clearTimeout(window.toastTimer);
		window.toastTimer = null;
	}
	if(showSecnonds && showSecnonds > 0) {
		window.toastTimer = window.setTimeout(function() {
			PageUtils.removeToast();
		}, showSecnonds * 1000);
	}
};
//移除提示信息
PageUtils.removeToast = function() {
	if(!window.toast) {
		return;
	}
	if(window.toastTimer) {
		window.clearTimeout(window.toastTimer);
		window.toastTimer = null;
	}
	PageUtils.destoryCover(window, window.toastCover);
	window.toast = null;
	window.toastCover = null;
};

PageUtils.getTopWindow = function() { //获取当前窗口隶属的顶层窗口或帧frame
	var topWindow = window;
	while(topWindow.frameElement) {
		if(topWindow.frameElement.tagName.toLowerCase() == "frame") {
			break;
		}
		var win = topWindow.frameElement.ownerDocument.parentWindow ? topWindow.frameElement.ownerDocument.parentWindow : topWindow.frameElement.ownerDocument.defaultView;
		if(!win) {
			break;
		}
		topWindow = win;
	}
	return topWindow;
};

//创建div覆盖当前窗口,屏蔽对本窗口的操作
PageUtils.createCover = function(parentWindow, opacity, scrollBarEnable) {
	var cover = parentWindow.document.createElement("div");
	cover.style.position = 'absolute';
	cover.id = 'cover';
	//创建iframe,避免因div优先级低不能覆盖select,iframe等元素
	var coverFrame = parentWindow.document.createElement('iframe') ;
	coverFrame.frameBorder = 0 ;
	coverFrame.allowTransparency = true ;
	coverFrame.style.position = 'absolute';
	coverFrame.style.left = '0px';
	coverFrame.style.top = '0px';
	coverFrame.style.width = '100%';
	coverFrame.style.height = '100%';
	coverFrame.style.filter = 'alpha(opacity=0);';
	coverFrame.style.opacity = 0;
	cover.appendChild(coverFrame);
	
	var maskDiv = parentWindow.document.createElement('div') ;
	maskDiv.style.position = 'absolute';
	maskDiv.style.left = '0px';
	maskDiv.style.top = '0px';
	maskDiv.style.width = '100%';
	maskDiv.style.height = '100%';
	maskDiv.style.backgroundColor = "#000";
	maskDiv.style.filter = 'alpha(opacity=' + opacity + ');';
	maskDiv.style.opacity = (opacity / 100.0);
	cover.appendChild(maskDiv);
	
	var childNodes = parentWindow.document.body.childNodes;
	if(childNodes.length==0) {
		parentWindow.document.body.appendChild(cover);
	}
	else {
		parentWindow.document.body.insertBefore(cover, childNodes[0]);
	}
	if(!parentWindow.coverLevel) {
		parentWindow.coverLevel = 0;
	}
	parentWindow.coverLevel++;
	//隐藏滚动条
	if(!scrollBarEnable && !parentWindow.scrollBarHidden) {
		try {
			parentWindow.scrollBarHidden = parentWindow.coverLevel;
			var clientWidth = parentWindow.document.body.clientWidth;
			//保存原来的样式
			cover.savedOverflowX = parentWindow.document.body.style.overflowX;
			cover.savedOverflowY = parentWindow.document.body.style.overflowY;
			cover.savedDocumentElementOverflowX = parentWindow.document.documentElement.style.overflowX;
			cover.savedDocumentElementOverflowY = parentWindow.document.documentElement.style.overflowY;
			cover.savedMarginRight = parentWindow.document.body.style.marginRight;
			//隐藏滚动条
			parentWindow.document.body.style.overflowX = 'hidden';
			parentWindow.document.body.style.overflowY = 'hidden';
			parentWindow.document.documentElement.style.overflowX = 'hidden';
			parentWindow.document.documentElement.style.overflowY = 'hidden';
			
			if(parentWindow.document.body.clientWidth!=clientWidth) { //隐藏滚动条, 宽度已经改变
				//修改右侧边距
				var marginRight = 0;
				try {
					marginRight = Number(parentWindow.document.body.style.marginRight.replace("px", ""));
				}
				catch(e) {
				
				}
				parentWindow.document.body.style.marginRight = (marginRight + (parentWindow.document.body.clientWidth - clientWidth)) + "px";
			}
		}
		catch(e) {
			
		}
	}
	//设置zIndex
	cover.style.zIndex = DomUtils.getMaxZIndex(parentWindow.document.body) + 1;
	//调整尺寸
	var adjustCoverSize = function()  {
		cover.style.left = DomUtils.getScrollLeft(parentWindow.document) + 'px';
		cover.style.top = DomUtils.getScrollTop(parentWindow.document) + 'px';
		cover.style.width = coverFrame.style.width = maskDiv.style.width = DomUtils.getClientWidth(parentWindow.document) + 'px';
		cover.style.height = coverFrame.style.height = maskDiv.style.height = DomUtils.getClientHeight(parentWindow.document) + 'px';
	};
	adjustCoverSize.call(null);
	//事件绑定
	cover.adjustCoverSize = adjustCoverSize;
	EventUtils.addEvent(parentWindow, 'resize', adjustCoverSize);
	EventUtils.addEvent(parentWindow, 'scroll', adjustCoverSize);
	return cover;
};
//销毁cover
PageUtils.destoryCover = function(parentWindow, cover) {
	//解除事件绑定
	EventUtils.removeEvent(parentWindow, 'resize', cover.adjustCoverSize);
	EventUtils.removeEvent(parentWindow, 'scroll', cover.adjustCoverSize);
	cover.adjustCoverSize = null;
	if(parentWindow.coverLevel==parentWindow.scrollBarHidden) { //已经是隐藏滚动条的cover
		parentWindow.scrollBarHidden = null;
		//恢复显示滚动条
		try {
			parentWindow.document.body.style.overflowX = cover.savedOverflowX;
			parentWindow.document.body.style.overflowY = cover.savedOverflowY;
		}
		catch(e) {
			
		}
		try {
			parentWindow.document.documentElement.style.overflowX = cover.savedDocumentElementOverflowX;
			parentWindow.document.documentElement.style.overflowY = cover.savedDocumentElementOverflowY;
		}
		catch(e) {
			
		}
		try {
			parentWindow.document.body.style.marginRight = cover.savedMarginRight;
		}
		catch(e) {
			
		}
	}
	cover.parentNode.removeChild(cover);
	parentWindow.coverLevel--;
};
PageUtils.requestFullScreen = function() { //全屏
	var documentElement = document.documentElement; 
	if(documentElement.requestFullscreen) { //W3C 
		documentElement.requestFullscreen();
	}
	else if(documentElement.mozRequestFullScreen) { //FireFox 
		documentElement.mozRequestFullScreen();
	}
	else if(documentElement.webkitRequestFullScreen) { //Chrome等
		documentElement.webkitRequestFullScreen();
	}
	else if(documentElement.msRequestFullscreen) { //IE11
		documentElement.msRequestFullscreen();
	}
};
PageUtils.exitFullScreen = function() { //退出全屏
	if(document.exitFullscreen) { 
		document.exitFullscreen(); 
	} 
	else if(document.mozCancelFullScreen) { 
		document.mozCancelFullScreen(); 
	} 
	else if(document.webkitCancelFullScreen) { 
		document.webkitCancelFullScreen(); 
	} 
	else if(document.msExitFullscreen) { 
		document.msExitFullscreen(); 
	}
};
PageUtils.isFullScreen = function() { //是否全屏
	if(document.exitFullscreen) { 
		return document.fullscreenElement || document.fullscreen; 
	}
	else if(document.mozCancelFullScreen) { 
		return document.mozFullScreen; 
	}
	else if(document.webkitCancelFullScreen) { 
		return document.webkitIsFullScreen; 
	}
	else if(document.msExitFullscreen) { //ie11
		return document.msFullscreenElement; 
	}
};