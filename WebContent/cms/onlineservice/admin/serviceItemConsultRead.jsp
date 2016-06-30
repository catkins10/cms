<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="50%">
	<col align="right">
	<col width="50%">
	<tr>
		<td nowrap="nowrap">咨询主题：</td>
		<td><ext:field writeonly="true" property="consult.subject"/></td>
		<td>编号：</td>
		<td colspan="3"><ext:field property="consult.sn"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">详细内容：</td>
		<td colspan="3"><ext:field readonly="true" property="consult.content" style="height: 40px"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">咨询人：</td>
		<td><ext:field writeonly="true" property="consult.creator"/></td>
		<td nowrap="nowrap">咨询时间：</td>
		<td><ext:field writeonly="true" property="consult.created"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">身份证号码：</td>
		<td><ext:field writeonly="true" property="consult.creatorIdentityCard"/></td>
		<td nowrap="nowrap">联系电话：</td>
		<td><ext:field writeonly="true" property="consult.creatorTel"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">邮箱：</td>
		<td><ext:field writeonly="true" property="consult.creatorMail"/></td>
		<td nowrap="nowrap">职业：</td>
		<td><ext:field writeonly="true" property="consult.creatorJob"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">所在单位：</td>
		<td><ext:field writeonly="true" property="consult.creatorUnit"/></td>
		<td nowrap="nowrap">地址：</td>
		<td><ext:field writeonly="true" property="consult.creatorAddress"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">邮编：</td>
		<td><ext:field writeonly="true" property="consult.creatorPostalcode"/></td>
		<td nowrap="nowrap">传真：</td>
		<td><ext:field writeonly="true" property="consult.creatorFax"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">公开正文：</td>
		<td><ext:field writeonly="true" property="consult.publicBody"/></td>
		<td nowrap="nowrap">公开流程：</td>
		<td><ext:field writeonly="true" property="consult.publicWorkflow"/></td>
	</tr>
	
	<tr>
		<td nowrap="nowrap">办理意见：</td>
		<td colspan="3"><ext:field readonly="true" property="opinion.opinion" style="height: 40px"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">填写人：</td>
		<td><ext:field writeonly="true" styleClass="field required" property="opinion.personName"/></td>
		<td nowrap="nowrap">填写时间：</td>
		<td><ext:field writeonly="true" styleClass="field required" property="opinion.created"/></td>
	</tr>
</table>