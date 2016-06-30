<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<link href="<%=request.getContextPath()%>/jeaf/tree/css/tree.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
<script>
	function selectSite(src) { //选择分类
		var width = 200;
		var pos = DomUtils.getAbsolutePosition(src);
		var left = pos.left - width + src.offsetWidth;
		if(left<10) {
			left = 10;
		}
		var top = pos.top + src.offsetHeight + 3;
		var divSelect = document.getElementById("divSelect");
		divSelect.style.left = left;
		divSelect.style.top = top;
		divSelect.style.width = width;
		divSelect.style.display = "";
		divSelect.focus();
	}
	function hideSiteSelect(src) {
		var divSelect = document.getElementById("divSelect");
		//检查是否子元素
		if(divSelect.contains(document.activeElement)) {
			divSelect.focus();
			return;
		}
		divSelect.style.display = "none";
	}
</script>
<div class="viewCategoryBar" style="float:left">
	<span nowrap onclick="selectSite(this)" class="viewSelectCategory">&nbsp;</span>
	<div id="divSelect" align="left" onblur="hideSiteSelect(this)" onselectstart="return false;" class="viweCategoryBox" style="overflow:auto;display:none"></div>
	<ext:tree property="orgTree" parentElementId="divSelect"/>
	<script>
		window.tree.onNodeSeleced = function(nodeId, nodeText, nodeType) { //事件:节点被选中
			var orgTypes = document.getElementsByName("orgTypes")[0].value;
			if(orgTypes!="" && orgTypes!="all" && ("," + orgTypes + ",").indexOf("," + nodeType + ",")==-1) {
				return;
			}
			var popedomNames = document.getElementsByName("popedomNames")[0].value;
			location.href = "?applicationName=" + document.getElementsByName("applicationName")[0].value + "&viewApplicationName=" + document.getElementsByName("viewApplicationName")[0].value + "&viewName=" + document.getElementsByName("viewName")[0].value + "&orgId=" + nodeId + "&orgTypes=" + orgTypes + (popedomNames="" ? "" : "&popedomNames=" + popedomNames);
		};
		window.tree.onGetUrlForListChildNodes = function(nodeId) { //事件:获取URL,以得到子节点列表
			var popedomNames = document.getElementsByName("popedomNames")[0].value;
			return "../listChildOrgs.shtml?parentNodeId=" + nodeId + "&selectNodeTypes=" + document.getElementsByName("orgTypes")[0].value + "&popedomFilters=" + (popedomNames=="" ? "manager" : popedomNames);
		};
	</script>
</div>