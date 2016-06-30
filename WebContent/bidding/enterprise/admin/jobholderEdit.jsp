<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:equal value="项目经理" property="jobholder.category">
	<table border="0" width="100%" cellspacing="0" cellpadding="2px" style="table-layout:fixed">
		<col width="100px">
		<col>
		<col width="80px">
		<col>
		<tr>
			<td align="right">姓名：</td>
			<td><ext:field property="jobholder.name"/></td>
			<td align="right">资质类型：</td>
			<td>
				<ext:equal value="其它" property="jobholder.category">
					<ext:field property="jobholder.category"/>
				</ext:equal>
				<ext:notEqual value="其它" property="jobholder.category">
					<ext:field property="jobholder.category" readonly="true"/>
				</ext:notEqual>
			</td>
		</tr>
		<tr>
			<td align="right">性别：</td>
			<td><ext:field property="jobholder.sex"/></td>
			<td align="right">身份证号码：</td>
			<td><ext:field property="jobholder.identityCard"/></td>
		</tr>
		<tr>
			<td align="right">培训证图片：</td>
			<td colspan="3"><ext:field property="jobholder.jobholderPicture"/></td>
		</tr>
		<tr>
			<td align="right">培训证号：</td>
			<td><ext:field property="jobholder.certificateNumber"/></td>
			<td align="right">资格等级：</td>
			<td><ext:field property="jobholder.qualification"/></td>
		</tr>
		<tr>
			<td align="right">发证时间：</td>
			<td><ext:field property="jobholder.certificateCreated"/></td>
			<td align="right">所在单位：</td>
			<td><ext:field property="jobholder.enterpriseName"/></td>
		</tr>
		<tr>
			<td align="right">学历：</td>
			<td><ext:field property="jobholder.education"/></td>
			<td align="right">职称：</td>
			<td><ext:field property="jobholder.job"/></td>
		</tr>
		<tr>
			<td align="right">毕业(培训)院校：</td>
			<td><ext:field property="jobholder.school"/></td>
			<td align="right">毕业专业：</td>
			<td><ext:field property="jobholder.schoolProfessional"/></td>
		</tr>
		<tr>
			<td align="right">专业：</td>
			<td colspan="3"><ext:field property="jobholder.professional"/></td>
		</tr>
		<tr>
			<td align="right">联系电话：</td>
			<td><ext:field property="jobholder.tel"/></td>
			<td align="right">手机：</td>
			<td><ext:field property="jobholder.mobile"/></td>
		</tr>
		<tr>
			<td align="right">通讯地址：</td>
			<td><ext:field property="jobholder.address"/></td>
			<td align="right">邮编：</td>
			<td><ext:field property="jobholder.postalCode"/></td>
		</tr>
		<tr>
			<td align="right">备注：</td>
			<td colspan="3"><ext:field property="jobholder.remark"/></td>
		</tr>
	</table>
</ext:equal>
<ext:equal value="注册监理师" property="jobholder.category">
	<table border="0" width="100%" cellspacing="0" cellpadding="2px" style="table-layout:fixed">
		<col width="100px">
		<col>
		<col width="80px">
		<col>
		<tr>
			<td align="right">资质类型：</td>
			<td>
				<ext:equal value="其它" property="jobholder.category">
					<ext:field property="jobholder.category"/>
				</ext:equal>
				<ext:notEqual value="其它" property="jobholder.category">
					<ext:field property="jobholder.category" readonly="true"/>
				</ext:notEqual>
			</td>
			<td align="right">等级：</td>
			<td><ext:field property="jobholder.qualification"/></td>
		</tr>
		<tr>
			<td align="right">注册证书图片：</td>
			<td colspan="3"><ext:field property="jobholder.jobholderPicture"/></td>
		</tr>
		<tr>
			<td align="right">注册证书号码：</td>
			<td><ext:field property="jobholder.certificateNumber"/></td>
			<td align="right">发证时间：</td>
			<td><ext:field property="jobholder.certificateCreated"/></td>
		</tr>
		<tr>
			<td align="right">姓名：</td>
			<td><ext:field property="jobholder.name"/></td>
			<td align="right">性别：</td>
			<td><ext:field property="jobholder.sex"/></td>
		</tr><tr>
			<td align="right">身份证号码：</td>
			<td><ext:field property="jobholder.identityCard"/></td>
			<td align="right">所在单位：</td>
			<td><ext:field property="jobholder.enterpriseName"/></td>
		</tr>
		<tr>
			<td align="right">职务：</td>
			<td><ext:field property="jobholder.duty"/></td>
			<td align="right">学历：</td>
			<td><ext:field property="jobholder.education"/></td>
		</tr>
		<tr>
			<td align="right">毕业(培训)院校：</td>
			<td><ext:field property="jobholder.school"/></td>
			<td align="right">第一专业：</td>
			<td><ext:field property="jobholder.schoolProfessional"/></td>
		</tr>
		<tr>
			<td align="right">第二专业：</td>
			<td><ext:field property="jobholder.secondProfessional"/></td>
			<td align="right">其它专业：</td>
			<td><ext:field property="jobholder.otherProfessional"/></td>
		</tr>
		<tr>
			<td align="right">职称：</td>
			<td><ext:field property="jobholder.job"/></td>
			<td align="right">联系电话：</td>
			<td><ext:field property="jobholder.tel"/></td>
		</tr>
		<tr>
			<td align="right">手机：</td>
			<td><ext:field property="jobholder.mobile"/></td>
			<td align="right">通讯地址：</td>
			<td><ext:field property="jobholder.address"/></td>
		</tr>
		<tr>
			<td align="right">邮编：</td>
			<td><ext:field property="jobholder.postalCode"/></td>
			<td align="right">备注：</td>
			<td><ext:field property="jobholder.remark"/></td>
		</tr>
	</table>
