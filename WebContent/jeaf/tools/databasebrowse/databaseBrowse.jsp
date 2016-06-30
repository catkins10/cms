<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<title>数据库浏览器</title>
	<link href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/jeaf/tree/css/tree.css" rel="stylesheet" type="text/css" />
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
	<style>
		html, body, form {
			width: 100%;
			height: 100%;
			overflow: hidden;
		}
		td {
			font-family:宋体;
			font-size:12px;
		}
	</style>
	<script language="JavaScript">
		var tableName = "";
		function executeSql() {
			var sql = FormUtils.getSelectText("sql");
			if(sql=="") {
				sql = document.getElementsByName("sql")[0].value;
			}
			if(sql=="") {
				return;
			}
			doExecuteSql(sql, document.getElementsByName("limit")[0].value);
		}
		function openTable() {
			if(tableName!="") {
				doExecuteSql("select * from " + tableName, document.getElementsByName("limit")[0].value);
			}
		}
		function doExecuteSql(sql, limit) {
			var form = document.forms['executeSqlForm'];
			form.sql.value = sql;
			form.limit.value = limit;
			form.submit();
		}
	</script>
</head>
<body style="border:0px solid gray; margin: 0px; background-color: #f2f2f2">
<ext:form action="/databaseBrowse">
	<ext:empty property="jdbcUserName">
		<jsp:include page="/jeaf/form/alert.jsp"/>
		<script>DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/tools/databaseBrowseLogin.shtml', 500, 300);</script>
	</ext:empty>
	<ext:notEmpty property="jdbcUserName">
		<div id="divTableTree" style="background-color: #ffffff; border: #a0a0a0 1px solid; position: absolute; padding: 3px; left:3px; top:3px; width:230px; bottom:3px; height: expression(document.documentElement.clientHeight - 15); overflow: auto;"></div>
		<ext:tree property="tableTree" parentElementId="divTableTree"/>
		<script language="JavaScript">
			window.tree.onNodeSeleced = function(nodeId, nodeText, nodeType) { //事件:节点被选中,从tree.js继承
				if(nodeType=="table") {
					tableName = nodeText;
					doExecuteSql("listTableFields " + nodeText);
				}
			};
			window.tree.onDblClickNode = function(nodeId, nodeText, nodeType, leafNode) { //事件:节点被双击,从tree.js继承
				if(nodeType=="table") {
					FormUtils.pasteText("sql", nodeText);
				}
			};
		</script>
		
		<div style="position: absolute; height:20px; padding-top:3px; left:250px; top:3px; right: 3px; width: expression(document.documentElement.clientWidth - 253);">
			<span style="cursor: pointer; display:inline-block" onmousedown="executeSql();">
				<img src="<%=request.getContextPath()%>/jeaf/tools/databasebrowse/icons/execute.gif" align="absmiddle"/>&nbsp;执行SQL(Ctrl+Enter)
			</span>
			&nbsp;&nbsp;
			<span style="cursor: pointer; display:inline-block" onmousedown="openTable();">
				<img src="<%=request.getContextPath()%>/jeaf/tools/databasebrowse/icons/table.gif" align="absmiddle"/>&nbsp;打开表
			</span>
		</div>
	
		<div style="position: absolute; height:160px !important; height:152px; left:250px; top:28px; right: 3px; width: expression(document.documentElement.clientWidth - 253);">
			<ext:field property="sql" styleClass="field" style="height:100%;border: #a0a0a0 1px solid;" onkeyup="if(event.keyCode==13 && event.ctrlKey)executeSql();"/>
		</div>
		
		<div style="position: absolute; height:22px; left:250px; top:192px; right: 3px; width: expression(document.documentElement.clientWidth - 253);">
			<div style="float:left; padding-top: 3px;">执行结果：</div><div style="float:left"><ext:field property="limit" style="width: 50px"/></div><div style="float:left; padding-left: 3px; padding-top: 3px;">行</div>
		</div>
		
		<div style="position: absolute; left:250px; top:220px; right: 3px; bottom: 3px; height: expression(document.documentElement.clientHeight - 226); width: expression(document.documentElement.clientWidth - 255); border: #a0a0a0 1px solid;">
			<iframe name="frameResultSet" frameborder="0" style="width:100%; height: 100%;" scrolling="auto"></iframe>
		</div>
	</ext:notEmpty>
</ext:form>
<form name="executeSqlForm" target="frameResultSet" action="executeSql.shtml" method="post">
	<input type="hidden" name="sql"/>
	<input type="hidden" name="limit"/>
</form>
</body>
</html>