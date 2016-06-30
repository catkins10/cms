<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="200px">评价等级</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">分数范围</td>
	</tr>
	<ext:iterate id="score" indexId="scoreIndex" property="scores">
		<tr align="left">
			<td class="tdcontent" align="center"><ext:writeNumber name="scoreIndex" plus="1"/></td>
			<td class="tdcontent" align="center"><ext:write name="score" property="level"/></td>
			<td class="tdcontent">
				<input type="hidden" name="levelId" value="<ext:write name="score" property="levelId"/>"/>
				介于
				<ext:equal name="score" property="valid" value="false">
					<input type="text" name="minScore" style="width:80px"/>
					和
					<input type="text" name="maxScore" style="width:80px"/>
				</ext:equal>
				<ext:equal name="score" property="valid" value="true">
					<input type="text" name="minScore" value="<ext:write name="score" property="minScore"/>" style="width:80px"/>
					和
					<input type="text" name="maxScore" value="<ext:write name="score" property="maxScore"/>" style="width:80px"/>
				</ext:equal>
			</td>
		</tr>
	</ext:iterate>
</table>