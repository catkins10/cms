/*
 *颜色数组
 */
 var color=new Array("#00AABB","#FFAA00","#00FF21","#FF4400","#27255F",
                     "#3666B0","#8B0000","#2CA8E0","#E9967A","#8FBC8F",
                     "#FF1493","#FF00FF","#DCDCDC","#ADD8E6","#CD853F",
                     "#9ACD32","#000000","#5F9EA0","#696969","#008080");
 
 //饼图有关参数
 var angleArray=new Array();//所有调查的扇形弧度
 
 //折线图有关参数
 var pointArray=new Array();//所有调查的折线点区域
 var pointX=30;
 var pointY=20;//折线点原点定位于canvas图中（20,30）处
 var pointRadius=2;//折线点的半径
 
 //鼠标移动有关
 var mouseClientX=0;
 var mouseClientY=0;//鼠标在屏幕上的位置。用于判断是否移动完毕
 
 
 var timer;//各统计图的timeout定时器
 var noticeDivSourceElement=null;//鼠标移入提示div对象前的统计图对象
 
 /*
  *切换显示模式
  */
 function changeDisplayMode(){
   displayModes=document.getElementsByName("viewDisplayMode");
   for(i=0;i<displayModes.length;i++){
     document.getElementById("view"+displayModes[i].value).style.display=displayModes[i].checked?"":"none";
   }
   if(timer){
     clearTimeout(timer);
     timer=null;
   }
   hiddenNoticeDiv();
 }
 
 
 /*画图
 *扇形的描述:id@选项@票数@百分比
 */
function drowPie(canvasId){
  var canvas=document.getElementById(canvasId);
  if(!canvas){
    return;
  }
  var ctx;
  try{
    ctx=canvas.getContext('2d');
  }
  catch(e){
    return;//不支持html5
  }
  var votes=document.getElementsByName(canvasId.replace("_PieCanvas","_inquiryResult_Pie"));//获取的投票情况,对应同样数量的扇形
  var startPoint=0;   //每个扇形的起始弧度
  var inquiryAngle=new Array(2);//存放每个调查与他的弧度数组
  var angle=new Array(votes.length);  /*扇形的始末弧度*/
  for(var i=colorIndex=0;i<votes.length;i++,colorIndex++){  
        angle[i]=new Array(3);
        votePercent=parseFloat(votes[i].value.split("@")[3]);//百分比
        angle[i][0]=startPoint;//当前扇形起始弧度
        angle[i][1]=startPoint+Math.PI*2*votePercent;//当前扇形终止弧度
        angle[i][2]=votes[i].value;//存放对应扇形描述
        colorIndex=colorIndex>=color.length?1:colorIndex;
        ctx.fillStyle = color[colorIndex];   
        ctx.beginPath();   
        ctx.moveTo(canvas.width/2, canvas.height/2);//画扇形要移动到圆心  
        ctx.arc(canvas.width/2, canvas.height/2,100,startPoint,startPoint+Math.PI*2*votePercent,false);//顺时针   
        ctx.fill();   
        startPoint+=Math.PI*2*votePercent;   
        document.getElementById(votes[i].value.split("@")[0]+"_colorDiv_"+i).style.background=color[colorIndex];//设置对应块颜色
  }
  addEventToElement(canvas,'mousemove',onMouseMoveTimerForPie);
  addEventToElement(canvas,'mouseout',onMouseMoveOutForPie);
  inquiryAngle[0]=canvasId;//存放调查id
  inquiryAngle[1]=angle;//id对应的扇形弧度数组
  angleArray[angleArray.length]=inquiryAngle;
}

/*
 *鼠标移动定时器
 */
