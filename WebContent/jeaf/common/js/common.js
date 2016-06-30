EventUtils = function() {
};
EventUtils.clickElement = function(element) { 
	if(document.all) { 
		element.click();
		return;
	}
	if(element.onclick) {
		element.onclick();
		return;
	}
	var win = (element.ownerDocument.parentWindow ? element.ownerDocument.parentWindow : element.ownerDocument.defaultView);
	var evt = win.document.createEvent("MouseEvents");
	evt.initMouseEvent('click', false, true, win, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
	element.dispatchEvent(evt);
};
EventUtils.createTouchEevent = function(element, eventType, pageX, pageY, screenX, screenY, clientX, clientY) { 
	var event = element.ownerDocument.createEvent('TouchEvent'); 
	
	var touch = element.ownerDocument.createTouch(window, element, 1, pageX, pageY, screenX, screenY, clientX, clientY, 0, 0, 0, true);
	var touches = element.ownerDocument.createTouchList(touch);
	var targetTouches = element.ownerDocument.createTouchList(touch);
	var changedTouches = element.ownerDocument.createTouchList(touch);
	
	event.initTouchEvent(eventType, true, true, window, null, screenX, screenY, clientX, clientY, false, false, false, false, touches, targetTouches, changedTouches, 1, 0);
	element.dispatchEvent(event);
};
EventUtils.addEvent = function(element, eventName, func) { 
	if(element.attachEvent) { 
		try {
			element.attachEvent(eventName, func);
		}
		catch(e) {
			
		}
		try {
			element.attachEvent("on" + eventName, func);
		}
		catch(e) {
			
		}
	}
	else if(element.addEventListener) { 
		element.addEventListener(eventName, func, true);
	}
	else { 
		element["on" + eventName] = func;
	}
};
EventUtils.removeEvent = function(element, eventName, func) { 
	if(element.detachEvent) { 
		element.detachEvent("on" + eventName, func);
	}
	else if(element.removeEventListener) { 
		element.removeEventListener(eventName, func, true);
	}
	else { 
		//element["on" + eventName] = func;
	}
};
EventUtils.stopPropagation = function(event) { 
	if(event.stopPropagation) {
		event.stopPropagation();
	}
	else {
		event.cancelBubble = true;
	}
};

FormField = function(inputElementHTML, fieldType, style, styleClass, isTextArea) { 
	this.inputElementHTML = inputElementHTML;
	this.id = "field_" + ("" + Math.random()).substring(2);
	this.fieldType = fieldType;
	this.style = !style || style=='null' || style=='' ? '' : style + (style.substring(style.length-1)==';' ? '' : ';');
	this.styleClass = !styleClass || styleClass=='null' ? '' : styleClass;
	this.isTextArea = isTextArea;
};
FormField.prototype.writeFieldElement = function(fieldBodyHTML, parentElement) { 
	var html = (this.fieldType=='hidden' ? this.resetInputElementHTML() : '') +
			   '<div id="' + this.id + '"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="display:block; ' + this.style + 'text-indent: 0px !important; background-color:transparent !important; padding:0px 0px 0px 0px !important; border: #ffffff 0px none !important; outline:none !important;">\n' +
		   	   '	<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">\n' +
		   	   '		<tr>' +
		   	   '			<td' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.style + 'width:100% !important; height:100% !important; text-indent: 0px !important; outline:none !important;">' +
			   '		' + fieldBodyHTML +
			   '			</td>' +
			   '		</tr>' +
			   '	</table>\n' +
			   '</div>';
	if(parentElement) {
		this.document = parentElement.ownerDocument;
		parentElement.innerHTML = html;
	}
	else {
		this.document = document;
		document.write(html);
	}
	this.getInputElement().formField = this;
};
FormField.prototype.resetInputElementHTML = function() { 
	this._resetAttribute("id", 'input_' + this.id);
	if(this.fieldType) {
		this._resetAttribute("type", this.fieldType);
	}
	if(this.fieldType!='hidden') {
		this._resetAttribute("style", this.getInputElementCssText());
		this._resetAttribute("class", this.styleClass);
	}
	return this.inputElementHTML;
};
FormField.prototype._resetAttribute = function(attributeName, attributeValue) { 
	var index = this.inputElementHTML.indexOf(attributeName + '="');
	if(index==-1) {
		index = this.inputElementHTML.indexOf(' ');
		this.inputElementHTML = this.inputElementHTML.substring(0, index) + ' ' + attributeName + '="' + attributeValue + '"' + this.inputElementHTML.substring(index);
	}
	else {
		index += (attributeName + '="').length;
		var indexEnd = this.inputElementHTML.indexOf('"', index);
		this.inputElementHTML = this.inputElementHTML.substring(0, index) + attributeValue + this.inputElementHTML.substring(indexEnd);
	}
};
FormField.prototype.getAttribute = function(attributeName) { 
	var index = this.inputElementHTML.indexOf(attributeName + '="');
	if(index==-1) {
		return "";
	}
	index += (attributeName + '="').length;
	var indexEnd = this.inputElementHTML.indexOf('"', index);
	return this.inputElementHTML.substring(index, indexEnd);
};
FormField.prototype.getFieldElement = function() { 
	return this.document.getElementById(this.id);
};
FormField.prototype.getInputElement = function() { 
	return this.document.getElementById('input_' + this.id);
};
FormField.prototype.getInputElementCssText = function() { 
	var height = this.styleClass=='' ? null : CssUtils.getStyleByName(document, '.' + this.styleClass, 'height');
	if(!height || height=='') {
		height = CssUtils.getStyle(this.style, 'height');
	}
	return (this.isTextArea ? "" : ";line-height:normal !important") + ';' + this.style + (!height || height=='' ? '' : 'height:100% !important;') + 'width:100% !important; background-color:transparent !important; padding:1px 0px 1px 1px !important; margin:0px !important; margin-left:-1px !important; border: 0px #fff none !important; outline:none !important;';
};
FormField.getFormField = function(fieldName) { 
	var field = document.getElementsByName(fieldName)[0];
	return field ? field.formField : null;
};

FormField.Picker = function(pickerTitle, pickerHtml, displayArea, pickerWidth, pickerHeight, alignRight, autoSize, transparentDisabled, coverDisabled, terminalType) {
	if(!pickerHtml || pickerHtml=='') {
		return;
	}
	this.pickerTitle = pickerTitle;
	this.displayArea = displayArea; 
	this.pickerWidth = (pickerWidth && pickerWidth>0 ? pickerWidth : (!this.displayArea ? 200 : this.displayArea.offsetWidth)); 
	this.pickerHeight = (pickerHeight && pickerHeight>0 ? pickerHeight : 165); 
	this.autoSize = autoSize; 
	this.alignRight = alignRight; 
	this.transparentDisabled = transparentDisabled; 
	this.coverDisabled = coverDisabled;
	this.isTouchMode = terminalType && terminalType!='' && terminalType!='computer';
	this.pickerHTML = typeof pickerHtml == 'function' ? pickerHtml.call(this) : pickerHtml;
	if(!this.pickerHTML || this.pickerHTML=='') {
		return;
	}
	this.create();
	this.created = true;
};
FormField.Picker.prototype.create = function() {
	if(!this.isTouchMode) {
		this._createComputerPicker();
	}
	else {
		this._createTouchPicker();
	}
};
FormField.Picker.prototype.show = function(pickerLeft, pickerTop) { 
	if(!this.created) {
		var picker = this;
		window.setTimeout(function() {
			picker.show(pickerLeft, pickerTop);
		}, 30);
		return;
	}
	if(!this.isTouchMode) {
		this._showComputerPicker(pickerLeft, pickerTop);
	}
	else {
		this._showTouchPicker(pickerLeft, pickerTop);
	}
};
FormField.Picker.prototype.hide = function() {
	this.pickerContainer.style.display = 'none';
	if(this.cover) {
		this.cover.style.display = 'none';
	}
};
FormField.Picker.prototype.destory = function() {
	if(!this.isTouchMode) {
		this._destoryComputerPicker();
	}
	else {
		this._destoryTouchPicker();
	}
};
FormField.Picker.prototype.getRelationFields = function() {
	
};
FormField.Picker.prototype._createComputerPicker = function() {
	this.topWindow = PageUtils.getTopWindow(); 
	if(!this.coverDisabled) {
		var picker = this;
		this.cover = PageUtils.createCover(this.topWindow, 0, true);
		this.cover.onclick = function() { 
			picker.destory();
		};
	}
	
	this.pickerContainer = this.topWindow.document.createElement("div");
	this.pickerContainer.style.zIndex = this.cover ? Number(this.cover.style.zIndex) + 1 : DomUtils.getMaxZIndex(this.topWindow.document.body) + 1; 
	this.pickerContainer.style.position = "absolute";
	this.pickerContainer.style.width = this.pickerWidth + "px";
	this.pickerContainer.style.height = "1px";
	this.pickerContainer.style.visibility = 'hidden';
	this.topWindow.document.body.insertBefore(this.pickerContainer, this.topWindow.document.body.childNodes[0]);
	this.pickerContainer.innerHTML = '<iframe style="width:' + this.pickerWidth + 'px; height:1px;" frameborder="0" scrolling="no"' + (this.transparentDisabled ? '' : ' allowTransparency="true"') + '></iframe>';
	
	this.pickerFrame = this.pickerContainer.getElementsByTagName("iframe")[0].contentWindow;
	var doc = this.pickerFrame.document;
	doc.open();
	doc.write(this.pickerHTML);
	CssUtils.cloneStyle(document, doc); 
	doc.close();
};
FormField.Picker.prototype._showComputerPicker = function(pickerLeft, pickerTop) {
	this.pickerContainer.style.visibility = 'hidden';
	this.pickerContainer.style.display = '';
	if(this.cover) {
		this.cover.style.display = '';
	}
	
	if(this.displayArea) {
		var pos = DomUtils.getAbsolutePosition(this.displayArea, null, true);
		pickerLeft = pos.left;
		pickerTop = pos.top;
	}
	
	var body = this.topWindow.document.body;
	var bottomSpacing = DomUtils.getClientHeight(this.topWindow.document) - (pickerTop + (this.displayArea ? this.displayArea.offsetHeight : 0) - DomUtils.getScrollTop(this.topWindow.document)) - 3; 
	var topSpacing = pickerTop - DomUtils.getScrollTop(this.topWindow.document) - 3; 
	
	
	var width = this.pickerWidth;
	if(this.autoSize) { 
		this.pickerContainer.getElementsByTagName("iframe")[0].style.width = "1px";
		width = Math.max(this.pickerWidth, this.pickerFrame.document.body.scrollWidth);
	}
	this.pickerContainer.style.width = width + "px";
	this.pickerContainer.getElementsByTagName("iframe")[0].style.width = width + "px";
	
	var height = this.pickerHeight;
	if(this.autoSize) {
		this.pickerContainer.getElementsByTagName("iframe")[0].style.height = "1px";
		height = Math.min(this.pickerFrame.document.body.scrollHeight, Math.max(bottomSpacing, topSpacing));
	}
	this.pickerContainer.style.height = height + "px";
	this.pickerContainer.getElementsByTagName("iframe")[0].style.height = height + "px";
	
	
	if(!this.displayArea && this.autoSize) {
		this.pickerContainer.style.top = Math.max(DomUtils.getScrollTop(this.topWindow.document), Math.min(pickerTop, DomUtils.getScrollTop(this.topWindow.document) + DomUtils.getClientHeight(this.topWindow.document) - height)) + "px";
	}
	else if(bottomSpacing>=height || topSpacing<height) { 
		this.pickerContainer.style.top = (pickerTop + (this.displayArea ? this.displayArea.offsetHeight : 0) - 2) + "px";
	}
	else { 
		this.pickerContainer.style.top = (pickerTop - height + 1) + "px";
	}
	
	if(this.displayArea) {
		this.pickerContainer.style.left = Math.max(3, Math.min(this.alignRight ? pickerLeft + this.displayArea.offsetWidth - width : pickerLeft, DomUtils.getScrollLeft(this.topWindow.document) + DomUtils.getClientWidth(this.topWindow.document) - this.pickerContainer.offsetWidth - 3)) + "px";
	}
	else {
		this.pickerContainer.style.left = Math.min(pickerLeft, DomUtils.getScrollLeft(this.topWindow.document) + DomUtils.getClientWidth(this.topWindow.document) - this.pickerContainer.offsetWidth - 3) + "px";
	}
	
	this.pickerFrame.document.body.scrollTop = 0;
	this.pickerContainer.style.visibility = 'visible';
};
FormField.Picker.prototype._destoryComputerPicker = function() {
	if(this.cover) {
		PageUtils.destoryCover(this.topWindow, this.cover);
	}
	this.pickerContainer.parentNode.removeChild(this.pickerContainer);
};
FormField.Picker.prototype._createTouchPicker = function() {
	this.topWindow = PageUtils.getTopWindow(); 
	this.cover = PageUtils.createCover(this.topWindow, 20, false);
	this.pickerContainer = this.topWindow.document.createElement('div');
	this.pickerContainer.className = 'touchPicker';
	this.pickerContainer.style.visibility = 'hidden';
	this.cover.appendChild(this.pickerContainer);
	var html = '<div class="touchPickerTitle">' + this.pickerTitle + '</div>' +
			   '<div class="pickerBody" id="pickerBody">' + this.pickerHTML + '</div>' +
			   '<div>' +
			   '	<table width="100%" border="0" cellspacing="0" cellpadding="0">' +
			   '		<tr>' +
			   '			<td class="touchPickerCancelButton" id="touchPickerCancelButton">\u53D6\u6D88</td>' +
			   '			<td class="touchPickerOkButton" id="touchPickerOkButton">\u786E\u5B9A</td>' +
			   '		</tr>' +
			   '	</table>' +
			   '</div>';
	this.pickerContainer.innerHTML = html;
	this.pickerBody = this.topWindow.document.getElementById('pickerBody');
	var picker = this;
	this.topWindow.document.getElementById('touchPickerOkButton').onclick = function() {
		 picker.doOK();
		 picker.destory();
	};
	this.topWindow.document.getElementById('touchPickerCancelButton').onclick = function() {
		picker.destory();
	};
};
FormField.Picker.prototype._showTouchPicker = function(pickerLeft, pickerTop) {
	if(this.cover) {
		this.cover.style.display = '';
	}
	this.pickerContainer.style.left = Math.round((this.cover.offsetWidth - this.pickerContainer.offsetWidth)/2) + 'px';
	this.pickerContainer.style.top = Math.round((this.cover.offsetHeight - this.pickerContainer.offsetHeight)/2) + 'px';
	this.pickerContainer.style.visibility = 'visible';
};
FormField.Picker.prototype._destoryTouchPicker = function() {
	PageUtils.destoryCover(this.topWindow, this.cover);
};

Ajax = function() {
	this.xmlHttp = null;
	this.errorCount = 0;
	try {
		this.xmlHttp = new XMLHttpRequest();
	}
	catch(e) {
		try { 
			this.xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
	    }
	    catch(ex) {
			this.xmlHttp = new ActiveXObject("Microsoft.XMLHTTP"); 
		}
	}
}
Ajax.prototype.openRequest = function(method, url, data, async, onDataArriveCallback, onErrorCallback, contentType) {
	try { 
		this.abort();
    }
    catch(e) {
		
	}
	try {
		this.xmlHttp.timeout = async ? 30000 : 0;
	}
    catch(e) {
		
	}
	this.xmlHttp.open(method, url, async);
	var ajax = this;
	this.xmlHttp.onreadystatechange = function() { 
		if(ajax.xmlHttp.readyState!=4) {
			return;
		}
		if(ajax.xmlHttp.status<300 && ajax.xmlHttp.status>=200) { 
			ajax.errorCount = 0;
			if(onDataArriveCallback) {
				onDataArriveCallback.call();
			}
		}
		else if(ajax.xmlHttp.status!=0) { 
			ajax.errorCount++;
			if(onErrorCallback) {
				onErrorCallback.call();
				ajax.errorCount = 0;
			}
		}
	};
	try {
		this.xmlHttp.onerror = this.xmlHttp.onabort  = function() { 
			if(onErrorCallback) {
				onErrorCallback.call();
			}
		};
	}
	catch(e) {
	
	}
	if(contentType) {
		this.xmlHttp.setRequestHeader("Content-Type", contentType);
	}
	this.xmlHttp.send(data);
};
Ajax.prototype.abort = function() { 
	this.xmlHttp.abort();
};
Ajax.prototype.getStatus = function() { 
	return this.xmlHttp.status;
};
Ajax.prototype.getErrorCount = function() { 
	return this.errorCount;
};
Ajax.prototype.getResponseText = function() { 
	return this.xmlHttp.responseText;
};
Ajax.prototype.getResponseXML = function() { 
	return this.xmlHttp.responseXML;
};
Ajax.getText = function(xmlElement) { 
	return document.all ? xmlElement.text : xmlElement.textContent;
};

BrowserInfo = function() {
};
BrowserInfo.isIE = function() {
	return /(msie\s|trident.*rv:)([\w.]+)/.test(navigator.userAgent.toLowerCase()) && !/opera/i.test(navigator.userAgent); 
};
BrowserInfo.getIEVersion = function() {
	return /(msie\s|trident.*rv:)([\w.]+)/.test(navigator.userAgent.toLowerCase()) && !/opera/i.test(navigator.userAgent) ? parseFloat( navigator.userAgent.toLowerCase().match(/(msie\s|trident.*rv:)([\w.]+)/)[2]) : 0;
};
BrowserInfo.isSafari = function() {
	return navigator.userAgent.toLowerCase().indexOf(' applewebkit/')!=-1;		// Read "IsWebKit"
};
BrowserInfo.isOpera = function() {
	return !!window.opera;
};
BrowserInfo.isAIR = function() {
	return navigator.userAgent.toLowerCase().indexOf(' adobeair/')!=-1;
}
BrowserInfo.isMac = function() {
	return navigator.userAgent.toLowerCase().indexOf('macintosh')!=-1;
};
BrowserInfo.isAndroid = function() {
	return navigator.userAgent.toLowerCase().indexOf('android')!=-1;
};
BrowserInfo.isIPhone = function() {
	return navigator.userAgent.toLowerCase().indexOf('iphone')!=-1;
};
BrowserInfo.isIPad = function() {
	return navigator.userAgent.toLowerCase().indexOf('ipad')!=-1;
};
BrowserInfo.getFlashVersion = function() { 
	var plugin;
	try {
		plugin = document.all ? new ActiveXObject('ShockwaveFlash.ShockwaveFlash') : null;
	}
	catch(e) {
	
	}
	if(plugin) {
		var version = plugin.GetVariable("$version");
 		return parseInt(version.split(" ")[1].split(",")[0]); 
 	}
 	else if(navigator.plugins && navigator.plugins.length > 0 && (plugin = navigator.plugins["Shockwave Flash"])) {
		var values = plugin.description.split(" ");
		for(var i = 0; i < values.length; ++i) {
			if(!isNaN(values[i]=parseInt(values[i]))) {
				return values[i];
			}
	 	}
 	}
 	return null;
}

CookieUtils = function() {
};
CookieUtils.getCookie = function(cookieName) {
	var cookies = document.cookie.split("; ");
	for (var i=0; i < cookies.length; i++) {
		var values = cookies[i].split("=");
	    if (cookieName == values[0]) {
	    	return unescape(values[1]);
		}
  	}
  	return null;
};

CssUtils = function() {
};
CssUtils.appendCssFile = function(doc, cssId, cssUrl) {
	var head = DomUtils.getHead(doc);
	var links = head.getElementsByTagName("link");
	if(links && links.length>0) {
		for(var i=0; i<links.length; i++) {
			if(links[i].href==cssUrl) {
				return;
			}
			else if(links[i].id && links[i].id==cssId) {
				links[i].href = cssUrl;
				return;
			}
		}
	}
	var link = doc.createElement("link");
	link.href = cssUrl;
	link.id = cssId;
	link.rel = "stylesheet";
	link.type = "text/css";
	head.appendChild(link);
};
CssUtils._appendStyle = function(doc, styleId, styleSheet) {
    
    var css;
    try {
    	css = styleId && styleId!="" ? doc.styleSheets[styleId] : doc.styleSheets[0];
    }
    catch(e) {
    
    }
	if(!css) {
		
		var head = DomUtils.getHead(doc);
		
	    var style = doc.createElement("style");
	    style.type = "text/css";
	    style.id = styleId;
	    head.appendChild(style);
	    css = styleId && styleId!="" ? doc.styleSheets[styleId] : doc.styleSheets[0];
    }
    
 	for(var i=0; i<styleSheet.rules.length; i++) {
		var cssText = styleSheet.rules[i].style.cssText;
		if(!cssText || cssText=="") {
			continue;
		}
		try {
			if(css.cssRules) { 
				css.insertRule(styleSheet.rules[i].selectorText + "{" + cssText + "}", css.cssRules.length);
			}
			else {
				css.addRule(styleSheet.rules[i].selectorText, cssText);
			}
		}
		catch(e) {
		
		}
	}
};
CssUtils.cloneStyle = function(fromDocument, toDocument) {
	if(!fromDocument || !toDocument || fromDocument==toDocument) {
		return;
	}
	if(!fromDocument.styleSheets) {
		return;
	}
	for(var i=0; i<fromDocument.styleSheets.length; i++) {
		var ownerNode = (fromDocument.styleSheets[i].ownerNode ? fromDocument.styleSheets[i].ownerNode : fromDocument.styleSheets[i].owningElement);
		CssUtils._appendStyle(toDocument, "", fromDocument.styleSheets[i]);
		if("link"==ownerNode.tagName.toLowerCase()) { 
			CssUtils.appendCssFile(toDocument, "", ownerNode.href); 
		}
    }
};
CssUtils.getElementComputedStyle = function(element, styleName) {
	if(!element) {
		return null;
	}
	var style;
	if(!element.currentStyle){ 
		style = element.ownerDocument.defaultView.getComputedStyle(element, null).getPropertyValue(styleName);
	}
	else if(!(style=element.currentStyle[styleName])) { 
		var styleNames = styleName.split('-');
		styleName = styleNames[0];
		for(var i=1; i<styleNames.length; i++) {
			styleName += styleNames[i].substring(0, 1).toUpperCase() + styleNames[i].substring(1);
		}
		eval('style=element.currentStyle.' + styleName + ';');
	}
	return style.replace(/['"]/g, '');
};
CssUtils.getStyle = function(cssText, styleName) {
	if(!cssText) {
		return null;
	}
	styleName = styleName.toLowerCase();
	var styles = cssText.split(";");
	for(var i=0; i<styles.length; i++) {
		var index = styles[i].indexOf(":");
		if(index!=-1 && styleName==styles[i].substring(0, index).toLowerCase().trim()) {
			return styles[i].substring(index+1).trim();
		}
	}
	return null;
};
CssUtils._getStyleRuleByName = function(document, rullName) {
	rullName = rullName.toLowerCase();
	for(var i=0; i<document.styleSheets.length; i++) {
		var styleSheet = document.styleSheets[i];
		for(var j=0; j<styleSheet.rules.length; j++) {
			var name = styleSheet.rules[j].selectorText.toLowerCase();
			if(name==rullName || name.indexOf(rullName + ",")==0 || name.indexOf(", " + rullName + ",")!=-1 || (name.indexOf(", " + rullName)!=-1 && name.indexOf(", " + rullName) + rullName.length + 1!=name.length)) {
				return styleSheet.rules[j];
			}
		}
    }
};
CssUtils.getStyleByName = function(document, rullName, styleName) {
	var rule = CssUtils._getStyleRuleByName(document, rullName);
	if(rule) {
		return eval("rule.style." + styleName);
	}
};
CssUtils.setGray = function(element) {
	var cssText = "-moz-opacity: 0.2;" +
				  "opacity: 0.2;" +
				  "-webkit-filter: grayscale(100%);" + 
				  "-moz-filter: grayscale(100%);" +
				  "-ms-filter: grayscale(100%);" +
				  "-o-filter: grayscale(100%);" + 
				  "filter: Alpha(opacity=20) progid:DXImageTransform.Microsoft.BasicImage(grayscale=1);";
	element.style.cssText += cssText;
};
CssUtils.removeGray = function(element) {
	var cssText = "-moz-opacity: 1;" +
				  "opacity: 1;" +
				  "-webkit-filter: grayscale(0%);" + 
				  "-moz-filter: grayscale(0%);" +
				  "-ms-filter: grayscale(0%);" +
				  "-o-filter: grayscale(0%);" + 
				  "filter: Alpha(opacity=100)";
	element.style.cssText += cssText;
};
CssUtils.setDegree = function(element, degree) {
	degree = (degree < 0 ? 360 : 0) + degree % 360;
	element.style.cssText += '-moz-transform: rotate(' + degree + 'deg);' +
	    					 '-o-transform: rotate(' + degree + 'deg);' +
	   						 '-webkit-transform: rotate(' + degree + 'deg);' +
	    					 'transform: rotate(' + degree + 'deg);';
};
CssUtils.rotate = function(element, step, interval, targetDegree) {
	var currentDegree = 0;
	var match;
	if(element.style.cssText && (match = element.style.cssText.match(/rotate\([^\)]*deg\)/i))) {
		match = match.toString();	
		currentDegree = Number(match.substring('rotate('.length, match.length - 'deg)'.length));
		currentDegree = (currentDegree < 0 ? 360 : 0) + currentDegree % 360;
	}
	if(targetDegree!=-1) {
		step = (targetDegree > currentDegree  ? 1 : -1) * Math.abs(step);
	}
	CssUtils.stopRotate(element);
	element.rotateTimer = window.setInterval(function() {
		currentDegree += step;
		if(targetDegree!=-1 && ((step<0 && currentDegree<=targetDegree) || (step>0 && currentDegree>=targetDegree))) {
			CssUtils.stopRotate(element);
			CssUtils.setDegree(element, targetDegree);
			return;
		}
		CssUtils.setDegree(element, currentDegree);
	}, interval);
};
CssUtils.stopRotate = function(element) {
	if(element.rotateTimer && element.rotateTimer!=0) {
		window.clearInterval(element.rotateTimer);
		element.rotateTimer = 0;
		CssUtils.setDegree(element, 0);
	}
};
CssUtils.setOpacity = function(element, opacity) {
	opacity = Math.max(0, opacity);
	element.style.cssText += 'filter: alpha(opacity=' + (opacity*100) + '); opacity: ' + opacity + ';'
};

DialogUtils = function() {
};
DialogUtils.openDialog = function(dialogUrl, width, height, dialogTitle, dialogArguments, onDialogLoad, onDialogBodyLoad, onDialogClose) {
	var topWindow = PageUtils.getTopWindow();
	var clientWidth = DomUtils.getClientWidth(topWindow.document);
	var clientHeight = DomUtils.getClientHeight(topWindow.document);
	if(("" + width).lastIndexOf('%')!=-1) {
		width = clientWidth * Number(width.substring(0, width.length - 1)) / 100;
	}
	if(("" + height).lastIndexOf('%')!=-1) {
		height = clientHeight * Number(height.substring(0, height.length - 1)) / 100;
	}
	
	var cover = PageUtils.createCover(topWindow, 6);
	var top  = Math.max((cover.offsetHeight - height - 20) / 2, 0);
	var left = Math.max((cover.offsetWidth - width - 20)  / 2, 0);
	
	
	var dialog = topWindow.document.createElement('iframe');
	dialog.dialogUrl = dialogUrl;
	if(onDialogLoad) {
		dialog.onDialogLoad = onDialogLoad;
	}
	if(onDialogBodyLoad) {
		dialog.onDialogBodyLoad = onDialogBodyLoad;
	}
	if(onDialogClose) {
		dialog.onDialogClose = onDialogClose;
	}
	dialog.frameBorder = 0 ;
	dialog.allowTransparency = true;
	
	var siteId = StringUtils.getPropertyValue(location.href, "siteId");
    if(!siteId || siteId=='') {
       siteId = DomUtils.getMetaContent(document, "siteIdMeta");
    }
	if((!siteId || siteId=='') && document.getElementsByName("siteId")[0]) {
       siteId = document.getElementsByName("siteId")[0].value;
    }
	var url = RequestUtils.getContextPath() + "/jeaf/dialog/dialog.shtml";
  	url += siteId && siteId!="" && siteId!="0" ? '?siteId=' + siteId : ''; 
    url += (dialogTitle ? (url.indexOf('?')==-1 ? '?' : '&') + "dialogTitle=" + StringUtils.utf8Encode(dialogTitle) : "");
	dialog.src = url;
	dialog.style.cssText = 'position: absolute; z-index:' + (Number(cover.style.zIndex) + 1) + '; left:' + left + 'px; top:' + top + 'px; width:' + width + 'px; height:' + height + 'px; visibility: hidden; box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.3)';
	dialog.opener = window;
	dialog.id = "dialog";
	dialog.dialogArguments = dialogArguments;
	cover.appendChild(dialog);
};
DialogUtils.closeDialog = function() { 
	if(!window.frameElement) { 
		window.close();
		return;
	}
	var win = PageUtils.getTopWindow();
	var dialogFrame = DialogUtils.getDialogFrame();
	if(dialogFrame.onDialogClose) {
		dialogFrame.onDialogClose.call(null, dialogFrame.contentWindow.document.getElementById("dialogBody"));
	}
	PageUtils.destoryCover(win, dialogFrame.parentNode);
	
	try {
		var range = DomSelection.createRange(win.document, win.document.body);
		range.collapse(true);
		DomSelection.selectRange(win, range);
	}
	catch(e) {
	
	}
};
DialogUtils.adjustPriority = function(applicationName, viewName, title, width, height, parameter) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/jeaf/dialog/adjustPriority.shtml";
   url += "?applicationName=" + applicationName; 
   url += "&viewName=" + viewName; 
   url += "&title=" + StringUtils.utf8Encode(title); 
   if(parameter && parameter!="") {
   	  url += "&" + parameter;
   }
   DialogUtils.openDialog(url, width, height);
};
DialogUtils.selectAttachment = function(selectorUrl, recordId, attachmentType, width, height, scriptRunAfterSelect) {
	var url = RequestUtils.getContextPath() + selectorUrl;
	url += (selectorUrl.lastIndexOf('?')==-1 ? '?' : '&') + 'id=' + recordId;
	url += '&attachmentSelector.scriptRunAfterSelect=' + scriptRunAfterSelect;
	url += '&attachmentSelector.type=' + attachmentType;
	DialogUtils.openDialog(url, width, height);
};

DialogUtils.openListDialog = function(dialogTitle, source, width, height, multiSelect, param, scriptEndSelect, key, itemsText, separator) {
	if(itemsText && itemsText!="") {
		source = "listDialogItemsText";
		var hidden = document.getElementsByName(source)[0];
		if(!hidden) {
			try {
				hidden = document.createElement('<input type="hidden" name="' + source + '"/>');
			}
			catch(e) {
				hidden = document.createElement('input');
				hidden.type = 'hidden';
				hidden.name = source;
			}
			document.body.appendChild(hidden);
		}
		hidden.value = itemsText;
	}
	var url = RequestUtils.getContextPath() + "/jeaf/dialog/listDialog.shtml" +
   			  "?title=" + StringUtils.utf8Encode(dialogTitle) +
   			  "&source=" + StringUtils.utf8Encode(source) +
   			  "&multiSelect=" + multiSelect +
   			  "&param=" + StringUtils.utf8Encode(param) +
   			  (scriptEndSelect ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "") +
   			  (key ? "&key=" + StringUtils.utf8Encode(key) : "") +
   			  (separator && separator!="" ? "&separator=" + StringUtils.utf8Encode(separator) : "");
	DialogUtils.openDialog(url, width, height);
};

DialogUtils.openInputDialog = function(dialogTitle, inputs, width, height, script, callback) {
	var url = RequestUtils.getContextPath() + "/jeaf/dialog/inputDialog.shtml" +
   			 "?formTitle=" + StringUtils.utf8Encode(dialogTitle) +
   			 (script && script!="" ? "&script=" + StringUtils.utf8Encode(script) : "");
   	for(var i=0; i<inputs.length; i++) {
   		var field = "";
		for(var attributeName in inputs[i]) {
			field += "&" + attributeName + "=" + inputs[i][attributeName];
		}
		url += "&field" + i + "=" + StringUtils.utf8Encode(field.substring(1));
	}
	DialogUtils.openDialog(url, width, height, dialogTitle, callback);
};

DialogUtils.openMessageDialog = function(dialogTitle, message, buttonNames, type, width, height, script, callback) {
   var url = RequestUtils.getContextPath() + "/jeaf/dialog/messageDialog.shtml" +
   			 "?formTitle=" + StringUtils.utf8Encode(dialogTitle) +
   			 "&message=" + StringUtils.utf8Encode(message) +
   			 "&buttonNames=" + StringUtils.utf8Encode(buttonNames) +
   			 "&type=" + type +
   			 "&script=" + StringUtils.utf8Encode(script);
   DialogUtils.openDialog(url, width, height, dialogTitle, callback);
};

DialogUtils.openColorDialog = function(dialogTitle, colorValue, script, callback) {
   var url = RequestUtils.getContextPath() + "/jeaf/dialog/colorDialog.shtml" +
   			 "?formTitle=" + StringUtils.utf8Encode(dialogTitle) +
   			 "&colorValue=" + StringUtils.utf8Encode(colorValue) +
   			 "&script=" + StringUtils.utf8Encode(script);
   DialogUtils.openDialog(url, 460, 400, dialogTitle, callback);
};

DialogUtils.openSelectDialog = function(applicationName, viewName, width, height, multiSelect, param, scriptEndSelect, defaultCategory, key, separator, paging, dialogParameter) {
   var url = RequestUtils.getContextPath() + "/jeaf/dialog/viewSelectDialog.shtml";
   url += "?applicationName=" + applicationName; 
   url += "&viewName=" + viewName; 
   url += "&multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (defaultCategory && defaultCategory!="" ? "&viewPackage.categories=" + StringUtils.utf8Encode(defaultCategory) : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += (separator && separator!="" ? "&separator=" + StringUtils.utf8Encode(separator) : ""); 
   url += (paging ? "&paging=true" : "");
   url += (dialogParameter ? "&" + dialogParameter : "");
   DialogUtils.openDialog(url, width, height);
};
DialogUtils.selectPerson = function(width, height, multiSelect, param, scriptEndSelect, personTypes, key, separator, assignOrgId, hideRoot) {
   if(!personTypes || personTypes=="" || personTypes=="all") {
      personTypes = "employee,student,teacher";
   }
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/selectPerson.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (personTypes && personTypes!="" ? "&selectNodeTypes=" + personTypes : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (assignOrgId && assignOrgId!="" ? "&parentNodeId=" + assignOrgId : ""); 
   url += "&hideRoot=true"; //\u603B\u662F\u9690\u85CF\u6839\u76EE\u5F55,url += (hideRoot ? "&hideRoot=true" : "");
   DialogUtils.openDialog(url, width, height);
};
DialogUtils.selectOrg = function(width, height, multiSelect, param, scriptEndSelect, orgTypes, key, separator, assignOrgId, hideRoot, leafNodeOnly, managedOnly) {
   if(!orgTypes || orgTypes=="") {
      orgTypes = "all";
   }
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/selectOrg.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (orgTypes && orgTypes!="" ? "&selectNodeTypes=" + orgTypes : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (assignOrgId && assignOrgId!="" ? "&parentNodeId=" + assignOrgId : ""); 
   url += "&hideRoot=true"; //\u603B\u662F\u9690\u85CF\u6839\u76EE\u5F55,url += (hideRoot ? "&hideRoot=true" : "");
   url += (leafNodeOnly ? "&leafNodeOnly=true" : "");
   url += (managedOnly ? "&managedOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
};
DialogUtils.selectRole = function(width, height, multiSelect, param, scriptEndSelect, key, separator, assignOrgId, hideRoot, postOnly) {
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/selectRole.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += "&selectNodeTypes=role"; 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (assignOrgId && assignOrgId!="" ? "&parentNodeId=" + assignOrgId : ""); 
   url += "&hideRoot=true"; //\u603B\u662F\u9690\u85CF\u6839\u76EE\u5F55,url += (hideRoot ? "&hideRoot=true" : "");
   url += (postOnly ? "&postOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
};
DialogUtils.selectPersonByRole = function(width, height, multiSelect, param, scriptEndSelect, key, separator, assignOrgId, hideRoot, postOnly) {
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/selectRoleMember.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += "&selectNodeTypes=employee,student,teacher"; 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (assignOrgId && assignOrgId!="" ? "&parentNodeId=" + assignOrgId : ""); 
   url += "&hideRoot=true"; //\u603B\u662F\u9690\u85CF\u6839\u76EE\u5F55,url += (hideRoot ? "&hideRoot=true" : "");
   url += (postOnly ? "&postOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
};
DialogUtils.selectUser = function(src, width, height, multiSelect, param, orgTypes, personTypes, userTypes, key, separator, assignOrgId, hideRoot, postOnly) {
	if(!userTypes || userTypes=="") {
		userTypes = "\u90E8\u95E8\0\u89D2\u8272\0\u4E2A\u4EBA(\u6309\u90E8\u95E8)\0\u4E2A\u4EBA(\u6309\u89D2\u8272)";
	}
	PopupMenu.popupMenu(userTypes, function(menuItemId, menuItemTitle) {
		switch(menuItemId) {
		case "\u90E8\u95E8":
			DialogUtils.selectOrg(width, height, multiSelect, param, '', orgTypes, key, separator, assignOrgId, hideRoot, false, false);
			break;
			
		case "\u89D2\u8272":
			DialogUtils.selectRole(width, height, multiSelect, param, '', key, separator, assignOrgId, hideRoot, postOnly);
			break;
		
		case "\u4E2A\u4EBA":
		case "\u4E2A\u4EBA(\u6309\u90E8\u95E8)":
			DialogUtils.selectPerson(width, height, multiSelect, param, '', personTypes, key, separator, assignOrgId, hideRoot);
			break;
			
		case "\u4E2A\u4EBA(\u6309\u89D2\u8272)":
			DialogUtils.selectPersonByRole(width, height, multiSelect, param, '', key, separator, assignOrgId, hideRoot, postOnly);
			break;
		}
	}, src, 120, "right");
};
DialogUtils.selectVisitors = function(visitorListFieldName, describe, src, assignOrgId, userTypes) { 
	if(visitorListFieldName=='' && src.id && src.id.indexOf("field_")!=-1) { 
		var input = document.getElementById('input_' + src.id.replace('picker_', ''));
		visitorListFieldName =  input.name.substring(0, input.name.indexOf(".visitorNames"));
		describe = input.title;
		if(!describe) {
			describe = "\u7528\u6237";
		}
	}
	DialogUtils.selectUser(src, 640, 400, true, visitorListFieldName + ".visitorIds{id}," + visitorListFieldName + ".visitorNames{name|" + describe + "|100%}", "root,unit,unitDepartment", "", userTypes, "", ",", assignOrgId, false, false);
};
DialogUtils.userPageTemplateConfigure = function(userId) { 
   var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectPage.shtml";
   url += "?selectNodeTypes=page";
   url += "&currentApplicationName=jeaf/usermanage"; 
   url += "&script=" + StringUtils.utf8Encode("DialogUtils._openUserPageTemplateView('" + userId + "', '{id}'.split('__')[0], '{id}'.split('__')[1])");
   url += "&internalPageOnly=true";
   url += "&userId=" + userId;
   DialogUtils.openDialog(url, 640, 400);
};
DialogUtils._openUserPageTemplateView = function(userId, applicationName, pageName) {
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/admin/selectTemplate.shtml";
   url += "?applicationName=" + applicationName;
   url += "&pageName=" + pageName; 
   url += "&userId=" + userId;
   DialogUtils.openDialog(url, 640, 400);
};
DialogUtils.getDialogOpener = function() { 
	return DialogUtils.getDialogFrame().opener;
};
DialogUtils.getDialogArguments = function() { 
	return DialogUtils.getDialogFrame().dialogArguments;
};
DialogUtils.adjustDialogSize = function() { 
	window.parent.setTimeout("adjustDialogSize()", 1);
};
DialogUtils.moveDialog = function(offsetX, offsetY) { 
	var dialogFrame = DialogUtils.getDialogFrame();
	dialogFrame.style.left = (Number(dialogFrame.style.left.replace('px', '')) + offsetX) + "px";
	dialogFrame.style.top = (Number(dialogFrame.style.top.replace('px', '')) + offsetY) + "px";
};
DialogUtils.getDialogFrame = function() { 
	var dialog = window.frameElement;
	while(dialog.id!='dialog') {
		dialog = (dialog.ownerDocument.parentWindow ? dialog.ownerDocument.parentWindow : dialog.ownerDocument.defaultView).frameElement;
	}
	return dialog;
};

DocumentUtils = function() {
};
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
	window.onRemoteDocumentReady = function(taskId, downloadDocumentURL, uploadPassportURL, uploadServerURL, documentApplicationName) { 
		DocumentUtils._onRemoteDocumentReady(taskId, downloadDocumentURL, uploadPassportURL, uploadServerURL, documentApplicationName);
	};
	window.onRemoteDocumentProcessError = function(error) { 
		DocumentUtils._onRemoteDocumentProcessError(error);
	};
	window.onRemoteDocumentDownloadStart = function() {
		DocumentUtils._removeExtensionNotRunWarnTimer();
		DocumentUtils._checkStatus(300); 
	};
	window.onRemoteDocumentDownloading = function(totalSize, complete) { 
		DocumentUtils._removeExtensionNotRunWarnTimer();
		var percent = Math.floor(complete / totalSize * 1000) / 10;console.log('complete:'+complete+",totalSize:"+totalSize+",percent:"+percent);
		PageUtils.showToast('\u6B63\u5728\u4E0B\u8F7D, \u5DF2\u5B8C\u6210' + percent + '%...');
	};
	window.onRemoteDocumentOpening = function() { 
		DocumentUtils._removeExtensionNotRunWarnTimer();
		PageUtils.showToast('\u6B63\u5728\u6253\u5F00\u6587\u6863, \u8BF7\u7A0D\u5019...');
	};
	window.onRemoteDocumentOpened = function() { 
		DocumentUtils._removeExtensionNotRunWarnTimer();
		DocumentUtils._onRemoteDocumentOpened();
	};
	window.onRemoteDocumentClosed = function() { 
		DocumentUtils._onRemoteDocumentClosed();
	};
	window.onfocus = function() { 
		DocumentUtils._doCheckStatus();
	};
	window.onRemoteDocumentUploaded = function() { 
		DocumentUtils._onRemoteDocumentUploadCompleted();
	};
	var parameter = (actionParameter ? actionParameter + "&" : "") + "documentCommand=" + documentCommand + (documentCommandParameter ? "&documentCommandParameter=" + StringUtils.utf8Encode(documentCommandParameter) : "") ;
	if(FormUtils.doAction(actionName, parameter, true, formName, 'iframeRemoteDocument')) {
		PageUtils.showToast('\u6B63\u5728\u4E0B\u8F7D, \u8BF7\u7A0D\u5019...');
	}
};
DocumentUtils._onRemoteDocumentReady = function(taskId, downloadDocumentURL, uploadPassportURL, uploadServerURL, documentApplicationName) {
	window.lastRemoteDocument.taskId = taskId;
	
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
		alert('\u4E0B\u8F7D\u6587\u6863\u65F6\u51FA\u9519,\u8BF7\u68C0\u67E5\u63D2\u4EF6\u662F\u5426\u6B63\u786E\u5B89\u88C5\u548C\u8FD0\u884C');
	}, 5000);
};
DocumentUtils._onRemoteDocumentProcessError = function(error) {
	PageUtils.showToast('');
	DocumentUtils._stopCheckStatus();
	if(error=="NOSESSIONINFO") {
		LoginUtils.openLoginDialog(function() {
			DocumentUtils.openRemoteDocument(window.lastRemoteDocument.documentCommand, window.lastRemoteDocument.documentCommandParameter, window.lastRemoteDocument.onUploaded, window.lastRemoteDocument.actionName, window.lastRemoteDocument.actionParameter, window.lastRemoteDocument.formName);
		});
	}
	else if(error=="NOPRIVILEGE") {
		alert("\u6CA1\u6709\u6743\u9650");
	}
	else if(error=="ERROR") {
		alert("\u7CFB\u7EDF\u9519\u8BEF,\u8BF7\u8054\u7CFB\u7BA1\u7406\u5458");
	}
	else {
		alert(error);
	}
};
DocumentUtils._onRemoteDocumentOpened = function() {
	PageUtils.showToast('');
	DocumentUtils._checkStatus(3000); 
};
DocumentUtils._onRemoteDocumentClosed = function() {
	PageUtils.showToast('');
	DocumentUtils._stopCheckStatus();
	window.lastRemoteDocument = null;
};
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
DocumentUtils._checkStatus = function(interval) {
	DocumentUtils._stopCheckStatus();
	if(!window.lastRemoteDocument) {
		return;
	}
	window.documentTimer = window.setInterval(function() {
		DocumentUtils._doCheckStatus();
	}, interval);
};
DocumentUtils._doCheckStatus = function() {
	if(window.lastRemoteDocument) {
		ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/document?action=status&taskId=' + window.lastRemoteDocument.taskId + '&seq=' + Math.random(), "scriptBorwserExtension");	
	}
};
DocumentUtils._stopCheckStatus = function() {
	if(window.documentTimer) {
		window.clearInterval(window.documentTimer);
		window.documentTimer = null;
	}
};
DocumentUtils._removeExtensionNotRunWarnTimer = function() {
	if(window.extensionNotRunWarnTimer) {
		window.clearTimeout(window.extensionNotRunWarnTimer);
		window.extensionNotRunWarnTimer = null;
	}
};

DomSelection = function() {
};
DomSelection.getSelection = function(window) { 
	if(window.getSelection) {
		return window.getSelection();
	}
	else {
		return window.document.selection;
	}
};
DomSelection.getRange = function(window) { 
	var selection = DomSelection.getSelection(window);
	if(!selection) {
		return null;
	}
	var range;
	if(selection.createRange) {
		range = selection.createRange();
	}
	else if(selection.rangeCount>0) {
		range = selection.getRangeAt(0);
	}
	var element = DomSelection.getSelectedElement(range);
	return element && element.ownerDocument!=window.document ? null : range;
};
DomSelection.getSelectedElement = function(range) { 
	if(!range) {
		return null;
	}
	if(range.getBookmark) { 
		return range.parentElement();
	}
	else if(range.select) { 
		return range.item(0);
	}
	else if(range.startContainer) { 
		if(range.startContainer.nodeType==3) { 
			return range.startContainer.parentNode;
		}
		var element = range.startContainer.childNodes[range.startOffset];
		if(element && element.nodeType==1) { 
			return element;
		}
		return range.startContainer;
	}
};
DomSelection.getSelectText = function(window) { 
	var range = DomSelection.getRange(window);
	if(!range) {
		return "";
	}
	if(range.getBookmark) { 
		return range.text;
	}
	else if(range.toString) {
		return range.toString(); 
	}
};
DomSelection.getSelectHtmlText = function(window) { 
	var range = DomSelection.getRange(window);
	if(!range) {
		return "";
	}
	if(range.getBookmark) { 
		return range.htmlText;
	}
	var htmlText = "";
	if(range.startContainer.nodeType==3) {
		htmlText = range.startContainer.nodeValue.substring(range.startOffset, range.startContainer==range.endContainer ? range.endOffset : range.startContainer.nodeValue.length);
	}
	else {
		htmlText = range.startContainer.outerHTML;
	}
	if(range.startContainer==range.endContainer) {
		return htmlText;
	}
	
	var startSibiling = range.startContainer;
	while(true) {
		if(DomUtils.containsNode(startSibiling.parentNode, range.endContainer)) {
			break;
		}
		startSibiling = startSibiling.parentNode;
	}
	startSibiling = startSibiling.nextSibling;
	while(!DomUtils.containsNode(startSibiling, range.endContainer)) {
		htmlText += startSibiling.nodeType==3 ? startSibiling.textContent : startSibiling.outerHTML;
		startSibiling = startSibiling.nextSibling;
	}
	if(range.endContainer.nodeType==3) {
		htmlText += range.endContainer.nodeValue.substring(0, range.endOffset);
	}
	else {
		htmlText += range.endContainer.outerHTML;
	}
	return htmlText;
};
DomSelection.selectRange = function(window, range) { 
	if(!range) {
		return;
	}
	if(range.select) {
		try { range.select();} catch(e) {}  
	}
	else if(range.startContainer) { 
		var selection = DomSelection.getSelection(window);
		window.focus();
		selection.removeAllRanges();
		selection.addRange(range);
	}
};
DomSelection.createRange = function(document, element) { 
	var range = document.createRange ? document.createRange() : document.body.createTextRange();
	if(element==document.body) {
		return range;
	}
	if(range.moveToElementText) {
		range.moveToElementText(element);
	}
	else if(element.childNodes.length==0) {
		range.setStart(element, 0);
		range.setEnd(element, 0);
	}
	else {
		range.setStart(element.childNodes[0], 0);
		var endNode = element.childNodes[element.childNodes.length - 1];
		range.setEnd(endNode, endNode.length ? endNode.length : 0);
	}
	return range;
};
DomSelection.getRangeBookmark = function(range) { 
	if(range.getBookmark) { 
		return {bookmark: range.getBookmark()};
	}
	else if(range.select) { 
		return {control: DomSelection._getRangeAddress(range.item(0), 0)};
	}
	else if(range.startContainer) { 
		return {start: DomSelection._getRangeAddress(range.startContainer, range.startOffset), end: DomSelection._getRangeAddress(range.endContainer, range.endOffset)};
	}
};
DomSelection.createRangeByBookmark = function(document, rangeBookmark) { 
	try {
		var range;
		if(rangeBookmark.bookmark) { 
			range = document.body.createTextRange();
			range.moveToBookmark(rangeBookmark.bookmark);
		}
		else if(rangeBookmark.control) { 
			range = document.body.createControlRange();
			range.add(DomSelection._getNodeByAddress(document, rangeBookmark.control.address));
		}
		else if(rangeBookmark.start) { 
			range = document.createRange();
			range.setStart(DomSelection._getNodeByAddress(document, rangeBookmark.start.address), rangeBookmark.start.offset);
			range.setEnd(DomSelection._getNodeByAddress(document, rangeBookmark.end.address), rangeBookmark.end.offset);
		}
		return range;
	}
	catch(e) {
		return null;
	}
};
DomSelection._getRangeAddress = function(node, offset) { 
	
	 while(node.nodeType==3) {
	 	if(!node.previousSibling || node.previousSibling.nodeType!=3) {
	 		break;
	 	}
 		node = node.previousSibling;
 		offset += node.length;
	 }
	 
	 var address = [];
	 while(node.tagName!="BODY") {
	 	var childNodes = node.parentNode.childNodes;
	 	for(var i=0; i<childNodes.length; i++) {
	 		if(childNodes[i]==node) {
	 			address.push(i);
	 			break;
	 		}
	 	}
	 	node = node.parentNode;
	 }
	 return {address: address, offset: offset};
};
DomSelection._getNodeByAddress = function(document, address) { 
	var node = document.body;
	for(var i=address.length-1; i>=0; i--) {
		node = node.childNodes[address[i]];
	}
	return node;
};
DomSelection.pasteHTML = function(window, range, html) { 
	DomSelection.selectRange(window, range);
	if(range && range.pasteHTML) {
		range.pasteHTML(html);
	}
	else if("" + window.document.queryCommandSupported('insertHTML')=="true") { 
		window.document.execCommand('insertHTML', false, html);
	}
	else if(range.surroundContents) {
		var element = window.document.createElement("b");
        range.surroundContents(element);
        element.outerHTML = html;
	}
};
DomSelection.inRange = function(range, element) { 
	if(DomSelection.getSelectedElement(range)==element) {
		return true;
	}
	else if(range.selectNode) {
		var elementRange = element.ownerDocument.createRange();
		elementRange.selectNode(element);
		var elementRect = elementRange.getBoundingClientRect();
		var rangeRect = range.getBoundingClientRect();
		return elementRect.right >= rangeRect.left &&
		   	   elementRect.left <= rangeRect.left + rangeRect.width &&
		   	   elementRect.bottom >= rangeRect.top &&
		   	   elementRect.top <= rangeRect.top + rangeRect.height;
	}
	else {
		var rect = element.getBoundingClientRect();
		return rect.right >= range.boundingLeft &&
		   	   rect.left <= range.boundingLeft + range.boundingWidth &&
		   	   rect.bottom >= range.boundingTop &&
		   	   rect.top <= range.boundingTop + range.boundingHeight;
	}
};

DomUtils = function() {
};
DomUtils.createActiveXObject = function(classid, id) {
	var obj = document.getElementById(id);
	if(obj) {
		return obj;
	}
	obj = document.createElement("object");
	obj.classid = classid;
	obj.id = id;
	document.body.appendChild(obj);
	return obj;
};
DomUtils.getElement = function(parentElement, tagName, id) {
	if(tagName && tagName!="") {
		var elements = parentElement.getElementsByTagName(tagName);
		for(var i = 0; elements && i < elements.length; i++) {
			if(elements[i].id == id || elements[i].name == id) {
				return elements[i];
			}
		}
		return null;
	}
	
	var found = null;
	DomUtils.traversalChildElements(parentElement, function(element) {
		if(element.id==id || element.name==id) {
			found = element;
			return true;
		}
	});
	return found;
};
DomUtils.getElements = function(parentElement, tagName, id) {
	var found = [];
	if(tagName && tagName!="") {
		var elements = parentElement.getElementsByTagName(tagName);
		for(var i = 0; elements && i < elements.length; i++) {
			if(elements[i].id == id || elements[i].name == id) {
				found.push(elements[i]);
			}
		}
		return found;
	}
	
	DomUtils.traversalChildElements(parentElement, function(element) {
		if(element.id==id || element.name==id) {
			founds.push(element);
			return true;
		}
	});
	return found;
};
DomUtils.nextElement = function(element) {
	var next = element.nextSibling;
	while(next) {
		if(next.tagName) {
			return next;
		}
		next = next.nextSibling;
	}
	return null;
};
DomUtils.getOffsetParent = function(element) {
	
	
	
	var parent = element.parentNode;
	for(; ",BODY,TD,TABLE,DIV,SPAN,A,UL,LI,PRE,".indexOf("," + parent.tagName + ",")==-1; parent = parent.parentNode);
	return parent;
};
DomUtils.insertAfter = function(newNode, refNode) { 
	var nextSibling = refNode.nextSibling;
	if(nextSibling) {
		refNode.parentNode.insertBefore(newNode, nextSibling);
	}
	else {
		refNode.parentNode.appendChild(newNode);
	}
};
DomUtils.containsNode = function(parentNode, childNode) { 
	if(parentNode==childNode) {
		return true;
	}
	var childNodes = parentNode.childNodes;
	for(var i=0; i<childNodes.length; i++) {
		if(childNodes[i]==childNode) {
			return true;
		}
		if(DomUtils.containsNode(childNodes[i], childNode)) { 
			return true;
		}
	}
	return false;
};
DomUtils.getParentNode = function(node, parentNodeNames, containerNode) { 
	if(!node || node==containerNode || (containerNode && !containerNode.contains(node))) {
		return null;
	}
	parentNodeNames = ',' + parentNodeNames.toLowerCase() + ',';
	while(node && node!=containerNode) {
		if(node.nodeName && parentNodeNames.indexOf(',' + node.nodeName.toLowerCase() + ',')!=-1) {
			return node;
		}
		if(node.nodeName && node.nodeName.toLowerCase()=="body") {
			break;
		}
		node = node.parentNode;
	}
	return null;
};
DomUtils.traversalChildElements = function(parentElement, callback) { 
	var childNodes = parentElement.childNodes;
	if(!childNodes) {
		return false;
	}
	for(var i=0; i<childNodes.length; i++) {
		if(!childNodes[i].tagName) {
			continue;
		}
		if(callback.call(null, childNodes[i])) {
			return true;
		}
		if(DomUtils.traversalChildElements(childNodes[i], callback)) { 
			return true;
		}
	}
	return false;
};
DomUtils.getAbsolutePosition = function(obj, parentObj, haveIframe) { 
	var currentObj = obj;
	var pos = {left:0, top:0};
	var offsetParent = obj;
	var lastObj = obj;
	while(obj && obj!=parentObj) {
		if(obj==offsetParent) {
			pos.left += obj.offsetLeft;
			pos.top += obj.offsetTop;
			offsetParent = obj.offsetParent;
			if(offsetParent) {
				lastObj = offsetParent;
			}
		}
		if(obj.tagName && obj.tagName!="TR" && obj.tagName!="FORM") {
			pos.left -= (obj!=currentObj && obj.scrollLeft ? obj.scrollLeft : 0);
			pos.top -= (obj!=currentObj && obj.scrollTop ? obj.scrollTop : 0);
		}
		obj = obj.parentNode;
		if(obj && obj!=parentObj && obj.tagName=="BODY") {
			var doctype;
			if(document.all && (doctype=document.documentElement.previousSibling) && doctype.nodeType==8 && doctype.data.indexOf('W3C')!=-1) { 
				pos.left += DomUtils.getSpacing(obj, 'left');
				pos.top += DomUtils.getSpacing(obj, 'top');
			}
			if(!haveIframe) {
				break;
			}
			var win = (obj.ownerDocument.parentWindow ? obj.ownerDocument.parentWindow : obj.ownerDocument.defaultView);
			obj = win.frameElement;
			if(obj) {
				lastObj = offsetParent = obj;
			}
		}
	}
	if(parentObj) {
		lastObj = parentObj;
	}
	pos.right = lastObj.offsetWidth - pos.left - currentObj.offsetWidth;
	pos.bottom = lastObj.offsetHeight - pos.top - currentObj.offsetHeight;
	return pos;
};
DomUtils.getMaxZIndex = function(parentElement) { 
	var maxZIndex = 0;
	DomUtils.traversalChildElements(parentElement, function(element) {
		var zIndex = element.style.zIndex;
		if(!zIndex || zIndex=="") {
			return;
		}
		zIndex = parseInt(zIndex);
		if(zIndex < 1000000 && zIndex>maxZIndex) {
			maxZIndex = zIndex;
		}
	});
	return maxZIndex;
};
DomUtils.getMetaContent = function(doc, name) {
	var metas = doc.getElementsByTagName("meta");
	if(!metas) {
		return null;
	}
	for(var i=metas.length-1; i>=0; i--) {
		if(metas[i].name==name) {
			return metas[i].content;
		}
	}
	return null;
};
DomUtils.getMeta = function(doc, name, autoCreate) {
	var metas = doc.getElementsByTagName('meta');
	for(var i=0; i < metas.length; i++) {
		if(metas[i].name==name) {
			return metas[i];
			break;
		}
	}
	if(!autoCreate) {
		return null;
	}
	var meta;
	try {
		meta = doc.createElement('<meta name="' + name + '"/>');
	}
	catch(e) {
		meta = doc.createElement('meta');
		meta.setAttribute('name', name);
	}
	doc.getElementsByTagName('head')[0].appendChild(meta);
	return meta;
};
DomUtils.setElementHTML = function(element, html, inner) { 
	
	html = html.replace(/<a(?=\s).*?\shref=((?:(?:\s*)("|').*?\2)|(?:[^"'][^ >]+))/gi,'$& _savedurl=$1');
	html = html.replace(/<img(?=\s).*?\ssrc=((?:(?:\s*)("|').*?\2)|(?:[^"'][^ >]+))/gi,'$& _savedurl=$1');
	html = html.replace(/<area(?=\s).*?\shref=((?:(?:\s*)("|').*?\2)|(?:[^"'][^ >]+))/gi,'$& _savedurl=$1');
	var parentElement;
	
	if(inner) {
		parentElement = element;
		element.innerHTML = html;
	}
	else  {
		parentElement = element.parentElement;
		element.outerHTML = html;
	}
	
	var tags = [["img", "src"], ["a", "href"], ["area", "href"]];
	for(var i=0; i<tags.length; i++) {
		var elements = parentElement.getElementsByTagName(tags[i][0]);
		if(!elements) {
			return;
		}
		for(var j=0; j<elements.length; j++) {
			var value = elements[j].getAttribute("_savedurl");
			if(value) {
				elements[j].setAttribute(tags[i][1], value);
				elements[j].removeAttribute("_savedurl");
			}
		}
	}
};
try {
	if(typeof(HTMLElement)!="undefined" && !window.opera) {
	    HTMLElement.prototype.__defineGetter__("outerHTML",function() { 
	        var a=this.attributes, str="<"+this.tagName, i=0;for(;i<a.length;i++) 
	        if(a[i].specified) 
	            str+=" "+a[i].name+'="'+a[i].value+'"'; 
	        if(!this.canHaveChildren) 
	            return str+" />"; 
	        return str+">"+this.innerHTML+"</"+this.tagName+">"; 
	    });
	    HTMLElement.prototype.__defineSetter__("outerHTML",function(s) { 
	        var r = this.ownerDocument.createRange(); 
	        r.setStartBefore(this); 
	        var df = r.createContextualFragment(s); 
	        this.parentNode.replaceChild(df, this); 
	        return s; 
	    }); 
	    HTMLElement.prototype.__defineGetter__("canHaveChildren",function() { 
	        return !/^(area|base|basefont|col|frame|hr|img|br|input|isindex|link|meta|param)$/.test(this.tagName.toLowerCase()); 
	    }); 
	}
}
catch(e) {
}
DomUtils.getHead = function(doc) {
	
	var head = doc.getElementsByTagName("head")[0];
	if(!head) {
		head = doc.createElement("head");
		if(doc.getFirstChild()) {
			doc.insertBefore(head, doc.getFirstChild());
		}
		else {
			doc.appendChild(head);
		}
	}
	return head;
};
DomUtils.getClientWidth = function(doc) { 
	if(doc.body.clientWidth > 0 && doc.body.clientWidth < doc.body.scrollWidth) {
		return doc.body.clientWidth;
	}
	var clientWidth = doc.documentElement.clientWidth;
	return clientWidth==0 ? doc.body.clientWidth : clientWidth;
};
DomUtils.getClientHeight = function(doc) { 
	if(doc.body.clientHeight > 0 && doc.body.clientHeight < doc.body.scrollHeight) {
		return doc.body.clientHeight;
	}
	var clientHeight = doc.documentElement.clientHeight;
	return clientHeight==0 ? doc.body.clientHeight : clientHeight;
};
DomUtils.getScrollTop = function(doc) { 
	var scrollTop = doc.documentElement.scrollTop;
	if(scrollTop==0) {
		scrollTop = doc.body.scrollTop;
	}
	return scrollTop;
};
DomUtils.getScrollLeft = function(doc) { 
	var scrollLeft = doc.documentElement.scrollLeft;
	if(scrollLeft==0) {
		scrollLeft = doc.body.scrollLeft;
	}
	return scrollLeft;
};
DomUtils.getSpacing = function(obj, place) { //\u83B7\u53D6\u5BF9\u8C61\u95F4\u9699,place=['left', 'top', 'right', 'bottom']
	var margin = CssUtils.getElementComputedStyle(obj, 'margin-' + place);
	var padding = CssUtils.getElementComputedStyle(obj, 'padding-' + place);
	margin = !margin ? 0 : Number(margin.replace("px", ""));
	padding = !padding ? 0 : Number(padding.replace("px", ""));
	return (isNaN(margin) ? 0 : margin) + (isNaN(padding) ? 0 : padding);
};
DomUtils.getBorderWidth = function(obj, place) { //\u83B7\u53D6\u8FB9\u6846\u5BBD\u5EA6,place=['left', 'top', 'right', 'bottom']
	var width = CssUtils.getElementComputedStyle(obj, 'border-' + place + "-width");
	width = !width ? 0 : Number(width.replace("px", ""));
	return (isNaN(width) ? 0 : width)
};
DomUtils.createLink = function(window, range) {
	var random = ("" + Math.random()).substring(2);
	var href = 'javascript:alert(' + random + ')';
	if(range && range.execCommand) {
		range.execCommand("createLink", false, href);
	}
	else {
		DomSelection.selectRange(window, range);
		if(!window.document.execCommand("createLink", false, href)) {
			var a = window.document.createElement("a");
	        range.surroundContents(a);
	        a.href = href;
		}
	}
	var i=0;
	var links = window.document.getElementsByTagName("a");
	for(; i < links.length && links[i].href.indexOf(random)==-1; i++);
	var images = links[i].getElementsByTagName("img");
	for(var j=0; j<(images ? images.length : 0); j++) {
		images[j].border =  0;
	}
	links[i].removeAttribute("href");
	if(links[i].innerHTML && links[i].innerHTML.indexOf(random)!=-1) {
		links[i].innerHTML = '';
	}
	return links[i];
};
DomUtils.createElement = function(window, range, elementType) { 
	var id = ("" + Math.random()).substring(2);
	DomSelection.pasteHTML(window, range, '<' + elementType + ' id="' + id + '"></' + elementType + '>');
	var element = window.document.getElementById(id);
	if(element) {
		element.removeAttribute("id");
		return element;
	}
};
DomUtils.getWindowBookmark = function(window, noFocusPrompt) { 
	var selection = DomSelection.getSelection(window);
	var range = DomSelection.getRange(window);
	var selectedElement = DomSelection.getSelectedElement(range);
	if(!selectedElement) {
		alert(noFocusPrompt);
		return null;
	}
	return {window:window, document:window.document, range:range, selection:selection, selectedElement:selectedElement};
};
DomUtils.setAttribute = function(element, propertyName, propertyValue) {
	var document = element.ownerDocument;
	if(!document.all) { 
		element.setAttribute(propertyName, propertyValue);
		return element;
	}
	var id = element.id;
	var newId = Math.random();
	element.id = newId;
	element.removeAttribute(propertyName);
	element.setAttribute("temp_property", propertyValue);
	element.outerHTML = element.outerHTML.replace("temp_property", propertyName);
	element = document.getElementById(newId);
	element.id = id;
	return element
};
DomUtils.moveTableRow = function(table, fromRowIndex, toRowIndex) { 
	if(fromRowIndex==toRowIndex || toRowIndex>=table.rows.length) {
		return;
	}
	if(table.moveRow) {
		table.moveRow(fromRowIndex, toRowIndex);
		return;
	}
	var parentNode = table.rows[fromRowIndex].parentNode;
	if(toRowIndex<fromRowIndex) {
		parentNode.insertBefore(table.rows[fromRowIndex], table.rows[toRowIndex]);
	}
	else if(toRowIndex>=table.rows.length-1) {
		parentNode.appendChild(table.rows[fromRowIndex]);
	}
	else {
		parentNode.insertBefore(table.rows[fromRowIndex], table.rows[toRowIndex + 1]);
	}
};

FieldValidator = function() {
};
FieldValidator.validateFieldRequired = function(src, required, fieldName) {
   if(!src) {
	  return "";
   }
   if(src.value=="" && required) {
      alert((fieldName ? fieldName : "\u5185\u5BB9") + "\u4E0D\u80FD\u4E3A\u7A7A\uFF01");
      src.focus();
      return "NaN";
   }
   return src.value;
};
FieldValidator.validateStringField = function(src, mask, required, fieldName) {
   var value = FieldValidator.validateFieldRequired(src, required, fieldName);
   if(value == "" || value == "NaN") {
      return value;
   }
   if(mask && mask != "") {
      var newMask = mask.replace(new RegExp("\\x27", "g"), "\\x27").replace(new RegExp(",", "g"), "");
      if(value.search(new RegExp("[" + newMask + "]")) != - 1) {
         alert((fieldName ? fieldName : "\u8F93\u5165\u5185\u5BB9") + "\u4E0D\u80FD\u5305\u542B" + mask + "\u7B49\u5B57\u7B26\uFF01");
         src.focus();
         src.select();
         return "NaN";
      }
   }
   return value;
};
FieldValidator.validateNumberField = function(src, required, fieldName) {
   var value = FieldValidator.validateFieldRequired(src, required, fieldName);
   if(value == "" || value == "NaN") {
      return value;
   }
   var value = new Number(value);
   if(isNaN(value)) {
      alert((fieldName ? fieldName : "\u60A8") + "\u8F93\u5165\u7684\u6570\u5B57\u4E0D\u6B63\u786E\uFF01");
      src.focus();
      src.select();
      return "NaN";
   }
   return value;
};
FieldValidator.validateDateField = function(src, required, fieldName) {
   var value = FieldValidator.validateFieldRequired(src, required, fieldName);
   if(value == "" || value == "NaN") {
      return value;
   }
   var dateValue = new Date(value.replace(new RegExp("-", "g"), "/"));
   if(isNaN(dateValue)) {
      alert((fieldName ? fieldName : "\u60A8") + "\u8F93\u5165\u7684\u65E5\u671F\u683C\u5F0F\u4E0D\u6B63\u786E\uFF01");
      src.focus();
      src.select();
      return "NaN";
   }
   return value;
};

DateField = function(inputElementHTML, styleClass, style, selectButtonStyleClass, selectButtonStyle, alignLeft, parentElement, terminalType) {
	FormField.call(this, inputElementHTML, 'text', style, styleClass);
	this.terminalType = terminalType;
	this.alignLeft = alignLeft;
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'dropDownButton';
	}
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">' +
			   '	<tr>' +
			   '		<td style="padding:0px !important;">' + this.resetInputElementHTML() + '</td>' +
			   '		<td nowrap="nowrap" id="picker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important; ' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	
	var datePickerButton = this.getInputElement().readOnly ?  this.getFieldElement() : this.document.getElementById("picker_" + this.id);
	var dateField = this;
	datePickerButton.onclick = function() {
		dateField.onSelectDate();
	}
};
DateField.prototype = new FormField();
DateField.prototype.onSelectDate = function() { 
	new FormField.DatePicker(this.getInputElement(), this.getFieldElement(), this.alignLeft, this.terminalType).show();
};

DateTimeField = function(inputElementHTML, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, clickShowPicker, terminalType) {
	FormField.call(this, inputElementHTML, 'hidden', style, styleClass);
	this.terminalType = terminalType;
	this.timeFocus = "Hour"; 
	var dateValue = '';
	var hourValue = '';
	var minuteValue = '';
	var secondValue = '';
	var fieldValue = this.getAttribute("value");
	if(fieldValue!='') {
		var values = fieldValue.split(' ');
		dateValue = values[0];
		if(values[1]) {
			values = values[1].split(":");
			hourValue = Number(values[0]);
			minuteValue = Number(values[1]);
			secondValue = Number(values[2]);
		}
	}
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'dropDownButton';
	}
	var alt = this.getAttribute('alt');
	var title = this.getAttribute('title');
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%"' + (title ? ' title="' + title + '"' : '') + '>' +
			   '	<tr>' +
			   '		<td style="padding:0px !important;"><input id="' + this.id + 'Date" dataType="date"' + (!alt || alt=='' ? '' : ' alt="' + alt + '" ' + 'onfocus="if(value==alt)value=\'\';" onblur="if(value==\'\')value=alt;"') + ' maxlength="10"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '" type="text" value="' + dateValue + '"' + (title ? ' title="' + title + '"' : '') + '/></td>' +
			   '		<td nowrap="nowrap" id="datePicker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important;' + selectButtonStyle + '"' : '') + '></td>' +
			   '		<td style="padding:0px !important; width:22px"><input id="' + this.id + 'Hour" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + hourValue + '"/></td>' +
			   '		<td style="padding:0px !important; width:8px" align="center">:</td>' +
			   '		<td style="padding:0px !important; width:22px"><input id="' + this.id + 'Minute" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + minuteValue + '"/></td>' +
			   '		<td style="padding:0px !important; width:8px" align="center">:</td>' +
			   '		<td style="padding:0px !important; width:22px"><input id="' + this.id + 'Second" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + secondValue + '"/></td>' +
			   '		<td nowrap="nowrap" id="timePicker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important; ' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	var dateTimeField = this;
	
	var datePickerButton = !clickShowPicker ? this.document.getElementById("datePicker_" + this.id) : this.document.getElementById('dateTable' + this.id);
	datePickerButton.onclick = function() {
		dateTimeField.onSelectDate(datePickerButton);
	}
	
	var timePickerButton = this.document.getElementById('timePicker_' + this.id);
	timePickerButton.onclick = function() {
		dateTimeField.onSelectTime(timePickerButton);
	};
	
	var fieldNames = ["Date", "Hour", "Minute", "Second"];
	for(var i=0; i<fieldNames.length; i++) {
		this.document.getElementById(this.id + fieldNames[i]).onchange = function() {
			dateTimeField._update();
		};
		if(i==0) {
			continue;
		}
		this.document.getElementById(this.id + fieldNames[i]).onfocus = function() {
			dateTimeField.timeFocus = this.id.substring(dateTimeField.id.length);
		};
	}
	
	var fontSize = CssUtils.getElementComputedStyle(this.document.getElementById(this.id + "Date"), 'font-size');
	fontSize = fontSize ? Number(fontSize.replace('px', '')) : 12;
	for(var i=1; fontSize > 12 && i < fieldNames.length; i++) {
		this.document.getElementById(this.id + fieldNames[i]).style.width = Math.floor(22 * fontSize / 12) + 'px';
	}
};
DateTimeField.prototype = new FormField();
DateTimeField.prototype.onSelectDate = function(datePickerButton) { 
	new FormField.DatePicker(this.document.getElementById(this.id + "Date"), datePickerButton, false, this.terminalType).show();
};
DateTimeField.prototype.onSelectTime = function(timePickerButton) { 
	var numberFields = [{field:this.document.getElementById(this.id + 'Hour'), minNumber:0, maxNumber:23, focus:this.timeFocus=='Hour'},
						{field:this.document.getElementById(this.id + 'Minute'), minNumber:0, maxNumber:59, focus:this.timeFocus=='Minute'},
						{field:this.document.getElementById(this.id + 'Second'), minNumber:0, maxNumber:59, focus:this.timeFocus=='Second'}];
	new FormField.NumberPicker(this.getAttribute('title'), numberFields, ':', timePickerButton, this.terminalType).show();
};
DateTimeField.prototype._update = function() {
	var value = this.document.getElementById(this.id + "Date").value;
	if(value!='') {
		value += ' ' + new Number(this.document.getElementById(this.id + "Hour").value);
		value += ':' + new Number(this.document.getElementById(this.id + "Minute").value);
		value += ':' + new Number(this.document.getElementById(this.id + "Second").value);
	}
	this.getInputElement().value = value;
	try {
		this.getInputElement().onchange();
	}
	catch(e) {
	
	}
};
DateTimeField.setValue = function(fieldName, timeField, timeValue) { 
	var field = FormField.getFormField(fieldName);
	var input = field.document.getElementById(field.id + timeField.substring(0, 1).toUpperCase() + timeField.substring(1));
	input.value = timeValue;
	input.onchange();
};

DayField = function(inputElementHTML, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, terminalType) {
	style = (!style || style=='null' ? '' : style + ";") + 'width:68px !important';
	FormField.call(this, inputElementHTML, 'hidden', style, styleClass);
	this.terminalType = terminalType;
	this.dayFocus = "Month"; 
	var monthValue = '';
	var dayValue = '';
	var fieldValue = this.getAttribute("value");
	if(fieldValue!='') {
		var values = fieldValue.split("\-");
		monthValue = Number(values[0]);
		dayValue = Number(values[1]);
	}
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'dropDownButton';
	}
	var title = this.getAttribute('title');
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%"' + (title ? ' title="' + title + '"' : '') + '>' +
			   '	<tr>' +
			   '		<td style="padding:0px !important; width:50%"><input id="' + this.id + 'Month" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + monthValue + '"/></td>' +
			   '		<td style="padding:0px !important; width:8px" align="center">-</td>' +
			   '		<td style="padding:0px !important; width:50%"><input id="' + this.id + 'Day" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + dayValue + '"/></td>' +
			   '		<td nowrap="nowrap" id="dayPicker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important; ' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	var dayField = this;
	
	var dayPickerButton = this.document.getElementById('dayPicker_' + this.id);
	dayPickerButton.onclick = function() {
		dayField.onSelectDay(dayPickerButton);
	};
	
	var fieldNames = ["Month", "Day"];
	for(var i=0; i<fieldNames.length; i++) {
		this.document.getElementById(this.id + fieldNames[i]).onchange = function() {
			dayField._update();
		};
		this.document.getElementById(this.id + fieldNames[i]).onfocus = function() {
			dayField.dayFocus = this.id.substring(dayField.id.length);
		};
	}
	
	var fontSize = CssUtils.getElementComputedStyle(this.document.getElementById(this.id + "Month"), 'font-size');
	fontSize = fontSize ? Number(fontSize.replace('px', '')) : 12;
	if(fontSize > 12) {
		this.getFieldElement().style.width = Math.floor(68 * fontSize / 12) + "px";
	}
};
DayField.prototype = new FormField();
DayField.prototype.onSelectDay = function(dayPickerButton) {
	var numberFields = [{field:this.document.getElementById(this.id + 'Month'), minNumber:1, maxNumber:12, focus:this.dayFocus=='Month'},
						{field:this.document.getElementById(this.id + 'Day'), minNumber:1, maxNumber:function(allFieldValues) {
							return allFieldValues[0]==2 ? 29 : (',1,3,5,7,8,10,12,'.indexOf(',' + allFieldValues[0] + ',')!=-1 ? 31 : 30);
						}, focus:this.dayFocus=='Day'}];
	new FormField.NumberPicker(this.getAttribute('title'), numberFields, '-', dayPickerButton, this.terminalType).show();
};
DayField.prototype._update = function() {
	var value = '';
	var month = new Number(this.document.getElementById(this.id + "Month").value);
	if(!isNaN(month) && month>=1 && month<=12) {
		var day = new Number(this.document.getElementById(this.id + "Day").value);
		if(!isNaN(month) && day>=1) {
			var limit = 30;
			if(month==2) {
				limit = 29;
			}
			else if(',1,3,5,7,8,10,12,'.indexOf(',' + month + ',')!=-1) {
				limit = 31;
			}
			day = (day>=limit ? limit : day);
			value = (month<10 ? "0" : "") + month + "-" + (day<10 ? "0" : "") + day;
		}
	}
	this.getInputElement().value = value;
	try {
		this.getInputElement().onchange();
	}
	catch(e) {
	
	}
};
DayField.setValue = function(fieldName, dayField, dayValue) { 
	var field = FormField.getFormField(fieldName);
	var input = field.document.getElementById(field.id + dayField.substring(0, 1).toUpperCase() + dayField.substring(1));
	input.value = dayValue;
	input.onchange();
};

DropdownField = function(inputElementHTML, listValues, valueField, titleField, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, listPickerWidth, terminalType) {
	FormField.call(this, inputElementHTML, 'text', style, styleClass);
	this.listValues = listValues;
	this.valueField = valueField;
	this.titleField = titleField;
	this.listPickerWidth = listPickerWidth;
	this.terminalType = terminalType;
	this.parentElement = parentElement;
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'dropDownButton';
	}
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">' +
			   '	<tr>' +
			   '		<td style="padding:0px !important;">' + this.resetInputElementHTML() + '</td>' +
			   '		<td nowrap="nowrap" id="picker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important; ' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	var dropdownField = this;
	var fieldElement = this.getFieldElement();
	var form = DomUtils.getParentNode(fieldElement, "form");
	this.form = form ? form : this.document.body;
	
	var dropdownPickerButton = inputElementHTML.toLowerCase().indexOf(" readonly")==-1 ? this.document.getElementById('picker_' + this.id) : fieldElement;
	dropdownPickerButton.onclick = function() {
		dropdownField.onDropdown();
	};
	EventUtils.addEvent(window, "load", function() {
		try { dropdownField.getField(true).onchange(); } catch(e) {}
		try { dropdownField.getField(false).onchange(); } catch(e) {}
	});
};
DropdownField.prototype = new FormField();
DropdownField.prototype.onDropdown = function() { 
	new FormField.ListPicker(this._getListValues(), this.getField(false), this.getField(true), this.getFieldElement(), this.listPickerWidth, false, null, this.terminalType).show();
};
DropdownField.prototype._getListValues = function() { 
	if(typeof this.listValues == 'function') {
		return this.listValues.call(this);
	}
	else {
		return this.listValues;
	}
};
DropdownField.prototype.getField = function(isTitleInput) { 
	var fieldName = isTitleInput ? this.titleField : this.valueField;
	if(!fieldName || fieldName=="") {
		return null;
	}
	var	input = this.inputElementHTML.indexOf('name="' + fieldName + '"')==-1 ? DomUtils.getElement(this.form, "input", fieldName) : this.getInputElement();
	if(input) {
		return input;
	}
	input = this.document.getElementsByName(fieldName)[0];
	if(input) {
		return input;
	}
	return this.getInputElement();
};
DropdownField.prototype.setValue = function(value) { 
	this.getField(false).value = value;
	
	var items = this._getListValues().split('\0');
	for(var i=0; i<items.length; i++) {
		var index = items[i].indexOf("|");
		if(items[i].substring(index+1).trim()==value) {
			this.getField(true).value = (index==-1 ? items[i] : items[i].substring(0, index)).trim();
			return true;
		}
	}
	this.getField(true).value = value;
	return false;
};
DropdownField.setListValues = function(fieldName, listValues) { 
	var dropdownField = DropdownField.getDropdownField(fieldName);
	if(dropdownField) {
		dropdownField.listValues = listValues;
	}
};
DropdownField.getListValues = function(fieldName) { 
	var dropdownField = DropdownField.getDropdownField(fieldName);
	if(dropdownField) {
		return dropdownField._getListValues();
	}
};
DropdownField.getSelectedListValue = function(fieldName) { 
	var list = DropdownField.getListValues(fieldName);
	return list.split('\0')[DropdownField.getSelectedIndex(fieldName)];
};
DropdownField.setValue = function(fieldName, value) { 
	if(!value) {
		return false;
	}
	var dropdownField = DropdownField.getDropdownField(fieldName);
	return dropdownField ? dropdownField.setValue(value) : false;
};
DropdownField.setValueByIndex = function(fieldName, valueIndex) { 
	var dropdownField = DropdownField.getDropdownField(fieldName);
	if(!dropdownField) {
		return;
	}
	var items = dropdownField._getListValues().split('\0');
	if(valueIndex>items.length-1) {
		return false;
	}
	var index = items[valueIndex].indexOf("|");
	dropdownField.getField(false).value = items[valueIndex].substring(index + 1).trim();
	dropdownField.getField(true).value = (index==-1 ? items[valueIndex] : items[valueIndex].substring(0, index)).trim();
	return true;
};
DropdownField.getSelectedIndex = function(fieldName) { 
	var dropdownField = DropdownField.getDropdownField(fieldName);
	if(!dropdownField || !dropdownField.listValues || dropdownField.listValues=="") {
		return -1;
	}
	var title = dropdownField.getField(true).value.trim();
	var value = dropdownField.getField(false).value.trim();
	var items = dropdownField._getListValues().split('\0');
	for(var i=0; i<items.length; i++) {
		var index = items[i].indexOf("|");
		if(value==items[i].substring(index+1).trim()) {
			return i;
		}
		if(title==(index==-1 ? items[i] : items[i].substring(0, index)).trim()) {
			return i;
		}
	}
	return -1;	
};
DropdownField.getDropdownField = function(fieldName) { 
	var dropdownField = FormField.getFormField(fieldName);
	return dropdownField ? dropdownField : FormField.getFormField(fieldName + "_title");
};

FormField.DatePicker = function(inputField, displayArea, alignLeft, terminalType) {
	this.inputField = inputField;
	var date = new Date(inputField.value.replace(new RegExp("-", "g"), "/"));
	if(!isNaN(date)) {
		window.datePickerLastValue = date;
	}
	else {
		date = window.datePickerLastValue ? window.datePickerLastValue : new Date();
   	}
   	this.year = date.getFullYear();
   	this.month = date.getMonth() + 1;
   	this.day = date.getDate();
   	FormField.Picker.call(this, inputField.title, this._generatePickerHTML, displayArea, 330, 200, !alignLeft, true, false, false, terminalType);
	if(!this.isTouchMode) {
		this._resetPicker(this.pickerFrame.document.body); 
	}
	else {
		this._initTouchPicker();
	}
};
FormField.DatePicker.prototype = new FormField.Picker();
FormField.DatePicker.prototype.getRelationFields = function() {
	return [this.inputField];
};
FormField.DatePicker.prototype._generatePickerHTML = function() { 
	var weekDays = ["\u661F\u671F\u65E5", "\u661F\u671F\u4E00", "\u661F\u671F\u4E8C", "\u661F\u671F\u4E09", "\u661F\u671F\u56DB", "\u661F\u671F\u4E94", "\u661F\u671F\u516D"];
	var html = ''
	html += '<div class="datePicker" style="clear: both;">' +
			' <div class="yearMonthPicker">' +
			'	<table border="0" cellspacing="0" cellpadding="0">' +
			'	 <tr>' +
			'	  <td><div id="yearPicker" class="yearPicker" style="overflow: hidden;"></div></td>' +
			'	  <td><div id="monthPicker" class="monthPicker" style="overflow: hidden;"></div></td>' +
			'	 </tr>' +
			'	</table>' +
			' </div>' +
			' <div id="dayPicker" style="clear: both;">' +
			'  <table id="dayTable" class="dayTable" border="1" cellspacing="0" cellpadding="0" width="100%">' +
			'	<tr>';
	for(var i=0; i<weekDays.length; i++) {
		html += '	<td align="center" class="week">' + weekDays[i] + '</td>';
	}
	html += '	</tr>';
	for(var i=0; i<6; i++) {
		html+= '<tr>';
		for(var j=0; j<7; j++) { 
			html += '<td align="center" class="day"></td>';
		}
		html += '</tr>';
	}
	html += '  </table>' +
			' </div>' +
			'</div>';
	return html;
};
FormField.DatePicker.prototype._initTouchPicker = function() { 
	this.created = false;
	var picker = this;
	this.doOK = function() {
		if(picker.day!=-1) {
			picker.inputField.value = picker.year + '-' + picker.month + '-' + picker.day;
			window.top.datePickerLastValue = new Date(picker.year + '/' + picker.month + '/' + picker.day);
			try {
				picker.inputField.onchange();
			}
			catch(e) {
			
			}
		}
	};
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/jeaf/common/js/scroller.js', 'scrollerScript', function() {
	   	var dayPicker = DomUtils.getElement(picker.pickerBody, 'div', 'dayPicker');
		dayPicker.scroller = new Scroller(dayPicker, false, false, false, false, false);
		dayPicker.scroller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
			if(Math.abs(y) < Math.abs(x)) {
				y = 0;
			}
			else {
				x = 0;
			}
			if(x>0 || y>0) {
				picker.month++;
				if(picker.month>12) {
					picker.month = 1;
					picker.year++;
				}
			}
			else if(x<0 || y<0) {
				picker.month--;
				if(picker.month<1) {
					picker.month = 12;
					picker.year--;
				}
			}
			picker.day = -1;
			picker._resetPicker(picker.pickerBody);
		};
		picker._resetPicker(picker.pickerBody);
		picker.created = true;
   	});
};
FormField.DatePicker.prototype._resetPicker = function(pickerBody) { 
   	var picker = this;
  	
   	FormField.NumberPicker.generateNumberPicker(DomUtils.getElement(pickerBody, 'div', 'yearPicker'), this.year - 10, this.year + 10, this.year, "\u5E74", function(numberValue) {
   		picker.year = numberValue;
   		picker.day = -1;
   		picker._resetPicker(pickerBody);
   	}, false, this.isTouchMode);
   	
   	
   	FormField.NumberPicker.generateNumberPicker(DomUtils.getElement(pickerBody, 'div', 'monthPicker'), 1, 12, this.month, "\u6708", function(numberValue) {
   		picker.month = numberValue;
   		picker.day = -1;
   		picker._resetPicker(pickerBody);
   	}, false, this.isTouchMode);
   	
   	
  	this.dayCell = null;
	var table = DomUtils.getElement(pickerBody, 'table', 'dayTable');
	var today = new Date();
	var date = new Date(this.year, this.month - 1, 1);
	var week = date.getDay();
	var maxDay = 100;
	for(var i=0; i<42; i++) {
		var cell = table.rows[Math.floor(i/7)+1].cells[i%7];
		var value = "&nbsp;";
		var className = "day";
		if(i >= week && i < maxDay) {
			value = date.getDate();
			date.setDate(value + 1);
			if(date.getMonth() + 1 != this.month) {
				maxDay = value;
			}
			if(i%7==0 || i%7==6) {
				className += " weekend";
			}
			if(this.year==today.getFullYear() && this.month==today.getMonth() + 1 && value==today.getDate()) {
				className += " today";
			}
			if(value==this.day) {
				this.dayCell = cell;
				className += " selectedDay";
			}
		}
		cell.innerHTML = value;
		cell.className = className;
		if(cell.onclick) {
			continue;
		}
		cell.onclick = function() {
			var day = Number(this.innerHTML);
			if(isNaN(day)) {
				return;
			}
			picker.day = day;
			if(picker.dayCell) {
				picker.dayCell.className = picker.dayCell.className.replace(" selectedDay", '');
			}
			picker.dayCell = this;
			this.className = this.className + " selectedDay";
			if(picker.isTouchMode) {
				return;
			}
			picker.inputField.value = picker.year + '-' + picker.month + '-' + picker.day;
			window.top.datePickerLastValue = new Date(picker.year + '/' + picker.month + '/' + picker.day);
			try {
				picker.inputField.onchange();
			}
			catch(e) {
			
			}
			picker.destory();
		};
	}
};
FormField.DatePicker.prototype._generateYearList = function() { 
	var itemsText = '';
	for(var i = this.year - 50; i < this.year + 50; i++) {
		itemsText += (itemsText=='' ? '' : '\0 ') + i + '\u5E74';
	}
	return itemsText;
};

FormField.ListPicker = function(values, valueField, titleField, displayArea, pickerWidth, alignRight, callback, terminalType) {
	var title = valueField ? valueField.title : (titleField ? titleField.title : null);
	FormField.Picker.call(this, title ? title : '\u9009\u62E9', this._generatePickerHTML, displayArea, pickerWidth, 180, alignRight, false, false, false, terminalType);
	this.valueField = valueField;
	this.titleField = titleField;
	this.callback = callback;
	
	if(values.substring(values.length - 1)=='\0') {
		values = values.substring(0, values.length - 1);
	}
	this.selectedIndex = -1;
	this.listItems = [];
	values = values.split("\0");
	for(var i = 0; i < values.length && values[i]!=""; i++) { 
		var index = values[i].indexOf("|");
		var listItem = {value: values[i].substring(index+1).trim(), title: (index==-1 ? values[i] : values[i].substring(0, index)).trim()};
		this.listItems.push(listItem);
		try {
			if(this.valueField.value==listItem.value) {
				this.selectedIndex = i;
			}
		}
		catch(e) {
		
		}
	}
	
	if(!this.isTouchMode) {
		this._initComputerPicker(); 
	}
	else {
		this._initTouchPicker(); 
	}
};
FormField.ListPicker.prototype = new FormField.Picker();
FormField.ListPicker.prototype.getRelationFields = function() {
	return [this.valueField, this.titleField];
};
FormField.ListPicker.prototype._generatePickerHTML = function() { 
	var html;
	if(this.isTouchMode) { 
	 	html = '<div>' +
	 		   '	<table id="listPicker.listTable" border="0" cellspacing="0" cellpadding="0" width="100%">' +
			   '	</table>' +
			   '</div>';
	 }
	 else { 
		html = '<!DOCTYPE html>\n' +
			   '<html style="width:100%; height:100%">\n' +
			   '	<head>\n' +
			   '		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">\n' +
			   '	</head>\n' +
			   '	<body style="overflow:hidden; margin:0px; padding:0px; width:100%;  height:100%;">\n' +
			   '		<div class="listbar" style="overflow-y: auto; overflow-x: hidden;">\n' +
			   '			<table id="listPicker.listTable" border="0" cellpadding="0" cellspacing="0" width="100%" style="table-layout:fixed;">\n' +
			   '			</table>\n' +
			   '		</div>\n' +
			   '	</body>\n' +
			   '</html>';
	 }
	 return html;
};
FormField.ListPicker.prototype._initComputerPicker = function() { 
	var picker = this;
	this.listTable = this.pickerFrame.document.getElementById("listPicker.listTable");
	for(var i = 0; i < this.listItems.length; i++) { 
		var td = this.listTable.insertRow(-1).insertCell(-1);
		td.noWrap = true;
		td.className = "listnormal";
		td.id = this.listItems[i].value;
		td.innerHTML = this.listItems[i].title;
		td.onmouseover = function() {
			for(var i = 0; i < picker.listTable.rows.length; i++) {
				var cell =  picker.listTable.rows[i].cells[0];
				cell.className = cell==this ? "listover" : "listnormal";
			}
		}
		td.onmousedown = function() {
			picker.destory();
			picker._onOK(this.id, this.innerHTML);
		}
	}
	this.pickerHeight = Math.min(Math.max(this.listTable.offsetHeight + DomUtils.getBorderWidth(this.listTable.parentNode, 'top') + DomUtils.getBorderWidth(this.listTable.parentNode, 'bottom'), 10), 180);
};
FormField.ListPicker.prototype._initTouchPicker = function() { 
	var picker = this;
	this.doOK = function() {
		var values = "", titles = ""; 
		var inputs = picker.pickerBody.getElementsByTagName('input');
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].checked) {
				values += (values=="" ? "" : ",") + inputs[i].value;
				titles += (titles=="" ? "" : ",") + inputs[i].parentNode.parentNode.cells[0].innerHTML;
			}
		}
		picker._onOK(values, titles);
	};
	this.listTable = DomUtils.getElement(this.pickerBody, 'table', 'listPicker.listTable');
	for(var i = 0; i < this.listItems.length; i++) { 
		var tr = this.listTable.insertRow(-1);
		tr.onclick = function() {
			var input = this.getElementsByTagName('input')[0];
			input.checked = this.multiSelect ? !input.checked : true;
		};
		
		var valueTd = tr.insertCell(-1);
		valueTd.width = "100%";
		valueTd.innerHTML = this.listItems[i].title;
		
		var boxTd = tr.insertCell(-1);
		boxTd.align = "center";
		boxTd.nowrap = true;
		var radio = this.listTable.ownerDocument.createElement('input');
		radio.name = "listPicker.selectedItem";
		radio.type = this.multiSelect ? 'checkbox' : 'radio';
		radio.value = this.listItems[i].value;
		boxTd.appendChild(radio);
		
		valueTd.className = boxTd.className = "listItem";
		if(i == this.listItems.length - 1) { 
			 valueTd.style.borderBottomStyle = boxTd.style.borderBottomStyle = "none";
		}
	}
	var clientHeight = DomUtils.getClientHeight(this.pickerContainer.ownerDocument);
	if(this.pickerContainer.offsetHeight <= clientHeight - 20) {
		return;
	}
	var div = this.listTable.parentNode;
	div.style.height = (div.offsetHeight - this.pickerContainer.offsetHeight + clientHeight - 20) + "px";
	div.style.overflow = "hidden";
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/jeaf/common/js/scroller.js', 'scrollerScript', function() {
		new Scroller(div, false, true, true, false, false);
	});
};
FormField.ListPicker.prototype._onOK = function(value, title) {
	try { this.titleField.value = title; this.titleField.focus(); } catch(e) {}
	try { this.valueField.value = value; this.valueField.focus(); } catch(e) {}
	try { this.titleField.onchange(); } catch(e) {}
	try { this.valueField.onchange(); } catch(e) {}
	if(!this.callback) {
		return;
	}
	try {
		this.callback.call(this, value, title);
	}
	catch(e) {
	
	}
};
FormField.ListPicker.prototype.show = function(pickerLeft, pickerTop, displayOnly) {
	this.constructor.prototype.show.call(this, pickerLeft, pickerTop, displayOnly);
	if(this.selectedIndex==-1) {
		return;
	}
	if(!this.isTouchMode) {
		var cell = this.listTable.rows[this.selectedIndex].cells[0];
		cell.className = "listover";
		this.listTable.parentNode.scrollTop = cell.offsetTop - this.pickerHeight / 2 + cell.offsetHeight / 2;
	}
	else {
		var row = this.listTable.rows[this.selectedIndex];
		row.cells[1].childNodes[0].checked = true;
		this.listTable.parentNode.scrollTop = row.cells[0].offsetTop - this.listTable.parentNode.offsetHeight / 2 + row.cells[0].offsetHeight / 2;
	}
};

FormField.NumberPicker = function(pickerTitle, numberFields, separator, displayArea, terminalType) {
	this.numberFields = numberFields;
	this.separator = separator;
	FormField.Picker.call(this, pickerTitle, this._generatePickerHTML, displayArea, 330, 200, true, true, false, false, terminalType);
	if(this.isTouchMode) {
		this._initTouchPicker();
	}
};
FormField.NumberPicker.prototype = new FormField.Picker();
FormField.NumberPicker.prototype.getRelationFields = function() {
	var fields = [];
	for(var i = 0; i < this.numberFields.length; i++) {
		fields.push(this.numberFields[i].field);
	}
	return fields;
};
FormField.NumberPicker.prototype._generatePickerHTML = function() { 
	if(!this.isTouchMode) {
		return null;
	}
	var html = '';
	html += '<table id="numberPickerTable" cellspacing="0" cellpadding="0" align="center">';
	html += '	<tr>';
	for(var i = 0; i < this.numberFields.length; i++) {
		html+= '	<td><div class="numberPicker" style="overflow: hidden;"></div></td>';
		if(i < this.numberFields.length - 1) {
			html +='<td class="numberPickerSeparator">' + this.separator + '</td>';
		}
	}
	html += '	</tr>';
	html += '</table>';
	return html;
};
FormField.NumberPicker.prototype._initTouchPicker = function() { 
	var picker = this;
	this.doOK = function() {
		for(var i = 0; i < picker.numberFields.length; i++) {
			picker.numberFields[i].field.value = picker.numberFields[i]._value;
			try {
				picker.numberFields[i].field.onchange();
			}
			catch(e) {
			
			}
		}
	};
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/jeaf/common/js/scroller.js', 'scrollerScript', function() {
	   	picker._resetPicker();
   	});
};
FormField.NumberPicker.prototype._resetPicker = function(pickerBody) { 
   	var picker = this;
   	this._retrieveFieldValues();
	var numberPickerTable = DomUtils.getElement(this.pickerBody, 'table', 'numberPickerTable');
   	for(var i = 0; i < this.numberFields.length; i++) {
  		FormField.NumberPicker.generateNumberPicker(numberPickerTable.rows[0].cells[i * 2].childNodes[0], this.numberFields[i]._minNumber, this.numberFields[i]._maxNumber, this.numberFields[i]._value, this.numberFields[i].suffix, function(numberValue, parentElement) {
	  		picker.numberFields[parentElement.parentNode.cellIndex / 2]._value = numberValue;
	  		picker._resetPicker();
	  	}, true, this.isTouchMode);
	}
};
FormField.NumberPicker.prototype.show = function(pickerLeft, pickerTop, displayOnly) {
	if(this.isTouchMode) {
		this.constructor.prototype.show.call(this, pickerLeft, pickerTop, displayOnly);
		return;
	}
	var focus = 0;
	for(; focus < this.numberFields.length && !this.numberFields[focus].focus; focus++);
	this._retrieveFieldValues();
	var itemsText = '';
	for(var i= this.numberFields[focus]._minNumber; i <= this.numberFields[focus]._maxNumber; i++) {
		itemsText += (itemsText=='' ? '' : '\0 ') + i;
	}
	var picker = this;
	new FormField.ListPicker(itemsText, this.numberFields[focus].field, null, this.displayArea, 50, true, function() {
		if(focus < picker.numberFields.length - 1) { 
			window.setTimeout(function() {
				picker.numberFields[focus + 1].field.focus();
			}, 100);
		}
	}).show();
};
FormField.NumberPicker.prototype._retrieveFieldValues = function() {
	var values = [];
	
	for(var i = 0; i < this.numberFields.length; i++) {
		if(!this.numberFields[i]._value && this.numberFields[i]._value!=0) {
			var suffix = this.numberFields[i].suffix;
			this.numberFields[i]._value = Number(suffix ? this.numberFields[i].field.value.replace(suffix, '') : this.numberFields[i].field.value);
			if(isNaN(this.numberFields[i]._value)) {
				this.numberFields[i]._value = 0;
			}
		}
		values.push(this.numberFields[i]._value);
	}
	
	for(var i = 0; i < this.numberFields.length; i++) {
		this.numberFields[i]._minNumber = typeof this.numberFields[i].minNumber == 'function' ? this.numberFields[i].minNumber.call(this, values) : this.numberFields[i].minNumber;
		this.numberFields[i]._maxNumber = typeof this.numberFields[i].maxNumber == 'function' ? this.numberFields[i].maxNumber.call(this, values) : this.numberFields[i].maxNumber;
		this.numberFields[i]._value = Math.min(Math.max(this.numberFields[i]._value, this.numberFields[i]._minNumber), this.numberFields[i]._maxNumber);
	}
};
FormField.NumberPicker.generateNumberPicker = function(parentElement, minValue, maxValue, currentValue, suffix, onNumberPicked, verticalScroll, isTouchMode) {
	if(!suffix) {
		suffix = '';
	}
	parentElement.innerHTML = "";
	var document = parentElement.ownerDocument;
	var divNumbers = document.createElement("div");
	parentElement.appendChild(divNumbers);
	var selectedIndex;
	for(var i = minValue-1; i <= maxValue+1; i++) {
		var span = document.createElement("span");
		span.className = "pickerItem" + (currentValue==i ? " selectedPickerItem" : "");
		if(!verticalScroll) {
			span.style.display = 'inline-block';
		}
		span.innerHTML = i==minValue-1 || i==maxValue+1 ? '&nbsp;' : i + suffix + (isTouchMode || currentValue!=i ? '' : '<span class="dropDownButton">&nbsp;</span>');
		if(currentValue==i) {
			selectedIndex = divNumbers.childNodes.length;
		}
		else if(i!=minValue-1 && i!=maxValue+1) {
			span.onclick = function() {
				onNumberPicked.call(null, Number(suffix=='' ? this.innerHTML : this.innerHTML.replace(suffix, '')), parentElement);
			};
		}
		divNumbers.appendChild(span);
		if(isTouchMode || currentValue!=i) {
			continue;
		}
		span.onclick = function() {
			var itemsText = '';
			for(var j = minValue; j <= maxValue; j++) {
				itemsText += (itemsText=='' ? '' : '\0 ') + j + suffix;
			}
			var listPicker = new FormField.ListPicker(itemsText, null, null, this, this.offsetWidth + 8, true, function(selectedItemValue, selectedItemTitle) {
				onNumberPicked.call(null, Number(suffix=='' ? selectedItemValue : selectedItemValue.replace(suffix, '')), parentElement);
			});
			listPicker.selectedIndex = currentValue - minValue;
			listPicker.show();
		};
	}
	if(verticalScroll) { 
		parentElement.scrollTop = (selectedIndex - 1) * divNumbers.childNodes[0].offsetHeight;
	}
	else { 
		divNumbers.style.width = (divNumbers.childNodes[0].offsetWidth * (maxValue - minValue + 3)) + "px";
		parentElement.scrollLeft = (selectedIndex - 1) * divNumbers.childNodes[0].offsetWidth;
	}
	if(!isTouchMode) {
		return;
	}
	
	parentElement.scroller = new Scroller(parentElement, !verticalScroll, verticalScroll, false, false, false);
	parentElement.scroller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
		var index;
		if(verticalScroll) {
			index = Math.round(parentElement.scrollTop / divNumbers.childNodes[0].offsetHeight) + 1;
		}
		else {
			index = Math.round(parentElement.scrollLeft / divNumbers.childNodes[0].offsetWidth) + 1;
		}
		onNumberPicked.call(null, Number(suffix=='' ? divNumbers.childNodes[index].innerHTML : divNumbers.childNodes[index].innerHTML.replace(suffix, '')), parentElement);
	};
};

