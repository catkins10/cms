<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectName"/></td>
		<td class="tdtitle" nowrap="nowrap">工程类别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectCategory"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标内容</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectProcedure"/></td>
		<td class="tdtitle" nowrap="nowrap">所属地区</td>
		<td class="tdcontent"><ext:field writeonly="true" property="city"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="biddingMode"/></td>
		<td class="tdtitle" nowrap="nowrap">是否委托招标代理</td>
		<td class="tdcontent"><ext:field writeonly="true" property="agentEnable"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资格审查方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="approvalMode"/></td>
		<td class="tdtitle" nowrap="nowrap">代理产生方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="agentMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="owner"/></td>
		<td class="tdtitle" nowrap="nowrap">单位性质</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerRepresentative"/></td>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerLinkman"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人身份证</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerLinkmanIdCard"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerFax"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮件</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设地点</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">建设规模</td>
		<td class="tdcontent"><ext:field writeonly="true" property="scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建筑面积</td>
		<td class="tdcontent"><ext:field writeonly="true" property="area"/>㎡</td>
		<td class="tdtitle" nowrap="nowrap">招标编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>