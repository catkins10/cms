<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<ext:equal value="1" property="isCompany">
		<tr>
			<td class="tdtitle" nowrap="nowrap">公司名称</td>
			<td class="tdcontent"><ext:field property="name"/></td>
			<td class="tdtitle" nowrap="nowrap">所在地区</td>
			<td class="tdcontent"><ext:field property="area"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">地址</td>
			<td class="tdcontent"><ext:field property="address"/></td>
			<td class="tdtitle" nowrap="nowrap">电话</td>
			<td class="tdcontent"><ext:field property="tel"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">传真</td>
			<td class="tdcontent"><ext:field property="fax"/></td>
			<td class="tdtitle" nowrap="nowrap">公司网址</td>
			<td class="tdcontent"><ext:field property="webSite"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap>营业执照号码</td>
			<td class="tdcontent"><ext:field property="businessLicence"/></td>
			<td class="tdtitle" nowrap="nowrap">法人代表</td>
			<td class="tdcontent"><ext:field property="legalRepresentative"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap>法人代表身份证号码</td>
			<td class="tdcontent"><ext:field property="representativeIdNumber"/></td>
			<td class="tdtitle" nowrap>法人代表联系电话</td>
			<td class="tdcontent"><ext:field property="representativeTel"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联系人</td>
			<td class="tdcontent"><ext:field property="linkman"/></td>
			<td class="tdtitle" nowrap="nowrap">联系电话</td>
			<td class="tdcontent"><ext:field property="linkmanTel"/></td>
		</tr>
	</ext:equal>
	<ext:notEqual value="1" property="isCompany">
		<tr>
			<td class="tdtitle" nowrap="nowrap">姓名</td>
			<td class="tdcontent"><ext:field property="name"/></td>
			<td class="tdtitle" nowrap="nowrap">所在地区</td>
			<td class="tdcontent"><ext:field property="area"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">地址</td>
			<td class="tdcontent"><ext:field property="address"/></td>
			<td class="tdtitle" nowrap="nowrap">联系电话</td>
			<td class="tdcontent"><ext:field property="linkmanTel"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系QQ</td>
		<td class="tdcontent"><ext:field property="linkmanQQ"/></td>
		<td class="tdtitle" nowrap="nowrap">联系邮箱</td>
		<td class="tdcontent"><ext:field property="linkmanMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>登录用户名</td>
		<td class="tdcontent"><ext:field property="loginName"/></td>
		<td class="tdtitle" nowrap="nowrap">登录密码</td>
		<td class="tdcontent"><ext:field property="password"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap>注册时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否停用</td>
		<td class="tdcontent"><ext:field property="isHalt"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>