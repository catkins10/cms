<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertIssueTotals">
	<style>
		.pageTd {
			border-bottom: #f0f0f0 1 solid;
		}
	</style>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script type="text/javascript">
	//显示扩展属性,由继承者实现
	function showExtendProperties(recordListProperties, recordListTextContent) {
		var totalCount = StringUtils.getPropertyValue(recordListProperties, "totalCount");
		if(!totalCount) {
			return;
		}
		document.getElementsByName("sort")[0].checked = ("true"==StringUtils.getPropertyValue(recordListProperties, "totalSort"));
		//设置输出方式
		DropdownField.setValue("writeMode", StringUtils.getPropertyValue(recordListProperties, "writeMode"));
		//设置图形尺寸
		document.getElementsByName("chartWidth")[0].value = StringUtils.getPropertyValue(recordListProperties, "chartWidth");
		document.getElementsByName("chartHeight")[0].value = StringUtils.getPropertyValue(recordListProperties, "chartHeight");
		onWriteModeChanged();
		//解析页面
		for(var i=0; i<totalCount; i++) {
			var tr = document.getElementById("totalTable").insertRow(-1);
			var td = tr.insertCell(-1);
			td.className = "pageTd";
			var totalColumnProperties = StringUtils.getPropertyValue(recordListProperties, "total" + i);
			td.innerHTML = '<input type="checkbox" class="checkbox" name="selectTotalPage">&nbsp;<a>' + StringUtils.getPropertyValue(totalColumnProperties, "totalTitle") + '</a>';
			var a = td.getElementsByTagName("a")[0];
			a.setAttribute("urn", totalColumnProperties);
		}
	}
	
	//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
	function getExtendPropertiesAsText() {
		var table = document.getElementById("totalTable");
		if(table.rows.length==0) {
			alert("页面未配置");
			return "ERROR";
		}
		var ret = "totalCount=" + table.rows.length +
				  "&totalSort=" + document.getElementsByName("sort")[0].checked +
				  "&writeMode=" + document.getElementsByName("writeMode")[0].value +
				  "&chartWidth=" + document.getElementsByName("chartWidth")[0].value +
				  "&chartHeight=" + document.getElementsByName("chartHeight")[0].value;
		for(var i=0; i< table.rows.length; i++) {
			var a = table.rows[i].cells[0].getElementsByTagName("a")[0];
			ret += "&total" + i + "=" + StringUtils.encodePropertyValue(a.getAttribute("urn"));
		}
		return ret;
	}
	//获取默认的左侧记录格式
	function getDeafultRecordLeftFormat() {
		return '<a id="field" urn="name=name&link=true">&lt;名称&gt;</a>';
	}
	//获取默认的右侧记录格式
	function getDeafultRecordRightFormat() {
		return '<a id="field" urn="name=total">&lt;统计&gt;</a>';
	}
	function addTotalPage() {
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/total/insertAccessTotalPage.shtml", 500, 180);
	}
	function doAddTotalPage(pageTitle, pageName, totalTitle, totalLink, recordId) {
		var tr = document.getElementById("totalTable").insertRow(-1);
		var td = tr.insertCell(-1);
		td.className = "pageTd";
		td.innerHTML = '<input type="checkbox" class="checkbox" name="selectTotalPage">&nbsp;<a>' + totalTitle + '</a>'
		var a = td.getElementsByTagName("a")[0];
		a.setAttribute("urn", "totalTitle=" + StringUtils.encodePropertyValue(totalTitle) +
				"&pageTitle=" + pageTitle +
				"&pageName=" + pageName +
				"&totalLink=" + StringUtils.encodePropertyValue(totalLink) +
				"&recordId=" + recordId);
	}
	function deleteTotalPage() {
		var table = document.getElementById("totalTable");
		var selects = document.getElementsByName("selectTotalPage");
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
		var selects = document.getElementsByName("selectTotalPage");
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
		var selects = document.getElementsByName("selectTotalPage");
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
	function onWriteModeChanged() {
		document.getElementById("spanChartSize").style.visibility = document.getElementsByName("writeMode")[0].value.indexOf('Chart')!=-1 ? "visible" : "hidden";
	}
	</script>
	<table border="0" width="100%" cellspacing="5px" cellpadding="0px" style="<%=("true".equals(request.getParameter("searchResults")) ? "display:none" : "")%>">
		<col align="right">
		<col>
		<tr>
			<td nowrap="nowrap">页面选择：</td>
			<td nowrap="nowrap">
				<input type="button" class="Button" value="添加页面" style="width:60px" onclick="addTotalPage()">
				<input type="button" class="Button" value="删除页面" style="width:60px" onclick="deleteTotalPage()">
				<input type="button" class="Button" value="上移" style="width:40px" onclick="moveUp()">
				<input type="button" class="Button" value="下移" style="width:40px" onclick="moveDown()">
				&nbsp;<ext:field property="sort"/>
			</td>
			<td nowrap="nowrap">&nbsp;输出方式：</td>
			<td width="80px" nowrap="nowrap"><ext:field property="writeMode" onchange="onWriteModeChanged()"/></td>
			<td nowrap="nowrap" width="100%" id="spanChartSize" style="visibility:hidden">
				<table border="0" cellspacing="0" cellpadding="0px" nowrap="nowrap">
					<tr>
						<td nowrap="nowrap">&nbsp;宽度：</td>
						<td width="36px"><ext:field property="chartWidth"/></td>
						<td nowrap="nowrap">&nbsp;高度：</td>
						<td width="36px"><ext:field property="chartHeight"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td></td>
			<td colspan="5">
				<div style="width:100%; height: 80px; border: #e0e0e0 1px solid; background-color:white; overflow:auto">
					<table id="totalTable" border="0" width="100%" cellspacing="0" cellpadding="2px"></table>
				</div>
			</td>
		</tr>
	</table>
</ext:form>