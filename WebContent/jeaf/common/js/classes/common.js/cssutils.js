CssUtils = function() {

};
//引入CSS
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

//引入样式
CssUtils._appendStyle = function(doc, styleId, styleSheet) {
    //复制rule
    var css;
    try {
    	css = styleId && styleId!="" ? doc.styleSheets[styleId] : doc.styleSheets[0];
    }
    catch(e) {
    
    }
	if(!css) {
		//获取head
		var head = DomUtils.getHead(doc);
		//创建style
	    var style = doc.createElement("style");
	    style.type = "text/css";
	    style.id = styleId;
	    head.appendChild(style);
	    css = styleId && styleId!="" ? doc.styleSheets[styleId] : doc.styleSheets[0];
    }
    //复制rule
 	for(var i=0; i<styleSheet.rules.length; i++) {
		var cssText = styleSheet.rules[i].style.cssText;
		if(!cssText || cssText=="") {
			continue;
		}
		try {
			if(css.cssRules) { //firefox
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

//复制样式
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
		if("link"==ownerNode.tagName.toLowerCase()) { //<link>
			CssUtils.appendCssFile(toDocument, "", ownerNode.href); //引入CSS
		}
    }
};

//获取实际样式
CssUtils.getElementComputedStyle = function(element, styleName) {
	if(!element) {
		return null;
	}
	var style;
	if(!element.currentStyle){ //w3c
		style = element.ownerDocument.defaultView.getComputedStyle(element, null).getPropertyValue(styleName);
	}
	else if(!(style=element.currentStyle[styleName])) { //ie
		var styleNames = styleName.split('-');
		styleName = styleNames[0];
		for(var i=1; i<styleNames.length; i++) {
			styleName += styleNames[i].substring(0, 1).toUpperCase() + styleNames[i].substring(1);
		}
		eval('style=element.currentStyle.' + styleName + ';');
	}
	return style.replace(/['"]/g, '');
};
//从样式文本解析样式
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
//按样式名称获取样式
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
//按样式名称获取样式
CssUtils.getStyleByName = function(document, rullName, styleName) {
	var rule = CssUtils._getStyleRuleByName(document, rullName);
	if(rule) {
		return eval("rule.style." + styleName);
	}
};
//变灰
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
//取消变灰
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
//设置角度
CssUtils.setDegree = function(element, degree) {
	degree = (degree < 0 ? 360 : 0) + degree % 360;
	element.style.cssText += '-moz-transform: rotate(' + degree + 'deg);' +
	    					 '-o-transform: rotate(' + degree + 'deg);' +
	   						 '-webkit-transform: rotate(' + degree + 'deg);' +
	    					 'transform: rotate(' + degree + 'deg);';
};
//旋转,当targetDegree!=-1时,旋转到指定的度数后停止转动
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
//停止旋转
CssUtils.stopRotate = function(element) {
	if(element.rotateTimer && element.rotateTimer!=0) {
		window.clearInterval(element.rotateTimer);
		element.rotateTimer = 0;
		CssUtils.setDegree(element, 0);
	}
};
//设置透明度
CssUtils.setOpacity = function(element, opacity) {
	opacity = Math.max(0, opacity);
	element.style.cssText += 'filter: alpha(opacity=' + (opacity*100) + '); opacity: ' + opacity + ';'
};