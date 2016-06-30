<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newContact() {
	PageUtils.newrecord('fdi/customer', 'admin/contact', 'mode=dialog,width=720,height=480', 'id=<ext:write property="id"/>');
}
function openContact(contactId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/fdi/customer/admin/contact.shtml?id=<ext:write property="id"/>&companyContact.id=' + contactId, 720, 480);
}
function newDiscuss(contactId) {
	PageUtils.newrecord('fdi/customer', 'admin/discuss', 'mode=dialog,width=600,height=300', 'id=<ext:write property="id"/>&discuss.contactId=' + contactId);
}
function openDiscuss(discussId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/fdi/customer/admin/discuss.shtml?id=<ext:write property="id"/>&discuss.id=' + discussId, 600, 300);
}
</script>
<ext:equal name="editabled" value="true">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加联系人" onclick="newContact()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="32px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">中文姓名</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">电话</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">手机</td>
		<td class="tdtitle" nowrap="nowrap" width="150px">E-mail</td>
		<td class="tdtitle" nowrap="nowrap">往来情况</td>
	</tr>
	<ext:iterate id="contact" indexId="contactIndex" property="contacts">
		<tr style="cursor:pointer" valign="top">
			<td class="tdcontent" align="center" onclick="openContact('<ext:write name="contact" property="id"/>')"><ext:writeNumber name="contactIndex" plus="1"/></td>
			<td class="tdcontent" onclick="openContact('<ext:write name="contact" property="id"/>')"><ext:write name="contact" property="name"/></td>
			<td class="tdcontent" align="center" onclick="openContact('<ext:write name="contact" property="id"/>')"><ext:write name="contact" property="tel"/></td>
			<td class="tdcontent" align="center" onclick="openContact('<ext:write name="contact" property="id"/>')"><ext:write name="contact" property="mobile"/></td>
			<td class="tdcontent" align="center" onclick="openContact('<ext:write name="contact" property="id"/>')"><ext:write name="contact" property="email"/></td>
			<td class="tdcontent">
				<ext:equal name="editabled" value="true">
					<div style="padding-bottom:3px">
						<input type="button" class="button" value="登记往来情况" onclick="newDiscuss('<ext:write name="contact" property="id"/>')">
					</div>
				</ext:equal>
				<ext:iterate id="discuss" indexId="discussIndex" name="contact" property="discusses">
					<a href="javascript:openDiscuss('<ext:write name="discuss" property="id"/>')">
						<pre style="padding-bottom:3px"><ext:writeNumber name="discussIndex" plus="1"/>、<ext:write name="discuss" property="discussTime" format="yyyy-MM-dd"/>&nbsp;&nbsp;<ext:write name="discuss" property="discussContent"/></pre>
					</a>
				</ext:iterate>
			</td>
		</tr>
	</ext:iterate>
</table>