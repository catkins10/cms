<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function doSendInfo() {
		if(document.getElementsByName('doneMedium')[0].value=='') {
			alert('实际的提供介质不能为空');
			return;
		}
		if(document.getElementsByName('doneReceiveMode')[0].value=='') {
			alert('实际的获取方不能为空');
			return;
		}
		FormUtils.doAction('doSendInfo');
	}
</script>
<table border="0" cellspacing="0" cellpadding="3" width="600px" style="table-layout:fixed">
	<col width="110px" align="right" style="font-weight: bold">
	<col width="50%">
	<col width="110px" align="right" style="font-weight: bold">
	<col width="50%">
	<tr>
		<td valign="top">内容描述：</td>
		<td colspan="3"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td valign="top">用途：</td>
		<td colspan="3"><ext:field writeonly="true" property="purpose"/></td>
	</tr>
	<tr>
		<td>审批结果：</td>
		<td colspan="3"><ext:field writeonly="true" property="approvalResult"/></td>
	</tr>
	<ext:equal value="不公开" property="approvalResult">
		<tr>
			<td width="90px">不公开原因：</td>
			<td <ext:notEqual value="其他原因" property="notPublicType">colspan="3"</ext:notEqual>><ext:field writeonly="true" property="notPublicType"/></td>
			<ext:equal value="其他原因" property="notPublicType">
				<td width="90px">其他原因说明：</td>
				<td><ext:field writeonly="true" property="notPublicReason"/></td>
			</ext:equal>
		</tr>
	</ext:equal>
	<ext:equal value="1" property="proposerType">
		<tr>
			<td>申请人姓名：</td>
			<td colspan="3"><ext:field writeonly="true" property="creator"/></td>
		</tr>
		<tr>
			<td>工作单位：</td>
			<td colspan="3"><ext:field writeonly="true" property="creatorUnit"/></td>
		</tr>
		<tr>
			<td>联系地址：</td>
			<td colspan="3"><ext:field writeonly="true" property="creatorAddress"/></td>
		</tr>
		<tr>
			<td>联系人电话：</td>
			<td><ext:field writeonly="true" property="creatorTel"/></td>
			<td>传真：</td>
			<td><ext:field writeonly="true" property="creatorFax"/></td>
		</tr>
		<tr>
			<td>电子邮箱：</td>
			<td><ext:field writeonly="true" property="creatorMail"/></td>
			<td>邮政编码：</td>
			<td><ext:field writeonly="true" property="creatorPostalcode"/></td>
		</tr>
	</ext:equal>
	<ext:equal value="2" property="proposerType">
		<tr>
			<td>申请企业：</td>
			<td colspan="3"><ext:field writeonly="true" property="creatorUnit"/></td>
		</tr>
		<tr>
			<td>联系人姓名：</td>
			<td colspan="3"><ext:field writeonly="true" property="creator"/></td>
		</tr>
		<tr>
			<td>联系人电话：</td>
			<td><ext:field writeonly="true" property="creatorTel"/></td>
			<td>传真：</td>
			<td><ext:field writeonly="true" property="creatorFax"/></td>
		</tr>
		<tr>
			<td>联系地址：</td>
			<td colspan="3"><ext:field writeonly="true" property="creatorAddress"/></td>
		</tr>
		<tr>
			<td>电子邮箱：</td>
			<td><ext:field writeonly="true" property="creatorMail"/></td>
			<td>邮政编码：</td>
			<td><ext:field writeonly="true" property="creatorPostalcode"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td>期望的提供介质：</td>
		<td><ext:field writeonly="true" property="medium"/></td>
		<td>期望的获取方式：</td>
		<td><ext:field writeonly="true" property="receiveMode"/></td>
	</tr>
	<tr>
		<td>实际的提供介质：</td>
		<td><ext:field property="doneMedium"/></td>
		<td>实际的获取方式：</td>
		<td><ext:field property="doneReceiveMode"/></td>
	</tr>
	<tr>
		<td>提供时间：</td>
		<td><ext:field property="approvalTime"/></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td>是否发布到网站：</td>
		<td><ext:field property="publicPass"/></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td>发布申请内容：</td>
		<td><ext:field property="publicBody"/></td>
		<td>发布办理过程：</td>
		<td><ext:field property="publicWorkflow"/></td>
	</tr>
</table>