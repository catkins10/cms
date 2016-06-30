HtmlEditor.prototype.onPaste = function(event) { //粘贴事件
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
	//创建DIV,用于获取HTML
	var div = this.editorDocument.createElement('div');
	div.id = "divClipboardHTML";
	div.style.position = 'absolute';
	div.style.overflow = 'hidden';
	div.style.width = div.style.height = 1;
	div.style.left = div.style.top = -1;
	this.editorDocument.body.appendChild(div);
	div.innerHTML = "&nbsp;";
	
	var range = DomSelection.createRange(this.editorDocument, div);
	if(range.execCommand) { //IE
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
HtmlEditor.prototype.retrievePasteHTML = function() { //获取粘贴的内容
	var divClipboardHTML = this.editorDocument.getElementById('divClipboardHTML');
	//w3c文本粘贴换行处理
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
	var singleElement = divClipboardHTML.childNodes.length==1 && divClipboardHTML.childNodes[0].childNodes.length==0; //是否单个元素
	divClipboardHTML.innerHTML = "";
	divClipboardHTML.parentNode.removeChild(divClipboardHTML);
	if(!/<(?!br)[^>]*?>/gi.test(html)) { //纯文本
		this.doPaste(html, 'pasteAsText');
	}
	else if(singleElement) {
		this.doPaste(html, 'pasteWithoutFilter');
	}
	else {
		var editor = this;
		var isWord = /<\w[^>]*(( class="?MsoNormal"?)|(="mso-))/gi.test(html);
		DialogUtils.openInputDialog('粘贴选项', [{name:'pasteOption', title:'粘贴选项', defaultValue:(isWord ? 'pasteWithFilter' : 'pasteWithoutFilter'), inputMode:'radio', itemsText:'默认|pasteWithoutFilter\\0粘贴为无格式文本|pasteAsText' + (isWord ? '\\0滤除WORD格式|pasteWithFilter' : '')}], 400, 200, '', function(value) {
			editor.doPaste(html, value.pasteOption);
		});
	}
};
HtmlEditor.prototype.doPaste = function(html, pasteOption) { //处理粘贴内容
	DomSelection.selectRange(this.editorWindow, this.pasteRange);
	this.saveUndoStep();
	html = html.replace(/<\S*\\?\?xml[^>]*>/gi, ""); //Remove XML elements and declarations
	html = html.replace(/<\/?\w+:[^>]*>/gi, ""); //Remove Tags with XML namespace declarations: <o:p><\/o:p>
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
	this.refreshToolbarButtonState(); //更新工具栏命令状态
};
HtmlEditor.prototype.pasteWithoutFilter = function(range, html) { //直接粘贴
	DomSelection.pasteHTML(this.editorWindow, range, html);
};
HtmlEditor.prototype.pasteAsText = function(range, html) { //粘贴为无格式文本
	html = html.replace(/<(p|div)[^>]*?>/gi, '<br/>').replace(/<(?!br)[^>]*?>/gi, '').replace(/<br[^>]*?>/gi, '<br/>');
	if(html.substring(0, 5)=='<br/>') {
		html = html.substring(5);
	}
	if(html.indexOf('<br/>')!=-1) {
		html = '<p>' + html.replace(/<br\/>/gi, '</p>\r\n<p>') + '</p>';
	}
	DomSelection.pasteHTML(this.editorWindow, range, html);
};
HtmlEditor.prototype.pasteWithFilter = function(range, html) { //过滤并粘贴
	var bIgnoreFont = true;
	var bRemoveStyles = true;

	//Remove mso-xxx styles.
	html = html.replace(/\s*mso-[^:]+:[^;"]+;?/gi, "") ;
	//Remove margin styles.
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

	//Remove FONT face attributes.
	if(bIgnoreFont) {
		html = html.replace(/\s*face="[^"]*"/gi, "");
		html = html.replace(/\s*face=[^ >]*/gi, "");
		html = html.replace(/\s*FONT-FAMILY:[^;"]*;?/gi, "");
		html = html.replace(/\s*FONT-SIZE:[^;"]*;?/gi, ""); //clear font size by linchuan
	}
	
	//Remove Class attributes
	html = html.replace(/<(\w[^>]*) class=([^ |>]*)([^>]*)/gi, "<$1$3") ;

	// Remove styles.
	//if ( bRemoveStyles )
	//	html = html.replace(/<(\w[^>]*) style="([^\"]*)"([^>]*)/gi, "<$1$3");

	//Remove empty styles.
	html =  html.replace(/\s*style="\s*"/gi, '');
	html = html.replace(/<SPAN\s*[^>]*>\s*&nbsp;\s*<\/SPAN>/gi, '&nbsp;');
	html = html.replace(/<SPAN\s*[^>]*><\/SPAN>/gi, '');
	
	//Remove Lang attributes
	html = html.replace(/<(\w[^>]*) lang=([^ |>]*)([^>]*)/gi, "<$1$3") ;
	html = html.replace(/<SPAN\s*>([.\d]*?)<\/SPAN>/gi, '$1'); //清除空的<SPAN>...</SPAN>
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

	// Remove empty tags (three times, just to be sure).
	html = html.replace(/<([^\s>]+)[^>]*>\s*<\/\1>/g, '');
	html = html.replace(/<([^\s>]+)[^>]*>\s*<\/\1>/g, '');
	html = html.replace(/<([^\s>]+)[^>]*>\s*<\/\1>/g, '');
	
	//清除padding
	html = html.replace(/\s*PADDING-LEFT:[^;"]*;?/gi, "");
	html = html.replace(/\s*PADDING-RIGHT:[^;"]*;?/gi, "");
	html = html.replace(/\s*PADDING-TOP:[^;"]*;?/gi, "");
	html = html.replace(/\s*PADDING-BOTTOM:[^;"]*;?/gi, "");
	
	//清除margin
	//html = html.replace(/\s*MARGIN-LEFT:[^;"]*;?/gi, "");
	//html = html.replace(/\s*MARGIN-RIGHT:[^;"]*;?/gi, "");
	//html = html.replace(/\s*MARGIN-TOP:[^;"]*;?/gi, "");
	//html = html.replace(/\s*MARGIN-BOTTOM:[^;"]*;?/gi, "");
	
	//html = html.replace(/\s*TEXT-INDENT:[^;"]*;?/gi, "");
	
	//清除WPS,<IMG alt="" src="file:///C:/Users/lenovo/AppData/Local/Temp/ksohtml/wpsBDBF.tmp.png" width=565 height=2>
	html = html.replace(/<IMG([^>]*) src="file:\/\/\/([^>]*)>/gi, '');
	
	//Transform <P> to <DIV>
	var re = new RegExp("(<P)([^>]*>.*?)(<\/P>)","gi") ;	// Different because of a IE 5.0 error
	html = html.replace(re, "<div$2<\/div>" );
	DomSelection.pasteHTML(this.editorWindow, range, html);
};