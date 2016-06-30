<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom:5px">
	<input type="button" class="button" value="添加栏目" onclick="DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/captureColumn.shtml?id=-1', 400, 200)">
</div>
<table id="columnConfigures" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="36px">序号</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="70%">栏目名称</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="30%">关键字过滤</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="60px">直接发布</td>
	</tr>
</table>
<script>
	var extendedParameters = document.getElementsByName("extendedParameters")[0].value;
	for(var i=0; i<100; i++) {
		var key = StringUtils.getPropertyValue(extendedParameters, "key" + (i==0 ? "" : "_" + i));
		var columnIds = StringUtils.getPropertyValue(extendedParameters, "columnIds" + (i==0 ? "" : "_" + i));
		var columnNames = StringUtils.getPropertyValue(extendedParameters, "columnNames" + (i==0 ? "" : "_" + i));
		var issue = StringUtils.getPropertyValue(extendedParameters, "issue" + (i==0 ? "" : "_" + i));
		if(columnIds=="") {
			break;
		}
		addColumn(-1, key, columnIds, columnNames, issue);
	}
	function addColumn(index, key, columnIds, columnNames, issue) {
		var tr;
		if(index!=-1) {
			tr = document.getElementById("columnConfigures").rows[index];
		}
		else  {
			tr = document.getElementById("columnConfigures").insertRow(-1);
			tr.style.cursor = "pointer";
			tr.onclick = function() {
				var url = '<%=request.getContextPath()%>/cms/siteresource/admin/captureColumn.shtml' +
						  '?id=' + tr.rowIndex +
						  '&key=' + StringUtils.utf8Encode(tr.cells[2].innerHTML) +
						  '&columnIds=' + tr.id +
						  '&columnNames=' + StringUtils.utf8Encode(tr.cells[1].innerHTML) +
						  '&issue=' + (tr.cells[3].innerHTML=='√' ? '1' : '0');
				DialogUtils.openDialog(url, 400, 200);
			};
			for(var i=0; i<4; i++) {
				var td = tr.insertCell(-1);
				td.className = "tdcontent";
				td.align = i==1 || i==2 ? "left" : "center";
			}
		}
		tr.id = columnIds;
		tr.cells[0].innerHTML = tr.rowIndex; //序号
		tr.cells[1].innerHTML = columnNames; //栏目名称
		tr.cells[2].innerHTML = key; //关键字
		tr.cells[3].innerHTML = (issue=="true" ? "√" : ""); //是否直接发布
	}
	function updateExtendedParameters() {
		var extendedParameters = "";
		var rows = document.getElementById("columnConfigures").rows;
		for(var i=1; i<rows.length; i++) {
			extendedParameters = (extendedParameters=="" ? "" : extendedParameters + "&") +
								 "key" + (i==1 ? "" : "_" + (i-1)) + "=" + StringUtils.encodePropertyValue(rows[i].cells[2].innerHTML) +
								 "&columnIds" + (i==1 ? "" : "_" + (i-1)) + "=" + rows[i].id +
								 "&columnNames" + (i==1 ? "" : "_" + (i-1)) + "=" + StringUtils.encodePropertyValue(rows[i].cells[1].innerHTML) +
								 "&issue" + (i==1 ? "" : "_" + (i-1)) + "=" + (rows[i].cells[3].innerHTML=="√" ? "true" : "false");
		}
		document.getElementsByName("extendedParameters")[0].value = extendedParameters;
	}
	window.addCaptureColumn = function(index, key, columnIds, columnNames, issue) {
		addColumn(index, key, columnIds, columnNames, "" + issue);
		updateExtendedParameters();
	}
	window.removeCaptureColumn = function(index) {
		document.getElementById("columnConfigures").deleteRow(index);
		updateExtendedParameters();
	}
</script>