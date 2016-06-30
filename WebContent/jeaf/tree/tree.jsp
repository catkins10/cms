<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<link href="<%=request.getContextPath()%>/jeaf/tree/css/tree.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>

<div id="divTree"></div>
<ext:tree property="tree" parentElementId="divTree"/>
<script language="javascript">
	window.tree.onGetUrlForListChildNodes = function(nodeId) { //事件:获取URL,以得到子节点列表
		var url = "<ext:write property="listChildNodesUrl" filter="false"/>";
		url += (url.lastIndexOf("?")==-1 ? "?" : "&") + "parentNodeId=" + StringUtils.utf8Encode(nodeId);
		return url;
	};
</script>