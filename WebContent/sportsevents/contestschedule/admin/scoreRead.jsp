<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<table  width="100%" border="0" cellpadding="3" cellspacing="0" >
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td  nowrap="nowrap">参赛代表团</td>
		<td  ><ext:field property="player" writeonly="true"/></td>
		<td  nowrap="nowrap">比赛项目名称</td>
		<td  ><ext:field property="gameName" writeonly="true"/></td>
	</tr>
	<tr>
		<td  nowrap="nowrap">分数</td>
		<td ><ext:field property="score" writeonly="true"/></td>
		<td  nowrap="nowrap">金牌数</td>
		<td><ext:field property="goldMedalCount" writeonly="true"/></td>
	</tr>
	<tr>
		<td  nowrap="nowrap">银牌数</td>
		<td ><ext:field property="silverMedalCount" writeonly="true"/></td>
		<td  nowrap="nowrap">铜牌数</td>
		<td ><ext:field property="bronzeMedalCount" writeonly="true"/></td>
	</tr>
</table>
