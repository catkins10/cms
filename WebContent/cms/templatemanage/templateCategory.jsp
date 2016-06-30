<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<link href="<%=request.getContextPath()%>/jeaf/tree/css/tree.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
<div class="viewCategoryBar" style="float:left">
	<span nowrap onclick="selectCategory(this, '<ext:write property="viewPackageName.view.applicationName" />', '<ext:write property="viewPackageName.view.name"/>')" class="viewSelectCategory">&nbsp;</span>
	<div id="divSelect" align="left" onblur="hideCategory(this)" onselectstart="return false;" class="viweCategoryBox" style="overflow:auto;display:none">
		<ext:tree parentElementId="divSelect" property="sitePageTree"/>
		<script>
			MAX_TREE_NODES = 1000;
			function selectCategory(src) { //选择分类
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
			function hideCategory(src) {
				var divSelect = document.getElementById("divSelect");
				//检查是否子元素
				if(divSelect.contains(document.activeElement)) {
					divSelect.focus();
					return;
				}
				divSelect.style.display = "none";
			}
			window.tree.onNodeSeleced = function(nodeId, nodeText, nodeType) { //事件:节点被选中
				if(nodeType!="page") { //站点页面
					return;
				}
				var values = nodeId.split("__");
				location.href = "?siteId=" + document.getElementsByName("siteId")[0].value + "&applicationName=" + values[0] + "&pageName=" + values[1] + "&themeId=" + document.getElementsByName("themeId")[0].value;
			};
		</script>
	</div>
</div>