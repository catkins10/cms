<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertHeadlineImage">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj || obj.id!="headlineImage") {
				return;
			}
			var properties = obj.getAttribute("urn");
			document.getElementsByName("width")[0].value = StringUtils.getPropertyValue(properties, "width"); //宽度
			document.getElementsByName("height")[0].value = StringUtils.getPropertyValue(properties, "height"); //高度
			//字体
			DropdownField.setValue("font", StringUtils.getPropertyValue(properties, "font"));
			document.getElementsByName("bold")[0].checked = "true"==StringUtils.getPropertyValue(properties, "bold"); //粗体
			document.getElementsByName("italic")[0].checked = "true"==StringUtils.getPropertyValue(properties, "italic"); //斜体
			document.getElementsByName("minCharCount")[0].value = StringUtils.getPropertyValue(properties, "minCharCount"); //最少字节数
			document.getElementsByName("maxCharCount")[0].value = StringUtils.getPropertyValue(properties, "maxCharCount"); //最大字节数
			document.getElementsByName("ellipsis")[0].value = StringUtils.getPropertyValue(properties, "ellipsis"); //超出时末尾添加
			document.getElementsByName("textColor")[0].value = StringUtils.getPropertyValue(properties, "textColor"); //文本颜色
			var backgroundTransparent = 'true'==StringUtils.getPropertyValue(properties, "backgroundTransparent"); //背景透明
			document.getElementsByName("backgroundTransparent")[0].checked = backgroundTransparent;
			document.getElementsByName("backgroundColor")[0].value = StringUtils.getPropertyValue(properties, "backgroundColor"); //背景颜色
			backgroundTransparentChanged(backgroundTransparent);
		}
		function doOk() {
			//宽度
			var width = Number(document.getElementsByName("width")[0].value);
			if(isNaN(width) || width==0) {
				alert("显示宽度设置不正确");
				return;
			}
			//高度
			var height = Number(document.getElementsByName("height")[0].value);
			if(isNaN(height) || height==0) {
				alert("显示高度设置不正确");
				return;
			}
			//最少字节数
			var minCharCount = Number(document.getElementsByName("minCharCount")[0].value);
			if(isNaN(minCharCount) || minCharCount==0) {
				alert("最少字节数不正确");
				return;
			}
			//最大字节数
			var maxCharCount = Number(document.getElementsByName("maxCharCount")[0].value);
			if(isNaN(maxCharCount) || maxCharCount==0) {
				alert("最大字节数设置不正确");
				return;
			}
			var ellipsis = document.getElementsByName("ellipsis")[0].value; //超出时末尾添加
			//文本颜色
			var textColor = document.getElementsByName("textColor")[0].value;
			if(textColor=="") {
				alert("文本颜色设置不正确");
				return;
			}
			//背景颜色
			var backgroundColor = document.getElementsByName("backgroundColor")[0].value;
			//开始插入头条图片配置
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
			}
			obj.innerHTML = "<标题图片>";
			obj.id = "headlineImage";
			obj.setAttribute("urn", "width=" + width +
									"&height=" + height +
									"&font=" + document.getElementsByName("font")[0].value +
									"&bold=" + document.getElementsByName("bold")[0].checked +
									"&italic=" + document.getElementsByName("italic")[0].checked +
									"&minCharCount=" + minCharCount +
									"&maxCharCount=" + maxCharCount +
									"&ellipsis=" +  ellipsis +
									"&textColor=" + textColor +
									"&backgroundTransparent=" + document.getElementsByName("backgroundTransparent")[0].checked +
									"&backgroundColor=" + backgroundColor);
			DialogUtils.closeDialog();
		}
		function backgroundTransparentChanged(transparent) {
			document.getElementById('tdBackgroundColor').style.display = transparent ? 'none' : '';
		}
		function computeCharCount() {
			if(window.charCountChangeDiaable) {
				return;
			}
			var width = Number(document.getElementsByName('width')[0].value);
			var height = Number(document.getElementsByName('height')[0].value);
			if(isNaN(width) || isNaN(height) || width==0 || height==0) {
				return;
			}
			document.getElementsByName('minCharCount')[0].value = Math.ceil(width/height) * 2;
			document.getElementsByName('maxCharCount')[0].value = Math.floor(width / height * 3) * 2;
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">显示宽度：</td>
			<td width="50%"><ext:field property="width" onchange="computeCharCount()"/></td>
			<td nowrap="nowrap" align="right">显示高度：</td>
			<td width="50%"><ext:field property="height" onchange="computeCharCount()"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">文本字体：</td>
			<td><ext:field property="font"/></td>
			<td colspan="2"><ext:field property="bold"/>&nbsp;<ext:field property="italic"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">最少字节数：</td>
			<td><ext:field property="minCharCount" onchange="window.charCountChangeDiaable=true;"/></td>
			<td nowrap="nowrap" align="right">最大字节数：</td>
			<td><ext:field property="maxCharCount" onchange="window.charCountChangeDiaable=true;"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">超出时末尾添加：</td>
			<td colspan="3"><ext:field property="ellipsis"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">文本颜色：</td>
			<td colspan="3"><ext:field property="textColor"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">背景颜色：</td>
			<td colspan="3">
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td nowrap="nowrap"><ext:field property="backgroundTransparent" onclick="backgroundTransparentChanged(checked)"/>&nbsp;</td>
						<td width="100%" id="tdBackgroundColor" style="display:none"><ext:field property="backgroundColor"/></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</ext:form>