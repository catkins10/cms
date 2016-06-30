var CaptureRuleConfigure = function(pageType, pageURL) { //pageType: list/content
	this.pageType = pageType;
	this.pageFrameName = pageType + "PageFrame";
	this.htmlFrameName = pageType + "PageHtmlFrame";
	this.htmlFieldName =  pageType + "PageHtml";
	this.currentFieldName = null;
	var captureRuleConfigure = this;
	//输出页面
	var onPageLoad = function() {
		captureRuleConfigure.onPageLoad();
	};
	var pageFrame = document.getElementById(this.pageFrameName);
	var htmlFrame = document.getElementById(this.htmlFrameName);
	var adjustFrameSize = function() {
		pageFrame.style.height = htmlFrame.style.height = ((document.body.clientHeight - 130) / 2) + "px";
	};
	adjustFrameSize.call();
	EventUtils.addEvent(window, "load", adjustFrameSize);
	EventUtils.addEvent(window, "resize", adjustFrameSize);
	pageFrame.onload = onPageLoad;
	if(pageURL!="") {
		pageFrame.src = pageURL + (pageURL.indexOf('?')==-1 ? '?' : '&') + 'seq=' + (new Date()).getTime();
		window.setTimeout(onPageLoad, 3000);
	}
	//输出源文件
	var doc = frames[this.htmlFrameName].document;
	doc.open();
	doc.write('<html><head><style>*{font-size:12px; line-height: 16px; font-family: 宋体;}</style></head><body style="border-style: none; margin:5px;"><pre style="margin:0"></pre></body></html>');
	doc.close();
	var html = document.getElementsByName(this.htmlFieldName)[0].value;
	doc.body.childNodes[0].appendChild(doc.createTextNode(html));
};

