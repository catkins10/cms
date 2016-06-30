<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertResources">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
	<script type="text/javascript">
		//获取配置元素标题,由继承者实现
		function getElementTitle(isSearchResults, isRssChannel) {
			if(isRssChannel) {
				return "RSS";
			}
			else if(isSearchResults) {
				return "搜索结果";
			}
			else {
				return (parent.document.getElementsByName("recordListName")[0].value=="hotResources" ? "热门" : "") + "文章列表:" + (document.getElementsByName("siteSelect")[0].checked ? "本站点/栏目" : document.getElementsByName("siteNames")[0].value);
			}
		}
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties!="") {
				var siteNames =  StringUtils.getPropertyValue(recordListProperties, "siteNames");
				var siteIds =  StringUtils.getPropertyValue(recordListProperties, "siteIds");
				var resourceTypes = StringUtils.getPropertyValue(recordListProperties, "resourceTypes");
				if(siteNames=="") {
					siteNames = recordListTextContent.replace("&lt;热门文章列表:", "").replace("&lt;文章列表:", "").replace("&gt;", "").replace("&lt;", ""); //站点名称
				}
				if(siteIds!="-1") {
					document.getElementsByName("siteNames")[0].value = siteNames;
					document.getElementsByName("siteIds")[0].value = siteIds;
					var siteSelect = document.getElementsByName("siteSelect")["true"==StringUtils.getPropertyValue(recordListProperties, "siteSelectByName") ? 2 : 1];
					siteSelect.checked = true;
					siteSelectChanged(siteSelect.value);
				}
				//资源类型
				if(resourceTypes.indexOf("all")==-1) {
					document.getElementsByName("articleEnable")[0].disabled = false;
					document.getElementsByName("linkEnable")[0].disabled = false;
					document.getElementsByName("selectAllTypes")[0].checked = false;
					document.getElementsByName("articleEnable")[0].checked = (resourceTypes.indexOf("article")!=-1);
					document.getElementsByName("linkEnable")[0].checked = (resourceTypes.indexOf("link")!=-1);
				}
				//是否仅图片文章
				document.getElementsByName("imageResourceOnly")[0].checked = "true"==StringUtils.getPropertyValue(recordListProperties, "imageResourceOnly");
				//是否仅视频文章
				document.getElementsByName("videoResourceOnly")[0].checked = "true"==StringUtils.getPropertyValue(recordListProperties, "videoResourceOnly");
				//是否包含子站资源
				document.getElementsByName("containChildren")[0].checked = "true"==StringUtils.getPropertyValue(recordListProperties, "containChildren");
			}
			updateResourcesView(); //更新视图
		}
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			var siteIds = document.getElementsByName("siteIds")[0].value;
			var siteNames = document.getElementsByName("siteNames")[0].value;
			if(document.getElementsByName("siteSelect")[0].checked) {
				siteNames = "本站点/栏目";
				siteIds = "-1";
			}
			else if(siteNames=='') {
				alert("站点列表不能为空");
				return "ERROR";
			}
			//资源类型
			var resourceTypes = "all";
			if(!document.getElementsByName("selectAllTypes")[0].checked) {
				resourceTypes = document.getElementsByName("articleEnable")[0].checked ? ",article" : "";
				resourceTypes += document.getElementsByName("linkEnable")[0].checked ? ",link" : "";
				if(resourceTypes=="") {
					alert("资源类型不能为空");
					return "ERROR";
				}
				resourceTypes = resourceTypes.substring(1);
			}
			var referenceApplicationName = !parent.document.getElementsByName("redirectApplicationName")[0] ? '' : parent.document.getElementsByName("redirectApplicationName")[0].value;
			var referenceRecordListName = !parent.document.getElementsByName("redirectRecordListName")[0] ? '' : parent.document.getElementsByName("redirectRecordListName")[0].value;
			return "siteNames=" + siteNames +
				   "&siteIds=" + siteIds + 
				   (!document.getElementsByName("siteSelect")[2].checked ? "" : "&siteSelectByName=true") +
				   "&resourceTypes=" + resourceTypes + 
				   (!document.getElementsByName("imageResourceOnly")[0].checked ? "" : "&imageResourceOnly=true") +
				   (!document.getElementsByName("videoResourceOnly")[0].checked ? "" : "&videoResourceOnly=true") +
				   (!document.getElementsByName("containChildren")[0].checked ? "" : "&containChildren=true") + //是否显示子栏目数据
				   (referenceApplicationName=="" ? "" : "&referenceApplicationName=" + referenceApplicationName) +
				   (referenceRecordListName=="" ? "" : "&referenceRecordListName=" + referenceRecordListName);
		}
		
		//获取默认的左侧记录格式
		function getDeafultRecordLeftFormat() {
			return '<a id="field" urn="name=subject&link=true">&lt;标题&gt;</a>&nbsp;<a id="field" urn="name=issueTime&fieldFormat=yyyy-MM-dd">&lt;发布时间&gt;</a>';
		}
		//获取默认的右侧记录格式
		function getDeafultRecordRightFormat() {
			return '';
		}
		function onClickSelectAll(checked) {
			document.getElementsByName("articleEnable")[0].disabled = checked;
			document.getElementsByName("linkEnable")[0].disabled = checked;
			if(checked) {
				document.getElementsByName("articleEnable")[0].checked = true;
				document.getElementsByName("linkEnable")[0].checked = true;
			}
		}
		function selectRecordListSite() {
			var siteSelects = document.getElementsByName("siteSelect");
			selectSite(600, 400, true, 'siteIds{id},siteNames{name|名称|100%}', '', '', ',', 'column,viewReference', (siteSelects[2].checked ? editor.document.getElementsByName("siteId")[0].value : ''));
		}
		function siteSelectChanged(mode) {
			document.getElementById("selectedSites").style.display = (mode=="current" ? "none" : "");
			updateResourcesView(); //更新视图
		}
		function onSiteChanged() { //选择的站点改变
			updateResourcesView(); //更新视图
			var siteIds = document.getElementsByName("siteIds")[0].value;
			if(siteIds=="") {
				return;
			}
			//修改统计栏目属性		
			var link = recordListDocument.getElementsByName("totalLink")[0];
			if(link && link.value!="") {
				var url = link.value;
				var index = url.indexOf("?");
				if(index!=-1) {
					url = url.substring(0, index);
				}
				link.value = url + "?siteId=" + siteIds.split(",")[0];
			}
			var totalTitle = recordListDocument.getElementsByName("totalTitle")[0];
			if(totalTitle) {
				totalTitle.value = document.getElementsByName("siteNames")[0].value;
			}
		}
		//更新视图
		function updateResourcesView() {
			var siteIds = document.getElementsByName("siteIds")[0].value;
			var siteSelects = document.getElementsByName("siteSelect");
			if(!siteSelects[0].checked && siteIds=="") {
				return;
			}
			var js = "updateResourcesView.js.shtml?siteId=" + (siteSelects[0].checked ? editor.document.getElementsByName("siteId")[0].value : siteIds.split(",")[0]);
			ScriptUtils.appendJsFile(document, js, "updateResourcesView");
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<col align="right">
		<col width="100%">
		<tr valign="middle" style="<%=("true".equals(request.getParameter("searchResults")) ? "display:none" : "")%>">
			<td height="26px" nowrap="nowrap">栏目选择：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td nowrap="nowrap"><ext:field property="siteSelect" onclick="siteSelectChanged(value)"/></td>
						<td id="selectedSites" style="display:none;width:160px"><ext:field property="siteNames" onchange="onSiteChanged()"/></td>
						<td nowrap="nowrap"><ext:field property="containChildren"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr valign="middle">
			<td>资源类型：</td>
			<td>
				<ext:field property="selectAllTypes" onclick="onClickSelectAll(checked)"/>&nbsp;
				<ext:field disabled="true" property="articleEnable"/>&nbsp;
				<ext:field disabled="true" property="linkEnable"/>&nbsp;
				<ext:field property="imageResourceOnly" onclick="if(this.checked)document.getElementsByName('videoResourceOnly')[0].checked=false"/>&nbsp;
				<ext:field property="videoResourceOnly" onclick="if(this.checked)document.getElementsByName('imageResourceOnly')[0].checked=false"/>
			</td>
		</tr>
	</table>
</ext:form>