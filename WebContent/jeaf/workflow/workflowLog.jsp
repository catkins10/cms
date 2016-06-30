<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	window.tabLists[0].onTabSelected = function(tabId) { //事件处理:选中TAB
		if(tabId == 'workflowLog') {
			var workflowLogFrame = document.getElementById("workflowLogFrame");
			if(workflowLogFrame.src.indexOf("viewWorkflowInstance")==-1) {
				 workflowLogFrame.src = "<%=request.getContextPath()%>/<ext:write property="formDefine.applicationName"/>/<ext:write property="formDefine.name"/>.shtml?id=<ext:write property="id"/>&workflowAction=viewWorkflowInstance&seq=" + Math.random();
			}
		}
	};
</script>
<iframe id="workflowLogFrame" allowtransparency="true" src="<%=request.getContextPath()%>/blank.html" style="width:100%; height:300px; border-style: none;"></iframe>