function onMouseMoveTimerForPie(event){
   event = window.event || event;//IE和Chrome下是window.event FF下是传入的第一个参数 
   var canvas = event.target || event.srcElement;//IE和Chrome下是srcElement FF下是target
   var x = event.clientX;  
   var y = event.clientY;
   mouseClientX=x;
   mouseClientY=y;
   if(timer){
      clearTimeout(timer);
      timer=null;
   }
   timer=setTimeout(function(){
                   if(x==mouseClientX && y==mouseClientY){//指定时间内都没移动鼠标，即鼠标已移动完毕。开始显示提示层
                       onMouseMoveForPie(canvas,mouseClientX,mouseClientY);
                       mouseClientX=0;
                       mouseClientY=0;
                   }
               },200);
}

/*
 *饼图的鼠标移动
 */
function onMouseMoveForPie(canvas,clientX,clientY) { 
    var bbox = canvas.getBoundingClientRect();//cancas相对屏幕的位置
    x = clientX - bbox.left-canvas.width/2;  
    y = clientY - bbox.top-canvas.height/2;  //获取鼠标在cancas上以圆心为原点的坐标.圆心为cancas中心
    if((x*x+y*y)>=100*100){//判断是否在圆内。公式(x-圆心x)^2+(Y-圆心Y)^2<=radius^2,大于的在圆外，等于在圆上，小于在圆内(直角三角形勾股定理：求斜边)
      hiddenNoticeDiv();
      return;
    }
    
    var ap=Math.atan2(y,x);//返回从 x 轴到点 (x,y) 之间的弧度,返回值-PI 到 PI 之间的值，是从 X 轴正向逆时针旋转到点 (x,y) 时经过的角度。
	if (ap<0){//本canvas扇形中不存在负数角。html中点坐标正负与数学中坐标轴象限Y坐标正负相反
		ap+=Math.PI*2;
	}
	var inquryArray=null;
	//查找对应调查id所拥有的弧度数组
	for (var i=0;i<angleArray.length ;i++ ){
	   if(angleArray[i][0]==canvas.id){
	     inquryArray=angleArray[i];
	     break;
	   }
	}
	if(inquryArray==null){
	  return;
	}
	var angle=inquryArray[1];//该调查下对应的扇形信息数组
	for (var i=0;i<angle.length ;i++ ){
		if (ap>=angle[i][0] && ap<=angle[i][1]){
		    noticeHtml=angle[i][2].split("@");
		    percent=Math.round(parseFloat(noticeHtml[3])*10000);//只留四位数
		    percent=percent/100;//最多保留2位小数
		    noticeHtml=noticeHtml[1]+"<br/>票数："+noticeHtml[2]+",比例："+percent+"%";
		    //蒙层div父对象是document，要算上滚动条位置。w3c标准下document.body.scrollLeft永远返回0
		    scrollTop=document.body.scrollTop==0?document.documentElement.scrollTop:document.body.scrollTop;
		    scrollLeft=document.body.scrollLeft==0?document.documentElement.scrollLeft:document.body.scrollLeft;
			createNoticeDiv(noticeHtml,clientX+15+scrollLeft,clientY+5+scrollTop);
			break;
		}
	}
	
	    
}  
 
/*
 *饼图的鼠标移除事件
 */ 
function onMouseMoveOutForPie(event) { 
    event = window.event || event;//IE和Chrome下是window.event FF下是传入的第一个参数 
    var canvas = event.target || event.srcElement;//IE和Chrome下是srcElement FF下是target
    var clientX = event.clientX;  
    var clientY = event.clientY;
    var bbox = canvas.getBoundingClientRect();//cancas相对屏幕的位置
    x = clientX - bbox.left-canvas.width/2;  
    y = clientY - bbox.top-canvas.height/2;  
    if((x*x+y*y)<100*100){//还在圆上，只是移动到提示层div上
      noticeDivSourceElement=canvas;
      return;
    }
    else{
      if(timer){
        clearTimeout(timer);
        timer=null;
      }
      hiddenNoticeDiv();
    }
 }
 /*
 *绑定柱状图相关鼠标移动事件
 */
