<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/recordListControlBar">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			//设置隔符自定义IFRAME
			var doc = window.frames["controlBarFormat"].document;
			doc.open();
			doc.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">');
			doc.write('<html style="height:100%">');
			doc.write('<head>');
			doc.write('<link href="' + RequestUtils.getContextPath() + '/cms/css/slideShow.css" rel="stylesheet" type="text/css">');
			doc.write('</head>');
			doc.write('<body onkeydown="return true;if(event.keyCode==13)return false;" style="height:100%; border-style:none; background-color:#ffffff; overflow:hidden; margin:0px;">');
			doc.write('<table border="0" cellpadding="3" cellspacing="0" width="100%" height="100%">');
			doc.write('	<tr>');
			doc.write('		<td>');
			doc.write('			<div contentEditable="true" class="slideShow"></div>');
			doc.write('		</td>');
			doc.write('	</tr>');
			doc.write('</table>');
			doc.write('</body>');
			doc.write('</html>');
			doc.close();
			//复制模板的样式
			CssUtils.cloneStyle(dialogArguments.document, doc);
			
			//设置记录列表为空时的提示
			doc = window.frames["nextPreviousButtonFormat"].document;
			doc.open();
			doc.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">');
			doc.write('<html style="height:100%">');
			doc.write('<head>');
			doc.write('<link href="' + RequestUtils.getContextPath() + '/cms/css/slideShow.css" rel="stylesheet" type="text/css">');
			doc.write('</head>');
			doc.write('<body onkeydown="return true;if(event.keyCode==13)return false;" style="height:100%;border-style:none; background-color:#ffffff; overflow:hidden; margin:0px;">');
			doc.write('<table border="0" cellpadding="3" cellspacing="0" width="100%" height="100%" style="table-layout:fixed;">');
			doc.write('	<tr>');
			doc.write('		<td width="50%" style="border-right:#999999 1px solid;">');
			doc.write('			<div class="slideShow" id="previousButtonFormatEditor" contentEditable="true"></div>');
			doc.write('		</td>');
			doc.write('		<td width="50%">');
			doc.write('			<div class="slideShow" id="nextButtonFormatEditor" contentEditable="true" style="text-align:right"></div>');
			doc.write('		</td>');
			doc.write('	</tr>');
			doc.write('</table>');
			doc.write('</body>');
			doc.write('</html>');
			doc.close();
			//复制模板的样式
			CssUtils.cloneStyle(dialogArguments.document, doc);
			
			document.getElementsByName('manualSwitch')[0].checked = dialogArguments.controlBar.manualSwitch; //是否手工切换
			document.getElementsByName('autoScaling')[0].checked = dialogArguments.controlBar.autoScaling; //是否自适应大小
			DropdownField.setValue("position", dialogArguments.controlBar.controlBarPosition); //显示位置
			document.getElementsByName('xMargin')[0].value = dialogArguments.controlBar.controlBarXMargin || 0; //水平边距
			document.getElementsByName('yMargin')[0].value = dialogArguments.controlBar.controlBarYMargin || 0; //垂直边距
			DropdownField.setValue("scrollDirection", dialogArguments.controlBar.scrollDirection); //滚动方向
			document.getElementsByName('controlBarRecordSpacing')[0].value = dialogArguments.controlBar.controlBarRecordSpacing || 0; //控制栏记录分隔距离
			DropdownField.setValue("mouseOverControlBarRecord", dialogArguments.controlBar.mouseOverControlBarRecord); //鼠标经过控制记录时的动作
			DropdownField.setValue("clickControlBarRecord", dialogArguments.controlBar.clickControlBarRecord); //点击控制记录时的动作
			setSource('controlBarFormat', dialogArguments.controlBar.controlBarFormat || ""); //控制栏格式
			DropdownField.setValue("imageClickAction", dialogArguments.controlBar.imageClickAction); //点击图片操作
			document.getElementsByName('switchByKey')[0].checked = dialogArguments.controlBar.switchByKey;
			DropdownField.setValue("displayNextPreviousButton", dialogArguments.controlBar.displayNextPreviousButton); //是否显示前一页、后一页按钮
			setSource('nextPreviousButtonFormat', dialogArguments.controlBar.nextPreviousButtonFormat || ""); //前一页、后一页按钮格式
		};
		function doOk() {
			dialogArguments.controlBar.manualSwitch = document.getElementsByName("manualSwitch")[0].checked; //是否手工切换
			dialogArguments.controlBar.autoScaling = document.getElementsByName("autoScaling")[0].checked; //是否自适应大小
			dialogArguments.controlBar.controlBarPosition = document.getElementsByName("position")[0].value; //显示位置
			dialogArguments.controlBar.controlBarXMargin = document.getElementsByName("xMargin")[0].value; //水平边距
			dialogArguments.controlBar.controlBarYMargin = document.getElementsByName("yMargin")[0].value; //垂直边距
			dialogArguments.controlBar.scrollDirection = document.getElementsByName("scrollDirection")[0].value; //滚动方向
			dialogArguments.controlBar.controlBarRecordSpacing = document.getElementsByName("controlBarRecordSpacing")[0].value; //控制栏记录分隔距离
			dialogArguments.controlBar.mouseOverControlBarRecord = document.getElementsByName("mouseOverControlBarRecord")[0].value; //鼠标经过控制记录时的动作
			dialogArguments.controlBar.clickControlBarRecord = document.getElementsByName("clickControlBarRecord")[0].value; //点击控制记录时的动作
			dialogArguments.controlBar.controlBarFormat = getSource("controlBarFormat"); //控制栏格式
			dialogArguments.controlBar.imageClickAction = document.getElementsByName("imageClickAction")[0].value; //点击图片操作
			dialogArguments.controlBar.switchByKey = document.getElementsByName('switchByKey')[0].checked; //左右方向键翻阅
			dialogArguments.controlBar.displayNextPreviousButton = document.getElementsByName("displayNextPreviousButton")[0].value; //是否显示前一页、后一页按钮
			dialogArguments.controlBar.nextPreviousButtonFormat = getSource("nextPreviousButtonFormat"); //前一页、后一页按钮格式
			DialogUtils.closeDialog();
		}
		function viewSource(button, editorName) {
			var sourceMode = false;
			var divSource = document.getElementById("div" + editorName.substring(0,1).toUpperCase() + editorName.substring(1) + "Source");
			if(divSource.style.display=='none') {
				document.getElementsByName(editorName + "Source")[0].value = getSource(editorName);
				sourceMode = true;
			}
			else {
				setSource(editorName, document.getElementsByName(editorName + "Source")[0].value);
			}
			button.value = sourceMode ? "回到编辑模式" : "查看源代码";
			divSource.style.display = sourceMode ? "" : "none";
			document.getElementById(editorName).style.display = !sourceMode ? "" : "none";
			for(var previousNode = button.parentNode.previousSibling; previousNode; previousNode=previousNode.previousSibling) {
				if(previousNode.tagName=='TD') {
					previousNode.getElementsByTagName('input')[0].disabled = sourceMode;
				}
			}
		}
		function getSource(editorName) {
			if(document.getElementById(editorName).style.display=='none') { //源码模式
				return document.getElementsByName(editorName + "Source")[0].value;
			}
			var doc = window.frames[editorName].document;
			if('controlBarFormat'==editorName) {
				return doc.getElementsByTagName('div')[0].innerHTML;
			}
			else {
				return '<div id="previousButtonFormat">' + doc.getElementById('previousButtonFormatEditor').innerHTML + '</div>' +
					   '<div id="nextButtonFormat">' + doc.getElementById('nextButtonFormatEditor').innerHTML + '</div>';
			}
		}
		function setSource(editorName, html) {
			var doc = window.frames[editorName].document;
			if('controlBarFormat'==editorName) {
				doc.getElementsByTagName('div')[0].innerHTML = html;
				return;
			}
			if(html=="") {
				doc.getElementById('previousButtonFormatEditor').innerHTML = '<span class=previousImageButton></span>';
				doc.getElementById('nextButtonFormatEditor').innerHTML = '<span class=nextImageButton></span>';
				return;
			}
			var tempDiv = doc.body.insertBefore(doc.createElement('div'));
			tempDiv.innerHTML = html;
			var previousButtonFormat = doc.getElementById('previousButtonFormat');
			doc.getElementById('previousButtonFormatEditor').innerHTML = previousButtonFormat ? previousButtonFormat.innerHTML : '';
			var nextButtonFormat = doc.getElementById('nextButtonFormat');
			doc.getElementById('nextButtonFormatEditor').innerHTML = nextButtonFormat ? nextButtonFormat.innerHTML : '';
			doc.body.removeChild(tempDiv);
		}
		function openInsertFieldDialog() { //打开插入字段对话框
			var args = generateDialogArguments(frames["controlBarFormat"]);
			if(args) {
				dialogArguments.recordListDialog.doOpenInsertFieldDialog(args);
			}
		}
		function generateDialogArguments(window, noFocusPrompt) { //生成对话框参数
			var args = DomUtils.getWindowBookmark(window, noFocusPrompt ? noFocusPrompt : '插入位置不正确，请重新选择');
			if(!args) {
				return null;
			}
			args.editor = dialogArguments.editor;
			return args;
		}
		function selectImage() {
			window.args = DomUtils.getWindowBookmark(window.frames['controlBarFormat'], '位置不正确，请重新选择');
			if(!window.args) {
				return;
			}
			DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('images'), 640, 400);
		}
		function setUrl(imageURL) {
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
		function setAsRecordButton(src) { //设为记录按钮
			var args = DomUtils.getWindowBookmark(window.frames['controlBarFormat'], '位置不正确，请重新选择');
			if(!args) {
				return;
			}
			var obj = DomUtils.getParentNode(args.selectedElement, "div,span,td", args.document.getElementsByTagName('div')[0]);
			if(!obj) {
				alert('位置不正确，请重新选择');
				return;
			}
			PopupMenu.popupMenu("未选中|unselected\0选中|selected", function(menuItemId, menuItemTitle) {
				obj.id = menuItemId + 'Record';
			}, src, '120px');
		}
		function setAsPagingButton(src) { //设为翻页按钮
			var args = DomUtils.getWindowBookmark(window.frames['controlBarFormat'], '位置不正确，请重新选择');
			if(!args) {
				return;
			}
			var obj = DomUtils.getParentNode(args.selectedElement, "div,span,td,a,img", args.document.getElementsByTagName('div')[0]);
			if(!obj) {
				alert('位置不正确，请重新选择');
				return;
			}
			PopupMenu.popupMenu("上一页|previousPage\0下一页|nextPage", function(menuItemId, menuItemTitle) {
				obj.id = menuItemId + 'Button';
			}, src, '120px');
		}
		function loadDefaultStyle(src) {
			PopupMenu.popupMenu("数字|number\0圆点|roundDot\0方点|squareDot\0右侧小图|rightSmallImage\0底部小图|bottomSmallImage", function(menuItemId, menuItemTitle) {
				var html = document.getElementById('controlBarStyle_' + menuItemId).innerHTML;
				if(document.getElementById('divControlBarFormatSource').style.display=='none') {
					setSource('controlBarFormat', html);
				}
				else {
					document.getElementsByName('controlBarFormatSource')[0].value = html;
				}
			}, src, '120px');
		}
	</script>
	<ext:iterate id="controlBarStyle" property="controlBarStyles">
		<div id="controlBarStyle_<ext:write name="controlBarStyle" property="id"/>" style="display:none">
			<ext:write name="controlBarStyle" property="name" filter="false"/>
		</div>
	</ext:iterate>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col>
		<col width="100%">
		<tr>
			<td nowrap="nowrap" align="right">滚动方向：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td width="120px"><ext:field property="scrollDirection"/></td>
						<td nowrap="nowrap">&nbsp;&nbsp;<ext:field property="manualSwitch"/></td>
						<td nowrap="nowrap">&nbsp;&nbsp;<ext:field property="autoScaling"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">显示位置：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td width="120px"><ext:field property="position"/></td>
						<td nowrap="nowrap">&nbsp;&nbsp;水平边距：</td>
						<td width="60px"><ext:field property="xMargin"/></td>
						<td nowrap="nowrap">&nbsp;&nbsp;垂直边距：</td>
						<td width="60px"><ext:field property="yMargin"/></td>
						<td nowrap="nowrap">&nbsp;&nbsp;记录间距：</td>
						<td width="60px"><ext:field property="controlBarRecordSpacing"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:6px" align="right">控制栏：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td><input type="button" class="button" style="width:96px" value="加载预置风格▼" onmousedown="loadDefaultStyle(this)"></td>
						<td style="padding-left:3px"><input type="button" class="button" style="width:96px" value="设为记录按钮▼" onmousedown="setAsRecordButton(this)"></td>
						<td style="padding-left:3px"><input type="button" class="button" style="width:96px" value="设为翻页按钮▼" onmousedown="setAsPagingButton(this)"></td>
						<td style="padding-left:3px"><input type="button" class="button" style="width:66px" value="插入字段" onmousedown="openInsertFieldDialog()"></td>
						<td style="padding-left:3px"><input type="button" class="button" style="width:66px" value="插入图片" onmousedown="selectImage('recordFormat')"></td>
						<td style="padding-left:3px"><input type="button" class="button" value="查看源代码" onmousedown="viewSource(this, 'controlBarFormat')"></td>
						<td width="100%"></td>
					</tr>
					<tr>
						<td style="padding-top:5px" colspan="7">
							<div id="divControlBarFormatSource" style="display:none">
								<ext:field property="controlBarFormatSource" style="width:100%; height:100px !important; word-break:break-all"/>
							</div>
							<iframe name="controlBarFormat" id="controlBarFormat" frameBorder="0" src="about:blank" class="frame" style="width:100%; height:100px;"></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">鼠标经过时：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td width="120px"><ext:field property="mouseOverControlBarRecord"/></td>
						<td nowrap="nowrap">&nbsp;点击时：</td>
						<td width="90px"><ext:field property="clickControlBarRecord"/></td>
						
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">点击图片：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td width="264px"><ext:field property="imageClickAction"/></td>
						<td nowrap="nowrap">&nbsp;<ext:field property="switchByKey"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:6px" align="right">翻阅按钮：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px" width="100%">
					<tr>
						<td width="120px" nowrap="nowrap"><ext:field property="displayNextPreviousButton"/></td>
						<td nowrap="nowrap">&nbsp;<input type="button" class="button" value="查看源代码" onmousedown="viewSource(this, 'nextPreviousButtonFormat')"></td>
						<td width="100%"></td>
					</tr>
					<tr>
						<td colspan="3" style="padding-top:5px">
							<div id="divNextPreviousButtonFormatSource" style="display:none">
								<ext:field property="nextPreviousButtonFormatSource" style="width:100%; height:100px !important; word-break:break-all"/>
							</div>
							<iframe name="nextPreviousButtonFormat" id="nextPreviousButtonFormat" frameBorder="0" src="about:blank" class="frame" style="width:100%; height:100px;"></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
    </table>
</ext:form>