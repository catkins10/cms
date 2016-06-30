<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/numeration/js/numeration.js"></script>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">档案类别</td>
		<td class="tdcontent"><ext:field property="archivesType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">档号格式</td>
		<td class="tdcontent">
			<input type="button" class="button" value="插入全宗号" style="width:72px" onclick="insertField('全宗号', false, 'codeConfig')">
			<input type="button" class="button" value="插入保管期限" style="width:90px" onclick="insertField('保管期限', false, 'codeConfig')">
			<input type="button" class="button" value="插入机构或问题" style="width:100px" onclick="insertField('机构或问题', false, 'codeConfig')">
			<input type="button" class="button" value="插入归档年度" style="width:90px" onclick="insertField('归档年度', true, 'codeConfig')">
			<input type="button" class="button" value="插入序号" style="width:72px" onclick="insertField('序号', true, 'codeConfig')">
			<br>
			<ext:field property="codeConfig"/>
		</td>
	</tr>
</table>