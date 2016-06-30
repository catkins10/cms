<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveInquiry">
   	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col valign="middle" align="right">
		<col valign="middle" width="100%">
		<tr>
			<td nowrap="nowrap" valign="top">调查说明：</td>
			<td><ext:field property="inquiry.description"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">投票方式：</td>
			<td>
				<div style="float:left">
					<ext:field property="inquiry.isMultiSelect" onclick="document.getElementById('divVote').style.display=(document.getElementsByName('inquiry.isMultiSelect')[1].checked ? '' : 'none');"/>
				</div>
				<div id="divVote" style="float:left;<ext:notEqual property="inquiry.isMultiSelect" value="1">display: none;</ext:notEqual>">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td nowrap="nowrap">&nbsp;最低投票数：</td>
							<td width="60px"><ext:field property="inquiry.minVote"/></td>
							<td nowrap="nowrap">&nbsp;最高投票数：</td>
							<td width="60px"><ext:field property="inquiry.maxVote"/></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">调查选项：</td>
			<td>
				<ext:iterate id="option" indexId="optionIndex" property="inquiry.options">
					<ext:writeNumber name="optionIndex" plus="1"/>、<a href="javascript:DialogUtils.openDialog('<%=request.getContextPath()%>/cms/inquiry/admin/inquiryOption.shtml?id=<ext:write property="id"/>&option.id=<ext:write name="option" property="id"/>&siteId=<ext:write property="siteId"/>', 640, 400);"><ext:write name="option" property="inquiryOption"/></a>&nbsp;
				</ext:iterate>
			</td>
		</tr>
	</table>
</ext:form>