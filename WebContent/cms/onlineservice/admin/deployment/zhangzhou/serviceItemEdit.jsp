<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:notEqual value="漳州市行政服务中心" property="serviceItemGuide.address">
	<ext:tab>
		<ext:tabBody tabId="basic">
			<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col width="33%">
				<col>
				<col width="33%">
				<col>
				<col width="33%">
				<tr>
					<td class="tdtitle" valign="top">名称</td>
					<td class="tdcontent" colspan="5"><ext:field property="name"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">编号</td>
					<td class="tdcontent"><ext:field property="code"/></td>
					<td class="tdtitle" nowrap="nowrap">是否发布</td>
					<td class="tdcontent"><ext:field property="isPublic"/></td>
					<td class="tdtitle" nowrap="nowrap">事项类型</td>
					<td class="tdcontent"><ext:field property="itemType"/></td>
				</tr>
					<tr>
					<td class="tdtitle" nowrap="nowrap">所属目录</td>
					<td class="tdcontent"><ext:field property="directoryName"/></td>
					<td class="tdtitle" nowrap="nowrap">所属其它目录</td>
					<td class="tdcontent"><ext:field property="otherDirectoryNames"/></td>
					<td class="tdtitle" nowrap="nowrap">公共服务类别</td>
					<td class="tdcontent"><ext:field property="publicServiceType"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">办理机构</td>
					<td class="tdcontent"><ext:field property="serviceItemUnitNames"/></td>
					<td class="tdtitle" nowrap="nowrap">责任部门</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.responsibleDepartment"/></td>
					<td class="tdtitle" nowrap="nowrap">监管等级</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.superviseLevel"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">办理地点</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.address"/></td>
					<td class="tdtitle" nowrap="nowrap">交通线路</td>
					<td class="tdcontent" colspan="3"><ext:field property="serviceItemGuide.traffic"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">经办人</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.transactor"/></td>
					<td class="tdtitle" nowrap="nowrap">联系电话</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.transactorTel"/></td>
					<td class="tdtitle" nowrap="nowrap">在线受理办理人</td>
					<td class="tdcontent"><ext:field property="serviceItemTransactorNames"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">办理期限</td>
					<td class="tdcontent" colspan="5"><ext:field property="serviceItemGuide.timeLimit"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">受理时间</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.acceptTime"/></td>
					<td class="tdtitle" nowrap="nowrap">受理数量限制</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.acceptLimit"/></td>
					<td class="tdtitle" nowrap="nowrap">监督投诉电话</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.complaintTel"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线受理</td>
					<td class="tdcontent"><ext:field property="acceptSupport"/></td>
					<td class="tdtitle" nowrap="nowrap">在线受理流程</td>
					<td class="tdcontent" title="在线受理流程"><ext:field property="acceptWorkflowName"/></td>
					<td class="tdtitle" nowrap="nowrap">在线受理URL</td>
					<td class="tdcontent"><ext:field property="acceptUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">状态查询</td>
					<td class="tdcontent"><ext:field property="querySupport"/></td>
					<td class="tdtitle" nowrap="nowrap">状态查询URL</td>
					<td class="tdcontent" colspan="3"><ext:field property="queryUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线投诉</td>
					<td class="tdcontent"><ext:field property="complaintSupport"/></td>
					<td class="tdtitle" nowrap="nowrap">在线投诉流程</td>
					<td class="tdcontent" title="在线投诉流程"><ext:field property="complaintWorkflowName"/></td>
					<td class="tdtitle" nowrap="nowrap">在线投诉URL</td>
					<td class="tdcontent"><ext:field property="complaintUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线咨询</td>
					<td class="tdcontent"><ext:field property="consultSupport"/></td>
					<td class="tdtitle" nowrap="nowrap">在线咨询流程</td>
					<td class="tdcontent" title="在线咨询流程"><ext:field property="consultWorkflowName"/>	</td>
					<td class="tdtitle" nowrap="nowrap">在线咨询URL</td>
					<td class="tdcontent"><ext:field property="consultUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">公开形式</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.publicMode"/></td>
					<td class="tdtitle" nowrap="nowrap">公开范围</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.publicRange"/></td>
					<td class="tdtitle" nowrap="nowrap">公开时间</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.publicTime"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">创建人</td>
					<td class="tdcontent"><ext:field property="creator"/></td>
					<td class="tdtitle" nowrap="nowrap">创建时间</td>
					<td class="tdcontent" colspan="3"><ext:field property="created"/></td>
				</tr>
			</table>
		</ext:tabBody>
		
		<%@ include file="serviceItemTabsEdit.jsp" %>
	</ext:tab>
</ext:notEqual>

