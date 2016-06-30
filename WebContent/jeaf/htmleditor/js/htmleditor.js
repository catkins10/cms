
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
	this.nonEmptyBlockElements = {p:1,div:1,h1:1,h2:1,h3:1,h4:1,h5:1,h6:1,address:1,pre:1,ol:1,ul:1,li:1}; 
	this.document = document;
	this.window = window;
	this.htmlValue = this._getField().value;
	this.undoStepIndex = -1;
	this.undoSteps = [];
	this.create();
};
HtmlEditor.prototype.create = function() {
	if(this.parentElement) { 
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
	if(this.readonly) { 
		this.createEditorArea(); 
		return;
	}
	
	var editor = this;
	this.loadPlugin(0, function() {
		var td = editor.editorElement.insertRow(-1).insertCell(-1);
		td.style.padding = '0px 0px 0px 0px';
		editor.createToolbar(td); 
		editor.createEditorArea(); 
	});
};
HtmlEditor.prototype.loadPlugin = function(pluginIndex, callback) { 
	if(pluginIndex >= (this.plugins ? this.plugins.length : 0)) {
		callback.call(null);
		return;
	}
	var editor = this;
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + this.plugins[pluginIndex], null, function() {
		editor.loadPlugin(pluginIndex + 1, callback);
	});
};
HtmlEditor.prototype.createEditorArea = function() { 
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
	if(document.all) { 
		var editor = this;
		this.editorElement.onresize = function() {
			editor._setEditorAreaHeight();
		};
		this._setEditorAreaHeight();
		cell.appendChild(this.iframe);
	}
	else { 
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
	if(!document.all) { 
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
	if(!this.fullPage && !this.editorDocument.getElementById('editorAreaStyle')) { 
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
	
	if(this.htmlValue=="" && !this.fullPage && this.autoIndentation) { 
		this.htmlValue = "<p>\u3000\u3000</p>";
	}
	this.setHTML(this.htmlValue);
	this.htmlValue = "";
	if(!this.readonly) {
		this.refreshToolbarButtonState(); 
	}
	
	if(!HtmlEditor.editorInstances) {
		HtmlEditor.editorInstances = [];
	}
	HtmlEditor.editorInstances.push(this);
};
HtmlEditor.prototype._getEditingStyles = function() { 
	return ".editingNoBorderTable {" + 
		   "	border-collapse: collapse;" +
		   "	border-spacing: 0px;" +
		   "}\r\n" +
		   ".editingNoBorderTable td {" + 
		   "	border: #f0f0f0 1px solid;" +
		   "}\r\n" +
		   ".editingFixedHeightTd {" + 
		   "	height: 1.8em;" +
		   "}\r\n" +
		   ".editingForm {" + 
		   "	border: #ffc0c0 1px dotted;" +
		   "}\r\n" +
		   ".editingFixedHeightForm {" + 
		   "	border: #ffc0c0 1px dotted;" +
		   "	height: 1.5em;" +
		   "}\r\n" +
		   ".editingHiddenInput {" + 
		   "	width: 1.2em;" +
		   "	height: 1.2em;" +
		   "	color: transparent;" +
		   "	border: #606060 1px dotted;" +
		   "}\r\n" +
		   ".editingVideoPlayer {" + 
		   "	display: inline-block;" +
		   "	overflow: hidden;" +
		   "}\r\n" +
		   ".editingVideoPlayer .playerControlBar {" + 
		   "	position: relative;" +
		   "	display: inline-block;" +
		   "	background-position: left bottom;" +
		   "	border-style: none !important;" +
		   "	background-repeat: repeat-x;" +
		   "	background-image: url(" + RequestUtils.getContextPath() + "/jeaf/htmleditor/images/video_center.png);" +
		   "}\r\n" +
		   ".editingVideoPlayer .playerControlBarLeft {" + 
		   "	position: relative;" +
		   "	display: inline-block;" +
		   "	background-position: left bottom;" +
		   "	background-repeat: no-repeat;" +
		   "	border-style: none !important;" +
		   "	background-image: url(" + RequestUtils.getContextPath() + "/jeaf/htmleditor/images/video_left.png);" +
		   "}\r\n" +
		   ".editingVideoPlayer .playerControlBarRight {" + 
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
HtmlEditor.saveHtmlContent = function() { 
	if(!HtmlEditor.editorInstances) {
		return;
	}
	for(var i=0; i<HtmlEditor.editorInstances.length; i++) {
		var editor = HtmlEditor.editorInstances[i];
		if(editor.readonly) {
			continue;
		}
		if(editor.sourceCodeMode) { 
			editor.setHTML(editor.sourceCodeTextArea.value); 
		}
		var value = editor.getHTML();
		if(value || value=='') {
			editor._getField().value = value;
		}
	}
};
HtmlEditor.getEditor = function(fieldName) { 
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
HtmlEditor.getHtmlContent = function(fieldName) { 
	var editor = HtmlEditor.getEditor(fieldName);
	if(!editor) {
		return "";
	}
	if(editor.sourceCodeMode) { 
		editor.setHTML(editor.sourceCodeTextArea.value); 
	}
	var value = editor.getHTML();
	if(editor.autoIndentation && value=="<P>\u3000\u3000</P>") {
		value = "";
	}
	return value;
};
HtmlEditor.prototype.getHTML = function() { 
	if(!this.fullPage) { 
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
HtmlEditor.prototype.setHTML = function(html) { 
	
	var match;
	this.resetScripts = [];
	if(!this.readonly) { 
		while((match = html.match(/<script[^>]*>[\s\S]*?<\/script[^>]*>/i))) {
			var resetScript = {id:"" + Math.random(), script:match.toString()};
			this.resetScripts[this.resetScripts.length] = resetScript;
			html = html.substring(0, match.index) + "<!--script" + resetScript.id + "-->" + html.substring(match.index + resetScript.script.length);
		}
	}
	if(!this.fullPage) { 
		this.editorDocument.body.innerHTML = html;
	}
	else { 
		if(!this.readonly) { 
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
	this.editorDocument.body.contentEditable = true; 
	var editor = this;
	this.editorDocument.onselectionchange = function() { 
		window.setTimeout(function() {
			editor._trim();
		}, 10);
		editor.refreshToolbarButtonState(); 
	};
	this.editorDocument.body.onkeydown = function(event) { 
		return editor._onKeydown(event);
	};
	this.editorDocument.body.onkeyup = function(event) { 
		if(!event) {
			event = editor.editorWindow.event;
		}
		if(event.keyCode==13 && editor.autoIndentation) { 
			DomSelection.pasteHTML(editor.editorWindow, DomSelection.getRange(editor.editorWindow), "\u3000\u3000");
		}
		if(event.keyCode<37 || event.keyCode>40) {
			window.setTimeout(function() {
				editor._resetElements();
			}, 10);
		}
	};
	this.editorDocument.body.onkeypress = function(event) { 
		if(!event) {
			event = editor.editorWindow.event;
		}
		if(event.keyCode==32 && !event.ctrlKey && !event.altKey) { 
			DomSelection.pasteHTML(editor.editorWindow, editor.getRange(), "&ensp;");
			return false;
		}
	};
	this.editorDocument.body.onpaste = function(event) { 
		return editor.onPaste(event ? event : editor.editorWindow.event);
	};
	this.editorDocument.body.oncut = function() { 
		editor.saveUndoStep();
		editor.refreshToolbarButtonState(); 
	};
	this.editorDocument.body.ondrop = function() { 
		editor.saveUndoStep();
		editor.refreshToolbarButtonState(); 
	};
};
HtmlEditor.prototype._trim = function() { 
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
HtmlEditor.prototype._resetElements = function() { 
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
		
		if(element.tagName=='INPUT' && element.type=='hidden') {
			element.className = 'editingHiddenInput';
			editor.resetHiddenInputs = true;
			return true;
		}
		
		if(element.tagName=='FORM') {
			var className = (element.childNodes.length==0 ? 'editingFixedHeightForm' : 'editingForm');
			if(!element.className || element.className.indexOf(className)==-1) {
				var currentClassName = element.className ? element.className.replace('editingFixedHeightForm', '').replace('editingForm', '') : '';
				element.className = className + (currentClassName=='' || currentClassName.substring(0, 1)==' ' ? '' : ' ') + currentClassName;
			}
			editor.resetForms = true;
		}
		
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
HtmlEditor.prototype._restoreHTML = function(html) { 
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
HtmlEditor.prototype._modifyStyleClass = function(element, className, isRemove) { 
	if(!isRemove) { 
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
HtmlEditor.prototype.getLinkedForm = function() { 
	return DomUtils.getParentNode(this.editorElement, "form");
};
HtmlEditor.prototype.getRange = function() { 
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
HtmlEditor.prototype._onKeydown = function(event) { 
	if(!event) {
		event = this.editorWindow.event;
	}
	if(event.keyCode==90 && event.ctrlKey) { 
		this.undo();
		return false;
	}
	if(event.keyCode==89 && event.ctrlKey) { 
		this.redo();
		return false;
	}
	if(event.keyCode==13 || 
	   (event.keyCode!=16 && event.keyCode!=17 && event.keyCode!=18 && !event.ctrlKey && !event.altKey && (this.undoStepIndex==-1 || this.undoStepIndex<this.undoSteps.length-1))) { 
		this.saveUndoStep();
	}
	this.refreshToolbarButtonState(); 
};
HtmlEditor.prototype._switchEditMode = function() { 
	if(this.sourceCodeMode) { 
		this.saveUndoStep();
		
		this.setHTML(this.sourceCodeTextArea.value);
		
		this.sourceCodeTextArea.parentNode.removeChild(this.sourceCodeTextArea);
		
		this.iframe.style.display = '';
		this.sourceCodeMode = false;
		
		
		var range = DomSelection.createRange(this.editorDocument, this.editorDocument.body);
		range.collapse(true);
		DomSelection.selectRange(this.editorWindow, range)
	}
	else {
		
		this.sourceCodeTextArea = document.createElement('textarea');
		this.sourceCodeTextArea.className = "sourceCodeTextArea";
		this.iframe.parentNode.insertBefore(this.sourceCodeTextArea, this.iframe);
		this.sourceCodeTextArea.value = this.getHTML();
		
		this.iframe.style.display = 'none';
		this.sourceCodeMode = true;
	}
	this.refreshToolbarButtonState();
};
HtmlEditor.prototype._switchFullScreen = function() { 
	if(this.fullScreenMode) { 
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

HtmlEditor.registerCommand = function(command) {
	if(!HtmlEditor.commands) {
		HtmlEditor.commands = [];
	}
	HtmlEditor.commands.push(command);
};
HtmlEditor.prototype.getCommand = function(name) { 
	
	if(HtmlEditor.commands) {
		for(var i=0; i<HtmlEditor.commands.length; i++) {
			if(HtmlEditor.commands[i].name==name) {
				return HtmlEditor.commands[i];
			}
		}
	}
	var command;
	
	if('bold'==name) {
		command = new EditorNamedCommand(name, '\u52A0\u7C97', 19);
	}
	else if('italic'==name) {
		command = new EditorNamedCommand(name, '\u659C\u4F53', 20);
	}
	else if('underline'==name) {
		command = new EditorNamedCommand(name, '\u4E0B\u5212\u7EBF', 21);
	}
	else if('strikeThrough'==name) {
		command = new EditorNamedCommand(name, '\u5220\u9664\u7EBF', 22);
	}
	else if('subscript'==name) {
		command = new EditorNamedCommand(name, '\u4E0B\u6807', 23);
	}
	else if('superscript'==name) {
		command = new EditorNamedCommand(name, '\u4E0A\u6807', 24);
	}
	else if('source'==name) {
		command = new EditorSourceCommand(name, '\u6E90\u4EE3\u7801', 0);
	}
	else if('fitWindow'==name) {
		command = new EditorFitWindowCommand(name, '\u5168\u5C4F\u7F16\u8F91', 65);
	}
	else if('undo'==name) {
		command = new EditorUndoRedoCommand(name, '\u64A4\u9500', 13);
	}
	else if('redo'==name) {
		command = new EditorUndoRedoCommand(name, '\u91CD\u505A', 14);
	}
	else if('copy'==name) {
		command = new EditorNamedCommand(name, '\u62F7\u8D1D', 7);
	}
	else if('cut'==name) {
		command = new EditorNamedCommand(name, '\u526A\u5207', 6);
	}
	else if('paste'==name) {
		command = new EditorNamedCommand(name, '\u7C98\u8D34', 8);
	}
	else if('selectAll'==name) {
		command = new EditorNamedCommand(name, '\u5168\u9009', 17);
	}
	else if('removeFormat'==name) {
		command = new EditorNamedCommand(name, '\u6E05\u9664\u683C\u5F0F', 18);
	}
	else if('justifyLeft'==name) {
		command = new EditorNamedCommand(name, '\u5DE6\u5BF9\u9F50', 29);
	}
	else if('justifyCenter'==name) {
		command = new EditorNamedCommand(name, '\u5C45\u4E2D\u5BF9\u9F50', 30);
	}
	else if('justifyRight'==name) {
		command = new EditorNamedCommand(name, '\u53F3\u5BF9\u9F50', 31);
	}
	else if('justifyFull'==name) {
		command = new EditorNamedCommand(name, '\u4E24\u7AEF\u5BF9\u9F50', 32);
	}
	else if('indent'==name) {
		command = new EditorNamedCommand(name, '\u589E\u52A0\u7F29\u8FDB\u91CF', 28);
	}
	else if('outdent'==name) {
		command = new EditorNamedCommand(name, '\u51CF\u5C11\u7F29\u8FDB\u91CF', 27);
	}
	else if('insertOrderedList'==name) {
		command = new EditorNamedCommand(name, '\u63D2\u5165\u7F16\u53F7\u5217\u8868', 25);
	}
	else if('insertUnorderedList'==name) {
		command = new EditorNamedCommand(name, '\u63D2\u5165\u9879\u76EE\u5217\u8868', 26);
	}
	else if('textColor'==name) {
		command = new EditorTextColorCommand('foreColor', '\u6587\u672C\u989C\u8272', 44);
	}
	else if('bgColor'==name) {
		command = new EditorTextColorCommand('backColor', '\u80CC\u666F\u989C\u8272', 45);
	}
	else if('fontName'==name) {
		command = new EditorFontCommand(name, '\u5B57\u4F53', 76);
	}
	else if('fontSize'==name) {
		command = new EditorFontCommand(name, '\u5B57\u4F53\u5927\u5C0F', 77);
	}
	else if('link'==name) {
		command = new EditorLinkCommand(name, '\u94FE\u63A5/\u9644\u4EF6', 33);
	}
	else if('unlink'==name) {
		command = new EditorNamedCommand(name, '\u53D6\u6D88\u94FE\u63A5', 34);
	}
	else if('image'==name) {
		command = new EditorImageCommand(name, '\u56FE\u7247', 36);
	}
	else if('flash'==name) {
		command = new EditorFlashCommand(name, 'FLASH', 37);
	}
	else if('video'==name) {
		command = new EditorVideoCommand(name, '\u89C6\u9891', 78);
	}
	else if('specialChar'==name) {
		command = new EditorSpecialCharCommand(name, '\u63D2\u5165\u7279\u6B8A\u5B57\u7B26', 41);
	}
	else if('smiley'==name) {
		command = new EditorSmileyCommand(name, '\u63D2\u5165\u8868\u60C5\u56FE\u6807', 40);
	}
	else if('pageBreak'==name) {
		command = new EditorPageBreakCommand(name, '\u63D2\u5165\u5206\u9875\u7B26', 42);
	}
	else if('rule'==name) {
		command = new EditorRuleCommand(name, '\u63D2\u5165\u6C34\u5E73\u7EBF', 39);
	}
	else if('find'==name) {
		command = new EditorFindCommand(name, '\u67E5\u627E/\u66FF\u6362', 15);
	}
	else if('table'==name) {
		command = new EditorTableCommand(name, '\u8868\u683C', 38);
	}
	else if('paragraph'==name) {
		command = new EditorParagraphCommand('paragraph', '\u6BB5\u843D\u683C\u5F0F', 79);
	}
	else if('print'==name) {
		command = new EditorPrintCommand('print', '\u6253\u5370', 11);
	}
	return command;
};
HtmlEditor.prototype.setFontStyle = function(styleName, styleValue) { 
	var range = this.getRange();
	if(range.getBookmark && range.boundingWidth==0) { 
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
	if(range.insertNode && range.startContainer==range.endContainer && range.startOffset==range.endOffset) { 
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
HtmlEditor.prototype.getAttachmentSelectorURL = function(attachmentType, scriptAfterSelect) { //\u83B7\u53D6\u9644\u4EF6\u9009\u62E9URL,scriptAfterSelect\u9ED8\u8BA4\u4E3A:setUrl("{URL}", {WIDTH}, {HEIGHT})
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
HtmlEditor.getDialogArguments = function() { 
	return {editor:HtmlEditor.editor, window:HtmlEditor.editorWindow, document:HtmlEditor.editorDocument, range:HtmlEditor.range, selectedElement:HtmlEditor.selectedElement};
};

EditorNamedCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
};
EditorNamedCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	if("" + editorDocument.queryCommandEnabled(this.name)!="true") { 
		if(!range) {
			return HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
		}
		if('paste'==this.name) { 
			return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
		}
		if(',cut,copy,'.indexOf(',' + this.name + ',')!=-1 && range && range.startOffset!=range.endOffset) { 
			return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
		}
		return HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
	}
	if(',bold,italic,underline,strikeThrough,subscript,superscript,insertOrderedList,insertUnorderedList,'.indexOf(',' + this.name + ',')!=-1) { 
		return "" + editorDocument.queryCommandValue(this.name)=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE : HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	}
	if(this.name.indexOf('justify')==0 && selectedElement) {
		var element = DomUtils.getParentNode(selectedElement, 'p,div,li');
		if(element) {
			var align = element.align ? element.align : element.style.textAlign;
			if(align && (align.toLowerCase()==this.name.substring("justify".length).toLowerCase() || (this.name=='justifyFull' && align.toLowerCase()=='justify'))) {
				return HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE;
			}
		}
	}
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorNamedCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	if(',cut,copy,paste,'.indexOf(',' + this.name + ',')!=-1 && "" + editorDocument.queryCommandEnabled(this.name)!="true") { 
		alert('\u60A8\u7684\u6D4F\u89C8\u5668\u5B89\u5168\u8BBE\u7F6E\u4E0D\u5141\u8BB8\u6267\u884C' + ('cut'==this.name ? '\u526A\u5207' : ('copy'==this.name ? '\u590D\u5236' : '\u7C98\u8D34')) + '\u64CD\u4F5C\uFF0C\u8BF7\u4F7F\u7528\u952E\u76D8\u5FEB\u6377\u952E(Ctrl+' + ('cut'==this.name ? 'X' : ('copy'==this.name ? 'C' : 'V')) + ')\u6765\u5B8C\u6210');
		return;
	}
	if(',cut,copy,paste,'.indexOf(',' + this.name + ',')==-1) {
		editor.saveUndoStep();
	}
	editorDocument.execCommand(this.name, false, null);
};

EditorMenuCommand = function(name, title, iconIndex, iconUrl) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.iconUrl = iconUrl;
	this.showDropButton = true;
	this.width = 150; 
	this.height = 200; 
	this.autoHeight = true; 
};
EditorMenuCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	
};
EditorMenuCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	
};
EditorMenuCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorMenuCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	DomSelection.selectRange(editorWindow, range);
	var items = this.createItems(editor, editorWindow, editorDocument, range, selectedElement);
	if(!items || items.length==0) {
		return;
	}
	var pickerHtml = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">' +
					 '<html>\n' +
					 '	<head>\n' +
					 '		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">\n' +
					 '	</head>\n' +
					 '	<body style="margin:0px; padding:0px; background-color:transparent;">\n' +
					 '		<div id="menuPicker" class="menubar" style="width: ' + (this.width-2) + 'px; position:static; overflow-y:auto; overflow-x:hidden;"/>' +
					 '	</body>\n' +
					 '</html>';
	this.picker = new FormField.Picker(this.title, pickerHtml, toolbarButton, this.width, this.height, false, this.autoHeight, false);
	var pickerDocument = this.picker.pickerFrame.document;
	var menuPicker = pickerDocument.getElementById("menuPicker");
	var command = this;
	
	for(var i=0; i<items.length; i++) {
		var item = pickerDocument.createElement('div');
		item.style.margin = "1px";
		item.className = "menunormal";
		item.id = items[i].id;
		if(items[i].html) {
			item.innerHTML = items[i].html;
		}
		else {
			var html = '<div style="word-break:keep-all; white-space:nowrap; height:16px; line-height:16px;">';
			if(items[i].iconIndex>=0 || items[i].iconUrl) {
				var icon = items[i].iconUrl;
				if(!icon) {
					icon = RequestUtils.getContextPath() + '/jeaf/htmleditor/images/icons.gif,0,' + (16 * items[i].iconIndex);
				}
				var values = icon.split(",");
				html += '<span style="display:inline-block; height:16px; width:20px; overflow:hidden; float:left;">' +
						' <img src="' + values[0] + '"' +
						'  style="' + (values.length<1 ? '' : 'margin-left:-' + values[1] + 'px; margin-top:-' + values[2] + 'px') + '"/>' +
						'</span>';
			}
			item.innerHTML = html + items[i].title + '</div>';
		}
		item.onmouseover = function() {
			this.className = "menuover";
		};
		item.onmouseout = function() {
			this.className = "menunormal";
		};
		item.onclick = function() {
			DomSelection.selectRange(editorWindow, range);
			command.onclick(this.id, editor, editorWindow, editorDocument, range, selectedElement);
			command.picker.destory();
		};
		menuPicker.appendChild(item);
	}
	
	this.picker.show();
	menuPicker.style.height = (DomUtils.getClientHeight(pickerDocument)-2) + "px";
};

HtmlEditor.prototype.onPaste = function(event) { 
	this.pasteRange = this.getRange();
	if(!this.pasteRange) {
		return;
	}
	var editor = this;
	if(event.srcElement && event.srcElement.id=="divClipboardHTML") {
		this.editorWindow.setTimeout(function() {
			editor.retrievePasteHTML();
		}, 1);
		return true;
	}
	
	var div = this.editorDocument.createElement('div');
	div.id = "divClipboardHTML";
	div.style.position = 'absolute';
	div.style.overflow = 'hidden';
	div.style.width = div.style.height = 1;
	div.style.left = div.style.top = -1;
	this.editorDocument.body.appendChild(div);
	div.innerHTML = "&nbsp;";
	
	var range = DomSelection.createRange(this.editorDocument, div);
	if(range.execCommand) { 
		range.execCommand("paste");
		return false;
	}
	else {
		DomSelection.selectRange(this.editorWindow, range);
		this.editorWindow.setTimeout(function() {
			editor.retrievePasteHTML();
		}, 1);
		return true;
	}
};
HtmlEditor.prototype.retrievePasteHTML = function() { 
	var divClipboardHTML = this.editorDocument.getElementById('divClipboardHTML');
	
	for(;;) {
		var divs = divClipboardHTML.getElementsByTagName("div");
		var found = false;
		for(var i=0; i<divs.length; i++) {
			if(divs[i].id!='divClipboardHTML') {
				continue;
			}
			for(var j=divs[i].childNodes.length; j>0; j--) {
				divs[i].parentNode.insertBefore(divs[i].childNodes[0], divs[i]);
			}
			divs[i].parentNode.replaceChild(this.editorDocument.createElement('br'), divs[i]);
			found = true;
			break;
		}
		if(!found) {
			break;
		}
	}
	var html = divClipboardHTML.innerHTML;
	var singleElement = divClipboardHTML.childNodes.length==1 && divClipboardHTML.childNodes[0].childNodes.length==0; 
	divClipboardHTML.innerHTML = "";
	divClipboardHTML.parentNode.removeChild(divClipboardHTML);
	if(!/<(?!br)[^>]*?>/gi.test(html)) { 
		this.doPaste(html, 'pasteAsText');
	}
	else if(singleElement) {
		this.doPaste(html, 'pasteWithoutFilter');
	}
	else {
		var editor = this;
		var isWord = /<\w[^>]*(( class="?MsoNormal"?)|(="mso-))/gi.test(html);
		DialogUtils.openInputDialog('\u7C98\u8D34\u9009\u9879', [{name:'pasteOption', title:'\u7C98\u8D34\u9009\u9879', defaultValue:(isWord ? 'pasteWithFilter' : 'pasteWithoutFilter'), inputMode:'radio', itemsText:'\u9ED8\u8BA4|pasteWithoutFilter\\0\u7C98\u8D34\u4E3A\u65E0\u683C\u5F0F\u6587\u672C|pasteAsText' + (isWord ? '\\0\u6EE4\u9664WORD\u683C\u5F0F|pasteWithFilter' : '')}], 400, 200, '', function(value) {
			editor.doPaste(html, value.pasteOption);
		});
	}
};
HtmlEditor.prototype.doPaste = function(html, pasteOption) { 
	DomSelection.selectRange(this.editorWindow, this.pasteRange);
	this.saveUndoStep();
	html = html.replace(/<\S*\\?\?xml[^>]*>/gi, ""); 
	html = html.replace(/<\/?\w+:[^>]*>/gi, ""); 
	if('pasteWithoutFilter'==pasteOption) {
		this.pasteWithoutFilter(this.pasteRange, html);
	}
	else if('pasteAsText'==pasteOption) {
		this.pasteAsText(this.pasteRange, html);
	}
	else if('pasteWithFilter'==pasteOption) {
		this.pasteWithFilter(this.pasteRange, html);
	}
	this.pasteRange = null;
	this.refreshToolbarButtonState(); 
};
HtmlEditor.prototype.pasteWithoutFilter = function(range, html) { 
	DomSelection.pasteHTML(this.editorWindow, range, html);
};
HtmlEditor.prototype.pasteAsText = function(range, html) { 
	html = html.replace(/<(p|div)[^>]*?>/gi, '<br/>').replace(/<(?!br)[^>]*?>/gi, '').replace(/<br[^>]*?>/gi, '<br/>');
	if(html.substring(0, 5)=='<br/>') {
		html = html.substring(5);
	}
	if(html.indexOf('<br/>')!=-1) {
		html = '<p>' + html.replace(/<br\/>/gi, '</p>\r\n<p>') + '</p>';
	}
	DomSelection.pasteHTML(this.editorWindow, range, html);
};
HtmlEditor.prototype.pasteWithFilter = function(range, html) { 
	var bIgnoreFont = true;
	var bRemoveStyles = true;
	
	html = html.replace(/\s*mso-[^:]+:[^;"]+;?/gi, "") ;
	
	html = html.replace(/\s*MARGIN: 0cm 0cm 0pt\s*;/gi, "") ;
	html = html.replace(/\s*MARGIN: 0cm 0cm 0pt\s*"/gi, "\"");
	html = html.replace(/\s*MARGIN:.?-.*?;/gi, "") ;
	html = html.replace(/\s*MARGIN:.?-.*?"/gi, "\"") ;
	html = html.replace(/\s*MARGIN-LEFT:.?-.*?;/gi, "") ;
	html = html.replace(/\s*MARGIN-LEFT:.?-.*?"/gi, "\"") ;
	html = html.replace(/\s*TEXT-INDENT: 0cm\s*;/gi, "");
	html = html.replace(/\s*TEXT-INDENT: 0cm\s*"/gi, "\"");
	
	html = html.replace(/\s*TEXT-INDENT:.?-.*?;/gi, "");
	html = html.replace(/\s*TEXT-INDENT:.?-.*?"/gi, "\"");
	//html = html.replace(/\s*TEXT-ALIGN: [^\s;]+;?"/gi, "\"");
	html = html.replace(/\s*PAGE-BREAK-BEFORE: [^\s;]+;?"/gi, "\"");
	html = html.replace(/\s*FONT-VARIANT: [^\s;]+;?"/gi, "\"");
	html = html.replace(/\s*tab-stops:[^;"]*;?/gi, "");
	html = html.replace(/\s*tab-stops:[^"]*/gi, "");
	
	if(bIgnoreFont) {
		html = html.replace(/\s*face="[^"]*"/gi, "");
		html = html.replace(/\s*face=[^ >]*/gi, "");
		html = html.replace(/\s*FONT-FAMILY:[^;"]*;?/gi, "");
		html = html.replace(/\s*FONT-SIZE:[^;"]*;?/gi, ""); 
	}
	
	
	html = html.replace(/<(\w[^>]*) class=([^ |>]*)([^>]*)/gi, "<$1$3") ;
	
	
	//	html = html.replace(/<(\w[^>]*) style="([^\"]*)"([^>]*)/gi, "<$1$3");
	
	html =  html.replace(/\s*style="\s*"/gi, '');
	html = html.replace(/<SPAN\s*[^>]*>\s*&nbsp;\s*<\/SPAN>/gi, '&nbsp;');
	html = html.replace(/<SPAN\s*[^>]*><\/SPAN>/gi, '');
	
	
	html = html.replace(/<(\w[^>]*) lang=([^ |>]*)([^>]*)/gi, "<$1$3") ;
	html = html.replace(/<SPAN\s*>([.\d]*?)<\/SPAN>/gi, '$1'); 
	html = html.replace(/<FONT\s*>(.*?)<\/FONT>/gi, '$1');
	html = html.replace(/<INS\s*[^>]*>(.*?)<\/INS>/gi, '$1');
	
	if(bIgnoreFont) {
		html = html.replace(/<H\d([^>]*)>(.*?)<\/H\d>/gi, '<div$1>$2</div>') ;
	}
	else {
		html = html.replace(/<H1([^>]*)>/gi, '<div$1><b><font size="6">');
		html = html.replace(/<H2([^>]*)>/gi, '<div$1><b><font size="5">');
		html = html.replace(/<H3([^>]*)>/gi, '<div$1><b><font size="4">');
		html = html.replace(/<H4([^>]*)>/gi, '<div$1><b><font size="3">');
		html = html.replace(/<H5([^>]*)>/gi, '<div$1><b><font size="2">');
		html = html.replace(/<H6([^>]*)>/gi, '<div$1><b><font size="1">');
		html = html.replace(/<\/H\d>/gi, '<\/font><\/b><\/div>' );
		html = html.replace(/<H\d*>/gi, '') ;
		html = html.replace(/<\/H\d>/gi, '') ;
	}
	html = html.replace(/<(U|I|STRIKE)>&nbsp;<\/\1>/g, '&nbsp;');
	
	html = html.replace(/<([^\s>]+)[^>]*>\s*<\/\1>/g, '');
	html = html.replace(/<([^\s>]+)[^>]*>\s*<\/\1>/g, '');
	html = html.replace(/<([^\s>]+)[^>]*>\s*<\/\1>/g, '');
	
	
	html = html.replace(/\s*PADDING-LEFT:[^;"]*;?/gi, "");
	html = html.replace(/\s*PADDING-RIGHT:[^;"]*;?/gi, "");
	html = html.replace(/\s*PADDING-TOP:[^;"]*;?/gi, "");
	html = html.replace(/\s*PADDING-BOTTOM:[^;"]*;?/gi, "");
	
	
	//html = html.replace(/\s*MARGIN-LEFT:[^;"]*;?/gi, "");
	//html = html.replace(/\s*MARGIN-RIGHT:[^;"]*;?/gi, "");
	//html = html.replace(/\s*MARGIN-TOP:[^;"]*;?/gi, "");
	//html = html.replace(/\s*MARGIN-BOTTOM:[^;"]*;?/gi, "");
	
	//html = html.replace(/\s*TEXT-INDENT:[^;"]*;?/gi, "");
	
	//\u6E05\u9664WPS,<IMG alt="" src="file:///C:/Users/lenovo/AppData/Local/Temp/ksohtml/wpsBDBF.tmp.png" width=565 height=2>
	html = html.replace(/<IMG([^>]*) src="file:\/\/\/([^>]*)>/gi, '');
	
	
	var re = new RegExp("(<P)([^>]*>.*?)(<\/P>)","gi") ;	
	html = html.replace(re, "<div$2<\/div>" );
	DomSelection.pasteHTML(this.editorWindow, range, html);
};

HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED = -1; 
HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL = 0; 
HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE = 1; 
HtmlEditor.prototype.createToolbar = function(toolbarContainer) { 
	var toolbarSet = document.createElement("div");
	toolbarSet.className = "toolbarSet";
	toolbarSet.onselectstart = function() {
		return false;
	};
	toolbarContainer.appendChild(toolbarSet);
	this.toolbarButtons = [];
	var toolbarGroups = this.commands.split(';');
	for(var i=0; i<toolbarGroups.length; i++) {
		var toolbarGroup = document.createElement("div");
		toolbarSet.appendChild(toolbarGroup);
		var toolbars = toolbarGroups[i].split(/\|/);
		for(var j=0; j<toolbars.length; j++) {
			var toolbar = document.createElement("span");
			toolbar.className = "toolbar";
			var separatorLine = document.createElement("span");
			separatorLine.style.float = "left";
			separatorLine.className = 'toolbarSeparatorLine';
			toolbar.appendChild(separatorLine);
			toolbarGroup.appendChild(toolbar);
			
			var buttons = toolbars[j].split(',');
			var previousButtonName = '-';
			for(var k=0; k<buttons.length; k++) {
				if(buttons[k]=='-' && previousButtonName=='-') { 
					continue;
				}
				var toolbarButton = this.createToolbarButton(toolbar, buttons[k]); 
				if(toolbarButton) {
					this.toolbarButtons.push(toolbarButton);
				}
				if(buttons[k]=='-' || toolbarButton) {
					previousButtonName = buttons[k];
				}
			}
			if(previousButtonName=='-') {
				if(toolbar.childNodes.length<=1) {
					toolbar.parentNode.removeChild(toolbar);
				}
				else {
					toolbar.removeChild(toolbar.childNodes[toolbar.childNodes.length-1]);
				}
			}
			if(toolbar.childNodes.length==0) {
				toolbarGroup.removeChild(toolbar);
			}
		}
		if(toolbarGroup.childNodes.length==0) {
			toolbarSet.removeChild(toolbarGroup);
		}
	}
};
HtmlEditor.prototype.createToolbarButton = function(toolbarElement, buttonName) { 
	if(buttonName=='-') { 
		var separator = document.createElement("span");
		separator.className = "toolbarButtonSeparatorBar";
		toolbarElement.appendChild(separator);
		var separatorLine = document.createElement("span");
		separatorLine.style.float = "left";
		separatorLine.className = 'toolbarButtonSeparatorLine';
		separator.appendChild(separatorLine);
		return;
	}
	var command = this.getCommand(buttonName);
	if(!command) {
		return null;
	}
	var editor = this;
	var toolbarButton = document.createElement("span");
	toolbarButton.title = command.title;
	toolbarButton.style.display = 'inline-block';
	toolbarElement.appendChild(toolbarButton);
	if(command.create) {
		command.create(toolbarButton);
		return toolbarButton;
	}
	toolbarButton.command = command;
	toolbarButton.className = "toolbarButton";
	toolbarButton.onmouseover = function() { 
		if(this.className.indexOf('toolbarButtonDisabled')==-1) {
			this.className += ' toolbarButtonOver';
		}
	};
	toolbarButton.onmouseout = function() { 
		if(this.className.indexOf('toolbarButtonDisabled')==-1) {
			this.className = this.className.replace(' toolbarButtonOver', '');
		}
	};
	toolbarButton.onmousedown = function() { 
		if(this.className.indexOf('toolbarButtonDisabled')!=-1) {
			return;
		}
		var button = this;
		var range = editor.getRange();
		var selectedElement = DomSelection.getSelectedElement(range);
		HtmlEditor.editor = editor;
		HtmlEditor.editorWindow = editor.editorWindow;
		HtmlEditor.editorDocument = editor.editorDocument;
		HtmlEditor.range = range;
		HtmlEditor.selectedElement = selectedElement;
		editor.editorWindow.setTimeout(function() {
			try {
				DomSelection.selectRange(editor.editorWindow, range);
			}
			catch(e) {
			
			}
			command.execute(button, editor, editor.editorWindow, editor.editorDocument, range, selectedElement);
			editor.refreshToolbarButtonState(); 
		}, 10);
	};
	
	if(command.iconUrl || command.iconIndex>=0) {
		var icon = command.iconUrl;
		if(!icon) {
			icon = RequestUtils.getContextPath() + '/jeaf/htmleditor/images/icons.gif,0,' + (16 * command.iconIndex);
		}
		var values = icon.split(",");
		var img = document.createElement("img");
		img.border = 0;
		img.src = values[0];
		if(values.length>1) {
			img.style.marginLeft = "-" + values[1] + "px";
			img.style.marginTop = "-" + values[2] + "px";
		}
		var iconSpan = document.createElement("span");
		iconSpan.className = "toolbarButtonIcon";
		iconSpan.style.overflow = "hidden";
		iconSpan.appendChild(img);
		toolbarButton.appendChild(iconSpan);
	}
	
	if(command.showTitle) { 
		var titleSpan = document.createElement("span");
		titleSpan.innerHTML = command.title;
		titleSpan.className = 'toolbarButtonTitle';
		toolbarButton.appendChild(titleSpan);
	}
	
	if(command.showDropButton) { 
		var img = document.createElement("img");
		img.border = 0;
		img.src = RequestUtils.getContextPath() + '/jeaf/htmleditor/images/dropdown.gif';
		var dropButton = document.createElement("span");
		dropButton.className = "toolbarButtonDropButton";
		dropButton.appendChild(img);
		toolbarButton.appendChild(dropButton);
		
	}
	return toolbarButton;
};
HtmlEditor.prototype.refreshToolbarButtonState = function() { 
	var editor = this;
	window.setTimeout(function() {
		editor._doRefreshToolbarButtonState();
	}, 100);
}
HtmlEditor.prototype._doRefreshToolbarButtonState = function() { 
	var range = this.getRange();
	var selectedElement = DomSelection.getSelectedElement(range);
	for(var i=0; i<this.toolbarButtons.length; i++) {
		var state = HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
		if(!this.toolbarButtons[i].command) {
			continue;
		}
		if(!this.sourceCodeMode || this.toolbarButtons[i].command.enabledInSourceCodeMode) { 
			try {
				state = this.toolbarButtons[i].command.getState(this, this.sourceCodeMode, this.editorWindow, this.editorDocument, range, selectedElement);
			}
			catch(e) {
				
			}
		}
		var className;
		if(state==HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED) { 
			className = "toolbarButton toolbarButtonDisabled";
		}
		else if(state==HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL) { 
			className = "toolbarButton";
		}
		else if(state==HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE) { 
			className = "toolbarButton toolbarButtonActive";
		}
		if(this.toolbarButtons[i].className!=className) {
			this.toolbarButtons[i].className = className;
		}
	}
};

var MAX_UNDO_STEP_COUNT = 10; 
HtmlEditor.prototype.saveUndoStep = function() { 
	var html = this.getHTML();
	var range = this.getRange();
	this.undoStepIndex++;
	if(this.undoStepIndex <= MAX_UNDO_STEP_COUNT) { 
		this.undoSteps = this.undoSteps.slice(0, this.undoStepIndex);
	}
	else {
		this.undoSteps = this.undoSteps.slice(1, this.undoStepIndex);
		this.undoStepIndex--;
	}
	var undoStep;
	if(!range) {
		undoStep = {html:html};
	}
	else {
		undoStep = {html:html, bookmark: DomSelection.getRangeBookmark(range)};
	}
	undoStep.scrollLeft = this.editorDocument.body.scrollLeft;
	undoStep.scrollTop = this.editorDocument.body.scrollTop;
	this.undoSteps.push(undoStep);
	var editor = this;
	window.setTimeout(function() {
		editor._resetElements();
	}, 100);
};
HtmlEditor.prototype._isAllowUndo = function() { 
	return this.undoStepIndex >= 0;
};
HtmlEditor.prototype._isAllowRedo = function() { 
	return this.undoStepIndex < this.undoSteps.length - 2;
};
HtmlEditor.prototype.undo = function() { 
	if(!this._isAllowUndo()) {
		return;
	}
	if(this.undoStepIndex==this.undoSteps.length-1) {
		this.saveUndoStep();
		this.undoStepIndex--;
	}
	this._applyUndoStep(this.undoStepIndex);
	this.undoStepIndex--;
};
HtmlEditor.prototype.redo = function() { 
	if(!this._isAllowRedo()) {
		return;
	}
	this._applyUndoStep(this.undoStepIndex + 2);
	this.undoStepIndex++;
};
HtmlEditor.prototype._applyUndoStep = function(stepIndex) { 
	var undoStep = this.undoSteps[stepIndex];
	this.iframe.style.display = 'none';
	this.setHTML(undoStep.html);
	this.iframe.style.display = '';
	this.editorDocument.body.scrollLeft = undoStep.scrollLeft;
	this.editorDocument.body.scrollTop = undoStep.scrollTop;
	if(undoStep.bookmark) {
		var range = DomSelection.createRangeByBookmark(this.editorDocument, undoStep.bookmark);
		DomSelection.selectRange(this.editorWindow, range);
	}
};

EditorDialogCommand = function(name, title, showTitle, iconIndex, iconUrl, dialogURL, dialogWidth, dialogHeight) {
	this.name = name;
	this.title = title;
	this.showTitle = showTitle;
	this.iconIndex = iconIndex;
	this.iconUrl = iconUrl;
	this.dialogURL = dialogURL;
	this.dialogWidth = dialogWidth;
	this.dialogHeight = dialogHeight;
};
EditorDialogCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorDialogCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	DialogUtils.openDialog(this.dialogURL, this.dialogWidth, this.dialogHeight, '', HtmlEditor.getDialogArguments());
};

EditorFindCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, false, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/find.shtml', 430, 200);
};
EditorFindCommand.prototype = new EditorDialogCommand;
EditorFindCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};

EditorFitWindowCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.enabledInSourceCodeMode = true; 
};
EditorFitWindowCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return editor.fullScreenMode ? HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE : HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorFitWindowCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	editor._switchFullScreen(); 
};

EditorFlashCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, false, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/flash.shtml', 430, 350);
};
EditorFlashCommand.prototype = new EditorDialogCommand;
EditorFlashCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return "" + editorDocument.queryCommandEnabled("InsertImage") == "true" || (selectedElement && selectedElement.tagName=="EMBED") ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};

EditorFontCommand = function(name, title, iconIndex) {
	EditorMenuCommand.call(this, name, title, iconIndex);
	this.width = 160; 
	this.height = 200; 
	this.autoHeight = true; 
};
EditorFontCommand.prototype = new EditorMenuCommand;
EditorFontCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	var items = [];
	if(this.name=="fontName") {
		var value = CssUtils.getElementComputedStyle(selectedElement, "font-family");
		var fontNames = '\u5B8B\u4F53,\u9ED1\u4F53,\u5FAE\u8F6F\u96C5\u9ED1,\u6977\u4F53_GB2312,\u4EFF\u5B8B_GB2312,\u96B6\u4E66,\u5E7C\u5706,\u65B0\u5B8B\u4F53,\u7EC6\u660E\u4F53,Arial,Comic Sans MS,Courier New,Tahoma,Times New Roman,Verdana'.split(',');
		for(var i=0; i<fontNames.length; i++) {
			var html = '<span style="display:inline-block; padding-left: 22px; background-repeat:no-repeat; background-position: center left;' + (value==fontNames[i] ? ' background-image:url(' + RequestUtils.getContextPath() + '/jeaf/htmleditor/images/checked.gif)' : '') + '">' +
					   '	<font style="font-family:' + fontNames[i] + '">' + fontNames[i] + '</font>' +
					   '</span>';
			items.push({id:fontNames[i], html:html});
		}
	}
	else if(this.name=="fontSize") {
		var value = CssUtils.getElementComputedStyle(selectedElement, "font-size");
		var fontSizes = '8,10,11,12,13,14,16,18,20,24,28,32,36,48,64'.split(',');
		for(var i=0; i<fontSizes.length; i++) {
			var html = '<span style="display:inline-block; padding-left: 22px; background-repeat:no-repeat; background-position: center left;' + (value==fontSizes[i] + "px" ? ' background-image:url(' + RequestUtils.getContextPath() + '/jeaf/htmleditor/images/checked.gif)' : '') + '">' +
					   '	<font style="font-size:' + fontSizes[i] + 'px">' + fontSizes[i] + 'px</fonr>' +
					   '</span>';
			items.push({id:fontSizes[i], html:html});
		}
	}
	return items;
};
EditorFontCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	editor.saveUndoStep();
	if(this.name=="fontName") {
		editor.setFontStyle('fontFamily', itemId);
	}
	else if(this.name=="fontSize") {
		editor.setFontStyle('fontSize', itemId + 'px');
	}
};
EditorFontCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return "" + editorDocument.queryCommandEnabled(this.name)=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};

EditorImageCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, true, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/image.shtml', 720, 600);
};
EditorImageCommand.prototype = new EditorDialogCommand;
EditorImageCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return "" + editorDocument.queryCommandEnabled("InsertImage") == "true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};

EditorLinkCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, true, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/link.shtml', 430, 230);
};
EditorLinkCommand.prototype = new EditorDialogCommand;
EditorLinkCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	var ret = range && range.queryCommandEnabled ? range.queryCommandEnabled("createLink") : editorDocument.queryCommandEnabled("createLink");
	return "" + ret == "true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};

EditorPageBreakCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
};
EditorPageBreakCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return "" + editorDocument.queryCommandEnabled("InsertParagraph") == "true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorPageBreakCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	editor.saveUndoStep();
	DomSelection.pasteHTML(editorWindow, range, '<div style="page-break-after: always"></div>');
};

EditorParagraphCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showTitle = true;
};
EditorParagraphCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return selectedElement && (selectedElement.getElementsByTagName('p')[0] || DomUtils.getParentNode(selectedElement, 'P')) ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorParagraphCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	var paragraphs = this._listSelectedParagraphs(editorDocument, range, 0);
	if(!paragraphs) {
		return;
	}
	var textIndent = {name:'textIndent', title:'\u9996\u884C\u7F29\u8FDB(\u5B57\u6570)', inputMode:'text', defaultValue:this._parseWordCount(paragraphs[0].style.textIndent)};
	var alignMode = {name:'alignMode', title:'\u5BF9\u9F50\u65B9\u5F0F', inputMode:'radio', defaultValue:paragraphs[0].align, itemsText:'\u5C45\u5DE6|left\\0\u5C45\u4E2D|center\\0\u5C45\u53F3|right'};
	var marginTop = {name:'marginTop', title:'\u6BB5\u524D\u95F4\u8DDD(\u5B57\u6570)', inputMode:'text', defaultValue:this._parseWordCount(paragraphs[0].style.marginTop)};
	var marginBottom = {name:'marginBottom', title:'\u6BB5\u540E\u95F4\u8DDD(\u5B57\u6570)', inputMode:'text', defaultValue:this._parseWordCount(paragraphs[0].style.marginBottom)};
	var lineHeight = {name:'lineHeight', title:'\u884C\u9AD8(\u5B57\u6570)', inputMode:'text', defaultValue:this._parseWordCount(paragraphs[0].style.lineHeight)};
	DialogUtils.openInputDialog('\u6BB5\u843D\u683C\u5F0F', [textIndent, alignMode, marginTop, marginBottom, lineHeight], 430, 300, '', function(values) {
		editor.saveUndoStep();
		for(var i=0; i<paragraphs.length; i++) {
			
			var value = Number(values.textIndent);
			paragraphs[i].style.textIndent = isNaN(value) || value<0 ? '' : value + "em";
			
			paragraphs[i].align = values.alignMode;
			
			value = Number(values.marginTop);
			paragraphs[i].style.marginTop = isNaN(value) || value<0 ? '' : value + "em";
			
			value = Number(values.marginBottom);
			paragraphs[i].style.marginBottom = isNaN(value) || value<0 ? '' : value + "em";
			
			value = Number(values.lineHeight);
			paragraphs[i].style.lineHeight = isNaN(value) || value<=0 ? '' : value + "em";
		}
	});
};
EditorParagraphCommand.prototype._parseWordCount = function(style) {
	if(!style || style.indexOf('em')==-1) {
		return "";
	}
	return Number(style.replace('em', ''));
};
EditorParagraphCommand.prototype._listSelectedParagraphs = function(editorDocument, range, max) {
	var paragraphs = editorDocument.getElementsByTagName('p');
	if(paragraphs.length==0) {
		return null;
	}
	var selectedParagraphs = [];
	for(var i=0; i<paragraphs.length && (max==0 || selectedParagraphs.length<max); i++) {
		if(DomSelection.inRange(range, paragraphs[i])) { 
			selectedParagraphs.push(paragraphs[i]);
		}
	}
	return selectedParagraphs.length==0 ? null : selectedParagraphs;
};

EditorPrintCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
};
EditorPrintCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorPrintCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	editorDocument.body.focus();
	editorWindow.print();
};

EditorRuleCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
};
EditorRuleCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return "" + editorDocument.queryCommandEnabled("InsertParagraph") == "true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorRuleCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	editor.saveUndoStep();
	DomSelection.pasteHTML(editorWindow, range, "<hr/>");
};

EditorSmileyCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showDropButton = true;
};
EditorSmileyCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return "" + editorDocument.queryCommandEnabled("InsertImage")=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorSmileyCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	var smileys = ('\u5FAE\u7B11|weixiao.gif,\u50B2\u6162|aomang.gif,\u767D\u773C|baiyan.gif,\u522B\u5634|biezui.gif,\u95ED\u5634|bizui.gif,\u64E6\u6C57|cahan.gif,\u5927\u5175|dabing.gif,\u5927\u54ED|daku.gif,\u5F97\u610F|deyi.gif,\u53D1\u5446|fadai.gif,' +
				  '\u53D1\u6012|fanu.gif,\u5C34\u5C2C|ganga.gif,\u5BB3\u7F9E|haixiu.gif,\u61A8\u7B11|hanxiao.gif,\u9965\u997F|jie.gif,\u60CA\u6050|jingkong.gif,\u60CA\u8BB6|jingya.gif,\u53EF\u7231|keai.gif,\u6263\u9F3B|koubi.gif,\u9177|ku.gif,' +
				  '\u56F0|kun.gif,\u51B7\u6C57|lenghan.gif,\u54A7\u7259|lieya.gif,\u6D41\u6C57|liuhan.gif,\u6D41\u6CEA|liulei.gif,\u96BE\u8FC7|nanguo.gif,\u8272|se.gif,\u8870|shuai.gif,\u7761|shui.gif,\u8C03\u76AE|tiaopi.gif,' +
				  '\u5077\u7B11|touxiao.gif,\u5410|tu.gif,\u6572\u6253|qiaoda.gif,\u518D\u89C1|zaijian.gif,\u6293\u72C2|zhuakuang.gif').split(",");
	var pickerHtml = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">' +
					 '<html>\n' +
					 '	<head>\n' +
					 '		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">\n' +
					 '	</head>\n' +
					 '	<body style="margin:0px; padding:0px; border-style:none;">\n' +
					 '		<div id="smileyPicker" class="listbar" style="position:static;"/>' +
					 '	</body>\n' +
					 '</html>';
	this.picker = new FormField.Picker(this.title, pickerHtml, toolbarButton, 212, 180, false, true, false);
	var pickerDocument = this.picker.pickerFrame.document;
	var smileyPicker = pickerDocument.getElementById("smileyPicker");
	var command = this;
	for(var i=0; i<smileys.length; i++) {
		if(i!=0 && i%7==0) {
			var br = pickerDocument.createElement('br');
			br.style.clear = 'both';
			smileyPicker.appendChild(br);
		}
		var smileyBox = pickerDocument.createElement('span');
		smileyBox.style.width = '24px';
		smileyBox.style.height = '24px';
		smileyBox.style.display = 'inline-block';
		smileyBox.style.margin = '1px';
		smileyBox.style.padding = '1px';
		smileyBox.style.border = '#fff 1px solid';
		smileyBox.style.textAlign = 'center';
		smileyBox.onmouseover = function() {
			this.style.borderColor = '#555555';
			this.style.backgroundColor = '#e0e0e8';
		};
		smileyBox.onmouseout = function() {
			this.style.borderColor = '#fff';
			this.style.backgroundColor = 'transparent';
		};
		smileyBox.onclick = function() {
			editor.saveUndoStep();
			var random = Math.random();
			DomSelection.pasteHTML(editorWindow, range, '<img id="' + random + '"/>');
			var img = editorDocument.getElementById(random);
			img.removeAttribute('id');
			img.src = this.getElementsByTagName('img')[0].getAttribute('src', 2);
			command.picker.destory();
		};
		smileyPicker.appendChild(smileyBox);
		
		var values = smileys[i].split("\|");
		var image = pickerDocument.createElement('img');
		image.src = RequestUtils.getContextPath() + '/jeaf/htmleditor/images/expression/' + values[1];
		smileyBox.title = values[0];
		smileyBox.appendChild(image);
	}
	
	this.picker.show();
};

EditorSourceCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showTitle = true;
	this.enabledInSourceCodeMode = true; 
};
EditorSourceCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return sourceCodeMode ? HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE : HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorSourceCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	editor._switchEditMode(); 
};

EditorSpecialCharCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showDropButton = true;
};
EditorSpecialCharCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return "" + editorDocument.queryCommandEnabled("InsertImage")=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorSpecialCharCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	var chars = "&asymp;&equiv;&ne;\uFF1D&le;&ge;\u226E\u226F\u2237&plusmn;\uFF0B\uFF0D&times;&divide;\uFF0F&int;\u222E&prop;&infin;&and;&or;&sum;&prod;&cup;&cap;&isin;\u2235&there4;&perp;\u2016&ang;\u2312\u2299\u224C\u223D&radic;&alpha;&beta;&gamma;&delta;&epsilon;&zeta;&eta;&theta;&iota;&kappa;&lambda;&mu;&nu;&xi;&omicron;&pi;&rho;&sigma;&tau;&upsilon;&phi;&chi;&psi;&omega;\u3014\u3015\u300A\u300B\u300C\u300D\u300E\u300F\u3016\u3017\u3010\u3011\u3014\u3015\u2160\u2161\u2162\u2163\u2164\u2165\u2166\u2167\u2168\u2169\u216A\u216B\u2460\u2461\u2462\u2463\u2464\u2465\u2466\u2467\u2468\u2469&deg;&prime;\u3003\uFF04\uFFE1\uFFE5&permil;\u2103&curren;\uFFE0&sect;\u2116\u2606\u2605\u25CB\u25CF\u25CE\u25C7\u25C6\u25A1\u25A0\u25B3\u25B2\u203B&rarr;&larr;&uarr;&darr;\u2640\u2642&copy;&reg;";
	var pickerHtml = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">' +
					 '<html>\n' +
					 '	<head>\n' +
					 '		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">\n' +
					 '	</head>\n' +
					 '	<body style="margin:0px; padding:0px; border-style:none;">\n' +
					 '		<div id="charPicker" class="listbar" style="position:static;"/>' +
					 '	</body>\n' +
					 '</html>';
	this.picker = new FormField.Picker(this.title, pickerHtml, toolbarButton, 162, 180, false, true, false);
	var pickerDocument = this.picker.pickerFrame.document;
	var charPicker = pickerDocument.getElementById("charPicker");
	
	var command = this;
	var index = 0;
	for(var i=0; i<chars.length; i++) {
		if(index!=0 && index%8==0) {
			var br = pickerDocument.createElement('br');
			br.style.clear = 'both';
			charPicker.appendChild(br);
		}
		var html = chars.charAt(i);
		if(html=="&") {
			var k = i;
			i = chars.indexOf(';', i);
			html = chars.substring(k, i+1);
		}
		index++;
		var charBox = pickerDocument.createElement('span');
		charBox.style.width = '14px';
		charBox.style.height = '14px';
		charBox.style.display = 'inline-block';
		charBox.style.margin = '1px';
		charBox.style.padding = '1px';
		charBox.style.border = '#fff 1px solid';
		charBox.style.textAlign = 'center';
		charBox.style.fontFamily = '\u5B8B\u4F53,Batang,Courier New';
		charBox.style.fontSize = '13px';
		charBox.innerHTML = html;
		charBox.id = html;
		charBox.onmouseover = function() {
			this.style.borderColor = '#555555';
			this.style.backgroundColor = '#e0e0e8';
		};
		charBox.onmouseout = function() {
			this.style.borderColor = '#fff';
			this.style.backgroundColor = 'transparent';
		};
		charBox.onclick = function() {
			editor.saveUndoStep();
			DomSelection.pasteHTML(editorWindow, range, this.id);
			command.picker.destory();
		};
		charPicker.appendChild(charBox);
	}
	
	this.picker.show();
};

EditorTableCommand = function(name, title, iconIndex) {
	EditorMenuCommand.call(this, name, title, iconIndex);
	this.width = 120; 
	this.height = 200; 
	this.autoHeight = true; 
	this.showDropButton = false;
};
EditorTableCommand.prototype = new EditorMenuCommand;
EditorTableCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	if(DomUtils.getParentNode(selectedElement, 'td')) {
		this.constructor.prototype.execute.call(this, toolbarButton, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else {
		this.openTableDialog(true, editor, editorWindow, editorDocument, range, selectedElement);
	}
};
EditorTableCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	var items = [{id:'insertTable', title:'\u63D2\u5165\u8868\u683C', iconIndex:80},
				 {id:'tableProperty', title:'\u8868\u683C\u5C5E\u6027', iconIndex:38},
				 {id:'tableCellProperty', title:'\u5355\u5143\u683C\u5C5E\u6027', iconIndex:56},
				 {id:'tableInsertRowAfter', title:'\u4E0B\u63D2\u5165\u884C', iconIndex:61},
				 {id:'tableInsertRowBefore', title:'\u4E0A\u63D2\u5165\u884C', iconIndex:69},
				 {id:'tableDeleteRows', title:'\u5220\u9664\u884C', iconIndex:62},
				 {id:'tableInsertColumnAfter', title:'\u53F3\u63D2\u5165\u5217', iconIndex:63},
				 {id:'tableInsertColumnBefore', title:'\u5DE6\u63D2\u5165\u5217', iconIndex:70},
				 {id:'tableDeleteColumns', title:'\u5220\u9664\u5217', iconIndex:64}];
	var tableCell = DomUtils.getParentNode(selectedElement, 'td');
	var table = tableCell ? DomUtils.getParentNode(tableCell, 'table') : null;
	if(tableCell.cellIndex!=tableCell.parentNode.cells.length-1) {
		items.push({id:'tableMergeRight', title:'\u53F3\u5408\u5E76\u5355\u5143\u683C', iconIndex:59});
	}
	if(tableCell.parentNode.rowIndex!=table.rows.length-1) {
		items.push({id:'tableMergeDown', title:'\u4E0B\u5408\u5E76\u5355\u5143\u683C', iconIndex:59});
	}
	if(tableCell.colSpan>1 || tableCell.rowSpan>1) {
		items.push({id:'tableSplitCell', title:'\u62C6\u5206\u5355\u5143\u683C', iconIndex:60});
	}
	items.push({id:'tableDelete', title:'\u5220\u9664\u8868\u683C', iconIndex:81});
	return items;
};
EditorTableCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorTableCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	var tableCell = DomUtils.getParentNode(selectedElement, 'td');
	var table = tableCell ? DomUtils.getParentNode(tableCell, 'table') : null;
	if(itemId=='insertTable') { 
		this.openTableDialog(true, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else if(itemId=='tableProperty') { 
		this.openTableDialog(false, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else if(itemId=='tableCellProperty') { 
		DialogUtils.openDialog(RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/tableCell.shtml', 430, 300, '', HtmlEditor.getDialogArguments());
	}
	else if(itemId=='tableInsertRowAfter') { 
		this.insertRow(editor, table, tableCell, false);
	}
	else if(itemId=='tableInsertRowBefore') { 
		this.insertRow(editor, table, tableCell, true);
	}
	else if(itemId=='tableDeleteRows') { 
		editor.saveUndoStep();
		if(table.rows.length>1) {
			table.deleteRow(tableCell.parentNode.rowIndex);
		}
		else {
			table.parentNode.removeChild(table);
		}
	}
	else if(itemId=='tableInsertColumnAfter') { 
		this.insertColumn(editor, table, tableCell, false);
	}
	else if(itemId=='tableInsertColumnBefore') { 
		this.insertColumn(editor, table, tableCell, true);
	}
	else if(itemId=='tableDeleteColumns') { 
		editor.saveUndoStep();
		if(tableCell.parentNode.cells.length==1) {
			table.parentNode.removeChild(table);
		}
		else {
			var cellIndex = tableCell.cellIndex;
			for(var i=0; i<table.rows.length; i++) {
				table.rows[i].deleteCell(cellIndex);
			}
		}
	}
	else if(itemId=='tableMergeRight') { 
		editor.saveUndoStep();
		try {
			for(var i=0; i<tableCell.rowSpan; i++) {
				var row = table.rows[tableCell.parentNode.rowIndex + i];
				var cell = row.cells[tableCell.cellIndex + (i==0 ? 1 : 0)];
				for(var j=cell.childNodes.length; j>0; j--) {
					tableCell.appendChild(cell.childNodes[0]);
				}
				i += cell.rowSpan - 1;
				row.deleteCell(cell.cellIndex);
			}
			tableCell.colSpan++;
		}
		catch(e) {
		
		}
	}
	else if(itemId=='tableMergeDown') { 
		editor.saveUndoStep();
		try {
			var row = table.rows[tableCell.parentNode.rowIndex + tableCell.rowSpan];
			for(var i=0; i<tableCell.colSpan; i++) {
				var cell = row.cells[tableCell.cellIndex];
				for(var j=cell.childNodes.length; j>0; j--) {
					tableCell.appendChild(cell.childNodes[0]);
				}
				i += cell.colSpan - 1;
				row.deleteCell(cell.cellIndex);
			}
			tableCell.rowSpan++;
		}
		catch(e) {
		
		}
	}
	else if(itemId=='tableSplitCell') { 
		editor.saveUndoStep();
		for(var i=0; i<tableCell.rowSpan; i++) {
			var row = table.rows[tableCell.parentNode.rowIndex + i];
			for(var j=(i==0 ? 1 : 0); j<tableCell.colSpan; j++) {
				row.insertCell(tableCell.cellIndex + j);
			}
		}
		tableCell.rowSpan = 1;
		tableCell.colSpan = 1;
	}
	else if(itemId=='tableDelete') { 
		editor.saveUndoStep();
		table.parentNode.removeChild(table);
	}	
};
EditorTableCommand.prototype.openTableDialog = function(isNew, editor, editorWindow, editorDocument, range, selectedElement) { 
	DialogUtils.openDialog(RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/table.shtml?newTable=' + isNew, 430, 300, '', HtmlEditor.getDialogArguments());
};
EditorTableCommand.prototype.insertRow = function(editor, table, tableCell, insertBefore) { 
	editor.saveUndoStep();
	var currentRow = tableCell.parentNode;
	var newRow = table.insertRow(currentRow.rowIndex + (insertBefore ? 0 : 1));
	for(var i=0; i<currentRow.cells.length; i++) {
		var newCell = newRow.insertCell(-1);
		this.cloneCell(newCell, currentRow.cells[i]);
	}
};
EditorTableCommand.prototype.insertColumn = function(editor, table, tableCell, insertBefore) { 
	editor.saveUndoStep();
	var cellIndex = tableCell.cellIndex;
	for(var i=0; i<table.rows.length; i++) {
		var currentCell = table.rows[i].cells[cellIndex];
		var newCell = table.rows[i].insertCell(cellIndex + (insertBefore ? 0 : 1));
		this.cloneCell(newCell, currentCell);
	}
};
EditorTableCommand.prototype.cloneCell = function(newCell, currentCell) { 
	newCell.width = currentCell.width;
	newCell.height = currentCell.height;
	if(currentCell.noWrap) {
		newCell.noWrap = currentCell.noWrap;
	}
	if(currentCell.align) {
		newCell.align = currentCell.align;
	}
	if(currentCell.vAlign) {
		newCell.vAlign = currentCell.vAlign;
	}
	if(currentCell.borderColor) {
		newCell.borderColor = currentCell.borderColor;
	}
	if(currentCell.bgColor) {
		newCell.bgColor = currentCell.bgColor;
	}
	if(currentCell.style.cssText) {
		newCell.style.cssText = currentCell.style.cssText;
	}
};

EditorTextColorCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showDropButton = true;
};
EditorTextColorCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return "" + editorDocument.queryCommandEnabled(this.name)=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorTextColorCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	var colors = '000000,993300,333300,003300,003366,000080,333399,333333,800000,FF6600,808000,808080,008080,0000FF,666699,808080,FF0000,FF9900,99CC00,339966,33CCCC,3366FF,800080,999999,FF00FF,FFCC00,FFFF00,00FF00,00FFFF,00CCFF,993366,C0C0C0,FF99CC,FFCC99,FFFF99,CCFFCC,CCFFFF,99CCFF,CC99FF,FFFFFF'.split(',');
	var pickerHtml = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">' +
					 '<html>\n' +
					 '	<head>\n' +
					 '		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">\n' +
					 '	</head>\n' +
					 '	<body style="margin:0px; padding:0px; border-style:none;">\n' +
					 '		<div id="colorPicker" class="listbar" style="position:static;"/>' +
					 '	</body>\n' +
					 '</html>';
	this.picker = new FormField.Picker(this.title, pickerHtml, toolbarButton, 163, 180, false, true, false);
	var pickerDocument = this.picker.pickerFrame.document;
	var colorPicker = pickerDocument.getElementById("colorPicker");
	
	var command = this;
	//\u6DFB\u52A0"\u81EA\u52A8"\u6309\u94AE
	this._addButton('\u81EA\u52A8', colorPicker).onclick = function() {
		command._setColor(command.name=="foreColor" ? "#000000" : "#ffffff", editor, editorWindow, editorDocument, range);
	};
	
	for(var i=0; i<colors.length; i++) {
		if(i!=0 && i%8==0) {
			var br = pickerDocument.createElement('br');
			br.style.clear = 'both';
			colorPicker.appendChild(br);
		}
		var colorBox = pickerDocument.createElement('span');
		colorBox.style.width = '12px';
		colorBox.style.height = '12px';
		colorBox.style.display = 'inline-block';
		colorBox.style.margin = '1px';
		colorBox.style.padding = '2px';
		colorBox.style.border = '#fff 1px solid';
		colorBox.id = colors[i];
		colorBox.onmouseover = function() {
			this.style.borderColor = '#555555';
			this.style.backgroundColor = '#e0e0e8';
		};
		colorBox.onmouseout = function() {
			this.style.borderColor = '#fff';
			this.style.backgroundColor = 'transparent';
		};
		colorBox.onclick = function() {
			command._setColor("#" + this.id, editor, editorWindow, editorDocument, range);
		};
		colorPicker.appendChild(colorBox);
		var colorDiv = pickerDocument.createElement('span');
		colorDiv.style.width = '10px';
		colorDiv.style.height = '10px';
		colorDiv.style.display = 'inline-block';
		colorDiv.style.border = '#dddddd 1px solid';
		colorDiv.style.backgroundColor = '#' + colors[i];
		colorBox.appendChild(colorDiv);
	}
	//\u6DFB\u52A0"\u5176\u5B83\u989C\u8272..."\u6309\u94AE
	var br = pickerDocument.createElement('br');
	br.style.clear = 'both';
	colorPicker.appendChild(br);
	this._addButton('\u5176\u5B83\u989C\u8272...', colorPicker).onclick = function() {
		DialogUtils.openColorDialog(command.title, '', '', function(colorValue) {
			command._setColor(colorValue, editor, editorWindow, editorDocument, range);
		});
	};
	
	this.picker.show();
};
EditorTextColorCommand.prototype._setColor = function(color, editor, editorWindow, editorDocument, range) {
	this.picker.destory();
	DomSelection.selectRange(editorWindow, range);
	editor.saveUndoStep();
	editor.setFontStyle(this.name=='foreColor' ? 'color' : 'backgroundColor', color);
};
EditorTextColorCommand.prototype._addButton = function(name, colorPicker) {
	var button = colorPicker.ownerDocument.createElement('div');
	button.style.padding = "4px 0px 4px 0px";
	button.style.textAlign = "center";
	button.innerHTML = name;
	button.style.border = '#ffffff 1px solid';
	button.onmouseover = function() {
		this.style.border = '#555555 1px solid';
		this.style.backgroundColor = '#e0e0e8';
	};
	button.onmouseout = function() {
		this.style.border = '#ffffff 1px solid';
		this.style.backgroundColor = '#ffffff';
	};
	colorPicker.appendChild(button);
	return button;
};

EditorUndoRedoCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
};
EditorUndoRedoCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	if(this.name=="undo") {
		return editor._isAllowUndo() ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
	}
	else {
		return editor._isAllowRedo() ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
	}
};
EditorUndoRedoCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	if(this.name=="undo") {
		editor.undo();
	}
	else {
		editor.redo();
	}
};

EditorVideoCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, true, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/video.shtml', 760, 560);
};
EditorVideoCommand.prototype = new EditorDialogCommand;
EditorVideoCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};

