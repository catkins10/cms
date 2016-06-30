<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function filing() { //归档
	if(document.getElementsByName("filingConfig.toArchives")[0].value=='1') {
		if(document.getElementsByName("filingOption.fondsName")[0].value=="") {
			alert("全宗名称不允许为空");
			return;
		}
		if(document.getElementsByName("filingOption.secureLevel")[0].value=="") {
			alert("文件密级不允许为空");
			return;
		}
		if(document.getElementsByName("filingOption.rotentionPeriod")[0].value=="") {
			alert("保管期限不允许为空");
			return;
		}
		if(document.getElementsByName("filingOption.docCategory")[0].value=="") {
			alert("公文种类不允许为空");
			return;
		}
		if(document.getElementsByName("filingOption.unit")[0].value=="") {
			alert("机构或问题不允许为空");
			return;
		}
		if(document.getElementsByName("filingOption.responsibilityPerson")[0].value=="") {
			alert("责任者不允许为空");
			return;
		}
	}
	var year = document.getElementsByName("filingOption.filingYear")[0].value;
	if(year=="" || Number(year)==0) {
		alert("归档年度设置不正确");
		return;
	}
	FormUtils.doAction('filing');
}
</script>
<table border="0" cellspacing="0" cellpadding="3" width="500px">
	<col align="right">
	<col width="100%">
	<ext:equal value="1" property="filingConfig.toArchives">
		<tr>
			<td nowrap="nowrap">全宗名称：</td>
			<td width="100%"><ext:field property="filingOption.fondsName"/></td>
		</tr>
		<tr title="参考密级：<ext:write property="secureLevel"/>">
			<td nowrap="nowrap">文件密级：</td>
			<td><ext:field property="filingOption.secureLevel"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">保管期限：</td>
			<td><ext:field property="filingOption.rotentionPeriod"/></td>
		</tr>
		<tr title="文件题名：<ext:write property="subject"/>">
			<td nowrap="nowrap">公文种类：</td>
			<td><ext:field property="filingOption.docCategory"/></td>
		</tr>
		<tr title="起草部门：<ext:write property="draftDepartment"/>">
			<td nowrap="nowrap">机构或问题：</td>
			<td><ext:field property="filingOption.unit"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">责&nbsp;任&nbsp;者：</td>
			<td><ext:field property="filingOption.responsibilityPerson"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td  nowrap="nowrap">归档年度：</td>
		<td width="100%"><ext:field property="filingOption.filingYear"/></td>
	</tr>
</table>