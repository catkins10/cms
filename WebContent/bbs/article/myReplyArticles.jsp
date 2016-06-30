<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>我发表的主题</title>
	<link href="../forum/css/bbs.css.jsp" type="text/css" rel="stylesheet">
</head>
<body leftmargin="0" topmargin="0" rightmargin="8px" bottommargin="0" style="border-style:none; overflow-x:hidden; overflow-y:hidden">
<ext:form action="/displayMyReplyArticles">
    <div class="viewPackage">
		<jsp:include page="articleView.jsp" />
		<div class="viewPageAndCategoryBottomBar">
			<jsp:include page="/jeaf/view/viewPageBar.jsp" />
		</div>
	</div>
	<html:hidden property="siteId"/>
</ext:form>
</body>
</html:html>