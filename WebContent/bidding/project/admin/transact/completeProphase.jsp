<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
com.yuanluesoft.bidding.project.forms.admin.Project projectForm = (com.yuanluesoft.bidding.project.forms.admin.Project)request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY);
//检查资料是否齐全
boolean allSelected = true;
if(projectForm.isNeedFullFiles() && projectForm.getProphaseFiles()!=null && !projectForm.getProphaseFiles().isEmpty()) {
	if(projectForm.getProphase()==null || projectForm.getProphase().getSubmitted()==null || projectForm.getProphase().getSubmitted().equals("")) {
		allSelected = false;
	}
	else {
		allSelected = projectForm.getProphase().getSubmitted().split("\r\n").length==projectForm.getProphaseFiles().size();
	}
}
if(projectForm.getAgentEnable().equals("是") && projectForm.getAgentMode().equals("随机抽签")) { %>
	<script>
	function completeProphase() {
		if('<ext:write property="agentDraw.content"/>'=='') {
			alert('委托代理内容不能为空。');
			return;
		}
		if('<ext:write property="agentDraw.drawTime"/>'=='') {
			alert('代理抽签时间不能为空。');
			return;
		}
		if(!<%=allSelected%>) {
			alert('前期资料未完全提供。');
			return;
		}
		FormUtils.doAction('completeProphase');
	}
	</script>
	<div style="width:550px">
		<jsp:include page="../tabpages/agentDrawRead.jsp" />
	</div>
<%
}
else { %>
	<script>
	function completeProphase() {
		if(!<%=allSelected%>) {
			alert('前期资料未完全提供。');
			return;
		}
		FormUtils.doAction('completeProphase');
	}
	</script>
	<div style="width:380px">
		<br>
		<div style="font-size:14px">
			&nbsp;&nbsp;是否确定完成招标前期资料备案?
		</div>
		<br>
	</div>
<%
}
%>