SelectField = function(inputElementHTML, onSelect, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement) {
	FormField.call(this, inputElementHTML, 'text', style, styleClass);
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'selectButton';
	}
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">' +
			   '	<tr>' +
			   '		<td style="padding:0px !important;">' + this.resetInputElementHTML() + '</td>' +
			   '		<td nowrap="nowrap" id="picker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important; ' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	
	var selectButton = inputElementHTML.toLowerCase().indexOf(" readonly")==-1 ? this.document.getElementById('picker_' + this.id ) : this.getFieldElement();
	selectButton.onclick = function() {
		eval(onSelect);
	};
};
SelectField.prototype = new FormField();

TextAreaField = function(inputElementHTML, styleClass, style, parentElement) {
	FormField.call(this, inputElementHTML, 'text', style, styleClass, true);
	this.writeFieldElement(this.resetInputElementHTML(), parentElement);
};
TextAreaField.prototype = new FormField();

TextField = function(inputElementHTML, styleClass, style, parentElement) {
	FormField.call(this, inputElementHTML, null, style, styleClass);
	this.writeFieldElement(this.resetInputElementHTML(), parentElement);
};
TextField.prototype = new FormField();

TimeField = function(inputElementHTML, secondEnabled, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, terminalType) {
	style = (!style || style=='null' ? '' : style + ";") + 'width:' + (secondEnabled ? 106 : 68) + 'px !important';
	FormField.call(this, inputElementHTML, 'hidden', style, styleClass);
	this.terminalType = terminalType;
	this.timeFocus = "Hour"; 
	this.secondEnabled = secondEnabled;
	var hourValue = '';
	var minuteValue = '';
	var secondValue = '';
	var fieldValue = this.getAttribute("value");
	if(fieldValue!='') {
		var values = fieldValue.split(":");
		hourValue = Number(values[0]);
		minuteValue = Number(values[1]);
		if(secondEnabled) {
			secondValue = Number(values[2]);
		}
	}
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'dropDownButton';
	}
	var title = this.getAttribute('title');
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%"' + (title ? ' title="' + title + '"' : '') + '>' +
			   '	<tr>' +
			   '		<td style="padding:0px !important; width:' + (secondEnabled ? 33 : 50) + '%"><input id="' + this.id + 'Hour" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + hourValue + '"/></td>' +
			   '		<td style="padding:0px !important; width:8px" align="center">:</td>' +
			   '		<td style="padding:0px !important; width:' + (secondEnabled ? 33 : 50) + '%"><input id="' + this.id + 'Minute" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + minuteValue + '"/></td>' +
			   (secondEnabled ? '		<td style="padding:0px !important; width:8px" align="center">:</td>' : '') +
			   (secondEnabled ? '		<td style="padding:0px !important; width:33%"><input id="' + this.id + 'Second" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + secondValue + '"/></td>' : '') +
			   '		<td nowrap="nowrap" id="timePicker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important;' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	var timeField = this;
	
	var timePickerButton = this.document.getElementById('timePicker_' + this.id);
	timePickerButton.onclick = function() {
		timeField.onSelectTime(timePickerButton);
	};
	
	var fieldNames = ["Hour", "Minute"];
	if(secondEnabled) {
		fieldNames.append("Second");
	}
	for(var i=0; i<fieldNames.length; i++) {
		this.document.getElementById(this.id + fieldNames[i]).onchange = function() {
			timeField._update();
		};
		this.document.getElementById(this.id + fieldNames[i]).onfocus = function() {
			timeField.timeFocus = this.id.substring(timeField.id.length);
		};
	}
	
	var fontSize = CssUtils.getElementComputedStyle(this.document.getElementById(this.id + "Hour"), 'font-size');
	fontSize = fontSize ? Number(fontSize.replace('px', '')) : 12;
	if(fontSize > 12) {
		this.getFieldElement().style.width = Math.floor((secondEnabled ? 106 : 68) * fontSize / 12) + "px";
	}
};
TimeField.prototype = new FormField();
TimeField.prototype.onSelectTime = function(timePickerButton) { 
	var numberFields = [{field:this.document.getElementById(this.id + 'Hour'), minNumber:0, maxNumber:23, focus:this.timeFocus=='Hour'},
						{field:this.document.getElementById(this.id + 'Minute'), minNumber:0, maxNumber:59, focus:this.timeFocus=='Minute'}];
	if(this.secondEnabled) {
		numberFields.push({field:this.document.getElementById(this.id + 'Second'), minNumber:0, maxNumber:59, focus:this.timeFocus=='Second'});
	}
	new FormField.NumberPicker(this.getAttribute('title'), numberFields, ':', timePickerButton, this.terminalType).show();
};
TimeField.prototype._update = function() {
	var hour = new Number(this.document.getElementById(this.id + "Hour").value);
	var value = (hour<10 ? "0" : "") + hour;
	var minute = new Number(this.document.getElementById(this.id + "Minute").value);
	value += ":" + (minute<10 ? "0" : "") + minute;
	if(this.secondEnabled) {
		var second = new Number(this.document.getElementById(this.id + "Second").value);
		value += ":" + (second<10 ? "0" : "") + second;
	}
	this.getInputElement().value = value;
	try {
		this.getInputElement().onchange();
	}
	catch(e) {
	
	}
};
TimeField.setValue = function(fieldName, timeField, timeValue) { 
	var field = FormField.getFormField(fieldName);
	var input = field.document.getElementById(field.id + timeField.substring(0, 1).toUpperCase() + timeField.substring(1));
	input.value = timeValue;
	input.onchange();
};