function bindingHistogramEvent(id){
  histogramDiv=document.getElementById(id);
  if(!histogramDiv){
    return;
  }
  iqueryId=id.substring(0,id.indexOf("_"));
  results=document.getElementsByName(iqueryId+"_inquiryResult_Histogram");
  histogramDiv.style.width=(results.length*80)+"px";
  addEventToElement(histogramDiv,'mousemove',onMouseMoveTimerForHistogram);
  addEventToElement(histogramDiv,'mouseout',onMouseOutForHistogram);
}

/*
 *鼠标移动定时器
 */
function onMouseMoveTimerForHistogram(event){
   event = window.event || event;
   var target = event.target || event.srcElement;
   var x = event.clientX;  
   var y = event.clientY;
   mouseClientX=x;
   mouseClientY=y;
   offsetX=event.offsetX || event.layerX;
   offsetY=event.offsetY || event.layerY;
   if(timer){
      clearTimeout(timer);
      timer=null;
   }
   timer=setTimeout(function(){
                   if(x==mouseClientX && y==mouseClientY){
                       onMouseMoveForHistogram(target,mouseClientX,mouseClientY,offsetX,offsetY);
                       mouseClientX=0;
                       mouseClientY=0;
                   }
               },200);
}

 /*
 *柱状图的鼠标移动
 */
function onMouseMoveForHistogram(target,clientX,clientY,offsetX,offsetY) {
    if(offsetX<=0 || offsetY<=0 || offsetX>=target.offsetWidth || offsetY>=target.offsetHeight){
       hiddenNoticeDiv();
       document.getElementById(iqueryId+"_histogram_backLineDiv").style.display="none";
       document.getElementById(iqueryId+"_histogram_coverDiv").style.display="none";
       return;
    }
	scrollTop=document.body.scrollTop==0?document.documentElement.scrollTop:document.body.scrollTop;
	scrollLeft=document.body.scrollLeft==0?document.documentElement.scrollLeft:document.body.scrollLeft;
	id=target.id;
	iqueryId=id.substring(0,id.indexOf("_"));
	index=Math.ceil(offsetX/80)-1;
    content=document.getElementsByName(iqueryId+"_inquiryResult_Histogram")[index];//获取信息内容
    if(content){
      content=content.value;
      createNoticeDiv(content,clientX+15+scrollLeft,clientY+5+scrollTop);
      percent=content.substring(content.indexOf("比例：")+3,content.length-1);
      percent=Math.round(parseFloat(percent)*100);//##.##只留四位数
	  percent=percent/10000;//最多保留2位小数	
	  coverDiv=document.getElementById(iqueryId+"_histogram_coverDiv");
	  height=200*percent;
      coverDiv.style.height=height+"px";//柱形覆盖层
      coverDiv.style.left=(80*index+15+1)+"px";//还有表格边框1px
      coverDiv.style.top=(200-height)+"px";
      //coverDiv.style.display=percent==0?"none":"";
    }
    document.getElementById(iqueryId+"_histogram_backLineDiv").style.left=(80*index+40)+"px";
    document.getElementById(iqueryId+"_histogram_backLineDiv").style.display="";
 }
 
 /*
  *鼠标移出
  */
 function onMouseOutForHistogram(event) {
    event = window.event || event;
    var target = event.target || event.srcElement;
    id=target.id;
    iqueryId=id.substring(0,id.indexOf("_"));
    offsetX=event.offsetX || event.layerX;
    offsetY=event.offsetY || event.layerY;
    
    if(offsetX<=0|| offsetY<=0 || offsetX>=target.offsetWidth || offsetY>=target.offsetHeight){
      if(timer){
        clearTimeout(timer);
        timer=null;
      }
      hiddenNoticeDiv();
      document.getElementById(iqueryId+"_histogram_backLineDiv").style.display="none";
      document.getElementById(iqueryId+"_histogram_coverDiv").style.display="none";
    }
    else{//移动到内容提示div层上
      noticeDivSourceElement=target;
      return;
    }
 }
 
 /*折线画图
 *扇形的描述:id@选项@票数@百分比
 */
