<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<ext:form action="/admin/outputInfoPublicStat">
	<table border="0" cellpadding="3" cellspacing="0">
   		<tr>
   			<td nowrap>目录：</td>
   			<td width="200px"><ext:field property="directoryName"/></td>
   			<td nowrap>统计时间：</td>
   			<td width="100px"><ext:field property="beginDate"/></td>
   			<td nowrap>至</td>
   			<td width="100px"><ext:field property="endDate"/></td>
		   	<td><input type="button" value="统计" onclick="FormUtils.submitForm()" class="button"></td>
		</tr>
	</table>
	
   	<ext:notEmpty property="infoStats">
   		<div style="padding: 6px 0px 3px 0px"><b>公开情况统计:</b></div>
   		<table border="1" cellpadding="3" cellspacing="0" bordercolor="#000000" style="border-collapse: collapse" width="95%">
   			<tr align="center" class="tdtitle" style="font-weight: bold">
   				<td width="32px">序号</td>
   				<td>指标</td>
   				<td width="50px">单位</td>
   				<td width="80px">数量</td>
				<td width="80px">所占比例</td>
   			</tr>
   			<ext:iterate id="stat" indexId="statIndex" property="infoStats">
   				<tr align="center" class="tdcontent">
   					<td><ext:writeNumber name="statIndex" plus="1"/></td>
	   				<td align="left"><ext:write name="stat" property="name"/></td>
	   				<td>条</td>
	   				<td><ext:write name="stat" property="count"/></td>
					<td>
						<logic:greaterThan value="-1"  name="stat" property="percent">
							<ext:write name="stat" property="percent" format="##.##%"/>
						</logic:greaterThan>
					</td>
				</tr>
   			</ext:iterate>
   		</table>
   	</ext:notEmpty>
   	
   	<ext:notEmpty property="infoCategoryStats">
   		<div style="padding: 16px 0px 3px 0px"><b>分类统计表:</b></div>
   		<table border="1" cellpadding="3" cellspacing="0" bordercolor="#000000" style="border-collapse: collapse" width="95%">
   			<tr align="center" class="tdtitle" style="font-weight: bold">
   				<td width="32px">序号</td>
   				<td>指标名称</td>
   				<td width="70px">计量单位</td>
   				<td width="100px">上一期数据</td>
				<td width="100px">本期增减量</td>
				<td width="100px">本年累计</td>
   			</tr>
   			<ext:iterate id="stat" indexId="statIndex" property="infoCategoryStats">
   				<tr align="center" class="tdcontent">
   					<td><ext:writeNumber name="statIndex" plus="1"/></td>
	   				<td align="left"><ext:write name="stat" property="name"/></td>
	   				<td>条</td>
	   				<td><ext:write name="stat" property="previousSeasonTotal"/></td>
					<td><ext:write name="stat" property="currentSeasonTotal"/></td>
					<td><ext:write name="stat" property="yearTotal"/></td>
				</tr>
   			</ext:iterate>
   		</table>
   	</ext:notEmpty>
   	
   	<ext:notEmpty property="requestStats">
   		<div style="padding: 16px 0px 3px 0px"><b>依申请公开情况统计:</b></div>
   		<table border="1" cellpadding="3" cellspacing="0" bordercolor="#000000" style="border-collapse: collapse" width="95%">
   			<tr align="center" class="tdtitle" style="font-weight: bold">
   				<td width="32px">序号</td>
   				<td>指标</td>
   				<td width="50px">单位</td>
   				<td width="80px">数量</td>
				<td width="80px">所占比例</td>
   			</tr>
   			<ext:iterate id="stat" indexId="statIndex" property="requestStats">
   				<tr align="center" class="tdcontent">
   					<td><ext:writeNumber name="statIndex" plus="1"/></td>
	   				<td align="left"><ext:write name="stat" property="name"/></td>
	   				<td>条</td>
	   				<td><ext:write name="stat" property="count"/></td>
					<td>
						<logic:greaterThan value="-1"  name="stat" property="percent">
							<ext:write name="stat" property="percent" format="##.##%"/>
						</logic:greaterThan>
					</td>
				</tr>
   			</ext:iterate>
   		</table>
   	</ext:notEmpty>
</ext:form>