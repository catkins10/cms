//表情图标命令
EditorSmileyCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showDropButton = true;
};
EditorSmileyCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return "" + editorDocument.queryCommandEnabled("InsertImage")=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorSmileyCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	var smileys = ('微笑|weixiao.gif,傲慢|aomang.gif,白眼|baiyan.gif,别嘴|biezui.gif,闭嘴|bizui.gif,擦汗|cahan.gif,大兵|dabing.gif,大哭|daku.gif,得意|deyi.gif,发呆|fadai.gif,' +
				  '发怒|fanu.gif,尴尬|ganga.gif,害羞|haixiu.gif,憨笑|hanxiao.gif,饥饿|jie.gif,惊恐|jingkong.gif,惊讶|jingya.gif,可爱|keai.gif,扣鼻|koubi.gif,酷|ku.gif,' +
				  '困|kun.gif,冷汗|lenghan.gif,咧牙|lieya.gif,流汗|liuhan.gif,流泪|liulei.gif,难过|nanguo.gif,色|se.gif,衰|shuai.gif,睡|shui.gif,调皮|tiaopi.gif,' +
				  '偷笑|touxiao.gif,吐|tu.gif,敲打|qiaoda.gif,再见|zaijian.gif,抓狂|zhuakuang.gif').split(",");
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
		//添加图片
		var values = smileys[i].split("\|");
		var image = pickerDocument.createElement('img');
		image.src = RequestUtils.getContextPath() + '/jeaf/htmleditor/images/expression/' + values[1];
		smileyBox.title = values[0];
		smileyBox.appendChild(image);
	}
	//显示选择器	
	this.picker.show();
};