</ext:equal>
<ext:equal value="注册建造师" property="jobholder.category">
	<table border="0" width="100%" cellspacing="0" cellpadding="2px" style="table-layout:fixed">
		<col width="100px">
		<col>
		<col width="80px">
		<col>
		<tr>
			<td width="100px" align="right">资质类型：</td>
			<td>
				<ext:equal value="其它" property="jobholder.category">
					<ext:field property="jobholder.category"/>
				</ext:equal>
				<ext:notEqual value="其它" property="jobholder.category">
					<ext:field property="jobholder.category" readonly="true"/>
				</ext:notEqual>
			</td>
			<td align="right">资格等级：</td>
			<td><ext:field property="jobholder.qualification"/></td>
		</tr>
		<tr>
			<td align="right">注册证书图片：</td>
			<td colspan="3"><ext:field property="jobholder.jobholderPicture"/></td>
		</tr>
		<tr>
			<td align="right">注册证书号码：</td>
			<td><ext:field property="jobholder.certificateNumber"/></td>
			<td align="right">发证时间：</td>
			<td><ext:field property="jobholder.certificateCreated"/></td>
		</tr>
		<tr>
			<td align="right">姓名：</td>
			<td><ext:field property="jobholder.name"/></td>
			<td align="right">性别：</td>
			<td><ext:field property="jobholder.sex"/></td>
		</tr><tr>
			<td align="right">身份证号码：</td>
			<td><ext:field property="jobholder.identityCard"/></td>
			<td align="right">所在单位：</td>
			<td><ext:field property="jobholder.enterpriseName"/></td>
		</tr>
		<tr>
			<td align="right">职务：</td>
			<td><ext:field property="jobholder.duty"/></td>
			<td align="right">学历：</td>
			<td><ext:field property="jobholder.education"/></td>
		</tr>
		<tr>
			<td align="right">毕业(培训)院校：</td>
			<td><ext:field property="jobholder.school"/></td>
			<td align="right">第一专业：</td>
			<td><ext:field property="jobholder.schoolProfessional"/></td>
		</tr>
		<tr>
			<td align="right">第二专业：</td>
			<td><ext:field property="jobholder.secondProfessional"/></td>
			<td align="right">其它专业：</td>
			<td><ext:field property="jobholder.otherProfessional"/></td>
		</tr>
		<tr>
			<td align="right">职称：</td>
			<td><ext:field property="jobholder.job"/></td>
			<td align="right">联系电话：</td>
			<td><ext:field property="jobholder.tel"/></td>
		</tr>
		<tr>
			<td align="right">手机：</td>
			<td><ext:field property="jobholder.mobile"/></td>
			<td align="right">通讯地址：</td>
			<td><ext:field property="jobholder.address"/></td>
		</tr>
		<tr>
			<td align="right">邮编：</td>
			<td><ext:field property="jobholder.postalCode"/></td>
			<td align="right">备注：</td>
			<td><ext:field property="jobholder.remark"/></td>
		</tr>
	</table>
