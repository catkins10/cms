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
				        		
				        		<a style="cursor:pointer" onclick="FormUtils.doAction('editOpinion', 'opinionId=<ext:write name="currentOpinion" property="id"/>')">修改</a>
				        		<a style="cursor:pointer" onclick="if(confirm('删除后不能恢复，是否确实删除？'))FormUtils.doAction('deleteOpinion', 'opinionId=<ext:write name="currentOpinion" property="id"/>')">删除</a>
				        	</td>
						</tr>
						<tr>
				   			<td style="height:8px"></td>
				   		</tr>
				  </ext:iterate>
				</table>
			</td>
		</ext:notEmpty>
		<td valign="top">
			<table width="100%" border="0" cellpadding="3" cellspacing="0" bordercolor="black" bgcolor="">
				<col>
				<col>
				<tr class="row">
					<td nowrap>意见类型:</td>
					<td nowrap="true"><ext:field property="opinionType"/></td>
				</tr>
				<tr class="row">
					<td nowrap>意见批示人:</td>
					<td  nowrap="true"><ext:field property="opinionCreator"/></td>
				</tr>
				<tr class="row">
					<td nowrap>意见录入人:</td>
					<td  nowrap="true"><ext:write name="SessionInfo" property="userName" scope="session"/></td>
				</tr>
				<tr class="row">
					<td nowrap>填写时间:</td>
					<td  nowrap="true"><ext:field property="opinionCreated"/></td>
				</tr>
				<tr class="row">
					<td nowrap valign="top" style="padding-top: 5px">意见内容:</td>
					<td >
						<html:textarea property="opinion" rows="10" style="width:100%; height:130px; overflow:auto"/>
					</td>
				</tr>
				<tr class="row" valign="top">
					<td colspan="5" align="right" style="padding-right:20px">
						<input onclick="saveOpinion()" type="button" class="button" style="width:50px" value="提交">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<script>
	function saveOpinion() {
		if(document.getElementsByName("opinionCreator")[0].value=="") {
			alert("意见填写人不能为空");
			return;
		}
		if(document.getElementsByName("opinionCreated")[0].value=="") {
			alert("意见填写时间不能为空");
			return;
		}
		if(document.getElementsByName("opinion")[0].value=="") {
			alert("意见不能为空");
			return;
		}
		FormUtils.doAction('saveOpinion');
	}
</script>