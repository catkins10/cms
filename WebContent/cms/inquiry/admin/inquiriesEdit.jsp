<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newInquiry() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/inquiry/admin/inquiry.shtml?id=<ext:write property="id"/>&isQuestionnaire=1&siteId=<ext:field property="siteId"/>', 800, 400);
}
function openInquiry(inquiryId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/inquiry/admin/inquiry.shtml?id=<ext:write property="id"/>&siteId=<ext:field property="siteId"/>&inquiry.id=' + inquiryId, 800, 400);
}
function adjustInquiryPriority() {
	DialogUtils.adjustPriority('cms/inquiry', 'admin/adjustInquiryPriority', '调查优先级调整', 640, 400, 'subjectId=<ext:write property="id"/>');
}
</script>
<div style="padding-bottom:5px">
	<input type="button" class="button" value="添加调查" onclick="newInquiry()">
	<input type="button" class="button" value="调整优先级" onclick="adjustInquiryPriority()">
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom">
		<td align="center" class="tdtitle" width="50px" nowrap="nowrap">序号</td>
		<td align="center" class="tdtitle" width="40%">调查描述</td>
		<td align="center" class="tdtitle" width="60%">选项列表</td>
	</tr>
	<ext:iterate id="inquiry" indexId="inquiryIndex" property="inquiries">
		<tr style="cursor:pointer" align="center" valign="top" onclick="openInquiry('<ext:write name="inquiry" property="id"/>')">
			<td class="tdcontent" align="center"><ext:writeNumber name="inquiryIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="inquiry" property="descriptionText" maxCharCount="100" ellipsis="..."/></td>
			<td class="tdcontent" align="left">
				<ext:iterate id="option" indexId="optionIndex" name="inquiry" property="options">
					<ext:notEqual value="0" name="optionIndex">、</ext:notEqual><ext:write name="option" property="inquiryOption"/>
				</ext:iterate>
			</td>
		</tr>
	</ext:iterate>
</table>