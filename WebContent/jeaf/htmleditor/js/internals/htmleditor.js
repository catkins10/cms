/**
 * HTML编辑器
 * @param fieldName 字段名称
 * @param height 高度
 * @param fullPage 是否完整的页面
 * @param autoIndentation 自动缩进
 * @param centerImage 插入图片时是否自动居中
 * @param readonly 是否只读
 * @param plugins 插件,格式: /cms/inquiry/editorplugins/plugin.js,...
 * @param commands 命令,格式: cut,copy,paste,-,pasteText;...
 * @param attachmentSelectorURL 附件选择器URL
 **/
HtmlEditor = function(fieldName, height, fullPage, autoIndentation, centerImage, readonly, plugins, commands, attachmentSelectorURL, parentElement) {
	this.fieldName = fieldName;
	this.height = height;
	this.fullPage = fullPage;
	this.autoIndentation = autoIndentation;
	this.centerImage = centerImage;
	this.readonly = readonly;
	this.plugins = plugins && plugins!="" ? plugins.split(",") : null;
	this.commands = commands;
	this.attachmentSelectorURL = attachmentSelectorURL;
	this.parentElement = parentElement;
	this.nonEmptyBlockElements = {p:1,div:1,h1:1,h2:1,h3:1,h4:1,h5:1,h6:1,address:1,pre:1,ol:1,ul:1,li:1}; //TODO:不允许为空的元素
	this.document = document;
	this.window = window;
	this.htmlValue = this._getField().value;
	this.undoStepIndex = -1;
	this.undoSteps = [];
	this.create();
};
HtmlEditor.prototype.create = function() {
	if(this.parentElement) { //指定了父元素
		this.editorElement = doucment.createElement("table");
		this.parentElement.appendChild(this.editorElement);
	}
	else {
		var id = 'editor_' + Math.random();
		document.write('<table id="' + id + '"></table>');
		this.editorElement = document.getElementById(id);
	}
	this.editorElement.border = 0;
	this.editorElement.cellPadding = 0;
	this.editorElement.cellSpacing = 0;
	this.editorElement.width = "100%";
	this.editorElement.style.height = this.height;
	this.editorElement.style.tableLayout = 'fixed';
	this.editorElement.className = "htmleditor";
	if(this.readonly) { //只读
		this.createEditorArea(); //创建编辑区域
		return;
	}
	//加载插件,加载完成后,创建工具栏和编辑区域
	var editor = this;
	this.loadPlugin(0, function() {
		var td = editor.editorElement.insertRow(-1).insertCell(-1);
		td.style.padding = '0px 0px 0px 0px';
		editor.createToolbar(td); //创建工具栏
		editor.createEditorArea(); //创建编辑区域
	});
};
HtmlEditor.prototype.loadPlugin = function(pluginIndex, callback) { //加载插件
	if(pluginIndex >= (this.plugins ? this.plugins.length : 0)) {
		callback.call(null);
		return;
	}
	var editor = this;
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + this.plugins[pluginIndex], null, function() {
		editor.loadPlugin(pluginIndex + 1, callback);
	});
};
HtmlEditor.prototype.createEditorArea = function() { //创建编辑区域
	try {
		this.iframe = document.createElement('<iframe onload="this.editor._onload();"></iframe>');
	}
	catch(e) {
		this.iframe = document.createElement('iframe');
		var editor = this;
		this.iframe.onload = function() {
			editor._onload();
		};
	}
	var cell = this.editorElement.insertRow(-1).insertCell(-1);
	cell.style.padding = '0px 0px 0px 0px';
	cell.height = "100%"
	
	this.iframe.scrolling = "auto";
	this.iframe.frameBorder = 0;
	this.iframe.style.width = "100%"; 		
	this.iframe.style.height = "100%";
	this.iframe.editor = this;
	if(document.all) { //IE6
		var editor = this;
		this.editorElement.onresize = function() {
			editor._setEditorAreaHeight();
		};
		this._setEditorAreaHeight();
		cell.appendChild(this.iframe);
	}
	else { //W3C
		var div = document.createElement('div');
		div.style.width = "100%"; 		
		div.style.height = "100%";
		div.style.position = 'relative';
		this.iframe.style.left = "0px";
		this.iframe.style.top = "0px";
		this.iframe.style.position = 'absolute';
		div.appendChild(this.iframe);
		cell.appendChild(div);
	}
};
HtmlEditor.prototype._setEditorAreaHeight = function(force) {
	if(!document.all) { //W3C
		return;
	}
	if(this.editorElement.rows.length==1 || this.editorElement.offsetHeight==0) {
		return;
	}
	if(!force && this.lastSetEditorAreaHeight && new Date().getTime()-this.lastSetEditorAreaHeight<100) {
		return;
	}
	this.lastSetEditorAreaHeight = new Date().getTime();
	var editorAreaCell = this.editorElement.rows[1].cells[0];
	editorAreaCell.style.height = this.iframe.style.height = '1px';
	var tableHeight = this.editorElement.offsetHeight;
	editorAreaCell.style.height = '100%';
	editorAreaCell.style.height = this.iframe.style.height = (tableHeight - this.editorElement.rows[0].cells[0].offsetHeight) + "px";
};
HtmlEditor.prototype._onload = function() {
	if(this.editorDocument) {
		return;
	}
	this.editorWindow = this.iframe.contentWindow;
	this.editorDocument = this.iframe.contentWindow.document;
	if(!this.fullPage && !this.editorDocument.getElementById('editorAreaStyle')) { //不是完整页面
	    this.editorDocument.open();
	    //this.editorDocument.write('<!DOCTYPE HTML>'); //this.editorDocument.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">');
		this.editorDocument.write('<html><head>');
		this.editorDocument.write('<style id="editorAreaStyle">');
		for(var i=0; i<window.parent.document.styleSheets.length; i++) {
			var styleSheet = window.parent.document.styleSheets[i];
			for(var j=0; j<styleSheet.rules.length; j++) {
				var name = styleSheet.rules[j].selectorText;
				if(name.indexOf(".htmleditor .editorarea ")==0) {
					this.editorDocument.write(name.replace(/\.htmleditor \.editorarea /gi, "") + "{" + styleSheet.rules[j].style.cssText + "}");
				}
			}
	    }
	    this.editorDocument.write(this._getEditingStyles());
	    this.editorDocument.write('</style>');
	    this.editorDocument.write('<script type="text/javascript">window.onerror=function(){return true;}</script>');
	    this.editorDocument.write('</head><body></body></html>');
		this.editorDocument.close();
	}
	//加载HTML
	if(this.htmlValue=="" && !this.fullPage && this.autoIndentation) { //正文为空、不是完整页面、自动缩进
		this.htmlValue = "<p>　　</p>";
	}
	this.setHTML(this.htmlValue);
	this.htmlValue = "";
	if(!this.readonly) {
		this.refreshToolbarButtonState(); //更新工具栏命令状态
	}
	//记录到编辑器列表
	if(!HtmlEditor.editorInstances) {
		HtmlEditor.editorInstances = [];
	}
	HtmlEditor.editorInstances.push(this);
};
HtmlEditor.prototype._getEditingStyles = function() { /*编辑时使用的样式*/
	return ".editingNoBorderTable {" + /*编辑状态的没有边框的表格*/
		   "	border-collapse: collapse;" +
		   "	border-spacing: 0px;" +
		   "}\r\n" +
		   ".editingNoBorderTable td {" + /*编辑状态的没有边框的表格单元格*/
		   "	border: #f0f0f0 1px solid;" +
		   "}\r\n" +
		   ".editingFixedHeightTd {" + /*编辑状态的固定高度单元格*/
		   "	height: 1.8em;" +
		   "}\r\n" +
		   ".editingForm {" + /*编辑状态的表单*/
		   "	border: #ffc0c0 1px dotted;" +
		   "}\r\n" +
		   ".editingFixedHeightForm {" + /*编辑状态的固定高度表单*/
		   "	border: #ffc0c0 1px dotted;" +
		   "	height: 1.5em;" +
		   "}\r\n" +
		   ".editingHiddenInput {" + /*编辑状态的隐藏域*/
		   "	width: 1.2em;" +
		   "	height: 1.2em;" +
		   "	color: transparent;" +
		   "	border: #606060 1px dotted;" +
		   "}\r\n" +
		   ".editingVideoPlayer {" + /*编辑状态的视频播放器*/
		   "	display: inline-block;" +
		   "	overflow: hidden;" +
		   "}\r\n" +
		   ".editingVideoPlayer .playerControlBar {" + /*编辑状态的视频播放器控制栏*/
		   "	position: relative;" +
		   "	display: inline-block;" +
		   "	background-position: left bottom;" +
		   "	border-style: none !important;" +
		   "	background-repeat: repeat-x;" +
		   "	background-image: url(" + RequestUtils.getContextPath() + "/jeaf/htmleditor/images/video_center.png);" +
		   "}\r\n" +
		   ".editingVideoPlayer .playerControlBarLeft {" + /*编辑状态的视频播放器控制栏左侧*/
		   "	position: relative;" +
		   "	display: inline-block;" +
		   "	background-position: left bottom;" +
		   "	background-repeat: no-repeat;" +
		   "	border-style: none !important;" +
		   "	background-image: url(" + RequestUtils.getContextPath() + "/jeaf/htmleditor/images/video_left.png);" +
		   "}\r\n" +
		   ".editingVideoPlayer .playerControlBarRight {" + /*编辑状态的视频播放器控制栏右侧*/
		   "	position: relative;" +
		   "	display: inline-block;" +
		   "	background-position: right bottom;" +
		   "	background-repeat: no-repeat;" +
		   "	border-style: none !important;" +
		   "	background-image: url(" + RequestUtils.getContextPath() + "/jeaf/htmleditor/images/video_right.png);" +
		   "}";
};
HtmlEditor.prototype._getField = function() {
	var field = document.getElementById(this.fieldName);
	return field ? field : FormUtils.getField(document, this.fieldName);
};
HtmlEditor.saveHtmlContent = function() { //保存HTML内容
	if(!HtmlEditor.editorInstances) {
		return;
	}
	for(var i=0; i<HtmlEditor.editorInstances.length; i++) {
		var editor = HtmlEditor.editorInstances[i];
		if(editor.readonly) {
			continue;
		}
		if(editor.sourceCodeMode) { //当前处于源码模式
			editor.setHTML(editor.sourceCodeTextArea.value); //重新加载HTML
		}
		var value = editor.getHTML();
		if(value || value=='') {
			editor._getField().value = value;
		}
	}
};
HtmlEditor.getEditor = function(fieldName) { //获取HTML编辑器
	if(!HtmlEditor.editorInstances) {
		return null;
	}
	for(var i=0; i<HtmlEditor.editorInstances.length; i++) {
		var editor = HtmlEditor.editorInstances[i];
		if(editor._getField().name==fieldName) {
			return editor;;
		}
	}
	return null;
};
HtmlEditor.getHtmlContent = function(fieldName) { //获取HTML内容
	var editor = HtmlEditor.getEditor(fieldName);
	if(!editor) {
		return "";
	}
	if(editor.sourceCodeMode) { //当前处于源码模式
		editor.setHTML(editor.sourceCodeTextArea.value); //重新加载HTML
	}
	var value = editor.getHTML();
	if(editor.autoIndentation && value=="<P>　　</P>") {
		value = "";
	}
	return value;
};
HtmlEditor.prototype.getHTML = function() { //获取HTML
	if(!this.fullPage) { //不是完整页面
		return this._restoreHTML(this.editorDocument.body.innerHTML);
	}
	var html = '';
	var doctype = this.editorDocument.doctype;
	if(!doctype) {
		doctype = this.editorDocument.documentElement.previousSibling;
		if(doctype && doctype.nodeType==8) {
			html = '<!' + doctype.data + '>\r\n';
		}
	}
	else {
		html = '<!DOCTYPE ' +
			   doctype.name +
			   (doctype.publicId ? ' PUBLIC "' + doctype.publicId + '"' : '') +
			   (!doctype.publicId && doctype.systemId ? ' SYSTEM' : '') +
			   (doctype.systemId ? ' "' + doctype.systemId + '"' : '') +
			   '>\r\n';
	}
	var documentElement = this.editorDocument.documentElement;
	var metas = documentElement.getElementsByTagName("meta");
	for(var i=metas.length-1; i>=0; i--) {
		if(metas[i].name=="GENERATOR") {
			metas[i].parentNode.removeChild(metas[i]);
		}
	}
	var innerHTML = documentElement.innerHTML.replace(/ contenteditable=['"]?true['"]?/i, '')
											 .replace(/<style(.*)id=['"]?editingStyle['"]?[^>]*>[\s\S]*?<\/style>/i, '');
	var htmlTagName = 'html';
	var index = innerHTML.indexOf('<');
	if(index!=-1 && innerHTML.length > index+1 && innerHTML.charAt(index+1)<='Z') {
		htmlTagName = 'HTML';
	}
	html += "<" + htmlTagName;
	for(var i=0; i<documentElement.attributes.length; i++) {
		if(documentElement.attributes[i].specified) {
			html += ' ' + documentElement.attributes[i].nodeName + '="' + documentElement.attributes[i].nodeValue + '"';
		}
	}
	return html + '>\r\n' + this._restoreHTML(innerHTML) + '\r\n</' + htmlTagName + '>';	
};
HtmlEditor.prototype.setHTML = function(html) { //加载HTML
	//替换脚本
	var match;
	this.resetScripts = [];
	if(!this.readonly) { //不是只读
		while((match = html.match(/<script[^>]*>[\s\S]*?<\/script[^>]*>/i))) {
			var resetScript = {id:"" + Math.random(), script:match.toString()};
			this.resetScripts[this.resetScripts.length] = resetScript;
			html = html.substring(0, match.index) + "<!--script" + resetScript.id + "-->" + html.substring(match.index + resetScript.script.length);
		}
	}
	if(!this.fullPage) { //不是完整页面
		this.editorDocument.body.innerHTML = html;
	}
	else { //完整页面
		if(!this.readonly) { //不是只读
			var style = '<style id="editingStyle" type="text/css">' + this._getEditingStyles() + "</style>";
			var index = html.search(/<\/head>/i);
			if(index!=-1) {
				html = html.substring(0, index) + style + html.substring(index);
			}
			else if((index = html.search(/<html/i))==-1) {
				index = html.search(/<\/body>/i);
				html = '<html><head>' + style + '</head>' + (index==-1 ? '<body>' + html + '</body>' : html) + '</html>';
			}
			else {
				var index = html.indexOf('>', index + 5) + 1;
				html = html.substring(0, index) + '<head>' + style + '</head>' + html.substring(index);
			}
		}
		this.editorDocument = this.editorDocument.open();
		this.editorDocument.write(html);
		this.editorDocument.close();
	}
	if(this.readonly) {
		return;
	}
	this._resetElements();
	if('' + this.editorDocument.body.contentEditable=='true') {
		return;
	}
	this.editorDocument.body.contentEditable = true; //进入编辑状态
	var editor = this;
	this.editorDocument.onselectionchange = function() { //事件处理:选择区域改变
		window.setTimeout(function() {
			editor._trim();
		}, 10);
		editor.refreshToolbarButtonState(); //更新工具栏命令状态
	};
	this.editorDocument.body.onkeydown = function(event) { //事件处理:按下按键
		return editor._onKeydown(event);
	};
	this.editorDocument.body.onkeyup = function(event) { //事件处理:松开按键
		if(!event) {
			event = editor.editorWindow.event;
		}
		if(event.keyCode==13 && editor.autoIndentation) { //回车、自动缩进
			DomSelection.pasteHTML(editor.editorWindow, DomSelection.getRange(editor.editorWindow), "　　");
		}
		if(event.keyCode<37 || event.keyCode>40) {
			window.setTimeout(function() {
				editor._resetElements();
			}, 10);
		}
	};
	this.editorDocument.body.onkeypress = function(event) { //事件处理:松开按下
		if(!event) {
			event = editor.editorWindow.event;
		}
		if(event.keyCode==32 && !event.ctrlKey && !event.altKey) { //空格
			DomSelection.pasteHTML(editor.editorWindow, editor.getRange(), "&ensp;");
			return false;
		}
	};
	this.editorDocument.body.onpaste = function(event) { //事件处理:粘贴
		return editor.onPaste(event ? event : editor.editorWindow.event);
	};
	this.editorDocument.body.oncut = function() { //事件处理:剪切
		editor.saveUndoStep();
		editor.refreshToolbarButtonState(); //更新工具栏命令状态
	};
	this.editorDocument.body.ondrop = function() { //事件处理:拖拽
		editor.saveUndoStep();
		editor.refreshToolbarButtonState(); //更新工具栏命令状态
	};
};
HtmlEditor.prototype._trim = function() { //清理空格
	var childNodes = this.editorDocument.body.childNodes;
	if(childNodes && childNodes.length>0 && childNodes[childNodes.length-1].nodeType==3) {
		var value = childNodes[childNodes.length-1].nodeValue;
		var index;
		for(index=value.length-1; index>=0 && ' \r\n'.indexOf(value.charAt(index))!=-1; index--);
		if(index==-1) {
			this.editorDocument.body.removeChild(childNodes[childNodes.length-1]);
		}
		else if(index!=value.length-1) {
			childNodes[childNodes.length-1].nodeValue = value.substring(0, index + 1);
		}
	}
};
HtmlEditor.prototype._resetElements = function() { //重置表格、表单、隐藏域等页面元素
	var editor = this;
	var events = ['onclick', 'onmouseover', 'onmouseout', 'onmousemove', 'onmousedown'];
	editor.resetEvents = false;
	editor.resetHiddenInputs = false;
	editor.resetForms = false;
	editor.resetTables = false;
	editor.resetTds = false;
	editor.resetVideos = false;
	DomUtils.traversalChildElements(this.editorDocument.body, function(element) {
		if(!element.tagName) {
			return true;
		}
		//阻止onclick、onmouse事件
		for(var i=0; i<events.length; i++) {
			var attribute;
			try {
				attribute = element.attributes[events[i]];
			}
			catch(e) {
			
			}
			if(attribute && attribute.value!='null') {
				if(attribute.value.substring(0, 12)!='return 1==2;') {
					attribute.value = 'return 1==2;' + attribute.value;
				}
				editor.resetEvents = true;
			}
		}
		//修改隐藏域的大小,并显示为图片
		if(element.tagName=='INPUT' && element.type=='hidden') {
			element.className = 'editingHiddenInput';
			editor.resetHiddenInputs = true;
			return true;
		}
		//显示表单的边框
		if(element.tagName=='FORM') {
			var className = (element.childNodes.length==0 ? 'editingFixedHeightForm' : 'editingForm');
			if(!element.className || element.className.indexOf(className)==-1) {
				var currentClassName = element.className ? element.className.replace('editingFixedHeightForm', '').replace('editingForm', '') : '';
				element.className = className + (currentClassName=='' || currentClassName.substring(0, 1)==' ' ? '' : ' ') + currentClassName;
			}
			editor.resetForms = true;
		}
		//处理表格,如果表格没有边框,修改样式以显示边框
		if(element.tagName=='TABLE') {
			editor._modifyStyleClass(element, 'editingNoBorderTable', element.border>0);
			if(element.border==0) {
				editor.resetTables = true;
			}
			for(var i=element.rows.length-1; i>=0; i--) {
				var j = element.rows[i].cells.length - 1;
				if(j<0) {
					continue;
				}
				for(; j>=0 && element.rows[i].cells[j].childNodes.length==0; j--);
				editor._modifyStyleClass(element.rows[i].cells[0], 'editingFixedHeightTd', j!=-1);
				if(j==-1) {
					editor.resetTds = true;
				}
			}
		}
		//处理视频
		if(element.tagName=='IMG' && element.id=='video') {
			editor.resetVideos = true;
			if(element.parentNode.className=='editingVideoPlayer') {
				return true;
			}
			var width = element.getAttribute('width', 2);
			var height = element.getAttribute('height', 2);
			var videoPlayer = editor.editorDocument.createElement('span');
			videoPlayer.className = 'editingVideoPlayer';
			videoPlayer.contentEditable = false;
			videoPlayer.style.width = width + "px";
			videoPlayer.style.height = height + "px";
			element.parentNode.replaceChild(videoPlayer, element);
			videoPlayer.appendChild(element);
			for(var i=0; i<3; i++) {
				var span = editor.editorDocument.createElement('span');
				span.style.top = "-" + (height * (i+1)) + "px";
				span.style.width = width + "px";
				span.style.height = height + "px";
				span.className = ['playerControlBar', 'playerControlBarLeft', 'playerControlBarRight'][i];
				span.innerHTML = "&nbsp;"
				videoPlayer.appendChild(span);
			}
			return true;
		}
	});
};
HtmlEditor.prototype._restoreHTML = function(html) { /*恢复HTML*/
	if(this.resetEvents) {
		html = html.replace(/return 1==2;/g, '');
	}
	if(this.resetHiddenInputs) {
		html = html.replace(/ class=['"]?editingHiddenInput['"]?/gi, '');
	}
	if(this.resetForms) {
		html = html.replace(/ class=editingFixedHeightForm/gi, '');
		html = html.replace(/editingFixedHeightForm /g, '');
		html = html.replace(/ class=['"]?editingFixedHeightForm['"]?/gi, '');
		html = html.replace(/ class=editingForm/gi, '');
		html = html.replace(/editingForm /g, '');
		html = html.replace(/ class=['"]?editingForm['"]?/gi, '');
	}
	if(this.resetTables) {
		html = html.replace(/editingTable /g, '');
		html = html.replace(/( class=['"])editingNoBorderTable /gi, '$1');
		html = html.replace(/ class=['"]?editingNoBorderTable['"]?/gi, '');
	}
	if(this.resetTds) {
		html = html.replace(/editingFixedHeightTd /g, '');
		html = html.replace(/( class=['"])editingFixedHeightTd /gi, '$1');
		html = html.replace(/ class=['"]?editingFixedHeightTd['"]?/gi, '');
	}
	if(this.resetVideos) {
		html = html.replace(/<span[^>]*class=['"]?playerControlBar[^>]*>[\s\S]*?<\/span>/gi, '');
		html = html.replace(/<span[^>]*class=['"]?editingVideoPlayer[^>]*>([\s\S]*?)<\/span>/gi, '$1');
	}
	for(var i=0; i<(this.resetScripts ? this.resetScripts.length : 0); i++) {
		html = html.replace(new RegExp("<!--script" + this.resetScripts[i].id + "-->", "i"), this.resetScripts[i].script);
	}
	if(html.replace(/<br[^>]*?>/, '')=='') {
		html = '';
	}
	return html;
};
HtmlEditor.prototype._modifyStyleClass = function(element, className, isRemove) { //添加或者移除样式
	if(!isRemove) { //添加样式
		if(!element.className || element.className.indexOf(className)==-1) {
			element.className = className + (element.className ? ' ' + element.className : '');
		}
	}
	else if(element.className==className) {
		element.removeAttribute('class');
		element.removeAttribute('className');
	}
	else if(element.className && element.className.indexOf(className)==0) {
		element.className = element.className.substring(className.length + 1);
	}
};
HtmlEditor.prototype.getLinkedForm = function() { //获取表单
	return DomUtils.getParentNode(this.editorElement, "form");
};
HtmlEditor.prototype.getRange = function() { //获取选取范围
	var range = DomSelection.getRange(this.editorWindow);
	var selectedElement = DomSelection.getSelectedElement(range);
	if(selectedElement && selectedElement.ownerDocument==this.editorDocument && selectedElement.tagName!='HTML') {
		this.lastRangeBookmark = DomSelection.getRangeBookmark(range);
	}
	else if(!this.lastRangeBookmark) {
		return null;
	}
	else {
		range = DomSelection.createRangeByBookmark(this.editorDocument, this.lastRangeBookmark);
	}
	return range;
};
HtmlEditor.prototype._onKeydown = function(event) { //处理按键事件
	if(!event) {
		event = this.editorWindow.event;
	}
	if(event.keyCode==90 && event.ctrlKey) { //ctrl+z
		this.undo();
		return false;
	}
	if(event.keyCode==89 && event.ctrlKey) { //ctrl+y
		this.redo();
		return false;
	}
	if(event.keyCode==13 || //回车
	   (event.keyCode!=16 && event.keyCode!=17 && event.keyCode!=18 && !event.ctrlKey && !event.altKey && (this.undoStepIndex==-1 || this.undoStepIndex<this.undoSteps.length-1))) { 
		this.saveUndoStep();
	}
	this.refreshToolbarButtonState(); //更新工具栏命令状态
};
HtmlEditor.prototype._switchEditMode = function() { //切换编辑方式
	if(this.sourceCodeMode) { //当前处于源码模式
		this.saveUndoStep();
		//加载HTML
		this.setHTML(this.sourceCodeTextArea.value);
		//删除文本框
		this.sourceCodeTextArea.parentNode.removeChild(this.sourceCodeTextArea);
		//显示iframe
		this.iframe.style.display = '';
		this.sourceCodeMode = false;
		
		//光标移到BODY起始位置
		var range = DomSelection.createRange(this.editorDocument, this.editorDocument.body);
		range.collapse(true);
		DomSelection.selectRange(this.editorWindow, range)
	}
	else {
		//创建源码文本框
		this.sourceCodeTextArea = document.createElement('textarea');
		this.sourceCodeTextArea.className = "sourceCodeTextArea";
		this.iframe.parentNode.insertBefore(this.sourceCodeTextArea, this.iframe);
		this.sourceCodeTextArea.value = this.getHTML();
		//隐藏iframe
		this.iframe.style.display = 'none';
		this.sourceCodeMode = true;
	}
	this.refreshToolbarButtonState();
};
HtmlEditor.prototype._switchFullScreen = function() { //切换全屏编辑方式
	if(this.fullScreenMode) { //当前全屏编辑模式
		this.fullScreenMode = false;
		PageUtils.destoryCover(window, this.fullScreenCover);
		this.editorElement.style.zIndex = 0;
		this.editorElement.style.position = 'static';
		this.editorElement.style.left = "";
		this.editorElement.style.top = "";
		this.editorElement.style.width = "100%";
		this.editorElement.style.height = this.height;
	}
	else {
		this.fullScreenMode = true;
		this.fullScreenCover = PageUtils.createCover(window, 6);
		this.editorElement.style.zIndex = parseInt(this.fullScreenCover.style.zIndex) + 1;
		this.editorElement.style.position = 'absolute';
		this.editorElement.style.left = this.fullScreenCover.style.left;
		this.editorElement.style.top = this.fullScreenCover.style.top;
		this.editorElement.style.width = this.fullScreenCover.style.width;
		this.editorElement.style.height = this.fullScreenCover.style.height;
	}
	this._setEditorAreaHeight(true);
};

/**
 * 注册HTML编辑器命令
 * 属性: name/名称, title/标题, iconIndex/图标序号(使用预置图标时有效), iconUrl/图标URL, showTitle/是否显示标题, showDropButton/是否显示下拉按钮, enabledInSourceCodeMode/在源码模式下是否有效
 * 获取状态方法: getState = function(sourceCodeMode, editorWindow, editorDocument, range, selectedElement)
 * 执行命令方法: execute = function(editorWindow, editorDocument, range, selectedElement)
 * 创建方法,用于自定义显示方式: create = function(toolbarButtonElement)
 **/
HtmlEditor.registerCommand = function(command) {
	if(!HtmlEditor.commands) {
		HtmlEditor.commands = [];
	}
	HtmlEditor.commands.push(command);
};
HtmlEditor.prototype.getCommand = function(name) { //获取命令
	//从插件中查找命令
	if(HtmlEditor.commands) {
		for(var i=0; i<HtmlEditor.commands.length; i++) {
			if(HtmlEditor.commands[i].name==name) {
				return HtmlEditor.commands[i];
			}
		}
	}
	var command;
	//检查是否预置命令
	if('bold'==name) {
		command = new EditorNamedCommand(name, '加粗', 19);
	}
	else if('italic'==name) {
		command = new EditorNamedCommand(name, '斜体', 20);
	}
	else if('underline'==name) {
		command = new EditorNamedCommand(name, '下划线', 21);
	}
	else if('strikeThrough'==name) {
		command = new EditorNamedCommand(name, '删除线', 22);
	}
	else if('subscript'==name) {
		command = new EditorNamedCommand(name, '下标', 23);
	}
	else if('superscript'==name) {
		command = new EditorNamedCommand(name, '上标', 24);
	}
	else if('source'==name) {
		command = new EditorSourceCommand(name, '源代码', 0);
	}
	else if('fitWindow'==name) {
		command = new EditorFitWindowCommand(name, '全屏编辑', 65);
	}
	else if('undo'==name) {
		command = new EditorUndoRedoCommand(name, '撤销', 13);
	}
	else if('redo'==name) {
		command = new EditorUndoRedoCommand(name, '重做', 14);
	}
	else if('copy'==name) {
		command = new EditorNamedCommand(name, '拷贝', 7);
	}
	else if('cut'==name) {
		command = new EditorNamedCommand(name, '剪切', 6);
	}
	else if('paste'==name) {
		command = new EditorNamedCommand(name, '粘贴', 8);
	}
	else if('selectAll'==name) {
		command = new EditorNamedCommand(name, '全选', 17);
	}
	else if('removeFormat'==name) {
		command = new EditorNamedCommand(name, '清除格式', 18);
	}
	else if('justifyLeft'==name) {
		command = new EditorNamedCommand(name, '左对齐', 29);
	}
	else if('justifyCenter'==name) {
		command = new EditorNamedCommand(name, '居中对齐', 30);
	}
	else if('justifyRight'==name) {
		command = new EditorNamedCommand(name, '右对齐', 31);
	}
	else if('justifyFull'==name) {
		command = new EditorNamedCommand(name, '两端对齐', 32);
	}
	else if('indent'==name) {
		command = new EditorNamedCommand(name, '增加缩进量', 28);
	}
	else if('outdent'==name) {
		command = new EditorNamedCommand(name, '减少缩进量', 27);
	}
	else if('insertOrderedList'==name) {
		command = new EditorNamedCommand(name, '插入编号列表', 25);
	}
	else if('insertUnorderedList'==name) {
		command = new EditorNamedCommand(name, '插入项目列表', 26);
	}
	else if('textColor'==name) {
		command = new EditorTextColorCommand('foreColor', '文本颜色', 44);
	}
	else if('bgColor'==name) {
		command = new EditorTextColorCommand('backColor', '背景颜色', 45);
	}
	else if('fontName'==name) {
		command = new EditorFontCommand(name, '字体', 76);
	}
	else if('fontSize'==name) {
		command = new EditorFontCommand(name, '字体大小', 77);
	}
	else if('link'==name) {
		command = new EditorLinkCommand(name, '链接/附件', 33);
	}
	else if('unlink'==name) {
		command = new EditorNamedCommand(name, '取消链接', 34);
	}
	else if('image'==name) {
		command = new EditorImageCommand(name, '图片', 36);
	}
	else if('flash'==name) {
		command = new EditorFlashCommand(name, 'FLASH', 37);
	}
	else if('video'==name) {
		command = new EditorVideoCommand(name, '视频', 78);
	}
	else if('specialChar'==name) {
		command = new EditorSpecialCharCommand(name, '插入特殊字符', 41);
	}
	else if('smiley'==name) {
		command = new EditorSmileyCommand(name, '插入表情图标', 40);
	}
	else if('pageBreak'==name) {
		command = new EditorPageBreakCommand(name, '插入分页符', 42);
	}
	else if('rule'==name) {
		command = new EditorRuleCommand(name, '插入水平线', 39);
	}
	else if('find'==name) {
		command = new EditorFindCommand(name, '查找/替换', 15);
	}
	else if('table'==name) {
		command = new EditorTableCommand(name, '表格', 38);
	}
	else if('paragraph'==name) {
		command = new EditorParagraphCommand('paragraph', '段落格式', 79);
	}
	else if('print'==name) {
		command = new EditorPrintCommand('print', '打印', 11);
	}
	return command;
};
HtmlEditor.prototype.setFontStyle = function(styleName, styleValue) { //设置字体样式
	var range = this.getRange();
	if(range.getBookmark && range.boundingWidth==0) { //IE:TextRange
		var id = Math.random();
		range.pasteHTML('<font id="' + id + '"></font>');
		var font = this.editorDocument.getElementById(id);
		font.removeAttribute("id");
		eval('font.style.' + styleName + '="' + styleValue + '";');
		font.innerHTML = '\u200b';
		range.moveToElementText(font);
		range.collapse(false);
		DomSelection.selectRange(this.editorWindow, range);
		font.removeChild(font.childNodes[0]);
		return;
	}
	if(range.insertNode && range.startContainer==range.endContainer && range.startOffset==range.endOffset) { //W3C
		var font = this.editorDocument.createElement('font');
		eval('font.style.' + styleName + '="' + styleValue + '";');
		font.innerHTML = '\u200b';
		range.insertNode(font);
		range.selectNodeContents(font);
		range.collapse(false);
		DomSelection.selectRange(this.editorWindow, range);
		return;
	}
	var fontName = "font_" + ("" + Math.random()).substring(2);
	this.editorDocument.execCommand("fontName", false, fontName);
	var fonts = this.editorDocument.getElementsByTagName("font");
	for(var i=0; i<fonts.length; i++) {
		if(fonts[i].face==fontName) {
			fonts[i].removeAttribute("face");
			eval('fonts[i].style.' + styleName + '="' + styleValue + '";');
		}
	}
};
HtmlEditor.prototype.getAttachmentSelectorURL = function(attachmentType, scriptAfterSelect) { //获取附件选择URL,scriptAfterSelect默认为:setUrl("{URL}", {WIDTH}, {HEIGHT})
	if(!this.attachmentSelectorURL || this.attachmentSelectorURL=="") {
		return null;
	}
	var url;
	if(this.attachmentSelectorURL.substring(0,1)=='/') {
		url = RequestUtils.getContextPath() + this.attachmentSelectorURL;
	}
	else {
		url = window.location.href;
		url = url.substring(0, url.lastIndexOf('/') + 1) + this.attachmentSelectorURL;
	}
	var queryString = document.getElementsByName("queryString")[0];
	if(queryString) {
		url += (url.lastIndexOf('?')==-1 ? '?' : '&') + StringUtils.removeQueryParameter(queryString.value, "id");
	}
	if(!scriptAfterSelect) {
		scriptAfterSelect = 'setUrl("{URL}")';
	}
	return url + '&attachmentSelector.scriptRunAfterSelect=' + scriptAfterSelect + '&attachmentSelector.type=' + attachmentType;
};
HtmlEditor.getDialogArguments = function() { //生成对话框参数
	return {editor:HtmlEditor.editor, window:HtmlEditor.editorWindow, document:HtmlEditor.editorDocument, range:HtmlEditor.range, selectedElement:HtmlEditor.selectedElement};
};