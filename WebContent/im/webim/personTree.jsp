<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/personTree" applicationName="im/webim" pageName="personTree">
	<jsp:include page="/jeaf/tree/tree.jsp" />
	<script>
		window.tree.onDblClickNode = function(nodeId, nodeText, nodeType) { //事件:节点被双击
			if(",employee,teacher,student,genearch,".indexOf("," + nodeType + ",")!=-1) {
				parent.frames['frameWebim'].webim.createChat(nodeId);
			}
		};
	</script>
</ext:form>