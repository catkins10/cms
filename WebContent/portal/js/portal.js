Portal = function(applicationName, pageName, userId, siteId, pageId, portalStyles, portletStyles) {
	this.applicationName = applicationName; //应用名称
	this.pageName = pageName; //页面名称
	this.userId  = userId; //用户ID
	this.siteId = siteId; //站点ID
	this.pageId = pageId; //当前页面ID
	this.portalStyles = portalStyles; //PORTAL样式列表
	this.portletStyles = portletStyles; //PORTLET样式列表
};
Portal.prototype.addPage = function() { //增加页面
	var url = RequestUtils.getContextPath() + '/portal/portalPage.shtml' +
			  '?act=create' +
			  '&applicationName=' + this.applicationName +
			  '&pageName=' + this.pageName + 
			  '&siteId=' + this.siteId + 
			  "&userId=" + this.userId;
	DialogUtils.openDialog(url, 550, 350);
};
Portal.prototype.editPage = function() { //编辑页面
	var url = RequestUtils.getContextPath() + '/portal/portalPage.shtml' +
			  '?act=edit' + 
			  '&id=' + this.pageId +
			  '&applicationName=' + this.applicationName +
			  '&pageName=' + this.pageName + 
			  '&siteId=' + this.siteId +
			  "&userId=" + this.userId;
	DialogUtils.openDialog(url, 550, 350);
};
Portal.prototype.openPage = function(pageId) { //打开页面
	document.cookie = "selectPortalPageId=" + pageId + ";path=/";
	location.replace(location.href);
};
Portal.prototype.addPortlet = function() { //添加PORTLET
	var url = RequestUtils.getContextPath() + '/portal/addPortlet.shtml' +
			  '?applicationName=' + this.applicationName +
			  '&pageName=' + this.pageName + 
			  '&siteId=' + this.siteId +
			  "&userId=" + this.userId +
			  "&pageId=" + this.pageId;
	DialogUtils.openDialog(url, 880, 550);
};
Portal.prototype.getColumnCount = function() { //获取列数
	return document.getElementById('layoutTable').rows[0].cells.length;
};
Portal.prototype.startDragPortlet = function(event, srcElement, portletInstanceId) { //开始拖动PORTLET
	var portal = this;
	this.portletInstance = document.getElementById('portletInstance_' + portletInstanceId);
	if(!document.body.onmousemove) {
		document.body.onmousemove = function(event) {
			event = !event ? window.event : event;
			portal.dragPortlet(event.clientX, event.clientY);
		};
		document.body.onmouseup = function(event) {
			portal.endDragPortlet(!event ? window.event : event);
		};
	}
	document.body.onselectstart = function() {
		return false;
	};
};
Portal.prototype.endDragPortlet = function(event) { //结束拖动PORTLET
	if(!this.portletInstance) {
		return;
	}
	if(this.portletMoveArea) {
		this.portletInstance.style.width = "100%";
		this.portletInstance.style.position = "static";
		this.portletMoveColumn.replaceChild(this.portletInstance, this.portletMoveArea);
		//通知服务器保存设置		
		var portletColumnIndex = this.portletMoveColumn.cellIndex; //列号
		var portletIndex = 0;
		var nodes = this.portletMoveColumn.childNodes;
		for(; portletIndex<nodes.length && nodes[portletIndex]!=this.portletInstance; portletIndex++);
		var portletInstanceId = this.portletInstance.id.substring(this.portletInstance.id.indexOf('_') + 1);
		this.executePortletAction(portletInstanceId, "move", "portletColumnIndex=" + portletColumnIndex + "&portletIndex=" + portletIndex);
	}
	this.portletInstance = null;
	this.portletMoveArea = null;
	document.body.onselectstart = function() {
		return true;
	};
};
Portal.prototype.dragPortlet = function(clientX, clientY, autoScolling) { //拖动PORTLET
	if(!this.portletInstance) {
		return false;
	}
	var changed = false;
	if(this.portletInstance.style.position!="absolute") {
		changed = true;
		var position = DomUtils.getAbsolutePosition(this.portletInstance);
		//计算鼠标位置
		this.xScale = (clientX + DomUtils.getScrollLeft(document) - position.left) / this.portletInstance.offsetWidth;
		this.yScale = (clientY + DomUtils.getScrollTop(document) - position.top) / this.portletInstance.offsetHeight;
		//设置移动区域位置
		this.portletMoveColumn = this.portletInstance.parentNode;
		this.portletMoveBefore = this.portletInstance.nextSibling;
		//设置为绝对坐标
		this.portletInstance.style.position = "absolute";
		this.previousClientY = clientY;
	}
	else {
		var portletLeft = Number(this.portletInstance.style.left.replace("px", ""));
		var portletTop = Number(this.portletInstance.style.top.replace("px", ""));
		if(portletLeft<this.portletMoveLeftBoundary) { //判断是否移动到左边界
			this.portletMoveColumn = this.portletMoveColumn.previousSibling;
			changed = true;
		}
		else if(portletLeft>this.portletMoveRightBoundary) { //判断是否移动到右边界
			this.portletMoveColumn = this.portletMoveColumn.nextSibling;
			changed = true;
		}
		if(changed) { //列已经改变
			this.portletMoveBefore = null;
			var redrag = function() {
				if(window.portal.dragPortlet(clientX, clientY)) {
					window.setTimeout(redrag, 1);
				}
			};
			window.setTimeout(redrag, 1);
		}
		else if(portletTop<this.portletMoveTopBoundary) { //判断是否到达上边界
			this.portletMoveBefore = this.portletMoveArea.previousSibling;
			if(this.portletMoveBefore==this.portletInstance) {
				this.portletMoveBefore = this.portletInstance.previousSibling;
			}
			changed = true;
		}
		else if(portletTop>this.portletMoveBottomBoundary) { //判断是否到达下边界
			this.portletMoveBefore = this.portletMoveArea.nextSibling.nextSibling;
			changed = true;
		}
	}
	if(changed) {
		this.createPortletMoveArea(); //创建移动区域
		this.retrievePortletMoveBoundary(); //计算移动区域边界
	}
	var scrollStep = 0;
	if(clientY <= this.previousClientY && clientY < 50) {
		scrollStep = -8;
	}
	else if(clientY >= this.previousClientY && clientY > DomUtils.getClientHeight(document) - 50) {
		scrollStep = 8;
	}
	this.previousClientX = clientX;
	this.previousClientY = clientY;
	this.portletInstance.style.left = (clientX + DomUtils.getScrollLeft(document) - (this.portletInstance.offsetWidth * this.xScale)) + "px";
	this.portletInstance.style.top = (clientY + DomUtils.getScrollTop(document) + scrollStep - (this.portletInstance.offsetHeight * this.yScale)) + "px";
	document.body.scrollTop += scrollStep;
	if(!autoScolling) {
		this.lastX = clientX;
		this.lastY = clientY;
	}
	if(scrollStep!=0 && !autoScolling) {
		var scrollPortlet = function() {
			if(window.portal.lastX==clientX && window.portal.lastY==clientY) {
				window.portal.dragPortlet(clientX, clientY, true);
				window.setTimeout(scrollPortlet, 20);
			}
		};
		window.setTimeout(scrollPortlet, 20);
	}
	return changed;
};
Portal.prototype.createPortletMoveArea = function() { //创建移动区域
	if(this.portletMoveArea) {
		this.portletMoveArea.parentNode.removeChild(this.portletMoveArea);
	}
	this.portletMoveArea = document.createElement("table");
	this.portletMoveArea.className = "portletMoveArea";
	this.portletMoveArea.border = "0";
	this.portletMoveArea.style.width = "100%";
	this.portletMoveArea.insertRow(-1).insertCell(-1).innerHTML = "&nbsp;";
	if(this.portletMoveBefore) {
		this.portletMoveColumn.insertBefore(this.portletMoveArea, this.portletMoveBefore);
	}
	else {
		this.portletMoveColumn.appendChild(this.portletMoveArea);
	}
	this.portletInstance.style.width = this.portletMoveArea.offsetWidth + "px";
	this.portletMoveArea.style.height = this.portletInstance.offsetHeight + "px";
};
Portal.prototype.retrievePortletMoveBoundary = function() { //计算移动区域边界
	var columns = document.getElementById('layoutTable').rows[0].cells;
	var columnIndex = this.portletMoveColumn.cellIndex;
	//获取当前列的左边坐标
	var position = DomUtils.getAbsolutePosition(this.portletMoveColumn);
	//计算左边界
	if(columnIndex==0) {
		this.portletMoveLeftBoundary = -1000000;
	}
	else if(columns[columnIndex-1].offsetWidth>=this.portletMoveColumn.offsetWidth) { //左侧列宽度>=当前列宽度
		var min = 0.5 * this.portletMoveColumn.offsetWidth; //最右时,移动0.5倍当前列宽度
		var max = Math.min(0.5 * columns[columnIndex-1].offsetWidth, 0.9 * this.portletMoveColumn.offsetWidth); //最左时,移动min(0.5倍左侧列宽度, 0.9倍当前列宽度)
		this.portletMoveLeftBoundary = position.left - min - (1 - this.xScale) * (max - min);
	}
	else { //左侧列宽度<=当前列宽度
		var min = 0.5 * columns[columnIndex-1].offsetWidth; //最左时,移动0.5倍列左侧列宽度
		var max = Math.max(this.portletMoveColumn.offsetWidth * 0.5, this.portletMoveColumn.offsetWidth - 0.9 * columns[columnIndex-1].offsetWidth); //最右时,移动max(当前列宽度*0.5, 前列宽度-0.9倍左侧列宽度)
		this.portletMoveLeftBoundary = position.left - min - this.xScale * (max - min);
	}
	//计算右边界
	if(columnIndex==columns.length-1) {
		this.portletMoveRightBoundary = 1000000;
	}
	else if(columns[columnIndex+1].offsetWidth>=this.portletMoveColumn.offsetWidth) { //右侧列宽度>=当前列宽度
		var min = 0.5 * this.portletMoveColumn.offsetWidth; //最左时,移动0.5倍列当前列宽度
		var max = Math.min(columns[columnIndex+1].offsetWidth * 0.5, 0.9 * this.portletMoveColumn.offsetWidth); //最右时,移动min(0.5倍右侧列宽度, 0.9倍列当前列宽度)
		this.portletMoveRightBoundary = position.left + min + this.xScale * (max - min);
	}
	else { //右侧列宽度<当前列宽度
		var min = 0.5 * columns[columnIndex+1].offsetWidth; //最右时移动0.5倍列右侧列宽度
		var max = Math.max(0.5 * this.portletMoveColumn.offsetWidth, this.portletMoveColumn.offsetWidth - 0.9 * columns[columnIndex+1].offsetWidth); //最左时,移动max(当前列宽度*0.5, 当前列宽度-0.9倍右侧列宽度)
		this.portletMoveRightBoundary = position.left + min + (1 - this.xScale) * (max - min);
	}
	//计算上边界
	var previousElement = this.portletMoveArea.previousSibling;
	if(previousElement==this.portletInstance) {
		previousElement = this.portletInstance.previousSibling;
	}
	if(!previousElement) { //已经是第一个PORTLET
		this.portletMoveTopBoundary = -1000000;
	}
	else {
		this.portletMoveTopBoundary = DomUtils.getAbsolutePosition(previousElement).top + 0.5 * previousElement.offsetHeight;
	}
	//计算下边界
	var nextElement = this.portletMoveArea.nextSibling;
	if(!nextElement) { //已经是最后一个PORTLET
		this.portletMoveBottomBoundary = 1000000;
	}
	else {
		this.portletMoveBottomBoundary = DomUtils.getAbsolutePosition(nextElement).top + 0.5 * nextElement.offsetHeight - this.portletInstance.offsetHeight;
	}
};
Portal.prototype.minimizePortlet = function(portletInstanceId) { //最小化
	document.getElementById('portletBody_' + portletInstanceId).innerHTML = ''; //清空内容
	this.showPortletButtons(portletInstanceId, true);
	this.executePortletAction(portletInstanceId, 'minimize');
};
Portal.prototype.restorePortlet = function(portletInstanceId, portletViewURL) { //还原
	this.showPortletButtons(portletInstanceId, false);
	this.openPortletURL(portletInstanceId, portletViewURL); //重新加载内容
	this.executePortletAction(portletInstanceId, 'restore');
};
Portal.prototype.editPortlet = function(portletInstanceId, portletEditURL) { //编辑
	this.openPortletURL(portletInstanceId, portletEditURL);
};
Portal.prototype.refreshPortlet = function(portletInstanceId, portletViewURL) { //刷新
	this.openPortletURL(portletInstanceId, portletViewURL);
};
Portal.prototype.removePortlet = function(portletInstanceId) { //删除
	var portletInstance = document.getElementById('portletInstance_' + portletInstanceId);
	portletInstance.parentNode.removeChild(portletInstance);
	this.executePortletAction(portletInstanceId, 'remove');
};
Portal.prototype.showPortletButtons = function(portletInstanceId, minimize) { //删除{
	document.getElementById('portletMinimizeButton_' + portletInstanceId).style.display = minimize ? 'none' : ''; //最小化按钮
	document.getElementById('portletRestoreButton_' + portletInstanceId).style.display = !minimize ? 'none' : ''; //还原按钮
	var button = document.getElementById('portletEditButton_' + portletInstanceId);
	if(button) {
		button.style.display = minimize ? 'none' : ''; //编辑按钮
	}
	document.getElementById('portletRefreshButton_' + portletInstanceId).style.display = minimize ? 'none' : ''; //刷新按钮
};
Portal.prototype.executePortletAction = function(portletInstanceId, portletAction, parameter) { //执行PORTLET操作
	var jsFile = RequestUtils.getContextPath() + '/portal/portlet.js.shtml' +
				 '?applicationName=' + this.applicationName +
			  	 '&pageName=' + this.pageName +
				 '&siteId=' + this.siteId +  //站点ID
				 '&userId=' + this.userId + //用户ID
				 '&pageId=' + this.pageId + //页面ID
				 '&portletInstanceId=' + portletInstanceId + //PORTLET实体ID
				 '&portletAction=' + portletAction + //PORTLET操作,move/minimize/restore/remove
				 (parameter ? '&' + parameter : '') +
				 '&seq=' + Math.random();
	ScriptUtils.appendJsFile(document, jsFile, "portletJs");
};
Portal.prototype.openPortletURL = function(portletInstanceId, portletURL) {
	var portletBody = document.getElementById('portletBody_' + portletInstanceId);
	//创建iframe
	var iframe;
	try {
		iframe = document.createElement("<iframe onload=\"window.portal.writePortletBody(this, document.getElementById('portletBody_" + portletInstanceId + "'))\"></iframe>");
	}
	catch(e) {
		iframe = document.createElement("iframe");
		var portal = this;
		iframe.onload = function() {
			portal.writePortletBody(iframe, portletBody); //iframe加载完成后,重写portlet内容
		};
	}
	iframe.style.display = "none";
	iframe.src = portletURL + "&seq=" + Math.random();
	portletBody.appendChild(iframe);
};
Portal.prototype.onFormActionProcessed = function(iframe, portletInstanceId) {
	this.writePortletBody(iframe, document.getElementById('portletBody_' + portletInstanceId));
};
Portal.prototype.writePortletBody = function(iframe, portletBody) {
	var childNodes = iframe.contentWindow.document.body.childNodes;
	portletBody.innerHTML = "";
	for(var i=0; i<(!childNodes ? 0 : childNodes.length); i++) {
		this.importNode(document, portletBody, childNodes[i], true);
	}
};
Portal.prototype.importNode = function(doc, parentElement, srcNode, deep) {
	if(srcNode.nodeName=="#text") {
		parentElement.appendChild(doc.createTextNode(srcNode.nodeValue));
		return;
	}
	if(srcNode.nodeName=='SCRIPT' && !srcNode.src) {
		doc.write = function(html) {
			parentElement.innerHTML = html;
		};
		var script = srcNode.innerHTML;
		eval(script.replace(/\/\/lazyload/, ''));
		return;
	}
	var targetElement;
	if(doc.importNode) { //firefox
		targetElement = doc.importNode(srcNode, false);
	}
	else { //IE
		var outerHTML = srcNode.outerHTML;
		targetElement = doc.createElement(outerHTML.substring(0, outerHTML.indexOf('>') + 1).trim());
	}
	if(parentElement!=null) {
		parentElement.appendChild(targetElement);
	}
	//复制下级
	if(targetElement.tagName!="STYLE") { //不是样式表
		for(var i=0; i<(deep && srcNode.tagName!="SCRIPT" && srcNode.childNodes ? srcNode.childNodes.length : 0); i++) {
			this.importNode(doc, targetElement, srcNode.childNodes[i], deep);
		}
		return;
	}
	//复制样式表
	srcNode.id = Math.random();
	if(!targetElement.id) {
		targetElement.id = srcNode.id;
	}
	var styleSheet = srcNode.ownerDocument.styleSheets[srcNode.id];
	var css = doc.styleSheets[targetElement.id];
	for(var i=0; i<styleSheet.rules.length; i++) {
		var cssText = styleSheet.rules[i].style.cssText;
		if(!cssText || cssText=="") {
			continue;
		}
		try {
			if(css.cssRules) { //firefox
				css.insertRule(styleSheet.rules[i].selectorText + "{" + cssText + "}", i);
			}
			else {
				css.addRule(styleSheet.rules[i].selectorText, styleSheet.rules[i].style.cssText);
			}
		}
		catch(e) {
		
		}
	}
};

//PORTAL样式
PortalStyle = function(name, icon, iconWidth, iconHeight, cssFileName) {
	this.name = name;
	this.icon = icon;
	this.iconWidth = iconWidth;
	this.iconHeight = iconHeight;
	this.cssFileName = cssFileName;
};