FormUtils = function() {
};
FormUtils.doAction = function(actionName, param, notLockForm, formName, target) { 
	if(window.submitting) {
		return false;
	}
	var form = FormUtils._getForm(formName);
	form.oldAction = form.action;
	form.oldTarget = form.target;
	if(target) {
		form.target = target;
	}
	if(!param) {
		param = "";
	}
	if(actionName.substring(0, 1)=="/") {
		form.action = actionName + (param=="" ? "" : (actionName.indexOf('?')==-1 ? '?' : '&') + param);
	}
	else {
		var action = form.action;
		var index = action.lastIndexOf(".shtml");
		if(index==-1) {
			return false;
		}
		index = action.lastIndexOf("/", index);
		if(index==-1) {
			return false;
		}
		form.action = action.substring(0, index + 1) + actionName + ".shtml" + (param=="" ? "" : "?" + param);
	}
	FormUtils.submitForm(notLockForm, formName);
	return true;
};
FormUtils.submitForm = function(notLockForm, formName) { 
	if(window.submitting) {
		return false;
	}
	try {
		HtmlEditor.saveHtmlContent(); 
	}
	catch(e) {
	}
	if(!formOnSubmit()) {
		return false;
	}
	var form = FormUtils._getForm(formName);
	FormUtils.resetBoxElements(form);
	try {
		form.ownerDocument.body.focus(); 
	}
	catch(e) {
	
	}
	window.submitting = !notLockForm && (!form.target || form.target=='' || form.target=='_self');
	if(window.submitting) {
		PageUtils.showToast('\u6B63\u5728\u5904\u7406\u4E2D, \u8BF7\u7A0D\u5019...');  //<img style="border:none" src="' + RequestUtils.getContextPath() + '/jeaf/common/img/loading.gif">
	}
	var displayMode = DomUtils.getElement(form, 'input', 'displayMode');
	var internalForm = DomUtils.getElement(form, 'input', 'internalForm');
	if(form.target=='_self' || !window.submitting || (displayMode && displayMode.value!='window') || !internalForm || internalForm.value!='true') {
		form.submit();
		FormUtils._restoreForm(form);
		return true;
	}
	DialogUtils.openDialog(null, 480, 200, null, null,
		function(dialogBodyFrame) { 
			form.target = dialogBodyFrame.name;
			if(!form.oldAction) {
				form.oldAction = form.action;
			}
			form.action += (form.action.indexOf('?')==-1 ? '?' : '&') + 'displayMode=dialog&internalForm=true' + (window.tabLists ? '&tabSelected=' + window.tabLists[0].getSelectedTabId() : '');
			form.submit();
			FormUtils._restoreForm(form);
		},
		function(dialogBodyFrame) { 
			PageUtils.removeToast();
		},
		function(dialogBodyFrame) { 
			var reloadPageURL = dialogBodyFrame.contentWindow.document.getElementsByName("reloadPageURL")[0];
			if(reloadPageURL && reloadPageURL.value!='') {
				PageUtils.showToast('\u91CD\u65B0\u52A0\u8F7D\u4E2D, \u8BF7\u7A0D\u5019...');
				window.location.href = reloadPageURL.value;
			}
		}
	);
};
FormUtils._getForm = function(formName) { 
	return !formName || formName=='' ? document.forms[0] : (document.forms[formName] ? document.forms[formName] : document.getElementById(formName));
};
FormUtils._restoreForm = function(form) { 
	if(form.oldAction) {
		form.action = form.oldAction;
	}
	if(form.oldTarget && form.oldTarget!='') {
		form.target = form.oldTarget;
	}
	else {
		form.removeAttribute("target");
	}
	form.oldAction = null;
	form.oldTarget = null;
};
FormUtils.resetBoxElements = function(form) { 
	var inputs = form.getElementsByTagName("input");
	for(var i=(inputs ? inputs.length-1: -1); i>=0; i--) {
		if(inputs[i].id && inputs[i].parentNode==form && inputs[i].id.indexOf("hidden_")==0) {
			form.removeChild(inputs[i]);
		}
		if(inputs[i].type=="text" && inputs[i].value==inputs[i].alt) { 
			inputs[i].value = "";
		}
	}
	inputs = form.getElementsByTagName("textarea");
	for(var i=(inputs ? inputs.length-1: -1); i>=0; i--) {
		if(inputs[i].value==inputs[i].getAttribute("alt")) {
			inputs[i].value = "";
		}
	}
	inputs = form.getElementsByTagName("input");
	var checkedFields = new Array(); 
	var uncheckedFields = new Array(); 
	var processedFields = "";
	for(var i=0; i<(inputs ? inputs.length : 0); i++) {
		if(!inputs[i].type || !inputs[i].name) {
			continue;
		}
		var type = inputs[i].type.toLowerCase();
		if(type!="checkbox" && type!="radio" || processedFields.indexOf("[" + inputs[i].name + "]")!=-1) {
			continue;
		}
		processedFields += "[" + inputs[i].name + "]";
		
		var checked = false;
		for(j=i; j<inputs.length; j++) {
			if(inputs[i].name==inputs[j].name && inputs[j].checked) {
				checked = true;
				break;
			}
		}
		if(checked) {
			checkedFields.push(inputs[i].name);
		}
		else {
			uncheckedFields.push(inputs[i].name);
		}
	}
	
	for(var i=0; i<checkedFields.length; i++) {
		var hidden = document.getElementById("hidden_" + checkedFields[i]);
		if(hidden) {
			hidden.parentNode.removeChild(hidden);
		}
	}
	for(var i=0; i<uncheckedFields.length; i++) {
		if(document.getElementById("hidden_" + uncheckedFields[i])) {
			continue;
		}
		var hidden = document.createElement("input");
		hidden.id = "hidden_" + uncheckedFields[i];
		hidden.type = "hidden";
		hidden.name = uncheckedFields[i];
		hidden.value = "";
		form.appendChild(hidden);
	}
};
function formOnSubmit() { 
	return true;
}
FormUtils.getCurrentAction = function() {
	var action = window.location.pathname;
	var endIndex = action.lastIndexOf(".shtml");
	if(endIndex == - 1) {
		endIndex = action.lastIndexOf(".do");
			if(endIndex == - 1)return "";
	}
	var beginIndex = action.lastIndexOf("/", endIndex);
	return action.substring(beginIndex + 1, endIndex);
};
FormUtils.reloadValidateCodeImage = function(validateCodeImageId) { 
	if(!validateCodeImageId) {
		validateCodeImageId = "validateCodeImage";
	}
	var src = document.getElementById(validateCodeImageId).src;
	var index = src.lastIndexOf("?");
	if(index!=-1) {
		src = src.substring(0, index);
	}
	document.getElementById(validateCodeImageId).src = src + "?reload=true&seq=" + Math.random();
};
FormUtils.sendValidateCodeSms = function(mobile, sendLimit, timeInterval, siteId) { 
	if(!mobile || mobile=='') {
		return;
	}
	var iframe = document.getElementById('validateCodeSmsFrame');
	if(!iframe) {
		iframe = document.createElement("iframe");
		iframe.style.display = 'none';
		iframe.id = 'validateCodeSmsFrame';
		var childNodes = document.body.childNodes;
		if(childNodes.length==0) {
			document.body.appendChild(iframe);
		}
		else {
			document.body.insertBefore(iframe, childNodes[0]);
		}
	}
	iframe.src = RequestUtils.getContextPath() + "/jeaf/validatecode/sendValidateCodeSms.shtml?mobile=" + mobile + "&reload=true&sendLimit=" + sendLimit + "&siteId=" + siteId + "&seq=" + Math.random();
};
FormUtils.getTimeValue = function(fieldName) {
	var time = document.getElementsByName(fieldName)[0].value;
	if(time=="") {
		return "";
	}
	return new Date(time.replace(new RegExp("-", "g"), "/").replace(new RegExp("\\x2E0", "g"), ""));
};
FormUtils.pasteText = function(inputFieldName, text) {
	var textbox = document.getElementsByName(inputFieldName)[0];
	textbox.focus();
	if(!textbox.selectionStart && textbox.selectionStart!=0) { 
		document.selection.createRange().text = text;
	}
	else {
		var startPos = textbox.selectionStart;
		var endPos = textbox.selectionEnd;
		textbox.value = textbox.value.substring(0, startPos) + text + textbox.value.substring(endPos);
		startPos += text.length;
		textbox.selectionStart = startPos;
		textbox.selectionEnd = startPos;
	}
};
FormUtils.getSelectText = function(inputFieldName) { 
	var textbox = document.getElementsByName(inputFieldName)[0];
	textbox.focus();
	if(!textbox.selectionStart && textbox.selectionStart!=0) { 
		return document.selection.createRange().text;
	}
	else {
		return textbox.value.substring(textbox.selectionStart, textbox.selectionEnd);
	}
};
FormUtils.getField = function(doc, fieldName) { 
	var fields = doc.getElementsByName(fieldName);
	for(var i=0; i < fields.length; i++) {
		if(',INPUT,TEXTAREA,'.indexOf(',' + fields[i].tagName + ',')!=-1) {
			return fields[i];
		}
	}
	return null;
};
FormUtils.appendInput = function(parentElement, type, name) {
	var input;
	try {
		input = parentElement.ownerDocument.createElement('<input type="' + type + '" name="' + name + '"/>');
	}
	catch(e) {
		input = parentElement.ownerDocument.createElement('input');
		input.type = type;
		input.name = name;
	}
	parentElement.appendChild(input);
};
FormUtils.getAttachmentCount = function(attachmentType) {
	var count = Number(document.getElementById("attachmentFrame_" + attachmentType).contentWindow.document.getElementsByName("attachmentSelector.attachmentCount")[0].value);
	return isNaN(count) ? 0 : count;
};

