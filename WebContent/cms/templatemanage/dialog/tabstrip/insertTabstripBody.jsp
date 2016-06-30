<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertTabstripBody">
	<script>
	var dialogArguments = DialogUtils.getDialogArguments();
	window.onload = function() {
		var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
		if(obj && obj.id=="tabstripBody") {
			var urn = obj.getAttribute("urn");
			document.getElementsByName("tabstripName")[0].value = StringUtils.getPropertyValue(urn, "name");
			document.getElementsByName("width")[0].value = StringUtils.getPropertyValue(urn, "width");
			document.getElementsByName("height")[0].value = StringUtils.getPropertyValue(urn, "height");
			//切换方式
			var switchMode = StringUtils.getPropertyValue(urn, "switchMode");
			document.getElementsByName("switchMode")[1].checked =  (switchMode=="click");
			//定时切换
			document.getElementsByName("timeSwitch")[0].checked = StringUtils.getPropertyValue(urn, "timeSwitch")=="true";
			document.getElementsByName("timeInterval")[0].value = StringUtils.getPropertyValue(urn, "timeInterval");
			//单击打开更多
			document.getElementsByName("clickOpenMore")[0].checked =  StringUtils.getPropertyValue(urn, "clickOpenMore")=="true";
			switchModeChanged();
		}
	}
	function doOk() {
		var tabstripName = document.getElementsByName("tabstripName")[0].value;
		if(tabstripName=='') {
			alert("选项卡名称不能为空");
			return;
		}
		var timeSwitch = document.getElementsByName("timeSwitch")[0].checked;
		var timeInterval = Number(document.getElementsByName("timeInterval")[0].value);
		if(isNaN(timeInterval) || timeInterval==0) {
			if(timeSwitch) {
				alert("切换时间设置不正确");
				return false;
			}
			timeInterval = 5;
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
		obj.innerHTML = "&lt;TAB标签页:" + tabstripName + "&gt;";
		obj.id = "tabstripBody";
		var urn = "name=" + tabstripName +
				  "&width=" + document.getElementsByName("width")[0].value +
				  "&height=" + document.getElementsByName("height")[0].value +
				  "&switchMode=" + (document.getElementsByName("switchMode")[0].checked ? "mouseOver" : "click") +
				  "&timeSwitch=" + timeSwitch +
				  "&timeInterval=" + timeInterval +
				  "&clickOpenMore=" + document.getElementsByName("clickOpenMore")[0].checked;
		obj.setAttribute("urn", urn);
		DialogUtils.closeDialog();
	}
	function switchModeChanged() {
		var switchMode = document.getElementsByName("switchMode");
		document.getElementById("trClickOpenMore").style.display = (switchMode[1].checked ? "none" : "");
		DialogUtils.adjustDialogSize();
	}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">选项卡名称：</td>
			<td colspan="3" width="100%"><ext:field property="tabstripName"/></td>
		</tr>
		<tr valign="middle">
			<td nowrap="nowrap" align="right">标签页宽度：</td>
			<td width="50%"><ext:field property="width"/></td>
			<td nowrap="nowrap" align="right">高度：</td>
			<td width="50%"><ext:field property="height"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">定时切换：</td>
			<td width="50%"><ext:field property="timeSwitch"/></td>
			<td nowrap="nowrap" align="right">时间间隔(秒)：</td>
			<td width="50%"><ext:field property="timeInterval"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">切换方式：</td>
			<td colspan="3"><ext:field onclick="switchModeChanged()" property="switchMode"/></td>
		</tr>
		<tr id="trClickOpenMore" height="30px">
			<td nowrap="nowrap" align="right">单击选项时：</td>
			<td colspan="3"><ext:field property="clickOpenMore"/></td>
		</tr>
	</table>
</ext:form>