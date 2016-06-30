<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="50%">
			<col>
			<col valign="middle" width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">姓名</td>
				<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
				<td class="tdtitle" nowrap="nowrap">分类</td>
				<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">电子邮件</td>
				<td class="tdcontent"><ext:field writeonly="true" property="email"/></td>
				<td class="tdtitle" nowrap="nowrap">手机</td>
				<td class="tdcontent"><ext:field writeonly="true" property="mobile"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">电话</td>
				<td class="tdcontent"><ext:field writeonly="true" property="companyTel"/></td>
				<td class="tdtitle" nowrap="nowrap">住宅电话</td>
				<td class="tdcontent"><ext:field writeonly="true" property="homeTel"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">QQ</td>
				<td class="tdcontent"><ext:field writeonly="true" property="qq"/></td>
				<td class="tdtitle" nowrap="nowrap">MSN</td>
				<td class="tdcontent"><ext:field writeonly="true" property="msn"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">主页</td>
				<td class="tdcontent"><ext:field writeonly="true" property="homepage"/></td>
				<td class="tdtitle" nowrap="nowrap">生日</td>
				<td class="tdcontent"><ext:field writeonly="true" property="birthday"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">备注</td>
				<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="remark"/></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<ext:tabBody tabId="company">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="50%">
			<col>
			<col valign="middle" width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">单位名称</td>
				<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="companyName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">单位地址</td>
				<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="companyAddress"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">邮政编码</td>
				<td class="tdcontent"><ext:field writeonly="true" property="companyPostalcode"/></td>
				<td class="tdtitle" nowrap="nowrap">传真</td>
				<td class="tdcontent"><ext:field writeonly="true" property="fax"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在部门</td>
				<td class="tdcontent"><ext:field writeonly="true" property="department"/></td>
				<td class="tdtitle" nowrap="nowrap">职务</td>
				<td class="tdcontent"><ext:field writeonly="true" property="job"/></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<ext:tabBody tabId="home">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">地址</td>
				<td class="tdcontent"><ext:field writeonly="true" property="homeAddress"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">邮政编码</td>
				<td class="tdcontent"><ext:field writeonly="true" property="homePostalcode"/></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<ext:tabBody tabId="log">
		<table width="100%" style="table-layout:fixed" border="0" cellpadding="3" cellspacing="0"  bgcolor="">
			<ext:iterate id="log" property="logs">
				<tr><td style="padding-bottom:10px">
					<ext:notEmpty name="log" property="time"><ext:write name="log" property="time"/>:&nbsp;</ext:notEmpty><ext:write name="log" property="content"/>
				</td></tr>
			</ext:iterate>
		</table>
	</ext:tabBody>
</ext:tab>