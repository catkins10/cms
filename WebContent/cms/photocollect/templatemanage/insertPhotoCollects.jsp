<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertPhotoCollects">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/recordlist/js/insertRecordListExtend.js"></script>
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
				return "图片列表" + (document.getElementsByName("photoCollectCategories")[0].value=="" ? "" : ":" + document.getElementsByName("photoCollectCategories")[0].value);
			}
		}
		//显示扩展属性,由继承者实现
		function showExtendProperties(recordListProperties, recordListTextContent) {
			if(recordListProperties=="") {
				return;
			}
			document.getElementsByName("photoCollectCategories")[0].value = StringUtils.getPropertyValue(recordListProperties, "photoCollectCategories");
			var siteNames =  StringUtils.getPropertyValue(recordListProperties, "siteNames");
			if(siteNames!="") {
				document.getElementsByName("siteNames")[0].value = siteNames;
				document.getElementsByName("siteIds")[0].value = StringUtils.getPropertyValue(recordListProperties, "siteIds");
				document.getElementsByName("siteSelect")[1].checked = true;
				onSiteSelectChanged('other');
				document.getElementsByName("containChildren")[0].checked = "true"==StringUtils.getPropertyValue(recordListProperties, "containChildren");
			}
		}
		
		//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
		function getExtendPropertiesAsText() {
			var extendProperties = "photoCollectCategories=" + document.getElementsByName("photoCollectCategories")[0].value;
			if(document.getElementsByName("siteSelect")[1].checked) {
				if(document.getElementsByName("siteNames")[0].value=="") {
					alert('站点不能为空');
					return "ERROR";
				}
				extendProperties += "&siteIds=" + document.getElementsByName("siteIds")[0].value +
								    "&siteNames=" + document.getElementsByName("siteNames")[0].value +
								    (document.getElementsByName("containChildren")[0].checked ? "&containChildren=true" : "");
			}
			return extendProperties;
		}
		//选择图片分类
		function selectPhotoCollectCategories() {
			DialogUtils.openSelectDialog("cms/photocollect", "selectPhotoCollectCategory", 560, 360, true, "photoCollectCategories{type},photoCollectCategories{category|图片分类|100%}", "", "", "", ",", false, "siteId=" + editor.document.getElementsByName("siteId")[0].value);
		}
		function onSiteSelectChanged(mode) {
			document.getElementById("tdSelectedSites").style.display = document.getElementById("tdContainChildren").style.display = (mode=="current" ? "none" : "");
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap">站点选择：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0px">
					<tr>
						<td nowrap="nowrap"><ext:field property="siteSelect" onclick="onSiteSelectChanged(value)"/></td>
						<td id="tdSelectedSites" style="display:none; width:360px"><ext:field property="siteNames"/></td>
						<td id="tdContainChildren" style="display:none;" nowrap="nowrap"><ext:field property="containChildren"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr valign="middle">
			<td nowrap="nowrap">图片分类：</td>
			<td><ext:field property="photoCollectCategories"/></td>
		</tr>
	</table>
</ext:form>