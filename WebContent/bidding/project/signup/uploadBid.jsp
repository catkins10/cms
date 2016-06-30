<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<ext:form action="/doUploadBid" applicationName="bidding/project/signup" pageName="uploadBid">
	<jsp:include page="/jeaf/form/warn.jsp" />
	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col align="right">
		<col width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">报名号</td>
			<td class="tdcontent"><ext:field writeonly="true" property="signUpNo"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目编号</td>
			<td class="tdcontent"><ext:field  writeonly="true" property="project.projectNumber"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap>投标文件上传</td>
			<td class="tdcontent"><ext:field property="bid"/></td>
		</tr>
	</table>
	<br>
	<center>
		<ext:button name="提交" onclick="submitBid()"/>
	</center>
	<script>
		function submitBid() {
			if(FormUtils.getAttachmentCount('bid')==0) {
				alert('尚未完成投标文件上传');
				return false;
			}
			if(!confirm('是否确定提交投标文件？')) {
				return false;
			}
			FormUtils.doAction('doUploadBid');
		}
	</script>
	<html:hidden property="signUpNo"/>
	<span id="project.projectNumber" style="display:none"><ext:write property="project.projectNumber"/></span>
</ext:form>