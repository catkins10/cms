<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col width="60px">
			<col width="50%">
			<col valign="middle" width="60px">
			<col valign="middle" width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">姓名</td>
				<td class="tdcontent"><ext:field property="name"/></td>
				<td class="tdtitle" nowrap="nowrap">分类</td>
				<td class="tdcontent"><ext:field property="category"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">电子邮件</td>
				<td class="tdcontent"><ext:field property="email"/></td>
				<td class="tdtitle" nowrap="nowrap">手机</td>
				<td class="tdcontent"><ext:field property="mobile"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">电话</td>
				<td class="tdcontent"><ext:field property="companyTel"/></td>
				<td class="tdtitle" nowrap="nowrap">住宅电话</td>
				<td class="tdcontent"><ext:field property="homeTel"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">QQ</td>
				<td class="tdcontent"><ext:field property="qq"/></td>
				<td class="tdtitle" nowrap="nowrap">MSN</td>
				<td class="tdcontent"><ext:field property="msn"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">主页</td>
				<td class="tdcontent"><ext:field property="homepage"/></td>
				<td class="tdtitle" nowrap="nowrap">生日</td>
				<td class="tdcontent"><ext:field property="birthday"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">备注</td>
				<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
			</tr>
		</table>
	</ext:tabBody>
	
	<ext:tabBody tabId="company">
		<table valign="middle" width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
			<col width="60px">
			<col width="50%">
			<col valign="middle" width="60px">
			<col valign="middle" width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">单位名称</td>
				<td class="tdcontent" colspan="3"><ext:field property="companyName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">单位地址</td>
				<td class="tdcontent" colspan="3"><ext:field property="companyAddress"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">邮政编码</td>
				<td class="tdcontent"><ext:field property="companyPostalcode"/></td>
				<td class="tdtitle" nowrap="nowrap">传真</td>
				<td class="tdcontent"><ext:field property="fax"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在部门</td>
				<td class="tdcontent"><ext:field property="department"/></td>
				<td class="tdtitle" nowrap="nowrap">职务</td>
				<td class="tdcontent"><ext:field property="job"/></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<ext:tabBody tabId="home">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">地址</td>
				<td class="tdcontent" colspan="3"><ext:field property="homeAddress"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">邮政编码</td>
				<td class="tdcontent" colspan="3"><ext:field property="homePostalcode"/></td>
			</tr>
		</table>
	</ext:tabBody>
	
	<ext:tabBody tabId="log">
		<table width="100%" style="table-layout:fixed" border="0" cellpadding="3" cellspacing="0"  bgcolor="">
			<tr>
				<ext:notEmpty property="logs">
					<td valign="top" width="50%">
						<table width="100%" border="0" cellpadding="3" cellspacing="0">
							<ext:iterate id="log" property="logs">
								<tr><td style="padding-bottom:10px">
									<ext:notEmpty name="log" property="time"><ext:write name="log" property="time"/>:&nbsp;</ext:notEmpty><ext:write name="log" property="content"/>
									&nbsp;
									<a style="cursor:pointer;" onclick="FormUtils.doAction('loadLog', 'logId=<ext:write name="log" property="id"/>', false, null, '_self')">修改</a>
									<a style="cursor:pointer;" onclick="if(confirm('删除后不可恢复，是否确定删除？'))FormUtils.doAction('deleteLog', 'logId=<ext:write name="log" property="id"/>')">删除</a>
								</td></tr>
							</ext:iterate>
						</table>
					</td>
				</ext:notEmpty>
				<td valign="top">
					<table width="100%" border="0" cellpadding="3" cellspacing="0" style="table-layout:fixed">
						<tr>
							<td width="36px">时间:</td>
							<td width="100%"><ext:field property="logTime"/></td>
						</tr>
						<tr>
							<td valign="top">描述:</td>
							<td><ext:field property="logContent"/></td>
						</tr>
						<tr>
							<td colspan="2" align="right" style="padding-right:20px">
								<input onclick="FormUtils.doAction('saveLog')" type="button" style="width:50px" class="button" value="提 交">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</ext:tabBody>
</ext:tab>