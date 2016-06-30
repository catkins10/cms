<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0" bordercolor="black">
	<tr>
		<ext:notEmpty property="opinions">
			<td valign="top" width="50%" style="border-right:0 solid black">
				<table id="opinionList" width="100%" border="0" cellpadding="3" cellspacing="0" style="table-layout:fixed">
					<ext:iterate id="currentOpinion" property="opinions">
						<ext:notEmpty name="currentOpinion" property="opinionType">
				    		<tr>
				   				<td><ext:write name="currentOpinion" property="opinionType"/>意见：</td>
				    		</tr>
				    	</ext:notEmpty>
						<tr>
							<td><pre style="word-wrap:break-word"><ext:write name="currentOpinion" property="opinion"/></pre></td>
						</tr>
						<tr>
				        	<td class="line" align="right">
				        		<ext:write name="currentOpinion" property="personName"/>(<ext:write name="currentOpinion" property="agentName"/>录入)&nbsp;<ext:write name="currentOpinion" property="created" format="yyyy-MM-dd"/>
				        	</td>
						</tr>
						<tr>
				   			<td style="height:8px"></td>
				   		</tr>
				  </ext:iterate>
				</table>
			</td>
		</ext:notEmpty>
	</tr>
</table>