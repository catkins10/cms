<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<title>执行SQL</title>
	<style>
		body {
			width: 100%;
			height: 100%;
		}
		body, td {
			font-family:宋体;
			font-size:12px;
		}
		.table, .table .tdheader, .table .tdcontent {
			border: #a0a0a0 1px solid;
			border-collapse: collapse;
		}
		.tdheader { /*字段标题*/
			background-color: #f2f2f2;
			padding: 5px 5px 5px 5px;
		}
		.tdcontent { /*内容框*/
			background-color: #ffffff;
			padding: 3px 3px 3px 3px !important;
			word-break: break-all;
			/*word-wrap: break-word;*/
			line-height: 16px;
		}
	</style>
</head>
<body style="border-style: none; margin:0px; overflow: auto;">
	<ext:form action="/executeSql">
		<ext:notEmpty property="executeResult">
			<div style="padding: 5px 5px 5px 5px;">
				<ext:write property="executeResult"/>
			</div>
		</ext:notEmpty>
		<ext:notEmpty property="resultList.fieldNames">
			<table background="0" cellpadding="0" cellspacing="0" style="margin-top:-1px; margin-left:-1px;" class="table">
				<tr>
					<td align="center" class="tdheader" nowrap="true">&nbsp;</td>
					<ext:iterate id="fieldName" property="resultList.fieldNames">
						<td align="center" class="tdheader" nowrap="true"><ext:write name="fieldName"/></td>
					</ext:iterate>
				</tr>
				<ext:iterate id="record" indexId="recordIndex" property="resultList">
					<tr>
						<td class="tdcontent" nowrap="true" align="center">&nbsp;<ext:writeNumber name="recordIndex" plus="1"/>&nbsp;</td>
						<ext:iterate id="fieldName" property="resultList.fieldNames">
							<td class="tdcontent" nowrap="true"><ext:write name="record" nameProperty="fieldName"/></td>
						</ext:iterate>
					</tr>
				</ext:iterate>
			<table>
		</ext:notEmpty>
	</ext:form>
</body>
</html>