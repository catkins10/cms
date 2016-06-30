<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:equal value="create" property="act">
	<ext:page applicationName="logistics/complaint" pageName="complaint"/>
</ext:equal>
<ext:notEqual value="create" property="act">
	<ext:equal value="1" property="publicPass">
		<ext:equal value="1" property="publicWorkflow">
			<ext:page applicationName="logistics/complaint" pageName="fullyComplaint"/>
		</ext:equal>
		<ext:notEqual value="1" property="publicWorkflow">
			<ext:equal value="1" property="publicBody">
				<ext:page applicationName="logistics/complaint" pageName="originalComplaint"/>
			</ext:equal>
			<ext:notEqual value="1" property="publicBody">
				<ext:page applicationName="logistics/complaint" pageName="poorComplaint"/>
			</ext:notEqual>
		</ext:notEqual>
	</ext:equal>
	<ext:notEqual value="1" property="publicPass">
		<ext:page applicationName="logistics/complaint" pageName="complaintFailed"/>
	</ext:notEqual>
</ext:notEqual>