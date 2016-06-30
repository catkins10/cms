<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	com.yuanluesoft.bidding.project.forms.admin.Project projectForm = (com.yuanluesoft.bidding.project.forms.admin.Project)request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY);
	//检查资料是否齐全
	boolean allSelected = true;
	if(projectForm.isNeedFullFiles() && projectForm.getArchiveFiles()!=null && !projectForm.getArchiveFiles().isEmpty()) {
		if(projectForm.getArchive()==null || projectForm.getArchive().getSubmitted()==null || projectForm.getArchive().getSubmitted().equals("")) {
			allSelected = false;
		}
		else {
			allSelected = projectForm.getArchive().getSubmitted().split("\r\n").length==projectForm.getArchiveFiles().size();
		}
	}
%>
<script>
function archive() {
	if(!<%=allSelected%>) {
		alert('备案资料未完全提供。');
		return;
	}
	FormUtils.doAction('archive');
}
</script>
<div style="width:380px">
	<br>
	<div style="font-size:14px">
		&nbsp;&nbsp;是否确定完成书面报告备案?
	</div>
	<br>
</div>