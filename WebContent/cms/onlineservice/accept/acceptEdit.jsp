<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function submitAccept(isNew) { //提交
	if(document.getElementsByName("step")[0].value!='new') {
		//检查是否已经上传附件
		var uploaded = false;
		for(var i=window.frames.length-1; i>=0; i--) {
			var win = window.frames[i];
			if(win.frameElement && win.frameElement.id.indexOf("attachmentFrame_")==0 && win.document.getElementsByName("attachmentSelector.attachmentCount")[0].value!="0") {
				uploaded = true;
				break;
			}
		}
		if(!uploaded && !confirm('尚未上传材料，是否仍然提交？')) {
			return;
		}
	}
	FormUtils.doAction(isNew ? "saveAccept" : "runAccept");
}
</script>

<ext:tab>
	<ext:tabBody tabId="basic">
		<div style="display:none">
			<ext:field property="caseNumber"/>
		</div>
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col valign="middle" width="50%">
			<col>
			<col valign="middle" width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">办事事项</td>
				<td class="tdcontent" colspan="3"><ext:field property="itemName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">受理编号</td>
				<td class="tdcontent" style="color:#ff0000" colspan="3"><ext:field property="sn"/> </td>
			</tr>
			<!-- (提示:输入编号和状态查询密码可以直接查询办理状态)<tr>
				<td class="tdtitle" nowrap="nowrap">状态查询密码</td>
				<td class="tdcontent" colspan="3"><ext:field property="queryPassword"/></td>
			</tr> -->
			<tr>
				<td class="tdtitle" nowrap="nowrap">申报人</td>
				<td class="tdcontent">
				<html:hidden property="member" />
				<ext:write property="member"/>
				</td>
				<td class="tdtitle" nowrap="nowrap">申报时间</td>
				<td class="tdcontent"><ext:field property="created"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">证照号</td>
				<td class="tdcontent" colspan="3"><ext:field property="creatorIdentityCard"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">联系电话</td>
				<td class="tdcontent"><ext:field property="creatorTel"/></td>
				<td class="tdtitle" nowrap="nowrap">邮箱</td>
				<td class="tdcontent"><ext:field property="creatorMail"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">地址</td>
				<td class="tdcontent"><ext:field property="creatorAddress"/></td>
				<td class="tdtitle" nowrap="nowrap">传真</td>
				<td class="tdcontent"><ext:field property="creatorFax"/></td>
			</tr>
			<ext:notEmpty property="caseDeclinedReason">
				<tr>
					<td class="tdtitle" nowrap="nowrap">受理情况</td>
					<td class="tdcontent" colspan="3" style="color:red;">未受理</td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">未受理原因</td>
					<td class="tdcontent" colspan="3">
						<pre><ext:field writeonly="true" property="caseDeclinedReason"/></pre>
					</td>
				</tr>
			</ext:notEmpty>
			<tr>
				<td class="tdtitle" nowrap="nowrap">创建人</td>
				<td class="tdcontent"><ext:field property="creator"/></td>
				<td class="tdtitle" nowrap="nowrap">创建时间</td>
				<td class="tdcontent"><ext:field property="created"/></td>
			</tr>
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
			<tr>
				<td class="tdtitle" nowrap="nowrap">表格</td>
				<td class="tdcontent">
					<ext:iterate id="download" property="serviceItem.downloads">
						<a href="<ext:write name="download" property="tableURL"/>" target="_blank"><ext:write name="download" property="tableName"/></a>&nbsp;&nbsp;
					</ext:iterate>
				</td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">样表</td>
				<td class="tdcontent">
					<ext:iterate id="download" property="serviceItem.exampleDownloads">
						<a href="<ext:write name="download" property="exampleURL"/>" target="_blank"><ext:write name="download" property="tableName"/></a>&nbsp;&nbsp;
					</ext:iterate>
				</td>
			</tr>
		</table>
	</ext:tabBody>-->
		
	<ext:tabBody tabId="material">
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
							<ext:field property="material"/>
						</td>
					</tr>
				</ext:notEmpty>
			</ext:iterate>
		</table>
		<div style="padding-top: 5px;">
			注：上传较大的申报材料时，请<a style="color:#ff0000" href="<%=request.getContextPath()%>/jeaf/res/plugin.exe">点击下载</a>文件上传插件。
		</div>
		
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
							<ext:field property="materialMissing"/>
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
							<ext:field property="materialMissing"/>
						</td>
					</tr>
				</ext:notEmpty>
			</ext:iterate>
		</table>
		</ext:iterateAttachment>
		</ext:notEqual>
		
	</ext:tabBody>
</ext:tab>