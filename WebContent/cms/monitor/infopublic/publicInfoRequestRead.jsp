<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请人类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="proposerType"/></td>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">机构代码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="code"/></td>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field writeonly="true" property="legalRepresentative"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请方式</td>
		<td class="tdcontent" nowrap="nowrap"><ext:empty property="applyMode">网站</ext:empty><ext:field writeonly="true" property="applyMode"/></td>
		<td class="tdtitle" nowrap="nowrap">申请时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">内容描述</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="content"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">用途</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="purpose"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">介质</td>
		<td class="tdcontent"><ext:field writeonly="true" property="medium"/></td>
		<td class="tdtitle" nowrap="nowrap">实际提供介质</td>
		<td class="tdcontent"><ext:field writeonly="true" property="doneMedium"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">获取方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="receiveMode"/></td>
		<td class="tdtitle" nowrap="nowrap">实际提供方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="doneReceiveMode"/></td>
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
</table>