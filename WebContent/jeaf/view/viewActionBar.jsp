<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %> 
<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<span class="viewActionBar" style="display: inline-block">
	<ext:notEmpty property="<%=viewPackageName + ".view.actions"%>">
		<div style="float:left">
			<ext:iterate id="action" indexId="actionIndex" property="<%=viewPackageName + ".view.actions"%>">
				<ext:notEqual value="0" name="actionIndex">
					<span class="viewActionSpace" style="float:left; font-size:0px">&nbsp;</span>
				</ext:notEqual>
				<div onclick="<ext:write name="action" property="execute"/>" style="float:left">
					<span style="float:left" class="viewActionImageNormal">
						<ext:empty name="action" property="image">
							<span style="float:left; display: inline-block" class="viewActionDefaultImage"></span>
						</ext:empty>
						<ext:notEmpty name="action" property="image">
							<img src="<ext:write name="action" property="image"/>">
						</ext:notEmpty>
					</span>
					<span style="float:left; white-space:nowrap; display: inline-block" class="viewActionTextNormal">
						<ext:write name="action" property="title"/><ext:instanceof className="ViewActionGroup" name="action"><span class="viewActionDropdown">&nbsp;</span></ext:instanceof>
					</span>
				</div>
			</ext:iterate>
		</div>
	</ext:notEmpty>
	<div style="float:right">
		<span onclick="printAsExcel()"><span class="printAsExcelImage" title="打印"></span><span class="printAsExcelText">打印</span></span>
		<span onclick="customView()"><span class="viewCustomImage" title="视图定制"></span><span class="viewCustomText">视图定制</span></span>
	</div>
</span>