<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<body>
    <script>
    	top.location = "sceneConfig.shtml?act=create&siteId=<ext:write property="siteId"/>";
    	top.opener.setTimeout("try {refreshView('viewPackage');}catch(e){}", 100);
    </script>
</body>
</html:html>