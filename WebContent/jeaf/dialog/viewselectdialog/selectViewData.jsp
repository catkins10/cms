<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>视图数据</title>
	<link href="<%=request.getContextPath()%>/jeaf/dialog/css/dialog.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script>
		var trSelected = null;
		function onSelect(src) {
			if(trSelected) {
				setSelected(trSelected, false);
			}
			trSelected = src;
			setSelected(src, true);
		}
		function setSelected(tr, selected) {
			var cells=tr.cells;
			for(var i=(cells ? cells.length-1 : -1); i>=0; i--) {
				cells[i].className = selected ? "viewData selected" : "viewData";
			}
		}
		
	</script>
</head>
<body style="margin:0px; border-style: none; overflow:auto; background: transparent;" onselectstart="return false;">
<html:form action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>">
	<table style="table-layout:fixed" class="view" width="100%" border="0" cellpadding="0" cellspacing="1">
		
		<!-- by zyh -->
		<ext:equal property="multiSelect" value="true">
			<col class="viewColEven">
		</ext:equal>
		<!-- end -->
		<ext:iterate id="column" indexId="columnIndex" property="viewPackage.view.columns">
			<col class="<ext:mod mod="2" value="0" name="columnIndex">viewColEven</ext:mod><ext:mod mod="2" value="1" name="columnIndex">viewColOdd</ext:mod>">
		</ext:iterate>
		<tr align="center" valign="middle">
			<!-- by zyh -->
			<ext:equal property="multiSelect" value="true">
				<td class="viewHeader" width="32px">
				</td>
			</ext:equal>
			<!-- end -->
			<ext:iterate id="column" property="viewPackage.view.columns">
				<td width="<ext:write name="column" property="width"/>" <ext:equal value="0" name="column" property="width">style="display:none"</ext:equal> align="center" class="viewHeader" nowrap id="<ext:write name="column" property="name"/>" <ext:equal value="0" name="column" property="width">style="display:none"</ext:equal>>
					<ext:notEqual value="true" name="column" property="hideTitle"><ext:write name="column" property="title"/></ext:notEqual>
					<font style="font-size:1px">&nbsp;</font>
				</td>
			</ext:iterate>
		</tr>
		<ext:iterate id="record" indexId="recordIndex" property="viewPackage.records">
			<tr id="<ext:write name="record" property="id"/>" style="cursor:pointer" onclick="onSelect(this)" ondblclick="parent.setTimeout('onDblClickData()', 1)">
				<!-- by zyh -->
				<ext:equal property="multiSelect" value="true">
					<td class="viewData"><input type="checkbox" class="checkbox" name="select"  onclick=""></td>
				</ext:equal>
				<!-- end -->
				<ext:iterate id="column" indexId="columnIndex" property="viewPackage.view.columns">
					<td width="<ext:write name="column" property="width"/>" <ext:equal value="0" name="column" property="width">style="display:none"</ext:equal> <ext:equal value="0" name="column" property="width">style="display:none"</ext:equal> align="<ext:write name="column" property="align"/>" class="viewData"><ext:viewField propertyViewPackage="viewPackage" nameColumn="column" nameRecord="record"/></td>
				</ext:iterate>
			</tr>
		</ext:iterate>
	</table>
	<html:hidden property="viewPackage.recordCount"/>
	<html:hidden property="viewPackage.pageCount"/>
	<html:hidden property="viewPackage.curPage"/>
</html:form>
</body>
</html:html>