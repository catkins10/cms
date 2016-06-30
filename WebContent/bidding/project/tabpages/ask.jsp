<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>

<table width="100%" border="0" cellpadding="3" cellspacing="0" bordercolor="#2D5C7A" bgcolor="" style="table-layout:fixed">
	<ext:iterate id="askFrom" property="askFroms">
		<ext:select id="ask" indexId="askIndex" isLastId="isLast" select="askFrom" property="askQuestions" nameValue="askFrom">
			<logic:equal value="0" name="askIndex">
				<tr>
					<td colspan="2"><b>针对<ext:write name="askFrom"/>的问题:</b></td>
				</tr>
			</logic:equal>
			<tr>
				<td colspan="2">
					<pre style="word-wrap:break-word"><ext:write name="ask" property="question"/></pre>
					<ext:notEmpty name="ask" property="reply">
						<br>
						<pre style="word-wrap:break-word"><b>答复:</b><ext:write name="ask" property="reply"/></pre>
					</ext:notEmpty>
				</td>
			</tr>
			<tr>
				<td style="width:100%" style="border-bottom:1 solid #ccccff">&nbsp;</td>
				<td nowrap="true" style="padding-right:50px; border-bottom:1 solid #ccccff" align="right"><br>
					<ext:write name="ask" property="enterpriseName"/>&nbsp;
					<ext:write name="ask" property="askTime" format="yyyy-M-d HH:mm"/>
				</td>
			</tr>
		</ext:select>
	</ext:iterate>
</table>