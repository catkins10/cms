<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveEnrollTemplate">
	<jsp:include page="/jeaf/fingerprint/enroll.jsp" />
	<script>
		function afterEnrollComplete() { //指纹采集完成
			saveTempdateData(false);
		}
		function saveTempdateData(alertEnabled) {
			//检查用户名是否为空
			if(document.getElementsByName("personName")[0] && document.getElementsByName("personName")[0].value=="") {
				if(alertEnabled) {
					alert("用户名不能为空");
				}
				return false;
			}
			if(document.getElementsByName("template")[0].value=="") {
				if(alertEnabled) {
					alert("指纹采集尚未完成");
				}
				return false;
			}
			FormUtils.submitForm();
		}
	</script>
	<table border="0" cellpadding="0" cellspacing="0" align="center" width="100%">
		<tr style="padding-top:15px; padding-bottom:15px; padding-left:10px; padding-right:10px;">
			<td nowrap align="center">
				<img width="180px" height="200px" src="<%=request.getContextPath()%>/jeaf/fingerprint/images/fingerprint.jpg">
			</td>
			<td valign="top" style="padding-top:20px">
				<table border="0" cellpadding="2" cellspacing="0" align="center">
					<tr height="30px">
						<td nowrap align="right"><b>用户名:</b></td>
						<td>
							<ext:equal value="true" property="selfEnroll">
								<ext:field property="personName" writeonly="true"/>
							</ext:equal>
							<ext:equal value="false" property="selfEnroll">
								<ext:field property="personName"/>
							</ext:equal>
						</td>
					</tr>
					<tr height="30px">
						<td nowrap align="right">&nbsp;<b>手　指:</b></td>
						<td>
							<ext:field property="finger"/>
						</td>
					</tr>
					<tr height="80px">
						<td nowrap align="right"><b>指　纹:</b></td>
						<td>
							<span id="feature1" style="border:1 solid gray; width:60; height:60; padding:1px; background-color:#ffffff">&nbsp;</span>&nbsp;
							<span id="feature2" style="border:1 solid gray; width:60; height:60; padding:1px; background-color:#ffffff">&nbsp;</span>&nbsp;
							<span id="feature3" style="border:1 solid gray; width:60; height:60; padding:1px; background-color:#ffffff">&nbsp;</span>&nbsp;
							<span id="feature4" style="border:1 solid gray; width:60; height:60; padding:1px; background-color:#ffffff">&nbsp;</span>&nbsp;
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</ext:form>