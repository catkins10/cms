<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="width:500px">
	<div style="font-size:14px">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">名称</td>
				<td class="tdcontent"><ext:field writeonly="true" property="projectName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">类别</td>
				<td class="tdcontent"><ext:field writeonly="true" property="projectCategory"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所属地区</td>
				<td class="tdcontent"><ext:field writeonly="true" property="city"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">招标方式</td>
				<td class="tdcontent"><ext:field writeonly="true" property="biddingMode"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">是否委托招标代理</td>
				<td class="tdcontent"><ext:field writeonly="true" property="agentEnable"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">资格审查方式</td>
				<td class="tdcontent"><ext:field writeonly="true" property="approvalMode"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">代理产生方式</td>
				<td class="tdcontent"><ext:field writeonly="true" property="agentMode"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">建设单位</td>
				<td class="tdcontent"><ext:field writeonly="true" property="owner"/></td>
			</tr>
		</table>
	</div>
</div>