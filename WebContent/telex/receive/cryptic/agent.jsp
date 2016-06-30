<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveAgent">
	<jsp:include page="/jeaf/fingerprint/enroll.jsp" />
	<table border="0" cellpadding="2" cellspacing="0" align="center" width="430px">
		<col width="70px" align="right">
		<col width="50%">
		<col width="40px" align="right">
		<col width="50%">
		<tr height="30px">
			<td nowrap><b>姓　　名:</b></td>
			<td><ext:field property="agentName"/></td>
			<td nowrap><b>性别:</b></td>
			<td><ext:field property="agentSex"/></td>
		</tr>
		<tr height="30px">
			<td nowrap><b>所在单位:</b></td>
			<td colspan="3"><ext:field property="agentOrgName"/></td>
		</tr>
		<tr height="30px">
			<td nowrap><b>证件名称:</b></td>
			<td><ext:field property="agentCertificateName"/></td>
			<td nowrap><b>号码:</b></td>
			<td><ext:field property="agentCertificateCode"/></td>
		</tr>
		<tr height="80px">
			<td nowrap><b>指纹采集:</b></td>
			<td colspan="3">
				<span id="feature1" style="border:1 solid gray; width:60; height:60; padding:1px; background-color:#ffffff">&nbsp;</span>&nbsp;
				<span id="feature2" style="border:1 solid gray; width:60; height:60; padding:1px; background-color:#ffffff">&nbsp;</span>&nbsp;
				<span id="feature3" style="border:1 solid gray; width:60; height:60; padding:1px; background-color:#ffffff">&nbsp;</span>&nbsp;
				<span id="feature4" style="border:1 solid gray; width:60; height:60; padding:1px; background-color:#ffffff">&nbsp;</span>&nbsp;
			</td>
		</tr>
	</table>
	<script>
		function formOnSubmit() { //提交校验样本数据
			if(document.getElementsByName("agentName")[0].value=="") {
				alert("代理人姓名不能为空");
				return false;
			}
			if(document.getElementsByName("agentOrgName")[0].value=="") {
				alert("代理人所在单位不能为空");
				return false;
			}
			if(document.getElementsByName("template")[0].value=="") {
				alert("指纹采集尚未完成");
				return false;
			}
			return true;
		}
	</script>
</ext:form>