function drowLineChart(canvasId){
  var canvas=document.getElementById(canvasId);
  if(!canvas){
    return;
  }
  var ctx;
  try{
    ctx=canvas.getContext('2d');
  }
  catch(e){
    return;//不支持html5
  }
  var votes=document.getElementsByName(canvasId.replace("_LineChartCanvas","_inquiryResult_LineChart"));
  if(!votes || votes.length==0){
    return;
  }
  canvasWidth=votes.length*80+pointX;//canvas图宽度。每个投票项宽80像素，外加左侧预留30用来显示比例值
  canvas.width=canvasWidth;
  var inquiryPoint=new Array(2);//存放每个调查与他的折点数组

  //开始画折线图的区间线，分5份，高度占200px，每份为40px
  var lastLineY=pointY+40*5;//最后一行的Y坐标
  percentage=new Array(100,80,60,40,20,0);//纵坐标百分比标注
  ctx.beginPath(); 
  ctx.strokeStyle = "#DCDCDC";
  ctx.fillStyle = "#FFF"; 
  //绘制背景横线 。注：由于canvas画图原理，线框为1px时画出的图像看上去像2px。要-0.5。
  for(i=0;i<6;i++){
    var startY=pointY+40*i;
    ctx.moveTo(pointX-0.5, startY-0.5);
    ctx.lineTo(canvasWidth-0.5, startY-0.5);
    ctx.font = "normal 12px Arial"; 
    ctx.fillText(percentage[i]+"%", 0, startY); 
  }
  //绘制背景竖线
  for(i=0;i<=votes.length;i++){
      var startX=pointX+80*i;
      ctx.moveTo(startX-0.5, pointY-0.5); 
      ctx.lineTo(startX-0.5, lastLineY-0.5); 
  }
  ctx.stroke();//背景线绘制完成
  //横纵坐标轴加粗
  ctx.beginPath();
  ctx.lineWidth =2;
  ctx.strokeStyle="#BEBEBE";
  ctx.moveTo(pointX, lastLineY);
  ctx.lineTo(canvasWidth, lastLineY);
  ctx.moveTo(pointX, pointY);
  ctx.lineTo(pointX, lastLineY);
  ctx.stroke();
  
  previousPoint={startX:-1,startY:-1};//上一折线点坐标
  var points=new Array(votes.length);  /*折线点所在区域的坐标位置*/
  for(i=0;i<votes.length;i++){
     startX=pointX+80*i;
     startX=startX+40;//坐标点x坐标位置定位选项条中央
     percent=votes[i].value.split("@")[3];//百分比
     percent=parseFloat(percent);
     startY=lastLineY-lastLineY*percent;//y坐标须加上点的半径。 
     if(startY ==0 ){//100%
       startY=pointY;
     }
     else if(startY != lastLineY){
       startY=startY+pointRadius;
     }
     
     points[i]=new Array(3);
     points[i][0]=startX;
     points[i][1]=startY;
     points[i][2]=votes[i].value;//存放对应描述
        
     if(previousPoint.startX!=-1 && previousPoint.startY!=-1){ //链接上一节点
        ctx.beginPath(); 
        ctx.moveTo(previousPoint.startX, previousPoint.startY); 
        ctx.lineTo(startX,startY);
        ctx.lineWidth = 3; 
        ctx.strokeStyle = "#0D8DCE"; 
        ctx.stroke();
        ctx.closePath();
     }
     previousPoint.startX=startX;
     previousPoint.startY=startY;
  }
  //输出折线点
  for(i=0;i<points.length;i++){
    //折点大圆，描白边
     ctx.beginPath();  
     ctx.fillStyle = "#FFF"; 
     ctx.moveTo(points[i][0], points[i][1]); 
     ctx.arc(points[i][0], points[i][1],pointRadius+2,0,Math.PI*2,false);
     ctx.fill();
     ctx.closePath();
     
     ctx.beginPath();  
     ctx.fillStyle = "#2CA8E0"; 
     ctx.moveTo(points[i][0], points[i][1]); 
     ctx.arc(points[i][0], points[i][1],pointRadius,0,Math.PI*2,false);
     ctx.fill();//填充颜色==实心圆
     //ctx.strokeStyle = "#2CA8E0"; 
     //ctx.stroke();//描边==空心圆
     ctx.closePath();
  }
  
  inquiryPoint[0]=canvasId;
  inquiryPoint[1]=points;
  pointArray[pointArray.length]=inquiryPoint;
  //设置鼠标移动时候的背景交叉坐标线
  bgPointXDiv=document.getElementById(canvasId.replace("_LineChartCanvas","_bgPointXDiv"));
  bgPointYDiv=document.getElementById(canvasId.replace("_LineChartCanvas","_bgPointYDiv"));
  if(!bgPointXDiv || !bgPointYDiv){
     return;
  }
  bgPointXDiv.style.width=(canvasWidth-pointX)+"px";
  bgPointYDiv.style.height=(lastLineY-pointY)+"px";
  
  addEventToElement(canvas,'mousemove',onMouseMoveTimerForLineChart);
  addEventToElement(canvas,'mouseout',onMouseOutForLineChart);
 
}

