<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>

<script>
function selectItem(item) {
	var items = document.getElementById("personalPanelMenuBar").childNodes;
	for(var i=0; i<items.length; i++) {
		if(items[i].className=="personalPanelMenuItemSelected") {
			items[i].className = "personalPanelMenuItem";
			break;
		}
	}
	item.className = "personalPanelMenuItemSelected";
	item.childNodes[0].click();
}
</script>
<div id="personalPanelMenuBar" class="personalPanelMenu">
	<div class="personalPanelMenuItem" onclick="selectItem(this)" onmouseover="if(className=='personalPanelMenuItem')className='personalPanelMenuItemHover';" onmouseout="if(className=='personalPanelMenuItemHover')className='personalPanelMenuItem';">
		<a target="personalPanelConfigure" href="bbsUser.shtml?id=<ext:write name="SessionInfo" property="userId" scope="session"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>">修改个人资料</a>
	</div>
	<div class="personalPanelMenuItem" onclick="selectItem(this)" onmouseover="if(className=='personalPanelMenuItem')className='personalPanelMenuItemHover';" onmouseout="if(className=='personalPanelMenuItemHover')className='personalPanelMenuItem';">
		<a target="personalPanelConfigure" href="javascript:DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/sso/changePassword.shtml', 430, 260);">修改密码</a>
	</div>
	<div class="personalPanelMenuItem" onclick="selectItem(this)" onmouseover="if(className=='personalPanelMenuItem')className='personalPanelMenuItemHover';" onmouseout="if(className=='personalPanelMenuItemHover')className='personalPanelMenuItem';">
		<a target="personalPanelConfigure" href="../article/myArticles.shtml<%=(request.getParameter("siteId")!=null ? "?siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>">我发表的主题</a>
	</div>
	<div class="personalPanelMenuItem" onclick="selectItem(this)" onmouseover="if(className=='personalPanelMenuItem')className='personalPanelMenuItemHover';" onmouseout="if(className=='personalPanelMenuItemHover')className='personalPanelMenuItem';">
		<a target="personalPanelConfigure" href="../article/myReplyArticles.shtml<%=(request.getParameter("siteId")!=null ? "?siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>">我回复过的主题</a>
	</div>
</div>