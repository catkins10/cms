<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%String cssFile=request.getParameter("cssFile");
  if(cssFile!=null && !cssFile.isEmpty()){
    out.print("<LINK href=\""+cssFile+"\" rel=\"stylesheet\" type=\"text/css\">");
  }
 %>


<ext:form action="/inquiryResultRich">
<script charset="utf8" src="<%=request.getContextPath() %>/cms/inquiry/js/inquiryResult.js"></script>

<div style="padding-top:20px;padding-bottom:20px;line-height:20px">
显示模式：
<input type="radio" value="List" id="displayMode_List" name="viewDisplayMode" checked="checked" onclick="changeDisplayMode()"/>&nbsp;<label for="displayMode_List">列表</label>
<input type="radio" value="Pie" id="displayMode_Pie" name="viewDisplayMode" onclick="changeDisplayMode()"/>&nbsp;<label for="displayMode_Pie">饼图</label>
<input type="radio" value="Histogram" id="displayMode_Histogram" name="viewDisplayMode" onclick="changeDisplayMode()"/>&nbsp;<label for="displayMode_Histogram">柱状图</label>
<input type="radio" value="LineChart" id="displayMode_LineChart" name="viewDisplayMode" onclick="changeDisplayMode()"/>&nbsp;<label for="displayMode_LineChart">折线图</label>
</div>

<div id="viewList">
<ext:iterate id="inquiry" indexId="inquiryIndex" property="inquiries">
	<ext:equal value="1" property="isQuestionnaire">
		&nbsp;<ext:writeNumber name="inquiryIndex" plus="1"/>、<ext:write name="inquiry" property="descriptionText" maxCharCount="100" ellipsis="..."/>
	</ext:equal>
	<ext:equal value="0" property="isQuestionnaire">
		&nbsp;<ext:write  property="subject" maxCharCount="100" ellipsis="..."/>
	</ext:equal>
	<table width="100%" class="table" style="margin-top:3px; margin-bottom: 8px" border="1" cellpadding="0" cellspacing="0">
		<tr height="23px" valign="bottom">
			<td align="center" nowrap="nowrap" class="tdtitle" width="50px">序号</td>
			<td align="center" nowrap="nowrap" class="tdtitle" width="100%">选项</td>
			<td align="center" nowrap="nowrap" class="tdtitle" width="80px">得票数</td>
			<td align="center" nowrap="nowrap" class="tdtitle" width="300px">得票比例</td>
		</tr>
		<ext:iterate id="result" indexId="resultIndex" name="inquiry" property="results">
			<tr align="center">
				<td class="tdcontent" align="center"><ext:writeNumber name="resultIndex" plus="1"/></td>
				<td class="tdcontent" align="left">
					<ext:write name="result" property="option"/>
				</td>
				<td class="tdcontent" align="center" nowrap="nowrap">
					<ext:write name="result" property="voteNumber"/>
				</td>
				<td class="tdcontent" align="left" nowrap="nowrap" >
					<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
						<tr>
							<td style="width:43px"><ext:write name="result" property="votePercent" format="###.##%"/></td>
							<td nowrap="nowrap" style="width:250px">
								<div style="background:red;width:<ext:write name="result" property="votePercent" format="###.##%"/>">&nbsp;</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</ext:iterate>
	</table>
</ext:iterate>
</div>