JsonUtils = function() {
};
JsonUtils.isEqual = function(obj0, obj1) {
	if(!obj0 && !obj1) {
		return true;
	}
	if(!obj0 || !obj1) {
		return false;
	}
	return JsonUtils._compareAttributes(obj0, obj1) && JsonUtils._compareAttributes(obj1, obj0);
};
JsonUtils._compareAttributes = function(obj0, obj1) {
	for(var key in obj0){
		if(key=='uuid') {
			if(obj0.uuid.substring(0, obj0.uuid.indexOf('@'))!=obj1.uuid.substring(0, obj1.uuid.indexOf('@'))) {
				return false;
			}
		}
		else if(!eval('obj0.' + key + '==obj1.' + key)) {
			return false;
		}
	}
	return true;
};
JsonUtils.addJsonObject = function(jsonObjectPool, className, jsonObject) {
    jsonObject.uuid = className + "@" + ("" + Math.random()).substring(2);
    jsonObjectPool.push(jsonObject);
    return jsonObject;
};
JsonUtils.stringify = function(jsonObject) {
	try {
		return JSON.stringify(jsonObject);
	}
	catch(e) {
	
	}
	return JsonUtils._doStringify(jsonObject);
};
JsonUtils._doStringify = function(jsonObject) {
	var jsonText = '{';
	var firstKey = true;
	for(var key in jsonObject) {
		jsonText += (firstKey ? "" : ",") + "\"" + key + "\":";
		firstKey = false;
		var value = eval('jsonObject.' + key);
		jsonText += JsonUtils._doStringifyValue(value);
	}
	return jsonText + "}";
};
JsonUtils._doStringifyValue = function(value) {
	if(value==null) {
		return "null";
	}
	else if((typeof value)=="string") { 
		return "\"" + value.replace(/\\/g, "\\\\").replace(/"/g, "\\\"").replace(/\r/g, "\\r").replace(/\n/g, "\\n").replace(/\t/g, "\\t").replace(/\f/g, "\\f").replace(/\0/g, "\\0") + "\"";
	}
	else if((typeof value)=="number" || (typeof value)=="boolean" || (value instanceof Number) || (value instanceof Boolean)) { 
		return value;
	}
	else if(value instanceof Date) { 
		return "\"" + value.format("yyyy-MM-dd HH:mm:ss") + "\"";
	}
	else if(value.push && value.pop) { 
		var jsonText = "[";
		for(var i=0; i < value.length; i++) {
			jsonText += (i==0 ? "" : ",") + JsonUtils._doStringifyValue(value[i]);
		}
		return jsonText + "]";
	}
	else if((typeof value)=="object") { 
		return JsonUtils._doStringify(value);
	}
	return "null";
};

