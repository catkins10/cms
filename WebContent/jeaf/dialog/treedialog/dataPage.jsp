<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	<ext:empty property="listChildNodesUrl">MAX_TREE_NODES = 20000;</ext:empty>
</script>
<table border="0" cellpadding="5px" cellspacing="0">
	<tr><td id="divTree"></td></tr>
</table>
<ext:tree property="tree" parentElementId="divTree"/>