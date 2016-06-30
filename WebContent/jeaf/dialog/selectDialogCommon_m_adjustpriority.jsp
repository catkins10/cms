<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" height="100%" width="100%" cellspacing="0" cellpadding="1" style="table-layout:fixed;">
	<col width="100%">
	<col width="36px">
	<col width="0%">
<%	if(!"true".equals(request.getAttribute("hideConditionBar"))) {%>
		<tr valign="top">
			<td id="dialogSearchBar">
			    <table border="0" cellpadding="0" cellspacing="0">
       				<tr>
     					<td align="left" style="padding-top:3px" nowrap>指定过滤条件：</td>
						<td>
							<table border="0" width="100%" cellpadding="0" cellspacing="0" class="searchBox" style="table-layout:fixed">
								<tr>
									<td width="100%"><html:text styleClass="searchKey" property="key" style="background-color:transparent;border:0;height:100%;width:100%" onkeypress="if(event.keyCode==13){query();return false;}"/></td>
									<td onclick="query();" class="searchButton">&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
			<td></td>
			<td>
<%				if(request.getAttribute("rightConditionBar")!=null) {%>
					<jsp:include page="<%=(String)request.getAttribute("rightConditionBar")%>" />
<%				}%>
			</td>
		</tr>
<%	}%>
	<ext:equal property="multiSelect" value="false">
        <tr valign="top">
         	<td colspan="3" height="100%">
         		<div class="divData">
					<div id="divData" onselectstart="return false;" style="display: none;">
<%						if(request.getAttribute("dataPage")!=null) {%>
							<jsp:include page="<%=(String)request.getAttribute("dataPage")%>" />
<%						}%>
					</div>
				</div>
         	</td>
        </tr>
	</ext:equal>
	<ext:equal property="multiSelect" value="true">
        <tr valign="top">
			<td>
				<div style="width:100%; height:100%; padding:0px 0px 0px 0px; overflow: auto;" class="divResult">
					<div id="divResult" style="display: none;"></div>
				</div>
			</td>
         	<td valign="middle" align="center" onselectstart="return false;">
       			<div class="moveUp" title="上移" onclick="moveUp()"><span class="moveText">◇</span></div><br>
				<div class="moveDown" title="下移" onclick="moveDown()"><span class="moveText"><span class="moveDownText">◇</span></span></div>
			</td>
         	<td  height="100%" >
         		<div class="divData">
					<div id="divData" onselectstart="return false;" style="display: none;">
<%						if(request.getAttribute("dataPage")!=null) {%>
							<jsp:include page="<%=(String)request.getAttribute("dataPage")%>" />
<%						}%>
					</div>
				</div>
         	</td>
        </tr>
	</ext:equal>
</table>
<script>
	EventUtils.addEvent(window, 'load', function() {
		var divData = document.getElementById('divData');
		divData.parentNode.style.height = (divData.parentNode.offsetHeight-2) + "px";
		divData.style.display = '';
		var divResult = document.getElementById('divResult');
		if(divResult) {
			divResult.parentNode.style.height = divData.parentNode.style.height;
			divResult.style.display = '';
		}
	});
	
	
</script>