ListUtils = function() {
};
ListUtils.findObjectByProperty = function(list, propertyName, propertyValue) {
	var objects = ListUtils.findObjectsByProperties(list, [{name:propertyName, value:propertyValue}], 1);
	return objects ? objects[0] : null;
};
ListUtils.findObjectsByProperty = function(list, propertyName, propertyValue) {
	return ListUtils.findObjectsByProperties(list, [{name:propertyName, value:propertyValue}], 0);
};
ListUtils.findObjectsByProperties = function(list, properties, max) {
	var objects = [];
	for(var i=0; i < (list ? list.length : 0) && (max <= 0 || objects.length < max); i++) {
		var j = 0;
		for(; j < properties.length; j++) {
			var value;
			eval('try{value=list[i].' + properties[j].name + ';}catch(e){}');
			if(value!=properties[j].value) {
				break;
			}
		}
		if(j == properties.length) {
			objects.push(list[i]);
		}
	}
	return objects.length==0 ? null : objects;
};
ListUtils.findObjectsByType = function(list, type, max) {
	var objects = [];
	for(var i=0; i < (list ? list.length : 0) && (max <= 0 || objects.length < max); i++) {
		var match = eval('list[i] instanceof ' + type);
		if(match) {
			objects.push(list[i]);
		}
	}
	return objects.length==0 ? null : objects;
};
ListUtils.indexOf = function(list, object) {
	for(var i=0; i<(list ? list.length : 0); i++) {
		if(list[i]==object) {
			return i;
		}
	}
	return -1;
};
ListUtils.removeObject = function(list, object) {
	var index = ListUtils.indexOf(list, object);
	if(index!=-1) {
		list.splice(index, 1);
	}
};
ListUtils.removeObjectByProperty = function(list, propertyName, propertyValue) {
	return ListUtils.removeObjectByProperties(list, [{name:propertyName, value:propertyValue}]);
};
ListUtils.removeObjectByProperties = function(list, properties) {
	for(var i=(list ? list.length - 1 : -1); i>=0; i--) {
		var j = 0;
		for(; j < properties.length; j++) {
			var value;
			eval('try{value=list[i].' + properties[j].name + ';}catch(e){}');
			if(value!=properties[j].value) {
				break;
			}
		}
		if(j == properties.length) {
			var obj = list[i];
			list.splice(i, 1);
			return obj;
		}
	}
};

