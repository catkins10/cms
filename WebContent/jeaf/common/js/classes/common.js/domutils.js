DomUtils = function() {

};
//创建ActiveXObject
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
//获取HTML元素
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
	//遍历页面元素
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
	//遍历页面元素
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
	//if(element.offsetParent) {
	//	return element.offsetParent;
	//}
	var parent = element.parentNode;
	for(; ",BODY,TD,TABLE,DIV,SPAN,A,UL,LI,PRE,".indexOf("," + parent.tagName + ",")==-1; parent = parent.parentNode);
	return parent;
};
DomUtils.insertAfter = function(newNode, refNode) { //在节点后面添加节点
	var nextSibling = refNode.nextSibling;
	if(nextSibling) {
		refNode.parentNode.insertBefore(newNode, nextSibling);
	}
	else {
		refNode.parentNode.appendChild(newNode);
	}
};
DomUtils.containsNode = function(parentNode, childNode) { //判断parentNode是否包含childNode
	if(parentNode==childNode) {
		return true;
	}
	var childNodes = parentNode.childNodes;
	for(var i=0; i<childNodes.length; i++) {
		if(childNodes[i]==childNode) {
			return true;
		}
		if(DomUtils.containsNode(childNodes[i], childNode)) { //递归
			return true;
		}
	}
	return false;
};
DomUtils.getParentNode = function(node, parentNodeNames, containerNode) { //移动到指定类型的元素
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
DomUtils.traversalChildElements = function(parentElement, callback) { //遍历页面元素,callback=function(element),返回true时不遍历子元素
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
		if(DomUtils.traversalChildElements(childNodes[i], callback)) { //递归调用
			return true;
		}
	}
	return false;
};
DomUtils.getAbsolutePosition = function(obj, parentObj, haveIframe) { //获取对象的绝对位置
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
			if(document.all && (doctype=document.documentElement.previousSibling) && doctype.nodeType==8 && doctype.data.indexOf('W3C')!=-1) { //ie,w3c
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

DomUtils.getMaxZIndex = function(parentElement) { //获取最大的zIndex
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

//获取META的内容
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

//按名称获取META
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

DomUtils.setElementHTML = function(element, html, inner) { //设置页面元素的HTML
	//复制href、src属性值到_savedurl
	html = html.replace(/<a(?=\s).*?\shref=((?:(?:\s*)("|').*?\2)|(?:[^"'][^ >]+))/gi,'$& _savedurl=$1');
	html = html.replace(/<img(?=\s).*?\ssrc=((?:(?:\s*)("|').*?\2)|(?:[^"'][^ >]+))/gi,'$& _savedurl=$1');
	html = html.replace(/<area(?=\s).*?\shref=((?:(?:\s*)("|').*?\2)|(?:[^"'][^ >]+))/gi,'$& _savedurl=$1');
	var parentElement;
	//设置HTML
	if(inner) {
		parentElement = element;
		element.innerHTML = html;
	}
	else  {
		parentElement = element.parentElement;
		element.outerHTML = html;
	}
	//还原href、src,并删除_savedurl
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

//给firefox增加outerHTML、canHaveChildren属性
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
//获取HEAD
DomUtils.getHead = function(doc) {
	//获取head
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
DomUtils.getClientWidth = function(doc) { //获取窗口宽度
	if(doc.body.clientWidth > 0 && doc.body.clientWidth < doc.body.scrollWidth) {
		return doc.body.clientWidth;
	}
	var clientWidth = doc.documentElement.clientWidth;
	return clientWidth==0 ? doc.body.clientWidth : clientWidth;
};
DomUtils.getClientHeight = function(doc) { //获取窗口高度
	if(doc.body.clientHeight > 0 && doc.body.clientHeight < doc.body.scrollHeight) {
		return doc.body.clientHeight;
	}
	var clientHeight = doc.documentElement.clientHeight;
	return clientHeight==0 ? doc.body.clientHeight : clientHeight;
};
DomUtils.getScrollTop = function(doc) { //获取窗口滚动条位置
	var scrollTop = doc.documentElement.scrollTop;
	if(scrollTop==0) {
		scrollTop = doc.body.scrollTop;
	}
	return scrollTop;
};
DomUtils.getScrollLeft = function(doc) { //获取窗口滚动条位置
	var scrollLeft = doc.documentElement.scrollLeft;
	if(scrollLeft==0) {
		scrollLeft = doc.body.scrollLeft;
	}
	return scrollLeft;
};
DomUtils.getSpacing = function(obj, place) { //获取对象间隙,place=['left', 'top', 'right', 'bottom']
	var margin = CssUtils.getElementComputedStyle(obj, 'margin-' + place);
	var padding = CssUtils.getElementComputedStyle(obj, 'padding-' + place);
	margin = !margin ? 0 : Number(margin.replace("px", ""));
	padding = !padding ? 0 : Number(padding.replace("px", ""));
	return (isNaN(margin) ? 0 : margin) + (isNaN(padding) ? 0 : padding);
};
DomUtils.getBorderWidth = function(obj, place) { //获取边框宽度,place=['left', 'top', 'right', 'bottom']
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
DomUtils.createElement = function(window, range, elementType) { //在指定区域创建HTML元素
	var id = ("" + Math.random()).substring(2);
	DomSelection.pasteHTML(window, range, '<' + elementType + ' id="' + id + '"></' + elementType + '>');
	var element = window.document.getElementById(id);
	if(element) {
		element.removeAttribute("id");
		return element;
	}
};
DomUtils.getWindowBookmark = function(window, noFocusPrompt) { //获取窗口当前状态,生成书签
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
	if(!document.all) { //不是IE
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
DomUtils.moveTableRow = function(table, fromRowIndex, toRowIndex) { //移动表格行
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