</ext:equal>
<ext:equal value="注册造价师" property="jobholder.category">
	<table border="0" width="100%" cellspacing="0" cellpadding="2px" style="table-layout:fixed">
		<col width="100px">
		<col>
		<col width="80px">
		<col>
		<tr>
			<td width="100px" align="right">资质类型：</td>
			<td colspan="3">
				<ext:equal value="其它" property="jobholder.category">
					<ext:field property="jobholder.category"/>
				</ext:equal>
				<ext:notEqual value="其它" property="jobholder.category">
					<ext:field property="jobholder.category" readonly="true"/>
				</ext:notEqual>
			</td>
		</tr>
		<tr>
			<td align="right">注册证书图片：</td>
			<td colspan="3"><ext:field property="jobholder.jobholderPicture"/></td>
		</tr>
		<tr>
			<td align="right">注册证书号码：</td>
			<td><ext:field property="jobholder.certificateNumber"/></td>
			<td align="right">发证时间：</td>
			<td><ext:field property="jobholder.certificateCreated"/></td>
		</tr>
		<tr>
			<td align="right">姓名：</td>
			<td><ext:field property="jobholder.name"/></td>
			<td align="right">性别：</td>
			<td><ext:field property="jobholder.sex"/></td>
		</tr>
		<tr>
			<td align="right">身份证号码：</td>
			<td><ext:field property="jobholder.identityCard"/></td>
			<td align="right">所在单位：</td>
			<td><ext:field property="jobholder.enterpriseName"/></td>
		</tr>
		<tr>
			<td align="right">职务：</td>
			<td><ext:field property="jobholder.duty"/></td>
			<td align="right">学历：</td>
			<td><ext:field property="jobholder.education"/></td>
		</tr>
		<tr>
			<td align="right">毕业(培训)院校：</td>
			<td><ext:field property="jobholder.school"/></td>
			<td align="right">第一专业：</td>
			<td><ext:field property="jobholder.schoolProfessional"/></td>
		</tr>
		<tr>
			<td align="right">第二专业：</td>
			<td><ext:field property="jobholder.secondProfessional"/></td>
			<td align="right">其它专业：</td>
			<td><ext:field property="jobholder.otherProfessional"/></td>
		</tr>
		<tr>
			<td align="right">职称：</td>
			<td><ext:field property="jobholder.job"/></td>
			<td align="right">联系电话：</td>
			<td><ext:field property="jobholder.tel"/></td>
		</tr>
		<tr>
			<td align="right">手机：</td>
			<td><ext:field property="jobholder.mobile"/></td>
			<td align="right">通讯地址：</td>
			<td><ext:field property="jobholder.address"/></td>
		</tr>
		<tr>
			<td align="right">邮编：</td>
			<td><ext:field property="jobholder.postalCode"/></td>
			<td align="right">备注：</td>
			<td><ext:field property="jobholder.remark"/></td>
		</tr>
	</table>
</ext:equal>
<ext:equal value="五大员" property="jobholder.category">
	<table border="0" width="100%" cellspacing="0" cellpadding="2px" style="table-layout:fixed">
		<col width="100px">
		<col>
		<col width="80px">
		<col>
		<tr>
			<td align="right">姓名：</td>
			<td><ext:field property="jobholder.name"/></td>
			<td align="right">资质类型：</td>
			<td>
				<ext:equal value="其它" property="jobholder.category">
					<ext:field property="jobholder.category"/>
				</ext:equal>
				<ext:notEqual value="其它" property="jobholder.category">
					<ext:field property="jobholder.category" readonly="true"/>
				</ext:notEqual>
			</td>
		</tr>
		<tr>
			<td align="right">性别：</td>
			<td><ext:field property="jobholder.sex"/></td>
			<td align="right">身份证号码：</td>
			<td><ext:field property="jobholder.identityCard"/></td>
		</tr>
		<tr>
			<td align="right">证书图片：</td>
			<td colspan="3"><ext:field property="jobholder.jobholderPicture"/></td>
		</tr>
		<tr>
			<td align="right">工作岗位：</td>
			<td><ext:field property="jobholder.qualification"/></td>
			<td align="right">岗位编号：</td>
			<td><ext:field property="jobholder.certificateNumber"/></td>
		</tr>
		<tr>
			<td align="right">发证时间：</td>
			<td><ext:field property="jobholder.certificateCreated"/></td>
			<td align="right">所在单位：</td>
			<td><ext:field property="jobholder.enterpriseName"/></td>
		</tr>
		<tr>
			<td align="right">学历：</td>
			<td><ext:field property="jobholder.education"/></td>
			<td align="right">职称：</td>
			<td><ext:field property="jobholder.job"/></td>
		</tr>
		<tr>
			<td align="right">毕业(培训)院校：</td>
			<td><ext:field property="jobholder.school"/></td>
			<td align="right">毕业专业：</td>
			<td><ext:field property="jobholder.schoolProfessional"/></td>
		</tr>
		<tr>
			<td align="right">专业：</td>
			<td colspan="3"><ext:field property="jobholder.professional"/></td>
		</tr>
		<tr>
			<td align="right">联系电话：</td>
			<td><ext:field property="jobholder.tel"/></td>
			<td align="right">手机：</td>
			<td><ext:field property="jobholder.mobile"/></td>
		</tr>
		<tr>
			<td align="right">通讯地址：</td>
			<td><ext:field property="jobholder.address"/></td>
			<td align="right">邮编：</td>
			<td><ext:field property="jobholder.postalCode"/></td>
		</tr>
		<tr>
			<td align="right">备注：</td>
			<td colspan="3"><ext:field property="jobholder.remark"/></td>
		</tr>
	</table>
</ext:equal>