<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">站点/栏目：</td>
		<td><ext:field property="putPage.siteNames"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">子站/子栏目：</td>
		<td><ext:field property="putPage.containChildSite"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">页面：</td>
		<td><ext:field property="putPage.pageTitles"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">客户端类型：</td>
		<td><ext:field property="putPage.clientTypeArray"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">投放方式：</td>
		<td><ext:field property="putPage.mode"/></td>
	</tr>
	<tr id="trX" style="display:none">
		<td nowrap="nowrap">水平边距：</td>
		<td><ext:field property="putPage.x"/></td>
	</tr>
	<tr id="trY" style="display:none">
		<td nowrap="nowrap">垂直边距：</td>
		<td><ext:field property="putPage.y"/></td>
	</tr>
	<tr id="trLoadMode" style="display:none">
		<td nowrap="nowrap">加载方式：</td>
		<td><ext:field property="putPage.loadMode"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">速度(像数/毫秒)：</td>
		<td><ext:field property="putPage.speed"/></td>
	</tr>
	<tr id="trHideMode" style="display:none">
		<td nowrap="nowrap">关闭方式：</td>
		<td><ext:field property="putPage.hideMode"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">显示时长(秒)：</td>
		<td><ext:field property="putPage.displaySeconds"/></td>
	</tr>
</table>

<script>
	function onModeChanged() {
		var mode = document.getElementsByName("putPage.mode")[0].value;
		//飘动|fly\0窗口左上角|windowLeftTop\0窗口右上角|windowRightTop\0窗口右下角|windowRightBottom\0窗口左下角|windowLeftBottom
		//页面左上角|pageLeftTop\0页面右上角|pageRightTop\0页面右下角|pageRightBottom\0页面左下角|pageLeftBottom
		//页面顶部(居中)|pageTop\0页面底部(居中)|pageBottom\0窗口中部|windowCenter
		//窗口上方(左右对称)|windowTopBalance\0窗口下方(左右对称)|windowBottomBalance\0页面上方(左右对称)|pageTopBalance\0页面下方(左右对称)|pageBottomBalance\0绝对位置|static
		document.getElementById("trLoadMode").style.display = mode!="" && ",fly,pageTop,".indexOf("," + mode + ",")==-1 ? "" : "none";
		document.getElementById("trHideMode").style.display = mode!="" && ",fly,".indexOf("," + mode + ",")==-1 ? "" : "none";
		document.getElementById("trX").style.display = mode!="" && ",pageTop,pageBottom,windowCenter,".indexOf("," + mode + ",")==-1 ? "" : "none";
		document.getElementById("trY").style.display = mode!="" && ",pageBottom,windowCenter,".indexOf("," + mode + ",")==-1 ? "" : "none";
		DialogUtils.adjustDialogSize();
	}
	onModeChanged();
</script>