/*
 *鼠标移动定时器
 */
function onMouseMoveTimerForLineChart(event){
   event = window.event || event;
   var target = event.target || event.srcElement;
   var x = event.clientX;  
   var y = event.clientY;
   mouseClientX=x;
   mouseClientY=y;
   if(timer){
      clearTimeout(timer);
      timer=null;
   }
   timer=setTimeout(function(){
                   if(x==mouseClientX && y==mouseClientY){
                       onMouseMoveForLineChart(target,mouseClientX,mouseClientY);
                       mouseClientX=0;
                       mouseClientY=0;
                   }
               },200);
}

/*
 *折线图的鼠标移动
 */
function onMouseMoveForLineChart(target,clientX,clientY) {
	scrollTop=document.body.scrollTop==0?document.documentElement.scrollTop:document.body.scrollTop;
	scrollLeft=document.body.scrollLeft==0?document.documentElement.scrollLeft:document.body.scrollLeft;
    var bbox = target.getBoundingClientRect();
    x = clientX - bbox.left;  
    y = clientY - bbox.top;
    if(x<=pointX || y<=pointY || y>=(pointY+40*5) || clientX>=bbox.left+target.width){//鼠标没在折线图区域
      hiddenLineChartNotice(target);
      return;
    }
    var inqueryPointResultArray=null;
    for(i=0;i<pointArray.length;i++){
      if(pointArray[i][0]==target.id){
        inqueryPointResultArray=pointArray[i][1];
        break;
      }
    }
    if(!inqueryPointResultArray){
      hiddenLineChartNotice(target.id);
      return;
    }
    for(i=0;i<inqueryPointResultArray.length;i++){
      if((inqueryPointResultArray[i][0]-40)<x && (inqueryPointResultArray[i][0]+40)>x){
            noticeHtml=inqueryPointResultArray[i][2].split("@");
		    percent=Math.round(parseFloat(noticeHtml[3])*10000);//只留四位数
		    percent=percent/100;//最多保留2位小数
		    noticeHtml=noticeHtml[1]+"<br/>票数："+noticeHtml[2]+",比例："+percent+"%";
		    scrollTop=document.body.scrollTop==0?document.documentElement.scrollTop:document.body.scrollTop;
		    scrollLeft=document.body.scrollLeft==0?document.documentElement.scrollLeft:document.body.scrollLeft;
			createNoticeDiv(noticeHtml,clientX+15+scrollLeft,clientY+5+scrollTop);

			bgPointXDiv=document.getElementById(target.id.replace("_LineChartCanvas","_bgPointXDiv"));
            bgPointYDiv=document.getElementById(target.id.replace("_LineChartCanvas","_bgPointYDiv"));
            if(bgPointXDiv){
               bgPointXDiv.style.top=(bbox.top+scrollTop+inqueryPointResultArray[i][1])+"px";
               bgPointXDiv.style.left=(bbox.left+scrollLeft+pointX)+"px";
               bgPointXDiv.style.display="";
            }
            if(bgPointYDiv){
               bgPointYDiv.style.top=(bbox.top+scrollTop+pointY)+"px";
               bgPointYDiv.style.left=(bbox.left+scrollLeft+inqueryPointResultArray[i][0])+"px";
               bgPointYDiv.style.display="";
            }
			break;
      }
    }
 }
 
 /*
  *折线图鼠标移出
  */
 function onMouseOutForLineChart(event){
   event = window.event || event;
   var target = event.target || event.srcElement;
   var bbox = target.getBoundingClientRect();
   x = event.clientX - bbox.left;  
   y = event.clientY - bbox.top;
   if(x<=pointX || y<=pointY || y>=(pointY+40*5) || event.clientX>=bbox.left+target.width){//鼠标没在折线图区域
      hiddenLineChartNotice(target);
      return;
   }
   else{
      noticeDivSourceElement=target;
      return;
   }
 }
 /*
  *隐藏折线图的提示信息
  */
 function hiddenLineChartNotice(target){
   //隐藏对应背景交叉线
   bgPointXDiv=document.getElementById(target.id.replace("_LineChartCanvas","_bgPointXDiv"));
   bgPointYDiv=document.getElementById(target.id.replace("_LineChartCanvas","_bgPointYDiv"));
   if(timer){
      clearTimeout(timer);
      timer=null;
   }
   setTimeout(function(){
               hiddenNoticeDiv();
               if(bgPointXDiv){
                 bgPointXDiv.style.display="none";
               }
               if(bgPointXDiv){
                 bgPointYDiv.style.display="none";
               }           
              },200);
   
 }
 /*
  *创建用于显示提示信息内容的div层
  */
 function createNoticeDiv(content,left,top){
    noticeDiv=document.getElementById("noticeDiv");
    if(!noticeDiv){
       noticeDiv=document.createElement("div");
       noticeDiv.id="noticeDiv";
       noticeDiv.style.cssText="color:#FFF; fonr-size:14px;"+
                               "z-index:1000; position:absolute; "+
                               "border:1px solid #FFF; padding:5px; line-height:20px; background-color : #2CA8E0;";
       //不透明度IE用filter，其他用opacity                        
       noticeDiv.style.filter = 'alpha(opacity=80);';
	   noticeDiv.style.opacity = 0.8;
	   addEventToElement(noticeDiv,'mousemove',function(event){//鼠标移动到提示层上，模拟一个事件，将其继续传递给源对象
	                if(!noticeDivSourceElement){
	                  return;
	                }
	                event = window.event || event;
	                if(document.createEvent){
	                  var MouseEvents = document.createEvent("MouseEvents"); 
　　                      MouseEvents.initMouseEvent("mousemove", true, true, document.defaultView, 0, event.screenX, event.screenY, event.clientX, event.clientY,false, false, false, false, 0, null); 
　　                      noticeDivSourceElement.dispatchEvent(MouseEvents);
	                }
	                else if(document.createEventObject){//ie
	                  /*当无事件可调用时创建一个模拟事件
	                  var MouseEvents = document.createEventObject(); 
　　                      MouseEvents.screenX=event.screenX; 
　　                      MouseEvents.screenY=event.screenY; 
　　                      MouseEvents.clientX=event.clientX; 
　　                      MouseEvents.clientY=event.clientY; 
　　                      MouseEvents.ctrlKey = false; 
　　                      MouseEvents.altKey = false; 
　　                      MouseEvents.shiftKey = false; 
　　                      MouseEvents.button = 0; 
　　                      noticeDivSourceElement.fireEvent("onmousemove", MouseEvents);
                      */
                      noticeDivSourceElement.fireEvent("onmousemove", event);
	                }
                 });
        addEventToElement(noticeDiv,'mouseout',function(event){
	                                  noticeDivSourceElement=null;
                 });
       document.body.appendChild(noticeDiv);
    }
    noticeDiv.innerHTML=content;
    var clientHeight = document.documentElement.clientHeight;//窗口高度
	clientHeight= clientHeight==0 || clientHeight==document.body.scrollHeight ? document.body.clientHeight : clientHeight;
    scrollTop=document.body.scrollTop==0?document.documentElement.scrollTop:document.body.scrollTop;
    newTop=0;
    newLeft=0;
    if(top-scrollTop+noticeDiv.offsetHeight>clientHeight){//显示内容超过屏幕高度，向上显示
      newTop=parseInt(top-noticeDiv.offsetHeight);
    }
    else{
      newTop=parseInt(top);
    }
    newLeft=parseInt(left);//转化为整数
    //无动画，直接显示
    //noticeDiv.style.left=newLeft+"px";//IE要接px
    //noticeDiv.style.top=newTop+"px";
    noticeDiv.style.display="";
    
    
    //动画移动效果
    proLeft = setInterval(function(){
                        if(noticeDiv.style.left==''){
                             noticeDiv.style.left="0px";
                        }
                        originalLeft=(noticeDiv.style.left+"").replace("px","");
                        originalLeft= parseInt(originalLeft);
                        if(Math.abs(originalLeft-newLeft)<1 && Math.abs(originalLeft-newLeft)!=0){//差距小于1px，不执行动画
                           noticeDiv.style.left = newLeft +"px";
                           clearInterval(proLeft);
                           return;
                        }
                        if(originalLeft < newLeft){//Math.ceil获取大于参数本身的最小整数.
		  	                 noticeDiv.style.left = (originalLeft + Math.ceil((newLeft-originalLeft)/5)) +"px";
		                }
		                else if(originalLeft > newLeft){
		                     noticeDiv.style.left = (originalLeft - Math.ceil((originalLeft-newLeft)/5)) +"px";
		                }
		                else{
		                    clearInterval(proLeft);
		                }  
                       },30);
     proTop = setInterval(function(){
                           if(noticeDiv.style.top==''){
                             noticeDiv.style.top="0px";
                           }
                           originalTop=(noticeDiv.style.top+"").replace("px","");
                           originalTop=parseInt(originalTop);
                           if(Math.abs(originalTop-newTop)<1 && Math.abs(originalTop-newTop)!=0){//差距小于1px，不执行动画
                              noticeDiv.style.top = newTop +"px";
                              clearInterval(proTop);
                              return;
                           }
                           if(originalTop < newTop){//Math.ceil获取大于参数本身的最小整数.
		  	                   noticeDiv.style.top = (originalTop + Math.ceil((newTop-originalTop)/5)) +"px";
		                   }
		                   else if(originalTop > newTop){
		                     noticeDiv.style.top = (originalTop - Math.ceil((originalTop-newTop)/5)) +"px";
		                   }
		                   else{
		                     clearInterval(proTop);
		                   }
		                   
                       },30);           
 }
 
  /*
  *隐藏信息提示层
  */
 function hiddenNoticeDiv(){
    noticeDiv=document.getElementById("noticeDiv");
    if(noticeDiv){
      noticeDiv.style.display="none";
    }
 }
 
 
 /*
  *为元素添加鼠标事件
  */
 function addEventToElement(element,eventName,func){
   if(!element || !eventName || !func){
     return;
   }
   if(element.attachEvent) { //IE
		try {
			element.attachEvent(eventName, func);
		}
		catch(e) {
			
		}
		try {
			element.attachEvent("on" + eventName, func);
		}
		catch(e) {
			
		}
	}
	else if(element.addEventListener) { //Gecko/W3C
		element.addEventListener(eventName, func, true);
	}
	else { // Opera (or old browsers)
		element["on" + eventName] = func;
	}
 }