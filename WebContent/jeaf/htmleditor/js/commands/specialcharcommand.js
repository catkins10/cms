//特殊字符命令
EditorSpecialCharCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showDropButton = true;
};
EditorSpecialCharCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return "" + editorDocument.queryCommandEnabled("InsertImage")=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorSpecialCharCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	var chars = "&asymp;&equiv;&ne;＝&le;&ge;≮≯∷&plusmn;＋－&times;&divide;／&int;∮&prop;&infin;&and;&or;&sum;&prod;&cup;&cap;&isin;∵&there4;&perp;‖&ang;⌒⊙≌∽&radic;&alpha;&beta;&gamma;&delta;&epsilon;&zeta;&eta;&theta;&iota;&kappa;&lambda;&mu;&nu;&xi;&omicron;&pi;&rho;&sigma;&tau;&upsilon;&phi;&chi;&psi;&omega;〔〕《》「」『』〖〗【】〔〕ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ①②③④⑤⑥⑦⑧⑨⑩&deg;&prime;〃＄￡￥&permil;℃&curren;￠&sect;№☆★○●◎◇◆□■△▲※&rarr;&larr;&uarr;&darr;♀♂&copy;&reg;";
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
		charBox.style.fontFamily = '宋体,Batang,Courier New';
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
	//显示选择器	
	this.picker.show();
};