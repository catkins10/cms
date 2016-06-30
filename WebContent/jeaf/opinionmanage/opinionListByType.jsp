<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:empty property="opinionPackage.opinionTypes">
	<jsp:include page="opinionListByTime.jsp" />
</ext:empty>
<ext:notEmpty property="opinionPackage.opinionTypes">
	<table id="opinionList" width="100%" border="0" cellpadding="3" cellspacing="0" bordercolor="#2D5C7A" bgcolor="" style="table-layout:fixed">
	  <ext:iterate id="type" property="opinionPackage.opinionTypes">
	    <ext:select id="currentOpinion" indexId="opinionIndex" isLastId="isLast" select="opinionType" property="opinionPackage.opinionList" nameValue="type">
	      <logic:equal value="0" name="opinionIndex">
	        <tr ondblclick="modifyOpinion('<ext:write name="currentOpinion" property="id"/>')">
	          <td colspan="2">
	            <ext:write name="type"/>意见:</td>
	        </tr>
	      </logic:equal>
	      <tr ondblclick="modifyOpinion('<ext:write name="currentOpinion" property="id"/>')">
	        <td colspan="2"><pre style="word-wrap:break-word">&nbsp;&nbsp;&nbsp;&nbsp;<ext:write name="currentOpinion" property="opinion"/></pre></td>
	      </tr>
	      <tr ondblclick="modifyOpinion('<ext:write name="currentOpinion" property="id"/>')">
	        <td style="width:100%" style="<logic:equal value="true" name="isLast">border-bottom:1 solid #ccccff</logic:equal>">&nbsp;
	        </td>
	        <td nowrap="true" style="padding-right: 20px;<logic:equal value="true" name="isLast">border-bottom:1 solid #ccccff;</logic:equal>" align="right"><br><ext:write name="currentOpinion" property="personName"/><ext:notEmpty name="currentOpinion" property="agentName"><ext:notEqual name="currentOpinion" property="agentName" nameCompare="currentOpinion" propertyCompare="personName">(<ext:write name="currentOpinion" property="agentName"/>代)</ext:notEqual></ext:notEmpty>&nbsp;<ext:write name="currentOpinion" property="created" format="yyyy-M-d"/>
	        </td>
	      </tr>
	    </ext:select>
	    <tr style="display:none" ondblclick="modifyOpinion('<ext:write name="currentOpinion" property="id"/>')">
	      <td colspan="2"><pre id="<ext:write name="type"/>"><ext:select id="currentOpinion" indexId="opinionIndex" isLastId="isLast" select="opinionType" property="opinionPackage.opinionList" nameValue="type"><ext:write name="currentOpinion" property="opinion"/>
			 <ext:write name="currentOpinion" property="personName"/><ext:notEmpty name="currentOpinion" property="agentName"><ext:notEqual name="currentOpinion" property="agentName" nameCompare="currentOpinion" propertyCompare="personName">(<ext:write name="currentOpinion" property="agentName"/>代)</ext:notEqual></ext:notEmpty>&nbsp;<ext:write name="currentOpinion" property="created" format="yyyy-M-d"/>
		  </ext:select></pre></td>
		</tr>
	  </ext:iterate>
	</table>
</ext:notEmpty>