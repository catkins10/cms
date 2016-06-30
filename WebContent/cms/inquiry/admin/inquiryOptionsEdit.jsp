<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newInquiryOption() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/inquiry/admin/inquiryOption.shtml?id=<ext:write property="id"/>&isQuestionnaire=<ext:write property="isQuestionnaire"/>&option.inquiryId=<ext:write property="id"/>&siteId=<ext:field property="siteId"/>', 640, 400);
}
function openOption(optionId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/inquiry/admin/inquiryOption.shtml?id=<ext:write property="id"/>&siteId=<ext:field property="siteId"/>&option.id=' + optionId, 640, 400);
}
function adjustOptionPriority() {
	DialogUtils.adjustPriority('cms/inquiry', 'admin/adjustOptionPriority', '选项优先级调整', 640, 400, 'inquiryId=<ext:write property="id"/>');
}
</script>
<div style="padding-bottom:5px">
	<input type="button" class="button" value="添加投票选项" onclick="newInquiryOption()">
	<input type="button" class="button" value="调整优先级" onclick="adjustOptionPriority()">
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom">
		<td align="center" class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="100%">选项主题</td>
	</tr>
	<ext:iterate id="option" indexId="optionIndex" property="options">
		<tr style="cursor:pointer" align="center" valign="top" onclick="openOption('<ext:write name="option" property="id"/>')">
			<td class="tdcontent" align="center"><ext:writeNumber name="optionIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="option" property="inquiryOption"/></td>
		</tr>
	</ext:iterate>
</table>