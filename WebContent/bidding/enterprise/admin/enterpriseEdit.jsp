<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent" colspan="3"><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">所在地区</td>
		<td class="tdcontent"><ext:field property="area"/></td>
	</tr>
<%	if(request.getAttribute("registUser")!=null) { %>
		<ext:equal property="act" value="create">
			<tr>
				<td class="tdtitle" nowrap="nowrap">登录用户名</td>
				<td class="tdcontent" colspan="3"><ext:field property="loginName"/></td>
				<td class="tdtitle" nowrap="nowrap">密码</td>
				<td class="tdcontent"><ext:field property="password"/></td>
			</tr>
		</ext:equal>
<%	} %>
	<tr>
		<td class="tdtitle" nowrap="nowrap">营业执照图片</td>
		<td class="tdcontent" colspan="3"><ext:field property="businessLicencePicture"/></td>
		<td class="tdtitle" nowrap="nowrap">营业执照号码</td>
		<td class="tdcontent"><ext:field property="businessLicence"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册时间</td>
		<td class="tdcontent"><ext:field property="registDate"/></td>
		<td class="tdtitle" nowrap="nowrap">注册资金(万元)</td>
		<td class="tdcontent"><ext:field property="registeredCapital"/></td>
		<td class="tdtitle" nowrap="nowrap">企业性质</td>
		<td class="tdcontent"><ext:field property="kind"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表身份证</td>
		<td class="tdcontent" colspan="3"><ext:field property="legalRepresentativePicture"/></td>
		<td class="tdtitle" nowrap="nowrap">法人代表姓名</td>
		<td class="tdcontent"><ext:field property="legalRepresentative"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表身份证号码</td>
		<td class="tdcontent" colspan="3"><ext:field property="representativeIdNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="representativeTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位统计证</td>
		<td class="tdcontent" colspan="3"><ext:field property="statisticsLicence"/></td>
		<td class="tdtitle" nowrap="nowrap">安全许可证</td>
		<td class="tdcontent"><ext:field property="safeLicence"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业经理</td>
		<td class="tdcontent"><ext:field property="manager"/></td>
		<td class="tdtitle" nowrap="nowrap">身份证号码</td>
		<td class="tdcontent"><ext:field property="managerIdNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="managerTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">技术负责人</td>
		<td class="tdcontent"><ext:field property="technicalLeader"/></td>
		<td class="tdtitle" nowrap="nowrap">身份证号码</td>
		<td class="tdcontent"><ext:field property="technicalLeaderIdNumner"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="technicalLeaderTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">通讯地址</td>
		<td class="tdcontent" colspan="3"><ext:field property="address"/></td>
		<td class="tdtitle" nowrap="nowrap">邮政编码</td>
		<td class="tdcontent"><ext:field property="postalcode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">身份证号码</td>
		<td class="tdcontent"><ext:field property="linkmanIdNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人手机</td>
		<td class="tdcontent"><ext:field property="mobile"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="fax"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮件</td>
		<td class="tdcontent"><ext:field property="email"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业主页</td>
		<td class="tdcontent" colspan="5"><ext:field property="website"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">业务范围</td>
		<td class="tdcontent" colspan="5"><ext:field property="introduction"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开户银行</td>
		<td class="tdcontent"><ext:field property="bank"/></td>
		<td class="tdtitle" nowrap="nowrap">开户帐号</td>
		<td class="tdcontent"><ext:field property="account"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="5"><ext:field property="remark"/></td>
	</tr>
	<ext:notEmpty property="alterDescription">
		<tr>
			<td class="tdtitle" valign="top">变更情况</td>
			<td class="tdcontent" colspan="5" style="color:red"><pre><ext:field property="alterDescription"/></pre></td>
		</tr>
	</ext:notEmpty>
</table>