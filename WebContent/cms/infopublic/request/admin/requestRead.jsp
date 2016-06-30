<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<ext:equal value="1" property="proposerType">
		<tr>
			<td class="tdtitle" nowrap="nowrap">申请人类型</td>
			<td class="tdcontent">个人</td>
			<td class="tdtitle" nowrap="nowrap">编号</td>
			<td class="tdcontent"><ext:field writeonly="true" property="sn"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">姓名</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
			<td class="tdtitle" nowrap="nowrap">工作单位</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorUnit"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">证件名称</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorCertificateName"/></td>
			<td class="tdtitle" nowrap="nowrap">证件号码</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorIdentityCard"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系人电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
			<td class="tdtitle" nowrap="nowrap">传真</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorFax"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系地址</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="creatorAddress"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
			<td class="tdtitle" nowrap="nowrap">邮政编码</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorPostalcode"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">申请方式</td>
			<td class="tdcontent" nowrap="nowrap"><ext:empty property="applyMode">网站</ext:empty><ext:field writeonly="true" property="applyMode"/></td>
			<td class="tdtitle" nowrap="nowrap">申请时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		</tr>
	</ext:equal>
	<ext:equal value="2" property="proposerType">
		<tr>
			<td class="tdtitle" nowrap="nowrap">申请人类型</td>
			<td colspan="3" class="tdcontent">企业</td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">名称</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorUnit"/></td>
			<td class="tdtitle" nowrap="nowrap">机构代码</td>
			<td class="tdcontent"><ext:field writeonly="true" property="code"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">法人代表</td>
			<td class="tdcontent"><ext:field writeonly="true" property="legalRepresentative"/></td>
			<td class="tdtitle" nowrap="nowrap">联系人姓名</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系人电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
			<td class="tdtitle" nowrap="nowrap">传真</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorFax"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系地址</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="creatorAddress"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
			<td class="tdtitle" nowrap="nowrap">邮政编码</td>
			<td class="tdcontent"><ext:field writeonly="true" property="creatorPostalcode"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">申请方式</td>
			<td class="tdcontent" nowrap="nowrap"><ext:empty property="applyMode">网站</ext:empty><ext:field writeonly="true" property="applyMode"/></td>
			<td class="tdtitle" nowrap="nowrap">申请时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">涉及单位</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="unit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">内容描述</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">用途</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="purpose"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">介质</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="medium"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">获取方式</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="receiveMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">审批结果</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="approvalResult"/></td>
		</tr>
		<ext:equal value="不公开" property="approvalResult">
			<tr>
				<td width="90px" class="tdtitle">不公开原因</td>
				<td <ext:notEqual value="其他原因" property="notPublicType">colspan="3"</ext:notEqual> class="tdcontent"><ext:field writeonly="true" property="notPublicType"/></td>
				<ext:equal value="其他原因" property="notPublicType">
					<td width="90px">其他原因说明</td>
					<td class="tdcontent"><ext:field writeonly="true" property="notPublicReason"/></td>
				</ext:equal>
			</tr>
		</ext:equal>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">实际提供介质</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="doneMedium"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">实际提供方式</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="doneReceiveMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">审批时间</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="approvalTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否允许公开</td>
		<td class="tdcontent"><ext:field property="isPublic" writeonly="true"/></td>
		<td class="tdtitle" nowrap="nowrap">IP地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorIP" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
	<tr>
		<td class="tdtitle">站点</td>
		<td class="tdcontent" colspan="3" class="tdcontent"><ext:field writeonly="true" property="siteName"/></td>
	</tr>
	<ext:notEmpty property="readers">
		<tr>
			<td class="tdtitle">访问者</td>
			<td class="tdcontent" colspan="3" class="tdcontent"><ext:field writeonly="true" property="readers.visitorNames"/></td>
		</tr>
	</ext:notEmpty>
</table>