<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertPageNo">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj || obj.id!="pageAction") {
				return;
			}
			var urn = obj.getAttribute("urn");
			if(!urn || urn=="" || urn.indexOf("页码")!=0) {
				return;
			}
			var values = urn.split("|");
			document.getElementsByName("pageNumber")[0].value = values[1];
			if(values.length<=2) {
				return;
			}
			values = values[2].split(";");
			for(var i=0; i<values.length; i++) {
				var style = values[i].split(":");
				if(style[0]=="font-family") {
					document.getElementsByName("fontName")[0].value = style[1];
				}
				else if(style[0]=="font-weight") {
					document.getElementsByName("fontBold")[0].checked = true;
				}
				else if(style[0]=="font-size") {
					document.getElementsByName("fontSize")[0].value = style[1];
				}
				else if(style[0]=="color") {
					document.getElementsByName("fontColor")[0].value = style[1];
				}
			}
		}
		function doOk() {
			var pageNumber = Number(document.getElementsByName("pageNumber")[0].value);
			if(pageNumber+""=="NaN" || pageNumber<=0) {
				alert("显示页码数不能小于1");
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					alert('请重新选择插入的位置');
					return;
				}
			}
			//设置样式表
			var fontName = document.getElementsByName("fontName")[0].value;
			var fontBold = document.getElementsByName("fontBold")[0].checked;
			var fontSize = document.getElementsByName("fontSize")[0].value;
			var fontColor = document.getElementsByName("fontColor")[0].value;
			var style = (fontName=="默认" ? "" : "font-family:" + fontName + ";");
			style += (!fontBold ? "" : "font-weight:bold;");
			style += (fontSize=="默认" ? "" : "font-size:" + fontSize + ";");
			style += (fontColor=="默认" ? "" : "color:" + fontColor + ";");
			
			obj.innerHTML = "<页码>";
			obj.id = "pageAction";
			obj.setAttribute("urn", "页码|" + pageNumber + "|" + style);
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">显示页码数：</td>
			<td><ext:field property="pageNumber"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">当前页码字体：</td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"><tr>
					<td width="100%"><ext:field property="fontName"/></td>
					<td nowrap><ext:field property="fontBold"/></td>
				</tr></table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">字体大小：</td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"><tr>
					<td width="50%"><ext:field property="fontSize"/></td>
					<td nowrap>&nbsp;字体颜色：</td>
					<td width="50%"><ext:field property="fontColor"/></td>
				</tr></table>
			</td>
		</tr>
	</table>
</ext:form>