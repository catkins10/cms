<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/bidding/enterprise/js/enterprise.js"></script>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<ext:equal value="create" property="act">
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目名称</td>
			<td class="tdcontent"><ext:field property="projectName"/></td>
			<td class="tdtitle" nowrap="nowrap">工程类别</td>
			<td class="tdcontent"><ext:field property="projectCategory"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">招标内容</td>
			<td class="tdcontent"><ext:field property="projectProcedure"/></td>
			<td class="tdtitle" nowrap="nowrap">所属地区</td>
			<td class="tdcontent"><ext:field property="city"/></td>
		</tr>
	</ext:equal>
	<ext:notEqual value="create" property="act">
		<tr>
			<td class="tdtitle" nowrap="nowrap">名称</td>
			<td class="tdcontent"><ext:field property="projectName"/></td>
			<td class="tdtitle" nowrap="nowrap">工程类别</td>
			<td class="tdcontent"><ext:field writeonly="true" property="projectCategory"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">招标内容</td>
			<td class="tdcontent"><ext:field writeonly="true" property="projectProcedure"/></td>
			<td class="tdtitle" nowrap="nowrap">所属地区</td>
			<td class="tdcontent"><ext:field writeonly="true" property="city"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标方式</td>
		<td class="tdcontent"><ext:field property="biddingMode"/></td>
		<td class="tdtitle" nowrap="nowrap">是否委托招标代理</td>
		<td class="tdcontent"><ext:field property="agentEnable" onclick="onOptionChange()"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资格审查方式</td>
		<td class="tdcontent"><ext:field property="approvalMode"/></td>
		<td class="tdtitle" nowrap="nowrap">代理产生方式</td>
		<td class="tdcontent"><ext:field onclick="onOptionChange()" property="agentMode"/></td>
	</tr>
	<tr id="agentRow1">
		<td class="tdtitle" nowrap="nowrap">指定代理</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentName"/></td>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentRepresentative"/></td>
	</tr>
	<tr id="agentRow2">
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentLinkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentTel"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设单位</td>
		<td class="tdcontent"><ext:field property="owner"/></td>
		<td class="tdtitle" nowrap="nowrap">单位性质</td>
		<td class="tdcontent"><ext:field property="ownerType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="ownerRepresentative"/></td>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="ownerLinkman"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人身份证</td>
		<td class="tdcontent"><ext:field property="ownerLinkmanIdCard"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="ownerTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="ownerFax"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮件</td>
		<td class="tdcontent"><ext:field property="ownerMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设地点</td>
		<td class="tdcontent"><ext:field property="projectAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">建设规模</td>
		<td class="tdcontent"><ext:field property="scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建筑面积(㎡)</td>
		<td class="tdcontent"><ext:field property="area"/></td>
		<td class="tdtitle" nowrap="nowrap">招标编号</td>
		<td class="tdcontent"><ext:field property="projectNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
</table>
<script>
function selectOwner() {
	if(document.getElementsByName('agentEnable')[0].checked) { //委托代理招标
		DialogUtils.openSelectDialog('bidding/project', 'projectOwner', 600, 400, false, 'owner{owner},ownerType{ownerType},ownerRepresentative{ownerRepresentative},ownerLinkman{ownerLinkman},ownerLinkmanIdCard{ownerLinkmanIdCard},ownerTel{ownerTel},ownerFax{ownerFax},ownerMail{ownerMail}', '', '', '', ',', true);
	}
	else { //自主招标
		selectEnterprise(600, 400, false, 'ownerId{id},owner{name},ownerType{kind},ownerRepresentative{legalRepresentative},ownerLinkman{linkman},ownerLinkmanIdCard{linkmanIdNumber},ownerTel{tel}');
	}
}
function onOptionChange() {
	var agentEnable = document.getElementsByName('agentEnable')[0].checked;
	document.getElementsByName('owner')[0].readOnly = !agentEnable;
	document.getElementsByName('agentMode')[0].disabled = !agentEnable;
	document.getElementsByName('agentMode')[1].disabled = !agentEnable;
	var agentDraw = document.getElementsByName('agentMode')[0].checked;
	document.getElementById('agentRow1').style.display = !agentDraw && agentEnable ? '' : 'none';
	document.getElementById('agentRow2').style.display = !agentDraw && agentEnable ? '' : 'none';
}
onOptionChange();
</script>