<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col valign="middle" width="50%">
			<col>
			<col valign="middle" width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">办事事项</td>
				<td class="tdcontent" colspan="3">
					<html:hidden property="itemId"/>
					<ext:write property="itemName"/>
				</td>
			</tr>
			<tr>
				<td valign="top" class="tdtitle">受理编号</td>
				<td class="tdcontent"><ext:field writeonly="true" property="sn"/></td>
				<td valign="top" class="tdtitle">受理件数</td>
				<td class="tdcontent"><ext:field writeonly="true" property="caseNumber"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">申报人</td>
				<td class="tdcontent"><ext:field writeonly="true" property="member"/></td>
				<td class="tdtitle" nowrap="nowrap">申报时间</td>
				<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">证件名称</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creatorCertificateName"/></td>
				<td class="tdtitle" nowrap="nowrap">证件号码</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creatorIdentityCard"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">联系电话</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
				<td class="tdtitle" nowrap="nowrap">邮箱</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在单位</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creatorUnit"/></td>
				<td class="tdtitle" nowrap="nowrap">传真</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creatorFax"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">地址</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creatorAddress"/></td>
				<td class="tdtitle" nowrap="nowrap">邮编</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creatorPostalcode"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">创建人</td>
				<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
				<td class="tdtitle" nowrap="nowrap">创建时间</td>
				<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
			</tr>
			<ext:notEmpty property="agentorId">
				<td class="tdtitle" nowrap="nowrap">代理人</td>
				<td class="tdcontent"><ext:field writeonly="true" property="agentor"/></td>
				<td class="tdtitle" nowrap="nowrap">代理申报时间</td>
				<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
			</ext:notEmpty>
			<ext:equal value="1" property="caseAccept">
				<tr>
					<td class="tdtitle" nowrap="nowrap">受理情况</td>
					<td class="tdcontent">同意受理</td>
					<td class="tdtitle" nowrap="nowrap">办理状态</td>
					<td class="tdcontent"><ext:field property="acceptStatus"/></td>
				</tr>
			</ext:equal>
			<ext:notEmpty property="caseDeclinedReason">
				<tr>
					<td class="tdtitle" nowrap="nowrap">受理情况</td>
					<td class="tdcontent" colspan="3">不同意受理</td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">不同意原因</td>
					<td class="tdcontent" colspan="3">
						<pre><ext:field writeonly="true" property="caseDeclinedReason"/></pre>
					</td>
				</tr>
			</ext:notEmpty>
			<ext:iterate id="missing" property="missings" length="1">
				<tr>
					<td class="tdtitle" nowrap="nowrap">缺件</td>
					<td class="tdcontent" colspan="3" style="color:#ff0000">
						<pre><ext:write name="missing" property="description"/></pre>
					</td>
				</tr>
			</ext:iterate>
		</table>
	</ext:tabBody>
	
	<ext:notEmpty property="serviceItem">
		<!--<ext:tabBody tabId="guide">
			<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col valign="middle" width="100%">
				<tr>
					<td class="tdtitle" nowrap="nowrap">项目名称</td>
					<td class="tdcontent"><ext:write property="serviceItem.name"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">申办条件</td>
					<td class="tdcontent"><ext:write property="serviceItem.serviceItemGuide.condition" filter="false"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">审批服务依据</td>
					<td class="tdcontent"><ext:write property="serviceItem.serviceItemGuide.according" filter="false"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">审批服务程序</td>
					<td class="tdcontent"><ext:write property="serviceItem.serviceItemGuide.program" filter="false"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">承诺时限</td>
					<td class="tdcontent"><ext:write property="serviceItem.serviceItemGuide.timeLimit"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">收费依据</td>
					<td class="tdcontent"><ext:write property="serviceItem.serviceItemGuide.chargeAccording" filter="false"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">收费标准</td>
					<td class="tdcontent"><ext:write property="serviceItem.serviceItemGuide.chargeStandard"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">申请人法律权利<br>及申述途径</td>
					<td class="tdcontent"><ext:write property="serviceItem.serviceItemGuide.legalRight" filter="false"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">受理单位</td>
					<td class="tdcontent">
						<ext:iterate id="unit" property="serviceItem.units">
							<ext:write name="unit" property="unitName"/>
						</ext:iterate>
					</td>
				</tr>
			</table>
		</ext:tabBody>-->
		
		<ext:tabBody tabId="material">
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" nowrap="nowrap" class="tdtitle" width="60%">材料名称</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="40%">申报说明</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="36px">表格</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="36px">样表</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="290px">材料上传</td>
				</tr>
				<ext:iterate id="material" indexId="materialIndex" property="serviceItem.materials">
					<ext:notEmpty name="material" property="name">
						<tr style="background-color:#ffffff" align="center" valign="middle">
							<td class="tdcontent" align="left"><ext:write name="material" property="name"/></td>
							<td class="tdcontent" align="left"><ext:write name="material" property="description"/></td>
							<td class="tdcontent" align="left">
								<ext:notEmpty name="material" property="tableURL">
									<a href="<ext:write name="material" property="tableURL"/>" target="_blank">下载</a>
								</ext:notEmpty>
							</td>
							<td class="tdcontent" align="left">
								<ext:notEmpty name="material" property="exampleURL">
									<a href="<ext:write name="material" property="exampleURL"/>" target="_blank">下载</a>
								</ext:notEmpty>
							</td>
							<td class="tdcontent" align="left">
								<%request.setAttribute("material", pageContext.getAttribute("material")); %>
								<ext:field writeonly="true" property="material"/>
							</td>
						</tr>
					</ext:notEmpty>
				</ext:iterate>
			</table>
			
				<ext:equal property="acceptStatus" value='缺件'>
		<div style="padding-top: 30px; padding-bottom: 10px;">
			<b>办理过程中产生的相关材料:</b>
		</div>
		<table valign="middle" width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
			<col width="60%">
			<col width="40%">
			<col width="36px">
			<col width="36px">
			<col width="290px">
			<tr height="23px">
				<td align="center" class="tdtitle">材料名称</td>
				<td align="center" class="tdtitle">申报说明</td>
				<td align="center" class="tdtitle">表格</td>
				<td align="center" class="tdtitle">样表</td>
				<td align="center" class="tdtitle">材料上传</td>
			</tr>
			<ext:greaterEqual></ext:greaterEqual>
			<ext:writeNumber name="materialIndex" plus="1"/>
			<ext:iterate id="material" indexId="materialIndex" property="serviceItem.materials">
				<ext:notEmpty name="material" property="name">
					<tr style="background-color:#ffffff" align="center" valign="middle">
						<td align="left" class="tdcontent"><ext:write name="material" property="name"/></td>
						<td align="left" class="tdcontent"><ext:write name="material" property="description"/></td>
						<td align="left" class="tdcontent">
							<ext:notEmpty name="material" property="tableURL">
								<a href="<ext:write name="material" property="tableURL"/>" target="_blank">下载</a>
							</ext:notEmpty>
						</td>
						<td align="left" class="tdcontent">
							<ext:notEmpty name="material" property="exampleURL">
								<a href="<ext:write name="material" property="exampleURL"/>" target="_blank">下载</a>
							</ext:notEmpty>
						</td>
						<td align="left" class="tdcontent">
							<%request.setAttribute("material", pageContext.getAttribute("material")); %>
							<ext:field writeonly="true" property="materialMissing"/>
						</td>
					</tr>
				</ext:notEmpty>
			</ext:iterate>
		</table>
		</ext:equal>
		
		<ext:notEqual property="acceptStatus" value='缺件'>
		<ext:iterateAttachment id="dataFile" indexId="missIndex" applicationName="cms/onlineservice/accept" propertyRecordId="id" attachmentType="materialMissing" length="1">
		<div style="padding-top: 30px; padding-bottom: 10px;">
			<b>办理过程中产生的相关材料:</b>
		</div>
		<table valign="middle" width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
			<col width="60%">
			<col width="40%">
			<col width="36px">
			<col width="36px">
			<col width="290px">
			<tr height="23px">
				<td align="center" class="tdtitle">材料名称</td>
				<td align="center" class="tdtitle">申报说明</td>
				<td align="center" class="tdtitle">表格</td>
				<td align="center" class="tdtitle">样表</td>
				<td align="center" class="tdtitle">材料上传</td>
			</tr>
			<ext:iterate id="material" indexId="materialIndex" property="serviceItem.materials">
				<ext:notEmpty name="material" property="name">
					<tr style="background-color:#ffffff" align="center" valign="middle">
						<td align="left" class="tdcontent"><ext:write name="material" property="name"/></td>
						<td align="left" class="tdcontent"><ext:write name="material" property="description"/></td>
						<td align="left" class="tdcontent">
							<ext:notEmpty name="material" property="tableURL">
								<a href="<ext:write name="material" property="tableURL"/>" target="_blank">下载</a>
							</ext:notEmpty>
						</td>
						<td align="left" class="tdcontent">
							<ext:notEmpty name="material" property="exampleURL">
								<a href="<ext:write name="material" property="exampleURL"/>" target="_blank">下载</a>
							</ext:notEmpty>
						</td>
						<td align="left" class="tdcontent">
							<%request.setAttribute("material", pageContext.getAttribute("material")); %>
							<ext:field writeonly="true" property="materialMissing"/>
						</td>
					</tr>
				</ext:notEmpty>
			</ext:iterate>
		</table>
		</ext:iterateAttachment>
		</ext:notEqual>
		</ext:tabBody>
	</ext:notEmpty>
</ext:tab>