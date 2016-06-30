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
			<td class="tdtitle" nowrap>公司名称</td>
			<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
			<td class="tdtitle" nowrap>所在地区</td>
			<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap>地址</td>
			<td class="tdcontent"><ext:field writeonly="true" property="address"/></td>
			<td class="tdtitle" nowrap>电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="tel"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap>传真</td>
			<td class="tdcontent"><ext:field writeonly="true" property="fax"/></td>
			<td class="tdtitle" nowrap>公司网址</td>
			<td class="tdcontent"><ext:field writeonly="true" property="webSite"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap>营业执照号码</td>
			<td class="tdcontent"><ext:field writeonly="true" property="businessLicence"/></td>
			<td class="tdtitle" nowrap>法人代表</td>
			<td class="tdcontent"><ext:field writeonly="true" property="legalRepresentative"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap>法人代表身份证号码</td>
			<td class="tdcontent"><ext:field writeonly="true" property="representativeIdNumber"/></td>
			<td class="tdtitle" nowrap>法人代表联系电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="representativeTel"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap>联系人</td>
			<td class="tdcontent"><ext:field writeonly="true" property="linkman"/></td>
			<td class="tdtitle" nowrap>联系电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="linkmanTel"/></td>
		</tr>
	</ext:equal>
	<ext:notEqual value="1" property="isCompany">
		<tr>
			<td class="tdtitle" nowrap>姓名</td>
			<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
			<td class="tdtitle" nowrap>所在地区</td>
			<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap>地址</td>
			<td class="tdcontent"><ext:field writeonly="true" property="address"/></td>
			<td class="tdtitle" nowrap>联系电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="linkmanTel"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td class="tdtitle" nowrap>联系QQ</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkmanQQ"/></td>
		<td class="tdtitle" nowrap>联系邮箱</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkmanMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap>注册人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap>注册时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否停用</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isHalt"/></td>
		<td class="tdtitle" nowrap>备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>