DomSelection = function() {

};
DomSelection.getSelection = function(window) { //获取选择区域
	if(window.getSelection) {
		return window.getSelection();
	}
	else {
		return window.document.selection;
	}
};
DomSelection.getRange = function(window) { //获取选中区域
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
DomSelection.getSelectedElement = function(range) { //获取选中的元素
	if(!range) {
		return null;
	}
	if(range.getBookmark) { //IE:TextRange
		return range.parentElement();
	}
	else if(range.select) { //IE:ControlRange
		return range.item(0);
	}
	else if(range.startContainer) { //W3C
		if(range.startContainer.nodeType==3) { //文本
			return range.startContainer.parentNode;
		}
		var element = range.startContainer.childNodes[range.startOffset];
		if(element && element.nodeType==1) { //1:Element
			return element;
		}
		return range.startContainer;
	}
};
DomSelection.getSelectText = function(window) { //获取选中的文本
	var range = DomSelection.getRange(window);
	if(!range) {
		return "";
	}
	if(range.getBookmark) { //IE:TextRange
		return range.text;
	}
	else if(range.toString) {
		return range.toString(); 
	}
};
DomSelection.getSelectHtmlText = function(window) { //获取选中的HTML文本
	var range = DomSelection.getRange(window);
	if(!range) {
		return "";
	}
	if(range.getBookmark) { //IE:TextRange
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
	//获取包含头尾节点的兄弟节点
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
DomSelection.selectRange = function(window, range) { //选中区域
	if(!range) {
		return;
	}
	if(range.select) {
		try { range.select();} catch(e) {}  //IE
	}
	else if(range.startContainer) { //W3C
		var selection = DomSelection.getSelection(window);
		window.focus();
		selection.removeAllRanges();
		selection.addRange(range);
	}
};
DomSelection.createRange = function(document, element) { //选取范围移动到指定元素的内部
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
DomSelection.getRangeBookmark = function(range) { //获取选取范围书签
	if(range.getBookmark) { //IE:TextRange
		return {bookmark: range.getBookmark()};
	}
	else if(range.select) { //IE:ControlRange
		return {control: DomSelection._getRangeAddress(range.item(0), 0)};
	}
	else if(range.startContainer) { //W3C
		return {start: DomSelection._getRangeAddress(range.startContainer, range.startOffset), end: DomSelection._getRangeAddress(range.endContainer, range.endOffset)};
	}
};
DomSelection.createRangeByBookmark = function(document, rangeBookmark) { //选取范围移动到书签所指区域
	try {
		var range;
		if(rangeBookmark.bookmark) { //IE:TextRange
			range = document.body.createTextRange();
			range.moveToBookmark(rangeBookmark.bookmark);
		}
		else if(rangeBookmark.control) { //IE:ControlRange
			range = document.body.createControlRange();
			range.add(DomSelection._getNodeByAddress(document, rangeBookmark.control.address));
		}
		else if(rangeBookmark.start) { //W3C
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
DomSelection._getRangeAddress = function(node, offset) { //获取节点地址
	//当前一个节点也是文本时,则使用前一个节点
	 while(node.nodeType==3) {
	 	if(!node.previousSibling || node.previousSibling.nodeType!=3) {
	 		break;
	 	}
 		node = node.previousSibling;
 		offset += node.length;
	 }
	 //获取节点序号
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
DomSelection._getNodeByAddress = function(document, address) { //根据地址获取节点
	var node = document.body;
	for(var i=address.length-1; i>=0; i--) {
		node = node.childNodes[address[i]];
	}
	return node;
};
DomSelection.pasteHTML = function(window, range, html) { //粘贴HTML
	DomSelection.selectRange(window, range);
	if(range && range.pasteHTML) {
		range.pasteHTML(html);
	}
	else if("" + window.document.queryCommandSupported('insertHTML')=="true") { //支持insertHTML命令
		window.document.execCommand('insertHTML', false, html);
	}
	else if(range.surroundContents) {
		var element = window.document.createElement("b");
        range.surroundContents(element);
        element.outerHTML = html;
	}
};
DomSelection.inRange = function(range, element) { //检查对象是否在range中
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