<div id="viewPie" style="display:none">
<ext:iterate id="inquiry" indexId="inquiryIndex" property="inquiries">
	<ext:equal value="1" property="isQuestionnaire">
		&nbsp;<ext:writeNumber name="inquiryIndex" plus="1"/>、<ext:write name="inquiry" property="descriptionText" maxCharCount="100" ellipsis="..."/>
	</ext:equal>
	<ext:equal value="0" property="isQuestionnaire">
		&nbsp;<ext:write  property="subject" maxCharCount="100" ellipsis="..."/>
	</ext:equal>
	<table width="100%"  style="margin-top:3px; margin-bottom: 20px;" border="0" cellpadding="0" cellspacing="0">
		<tr height="250px">
			<td align="center" valign="middle" nowrap="nowrap"  width="300px">
			 <canvas id="<ext:write name="inquiry" property="id"/>_PieCanvas" width="250px" height="250px">您的浏览器不支持HTML5 canvas。<br/><br/>请使用IE9以上浏览器</canvas>
			 <ext:iterate id="result" indexId="resultIndex" name="inquiry" property="results">
			   	 <input name="<ext:write name="inquiry" property="id"/>_inquiryResult_Pie" type="hidden" value='<ext:write name="inquiry" property="id"/>_<ext:write name="result" property="id"/>@<ext:write name="result" property="option"/>@<ext:write name="result" property="voteNumber"/>@<ext:write name="result" property="votePercent"  format="###.####"/>'/>
			 </ext:iterate>
			</td>
			<td width="100%" valign="top" style="padding-left:20px;">
			<ext:iterate id="result" indexId="resultIndex" name="inquiry" property="results">
			 <div style="line-height: 23px;padding-left:10px;padding-top:10px;float:left;width:300px;" title="<ext:write name="result" property="option"/>：<ext:write name="result" property="voteNumber"/>票，比例：<ext:write name="result" property="votePercent" format="###.##%"/>">
			   <div style="width:30px;float:left;margin-right:5px" id="<ext:write name="inquiry" property="id"/>_<ext:write name="result" property="id"/>_colorDiv_<ext:writeNumber name="resultIndex" plus="0"/>">&nbsp;</div>
			   <div style="float:right;line-height: 23px;width:260px;overflow:hidden">
			     <ext:writeNumber name="resultIndex" plus="1"/>、<ext:write name="result" property="option" maxCharCount="24" ellipsis="..."/>：<ext:write name="result" property="voteNumber"/>票,占<ext:write name="result" property="votePercent" format="###.##%"/>
			   </div>
			 </div>	
		   </ext:iterate>
			</td>
		</tr>
	</table>
	<script type="text/javascript">
	    drowPie('<ext:write name="inquiry" property="id"/>_PieCanvas');//画图
	</script>
</ext:iterate>
</div>


<div id="viewHistogram" style="display:none;">
<ext:iterate id="inquiry" indexId="inquiryIndex" property="inquiries">
	<ext:equal value="1" property="isQuestionnaire">
	    <div style="margin-bottom: 10px">
		&nbsp;<ext:writeNumber name="inquiryIndex" plus="1"/>、<ext:write name="inquiry" property="descriptionText" maxCharCount="100" ellipsis="..."/>
	    </div>
	</ext:equal>
	<ext:equal value="0" property="isQuestionnaire">
		&nbsp;<ext:write  property="subject" maxCharCount="100" ellipsis="..."/>
	</ext:equal>
	<div style="position:relative;margin-top:10px; margin-bottom: 30px;margin-left:30px;" >
	   <table style="border-collapse: collapse;position:relative;" border="0" cellpadding="0" cellspacing="0">
	   	<tbody>
	   	 <tr> 
	   	   <ext:iterate id="result" indexId="resultIndex" name="inquiry" property="results">
             <td width="80px"  align="center" valign="bottom" 
             style="border-bottom: 2px #383E46 solid;<ext:equal name="resultIndex" value="0">border-left: 1px #383E46 solid;</ext:equal>">
               <div style="width:80px;height:200px;position:relative;">
                 <ext:notEqual name="result" property="votePercent" value="0.0">
                  <div style="background:#FE7F50;left:15px;position:absolute; bottom:0px;width:50px;height:<ext:write name="result" property="votePercent" format="###.##%"/>;">
                  </div>
                  </ext:notEqual>
		        </div>
		        <input type="hidden" name="<ext:write name="inquiry" property="id"/>_inquiryResult_Histogram"
		           value="<ext:write name="result" property="option"/><br/><ext:write name="result" property="voteNumber"/>票，比例：<ext:write name="result" property="votePercent" format="###.##%"/>"/>
		     
		     </td>
		   </ext:iterate>
		 </tr>
		 <tr>
		   <ext:iterate id="result" indexId="resultIndex" name="inquiry" property="results">
             <td height="30px" align="center" width="80px" valign="middle" style="word-break:break-all" >
               <ext:write name="result" property="option" maxCharCount="20" ellipsis="..."/>
		     </td>
		   </ext:iterate>
		  </tr>
		</tbody>
	   </table>
	   <div id="<ext:write name="inquiry" property="id"/>_histogram_coverDiv"
              style=" filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;background:#FFF;left:0px;position:absolute;width: 50px;display:none">
       </div>
	   <div id="<ext:write name="inquiry" property="id"/>_histogram_backLineDiv"
              style="background:#0D8DCE;left:0px;top:0px;position:absolute;height:200px;width: 2px;display:none">
       </div>
	   <div id="<ext:write name="inquiry" property="id"/>_histogram_resultDiv"
                  style="left:0px;position:absolute; top:0;height:200px;">
       </div>
    </div>
    <script type="text/javascript"> 
	     bindingHistogramEvent('<ext:write name="inquiry" property="id"/>_histogram_resultDiv');//办定柱状图div的鼠标移动事件
	</script>