<ext:equal value="漳州市行政服务中心" property="serviceItemGuide.address"><ext:notEmpty property="creator">
	<ext:tab>
		<ext:tabBody tabId="basic">
			<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col width="33%">
				<col>
				<col width="33%">
				<col>
				<col width="33%">
				<tr>
					<td class="tdtitle" valign="top">名称</td>
					<td class="tdcontent" colspan="5"><ext:field property="name"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">编号</td>
					<td class="tdcontent"><ext:field property="code"/></td>
					<td class="tdtitle" nowrap="nowrap">是否发布</td>
					<td class="tdcontent"><ext:field property="isPublic"/></td>
					<td class="tdtitle" nowrap="nowrap">事项类型</td>
					<td class="tdcontent"><ext:field property="itemType"/></td>
				</tr>
					<tr>
					<td class="tdtitle" nowrap="nowrap">所属目录</td>
					<td class="tdcontent"><ext:field property="directoryName"/></td>
					<td class="tdtitle" nowrap="nowrap">所属其它目录</td>
					<td class="tdcontent"><ext:field property="otherDirectoryNames"/></td>
					<td class="tdtitle" nowrap="nowrap">公共服务类别</td>
					<td class="tdcontent"><ext:field property="publicServiceType"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">办理机构</td>
					<td class="tdcontent"><ext:field property="serviceItemUnitNames"/></td>
					<td class="tdtitle" nowrap="nowrap">责任部门</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.responsibleDepartment"/></td>
					<td class="tdtitle" nowrap="nowrap">监管等级</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.superviseLevel"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">办理地点</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.address"/></td>
					<td class="tdtitle" nowrap="nowrap">交通线路</td>
					<td class="tdcontent" colspan="3"><ext:field property="serviceItemGuide.traffic"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">经办人</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.transactor"/></td>
					<td class="tdtitle" nowrap="nowrap">联系电话</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.transactorTel"/></td>
					<td class="tdtitle" nowrap="nowrap">在线受理办理人</td>
					<td class="tdcontent"><ext:field property="serviceItemTransactorNames"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">办理期限</td>
					<td class="tdcontent" colspan="5"><ext:field property="serviceItemGuide.timeLimit"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">受理时间</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.acceptTime"/></td>
					<td class="tdtitle" nowrap="nowrap">受理数量限制</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.acceptLimit"/></td>
					<td class="tdtitle" nowrap="nowrap">监督投诉电话</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.complaintTel"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线受理</td>
					<td class="tdcontent"><ext:field property="acceptSupport"/></td>
					<td class="tdtitle" nowrap="nowrap">在线受理流程</td>
					<td class="tdcontent" title="在线受理流程"><ext:field property="acceptWorkflowName"/></td>
					<td class="tdtitle" nowrap="nowrap">在线受理URL</td>
					<td class="tdcontent"><ext:field property="acceptUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">状态查询</td>
					<td class="tdcontent"><ext:field property="querySupport"/></td>
					<td class="tdtitle" nowrap="nowrap">状态查询URL</td>
					<td class="tdcontent" colspan="3"><ext:field property="queryUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线投诉</td>
					<td class="tdcontent"><ext:field property="complaintSupport"/></td>
					<td class="tdtitle" nowrap="nowrap">在线投诉流程</td>
					<td class="tdcontent" title="在线投诉流程"><ext:field property="complaintWorkflowName"/></td>
					<td class="tdtitle" nowrap="nowrap">在线投诉URL</td>
					<td class="tdcontent"><ext:field property="complaintUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线咨询</td>
					<td class="tdcontent"><ext:field property="consultSupport"/></td>
					<td class="tdtitle" nowrap="nowrap">在线咨询流程</td>
					<td class="tdcontent" title="在线咨询流程"><ext:field property="consultWorkflowName"/>	</td>
					<td class="tdtitle" nowrap="nowrap">在线咨询URL</td>
					<td class="tdcontent"><ext:field property="consultUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">公开形式</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.publicMode"/></td>
					<td class="tdtitle" nowrap="nowrap">公开范围</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.publicRange"/></td>
					<td class="tdtitle" nowrap="nowrap">公开时间</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.publicTime"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">创建人</td>
					<td class="tdcontent"><ext:field property="creator"/></td>
					<td class="tdtitle" nowrap="nowrap">创建时间</td>
					<td class="tdcontent" colspan="3"><ext:field property="created"/></td>
				</tr>
			</table>
		</ext:tabBody>
		
		<%@ include file="serviceItemTabsEdit.jsp" %>
	</ext:tab>
</ext:notEmpty></ext:equal>

