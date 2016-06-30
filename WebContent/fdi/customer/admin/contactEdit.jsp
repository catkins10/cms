<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td nowrap="nowrap" align="right">中文姓名：</td>
		<td><ext:field property="companyContact.name"/></td>
		<td nowrap="nowrap" align="right">英文姓名：</td>
		<td><ext:field property="companyContact.englishName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">性别：</td>
		<td><ext:field property="companyContact.sex"/></td>
		<td nowrap="nowrap" align="right">个人地址：</td>
		<td><ext:field property="companyContact.address"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">电话：</td>
		<td><ext:field property="companyContact.tel" title="含国别、地区号"/></td>
		<td nowrap="nowrap" align="right">手机：</td>
		<td><ext:field property="companyContact.mobile"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">传真：</td>
		<td><ext:field property="companyContact.fax" title="含国别、地区号"/></td>
		<td nowrap="nowrap" align="right">E-mail：</td>
		<td><ext:field property="companyContact.email"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">QQ\MSN\微博：</td>
		<td><ext:field property="companyContact.im"/></td>
		<td nowrap="nowrap" align="right">所在部门及职务：</td>
		<td><ext:field property="companyContact.post"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">个人备注：</td>
		<td colspan="3"><ext:field property="companyContact.remark" title="特点、兴趣、任期等"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">商谈事项：</td>
		<td colspan="3"><ext:field property="companyContact.discuss"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">信息来源：</td>
		<td><ext:field property="companyContact.source"/></td>
		<td nowrap="nowrap" align="right">最初中方联系人：</td>
		<td><ext:field property="companyContact.chinaContact"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">收录时间：</td>
		<td><ext:field property="companyContact.created"/></td>
		<td nowrap="nowrap" align="right">录入者：</td>
		<td><ext:field property="companyContact.creator"/></td>
	</tr>
	<ext:notEmpty property="companyContact.discusses">
		<tr>
			<td nowrap="nowrap" align="right" valign="top">往来情况：</td>
			<td colspan="3">
				<ext:iterate id="discuss" indexId="discussIndex" property="companyContact.discusses" length="3">
					<a style="line-height: 16px;" href="javascript:DialogUtils.openDialog('<%=request.getContextPath()%>/fdi/customer/admin/discuss.shtml?id=<ext:write property="id"/>&discuss.id=<ext:write name="discuss" property="id"/>&openFromContact=true', 600, 300)">
						<ext:writeNumber name="discussIndex" plus="1"/>、<ext:write name="discuss" property="discussTime" format="yyyy-MM-dd"/>&nbsp;&nbsp;<ext:write name="discuss" property="discussContent" maxCharCount="88" ellipsis="..."/><br/>
					</a>
				</ext:iterate>
			</td>
		</tr>
	</ext:notEmpty>
</table>