LoginUtils = function() {
};
LoginUtils.openLoginDialog = function(callback) { 
	DialogUtils.openDialog(RequestUtils.getContextPath() + "/jeaf/sessionmanage/resetSession.shtml", 350, 180, "\u767B\u5F55", callback);
};
LoginUtils.logout = function(redirect, external) {
	if(!redirect) {
		redirect = top.location.href;
	}
	else if(redirect.indexOf("http://")==-1 && redirect.indexOf("https://")==-1) {
		redirect = top.location.protocol + "//" + location.host + redirect;
	}
	window.top.location = RequestUtils.getContextPath() + '/jeaf/sessionmanage/logout.shtml?' + (external ? 'external=true&' : '') + 'redirect=' + StringUtils.utf8Encode(redirect);
};

PopupMenu = function() {
	
};
PopupMenu.popupMenu = function(menuDefinition, onClick, displayArea, menuWidth, align) {
	new PopupMenu().popupMenu(menuDefinition, onClick, displayArea, menuWidth, align);
};
PopupMenu.prototype.popupMenu = function(menuDefinition, onClick, displayArea, menuWidth, align) {
	var popupMenu = this;
	this.topWindow = PageUtils.getTopWindow(); 
	this.cover = PageUtils.createCover(this.topWindow, 0, true);
	this.cover.onclick = function() { 
		popupMenu._hide();
	};
	if(!menuDefinition[0] || !menuDefinition[0].id) {
		var items = menuDefinition.split("\0");
		menuDefinition = new Array();
		for(var i = 0; i < items.length; i++) {
			var itemValues = items[i].split("|");
			menuDefinition[i] = {id:itemValues[itemValues.length-1], title:itemValues[0]};
		}
	}
	this.window = window;
	this.menuDefinition = menuDefinition;
	this.onClick = onClick;
	this.displayArea = displayArea;
	if(!menuWidth || menuWidth<0 || menuWidth==0) {
		menuWidth = this.displayArea.offsetWidth;
	}
	this.menuWidth = !menuWidth || menuWidth<120 ? 120 : menuWidth;
	this.align = (!align ? "auto" : align);
	
	
	this.popupMenuDiv = this._createMenu(false, this.menuDefinition);
	this.popupMenuFrame = this.topWindow.frames["popupMenuFrame"];
	
	
	var pos = DomUtils.getAbsolutePosition(this.displayArea, null, true);
	if(this.align=="auto") {
		this.align = pos.left > this.topWindow.document.body.offsetWidth/2 ? "right" : "left";
	}
	if(align=="topRight") {
		this.popupMenuDiv.style.left = (pos.left + this.displayArea.offsetWidth) + "px";
		this.popupMenuDiv.style.top = pos.top + "px";
	}
	else {
		this.popupMenuDiv.style.display = "";
		this.popupMenuDiv.style.left = (align=="right" ? pos.left + this.displayArea.offsetWidth - this.popupMenuDiv.offsetWidth : pos.left) + "px";
		this.popupMenuDiv.style.top = (pos.top + this.displayArea.offsetHeight + this.popupMenuDiv.offsetHeight > this.topWindow.document.body.clientHeight && pos.top - this.popupMenuDiv.offsetHeight > 0 ? pos.top - this.popupMenuDiv.offsetHeight : pos.top + this.displayArea.offsetHeight) + "px";
	}
	this.popupMenuDiv.style.visibility = "visible";
};
PopupMenu.prototype._createMenu = function(isSubMenu, menuItems) { 
	var popupMenuDiv = this.topWindow.document.getElementById((isSubMenu ? "popupSubMenu" : "popupMenu") + "Div");
	if(!popupMenuDiv) {
		popupMenuDiv = this.topWindow.document.createElement("div");
		popupMenuDiv.style.position = "absolute";
		popupMenuDiv.style.display = "none";
		popupMenuDiv.style.visibility = "hidden";
		popupMenuDiv.id = (isSubMenu ? "popupSubMenu" : "popupMenu") + "Div";
		popupMenuDiv.style.height = "0px";
		this.topWindow.document.body.insertBefore(popupMenuDiv, this.topWindow.document.body.childNodes[0]);
		popupMenuDiv.innerHTML = '<iframe name="' + (isSubMenu ? "popupSubMenu" : "popupMenu") + 'Frame" style="z-index:-1" frameborder="0" scrolling="no" allowTransparency="true"></iframe>';
	}
	popupMenuDiv.style.zIndex = DomUtils.getMaxZIndex(this.topWindow.document.body) + 1;
	
	var popupMenuFrame = this.topWindow.frames[(isSubMenu ? "popupSubMenu" : "popupMenu") + "Frame"];
	var doc = popupMenuFrame.document;
	doc.open();
	doc.write('<html><body style="margin:0px"></body></html>');
	doc.close();
	
	popupMenuDiv.style.display = "";
	
	CssUtils.cloneStyle(this.window.document, doc);
	var table = doc.createElement("table");
	doc.body.appendChild(table);
	table.style.cssText = "outline-style:none; -moz-outline:none;";
	table.className = "menubar";
	table.border = 0;
	table.cellPadding = 0;
	table.cellSpacing = 0;
	table.width = this.menuWidth + "px";
	var popupMenu = this;
	for(var i=0; i<menuItems.length; i++) {
		var td = table.insertRow(-1).insertCell(-1);
		td.className = "menunormal";
		td.id = menuItems[i].id;
		td.innerHTML = menuItems[i].title;
		td.noWrap = true;
		td.onmouseover = function() {
			popupMenu._mouseOverMenuItem(menuItems[this.parentNode.rowIndex], this, isSubMenu);
		};
		td.onmouseout = function() {
			popupMenu._mouseOutMenuItem(menuItems[this.parentNode.rowIndex], this, isSubMenu);
		};
		td.onmousedown = function() {
			popupMenu._clickMenuItem(menuItems[this.parentNode.rowIndex], this, isSubMenu);
		};
	}
	popupMenuDiv.style.width = popupMenuDiv.childNodes[0].style.width = table.offsetWidth + "px";
	popupMenuDiv.style.height = popupMenuDiv.childNodes[0].style.height = table.offsetHeight + "px";
	return popupMenuDiv;
};
PopupMenu.prototype._hide = function() {
	if(this.popupSubMenuDiv) {
		this.popupSubMenuDiv.parentNode.removeChild(this.popupSubMenuDiv);
	}
	this.popupMenuDiv.parentNode.removeChild(this.popupMenuDiv);
	PageUtils.destoryCover(this.topWindow, this.cover);
};
PopupMenu.prototype._hideSubMenu = function() { 
	if(!this.popupSubMenuDiv || this.popupSubMenuDiv.style.display=="none") {
		return;
	}
	this.popupSubMenuDiv.style.display = "none";
	this.popupSubMenuDiv.style.visibility = "hidden";
	this.parentMenuItemElement.className = "menunormal";
	this.parentMenuItemElement = null;
};
PopupMenu.prototype._mouseOverMenuItem = function(menuItemDefine, menuItemElement, isSubMenu) {
	menuItemElement.className = "menuover";
	if(!menuItemDefine.subItems || menuItemDefine.subItems.length==0) { 
		if(!isSubMenu) {
			this._hideSubMenu();
		}
	}
	else { 
		this.popupSubMenuDiv = this._createMenu(true, menuItemDefine.subItems);
		var pos = DomUtils.getAbsolutePosition(menuItemElement, null, true);
		this.popupSubMenuDiv.style.top = (pos.top - 1) + "px";
		this.popupSubMenuDiv.style.left = (this.align=="left" ? pos.left + menuItemElement.offsetWidth : pos.left - this.popupSubMenuDiv.offsetWidth) + "px";
		this.popupSubMenuDiv.style.visibility = "visible";
		this.parentMenuItemElement = menuItemElement;
	}
};
PopupMenu.prototype._mouseOutMenuItem = function(menuItemDefine, menuItemElement, isSubMenu) {
	if(this.parentMenuItemElement!=menuItemElement) {
		menuItemElement.className = "menunormal";
	}
};
PopupMenu.prototype._clickMenuItem = function(menuItemDefine, menuItemElement, isSubMenu) {
	if(!menuItemDefine.subItems || menuItemDefine.subItems.length==0) { 
		this._hide();
		var func = this.onClick;
		window.setTimeout(function() {
			func.call(null, menuItemDefine.id, menuItemDefine.title);
		}, 10);
	}
};

