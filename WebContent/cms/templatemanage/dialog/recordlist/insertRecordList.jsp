<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertRecordList">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		var controlBar = {};
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			var properties = "";
			if(obj && obj.id=="recordList") {
				properties = getUrn(obj);
				loadRecordList(obj);
			}
			var embedViews = DropdownField.getListValues('embedViews');
			if(!embedViews || embedViews=="") {
				document.getElementById('tdInsertEmbedViewButton').style.display = 'none';
			}
			//设置隔符自定义IFRAME
			var doc = window.frames["separatorCustom"].document;
			doc.open();
			doc.write('<html>');
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; background-color:#ffffff; overflow:hidden; margin:0px; padding-left:3px; font-family:宋体; font-size:12px; line-height:18px;">');
			doc.write(StringUtils.getPropertyValue(properties, "separatorCustom"));
			doc.write('</body>');
			doc.write('</html>');
			doc.close();
			
			//设置记录格式
			setRecordFormat();
			
			//设置记录列表为空时的提示
			doc = window.frames["emptyPromptFormat"].document;
			doc.open();
			doc.write('<html>');
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; background-color:#ffffff; overflow:hidden; margin:0px; padding-left:3px; font-family:宋体; font-size:12px; line-height:18px;">');
			doc.write(properties=="" ? document.getElementsByName("emptyPrompt")[0].value : StringUtils.getPropertyValue(properties, "emptyPrompt"));
			doc.write('</body>');
			doc.write('</html>');
			doc.close();
			
			//复制模板的样式
			CssUtils.cloneStyle(dialogArguments.document, doc);
			
			//如果是内嵌记录列表,调整对话框位置
			if("true"==document.getElementsByName("embedRecordList")[0].value) {
				DialogUtils.moveDialog(20, 20);
			}
		};
		function loadRecordList(obj) { //加载记录列表
			var properties = getUrn(obj);
			//显示扩展属性
			try {
				window.frames['dialogExtendFrame'].showExtendProperties(StringUtils.getPropertyValue(properties, "extendProperties"), obj.innerHTML)
			}
			catch(e) {
				
			}
			//滚动显示
			var propertyValue;
			if((propertyValue=StringUtils.getPropertyValue(properties, "scrollMode"))!="") {
				DropdownField.setValue("scrollMode", propertyValue);
				document.getElementsByName("scrollJoin")[0].checked = "true"==StringUtils.getPropertyValue(properties, "scrollJoin");
				document.getElementsByName("scrollSpeed")[0].value = StringUtils.getPropertyValue(properties, "scrollSpeed");
				document.getElementsByName("scrollAmount")[0].value = StringUtils.getPropertyValue(properties, "scrollAmount");
				if(propertyValue=="switch") { //滚动切换
					controlBar.switchMode = StringUtils.getPropertyValue(properties, "switchMode"); //切换方式,保持和旧系统兼容
					controlBar.manualSwitch = "true"==StringUtils.getPropertyValue(properties, "manualSwitch"); //是否手工切换
					controlBar.autoScaling = "true"==StringUtils.getPropertyValue(properties, "autoScaling"); //是否自适应大小
					controlBar.controlBarPosition = StringUtils.getPropertyValue(properties, "controlBarPosition"); //显示位置
					controlBar.controlBarXMargin = StringUtils.getPropertyValue(properties, "controlBarXMargin"); //水平边距
					controlBar.controlBarYMargin = StringUtils.getPropertyValue(properties, "controlBarYMargin"); //垂直边距
					controlBar.scrollDirection = StringUtils.getPropertyValue(properties, "scrollDirection"); //滚动方向
					controlBar.controlBarRecordSpacing = StringUtils.getPropertyValue(properties, "controlBarRecordSpacing"); //控制栏记录分隔距离
					controlBar.mouseOverControlBarRecord = StringUtils.getPropertyValue(properties, "mouseOverControlBarRecord"); //鼠标经过控制记录时的动作
					controlBar.clickControlBarRecord = StringUtils.getPropertyValue(properties, "clickControlBarRecord"); //点击控制记录时的动作
					controlBar.controlBarFormat = StringUtils.getPropertyValue(properties, "controlBarFormat"); //控制栏格式
					controlBar.imageClickAction = StringUtils.getPropertyValue(properties, "imageClickAction"); //点击图片操作
					controlBar.switchByKey = "true"==StringUtils.getPropertyValue(properties, "switchByKey"); //左右方向键翻阅
					controlBar.displayNextPreviousButton = StringUtils.getPropertyValue(properties, "displayNextPreviousButton"); //是否显示前一页、后一页按钮
					controlBar.nextPreviousButtonFormat = StringUtils.getPropertyValue(properties, "nextPreviousButtonFormat"); //前一页、后一页按钮格式
				}
			}
			//解析记录数
			document.getElementsByName("recordCount")[0].value = StringUtils.getPropertyValue(properties, "recordCount");
			//解析记录格式
			document.getElementsByName("recordFormatSource")[0].value = StringUtils.getPropertyValue(properties, "recordFormat");
			//解析分隔符
			document.getElementsByName("separatorHeight")[0].value = StringUtils.getPropertyValue(properties, "separatorHeight");
			var separatorMode = StringUtils.getPropertyValue(properties, "separatorMode");
			DropdownField.setValue("separatorMode", separatorMode);
			if(separatorMode=="image") {
				document.getElementsByName("separatorImage")[0].value = StringUtils.getPropertyValue(properties, "separatorImage");
				document.getElementsByName("separatorImageOfLastRecord")[0].checked = StringUtils.getPropertyValue(properties, "separatorImageOfLastRecord")!="false";
			}
			else if(separatorMode=="custom") {
				document.getElementsByName("separatorOfLastRecord")[0].checked = StringUtils.getPropertyValue(properties, "separatorOfLastRecord")=="true";
			}
			onSeparatorModeChanged();
			//宽度
			if((propertyValue=StringUtils.getPropertyValue(properties, "areaWidth"))!="") {
				document.getElementsByName("width")[0].value = propertyValue;
			}
			//高度
			if((propertyValue=StringUtils.getPropertyValue(properties, "areaHeight"))!="") {
				document.getElementsByName("height")[0].value = propertyValue;
			}
			//记录宽度
			if((propertyValue=StringUtils.getPropertyValue(properties, "recordWidth"))!="") {
				document.getElementsByName("recordWidth")[0].value = propertyValue;
			}
			//记录高度
			if((propertyValue=StringUtils.getPropertyValue(properties, "recordHeight"))!="") {
				document.getElementsByName("recordHeight")[0].value = propertyValue;
			}
			//记录缩进
			if((propertyValue=StringUtils.getPropertyValue(properties, "recordIndent"))!="") {
				document.getElementsByName("recordIndent")[0].value = propertyValue;
			}
			//链接方式
			setLinkOpenMode(StringUtils.getPropertyValue(properties, "linkOpenMode"));
			//链接类型
			DropdownField.setValue("recordLink", StringUtils.getPropertyValue(properties, "linkTitle"));
			//显示方式
			var recordMode = document.getElementsByName("recordMode")[0];
			if("true"==StringUtils.getPropertyValue(properties, "tableMode")) {
				window.currentRecordMode = "table";
			}
			else if("true"==StringUtils.getPropertyValue(properties, "simpleMode")) {
				window.currentRecordMode = "simple";
			}
			else {
				window.currentRecordMode = "normal";
			}
			DropdownField.setValue("recordMode", window.currentRecordMode);
			//滚动方式
			onScrollModeChange();
		}
		function doOk() {
			//保存扩展属性
			var extendProperties = "";
			try {
				extendProperties = window.frames['dialogExtendFrame'].getExtendPropertiesAsText();
			}
			catch(e) {

			}
			if(extendProperties=="ERROR") {
				return false;
			}
			var urn = "recordListName=" + document.getElementsByName("recordListName")[0].value + //记录列表名称
				   	  ("true"==document.getElementsByName("privateRecordList")[0].value ? "&privateRecordList=true&recordClassName=" + document.getElementsByName("recordClassName")[0].value : "") + //是否私有记录列表
				   	  "&applicationName=" + document.getElementsByName("applicationName")[0].value + //应用名称
				   	  (document.getElementsByName("searchResults")[0].value=="true" ? "&searchResults=true" : ""); //是否搜索结果
			//链接类型
			var linkTitle = document.getElementsByName("recordLink")[0].value;
			if(linkTitle!="") {
				 urn += "&linkTitle=" + linkTitle;
			}
			//记录数
			var recordCount = Number(document.getElementsByName("recordCount")[0].value);
			if(isNaN(recordCount)) {
				alert("记录数输入错误");
				document.getElementsByName("recordCount")[0].focus;
				return;
			}
			if(recordCount<1) {
				alert("记录数不能小于1");
				document.getElementsByName("recordCount")[0].focus;
				return;
			}
			urn += "&recordCount=" + recordCount;
			//记录格式
			var recordFormat = getRecordFormatHTML();
			if(recordFormat=='') {
				alert("记录格式不能为空");
				return;
			}
			urn += "&recordFormat=" + StringUtils.encodePropertyValue(recordFormat);
			//记录分隔符
			var separatorMode = document.getElementsByName("separatorMode")[0].value;
			var separatorImage = '';
			var separatorCustom = '';
			var separatorImageOfLastRecord = '';
			var separatorOfLastRecord = '';
			if(separatorMode == 'image') { //图片分隔符
				separatorImage = document.getElementsByName("separatorImage")[0].value;
				if(separatorImage=='') {
					alert("分隔图片不能为空");
					return;
				}
				separatorImageOfLastRecord = document.getElementsByName("separatorImageOfLastRecord")[0].checked ? "true" : "false";
			}
			else if(separatorMode == 'custom') { //自定义分隔符
				separatorOfLastRecord = document.getElementsByName("separatorOfLastRecord")[0].checked ? "true" : "false";
				separatorCustom = frames["separatorCustom"].document.body.innerHTML;
			}
			//间距
			var separatorHeight = Number(document.getElementsByName("separatorHeight")[0].value);
			if(separatorHeight=='NaN') {
				alert("间距不正确");
				return;
			}
			urn += "&separatorMode=" + separatorMode +
				   (separatorImage=="" ? "" : "&separatorImage=" + separatorImage) +
				   (separatorCustom=="" ? "" : "&separatorCustom=" + StringUtils.encodePropertyValue(separatorCustom)) +
				   (separatorImageOfLastRecord=="" ? "" : "&separatorImageOfLastRecord=" + separatorImageOfLastRecord) +
				   (separatorOfLastRecord=="" ? "" : "&separatorOfLastRecord=" + separatorOfLastRecord) +
				   "&separatorHeight=" + separatorHeight +
				   (document.getElementsByName("width")[0].value=="" || document.getElementsByName("width")[0].value=="自动" ? "" : "&areaWidth=" + document.getElementsByName("width")[0].value) +
				   (document.getElementsByName("height")[0].value=="" || document.getElementsByName("height")[0].value=="自动" ? "" : "&areaHeight=" + document.getElementsByName("height")[0].value) +
				   (document.getElementsByName("recordWidth")[0].value=="" || document.getElementsByName("recordWidth")[0].value=="自动" ? "" : "&recordWidth=" + document.getElementsByName("recordWidth")[0].value) +
				   (document.getElementsByName("recordHeight")[0].value=="" || document.getElementsByName("recordHeight")[0].value=="自动" ? "" : "&recordHeight=" + document.getElementsByName("recordHeight")[0].value) +
				   (document.getElementsByName("recordIndent")[0].value=="" ? "" : "&recordIndent=" + document.getElementsByName("recordIndent")[0].value);
			//滚动显示
			var scrollMode = document.getElementsByName("scrollMode")[0].value;
			if(scrollMode!="none") {
				urn += "&scrollMode=" + scrollMode +
					   "&scrollSpeed=" + document.getElementsByName("scrollSpeed")[0].value +
				       "&scrollAmount=" + document.getElementsByName("scrollAmount")[0].value +
				       (!document.getElementsByName("scrollJoin")[0].checked ? "" : "&scrollJoin=true");
				if(scrollMode=="switch") {
					urn += (controlBar.switchMode=="" || controlBar.controlBarFormat!="" ? "" : "&switchMode=" + controlBar.switchMode) + 
						   (!controlBar.manualSwitch ? "" : "&manualSwitch=true") + //是否手工切换
						   (!controlBar.autoScaling ? "" : "&autoScaling=true") + //是否自适应大小
						   "&controlBarPosition=" + controlBar.controlBarPosition + //显示位置
						   "&controlBarXMargin=" + controlBar.controlBarXMargin + //水平边距
						   "&controlBarYMargin=" + controlBar.controlBarYMargin + //垂直边距
						   "&scrollDirection=" + controlBar.scrollDirection + //滚动方向
						   "&controlBarRecordSpacing=" + controlBar.controlBarRecordSpacing + //控制栏记录分隔距离
						   "&mouseOverControlBarRecord=" + controlBar.mouseOverControlBarRecord + //鼠标经过控制记录时的动作
						   "&clickControlBarRecord=" + controlBar.clickControlBarRecord + //点击控制记录时的动作
						   "&controlBarFormat=" + StringUtils.encodePropertyValue(controlBar.controlBarFormat) + //控制栏格式
						   "&imageClickAction=" + controlBar.imageClickAction + //点击图片操作
						   (!controlBar.switchByKey ? "" : "&switchByKey=true") + //左右方向键翻阅
						   "&displayNextPreviousButton=" + controlBar.displayNextPreviousButton + //是否显示前一页、后一页按钮
						   "&nextPreviousButtonFormat=" + StringUtils.encodePropertyValue(controlBar.nextPreviousButtonFormat); //前一页、后一页按钮格式
				}
			}
			//链接方式
			urn += "&linkOpenMode=" + getLinkOpenMode() + 
				   (window.currentRecordMode=="table" ? "&tableMode=true" : "") + //是否表格方式
				   (window.currentRecordMode=="simple" ? "&simpleMode=true" : "") + //是否精简方式
				   "&emptyPrompt=" + StringUtils.encodePropertyValue(frames["emptyPromptFormat"].document.body.innerHTML); //无记录时的提示
				   
			urn += "&extendProperties=" + StringUtils.encodePropertyValue(extendProperties);
			
			//插入记录列表对象
			if(dialogArguments.window==dialogArguments.editor.editorWindow) { //不是为记录列表插入字段
				dialogArguments.editor.saveUndoStep();
			}
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					alert('请重新选择插入的位置');
					return;
				}
			}
			var elementTitle = "";
			try {
				elementTitle = window.frames['dialogExtendFrame'].getElementTitle("true"==document.getElementsByName("searchResults")[0].value, false);
			}
			catch(e) {
			
			}
			if(!elementTitle || elementTitle=='') {
				elementTitle = (document.getElementsByName("searchResults")[0].value=="true" ? "搜索结果" : document.getElementsByName("viewTitle")[0].value);
			}
			obj.innerHTML = '&lt;' + elementTitle + '&gt;';
			obj.id = 'recordList';
			setUrn(obj, urn);
			DialogUtils.closeDialog();
		}
		function getRecordFormatHTML() { //获取格式HTML
			var divRecordFormatSource = document.getElementById("divRecordFormatSource");
			if(divRecordFormatSource.style.display!='none') {
				return document.getElementsByName("recordFormatSource")[0].value;
			}
			var doc = frames["recordFormat"].document;
			if(window.currentRecordMode=="table") { //表格方式
				return doc.body.innerHTML;
			}
			var leftContent = doc.getElementById("spanLeft").innerHTML;
			var rightContent = doc.getElementById("spanRight").innerHTML;
			if(rightContent.trim()=="" || rightContent.toLowerCase()=="<br/>" || rightContent.toLowerCase()=="<br>" || rightContent=="&nbsp;") {
				return leftContent;
			}
			return '<div style="float:left">' + leftContent + '</div><div style="float:right">' + rightContent + '</div>';
		}
		//初始化记录格式设置框
		function setRecordFormat() {
			var doc = frames["recordFormat"].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="background-color:#ffffff; font-family:宋体; font-size:12px; border-style:none; overflow:auto; margin:3px">');
			doc.write(document.getElementsByName("recordFormatSource")[0].value);
			doc.write('</body>');
			doc.close();
			if(window.currentRecordMode!="table") { //不是表格方式
				var leftFormat = '';
				var rightFormat = '';
				var objs = doc.body.childNodes;
				if(objs.length==2 &&
				   objs[0].tagName && objs[1].tagName && objs[0].tagName=='DIV' && objs[1].tagName=='DIV' &&
				   objs[0].style.float=='left' && objs[1].style.float=='right') {
					leftFormat = objs[0].innerHTML;
					rightFormat = objs[1].innerHTML;
				}
				else {
					leftFormat = doc.body.innerHTML;
				}
				if(leftFormat=='') { //左侧内容为空,设置默认格式
					try {
						leftFormat = window.frames['dialogExtendFrame'].getDeafultRecordLeftFormat();
					}
					catch(e) {
					
					}
					if(rightFormat=='') { //左侧内容为空,设置默认格式
						try {
							rightFormat = window.frames['dialogExtendFrame'].getDeafultRecordRightFormat();
							}
						catch(e) {
						
						}
					}
				}
				//非表格方式
				doc.open();
				doc.write('<body onkeydown="return true;if(event.keyCode==13)return false;" style="background-color:#ffffff; border-style:none; overflow:hidden; margin:0px; padding:0px;">');
				doc.write('<table cellspacing="0" cellpadding="2" width="100%" height="100%" style="font-family:宋体; font-size:12px"><tr valign="middle">');
				doc.write('<td><span id="spanLeft" style="width:100%" contentEditable="true">');
				doc.write(leftFormat);
				doc.write('</span></td>');
				doc.write('<td style="border-left:#cccccc 1px solid; text-align:right"><span id="spanRight" style="width:100%" contentEditable="true">');
				doc.write(rightFormat);
				doc.write('</span></td></tr></table>');
				doc.write('</body>');
				doc.close();
			}
			//复制模板的样式
			CssUtils.cloneStyle(dialogArguments.document, doc);
		}
		function onRecordModeChanged() { //记录显示方式切换
			if(window.currentRecordMode=="simple" || window.currentRecordMode=="normal") {
				window.normalRecordFormat = getRecordFormatHTML();
			}
			else if(window.currentRecordMode=="table") {
				window.tableRecordFormat = getRecordFormatHTML();
			}
			window.currentRecordMode = document.getElementsByName("recordMode")[0].value;
			if(window.currentRecordMode=="simple" || window.currentRecordMode=="normal") {
				document.getElementsByName("recordFormatSource")[0].value = window.normalRecordFormat ? window.normalRecordFormat : "";
			}
			else if(window.currentRecordMode=="table") {
				document.getElementsByName("recordFormatSource")[0].value = window.tableRecordFormat ? window.tableRecordFormat : "";
			}
			setRecordFormat();
		}
		function openInsertFieldDialog() { //打开插入字段对话框
			var args = generateDialogArguments(frames["recordFormat"]);
			if(args) {
				doOpenInsertFieldDialog(args);
			}
		}
		function doOpenInsertFieldDialog(args) {
			var redirectApplicationName = document.getElementsByName("redirectApplicationName")[0].value;
			var redirectRecordListName = document.getElementsByName("redirectRecordListName")[0].value;
			var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertField.shtml" +
							"?applicationName=" + (redirectApplicationName=="" ? document.getElementsByName("applicationName")[0].value : redirectApplicationName)+
							"&recordListName=" + (redirectRecordListName=="" ? document.getElementsByName("recordListName")[0].value : redirectRecordListName) +
							"&privateRecordList=" + document.getElementsByName("privateRecordList")[0].value +
							"&recordClassName=" + document.getElementsByName("recordClassName")[0].value +
							"&linkable=true";
			DialogUtils.openDialog(dialogUrl, 550, 360, '', args);
		}
		function openInsertNewestImageDialog() { //打开插入"new"图片对话框
			var args = generateDialogArguments(frames["recordFormat"]);
			if(!args) {
				return;
			}
			DialogUtils.openDialog(RequestUtils.getContextPath() + '/cms/templatemanage/dialog/insertNewestImage.shtml', 430, 180, '', args);
		}
		function customControlBar() { //配置切换方式
			var args = {document:dialogArguments.document,
						editor:dialogArguments.editor,
						recordListDialog:window,
						controlBar:controlBar};
			DialogUtils.openDialog(RequestUtils.getContextPath() + '/cms/templatemanage/dialog/recordListControlBar.shtml', 680, 380, '', args);
		}
		function openInsertLinkDialog() { //打开插入链接对话框
			var args = generateDialogArguments(frames["recordFormat"]);
			if(!args) {
				return;
			}
			var redirectApplicationName = document.getElementsByName("redirectApplicationName")[0].value;
			var redirectRecordListName = document.getElementsByName("redirectRecordListName")[0].value;
			var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRecordLink.shtml" +
							"?applicationName=" + (redirectApplicationName=="" ? document.getElementsByName("applicationName")[0].value : redirectApplicationName)+
							"&recordListName=" + (redirectRecordListName=="" ? document.getElementsByName("recordListName")[0].value : redirectRecordListName) +
							"&privateRecordList=" + document.getElementsByName("privateRecordList")[0].value +
							"&recordClassName=" + document.getElementsByName("recordClassName")[0].value +
							"&themeType=" + dialogArguments.editor.document.getElementsByName("themeType")[0].value;
			DialogUtils.openDialog(dialogUrl, 450, 280, '', args);
		}
		function selectEmbedView(src) { //选择内嵌记录列表
			var args = generateDialogArguments(frames["recordFormat"]);
			if(!args) {
				return;
			}
			PopupMenu.popupMenu(DropdownField.getListValues('embedViews'), function(menuItemId, menuItemTitle) {
				var embedView;
				eval('embedView=' + menuItemId);
				var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRecordList.shtml" +
								"?embedRecordList=true" +
								"&applicationName=" + embedView.applicationName +
								"&recordListName=" + embedView.recordListName +
								(embedView.privateRecordList ? "&privateRecordList=true&recordClassName=" + embedView.recordClassName : "") +
								"&themeType=" + dialogArguments.editor.document.getElementsByName("themeType")[0].value;
				DialogUtils.openDialog(dialogUrl, 780, 400, "", args);
			}, src, '120px');
		}
		function selectImage(selectFor) { //选择图片, selectFor:separatorCustomImage/分隔符图片,recordFormat/记录格式
			if((selectFor=="recordFormat" || selectFor=="separatorCustomImage") &&
				!(window.args = generateDialogArguments(window.frames[selectFor=="separatorCustomImage" ? "separatorCustom" : "recordFormat"]))) {
				return;
			}
			DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('images', 'writeImage("{URL}", "' + selectFor + '")'), 640, 400);
		}
		function writeImage(imageURL, selectFor) {
			if(selectFor=="separatorImage") { //图片分隔符
				document.getElementsByName("separatorImage")[0].value = imageURL;
			}
			else if(selectFor=="recordFormat" || selectFor=="separatorCustomImage") {
				var img = window.args.selectedElement;
				if(!img || img.tagName!='IMG') {
					img = DomUtils.createElement(window.args.window, window.args.range, "img");
				}
				if(!img) {
					alert("图片插入失败");
				}
				else {
					img.src = imageURL;
				}
			}
		}
		function onSeparatorModeChanged() {
			var separatorMode = document.getElementsByName("separatorMode")[0].value;
			document.getElementById("tdSeparatorImage").style.display = (separatorMode=="image" ? "" : "none");
			document.getElementById("tdSeparatorCustom").style.display = (separatorMode=="custom" ? "" : "none");
		}
		function onScrollModeChange() {
			var scrollMode =  document.getElementsByName("scrollMode")[0].value;
			document.getElementById("tdScrollJoin").style.display = (",down,up,right,left,".indexOf("," + scrollMode + ",")!=-1 ? "" : "none");
			document.getElementById("tdSwitchMode").style.display = (scrollMode=="switch" ? "" : "none");
		}
		function viewRecordFormatSource(button) {
			var sourceMode = false;
			var divRecordFormatSource = document.getElementById("divRecordFormatSource");
			if(divRecordFormatSource.style.display=='none') {
				document.getElementsByName("recordFormatSource")[0].value = getRecordFormatHTML();
				sourceMode = true;
			}
			else {
				setRecordFormat();
			}
			button.value = sourceMode ? "回到编辑模式" : "查看源代码";
			divRecordFormatSource.style.display = sourceMode ? "" : "none";
			document.getElementById("recordFormat").style.display = !sourceMode ? "" : "none";
			document.getElementById("insertFieldButton").disabled = sourceMode;
			document.getElementById("insertImageButton").disabled = sourceMode;
			document.getElementById("insertNewestImageButton").disabled = sourceMode;
			document.getElementById("insertLinkButton").disabled = sourceMode;
			if(document.getElementById("insertEmbedView")) {
				document.getElementById("insertEmbedView").disabled = sourceMode;
			}
			document.getElementById("insertEmbedViewButton").disabled = sourceMode;
			document.getElementsByName("recordMode")[0].disabled = sourceMode;
		}
		function redirectToNewView(newApplicationName , newRecordListName) { //重定向到新视图
			if(newApplicationName==document.getElementsByName("applicationName")[0].value &&
			   newRecordListName==document.getElementsByName("recordListName")[0].value) {
				newApplicationName = "";
				newRecordListName = "";
			}
			if(newApplicationName==document.getElementsByName("redirectApplicationName")[0].value &&
			   newRecordListName==document.getElementsByName("redirectRecordListName")[0].value) {
				return;
			}
			var js = "redirectToNewView.js.shtml" +
					 "?redirectApplicationName=" + (newApplicationName!="" ? newApplicationName : document.getElementsByName("applicationName")[0].value) +
					 "&redirectRecordListName=" + (newRecordListName!="" ? newRecordListName : document.getElementsByName("recordListName")[0].value) +
					 "&seq=" + Math.random();
			ScriptUtils.appendJsFile(document, js, "redirectToNewView");
			document.getElementsByName("redirectApplicationName")[0].value = newApplicationName;
			document.getElementsByName("redirectRecordListName")[0].value = newRecordListName;
		}
		function generateDialogArguments(window, noFocusPrompt) { //生成对话框参数
			var args = DomUtils.getWindowBookmark(window, noFocusPrompt ? noFocusPrompt : '插入位置不正确，请重新选择');
			if(!args) {
				return null;
			}
			args.editor = dialogArguments.editor;
			return args;
		}
		function getUrn(element) {
			var urn = element.getAttribute('urn');
			for(var i=1; ; i++) {
				var value = element.getAttribute('urn' + i);
				if(!value) {
					break;
				}
				urn += value;
			}
			return urn;
		}
		function setUrn(element, urnValue) {
			for(var i=0; ; i++) {
				if(urnValue.length > i*4096) {
					var urn = urnValue.substring(i * 4096, Math.min((i+1) * 4096, urnValue.length));
					element.setAttribute('urn' + (i==0 ? "" : i), urn);
				}
				else {
					if(!element.getAttribute('urn' + i)) {
						break;
					}
					element.removeAttribute('urn' + i);
				}
			}
		}
	</script>
	<ext:notEmpty property="templateExtendURL">
		<iframe name="dialogExtendFrame" id="dialogExtendFrame" allowtransparency="true" src="<%=request.getContextPath()%><ext:write property="templateExtendURL"/>" frameborder="0" style="width:100%;height: 0px"></iframe>
	</ext:notEmpty>
	<table id="mainTable" border="0" width="100%" cellspacing="5px" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">记录数量：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td width="100px" id="tdRecordCount"><ext:field property="recordCount"/></td>
						<td>&nbsp;&nbsp;区域宽度：</td>
						<td width="100px"><ext:field property="width"/></td>
						<td>&nbsp;&nbsp;区域高度：</td>
						<td width="100px"><ext:field property="height"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">段落缩进：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td width="100px"><ext:field property="recordIndent"/></td>
						<td>&nbsp;&nbsp;记录宽度：</td>
						<td width="100px"><ext:field property="recordWidth"/></td>
						<td>&nbsp;&nbsp;记录高度：</td>
						<td width="100px"><ext:field property="recordHeight"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">链接方式：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
						<td>&nbsp;&nbsp;链接地址：</td>
						<td width="100px"><ext:field property="recordLink"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">滚动显示：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td width="100px"><ext:field property="scrollMode" onchange="onScrollModeChange()"/></td>
						<td id="tdScrollJoin" style="display:none">&nbsp;<ext:field property="scrollJoin"/></td>
						<td id="tdSwitchMode" style="display:none; padding-left:3px;">
							<input type="button" class="button" value="配置切换方式" onclick="customControlBar()">
						</td>
						<td>
							<table border="0" cellspacing="0" cellpadding="0px">
								<tr>
									<td>&nbsp;&nbsp;速度(毫秒)：</td>
									<td width="36px"><ext:field property="scrollSpeed"/></td>
								</tr>
							</table>
						</td>
						<td>
							<table border="0" cellspacing="0" cellpadding="0px">
								<tr>
									<td>&nbsp;&nbsp;滚动距离(像素)：</td>
									<td width="36px"><ext:field property="scrollAmount"/></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:6px">记录格式：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td nowrap="nowrap" width="100px"><ext:field property="recordMode" onchange="onRecordModeChanged()"/></td>
						<td style="padding-left:3px"><input type="button" class="button" value="插入字段" id="insertFieldButton" onmousedown="openInsertFieldDialog()"></td>
						<td style="padding-left:3px"><input type="button" class="button" value="插入图片" id="insertImageButton" onmousedown="selectImage('recordFormat')"></td>
						<td style="padding-left:3px"><input type="button" class="button" value="插入&quot;new&quot;图片" id="insertNewestImageButton" onmousedown="openInsertNewestImageDialog()"></td>
						<td style="padding-left:3px"><input type="button" class="button" value="插入链接" id="insertLinkButton" onmousedown="openInsertLinkDialog()"></td>
						<td id="tdInsertEmbedViewButton" style="padding-left:3px">
							<input type="button" class="button" value="内嵌列表▼" id="insertEmbedViewButton" onmousedown="selectEmbedView(this);return false;">
							<ext:field style="display:none" property="embedViews"/>
						</td>
						<td style="padding-left:3px"><input type="button" class="button" value="查看源代码" onmousedown="viewRecordFormatSource(this)"></td>
						<td width="100%"></td>
					</tr>
					<tr>
						<td style="padding-top:5px" colspan="8">
							<div id="divRecordFormatSource" style="display:none">
								<ext:field property="recordFormatSource" styleClass="field" style="width:100%; height:100px !important; word-break:break-all"/>
							</div>
							<iframe name="recordFormat" id="recordFormat" frameBorder="0" src="about:blank" class="frame" style="width:100%; height:100px;"></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" height="26px">记录分隔：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td width="100px"><ext:field property="separatorMode" onchange="onSeparatorModeChanged()"/></td>
						<td id="tdSeparatorImage" style="display:none; padding-left:3px;">
							<table border="0" cellspacing="0" cellpadding="0px">
								<tr>
									<td width="200px"><ext:field property="separatorImage"/></td>
									<td>&nbsp;&nbsp;<ext:field property="separatorImageOfLastRecord"/></td>
								</tr>
							</table>
						</td>
						<td id="tdSeparatorCustom" style="display:none; padding-left:3px;">
							<table border="0" cellspacing="0" cellpadding="0px">
								<tr>
									<td width="200px"><iframe id="separatorCustom" name="separatorCustom" src="about:blank" class="frame" style="width:208px; height:18px;" align="top" frameborder="0"></iframe></td>
									<td style="padding-left:3px;"><input type="button" class="button" value="插入图片" style="width:60px" onmousedown="selectImage('separatorCustomImage')">&nbsp;</td>
									<td>&nbsp;&nbsp;<ext:field property="separatorOfLastRecord"/></td>
								</tr>
							</table>
						</td>
						<td>&nbsp;&nbsp;间距：</td>
						<td width="33px"><ext:field property="separatorHeight"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" height="26px">无记录提示：</td>
			<td>
				<iframe id="emptyPromptFormat" name="emptyPromptFormat" src="about:blank" class="frame" style="width:100%; height:18px;" frameborder="0"></iframe>
			</td>
		</tr>
	</table>
</ext:form>