<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<col>
	<col width="100%">
	<tr>
		<td nowrap="nowrap" align="right">业务名称：</td>
		<td><ext:field property="unitBusiness.businessName" readonly="true"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">是否启用：</td>
		<td><ext:field property="unitBusiness.enabled"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">短信号码：</td>
		<td><ext:field property="unitBusiness.smsNumber"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">计费方式：</td>
		<td><ext:field property="unitBusiness.chargeMode"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">价格(元)：</td>
		<td><ext:field property="unitBusiness.price"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">折扣：</td>
		<td><ext:field property="unitBusiness.discount"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">附加信息：</td>
		<td><ext:field property="unitBusiness.postfix"/></td>
	</tr>
	<ext:equal value="true" property="sendPopedomConfig">
		<tr>
			<td nowrap="nowrap" align="right">短信发送编辑：</td>
			<td><ext:field property="smsSendEditors.visitorNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">短信发送审核：</td>
			<td><ext:field property="smsSendAuditors.visitorNames"/></td>
		</tr>
	</ext:equal>
	<ext:equal value="true" property="receivePopedomConfig">
		<tr>
			<td nowrap="nowrap" align="right">短信接收受理：</td>
			<td><ext:field property="smsReceiveAccepters.visitorNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">短信接收审核：</td>
			<td><ext:field property="smsReceiveAuditors.visitorNames"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td nowrap="nowrap" align="right">最后修改时间：</td>
		<td><ext:field property="unitBusiness.lastModified"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">最后修改人：</td>
		<td><ext:field property="unitBusiness.lastModifier"/></td>
	</tr>
</table>