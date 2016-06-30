<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertField">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		var imageInstead = document.createElement("object"); //替代图片
		var field;
		window.onload = function() {
			window.setTimeout(function() {
				var fields = DropdownField.getListValues('field').split("\0");
				var obj = DomUtils.getParentNode(dialogArguments.selectedElement, 'a');
				if(obj && obj.id=="field") {
					var fieldName = StringUtils.getPropertyValue(obj.urn, "name");
					for(var i=0; i < fields.length; i++) {
						if(fields[i].trim().indexOf("'" + fieldName + "'")==-1) {
							continue;
						}
						DropdownField.setValueByIndex("field", i);
						onFieldChanged();
						var urn = obj.getAttribute("urn");
						if((field.type=="imageName") || urn.indexOf("imageWidth")!=-1) { //图片字段
							setImageFieldProperties(obj);
						}
						else if((field.type=="viodeName") || urn.indexOf("videoWidth")!=-1) { //视频字段
							setVideoFieldProperties(obj);
						}
						else { //其他字段
							setFieldProperties(obj);
						}
						return;
					}
				}
				DropdownField.setValueByIndex("field", 0);
				onFieldChanged();
			}, 10);
		};
		function doOk() {
			if(!field) {
				return;
			}
			var result = false;
			if(field.type=="imageName") {
				result = createImageFieldElement();
			}
			else if(field.type=="videoName") {
				result = createVideoFieldElement();
			}
			else {
				result = createFieldElement();
			}
			if(result) {
				DialogUtils.closeDialog();
			}
		}
		function setFieldProperties(fieldElement) { //设置字段属性
			var fontName = fieldElement.style.fontFamily;
			var fontBold = (fieldElement.style.fontWeight=="bold");
			var fontSize = fieldElement.style.fontSize;
			var fontColor = fieldElement.style.color;
			var properties = fieldElement.getAttribute("urn");
			var link  = StringUtils.getPropertyValue(properties, "link");
			var chineseNumber = StringUtils.getPropertyValue(properties, "chineseNumber");
			var maxLength = StringUtils.getPropertyValue(properties, "maxLength");
			var fieldFormat = StringUtils.getPropertyValue(properties, "fieldFormat");
			
			document.getElementsByName("fontName")[0].value = (fontName ? fontName : "默认"); //字体
			document.getElementsByName("fontBold")[0].checked = fontBold; //是否加粗
			document.getElementsByName("fontSize")[0].value = (fontSize ? fontSize : "默认"); //字体大小
			document.getElementsByName("fontColor")[0].value = (fontColor ? fontColor : "默认"); //字体颜色
			if(document.getElementsByName("link")[0]) {
				document.getElementsByName("link")[link=="true" ? 0 : 1].checked = true; //是否显示连接
			}
			document.getElementsByName("maxLength")[0].value = (maxLength ? maxLength : "全部显示"); //字数
			document.getElementsByName("fontName")[0].value = (fontName ? fontName : "默认"); //字体
			document.getElementsByName("chineseNumber")[chineseNumber=="true" ? 0 : 1].checked = true; //中文数字
			
			//设置显示格式
			document.getElementsByName("fieldFormat")[0].value = (fieldFormat ? fieldFormat : "默认");
			if(field.type=="date" || field.type=="timestamp") {
				if(fieldFormat && !DropdownField.setValue(field.type + "FieldFormat", fieldFormat)) {
					DropdownField.setValue(field.type + "FieldFormat", 'custom');
				}
				onDateTimeFormatChanged();
			}
			//设置替代图片
			if(field.itemsText && field.itemsText!="") {
				var values = field.itemsText.split("\0");
				for(var i=0; i<values.length; i++) {
					var itemValues = StringUtils.trim(values[i]).split("|");
					var attributeName = "imageInstead_" + field.name + "_" + itemValues[itemValues.length - 1];
					imageInstead.setAttribute(attributeName, StringUtils.getPropertyValue(properties, attributeName));
				}
				onImageInsteadValueChanged();
				document.getElementsByName("insteadImageWidth")[0].value = StringUtils.getPropertyValue(properties, "insteadImageWidth");; //替代图片宽度
				document.getElementsByName("insteadImageHeight")[0].value = StringUtils.getPropertyValue(properties, "insteadImageHeight");; //替代图片高度
				//对齐方式
				var insteadImageAlign = StringUtils.getPropertyValue(properties, "insteadImageAlign");
				DropdownField.setValue('insteadImageAlign', insteadImageAlign);
			}
		}
		function setImageFieldProperties(imageFieldElement) { //设置图片字段属性
			var properties = imageFieldElement.getAttribute("urn");
			var imageWidth = StringUtils.getPropertyValue(properties, "imageWidth");
			if(imageWidth!="0") {
				document.getElementsByName("imageWidth")[0].value = imageWidth; //宽度
			}
			var imageHeight = StringUtils.getPropertyValue(properties, "imageHeight");
			if(imageHeight!="0") {
				document.getElementsByName("imageHeight")[0].value = imageHeight; //高度
			}
			//是否允许裁剪
			document.getElementsByName("clipEnabled")[0].checked = "true"==StringUtils.getPropertyValue(properties, "clipEnabled");
			//对齐方式
			var imageAlign = StringUtils.getPropertyValue(properties, "imageAlign");
			DropdownField.setValue('imageAlign', imageAlign);
			//显示链接
			if(document.getElementsByName("imageLink")[0]) {
				document.getElementsByName("imageLink")[1].checked = StringUtils.getPropertyValue(properties, "link")!="true";
			}
		}
		function setVideoFieldProperties(videoFieldElement) { //设置视频字段属性
			var properties = videoFieldElement.getAttribute("urn");
			var videoWidth = StringUtils.getPropertyValue(properties, "videoWidth");
			if(videoWidth!="0") {
				document.getElementsByName("videoWidth")[0].value = videoWidth; //宽度
			}
			var videoHeight = StringUtils.getPropertyValue(properties, "videoHeight");
			if(videoHeight!="0") {
				document.getElementsByName("videoHeight")[0].value = videoHeight; //高度
			}
			//对齐方式
			var videoAlign = StringUtils.getPropertyValue(properties, "videoAlign");
			DropdownField.setValue('videoAlign', videoAlign);
			//是否自动播放
			document.getElementsByName("autostart")[0].checked = StringUtils.getPropertyValue(properties, "autostart")=="true";
			//是否显示播放控制条
			document.getElementsByName("controlbar")[0].checked = StringUtils.getPropertyValue(properties, "controlbar")=="true";
			//是否显示截屏
			document.getElementsByName("captureImage")[0].checked = StringUtils.getPropertyValue(properties, "captureImage")=="true";
			//显示链接
			if(document.getElementsByName("videoLink")[0]) {
				document.getElementsByName("videoLink")[1].checked = StringUtils.getPropertyValue(properties, "link")!="true";
			}
			if(document.getElementsByName("captureImage")[0].checked) {
				var showPlayIcon = document.getElementsByName("showPlayIcon");
				for(var i=0; i<showPlayIcon.length; i++) {
					if(showPlayIcon[i].value==StringUtils.getPropertyValue(properties, "showPlayIcon")) {
						showPlayIcon[i].checked = true;
						break;
					}
				}
				document.getElementsByName("playIconURL")[0].value = StringUtils.getPropertyValue(properties, "playIconURL");
				document.getElementsByName("playIconWidth")[0].value = StringUtils.getPropertyValue(properties, "playIconWidth");
				document.getElementsByName("playIconHeight")[0].value = StringUtils.getPropertyValue(properties, "playIconHeight");
				DropdownField.setValue("playIconPosition", StringUtils.getPropertyValue(properties, "playIconPosition"));
				document.getElementsByName("playIconXMargin")[0].value = StringUtils.getPropertyValue(properties, "playIconXMargin");
				document.getElementsByName("playIconYMargin")[0].value = StringUtils.getPropertyValue(properties, "playIconYMargin");
			}
			onCaptureImageChanged();
		}
		function createFieldElement() {
			if(dialogArguments.window==dialogArguments.editor.editorWindow) { //不是为记录列表插入字段
				dialogArguments.editor.saveUndoStep();
			}
			var fontName = document.getElementsByName("fontName")[0].value;
			var fontBold = document.getElementsByName("fontBold")[0].checked;
			var fontSize = document.getElementsByName("fontSize")[0].value;
			var fontColor = document.getElementsByName("fontColor")[0].value;
			var fieldFormat = document.getElementsByName("fieldFormat")[0].value;
			var link = (document.getElementsByName("link")[0] ? document.getElementsByName("link")[0].checked : false);
			var chineseNumber = document.getElementsByName("chineseNumber")[0].checked;
			var maxLength = document.getElementsByName("maxLength")[0].value;
			
			var objField = createLinkElement();
			if(!objField) {
				return false;
			}
			objField.id = "field";
			var style = (fontName=="默认" ? "" : "font-family:" + fontName + ";");
			style += (!fontBold ? "" : "font-weight:bold;");
			style += (fontSize=="默认" ? "" : "font-size:" + fontSize + ";");
			style += (fontColor=="默认" ? "" : "color:" + fontColor + ";");
			if(style!="") {
				objField.style.cssText = style;
			}
			else {
				objField.removeAttribute("style");
			}
			var urn = "name=" + field.name;
			urn += (!link ? "" : "&link=true");
			urn += (!chineseNumber ? "" : "&chineseNumber=true");
			urn += (maxLength=="全部显示" ? "" : "&maxLength=" + maxLength);
			urn += (fieldFormat=="默认" ? "" : "&fieldFormat=" + fieldFormat);
			//设置替代图片
			if(field.itemsText && field.itemsText!='') {
				var values = field.itemsText.split("\0");
				for(var i = 0; i<values.length; i++) {
					var itemValues = StringUtils.trim(values[i]).split("|");
					var attributeName = "imageInstead_" + field.name + "_" + itemValues[itemValues.length-1];
					var imageURL = imageInstead.getAttribute(attributeName);
					if(imageURL && imageURL!="") {
						urn += "&" + attributeName + "=" + StringUtils.encodePropertyValue(imageURL);
					}
				}
				urn += "&insteadImageWidth=" + document.getElementsByName("insteadImageWidth")[0].value; //替代图片宽度
				urn += "&insteadImageHeight=" + document.getElementsByName("insteadImageHeight")[0].value; //替代图片高度
				urn += "&insteadImageAlign=" + document.getElementsByName("insteadImageAlign")[0].value;//对齐方式
			}
			objField.innerHTML = "&lt;" + field.title + "&gt;";
			objField.setAttribute("urn", urn);
			return true;
		}
		function createImageFieldElement() {
			var imageWidth = document.getElementsByName("imageWidth")[0].value;
			if(imageWidth=="原始") {
				imageWidth = 0;
			}
			else {
				imageWidth = Number(imageWidth);
				if(isNaN(imageWidth)) {
					alert("图片宽度设置不正确");
					return;
				}
			}
			var imageHeight = document.getElementsByName("imageHeight")[0].value;
			if(imageHeight=="原始") {
				imageHeight = 0;
			}
			else {
				imageHeight = Number(imageHeight);
				if(isNaN(imageHeight)) {
					alert("图片高度设置不正确");
					return;
				}
			}
			var link = (document.getElementsByName("imageLink")[0] ? document.getElementsByName("imageLink")[0].checked : false);
			var imageAlign = document.getElementsByName("imageAlign")[0].value;
			
			var objField = createLinkElement();
			if(!objField) {
				return false;
			}
			objField.innerHTML = '<' + field.title + '>';
			objField.id = "field";
			var urn = "name=" + field.name + "&imageWidth=" + imageWidth + "&imageHeight=" + imageHeight + "&imageAlign=" + imageAlign;
			//是否允许裁剪
			if(document.getElementsByName("clipEnabled")[0].checked) {
				urn += "&clipEnabled=true";
			}
			urn += (!link ? "" : "&link=true");
			objField.setAttribute("urn", urn);
			return true;
		}
		function createVideoFieldElement() {
			var videoWidth = document.getElementsByName("videoWidth")[0].value;
			if(videoWidth=="原始") {
				videoWidth = 0;
			}
			else {
				videoWidth = Number(videoWidth);
				if(isNaN(videoWidth)) {
					alert("宽度设置不正确");
					return;
				}
			}
			var videoHeight = document.getElementsByName("videoHeight")[0].value;
			if(videoHeight=="原始") {
				videoHeight = 0;
			}
			else {
				videoHeight = Number(videoHeight);
				if(isNaN(videoHeight)) {
					alert("高度设置不正确");
					return;
				}
			}
			var captureImage = document.getElementsByName("captureImage")[0].checked;
			var link = (document.getElementsByName("videoLink")[0] ? document.getElementsByName("videoLink")[0].checked : false);
			var autostart = document.getElementsByName("autostart")[0].checked;
			var controlbar = document.getElementsByName("controlbar")[0].checked;
			var videoAlign = document.getElementsByName("videoAlign")[0].value;
			var showPlayIcon = document.getElementsByName("showPlayIcon");
			for(var i=0; i<showPlayIcon.length; i++) {
				if(showPlayIcon[i].checked) {
					showPlayIcon = showPlayIcon[i].value;
					break;
				}
			}
			var playIconURL = document.getElementsByName("playIconURL")[0].value;
			if(showPlayIcon=="custom" && playIconURL=="") {
				alert("播放图标未设置");
				return;
			}
			var playIconWidth = document.getElementsByName("playIconWidth")[0].value;
			var playIconHeight = document.getElementsByName("playIconHeight")[0].value;
			var playIconPosition = document.getElementsByName("playIconPosition")[0].value;
			var playIconXMargin = document.getElementsByName("playIconXMargin")[0].value;
			var playIconYMargin = document.getElementsByName("playIconYMargin")[0].value;
			
			var objField = createLinkElement();
			if(!objField) {
				return false;
			}
			objField.innerHTML = '<' + field.title + '>';
			objField.id = "field";
			var urn = "name=" + field.name + "&videoWidth=" + videoWidth + "&videoHeight=" + videoHeight + "&videoAlign=" + videoAlign;
			urn += (!autostart ? "" : "&autostart=true");
			urn += (!controlbar ? "" : "&controlbar=true");
			urn += (!captureImage ? "" : "&captureImage=true");
			urn += (!link ? "" : "&link=true");
			if(captureImage) {
				urn += "&showPlayIcon=" + showPlayIcon;
				urn += "&playIconURL=" + playIconURL;
				urn += "&playIconWidth=" + playIconWidth;
				urn += "&playIconHeight=" + playIconHeight;
				urn += "&playIconPosition=" + playIconPosition;
				urn += "&playIconXMargin=" + playIconXMargin;
				urn += "&playIconYMargin=" + playIconYMargin;
			}
			objField.setAttribute("urn", urn);
			return true;
		}
		function createLinkElement() {
			var objLink = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!objLink) {
				objLink = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!objLink) {
					alert('请重新选择插入的位置');
				}
			}
			return objLink;
		}
		function onFieldChanged() {
			field = document.getElementsByName("field")[0].value;
			if(field=="") {
				return;
			}
			eval("field=" + field);
			document.getElementById("fieldConfig").style.display = (field.type=="imageName" || field.type=="videoName" ? "none" : "");
			document.getElementById("imageFieldConfig").style.display = (field.type=="imageName" ? "" : "none");
			document.getElementById("videoFieldConfig").style.display = (field.type=="videoName" ? "" : "none");
			if(field.type!="imageName" && field.type!="videoName") { //不是图片或者视频
				//格式配置
				var fieldFormatTd = document.getElementById("fieldFormatTd");
				fieldFormatTd.style.display = (field.type!="date" && field.type!="timestamp" ? "" : "none");
				fieldFormatTd.style.padding = "0px 0px 0px 0px";
				fieldFormatTd.width = "100%";
				document.getElementsByName("fieldFormat")[0].value = "默认";

				//日期格式配置
				var dateFieldFormatTd = document.getElementById("dateFieldFormatTd");
				dateFieldFormatTd.style.display = (field.type=="date" ? "" : "none");
				dateFieldFormatTd.width = "100%";
				
				//时间格式配置
				var timestampFieldFormatTd = document.getElementById("timestampFieldFormatTd");
				timestampFieldFormatTd.style.display = (field.type=="timestamp" ? "" : "none");
				timestampFieldFormatTd.width = "100%";
				
				DropdownField.setValue("dateFieldFormat", "yyyy-MM-dd");
				DropdownField.setValue("timestampFieldFormat", "yyyy-MM-dd");
				if(field.type=="date" || field.type=="timestamp") {
					document.getElementsByName("fieldFormat")[0].value = "yyyy-MM-dd";
				}
				
				//中文数字
				document.getElementById('trChineseNumber').style.display = field.type=="number" ? "" : "none";
				
				var trImageInstead = document.getElementById('trImageInstead');
				if(!field.itemsText || field.itemsText=='') {
					trImageInstead.style.display = 'none';
					trImageInstead.cells[1].style.display = 'none';
				}
				else {
					trImageInstead.style.display = '';
					trImageInstead.cells[1].style.display = '';
					DropdownField.setListValues("imageInsteadValue", field.itemsText);
					DropdownField.setValueByIndex("imageInsteadValue", 0);
					onImageInsteadValueChanged();
				}
			}
			DialogUtils.adjustDialogSize();
		}
		function onDateTimeFormatChanged() {
			var format = document.getElementsByName(field.type + "FieldFormat")[0].value;
			if(format=="custom") {
				document.getElementById(field.type + 'FieldFormatTd').width = "48%";
				document.getElementById('fieldFormatTd').width = "48%";
				document.getElementById('fieldFormatTd').style.padding = "0px 1px 0px 3px";
				document.getElementById('fieldFormatTd').style.display = "";
			}
			else {
				document.getElementsByName("fieldFormat")[0].value = format;
				document.getElementById(field.type + 'FieldFormatTd').width = "100%";
				document.getElementById('fieldFormatTd').style.display = "none";
			}
		}
		function onImageInsteadValueChanged() {
			var imageInsteadValue = document.getElementsByName('imageInsteadValue')[0].value;
			var imageURL = imageInstead.getAttribute("imageInstead_" + field.name + "_" + imageInsteadValue);
			document.getElementsByName("insteadImageURL")[0].value = imageURL ? imageURL : "";
		}
		function selectInsteadImage() { //选择替换图片
			DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('images', 'setInsteadImageURL("{URL}")'), 640, 400);
		}
		function setInsteadImageURL(imageURL) {
			document.getElementsByName("insteadImageURL")[0].value = imageURL;
			var imageInsteadValue = document.getElementsByName("imageInsteadValue")[0].value;
			imageInstead.setAttribute("imageInstead_" + field.name + "_" + imageInsteadValue, imageURL);
		}
		function onCaptureImageChanged() {
			var captureImage = document.getElementsByName("captureImage");
			document.getElementById("videoWidthLabel").innerHTML = (captureImage[0].checked ? "截屏" : "播放器") + "宽度：";
			document.getElementById("videoHeightLabel").innerHTML = (captureImage[0].checked ? "截屏" : "播放器") + "高度：";
			document.getElementById("trAutostart").style.display = (captureImage[0].checked ? "none" : "");
			document.getElementById("trControlbar").style.display = (captureImage[0].checked ? "none" : "");
			document.getElementById("trPlayIcon").style.display = (!captureImage[0].checked ? "none" : "");
			document.getElementById("trPlayIconPosition").style.display = (!captureImage[0].checked ? "none" : "");
			document.getElementById("trPlayIconXMargin").style.display = (!captureImage[0].checked ? "none" : "");
			document.getElementById("trPlayIconYMargin").style.display = (!captureImage[0].checked ? "none" : "");
			onPlayIconChanged();
			DialogUtils.adjustDialogSize();
		}
		function onPlayIconChanged() {
			document.getElementById("tdPlayIconURL").style.display = document.getElementsByName("showPlayIcon")[2].checked ? "" : "none";
		}
		function selectPlayIcon() {
			DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('images', 'setPlayIconURL("{URL}", {WIDTH}, {HEIGHT});'), 640, 400);
		}
		function setPlayIconURL(url, width, height) {
			document.getElementsByName("playIconURL")[0].value = url;
			document.getElementsByName("playIconWidth")[0].value = width;
			document.getElementsByName("playIconHeight")[0].value = height;
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed;">
		<col width="85px" align="right">
		<col>
		<tr>
			<td>选择字段：</td>
			<td><ext:field property="field" onchange="onFieldChanged()"/></td>
		</tr>
	</table>
	
	<table id="fieldConfig" border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed;">
		<col width="85px" align="right">
		<col>
		<tr>
			<td>字体：</td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="100%"><ext:field property="fontName"/></td>
						<td style="padding-left:3px" nowrap="nowrap"><ext:field property="fontBold"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>字体大小：</td>
			<td><ext:field property="fontSize"/></td>
		</tr>
		<tr>
			<td>颜色：</td>
			<td><ext:field property="fontColor"/></td>
		</tr>
		<ext:equal value="true" property="linkable">
			<tr>
				<td>显示链接：</td>
				<td><ext:field property="link"/></td>
			</tr>
		</ext:equal>
		<tr>
			<td>最大字节数：</td>
			<td><ext:field property="maxLength"/></td>
		</tr>
		<tr id="trChineseNumber" style="display:none">
			<td>中文数字：</td>
			<td><ext:field property="chineseNumber"/></td>
		</tr>
		<tr>
			<td>格式：</td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="100%" id="dateFieldFormatTd" style="display:none"><ext:field property="dateFieldFormat" onchange="onDateTimeFormatChanged()"/></td>
						<td width="100%" id="timestampFieldFormatTd" style="display:none"><ext:field property="timestampFieldFormat" onchange="onDateTimeFormatChanged()"/></td>
						<td width="100%" id="fieldFormatTd"><ext:field property="fieldFormat"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="trImageInstead" style="display:none">
			<td valign="top" style="padding-top:8px">替换图片：</td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="100px"><ext:field property="imageInsteadValue" onchange="onImageInsteadValueChanged()"/></td>
						<td style="padding-left: 3px"><ext:field property="insteadImageURL" onchange="setUrl(this.value);"/></td>
					</tr>
				</table>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" style="margin-top: 5px">
					<tr>
						<td nowrap="nowrap">宽度：</td>
						<td width="64px"><ext:field property="insteadImageWidth"/></td>
						<td nowrap="nowrap">&nbsp;&nbsp;高度：</td>
						<td width="64px"><ext:field property="insteadImageHeight"/></td>
						<td nowrap="nowrap">&nbsp;&nbsp;对齐方式：</td>
						<td><ext:field property="insteadImageAlign"/></td>
					</tr>
				</table>
			</td>
		</tr>
    </table>
    
    <table id="imageFieldConfig" border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed;display:none">
		<col width="85px" align="right">
		<col>
		<tr>
			<td>宽度：</td>
			<td><ext:field property="imageWidth"/></td>
		</tr>
		<tr>
			<td>高度：</td>
			<td><ext:field property="imageHeight"/></td>
		</tr>
		<tr>
			<td></td>
			<td><ext:field property="clipEnabled"/></td>
		</tr>
		<ext:equal value="true" property="linkable">
			<tr>
				<td>显示链接：</td>
				<td><ext:field property="imageLink"/></td>
			</tr>
		</ext:equal>
		<tr>
			<td>对齐方式：</td>
			<td><ext:field property="imageAlign"/></td>
		</tr>
    </table>
    
    <table id="videoFieldConfig" border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed;display:none">
		<col width="85px" align="right">
		<col>
		<tr>
			<td>显示截图：</td>
			<td><ext:field property="captureImage" onclick="onCaptureImageChanged()"/></td>
		</tr>
		<ext:equal value="true" property="linkable">
			<tr>
				<td>显示链接：</td>
				<td><ext:field property="videoLink"/></td>
			</tr>
		</ext:equal>
		<tr>
			<td id="videoWidthLabel">播放器宽度：</td>
			<td><ext:field property="videoWidth"/></td>
		</tr>
		<tr>
			<td id="videoHeightLabel">播放器高度：</td>
			<td><ext:field property="videoHeight"/></td>
		</tr>
		<tr id="trAutostart">
			<td>自动播放：</td>
			<td><ext:field property="autostart"/></td>
		</tr>
		<tr id="trControlbar">
			<td>播放控制条：</td>
			<td><ext:field property="controlbar"/></td>
		</tr>
		<tr>
			<td>对齐方式：</td>
			<td><ext:field property="videoAlign"/></td>
		</tr>
		<tr id="trPlayIcon" style="display:none">
			<td>播放图标：</td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td nowrap="nowrap" width="220px"><ext:field property="showPlayIcon" onclick="onPlayIconChanged()"/></td>
						<td id="tdPlayIconURL" style="display:none"><ext:field property="playIconURL"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="trPlayIconPosition" style="display:none">
			<td>显示位置：</td>
			<td><ext:field property="playIconPosition"/></td>
		</tr>
		<tr id="trPlayIconXMargin" style="display:none">
			<td>水平边距：</td>
			<td><ext:field property="playIconXMargin"/></td>
		</tr>
		<tr id="trPlayIconYMargin" style="display:none">
			<td>垂直边距：</td>
			<td><ext:field property="playIconYMargin"/></td>
		</tr>
    </table>
</ext:form>