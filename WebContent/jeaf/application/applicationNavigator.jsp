<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>应用导航栏</title>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<ext:instanceof name="applicationNavigator" property="applicationNavigator" className="ApplicationTreeNavigator">
   		<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/tree/js/tree.js"></script>
   		<link href="<%=request.getContextPath()%>/jeaf/tree/css/tree.css" type="text/css" rel="stylesheet">
   	</ext:instanceof>
   	<ext:notInstanceof name="applicationNavigator" property="applicationNavigator" className="ApplicationTreeNavigator">
		<script>
			function onClickLink(link, event) {
				if(!event) {
					event = window.event;
				}
				var a = link.getElementsByTagName('a')[0];
				if(event.srcElement!=a) {
					EventUtils.clickElement(a);
				}
				if(a.target!="view" || a.href.substring(0, "javascript".length)=="javascript") {
					return;
				}
				var links = document.getElementById("links").getElementsByTagName("div");
				for(var i=0; i<links.length; i++) {
					links[i].className = "link";
				}
				link.className = "link selected";
			}
			function onMouseOverLink(link) {
				if(link.className=='link') {
					link.className = 'link mouseOver';
				}
			}
			function onMouseOutLink(link) {
				if(link.className=='link mouseOver') {
					link.className = 'link';
				}
			}
		</script>
	</ext:notInstanceof>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/js/subPageCssImport.js"></script>
</head>
<body>
    <ext:form action="/applicationNavigator">
    	<ext:notInstanceof property="applicationNavigator" className="ApplicationTreeNavigator">
    		<div class="applicationTitle">
    			<span class="applicationIcon"><img border="0" src="<%=request.getContextPath()%>/jeaf/application/icons/application.gif"/></span>
    			<span class="applicationTitleText"><ext:write property="applicationTitle"/></span>
    		</div>
    		<div class="linkSpace"></div>
    		<div id="links">
	    		<ext:iterate id="link" property="applicationNavigator.links">
	    			<div id="link" onclick="onClickLink(this, event)" onmouseover="onMouseOverLink(this)" onmouseout="onMouseOutLink(this)" class="link<ext:equal value="true" name="link" property="selected"> selected</ext:equal>">
		    			<span class="linkIcon"><img border="0" src="<ext:write name="link" property="iconURL"/>" align="bottom"/></span>
		    			<span class="linkTitle"><ext:write name="link" property="title"/></span>
		    			<a target="<ext:empty name="link" property="target">view</ext:empty><ext:notEmpty name="link" property="target"><ext:write name="link" property="target"/></ext:notEmpty>" href="<ext:write name="link" property="href"/>"></a>
	    			</div>
	    		</ext:iterate>
    		</div>
    		<ext:empty property="viewUrl">
    			<ext:iterate id="link" property="applicationNavigator.links">
    				<ext:equal value="true" name="link" property="selected">
		    			<script>top.frames["view"].location.replace("<ext:write name="link" property="href" filter="false"/>");</script>
	    			</ext:equal>
    			</ext:iterate>
    		</ext:empty>
    	</ext:notInstanceof>
    	<ext:instanceof property="applicationNavigator" className="ApplicationTreeNavigator">
    		<div id="navigatorTree"></div>
    		<ext:tree property="applicationNavigator.tree" parentElementId="navigatorTree"/>
    		<script>
				window.tree.onGetUrlForListChildNodes = function(nodeId) { //事件:获取URL,以得到子节点列表
					return "listApplicationNavigatorTreeNodes.shtml?applicationName=<ext:write property="applicationName"/>&parentNodeId=" + nodeId;
				};
				window.tree.onNodeSeleced = function(nodeId, nodeText, nodeType) { //事件:选中节点
					var url = this.getNodeAttribute(this.getNodeById(nodeId), "dataUrl");
					if(url!='') {
						if(url.substring(0, "javascript:".length).toLowerCase()=="javascript:") {
							window.setTimeout(url.substring("javascript:".length), 100);
						}
						else {
							top.frames["view"].location.replace(url);
						}
					}
				};
			</script>
    		<ext:empty property="viewUrl">
    			<ext:empty property="nodeId">
    				<script>window.tree.selectNode(window.tree.getRootNode());</script>
    			</ext:empty>
    			<ext:notEmpty property="nodeId">
    				<script>window.tree.selectNode(window.tree.getNodeById('<ext:write property="nodeId"/>'));</script>
    			</ext:notEmpty>
    		</ext:empty>
    	</ext:instanceof>
    	<ext:notEmpty property="viewUrl">
   			<script>top.frames["view"].location.replace("<ext:write property="viewUrl" filter="false"/>");</script>
   		</ext:notEmpty>
    </ext:form>
</body>
</html:html>