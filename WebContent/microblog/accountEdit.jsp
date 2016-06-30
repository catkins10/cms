<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent"><ext:field property="unitName"/></td>
	</tr>
	<ext:empty property="platform">
		<tr>
			<td class="tdtitle" nowrap>微博平台</td>
			<td class="tdcontent"><ext:field property="platform" onclick="FormUtils.doAction('account')"/></td>
		</tr>
	</ext:empty>
	<ext:notEmpty property="platform">
		<tr>
			<td class="tdtitle" nowrap>微博平台</td>
			<td class="tdcontent">
				<html:hidden property="platform"/>
				<ext:field writeonly="true" property="platform"/>
			</td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">网址</td>
			<td class="tdcontent"><ext:field property="siteUrl"/></td>
		</tr>
		<tr style="display:none;">
			<td class="tdtitle" nowrap="nowrap">消息接收URL</td>
			<td class="tdcontent"><ext:field property="receiveMessageURL"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">微博名称</td>
			<td class="tdcontent"><ext:field property="name"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">微博帐号</td>
			<td class="tdcontent"><ext:field property="userName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">微博密码</td>
			<td class="tdcontent"><ext:field property="password"/></td>
		</tr>
		<ext:iterate id="parameter" property="parameters">
			<tr>
				<td class="tdtitle" nowrap><ext:write name="parameter" property="parameterDefine.label"/></td>
				<td class="tdcontent"><ext:field name="parameter" property="parameterValue" title="<%=((com.yuanluesoft.microblog.pojo.MicroblogAccountParameter)pageContext.getAttribute("parameter")).getParameterDefine().getRemark()%>"/></td>
			</tr>
		</ext:iterate>
	</ext:notEmpty>
</table>