//页面加载完成
CaptureRuleConfigure.prototype.onPageLoad = function() {
	try {
		var win = frames[this.pageFrameName];
		//创建焦点DIV
		if(!this.focusDiv) {
			this.focusDiv = win.document.createElement('div');
			this.focusDiv.style.position = 'absolute';
			this.focusDiv.style.border = "#bbb 1px solid";
			this.focusDiv.style.backgroundColor = "#ffffcc";
			this.focusDiv.style.filter = 'alpha(opacity=80);';
			this.focusDiv.style.opacity = 0.8;
			this.focusDiv.style.width = '0px';
			this.focusDiv.style.height = '0px';
			this.focusDiv.style.zIndex = -100000; 
			win.document.body.appendChild(this.focusDiv);
		}
		//事件处理
		var captureRuleConfigure = this;
		win.onerror = function() {
			return false;
		};
		win.document.body.onselectstart = function() {
			return false;
		};
		win.document.body.onmousedown = function() {
			return false;
		};
		win.document.body.onmouseup = function() {
			captureRuleConfigure.onMouseUp(win.event ? win.event : event);
			return false;
		};
		win.document.body.onclick = function() {
			return false;
		};
		win.document.body.onmousemove = function(event) {
			captureRuleConfigure.onMouseMove(win.event ? win.event : event);
		};
	}
	catch(e) {
	
	}
};
CaptureRuleConfigure.prototype.onMouseMove = function(event) {
	if(event.srcElement==this.focusElement || event.srcElement==this.focusDiv) {
		return;
	}
	this.focusElement = event.srcElement;
	var pos = DomUtils.getAbsolutePosition(this.focusElement, this.focusElement.ownerDocument.body, true);
	this.focusDiv.style.left = (pos.left - 2) + 'px';
	this.focusDiv.style.top = (pos.top - 2) + 'px';
	this.focusDiv.style.width = (this.focusElement.offsetWidth + 2) + 'px';
	this.focusDiv.style.height = (this.focusElement.offsetHeight + 2) + 'px';
};
CaptureRuleConfigure.prototype.onMouseUp = function(event) {
	//获取选中元素之前的所有元素的标记
	var tagNames = ['BODY'];
	DomUtils.traversalChildElements(event.srcElement.ownerDocument.body, function(element) { //遍历页面元素,callback=function(element),返回true时不遍历子元素
		tagNames.push(element.tagName);
		return element == event.srcElement;
	});
	
	//定位元素
	var pageHtml = document.getElementsByName(this.htmlFieldName)[0].value.toUpperCase(); 
	this.focusBegin = 0;
	for(var i = 0; i < tagNames.length; i++) {
		var tagIndex = pageHtml.indexOf('<' + tagNames[i], this.focusBegin);
		if(tagIndex==-1) {
			if(tagNames[i]=="TBODY") {
				continue;
			}
			return;
		}
		if(i==tagNames.length-1) {
			this.focusBegin = tagIndex;
			break;
		}
		tagIndex = pageHtml.indexOf('>', tagIndex + tagNames[i].length + 1);
		if(tagIndex==-1) {
			return;
		}
		this.focusBegin = tagIndex;
	}
	
	//获取元素的结束位置
	pageHtml = pageHtml.substring(this.focusBegin);
	var match = pageHtml.match(new RegExp(event.srcElement.outerHTML.replace(/<[^>]*>/g, '<[^>]*>'), "i"));
	this.focusEnd = this.focusBegin + (match ? match[0].length : pageHtml.indexOf('>') + 1);
	this.highLightSource(true);
};
CaptureRuleConfigure.prototype.highLightSource = function(scrollToFocus) {
	var iframe = document.getElementById(this.htmlFrameName);
	var win = frames[this.htmlFrameName];
	var doc = win.document;
	//获取当前滚动条位置
	var scrollLeft = doc.body.scrollLeft, scrollTop = doc.body.scrollTop;
	//清空源码框
	var pre = doc.body.childNodes[0];
	pre.innerHTML = "";

	var pageHtml = document.getElementsByName(this.htmlFieldName)[0].value;
	var beginIndex = 0;
	var table = document.getElementById(this.pageType + "FieldTable");
	for(var i=0; i<table.rows.length && table.rows[i]!=this.fieldRow; i++) {
		var fieldName = table.rows[i].id.substring((this.pageType + "Field_").length);
		var fieldBegin = document.getElementsByName(this.pageType + "Field_" + fieldName + "_begin")[0].value;
		if(fieldBegin=='') {
			continue;
		}
		var index = pageHtml.indexOf(fieldBegin, beginIndex);
		if(index==-1) {
			break;
		}
		beginIndex = index + fieldBegin.length;
	}
	var fieldBegin = document.getElementsByName(this.pageType + "FieldBegin")[0].value;
	var fieldEnd = document.getElementsByName(this.pageType + "FieldEnd")[0].value;
	var ranges = [{beginIndex: 0, endIndex: beginIndex, type:'normal'}];
	for(var i = 0; (fieldBegin!='' || fieldEnd!='') && i < 10; i++) {
		var endIndex = -1;
		if(fieldBegin!='') {
			endIndex = pageHtml.indexOf(fieldBegin, beginIndex);
			if(endIndex==-1) {
				break;
			}
			ranges.push({beginIndex: beginIndex, endIndex: endIndex, type:'normal'});
			beginIndex = endIndex + fieldBegin.length;
			ranges.push({beginIndex: endIndex, endIndex: beginIndex, type:'fieldBegin'});
		}
		if(fieldEnd!='') {
			endIndex = pageHtml.indexOf(fieldEnd, beginIndex);
			if(endIndex==-1) {
				break;
			}
			ranges.push({beginIndex: beginIndex, endIndex: endIndex, type: (fieldBegin=='' ? 'normal' : 'fieldValue')});
			beginIndex = endIndex + fieldEnd.length;
			ranges.push({beginIndex: endIndex, endIndex: beginIndex, type:'fieldEnd'});
		}
	}
	ranges.push({beginIndex: beginIndex, endIndex: pageHtml.length, type:'normal'});
	
	for(var i = 0; this.focusEnd && i < ranges.length; i++) {
		if(ranges[i].beginIndex >= this.focusBegin && ranges[i].endIndex <= this.focusEnd) { //整个都在选中范围内
			ranges[i].type += " focus";
		}
		else if(ranges[i].beginIndex <= this.focusBegin && ranges[i].endIndex >= this.focusEnd) { //包含了选中范围
			ranges.splice(i, 1, {beginIndex: ranges[i].beginIndex, endIndex: this.focusBegin, type: ranges[i].type}, {beginIndex: this.focusBegin, endIndex: this.focusEnd, type: ranges[i].type + ' focus'}, {beginIndex: this.focusEnd, endIndex: ranges[i].endIndex, type: ranges[i].type});
			i+= 2;
		}
		else if(ranges[i].beginIndex >= this.focusBegin && ranges[i].beginIndex <= this.focusEnd && ranges[i].endIndex >= this.focusEnd) { //头部在选中范围中
			ranges.splice(i, 1, {beginIndex: ranges[i].beginIndex, endIndex: this.focusEnd, type: ranges[i].type + ' focus'}, {beginIndex: this.focusEnd, endIndex: ranges[i].endIndex, type: ranges[i].type});
			i++;
		}
		else if(ranges[i].beginIndex <= this.focusBegin && ranges[i].endIndex >= this.focusBegin && ranges[i].endIndex <= this.focusEnd) { //尾部在选中范围中
			ranges.splice(i, 1, {beginIndex: ranges[i].beginIndex, endIndex: this.focusBegin, type: ranges[i].type}, {beginIndex: this.focusBegin, endIndex: ranges[i].endIndex, type: ranges[i].type + ' focus'});
			i++;
		}
	}
	var focusElement;
	for(var i = 0; i < ranges.length; i++) {
		if(ranges[i].beginIndex==ranges[i].endIndex) {
			continue;
		}
		var span = doc.createElement('span');
		var focus = ranges[i].type.indexOf(' focus')!=-1;
		var fieldBegin = ranges[i].type.indexOf('fieldBegin')!=-1;
		var fieldEnd = ranges[i].type.indexOf('fieldEnd')!=-1;
		var fieldValue = ranges[i].type.indexOf('fieldValue')!=-1;
		var normal = ranges[i].type.indexOf('normal')!=-1;
		if(!focusElement && ((!scrollToFocus && !normal) || (scrollToFocus && focus))) {
			focusElement = span;
		}
		span.style.color = fieldBegin || fieldEnd ? '#ffff88' : (fieldValue ? '#fff' : (focus ? '#0040ff' : '#000'));
		span.style.backgroundColor = !normal ? '#3399ff' : (focus ? '#ffff88' : '');
		span.appendChild(doc.createTextNode(pageHtml.substring(ranges[i].beginIndex, ranges[i].endIndex)));
		pre.appendChild(span);
	}
	if(focusElement) {
		win.scroll(scrollLeft <= focusElement.offsetLeft && scrollLeft + iframe.offsetWidth - 12 >= focusElement.offsetLeft + focusElement.offsetWidth ? scrollLeft : focusElement.offsetLeft - 50, scrollTop <= focusElement.offsetTop && scrollTop + iframe.offsetHeight - 12 >= focusElement.offsetTop + focusElement.offsetHeight ? scrollTop : focusElement.offsetTop - 10);
	}
};
//保存字段配置
CaptureRuleConfigure.prototype.saveFieldConfig = function() {
	if(this.currentFieldName==null) {
		return;
	}
	document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_begin")[0].value = document.getElementsByName(this.pageType + "FieldBegin")[0].value;
	document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_end")[0].value = document.getElementsByName(this.pageType + "FieldEnd")[0].value;
	document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_arraySeparator")[0].value = document.getElementsByName(this.pageType + "ArraySeparator")[0].value;
	document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_value")[0].value = document.getElementsByName(this.pageType + "FieldValue")[0].value;
	document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_format")[0].value = document.getElementsByName(this.pageType + "FieldFormat")[0].value;
};
//字段被选中
CaptureRuleConfigure.prototype.onSelectField = function(fieldRow) {
	this.fieldRow = fieldRow;
	var table = document.getElementById(this.pageType + "FieldTable");
	for(var i=0; i<table.rows.length; i++) {
		var cells = table.rows[i].cells;
		cells[0].style.backgroundColor = cells[1].style.backgroundColor = (table.rows[i]==fieldRow ? "#1050A0" : "transparent");
		cells[0].style.color = cells[1].style.color = (table.rows[i]==fieldRow ? "#ffffff" : "#000000");
	}
	this.currentFieldName = fieldRow.id.substring((this.pageType + "Field_").length);
	document.getElementsByName(this.pageType + "FieldBegin")[0].value = document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_begin")[0].value;
	document.getElementsByName(this.pageType + "FieldEnd")[0].value = document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_end")[0].value;
	document.getElementsByName(this.pageType + "ArraySeparator")[0].value = document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_arraySeparator")[0].value;
	document.getElementsByName(this.pageType + "FieldValue")[0].value = document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_value")[0].value;
	document.getElementsByName(this.pageType + "FieldFormat")[0].value = document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_format")[0].value;
	
	var fieldType = document.getElementsByName(this.pageType + "Field_" + this.currentFieldName + "_fieldType")[0].value; //获取字段类型
	document.getElementById(this.pageType + "FieldFormatDiv").style.display = fieldType=="date" || fieldType=="timestamp" || fieldType=="date[]" || fieldType=="timestamp[]" ? "" : "none"; //日期或者时间字段,显示格式DIV
	document.getElementById(this.pageType + "ArraySeparatorDiv").style.display = fieldType.indexOf('[]')!=-1 ? "" : "none"; //数组字段,显示数组分隔符DIV
	
	this.highLightSource(false);
};
//字段被双击
CaptureRuleConfigure.prototype.onDblclickField = function(fieldRow) {
	if(this.isFieldValid(fieldRow)) { //当前字段已经是有效的,则不移动
		return;
	}
	//获取最后一个有效字段(设置了开始、结束位置或者字段指定)
	var table = document.getElementById(this.pageType + "FieldTable");
	var index = table.rows.length-1;
	for(; index>=0 && !this.isFieldValid(table.rows[index]); index--);
	index -= fieldRow.rowIndex;
	for(var i=0; i<Math.abs(index) + (index<0 ? -1 : 0); i++) {
		this.moveField(index<0, true);
	}
};
//检查字段是否有效,设置了开始、结束位置或者字段指定
CaptureRuleConfigure.prototype.isFieldValid = function(fieldRow) {
	var fieldName = fieldRow.id.substring((this.pageType + "Field_").length);
	return document.getElementsByName(this.pageType + "Field_" + fieldName + "_value")[0].value!="" ||
		   document.getElementsByName(this.pageType + "Field_" + fieldName + "_begin")[0].value!="" ||
		   document.getElementsByName(this.pageType + "Field_" + fieldName + "_end")[0].value!="";
};
//移动字段
CaptureRuleConfigure.prototype.moveField = function(up, currentFieldOnly) {
	var table = document.getElementById(this.pageType + "FieldTable");
	var selects = table.getElementsByTagName("input");
	var selectedRows = new Array();
	for(var i=0; !currentFieldOnly && i<selects.length; i++) {
		if(selects[i].checked) {
			selectedRows[selectedRows.length] = selects[i].parentNode.parentNode;
		}
	}
	if(selectedRows.length==0 && this.currentFieldName) {
		selectedRows[selectedRows.length] = document.getElementById(this.pageType + "Field_" + this.currentFieldName);
	}
	if(up) {
		for(var i=0; i<selectedRows.length; i++) {
			var rowIndex = selectedRows[i].rowIndex;
			if(rowIndex==0) {
				return;
			}
			var selected = table.rows[rowIndex].getElementsByTagName("input")[0].checked;
			DomUtils.moveTableRow(table, rowIndex, rowIndex-1);
			table.rows[rowIndex-1].getElementsByTagName("input")[0].checked = selected;
		}
	}
	else {
		for(var i=selectedRows.length-1; i>=0; i--) {
			var rowIndex = selectedRows[i].rowIndex;
			if(rowIndex==table.rows.length-1) {
				return;
			}
			var selected = table.rows[rowIndex].getElementsByTagName("input")[0].checked;
			DomUtils.moveTableRow(table, rowIndex, rowIndex+1);
			table.rows[rowIndex+1].getElementsByTagName("input")[0].checked = selected;
		}
	}
	selectedRows[0].cells[0].focus();
};