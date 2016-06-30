<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	/*
	//获取样式名称列表,格式:样式1名称|标题,样式2名称|标题,由调用者实现
	function getStyleNames();
	//获取默认的样式,由调用者实现
	function getDefaultStyle(styleName);
	//获取页面元素属性(urn),由调用者实现
	function getPageElementProperties();
	*/
	//选取图片,并将url插入到样式配置框中
	function selectImage() {
		document.getElementsByName('styleEditor')[0].focus();
		DialogUtils.openDialog(DialogUtils.getDialogArguments().editor.getAttachmentSelectorURL('images', 'insertStyleImage("{URL}")'), 640, 400);
	}
	function insertStyleImage(imageUrl) {
		FormUtils.pasteText('styleEditor', imageUrl);
	}
	//保存配置的样式
	function saveStyle() {
		var styleName = document.getElementsByName("styleName")[0].value;
		if(styleName=="") {
			return;
		}
		eval('window.' + styleName + 'Style=document.getElementsByName("styleEditor")[0].value;');
	}
	//样式名称选择
	function onStyleNameChange() {
		document.getElementsByName("styleEditor")[0].value = getStyle(document.getElementsByName("styleName")[0].value);
	}
	//获取样式
	function getStyle(styleName) {
		if(styleName=="") {
			return "";
		}
		var style;
		eval('style=window.' + styleName + 'Style;'); //获取已保存的样式
		if(style) {
			return style;
		}
		//从页面元素属性获取样式配置
		var properties;
		try {
			properties = getPageElementProperties();
		}
		catch(e) {
		
		}
		if(!properties || (style=StringUtils.getPropertyValue(properties, styleName + "Style"))=="") {
			try {
				style = getDefaultStyle(styleName);
			}
			catch(e) {
		
			}
		}
		return (style ? style : "");
	}
	//生成属性值列表
	function generateStylesProperties() {
		var properties = "";
		var styleNames = getStyleNames().split("\0");
		for(var i=0; i<styleNames.length; i++) {
			var values = styleNames[i].split('|');
			var style = getStyle(values[1]);
			if(style!="") {
				properties = (properties=="" ? "" : properties + "&") + values[1] + "Style=" + StringUtils.encodePropertyValue(style.replace(/[\r\n\"\']/gi, ''));
			}
		}
		return properties;
	}
</script>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="200px">
			<script>new DropdownField('<input type="text" name="styleName_title" value="" onchange="onStyleNameChange()" readonly="readonly" class="field" title="样式">', getStyleNames(), 'styleName', 'styleName_title', 'field', 'null', '', '', null, null);</script>
			<input name="styleName" type="hidden"/>
		</td>
		<td>&nbsp;<input type="button" class="button" style="width:70px" onclick="selectImage()" value="插入图片"></td>
	</tr>
	<tr>
		<td colspan="2">
			<textarea name="styleEditor" class="field" style="margin-top:3px; height:80px !important; font-size:12px" onchange="saveStyle()"></textarea>
		</td>
	</tr>
</table>