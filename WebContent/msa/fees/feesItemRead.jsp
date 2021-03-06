<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="0" cellpadding="5" cellspacing="0">
	<col align="right">
	<col width="50%">
	<col align="right">
	<col width="50%">
	<tr>
		<td nowrap="nowrap">考试类别：</td>
		<td><ext:field writeonly="true" property="item.examCategory"/></td>
		<td nowrap="nowrap">院校：</td>
		<td><ext:field writeonly="true" property="item.address"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">考试时间：</td>
		<td><ext:field writeonly="true" property="item.examTime"/></td>
		<td nowrap="nowrap">考试期数：</td>
		<td><ext:field writeonly="true" property="item.period"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">考生人数：</td>
		<td><ext:field writeonly="true" property="item.examineeNumber"/></td>
		<td nowrap="nowrap">费用(元)：</td>
		<td><ext:field writeonly="true" property="item.charge"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">缴费方式：</td>
		<td><ext:field writeonly="true" property="item.paymentMode"/></td>
		<td nowrap="nowrap">负责单位：</td>
		<td><ext:field writeonly="true" property="item.unitName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">备注：</td>
		<td colspan="3"><ext:field writeonly="true" property="item.remark"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">完成时间：</td>
		<td><ext:field writeonly="true" property="item.completeTime"/></td>
		<td nowrap="nowrap">经办人：</td>
		<td><ext:field writeonly="true" property="item.transactor"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">完成情况说明：</td>
		<td colspan="3"><ext:field writeonly="true" property="item.feedback"/></td>
	</tr>
</table>