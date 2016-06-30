<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function onProposerTypeChanged() {
		var proposerTypes = document.getElementsByName("proposerType");
		document.getElementById("trPersonProperty").style.display = proposerTypes[0].checked ? "" : "none";
		document.getElementById("trCompanyProperty").style.display = proposerTypes[1].checked ? "" : "none";
	}
</script>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请人类型</td>
		<td class="tdcontent"><ext:field property="proposerType" onclick="onProposerTypeChanged()"/></td>
		<td class="tdtitle" nowrap="nowrap">编号</td>
		<td class="tdcontent"><ext:field property="sn"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent"><ext:field property="creatorUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">联系人姓名</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr id="trPersonProperty" style="<ext:notEqual value="1" property="proposerType">display:none</ext:notEqual>">
		<td class="tdtitle" nowrap="nowrap">证件名称</td>
		<td class="tdcontent"><ext:field property="creatorCertificateName"/></td>
		<td class="tdtitle" nowrap="nowrap">证件号码</td>
		<td class="tdcontent"><ext:field property="creatorIdentityCard"/></td>
	</tr>
	<tr id="trCompanyProperty" style="<ext:notEqual value="2" property="proposerType">display:none</ext:notEqual>">
		<td class="tdtitle" nowrap="nowrap">机构代码</td>
		<td class="tdcontent"><ext:field property="code"/></td>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="legalRepresentative"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人电话</td>
		<td class="tdcontent"><ext:field property="creatorTel"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="creatorFax"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系地址</td>
		<td colspan="3" class="tdcontent"><ext:field property="creatorAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><ext:field property="creatorMail"/></td>
		<td class="tdtitle" nowrap="nowrap">邮政编码</td>
		<td class="tdcontent"><ext:field property="creatorPostalcode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">隶属站点</td>
		<td class="tdcontent"><ext:field property="siteName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">涉及单位</td>
		<td colspan="3" class="tdcontent"><ext:field property="unit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" nowrap="nowrap">内容描述</td>
		<td colspan="3" class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" nowrap="nowrap">用途</td>
		<td colspan="3" class="tdcontent"><ext:field property="purpose"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">介质</td>
		<td colspan="3" class="tdcontent"><ext:field property="mediums"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">获取方式</td>
		<td colspan="3" class="tdcontent"><ext:field property="receiveModes"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请方式</td>
		<td class="tdcontent"><ext:field property="applyMode"/></td>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="registrant"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="3" class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>