PageUtils = function() {
};
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
PageUtils._newWorkflowInstnace = function(recordUrl, workflowId, openFeatues) {
   PageUtils.openurl(recordUrl + (recordUrl.indexOf('?')==-1 ? "?" : "&") + "workflowId=" + workflowId.split(".")[0] + "&activityId=" + workflowId.split(".")[1] + "&seq=" + Math.random(), openFeatues, recordUrl.replace(new RegExp('[=/\\?\\.&]', 'g'), ""));
};
PageUtils.newrecord = function(applicationName, formName, openFeatues, param) {
   PageUtils.openurl(RequestUtils.getContextPath() + "/" + applicationName + "/" + formName + ".shtml?act=create" + (param ? "&" + param : "") + "&seq=" + Math.random(), openFeatues, applicationName + formName);
};
PageUtils.openrecord = function(applicationName, formName, id, openFeatues, param) {
   PageUtils.openurl(RequestUtils.getContextPath() + "/" + applicationName + "/" + formName + ".shtml?act=open&id=" + id + "&seq=" + Math.random(), openFeatues, id);
};
PageUtils.editrecord = function(applicationName, formName, id, openFeatues, workItemId, param) {
   PageUtils.openurl(RequestUtils.getContextPath() + "/" + applicationName + "/" + formName + ".shtml?act=edit" + (param ? "&" + param : "") +  "&id=" + id + (workItemId && workItemId>0 ? "&workItemId=" + workItemId : "") + "&seq=" + Math.random(), openFeatues, id);
};
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
PageUtils.sendMail = function(mailAddress, name) {
	PageUtils.openurl(RequestUtils.getContextPath() + "/webmail/mail.shtml?mailTo=" + StringUtils.utf8Encode(name ? "\"" + name + "\" &lt;" + mailAddress + "&gt;" : mailAddress), "width=760,height=520", "mail");
};
PageUtils._getOpenFeatue = function(openFeatues, name) {
	var index = openFeatues.indexOf(name + "=");
	if(index==-1) {
		return null;
	}
	index += name.length + 1;
	var indexNext = openFeatues.indexOf(",", index);
	return indexNext==-1 ? openFeatues.substring(index) : openFeatues.substring(index, indexNext);
};
PageUtils.showToast = function(message, showSecnonds) {
	if(!window.toast) {
		window.toastCover = PageUtils.createCover(window, 0, true);
		window.toast = document.createElement("div");
		window.toast.style.cssText = "float:right; border:#aaaaaa 1px solid; background:#ffff66; padding:3px; margin-top:2px; margin-right:2px; font-family:\u5B8B\u4F53; font-size:12px";
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
PageUtils.getTopWindow = function() { 
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
PageUtils.createCover = function(parentWindow, opacity, scrollBarEnable) {
	var cover = parentWindow.document.createElement("div");
	cover.style.position = 'absolute';
	cover.id = 'cover';
	
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
	
	if(!scrollBarEnable && !parentWindow.scrollBarHidden) {
		try {
			parentWindow.scrollBarHidden = parentWindow.coverLevel;
			var clientWidth = parentWindow.document.body.clientWidth;
			
			cover.savedOverflowX = parentWindow.document.body.style.overflowX;
			cover.savedOverflowY = parentWindow.document.body.style.overflowY;
			cover.savedDocumentElementOverflowX = parentWindow.document.documentElement.style.overflowX;
			cover.savedDocumentElementOverflowY = parentWindow.document.documentElement.style.overflowY;
			cover.savedMarginRight = parentWindow.document.body.style.marginRight;
			
			parentWindow.document.body.style.overflowX = 'hidden';
			parentWindow.document.body.style.overflowY = 'hidden';
			parentWindow.document.documentElement.style.overflowX = 'hidden';
			parentWindow.document.documentElement.style.overflowY = 'hidden';
			
			if(parentWindow.document.body.clientWidth!=clientWidth) { 
				
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
	
	cover.style.zIndex = DomUtils.getMaxZIndex(parentWindow.document.body) + 1;
	
	var adjustCoverSize = function()  {
		cover.style.left = DomUtils.getScrollLeft(parentWindow.document) + 'px';
		cover.style.top = DomUtils.getScrollTop(parentWindow.document) + 'px';
		cover.style.width = coverFrame.style.width = maskDiv.style.width = DomUtils.getClientWidth(parentWindow.document) + 'px';
		cover.style.height = coverFrame.style.height = maskDiv.style.height = DomUtils.getClientHeight(parentWindow.document) + 'px';
	};
	adjustCoverSize.call(null);
	
	cover.adjustCoverSize = adjustCoverSize;
	EventUtils.addEvent(parentWindow, 'resize', adjustCoverSize);
	EventUtils.addEvent(parentWindow, 'scroll', adjustCoverSize);
	return cover;
};
PageUtils.destoryCover = function(parentWindow, cover) {
	
	EventUtils.removeEvent(parentWindow, 'resize', cover.adjustCoverSize);
	EventUtils.removeEvent(parentWindow, 'scroll', cover.adjustCoverSize);
	cover.adjustCoverSize = null;
	if(parentWindow.coverLevel==parentWindow.scrollBarHidden) { 
		parentWindow.scrollBarHidden = null;
		
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
PageUtils.requestFullScreen = function() { 
	var documentElement = document.documentElement; 
	if(documentElement.requestFullscreen) { 
		documentElement.requestFullscreen();
	}
	else if(documentElement.mozRequestFullScreen) { 
		documentElement.mozRequestFullScreen();
	}
	else if(documentElement.webkitRequestFullScreen) { 
		documentElement.webkitRequestFullScreen();
	}
	else if(documentElement.msRequestFullscreen) { 
		documentElement.msRequestFullscreen();
	}
};
PageUtils.exitFullScreen = function() { 
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
PageUtils.isFullScreen = function() { 
	if(document.exitFullscreen) { 
		return document.fullscreenElement || document.fullscreen; 
	}
	else if(document.mozCancelFullScreen) { 
		return document.mozFullScreen; 
	}
	else if(document.webkitCancelFullScreen) { 
		return document.webkitIsFullScreen; 
	}
	else if(document.msExitFullscreen) { 
		return document.msFullscreenElement; 
	}
};

RequestUtils = function() {
};
RequestUtils.contextPath = null;
RequestUtils.getContextPath = function() { 
	if(RequestUtils.contextPath!=null) {
		return RequestUtils.contextPath;	
	}
	var scripts = document.getElementsByTagName("script");
	for(var i=0; i<scripts.length; i++) {
		if(scripts[i].src.indexOf("common.js")==-1) {
			continue;
		}
		var url = RequestUtils._retrieveUrl(scripts[i].src);
		var endIndex = url.indexOf("/jeaf/common/js/common.js");
		if(endIndex==-1) {
			continue;
		}
		var beginIndex = url.indexOf("://");
		beginIndex = (beginIndex==-1 ? 0 : url.indexOf("/", beginIndex + 3));
		RequestUtils.contextPath = url.substring(beginIndex, endIndex);
	}
	return RequestUtils.contextPath;
};
RequestUtils._retrieveUrl = function(url) { //\u91CD\u7F6EURL,\u5220\u6389".."
	if(url.indexOf("://")==-1 && url.substring(0,1)!='/') {
		url = location.pathname.substring(0, location.pathname.lastIndexOf('/') + 1) + url;
	}
	var values = url.split("/");
	url = values[values.length-1];
	var skip = 0;
	for(var j=values.length-2; j>=0; j--) {
		if(values[j]=="..") {
			skip++;
		}
		else if(skip==0 || (skip--)==0) {
			url = values[j] + "/" + url;
		}
	}
	return url;
};
RequestUtils.getFullURL = function(url) { 
	if(url.indexOf('://')!=-1) {
		return url;
	}
	var href = location.href;
	var index = href.indexOf('/', href.indexOf('://') + 3);
	var prefix = (index==-1 ? href : href.substring(0, index));
	if(url.substring(0, 1)=='/') {
		return prefix + url;
	}
	else {
		index = location.pathname.lastIndexOf('/');
		return prefix + (index==-1 ? '' : location.pathname.substring(0, index + 1)) + url;
	}
};

ScriptUtils = function() {
};
ScriptUtils.appendJsFile = function(document, jsFile, scriptId, onLoadCallback) {
	var head = document.getElementsByTagName("head")[0];
	var scripts = head.getElementsByTagName("script");
	for(var i=0; i < (scripts ? scripts.length : 0); i++) {
		if(!scripts[i].src) {
			continue;
		}
		if(scripts[i].src.indexOf(jsFile) == scripts[i].src.length - jsFile.length || (scripts[i].src && jsFile.substring(jsFile.length - 3, jsFile.length)==".js" && scripts[i].src.indexOf(jsFile)!=-1)) {
			if(onLoadCallback) {
				onLoadCallback.call(null);
			}
			return;
		}
	}
	if(document!=window.top.document && jsFile.substring(jsFile.length - 3, jsFile.length)==".js") { 
		var scripts = window.top.document.getElementsByTagName('script');
		for(var i=0; i < (scripts ? scripts.length : 0); i++) {
			if(scripts[i].src && scripts[i].src.indexOf(jsFile)!=-1) {
				jsFile = scripts[i].src;
				break;
			}
		}
	}
	if(scriptId) {
		var script = document.getElementById(scriptId);
	   	if(script) {
	   	 	script.parentNode.removeChild(script);
	   	}
   	}
	var script = document.createElement("script");
	if(scriptId) {
		script.id = scriptId;
	}
	script.src = jsFile;
	script.language = 'JavaScript';
	if(onLoadCallback) {
		script.onload = script.onreadystatechange = script.onerror = function() {  
			if(script.readyState && script.readyState != 'loaded' && script.readyState != 'complete') {
				return;
			}
			script.onreadystatechange = script.onload = null;
			onLoadCallback.call(null);
		}
	}
	head.appendChild(script);
};

StringUtils = function() {
};
StringUtils.formatBytes =  function(bytes) {
	if(bytes>1000000000) {
		return (Math.round(bytes/10000000) / 100.0) + 'GB';
	}
	else if(bytes>1000000) {
		return (Math.round(bytes/10000) / 100.0) + 'MB';
	}
	if(bytes>1000) {
		return (Math.round(bytes/10)/100.0) + 'KB';
	}
	return Math.round(bytes) + "B";
};
StringUtils.formatSeconds = function(seconds) {
	var hours = Math.floor(seconds / 3600);
	var minutes = Math.floor(seconds % 3600 / 60);
	seconds = Math.floor(seconds % 60);
	return (hours>0 ? hours + '\u5C0F\u65F6' : '') + (minutes>0 ? minutes + '\u5206' : '') + seconds + '\u79D2';
};
StringUtils.getMoneyCapital = function(money) {
	money = new Number(money);
	if(isNaN(money) || money==0 || money>999999999999.99) {
        return "";
    }
    var nums = "\u96F6,\u58F9,\u8D30,\u53C1,\u8086,\u4F0D,\u9646,\u67D2,\u634C,\u7396,\u62FE,\u4F70,\u4EDF,\u842C,\u4EBF".split(",");
    var capital = "\u5143";
    money = Math.round(money*100);
    if(money % 100 ==0) {
        capital += "\u6574";
    }
    else {
        capital += nums[Math.floor(money % 100 / 10)] + "\u89D2";
        capital += nums[money % 10] + "\u5206";
    }
    money = Math.floor(money/100);
    var i = 0;
    do {
        if(i%4==0) {
        	capital = (i==0 ? "" : nums[12 + i/4]) + capital;
        }
        else {
        	capital = nums[9 + i%4] + capital;
        }
        i++;
        capital = '<font style="text-decoration:underline">&nbsp;' + nums[money%10] + '&nbsp;</font>' + capital;
        money = Math.floor(money/10);
    }while(money>0);
    return capital;
};
StringUtils.encodePropertyValue = function(propertyValue) {
	return !propertyValue ? propertyValue : propertyValue.replace(new RegExp("%", "g"), "%25").replace(new RegExp("&", "g"), "%26").replace(new RegExp("=", "g"), "%3D");
};
StringUtils.getPropertyValue = function(properties, propertyName, defaultValue) {
	if(!properties || properties=='') {
		return defaultValue ? defaultValue : "";
	}
	var index = properties.indexOf(propertyName + "=");
	if(index==-1) {
		return defaultValue ? defaultValue : "";
	}
	index += propertyName.length + 1;
	var indexNext = properties.indexOf("&", index);
	var propertyValue = (indexNext==-1 ? properties.substring(index) : properties.substring(index, indexNext));
	return propertyValue.replace(new RegExp("%26", "g"), "&").replace(new RegExp("%3D", "g"), "=").replace(new RegExp("%25", "g"), "%");
};
StringUtils.trim = function(str) {
	return str.replace(/(^\s*)|(\s*$)/g, '');
};
StringUtils.ltrim = function(str) {
	return str.replace(/^\s*/g,'');
};
StringUtils.rtrim = function(str) {
	return str.replace(/\s*$/g,'');
};
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
String.prototype.leftTrim = function() {
	return this.replace(/(^\s*)/g, "");
};
String.prototype.rightTrim = function() {
	return this.replace(/(\s*$)/g, "");
};
StringUtils.utf8Encode = function(text) {
	var s = escape(text).replace('+', '%2b');
	var sa = s.split("%");
    var retV ="";
    if(sa[0] != "") {
       retV = sa[0];
    }
    for(var i = 1; i < sa.length; i++) {
         if(sa[i].substring(0,1) == "u") {
             retV += StringUtils.hex2Utf8(StringUtils.str2Hex(sa[i].substring(1,5))) + sa[i].substring(5);
         }
         else {
         	retV += "%" + sa[i];
         }
    }
    return retV;
};
StringUtils.str2Hex = function(s) {
    var c = "";
    var n;
    var ss = "0123456789ABCDEF";
    var digS = "";
    for(var i = 0; i < s.length; i ++) {
       c = s.charAt(i);
       n = ss.indexOf(c);
       digS += StringUtils.dec2Dig(eval(n));
    }
    return digS;
};
StringUtils.dec2Dig = function(n1) {
    var s = "";
    var n2 = 0;
    for(var i = 0; i < 4; i++) {
		n2 = Math.pow(2,3 - i);
		if(n1 >= n2) {
			s += '1';
			n1 = n1 - n2;
       	}
       	else {
			s += '0';
       	}
    }
    return s;
};
StringUtils.dig2Dec = function(s) {
    var retV = 0;
    if(s.length == 4) {
        for(var i = 0; i < 4; i ++) {
            retV += eval(s.charAt(i)) * Math.pow(2, 3 - i);
        }
        return retV;
    }
    return -1;
};
StringUtils.hex2Utf8 = function(s) {
   var retS = "";
   var tempS = "";
   var ss = "";
   if(s.length == 16) {
       tempS = "1110" + s.substring(0, 4);
       tempS += "10" +  s.substring(4, 10); 
       tempS += "10" + s.substring(10,16); 
       var sss = "0123456789ABCDEF";
       for(var i = 0; i < 3; i ++) {
          retS += "%";
          ss = tempS.substring(i * 8, (eval(i)+1)*8);
          retS += sss.charAt(StringUtils.dig2Dec(ss.substring(0,4)));
          retS += sss.charAt(StringUtils.dig2Dec(ss.substring(4,8)));
       }
       return retS;
   }
   return "";
};
StringUtils.utf8Decode = function(szInput) {
	var x,wch,wch1,wch2,uch="",szRet="";
	for (x=0; x<szInput.length; x++) {
		if (szInput.charAt(x)=="%") {
			wch =parseInt(szInput.charAt(++x) + szInput.charAt(++x),16);
			if (!wch) {
				break;
			}
			if (!(wch & 0x80)) {
				wch = wch;
			}
			else if (!(wch & 0x20)) {
				x++;
				wch1 = parseInt(szInput.charAt(++x) + szInput.charAt(++x),16);
				wch = (wch & 0x1F)<< 6;
				wch1 = wch1 & 0x3F;
				wch = wch + wch1;
			}
			else {
				x++;
				wch1 = parseInt(szInput.charAt(++x) + szInput.charAt(++x),16);
				x++;
				wch2 = parseInt(szInput.charAt(++x) + szInput.charAt(++x),16);
				wch = (wch & 0x0F)<< 12;
				wch1 = (wch1 & 0x3F)<< 6;
				wch2 = (wch2 & 0x3F);
				wch = wch + wch1 + wch2;
			}
			szRet += String.fromCharCode(wch);
		}
		else {
			szRet += szInput.charAt(x);
		}
	}
	return szRet;
};
StringUtils.removeQueryParameter = function(queryString, parameterNames) {
	if(!queryString || queryString=="") {
		return queryString;
	}
	var names = parameterNames.split(",");
	for(var i=0; i<names.length; i++) {
		var beginIndex = queryString.indexOf(names[i] + "=");
		if(beginIndex!=-1) {
			var endIndex = queryString.indexOf('&', beginIndex);
			queryString = (endIndex==-1 ? queryString.substring(0, beginIndex) : queryString.substring(0, beginIndex) + queryString.substring(endIndex + 1));
		}
	}
	if(queryString!="") {
		var last = queryString.substring(queryString.length-1);
		if(last=="&" || last=="?") {
			queryString = queryString.substring(0, queryString.length-1);
		}
	}
	return queryString;
};
StringUtils.setQueryParameter = function(queryString, parameterName, parameterValue) {
	queryString = StringUtils.removeQueryParameter(queryString, parameterName);
	if(!queryString) {
		queryString = "";
	}
	return queryString + (queryString=="" ? "" : "&") + parameterName + "=" + StringUtils.utf8Encode(parameterValue);
};
StringUtils.decToHex = function(dec) {
	var hexa="0123456789ABCDEF"; 
	var hex=""; 
	while(dec>15) { 
		tmp=dec-(Math.floor(dec/16))*16; 
		hex=hexa.charAt(tmp)+hex; 
		dec=Math.floor(dec/16); 
	} 
	hex = hexa.charAt(dec)+hex;
	return(hex);
};
StringUtils.generateHtmlContent = function(text) {
	return text.replace(/&/g, "&amp;")
			   .replace(/ /g, "&nbsp;")
			   .replace(/</g, "&lt;")
			   .replace(/>/g, "&gt;")
			   .replace(/"/g, "&quot;")
			   .replace(/\r/g, "")
			   .replace(/\n/g, "<br/>")
			   .replace(/\u201C/g, "&ldquo;")
			   .replace(/\u201D/g, "&rdquo;")
			   .replace(/\u00B7/g, "&middot;")
			   .replace(/\uFFE0/g, "&cent;")
			   .replace(/\u00A3/g, "&pound;")
			   .replace(/\u00A5/g, "&yen;")
			   .replace(/\u00A7/g, "&sect;")
			   .replace(/\u00A9/g, "&copy;")
			   .replace(/\u00AE/g, "&reg;")
			   .replace(/\u00D7/g, "&times;")
			   .replace(/\u00F7/g, "&divide;")
			   .replace(/\u00AB/g, "&laquo;")
			   .replace(/\u00BB/g, "&raquo;")
			   .replace(/\u00B1/g, "&plusmn;")
			   .replace(/\u00B0/g, "&deg;")
			   .replace(/\u2032/g, "&prime;");
};
StringUtils.filterEscapeCharacter = function(text) {
	return text.replace(/&amp;/g, "&")
			   .replace(/&nbsp;/g, " ")
			   .replace(/&lt;/g, "<")
			   .replace(/&gt;/g, ">")
			   .replace(/&quot;/g, '"')
			   .replace(/&ldquo;/g, "\u201C")
			   .replace(/&rdquo;/g, "\u201D")
			   .replace(/&middot;/g, "\u00B7")
			   .replace(/&cent;/g, "\uFFE0")
			   .replace(/&pound;/g, "\u00A3")
			   .replace(/&yen;/g, "\u00A5")
			   .replace(/&sect;/g, "\u00A7")
			   .replace(/&copy;/g, "\u00A9")
			   .replace(/&reg;/g, "\u00AE")
			   .replace(/&times;/g, "\u00D7")
			   .replace(/&divide;/g, "\u00F7")
			   .replace(/&laquo;/g, "\u00AB")
			   .replace(/&raquo;/g, "\u00BB")
			   .replace(/&plusmn;/g, "\u00B1")
			   .replace(/&deg;/g, "\u00B0")
			   .replace(/&prime;/g, "\u2032");
};

Date.prototype.format = function(fmt) {
    var o = {
        "M+": this.getMonth() + 1, 
        "d+": this.getDate(), 
        "H+": this.getHours(), 
        "h+": (this.getHours()%12==0 ? 12 : this.getHours()%12), 
        "m+": this.getMinutes(), 
        "s+": this.getSeconds(), 
        "q+": Math.floor((this.getMonth() + 3) / 3), 
        "S": this.getMilliseconds(), 
        "t+": (this.getHours()<12 ? 'AM' : 'PM'),
        "T+": (this.getHours()<12 ? '\u4E0A\u5348' : '\u4E0B\u5348')
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

TabList = function(tabs) {
	this.tabs = tabs;
	if(!window.tabLists) {
		window.tabLists = [];
	}
	window.tabLists.push(this);
	this.id = 'tabList_' + window.tabLists.length;
	this._writeTabList();
};
TabList.prototype.onTabSelected = function(tabId) { 
	
};
TabList.prototype._writeTabList = function() {
	document.write('<div id="' + this.id + '" onselectstart="return false;" style="position:relative;"><span class="tabBarLeft"></span></div>');
	document.write('<div class="tabBarBottomLine"></div>');
	var tabList = this;
	var tabListDiv = document.getElementById(this.id);
	for(var i = 0; i < this.tabs.length; i++) {
		var tab = document.createElement('span');
		tab.id = this.tabs[i].id;
		tab.className = 'tab';
		tab.onclick = function() {
			tabList.selectTab(this.id);
		};
		this.tabs[i].element = tab;
		tab.innerHTML = this.tabs[i].name;
		tabListDiv.appendChild(tab);
		tabListDiv.appendChild(document.createElement('span')).className = 'tabSeparation';
		if(this.tabs[i].selected) {
			this.selectTab(this.tabs[i].id);
		}
	}
	window.setTimeout(function() {
		tabList.onTabSelected.call(null, tabList.getSelectedTabId());
	}, 300);
};
TabList.prototype.selectTab = function(tabId) { 
	for(var i = 0; i < this.tabs.length; i++) {
		if(this.tabs[i].id==tabId) {
			this.tabs[i].selected = true;
		}
		else if(this.tabs[i].selected) {
			this.tabs[i].selected = false;
		}
		else {
			continue;
		}
		this.tabs[i].element.className = this.tabs[i].selected ? "tab tabSelected" : "tab";
		if(this.tabs[i].selected) {
//alert(CssUtils.getElementComputedStyle(this.tabs[i].element, 'border-bottom-color'));
		}
		var tabContent = document.getElementById("tab" + this.tabs[i].id);
		if(tabContent) {
			tabContent.style.display = this.tabs[i].selected ? "" : "none";
		}
		if(this.tabs[i].selected) {
			this.onTabSelected.call(null, this.tabs[i].id);
		}
	}
};
TabList.prototype.getSelectedTabId = function() { 
	var tab = ListUtils.findObjectByProperty(this.tabs, 'selected', true);
	return tab ? tab.id : null;
};

Timer = function() {
	
};
Timer.prototype.schedule = function(task, delay, period) { 
	this.task = task;
	this.period = period;
	this.canceled = false;
	this._processTask(delay);
};
Timer.prototype._processTask = function(delay) { 
	var timer = this;
	this.timeout = window.setTimeout(function() {
		var time = new Date().valueOf();
		timer.t = time;
		timer.task.call(null);
		if(timer.canceled || !timer.period || timer.period<=0) {
			return;
		}
		timer._processTask(timer.period - (new Date().valueOf() - time));
	}, Math.max(0, delay));
};
Timer.prototype.cancel = function() { 
	if(this.timeout) {
		window.clearTimeout(this.timeout);
	}
	this.canceled = true;
};

