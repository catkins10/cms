<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>搜索结果</title>
</head>
<body>
    <html:form action="/unitSearchResult">
    	<script type="text/javascript">
    		<ext:empty property="units">
    			parent.setTimeout("noUnitsFound()", 10);
    		</ext:empty>
	    	<ext:iterate id="unit" indexId="unitIndex" property="units">
				parent.setTimeout("appendFoundUnit('<ext:write name="unit" property="id"/>', '<ext:write name="unit" property="directoryName"/>', <ext:write name="unitIndex"/>)", 10);
			</ext:iterate>
		</script>
    </html:form>
</body>
</html:html>