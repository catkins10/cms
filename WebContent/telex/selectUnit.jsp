<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>单位选择</title>
</head>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/dialog/js/selectDialog.js"></script>

<script>
var personIds = new Array(), personNames = new Array();
function initData() {
	var openerDocument = DialogUtils.getDialogOpener().document;
	var unitIds = "", unitNames = "";
	for(var j=0; j<dialogParam.length; j++) {
		if(dialogParam[j].field=="id") {
			unitIds = openerDocument.getElementsByName(dialogParam[j].name)[0].value;
		}
		if(dialogParam[j].field=="name") {
			unitNames = openerDocument.getElementsByName(dialogParam[j].name)[0].value;
		}
	}
	if(unitIds=="") {
		return;
	}
	var separator = document.getElementsByName("separator")[0].value;
	if(separator=="") {
	 	separator = ",";
	}
	unitIds = unitIds.split(separator);
	unitNames = unitNames.split(separator);
	for(var i=0; i<unitIds.length; i++) {
		if(document.getElementById(unitIds[i])) {
			document.getElementById(unitIds[i]).checked = true;
		}
		else { //不是单位
			personIds[personIds.length] = unitIds[i];
			personNames[personNames.length] = unitNames[i];
		}
	}
}
function doOk() {
	var openerDocument = DialogUtils.getDialogOpener().document;
	var units = document.getElementsByName('unit');
	var unitIds = "";
	var unitNames = "";
	var separator = document.getElementsByName("separator")[0].value;
	if(separator=="") {
	 	separator = ",";
	}
	for(var i=0; i<personIds.length; i++) {
		unitIds += (unitIds=="" ? "" : separator) + personIds[i];
		unitNames += (unitNames=="" ? "" : separator) + personNames[i];
	}
	for(var i=0; i<units.length; i++) {
		if(units[i].checked) {
			unitIds += (unitIds=="" ? "" : separator) + units[i].id;
			unitNames += (unitNames=="" ? "" : separator) + units[i].value;
		}
	}
	for(var j=0; j<dialogParam.length; j++) {
		if(dialogParam[j].field=="id") {
			openerDocument.getElementsByName(dialogParam[j].name)[0].value = unitIds;
		}
		else if(dialogParam[j].field=="name") {
			openerDocument.getElementsByName(dialogParam[j].name)[0].value = unitNames;
		}
		else {
			continue;
		}
		try {
			openerDocument.getElementsByName(dialogParam[j].name)[0].onchange();
		}
		catch(e) {
		
		}
	}
	if(document.getElementsByName("script")[0].value!="") {
		DialogUtils.getDialogOpener().setTimeout(document.getElementsByName("script")[0].value, 300);
	}
	DialogUtils.closeDialog();
}
function expand(src) {
	var img = src.getElementsByTagName("img")[0];
	var unitRow = src.parentElement.nextSibling;
	if(img.src.indexOf("collapse")!=-1) {
		img.src = img.src.replace("collapse", "expand");
		unitRow.style.display = "none";
	}
	else {
		img.src = img.src.replace("expand", "collapse");
		unitRow.style.display = "";
	}
}
function search() {
	var key = document.getElementsByName("key")[0].value;
	if(key=="") {
		return;
	}
	document.getElementById("frameQuery").src = "unitSearch.shtml?key=" + StringUtils.utf8Encode(key);
}
function appendFoundUnit(unitId, unitName, unitIndex) {
	//只显示"搜索结果"分类,隐藏其他分类
	if(unitIndex==0) {
		var rows = document.getElementById("unitTable").rows;
		rows[rows.length-2].style.display = "";
		for(var i=0; i<rows.length; i+=2) {
			var img = rows[i].cells[0].getElementsByTagName("img")[0];
			var index = img.src.indexOf("collapse");
			if((i<rows.length-2 && index!=-1) || (i==rows.length-2 && index==-1)) {
				expand(rows[i].cells[0]);
			}
		}
		document.getElementById("searchResult").innerHTML = "";
	}
	//插入单位
	var div = document.createElement("span");
	div.style.float = "left";
	div.style.width = "200px";
	var html = '<pre style="margin:0px">';
	html += '<input type="checkbox" class="checkbox" onclick="clickSearchResult(this)" ' + (document.getElementById(unitId).checked ? "checked" : "") + ' id="search_' + unitId + '" value="' + unitId + '">';
	html += '<label for="search_' + unitId + '">' + unitName + '</label></pre>';
	div.innerHTML = html;
	document.getElementById("searchResult").appendChild(div);
}
function clickSearchResult(src) {
	document.getElementById(src.value).checked = src.checked;
}
function noUnitsFound() {
	var rows = document.getElementById("unitTable").rows;
	rows[rows.length-2].style.display = "";
	for(var i=0; i<rows.length; i+=2) {
		var img = rows[i].cells[0].getElementsByTagName("img")[0];
		var index = img.src.indexOf("collapse");
		if((i<rows.length-2 && index!=-1) || (i==rows.length-2 && index==-1)) {
			expand(rows[i].cells[0]);
		}
	}
	document.getElementById("searchResult").innerHTML = "&nbsp;没有匹配的单位。";
}
function selectChildOrg(src) {
	var counties = "长乐,福清,连江,罗源,闽侯,闽清,平潭,永泰";
	var sections = "鼓楼,台江,仓山,晋安,马尾"
	var selectAllCounties = (src.value=="十三个县（市）区" || src.value=="八县（市）" || src.value=="八县（市） + 马尾");
	var selectAllSections = (src.value=="十三个县（市）区" || src.value=="五区");
	var units = src.parentElement.parentElement.parentElement.getElementsByTagName("input");
	for(var i=0; i<units.length; i++) {
		if(units[i].name!="unit") {
			continue;
		}
		if(selectAllCounties) {
			if(counties.indexOf(units[i].value)!=-1) {
				units[i].checked = src.checked;
			}
		}
		if(selectAllSections) {
			if(sections.indexOf(units[i].value)!=-1) {
				units[i].checked = src.checked;
			}
		}
		if(units[i].value=="马尾" && src.value=="八县（市） + 马尾") {
			units[i].checked = src.checked;
		}
	}
}
</script>
<body class="dialogBody" topmargin="5px" bottommargin="12" style="border:0;">
<html:form action="/displaySelectUnit">
	<table border="0" height="100%" width="100%" cellspacing="0" cellpadding="0px" style="table-layout:fixed">
		<tr height="100%" valign="top"><td>
			<div style="width:100%; height:100%; overflow:auto; padding:5px"><table id="unitTable" cellpadding="3px" cellspacing="0" border="0" style="border-collapse:collapse" width="100%">
				<ext:iterate id="telegramUnit" property="telegramUnits">
					<tr>
						<td align="left" style="padding-left:0px; cursor:pointer" nowrap="true" onclick="expand(this)">
							<img src="images/expand.gif" border="0" align="absmiddle" style="cursor:pointer">&nbsp;<ext:write name="telegramUnit" property="category"/>
						</td>
					</tr>
					<tr style="display:none">
						<td align="left" style="padding-left:0px">
							<ext:iterate id="unit" name="telegramUnit" property="units">
								<div style="float:left; width:200px"><pre style="margin:0px"><input name="unit" type="checkbox" class="checkbox" value="<ext:write name="unit" property="directoryName"/>" id="<ext:write name="unit" property="id"/>"><label for="<ext:write name="unit" property="id"/>"><ext:write name="unit" property="directoryName"/></label></pre></div>
							</ext:iterate>
							<ext:equal value="下级部门" name="telegramUnit" property="category">
								<div style="float:left; width:200px"><pre style="margin:0px"><input onclick="selectChildOrg(this)" type="checkbox" id="childOrg1" class="checkbox" value="十三个县（市）区"><label for="childOrg1">十三个县（市）区</label></pre></div>
								<div style="float:left; width:200px"><pre style="margin:0px"><input onclick="selectChildOrg(this)" type="checkbox" id="childOrg2" class="checkbox" value="八县（市）"><label for="childOrg2">八县（市）</label></pre></div>
								<div style="float:left; width:200px"><pre style="margin:0px"><input onclick="selectChildOrg(this)" type="checkbox" id="childOrg3" class="checkbox" value="八县（市） + 马尾"><label for="childOrg3">八县（市） + 马尾</label></pre></div>
								<div style="float:left; width:200px"><pre style="margin:0px"><input onclick="selectChildOrg(this)" type="checkbox" id="childOrg4" class="checkbox" value="五区"><label for="childOrg4">五区</label></pre></div>
							</ext:equal>
						</td>
					</tr>
	         	</ext:iterate>
	         	<tr style="display:none" id="searchResultLabel">
					<td align="left" style="padding-left:0px; cursor:pointer" nowrap="true" onclick="expand(this)">
						<img src="images/expand.gif" border="0" align="absmiddle" style="cursor:pointer">&nbsp;搜索结果
					</td>
				</tr>
				<tr style="display:none">
					<td align="left" style="padding-left:0px" id="searchResult"></td>
				</tr>
	         </table></div>
	    </td></tr>
	    <tr><td class="dialogButtonBar" id="dialogButtonBar">
	    	<div style="float: left">
	    		&nbsp;单位搜索：<input type="text" name="key" class="field" style="width:160px" onkeypress="if(event.keyCode==13){search();return false;}return true;">
	    		<input type="button" class="button" onclick="search()" value="&nbsp;搜索&nbsp;">
	    		<iframe style="display:none" id="frameQuery"></iframe>
	    	</div>
	    	<div style="float: right">
		   		<input type="button" class="button" onclick="doOk()" value="&nbsp;确定&nbsp;">&nbsp;
				<input type="button" class="button" onclick="DialogUtils.closeDialog()" value="&nbsp;取消&nbsp;">
			</div>
		</td></tr>
    </table>
	<html:hidden property="param"/>
	<html:hidden property="script"/>
	<html:hidden property="title"/>
	<html:hidden property="separator"/>
	<input type="hidden" id="contextPath" value="<%=request.getContextPath()%>">
</html:form>
</body>
</html:html>