</ext:iterate>
</div>


<div id="viewLineChart" style="display:none">
<ext:iterate id="inquiry" indexId="inquiryIndex" property="inquiries">
	<ext:equal value="1" property="isQuestionnaire">
		&nbsp;<ext:writeNumber name="inquiryIndex" plus="1"/>、<ext:write name="inquiry" property="descriptionText" maxCharCount="100" ellipsis="..."/>
	</ext:equal>
	<ext:equal value="0" property="isQuestionnaire">
		&nbsp;<ext:write  property="subject" maxCharCount="100" ellipsis="..."/>
	</ext:equal>
	<table style="margin-top:3px; margin-bottom: 20px;background-color: #383E46" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="left" valign="middle" nowrap="nowrap" style="padding-left: 10px;padding-top: 20px;padding-right: 40px">
			 <canvas id="<ext:write name="inquiry" property="id"/>_LineChartCanvas" height="225px">
			  <div style="color:#FFF;height:100px;font-size: 14px">您的浏览器不支持HTML5 canvas。<br/><br/>请使用IE9以上浏览器</div>
			 </canvas>
			 <ext:iterate id="result" indexId="resultIndex" name="inquiry" property="results">
			   	 <input name="<ext:write name="inquiry" property="id"/>_inquiryResult_LineChart" type="hidden" value='<ext:write name="inquiry" property="id"/>_<ext:write name="result" property="id"/>@<ext:write name="result" property="option"/>@<ext:write name="result" property="voteNumber"/>@<ext:write name="result" property="votePercent"  format="###.####"/>'/>
			 </ext:iterate>
			</td>
		</tr>
		<tr>
		   <td>
		     <table cellpadding="0" cellspacing="0" style="margin-left: 40px" id="<ext:write name="inquiry" property="id"/>_LineChartLableTable">
		       <tr>
		         <ext:iterate id="result" indexId="resultIndex" name="inquiry" property="results">
		            <td align="center" width="80px" height="30px" valign="middle" style="color:#FFF">
			   	    <ext:write name="result" property="option" ellipsis="..." maxCharCount="20"/>
			   	    </td>
			     </ext:iterate>
		       </tr>
		     </table>
		   </td>
		</tr>
	</table>
	<div id="<ext:write name="inquiry" property="id"/>_bgPointXDiv" style="height:1px;z-index:1000; position:absolute; background-color:#62bce9;display: none">&nbsp;</div>
	<div id="<ext:write name="inquiry" property="id"/>_bgPointYDiv" style="width:1px;z-index:1000; position:absolute; background-color:#62bce9;display: none">&nbsp;</div>
	
	<script type="text/javascript">
	    drowLineChart('<ext:write name="inquiry" property="id"/>_LineChartCanvas');//画图
	</script>
</ext:iterate>
</div>
</ext:form>