<ext:equal value="漳州市行政服务中心" property="serviceItemGuide.address"><ext:empty property="creator">
	<ext:tab>
		<ext:tabBody tabId="basic">
			<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col width="33%">
				<col>
				<col width="33%">
				<col>
				<col width="33%">
				<tr>
					<td class="tdtitle" valign="top">名称</td>
					<td class="tdcontent" colspan="5"><ext:field writeonly="true" property="name"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">编号</td>
					<td class="tdcontent"><ext:field writeonly="true" property="code"/></td>
					<td class="tdtitle" nowrap="nowrap">是否发布</td>
					<td class="tdcontent"><ext:field writeonly="true" property="isPublic"/></td>
					<td class="tdtitle" nowrap="nowrap">审批服务类型</td>
					<td class="tdcontent"><ext:field writeonly="true" property="itemType"/></td>
				</tr>
					<tr>
					<td class="tdtitle" nowrap="nowrap">所属目录</td>
					<td class="tdcontent"><ext:field writeonly="true" property="directoryName"/></td>
					<td class="tdtitle" nowrap="nowrap">所属其它目录</td>
					<td class="tdcontent"><ext:field property="otherDirectoryNames"/></td>
					<td class="tdtitle" nowrap="nowrap">公共服务类别</td>
					<td class="tdcontent"><ext:field property="publicServiceType"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">办理机构</td>
					<td class="tdcontent"><ext:field writeonly="true" property="serviceItemUnitNames"/></td>
					<td class="tdtitle" nowrap="nowrap">责任部门</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.responsibleDepartment"/></td>
					<td class="tdtitle" nowrap="nowrap">办理地点</td>
					<td class="tdcontent"><ext:field writeonly="true" property="serviceItemGuide.address"/><html:hidden property="serviceItemGuide.address"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">交通线路</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.traffic"/></td>
					<td class="tdtitle" nowrap="nowrap">经办人</td>
					<td class="tdcontent"><ext:field writeonly="true" property="serviceItemGuide.transactor"/><html:hidden property="serviceItemGuide.transactor"/></td>
					<td class="tdtitle" nowrap="nowrap">联系电话</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.transactorTel"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线受理办理人</td>
					<td class="tdcontent"><ext:field property="serviceItemTransactorNames"/></td>
					<td class="tdtitle" nowrap="nowrap">办理期限</td>
					<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="serviceItemGuide.timeLimit"/><html:hidden property="serviceItemGuide.timeLimit"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">受理时间</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.acceptTime"/></td>
					<td class="tdtitle" nowrap="nowrap">受理数量限制</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.acceptLimit"/></td>
					<td class="tdtitle" nowrap="nowrap">监督投诉电话</td>
					<td class="tdcontent"><ext:field property="serviceItemGuide.complaintTel"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线受理</td>
					<td class="tdcontent">
						<ext:equal value="true" property="manager"><ext:field property="acceptSupport"/></ext:equal>
						<ext:equal value="false" property="manager"><ext:field writeonly="true" property="acceptSupport"/></ext:equal>
					</td>
					<td class="tdtitle" nowrap="nowrap">在线受理流程</td>
					<td class="tdcontent" title="在线受理流程"><ext:field writeonly="true" property="acceptWorkflowName"/></td>
					<td class="tdtitle" nowrap="nowrap">在线受理URL</td>
					<td class="tdcontent"><ext:field writeonly="true" property="acceptUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">状态查询</td>
					<td class="tdcontent"><ext:field writeonly="true" property="querySupport"/></td>
					<td class="tdtitle" nowrap="nowrap">状态查询URL</td>
					<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="queryUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线投诉</td>
					<td class="tdcontent">
						<ext:equal value="true" property="manager"><ext:field property="complaintSupport"/></ext:equal>
						<ext:equal value="false" property="manager"><ext:field writeonly="true" property="complaintSupport"/></ext:equal>
					</td>
					<td class="tdtitle" nowrap="nowrap">在线投诉流程</td>
					<td class="tdcontent" title="在线投诉流程"><ext:field writeonly="true" property="complaintWorkflowName"/></td>
					<td class="tdtitle" nowrap="nowrap">在线投诉URL</td>
					<td class="tdcontent"><ext:field writeonly="true" property="complaintUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">在线咨询</td>
					<td class="tdcontent">
						<ext:equal value="true" property="manager"><ext:field property="consultSupport"/></ext:equal>
						<ext:equal value="false" property="manager"><ext:field writeonly="true" property="consultSupport"/></ext:equal>
					</td>
					<td class="tdtitle" nowrap="nowrap">在线咨询流程</td>
					<td class="tdcontent" title="在线咨询流程"><ext:field writeonly="true" property="consultWorkflowName"/>	</td>
					<td class="tdtitle" nowrap="nowrap">在线咨询URL</td>
					<td class="tdcontent"><ext:field writeonly="true" property="consultUrl"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">备注</td>
					<td class="tdcontent" colspan="5"><ext:field writeonly="true" property="remark"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">创建人</td>
					<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
					<td class="tdtitle" nowrap="nowrap">创建时间</td>
					<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="created"/></td>
				</tr>
			</table>
		</ext:tabBody>
			
		<ext:tabBody tabId="according">
			<div>
				<ext:field writeonly="true" property="serviceItemGuide.according"/>
				<html:hidden property="serviceItemGuide.according"/>
			</div>
		</ext:tabBody>
			
		<ext:tabBody tabId="condition">
			<div>
				<ext:field writeonly="true" property="serviceItemGuide.condition"/>
				<html:hidden property="serviceItemGuide.condition"/>
			</div>
		</ext:tabBody>
			
		<ext:tabBody tabId="program">
			<div style="height:300px">
				<ext:field property="serviceItemGuide.program"/>
			</div>
		</ext:tabBody>
			
		<ext:tabBody tabId="legalRight">
			<div>
				<ext:field writeonly="true" property="serviceItemGuide.legalRight"/>
				<html:hidden property="serviceItemGuide.legalRight"/>
			</div>
		</ext:tabBody>
			
		<ext:tabBody tabId="charge">
			<table valign="middle" width="100%" style="table-layout:fixed;" border="1" cellpadding="0" cellspacing="0" class="table">
				<col width=86px">
				<col width="100%">
				<tr>
					<td class="tdtitle" nowrap="nowrap">收费标准</td>
					<td class="tdcontent">
						<ext:field writeonly="true" property="serviceItemGuide.chargeStandard" />
						<html:hidden property="serviceItemGuide.chargeStandard" />
					</td>
				</tr>	
				<tr valign="top">
					<td class="tdtitle" nowrap="nowrap">收费依据</td>
					<td class="tdcontent">
						<ext:field writeonly="true" property="serviceItemGuide.chargeAccording"/>
						<html:hidden property="serviceItemGuide.chargeAccording" />
					</td>
				</tr>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="materials">
			<jsp:include page="serviceItemMaterials.jsp" />
		</ext:tabBody>
		
		<ext:tabBody tabId="faqs">
			<jsp:include page="serviceItemFaqs.jsp" />
		</ext:tabBody>
		
		<ext:tabBody tabId="complaints">
			<script>
				function newComplaint() {
					DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemComplaint.shtml?id=<ext:write property="id"/>', 800, 480);
				}
				function openComplaint(complaintId) {
					DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemComplaint.shtml?id=<ext:write property="id"/>&complaint.id=' + complaintId, 800, 480);
				}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="投诉登记" onclick="newComplaint()">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" class="tdtitle" nowrap="nowrap" width="36px">序号</td>
					<td align="center" class="tdtitle" nowrap="nowrap" width="100%">投诉主题</td>
					<td align="center" class="tdtitle" nowrap="nowrap" width="100px">投诉人</td>
					<td align="center" class="tdtitle" nowrap="nowrap" width="120px">投诉时间</td>
				</tr>
				<ext:iterate id="complaint" indexId="complaintIndex" property="complaints">
					<tr style="cursor:pointer" valign="top" onclick="openComplaint('<ext:write name="complaint" property="id"/>')">
						<td class="tdcontent" align="center"><ext:writeNumber name="complaintIndex" plus="1"/></td>
						<td class="tdcontent"><ext:write name="complaint" property="subject"/></td>
						<td class="tdcontent"><ext:write name="complaint" property="creator"/></td>
						<td class="tdcontent" align="center"><ext:write name="complaint" property="created" format="yyyy-MM-dd HH:mm"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="consults">
			<script>
				function newConsult() {
					DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemConsult.shtml?id=<ext:write property="id"/>', 800, 480);
				}
				function openConsult(consultId) {
					DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemConsult.shtml?id=<ext:write property="id"/>&consult.id=' + consultId, 800, 480);
				}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="咨询登记" onclick="newConsult()">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" class="tdtitle" nowrap="nowrap" width="36px">序号</td>
					<td align="center" class="tdtitle" nowrap="nowrap" width="100%">咨询主题</td>
					<td align="center" class="tdtitle" nowrap="nowrap" width="100px">咨询人</td>
					<td align="center" class="tdtitle" nowrap="nowrap" width="120px">咨询时间</td>
				</tr>
				<ext:iterate id="consult" indexId="consultIndex" property="consults">
					<tr style="cursor:pointer" valign="top" onclick="openConsult('<ext:write name="consult" property="id"/>')">
						<td class="tdcontent" align="center"><ext:writeNumber name="consultIndex" plus="1"/></td>
						<td class="tdcontent"><ext:write name="consult" property="subject"/></td>
						<td class="tdcontent"><ext:write name="consult" property="creator"/></td>
						<td class="tdcontent" align="center"><ext:write name="consult" property="created" format="yyyy-MM-dd HH:mm"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
	</ext:tab>
</ext:empty></ext:equal>