<%@ page contentType="text/javascript; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	String beanName = ((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getName();
%>
var tree = window.trees[<%=request.getParameter("treeIndex")%>];
<ext:equal name="<%=beanName%>" property="actionResult" value="NOSESSIONINFO">
	LoginUtils.openLoginDialog(function() {
		tree.relistChildNodes('<%=com.yuanluesoft.jeaf.util.StringUtils.removeQueryParameters(com.yuanluesoft.jeaf.util.RequestUtils.getRequestURL(request, true), "seq")%>');
	});
</ext:equal>
<ext:equal name="<%=beanName%>" property="actionResult" value="SUCCESS">
	<ext:empty name="<%=beanName%>" property="childNodes">
		tree.setLeafNode('<ext:write name="<%=beanName%>" property="parentNodeId"/>');
	</ext:empty>
	<ext:iterate name="<%=beanName%>" id="childNode" indexId="childNodeIndex" property="childNodes">
		window.setTimeout(function() {
			tree.appendNode('<ext:write name="<%=beanName%>" property="parentNodeId"/>', '<ext:write name="childNode" property="nodeId"/>', '<%=((com.yuanluesoft.jeaf.tree.model.TreeNode)pageContext.getAttribute("childNode")).getNodeText()==null ? "" : ((com.yuanluesoft.jeaf.tree.model.TreeNode)pageContext.getAttribute("childNode")).getNodeText().replaceAll("'", "\\\\'")%>', '<ext:write name="childNode" property="nodeType"/>', '<ext:write name="childNode" property="nodeIcon"/>', '<ext:write name="childNode" property="nodeExpandIcon"/>', <ext:write name="childNode" property="hasChildNodes"/>, false, {<ext:iterate name="childNode" property="extendNodeProperties" id="extendNodeProperty" indexId="extendNodePropertyIndex"><ext:notEqual name="extendNodePropertyIndex" value="0">,</ext:notEqual><ext:write name="extendNodeProperty" property="name"/>:'<%=((com.yuanluesoft.jeaf.base.model.Attribute)pageContext.getAttribute("extendNodeProperty")).getValue()==null ? "" : ((com.yuanluesoft.jeaf.base.model.Attribute)pageContext.getAttribute("extendNodeProperty")).getValue().replaceAll("'", "\\\\'")%>'</ext:iterate>});
		}, <ext:write name="childNodeIndex"/>, 'JavaScript');
	</ext:iterate>
</ext:equal>