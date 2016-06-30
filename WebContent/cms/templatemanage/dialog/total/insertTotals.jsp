<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertTotals">
	<style>
		.columnTd {
			border-bottom: #f0f0f0 1 solid;
		}
	</style>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
	<script type="text/javascript">
	//显示扩展属性,由继承者实现
	function showExtendProperties(recordListProperties, recordListTextContent) {
		var totalCount = StringUtils.getPropertyValue(recordListProperties, "totalCount");
		if(!totalCount) {
			return;
		}
		document.getElementsByName("sort")[0].checked = ("true"==StringUtils.getPropertyValue(recordListProperties, "totalSort"));
		//解析统计栏目
		for(var i=0; i<totalCount; i++) {
			var tr = document.getElementById("totalTable").insertRow(-1);
			var td = tr.insertCell(-1);
			td.className = "columnTd";
			var totalColumnProperties = StringUtils.getPropertyValue(recordListProperties, "total" + i);
			td.innerHTML = '<input type="checkbox" class="checkbox" name="selectTotalColumn">&nbsp;<a>' + StringUtils.getPropertyValue(totalColumnProperties, "totalTitle") + '</a>';
			var a = td.getElementsByTagName("a")[0];
			a.setAttribute("urn", totalColumnProperties);
		}
	}
	
	//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
	function getExtendPropertiesAsText() {
		var table = document.getElementById("totalTable");
		if(table.rows.length==0) {
			alert("栏目未配置");
			return "ERROR";
		}
		var ret = "totalCount=" + table.rows.length +
				  "&totalSort=" + document.getElementsByName("sort")[0].checked;
		for(var i=0; i< table.rows.length; i++) {
			var a = table.rows[i].cells[0].getElementsByTagName("a")[0];
			ret += "&total" + i + "=" + StringUtils.encodePropertyValue(a.getAttribute("urn"));
		}
		return ret;
	}
	
	//获取默认的左侧记录格式
	function getDeafultRecordLeftFormat() {
		return '<a id="field" urn="name=name&link=true">&lt;栏目名称&gt;</a>';
	}
	//获取默认的右侧记录格式
	function getDeafultRecordRightFormat() {
		return '<a id="field" urn="name=total">&lt;记录数&gt;</a>';
	}
	function selectTotal() {
		selectView(640, 400, "addTotalColumn('{id}'.split('__')[0] ,'{id}'.split('__')[1], '{name}', '{templateExtendURL}', '{link}')", editor.document.getElementsByName("applicationName")[0].value, '', false, true);
	}
	function addTotalColumn(applicationName, name, title, templateExtendURL, link) {
		window.totalTitle = title;
		window.totalApplication = applicationName;
		window.totalName = name;
		window.totalLink = link;
		templateExtendURL += (templateExtendURL.indexOf('?')==-1 ? "?" : "&") + "recordListName=" + name + "&applicationName=" + applicationName + "&displayMode=dialog";
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/total/insertTotalColumn.shtml" + (templateExtendURL ? "?templateExtendURL=" + StringUtils.utf8Encode(templateExtendURL) : ""), 680, 180, "", {editor:editor});
	}
	function doAddTotalColumn(totalTitle, totalApplication, totalName, totalLink, totalRecordListPropertiess) {
		var tr = document.getElementById("totalTable").insertRow(-1);
		var td = tr.insertCell(-1);
		td.className = "columnTd";
		td.innerHTML = '<input type="checkbox" class="checkbox" name="selectTotalColumn">&nbsp;<a>' + totalTitle + '</a>'
		var a = td.getElementsByTagName("a")[0];
		a.setAttribute("urn", "totalTitle=" + StringUtils.encodePropertyValue(totalTitle) +
				"&totalApplication=" + totalApplication +
				"&totalName=" + totalName +
				"&totalLink=" + StringUtils.encodePropertyValue(totalLink) +
				"&totalRecordListProperties=" + StringUtils.encodePropertyValue(totalRecordListPropertiess));
	}
	function deleteTotalColumn() {
		var table = document.getElementById("totalTable");
		var selects = document.getElementsByName("selectTotalColumn");
		if(selects.length==0) {
			return;
		}
		for(var i=selects.length-1; i>=0; i--) {
			if(selects[i].checked) {
				table.deleteRow(i);
			}
		}
	}
	function moveUp() {
		var table = document.getElementById("totalTable");
		var selects = document.getElementsByName("selectTotalColumn");
		if(selects.length==0) {
			return;
		}
		for(var i=0; i<selects.length; i++) {
			if(selects[i].checked) {
				if(i==0) {
					return;
				}
				DomUtils.moveTableRow(table, i, i-1);
				selects[i-1].checked = true;
			}
		}
	}
	function moveDown() {
		var table = document.getElementById("totalTable");
		var selects = document.getElementsByName("selectTotalColumn");
		if(selects.length==0) {
			return;
		}
		for(var i=selects.length-1; i>=0; i--) {
			if(selects[i].checked) {
				flag = false;
				if(i==selects.length-1) {
					return;
				}
				DomUtils.moveTableRow(table, i, i+1);
				selects[i+1].checked = true;
			}
		}
	}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px" style="<%=("true".equals(request.getParameter("searchResults")) ? "display:none" : "")%>">
		<col align="right">
		<col>
		<tr>
			<td nowrap="nowrap">栏目选择：</td>
			<td width="100%">
				<input type="button" class="Button" value="添加栏目" style="width:60px" onclick="selectTotal()">
				<input type="button" class="Button" value="删除栏目" style="width:60px" onclick="deleteTotalColumn()">
				<input type="button" class="Button" value="上移" style="width:40px" onclick="moveUp()">
				<input type="button" class="Button" value="下移" style="width:40px" onclick="moveDown()">
				<ext:field property="sort"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<div style="width:100%; height: 80px; border: #e0e0e0 1px solid; background-color:white; overflow:auto">
					<table id="totalTable" border="0" width="100%" cellspacing="0" cellpadding="2px">
					</table>
				</div>
			</td>
		</tr>
	</table>
</ext:form>