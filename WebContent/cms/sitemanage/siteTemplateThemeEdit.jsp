<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="5px">
	<col align="right">
	<col width="100%">
	<ext:equal value="create" property="act">
		<tr>
			<td nowrap="nowrap">主题所属站点：</td>
			<td><ext:field property="siteName"/></td>
		</tr>
	</ext:equal>
	<ext:notEqual value="create" property="act">
		<tr>
			<td nowrap="nowrap">主题所属站点：</td>
			<td><ext:field property="siteName" writeonly="true"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td nowrap="nowrap">主题名称：</td>
		<td><ext:field property="name"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">浏览器类型：</td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<ext:equal value="create" property="act">
						<td width="200px">	
							<ext:field property="type" onchange="document.getElementById('smartPhoneProperties').style.display=(',1,3,'.indexOf(',' + document.getElementsByName('type')[0].value + ',')!=-1 ? '' : 'none')"/>
						</td>
					</ext:equal>
					<ext:notEqual value="create" property="act">
						<td>	
							<ext:field property="typeAsText"/>
						</td>
					</ext:notEqual>
					<td id="smartPhoneProperties" style="<ext:notEqual value="1" property="type"><ext:notEqual value="3" property="type">display:none;</ext:notEqual></ext:notEqual>">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td nowrap="nowrap" valign="middle">&nbsp;页面宽度：</td>
								<td style="width:50px"><ext:field property="pageWidth"/></td>
								<td nowrap="nowrap">&nbsp;<ext:field property="flashSupport"/></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap">当前站点：</td>
		<td><ext:field property="pageSiteName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">是否默认主题：</td>
		<td><ext:equal value="true" property="defaultTheme">是</ext:equal></td>
	</tr>
	<tr>
		<td nowrap="nowrap">最后修改时间：</td>
		<td><ext:field property="lastModified"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">最后修改人：</td>
		<td><ext:field property="lastModifier"/></td>
	</tr>
</table>