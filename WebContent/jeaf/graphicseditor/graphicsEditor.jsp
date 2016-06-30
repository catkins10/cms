<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<title><ext:write property="formTitle"/></title>
	<link href="<%=request.getContextPath()%>/jeaf/tree/css/tree.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/jeaf/graphicseditor/css/graphicseditor.css" type="text/css" rel="stylesheet">
	<script language="JavaScript" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%>/jeaf/graphicseditor/js/graphicseditor.js"></script>
	<script language="JavaScript" src="<%=request.getContextPath()%><ext:write property="js"/>"></script>
</head>
<body style="margin:0; overflow:hidden; border-style: none">
<ext:form action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>">
	<div id="actionBar">
		<ext:iterate id="formAction" property="formActions">
			<div onclick="<ext:write name="formAction" property="execute"/>" class="action" style="background-image:url(<%=request.getContextPath()%><ext:write name="formAction" property="image"/>);" onmouseover="this.className='action actionOver';" onmouseout="this.className='action';">
				<ext:write name="formAction" property="title"/>
			</div>
		</ext:iterate>
	</div>
	<div id="drawActionBar"></div>
	<div id="canvasView"></div>
	<div id="propertyView">
		<div id="propertyTree">&nbsp;</div>
		<div id="propertyDetail">&nbsp;</div>
	</div>
</ext:form>
</body>
</html>