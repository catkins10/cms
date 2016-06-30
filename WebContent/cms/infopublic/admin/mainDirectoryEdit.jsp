<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
<script language="JavaScript" charset="utf-8" src="../js/infopublic.js"></script>
<ext:tab>
	<ext:tabBody tabId="basic">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col valign="middle">
			<col valign="middle" width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">名称</td>
				<td class="tdcontent"><ext:field property="directoryName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">描述</td>
				<td class="tdcontent"><ext:field property="description"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">机构代码</td>
				<td class="tdcontent">
					<ext:iterate id="unitCode" indexId="unitCodeIndex" property="unitCodes"><ext:notEqual value="0" name="unitCodeIndex">、</ext:notEqual><a href="javascript:DialogUtils.openDialog('<%=request.getContextPath()%>/cms/infopublic/admin/unitCode.shtml?act=edit&id=<ext:write property="id"/>&unitCode.id=<ext:write name="unitCode" property="id"/>', 360, 200)"><ext:write name="unitCode" property="unitName"/>：<ext:write name="unitCode" property="code"/></a></ext:iterate>
					<input type="button" class="button" value="添加" onclick="DialogUtils.openDialog('<%=request.getContextPath()%>/cms/infopublic/admin/unitCode.shtml?act=create&id=<ext:write property="id"/>', 360, 200)"/>
				</td>
			</tr>
			<ext:notEqual value="0" property="id">
				<tr>
					<td class="tdtitle" nowrap="nowrap">上级目录</td>
					<td class="tdcontent">
						<ext:equal value="true" property="changeParentDirectoryDisabled">
							<ext:field writeonly="true" property="parentDirectoryName"/>
						</ext:equal>
						<ext:equal value="false" property="changeParentDirectoryDisabled">
							<ext:field property="parentDirectoryName"/>
						</ext:equal>
					</td>
				</tr>
			</ext:notEqual>
			<tr>
				<td class="tdtitle" nowrap="nowrap">隶属站点</td>
				<td class="tdcontent"><ext:field property="siteName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">允许手工编制索引号</td>
				<td class="tdcontent"><ext:field property="manualCodeEnabled"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">信息删除后重新编号</td>
				<td class="tdcontent"><ext:field property="recodeEnabled"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">流水号</td>
				<td class="tdcontent"><ext:field property="sequenceByDirectory"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">同步到网站栏目</td>
				<td class="tdcontent"><ext:field property="synchSiteNames"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">信息发布流程</td>
				<td class="tdcontent"><ext:field property="workflowName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">信息编辑删除信息</td>
				<td class="tdcontent"><ext:field property="editorDeleteable"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">信息编辑撤销信息</td>
				<td class="tdcontent"><ext:field property="editorReissueable"/></td>
			</tr>
			<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
		</table>
	</ext:tabBody>
	
	<ext:tabBody tabId="publicGuide">
		<div>
			<ext:field property="guide"/>
		</div>
	</ext:tabBody>
</ext:tab>