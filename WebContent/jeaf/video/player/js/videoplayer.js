VideoPlayer = function(parentElement, videoUrl, previewImageUrl, logoUrl, width, height, autoPlay, hideControls, videoDuration, terminalType) {
	this.parentElement = parentElement;
	this.videoUrl = videoUrl;
	this.previewImageUrl = previewImageUrl;
	this.logoUrl = logoUrl;
	this.width = width;
	this.height = height;
	this.autoPlay = autoPlay;
	this.hideControls = hideControls;
	this.videoDuration = videoDuration;
	this.terminalType = terminalType;
	window.videoPlayer = this;
	var flashVersion = BrowserInfo.getFlashVersion();
	if(BrowserInfo.isAndroid() || BrowserInfo.isIPhone() || BrowserInfo.isIPad() || !flashVersion || flashVersion<10) {
		this.createHtml5Player();
	}
	else {
		this.createFlashPlayer();
	}
};
VideoPlayer.prototype.createFlashPlayer = function() { //创建flash播放器,成功返回true
	var embed = document.createElement('embed');
	embed.setAttribute("allowScriptAccess", "always");
	embed.setAttribute("allowFullScreen", "true");
	embed.setAttribute("quality", "high");
	embed.setAttribute("bgcolor", "#FFF");
	embed.setAttribute("wmode", "transparent");
	embed.setAttribute("src", RequestUtils.getContextPath() + "/jeaf/video/player/ckplayer/ckplayer.swf");
	/**
	 * flashvars
	 * f s=0时地为普通的视频地址 s=1时是一个网址，网址里存放视频地址 s=2时是一个网址，网址里输出xml格式的视频地址 s=3时是一个swf文件地址，swf和播放器进行交互读取地址 
	 * a 当s>0时，a和f值拼出一个新的地址，在新的地址里读取视频地址， 
	 * s 调用方式，0=普通方法（f=视频地址），1=网址形式,2=xml形式，3=swf形式(s>0时f=网址，配合a来完成对地址的组装) 
	 * c 是否读取文本配置,0不是，1是，当=1时，播放器会自动读取和播放器相同名称的txml(默认是的ckplayer.xml)来进行进一步的配置 
	 * x 在c=1的时候，自定义调用xml风格路径，为空的话将调用跟播放器同名的xml文件。这个参数的作用是可以使用多套风格或设置的文件来进行随机调用 
	 * i 初始图片地址，就是在播放器默认是暂停或默认不加载的情况下先给一张图片遮在播放器前面，让其看起来不会一片黑，关于初始图片的大小的控制请参考配置文件里(ckplayer.js或ckplayer.xml)的ck.setup的第14个参数 
	 * d 暂停时播放的广告，swf/图片,多个用竖线隔开，图片要加链接地址，没有的时候留空就行 
	 * u 暂停时如果是图片的话，加个链接地址，如果没有就留空， 
	 * l 前置广告地址（也可以是以|隔开的数组），swf/图片/视频，多个用竖线隔开，图片和视频要加链接地址，关于前置广告 
	 * r 前置广告的链接地址，多个用竖线隔开，没有的留空 
	 * t 视频开始前播放swf/图片时的时间，多个用竖线隔开 
	 * y 这里是使用网址形式调用广告地址时使用，如果要使用这个参数，前置广告的l,r,t里至少要设置l的参数为空，播放器才会根据y的值进行调用 
	 * z 缓冲广告，只能放一个，swf格式 
	 * e 视频结束后的动作，0是调用js函数function playerstop(){}、这个参数有一篇单独的使用说明，1是循环播放，2是暂停播放并且不调用暂停广告，3是调用视频推荐列表的插件，4是清除视频流并调用js、功能和0差不多，5是暂停并且同时调用暂停广告 
	 * v 默认音量，0-100之间 
	 * p 视频默认0是暂停，1是播放 
	 * h 播放http视频流时采用何种拖动方法，=0不使用任意拖动，=1是使用按关键帧，=2是按时间点，=3是自动判断按什么(如果视频格式是.mp4就按关键时间，.flv就按关键帧)，=4也是自动判断(只要包含字符mp4就按mp4来，只要包含字符flv就按flv来) 
	 * q 视频流拖动时参考参数，默认是start 
	 * m 默认是否采用点击播放按钮后再加载视频，0不是，1是，这个参数的好处是一个页面上放多个视频时不需要加载所以的视频，点击哪个加载哪个 
	 * g 视频直接g秒开始播放，这个功能类似跳过片头的功能，当然这个功能还可以用js来实现 
	 * j 视频提前j秒结束，跳过片尾的功能 
	 * k 提示点时间，如 30|60鼠标经过进度栏30秒，60秒会提示n参数指定的相应的文字，这是以|隔开的一个数字数组
	 * 这个是鼠标经过进度栏上一些关键点时显示一个提示框，这些点需要自行设置，点的样式可以在配置文件里ck.pm_start参数设置 
	 * n 提示点文字，跟k配合使用，各提示文字以|隔开，所以提示文字里不能有|，(如：提示点1|提示点2) 
	 * o 在设置m=1，即默认不加载视频的时候向播放器传递该视频的时间，单位：秒，也可以不传 
	 * w 在设置m=1，即默认不加载视频的时候向播放器传递该视频的字节数，也可以不传 
	 * b 指定播放器是否进行交互，默认交互，b=1时不使用交互，所以在站外引用时需要设置ckplayer.xml里的里设置<flashvars>{b->1}</flashvars> 
	 **/
	embed.setAttribute("flashvars", "f=" + this.videoUrl + (this.previewImageUrl ? "&i=" + this.previewImageUrl : "") + "&t=0&c=0&b=1&p=" + (this.autoPlay ? 1 : 0));
	embed.setAttribute("width", this.width);
	embed.setAttribute("height", this.height);
	embed.setAttribute("align", "middle");
	embed.setAttribute("type", "application/x-shockwave-flash");
	embed.setAttribute("pluginspage", "http://www.macromedia.com/go/getflashplayer");
	this.parentElement.appendChild(embed);
	window.setTimeout(function() {
		embed.setAttribute("flashvars", "");
	}, 1000);
};
VideoPlayer.prototype.createHtml5Player = function() { //创建html5播放器
	var videoPlayer = this;
	var video = document.createElement('video');
	if(!video.canPlayType) { //不是html5
		return;
	}
	if(this.autoPlay) {
		video.setAttribute("autoplay", "true"); //视频在就绪后马上播放
	}
	if(this.previewImageUrl) {
		video.setAttribute("poster", this.previewImageUrl); //视频在页面加载时进行加载，并预备播放，如果使用 "autoplay"，则忽略该属性
	}
	video.style.backgroundColor = '#000';
	if(this.terminalType && this.terminalType!='computer' && this.terminalType!='client') {
		video.setAttribute("controls", "controls");
		this.hideControls = true;
	}
	video.setAttribute("webkit-playsinline", "webkit-playsinline");
	//video.setAttribute("-webkit-playsinline", "-webkit-playsinline");
	video.setAttribute("width", "100%"); //设置视频播放器的宽度 
	video.setAttribute("height", "100%"); //设置视频播放器的高度
	var source = document.createElement("source");
	source.setAttribute("webkit-playsinline", "");
	source.setAttribute("src", this.videoUrl);
	video.appendChild(source);
	this.videoSpan = document.createElement('span');
	this.videoSpan.style.display = 'block';
	this.videoSpan.style.position = 'relative';
	this.videoSpan.style.width = this.width + "px";
	this.videoSpan.style.height = this.height + "px";
	this.parentElement.appendChild(this.videoSpan);
	this.videoSpan.appendChild(video);
	this.videoSpan.onselectstart = function() {
		return false;
	};
	if(!this.hideControls) {
		new VideoController(this, video); //向用户显示控件，比如播放按钮
	}
	else { //隐藏控制栏
		video.onclick = function() {
			if(this.paused) {
				this.play();
			}
			else {
				this.pause();
			}
		};
	}
	window.setTimeout(function() {
		source.setAttribute("src", "");
	}, 1000);
};
VideoPlayer.prototype.isFullScreen = function() { //是否全屏
	return this.videoSpan.style.position=='absolute';
};
VideoPlayer.prototype.fullScreen = function() { //全屏
	this.videoSpan.style.position = 'absolute';
	this.videoSpan.style.zIndex = 1000;
	this.videoSpan.style.left = '0px';
	this.videoSpan.style.top = '0px';
	this.videoSpan.style.width = document.documentElement.clientWidth + 'px';
	this.videoSpan.style.height = document.documentElement.clientHeight + 'px';
};
VideoPlayer.prototype.exitFullScreen = function() { //退出全屏
	this.videoSpan.style.position = 'relative';
	this.videoSpan.style.zIndex = 0;
	this.videoSpan.style.left = 'auto';
	this.videoSpan.style.top = 'auto';
	this.videoSpan.style.width = this.width + 'px';
	this.videoSpan.style.height = this.height + 'px';
};
VideoPlayer.loadVideo = function(videoUrl) { //加载视频
	var id = (Math.random() + "").substring(2);
	document.write('<span id="' + id + '" style="display: inline-block;"></span>');
	var iframe = document.createElement('iframe');
	iframe.style.display = 'none';
	document.body.insertBefore(iframe, document.body.childNodes[0]);
	var doc = iframe.contentWindow.document;
	doc.open();
	doc.write('<form method="post" action="' + videoUrl + '&id=' + id + '&clientWidth=' + DomUtils.getClientWidth(document) + '"></form>');
	doc.close();
	doc.forms[0].submit();
};
window.ckstyle = function() { //ckplayer:播放器功能配置
    return {
		cpath: RequestUtils.getContextPath() + '/jeaf/video/player/ckplayer/skins/sina.swf', //播放器风格压缩包文件的路径，默认的是style.swf
		language: '', //播放器所使用的语言配置文件，需要和播放器在同目录下，默认是language.xml
        flashvars: '', //这里是用来做为对flashvars值的补充，除了c和x二个参数以外的设置都可以在这里进行配置
		setup: '1,' + //1、鼠标经过按钮是否使用手型，0普通鼠标，1手型鼠标，2是只有按钮手型，3是控制栏手型
			   '1,' + //2、是否支持单击暂停，0不支持，1是支持
			   '1,' + //3、是否支持双击全屏，0不支持，1是支持
			   '1,' + //4、在播放前置广告时是否同时加载视频，0不加载，1加载
			   '1,' + //5、广告显示的参考对象，0是参考视频区域，1是参考播放器区域
			   '2,' + //6、广告大小的调整方式,只针对swf和图片有效,视频是自动缩放的 0是自动调整大小，意思是说大的话就变小，小的话就变大 1是大的化变小，小的话不变 2是什么也不变，就这么大 3是跟参考对像(第5个控制)参数设置的一样宽高
			   '0,' + //7、前置广告播放顺序，0是顺序播放，1是随机播放
			   '1,' + //8、对于视频广告是否采用修正，0是不使用，1是使用，如果是1，则用户在网速慢的情况下会按设定的倒计时进行播放广告，计时结束则放正片（比较人性化），设置成0的话，则强制播放完广告才能播放正片
			   '0,' + //9、是否开启滚动文字广告，0是不开启，1是开启且不使用关闭按钮，2是开启并且使用关闭按钮，开启后将在加载视频的时候加载滚动文字广告
			   '0,' + //10、视频的调整方式 0是自动调整大小，意思是说大的话就变小，小的话就变大，同时保持长宽比例不变 1是大的化变小，小的话不变 2是什么也不变，就这么大 3是跟参考对像(pm_video的设置)参数设置的一样宽高
			   '0,' + //11、是否在多视频时分段加载，0不是，1是
			   '1,' + //12、缩放视频时是否进行平滑处理，0不是，1是
			   '200,' + //13、视频缓冲时间,单位：毫秒,建议不超过300
			   '3,' + //14、初始图片调整方式(0是自动调整大小，意思是说大的话就变小，小的话就变大，同时保持长宽比例不变 1是大的化变小，小的话不变 2是什么也不变，就这么大 3是跟pm_video参数设置的一样宽高
			   '2,' + //15、暂停广告调整方式(0是自动调整大小，意思是说大的话就变小，小的话就变大，同时保持长宽比例不变 1是大的化变小，小的话不变 2是什么也不变，就这么大 3是跟pm_video参数设置的一样宽
			   '1,' + //16、暂停广告是否使用关闭广告设置，0不使用，1使用
			   '0,' + //17、缓冲时是否播放广告，0是不显示，1是显示并同时隐藏掉缓冲图标和进度，2是显示并不隐藏缓冲图标
			   '1,' + //18、是否支持键盘空格键控制播放和暂停0不支持，1支持
			   '1,' + //19、是否支持键盘左右方向键控制快进快退0不支持，1支持
			   '1,' + //20、是否支持键盘上下方向键控制音量0不支持，1支持
			   '2,' + //21、播放器返回js交互函数的等级，0-2,等级越高，返回的参数越多 0是返回少量常用交互 1返回播放器在播放的时候的参数，不返回广告之类的参数 2返回全部参数 3返回全部参数，并且在参数前加上"播放器ID->"，用于多播放器的监听
			   '10,' + //22、快进和快退的秒数
			   '3,' + //23、界面上图片元素加载失败重新加载次数
			   '0,' + //24、开启加载皮肤压缩文件包的加载进度提示
			   '1,' + //25、使用隐藏控制栏时显示简单进度条的功能,0是不使用，1是使用，2是只在普通状态下使用
			   '2,' + //26、控制栏隐藏设置 0不隐藏，1全屏时隐藏，2都隐藏
			   '2000,' + //27、控制栏隐藏延时时间，即在鼠标离开控制栏后多少毫秒后隐藏控制栏
			   '0,' + //28、左右滚动时是否采用无缝，默认0采用，1是不采用
			   (window.videoPlayer.hideControls ? 2 : 0) + ',' + //29、0是正常状态，1是控制栏默认隐藏，播放状态下鼠标经过播放器显示控制栏，2是一直隐藏控制栏
			   '0,' + //30、在播放rtmp视频时暂停后点击播放是否采用重新链接的方式,这里一共分0-2三个等级
			   '0,' + //31、当采用网址形式(flashvars里s=1/2时)读取视频地址时是采用默认0=get方法，1=post方式
			   '1,' + //32、是否启用播放按钮和暂停按钮
			   '1,' + //33、是否启用中间暂停按钮
			   '1,' + //34、是否启用静音按钮
			   '1,' + //35、是否启用全屏按钮
			   '1,' + //36、是否启用进度调节栏
			   '1,' + //37、是否启用调节音量
			   '250,' + //38、计算时间的间隔，毫秒
			   '0,' + //39、前置logo至少显示的时间，单位：毫秒
			   '90', //40、前置视频广告的默认音量
        pm_bg: '0x000000,100,230,180', //播放器整体的背景配置，请注意，这里只是一个初始化的设置，如果需要真正的改动播放器的背景和最小宽高，需要在风格文件里找到相同的参数进行更改。1、整体背景颜色 2、背景透明度 3、播放器最小宽度4、播放器最小高度 这里只是初始化时的设置，最终加载完播放器后显示的效果需要在style.swf/style.xml里设置该参数
		mylogo: 'null', //视频加载前显示的logo文件，不使用设置成null，即ck.mylogo='null';
		pm_mylogo: '1,1,-100,-55', //视频加载前显示的logo文件(mylogo参数的)的位置 本软件所有的四个参数控制位置的方式全部都是统一的意思，如下1、水平对齐方式，0是左，1是中，2是右 2、垂直对齐方式，0是上，1是中，2是下 3、水平偏移量，举例说明，如果第1个参数设置成0左对齐，第3个偏移量设置成10，就是离左边10个像素，第一个参数设置成2，偏移量如果设置的是正值就会移到播放器外面，只有设置成负值才行，设置成-1，按钮就会跑到播放器外面 4、垂直偏移量 
		logo: window.videoPlayer.logoUrl, //默认右上角一直显示的logo，不使用设置成null，即ck.logo='null';
		pm_logo: '0,0,10,10', //播放器右上角的logo的位置 1、水平对齐方式，0是左，1是中，2是右  2、垂直对齐方式，0是上，1是中，2是下 3、水平偏移量 4、垂直偏移量 
		control_rel: 'related.swf,ckplayer/related.xml,0', //视频结束显示精彩视频的插件 1、视频播放结束后显示相关精彩视频的插件文件（注意，视频结束动作设置成3时(即var flashvars={e:3})有效），2、xml文件是调用精彩视频的示例文件，可以自定义文件类型（比如asp,php,jsp,.net只要输出的是xml格式就行）,实际使用中一定要注意第二个参数的路径要正确 3、第三个参数是设置配置文件的编码，0是默认的utf-8,1是gbk2312 
		control_pv: 'Preview.swf,105,2000', //视频预览插件 1、插件文件名称(该插件和上面的精彩视频的插件都是放在风格压缩包里的) 2、离进度栏的高(指的是插件的顶部离进度栏的位置) 3、延迟时间(该处设置鼠标经过进度栏停顿多少毫秒后才显示插件) 建议一定要设置延时时间，不然当鼠标在进度栏上划过的时候就会读取视频地址进行预览，很占资源 
		pm_repc: '', //视频地址替换符，该功能主要是用来做简单加密的功能，使用方法很简单，请注意，只针对f值是视频地址的时候有效，其它地方不能使用。具体的请查看http://www.ckplayer.com/manual.php?id=4#title_25
		pm_spac: '|', //视频地址间隔符，这里主要是播放多段视频时使用普通调用方式或网址调用方式时使用的。默认使用|，如果视频地址里本身存在|的话需要另外设置一个间隔符，注意，即使只有一个视频也需要设置。另外在使用rtmp协议播放视频的时候，如果视频存在多级目录的话，这里要改成其它的符号，因为rtmp协议的视频地址多级的话也需要用到|隔开流地址和实例地址 
		pm_fpac: 'file->f', //该参数的功能是把自定义的flashvars里的变量替换成ckplayer里对应的变量，默认的参数的意思是把flashvars里的file值替换成f值，因为ckplayer里只认f值，多个替换之间用竖线隔开
		pm_advtime: '2,0,-110,10,0,300,0', //前置广告倒计时文本位置，播放前置 广告时有个倒计时的显示文本框，这里是设置该文本框的位置和宽高，对齐方式的。一共7个参数，分别表示： 1、水平对齐方式，0是左对齐，1是中间对齐，2是右对齐 2、垂直对齐方式，0是上对齐，1是中间对齐，2是低部对齐 3、水平位置偏移量 4、垂直位置偏移量 5、文字对齐方式，0是左对齐，1是中间对齐，2是右对齐，3是默认对齐 6、文本框宽席 7、文本框高度 
		pm_advstatus: '1,2,2,-200,-40', //前置广告静音按钮，静音按钮只在是视频广告时显示，当然也可以控制不显示 1、是否显示0不显示，1显示 2、水平对齐方式 3、垂直对齐方式 4、水平偏移量 5、垂直偏移量
		pm_advjp: '1,1,2,2,-100,-40', //前置广告跳过广告按钮的位置 1、是否显示0不显示，1是显示 2、跳过按钮触发对象(值0/1,0是直接跳转,1是触发js:function ckadjump(){}) 3、水平对齐方式 4、垂直对齐方式 5、水平偏移量 6、垂直偏移量
		pm_padvc: '2,0,-10,-10', //暂停广告的关闭按钮的位置 1、水平对齐方式 2、垂直对齐方式 3、水平偏移量 4、垂直偏移量
		pm_advms: '2,2,-46,-56', //滚动广告关闭按钮位置 1、水平对齐方式 2、垂直对齐方式 3、水平偏移量 4、垂直偏移量
		pm_zip: '1,1,-20,-8,1,0,0', //加载皮肤压缩包时提示文字的位置 1、水平对齐方式，0是左对齐，1是中间对齐，2是右对齐 2、垂直对齐方式，0是上对齐，1是中间对齐，2是低部对齐 3、水平位置偏移量 4、垂直位置偏移量 5、文字对齐方式，0是左对齐，1是中间对齐，2是右对齐，3是默认对齐 6、文本框宽席 7、文本框高度
		pm_advmarquee: '1,2,50,-60,50,18,0,0x000000,50,0,20,1,15,2000', //滚动广告的控制，要使用的话需要在setup里的第9个参数设置成1
		advmarquee: escape(''), //该处是滚动文字广告的内容，如果不想在这里设置，就把这里清空并且在页面中使用js的函数定义function ckmarqueeadv(){return '广告内容'}
		mainfuntion:'', //当flashvars里s=3/4时，调用的函数包名称，默认为空，调用时间轴上的函数setAppObj
		flashplayer:'', //当flashvars里的s=3/4时，也可以把swf文件放在这里
		calljs:'ckplayer_status,ckadjump,playerstop,ckmarqueeadv', //跳过广告和播放结束时调用的js函数
		myweb: escape(''),
        cpt_lights: '0', //该处定义是否使用开关灯，和right.swf插件配合作用,使用开灯效果时调用页面的js函数function closelights(){};
		cpt_share: 'ckplayer/share.xml' //分享插件调用的配置文件地址 调用插件开始
		//cpt_list: ckcpt() //ckcpt()是本文件最上方的定义插件的函数
    };
};

VideoController = function(videoPlayer, videoElement) {
	this.videoPlayer = videoPlayer;
	this.videoElement = videoElement;
	this.create();
};
VideoController.prototype.create = function() { //创建
	var videoController = this;
	//创建表格,放置播放区域和控制栏
	var table = this.videoElement.ownerDocument.createElement('table');
	table.style.position = 'absolute';
	table.style.left = '0px';
	table.style.top = '0px';
	table.style.width = '100%';
	table.style.height = '100%';
	table.border = 0;
	table.cellPadding = 0;
	table.cellSpacing = 0;
	table.className = 'videoController';
	this.videoElement.parentNode.appendChild(table);
	
	//创建播放区域,放置播放图标、提示图标等
	var videoArea = table.insertRow(-1).insertCell(-1);
	videoArea.height = '100%';
	videoArea.style.cssText = 'text-align:center; position:relative;';
	
	//添加提示信息
	this.prompt = this.videoElement.ownerDocument.createElement('span');
	this.prompt.className = 'prompt';
	this.prompt.style.display = 'none';
	videoArea.appendChild(this.prompt);
	this.promptIcon = this.videoElement.ownerDocument.createElement('span');
	this.promptIcon.className = 'promptIcon';
	this.prompt.appendChild(this.promptIcon);
	this.promptText = this.videoElement.ownerDocument.createElement('span');
	this.promptText.className = 'promptText';
	this.prompt.appendChild(this.promptText);
	
	//创建控制栏
	var controllBarTd = table.insertRow(-1).insertCell(-1);
	this.controllBarContainer = this.videoElement.ownerDocument.createElement('div');
	controllBarTd.appendChild(this.controllBarContainer);
	this.controllBar = this.videoElement.ownerDocument.createElement('table');
	this.controllBar.border = 0;
	this.controllBar.cellPadding = 0;
	this.controllBar.cellSpacing = 0;
	this.controllBar.style.width = '100%';
	this.controllBar.style.tableLayout = 'fixed';
	this.controllBar.className = 'controllBar';
	this.controllBarContainer.appendChild(this.controllBar);
	
	//添加快退按钮
	var rewindButton = this.controllBar.insertRow(-1).insertCell(-1);
	rewindButton.className = 'rewindButton';
	rewindButton.title = '快退';

	//添加进度条
	var progressBarCell = this.controllBar.rows[0].insertCell(-1);
	progressBarCell.style.width = '100%';
	this.progressBar = new ProgressBar(progressBarCell, 'progressBar', 'playProgress', 'bufferProgress', 'thumb', 'thumb thumbOver');
	
	//添加快进按钮
	var forwardButton = this.controllBar.rows[0].insertCell(-1);
	forwardButton.className = 'forwardButton';
	forwardButton.title = '快进';
	
	var td = this.controllBar.insertRow(-1).insertCell(-1);
	td.colSpan = 3;
		      
	//添加播放按钮
	this.playButton = this.videoElement.ownerDocument.createElement('div');
	this.playButton.className = 'playButton';
	this.playButton.title = '播放';
	td.appendChild(this.playButton);
	
	//添加当前时间
	this.currentTime = this.videoElement.ownerDocument.createElement('div');
	this.currentTime.className = 'currentTime';
	this.currentTime.innerHTML = this._formatTime(this.videoElement.currentTime);
	td.appendChild(this.currentTime);
	
	//添加时间分隔符
	this.timeSeparator = this.videoElement.ownerDocument.createElement('div');
	this.timeSeparator.className = 'timeSeparator';
	this.timeSeparator.innerHTML = '/';
	td.appendChild(this.timeSeparator);
	
	//添加总时间
	this.duration = this.videoElement.ownerDocument.createElement('div');
	this.duration.className = 'duration';
	this.duration.innerHTML = this._formatTime(this.videoPlayer.videoDuration);
	td.appendChild(this.duration);
	
	//添加声音按钮
	var soundButton = this.videoElement.ownerDocument.createElement('div');
	soundButton.className = this.videoElement.muted ? 'muteButton' : 'soundButton';
	soundButton.title = '声音';
	td.appendChild(soundButton);
	
	this.volumeBar = new ProgressBar(td, 'volumeBar', 'volume', null, 'volumeThumb', 'volumeThumb volumeThumbOver');
	this.volumeBar.setProgress(this.videoElement.volume);
	
	//添加全屏按钮
	var fullScreenButton = this.videoElement.ownerDocument.createElement('div');
	fullScreenButton.className = 'fullScreenButton';
	fullScreenButton.title = '全屏';
	td.appendChild(fullScreenButton);
	
	//添加独立进度条,控制栏关闭时显示
	this.aloneProgressBarContainer = this.videoElement.ownerDocument.createElement('div');
	this.aloneProgressBarContainer.style.display = 'none';
	controllBarTd.appendChild(this.aloneProgressBarContainer);
	this.aloneProgressBar = new ProgressBar(this.aloneProgressBarContainer, 'progressBar progressBarAlone', 'playProgress', 'bufferProgress');
	
	this._processVideoEvents(); //处理播放器事件
	this._processPlayButtonEvents(); //处理播放按钮事件
	this._processRewindForwardButtonEvents(rewindButton, false); //处理快退事件
	this._processRewindForwardButtonEvents(forwardButton, true); //处理快进事件
	this._processFullScreenButtonEvents(fullScreenButton); //处理全屏按钮事件;
	this._processProgressBarEvents(); //处理进度条事件
	this._processSoundEvents(soundButton); //处理声音事件
	this._processVideoAreaEvents(videoArea); //数量视频区域事件
	
	table.onmouseover = function(event) {
		videoController.mouseX = event.screenX;
		videoController.mouseY = event.screenY;
	};
	table.onmouseout = function(event) {
		videoController.mouseX = -1;
		videoController.mouseY = -1;
	};
	table.onmousemove = function(event) {
		if(!videoController.mouseX || videoController.mouseX==-1) {
			return;
		}
		if(Math.abs(event.screenX-videoController.mouseX) < 5 && Math.abs(event.screenY-videoController.mouseY) < 5) {
			return;
		}
		videoController.mouseX = event.screenX;
		videoController.mouseY = event.screenY;
		videoController._showControllBar();
	};
	table.ontouchstart = function(event) {
		this.startX = event.touches[0].screenX;
		this.startY = event.touches[0].screenY;
		videoController.mouseX = -1;
		videoController.mouseY = -1;
		this.touchMoved = false;
		EventUtils.stopPropagation(event);
	};
	table.ontouchmove = function(event) {
		EventUtils.stopPropagation(event);
		if(Math.abs(this.startX - event.touches[0].screenX)>10 || Math.abs(this.startY - event.touches[0].screenY)>10) {
			this.touchMoved = true;
		}
	};
	table.ontouchend = function(event) {
		if(!this.touchMoved) {
			if(videoController.controllBar.style.marginTop=='0px') {
				videoController._closeControllBar();
			}
			else {
				videoController._showControllBar();
			}
		}
		EventUtils.stopPropagation(event);
	};
	this._showControllBar();
	this.controllBarContainer.style.height = CssUtils.getElementComputedStyle(this.controllBar, 'height');
};
VideoController.prototype._showControllBar = function() { //显示播放控制栏
	var videoController = this;
	if(this.hideControllBarTimer) {
		window.clearTimeout(this.hideControllBarTimer);
		this.hideControllBarTimer = null;
	}
	if(this.closeControllBarTimer) {
		this.closeControllBarTimer.cancel();
		this.closeControllBarTimer = null;
	}
	this.controllBarContainer.style.overflow = 'visible';
	this.progressBar.showThumb(true);
	this.controllBar.style.marginTop = '0px';
	this.aloneProgressBarContainer.style.display = 'none';
	this.controllBar.style.display = '';
	this.progressBar.setProgress(this.videoElement.currentTime / this.videoPlayer.videoDuration);
	this.hideControllBarTimer = window.setTimeout(function() {
		videoController._closeControllBar();
	}, 3000);
};
VideoController.prototype._closeControllBar = function() { //隐藏播放控制栏
	if(this.hideControllBarTimer) {
		window.clearTimeout(this.hideControllBarTimer);
		this.hideControllBarTimer = null;
	}
	if(this.closeControllBarTimer) {
		this.closeControllBarTimer.cancel();
		this.closeControllBarTimer = null;
	}
	if(this.videoElement.paused) {
		return;
	}
	var videoController = this;
	this.controllBarContainer.style.overflow = 'hidden';
	this.progressBar.showThumb(false);
	var marginTop = 0;
	this.closeControllBarTimer = new Timer();
	this.closeControllBarTimer.schedule(function() {
		if(marginTop < videoController.controllBar.offsetHeight) {
			videoController.controllBar.style.marginTop = marginTop + 'px';
			marginTop += Math.floor(videoController.controllBar.offsetHeight / 8);
		}
		else {
			videoController.closeControllBarTimer.cancel();
			videoController.closeControllBarTimer = null;
			videoController.controllBar.style.display = 'none';
			if(!videoController.videoPlayer.isFullScreen()) { //不是全屏
				videoController.aloneProgressBarContainer.style.display = '';
			}
		}
	}, 5, 30);
};
VideoController.prototype._processVideoEvents = function() { //处理播放器事件
	var videoController = this;
	window.setTimeout(function() {
		if(videoController.videoElement.readyState==0 && !videoController.hasError) {
			videoController._showLoading();
		}
	}, 1000);
	EventUtils.addEvent(this.videoElement, 'loadedmetadata', function() { //当元数据（比如分辨率和时长）被加载时运行的脚本
		videoController.duration.innerHTML = videoController._formatTime(videoController.videoPlayer.videoDuration);
	});
	EventUtils.addEvent(this.videoElement, 'loadeddata', function() { //客户端开始请求数据
		videoController.hasError = false;
		videoController._hidePrompt();
	});
	EventUtils.addEvent(this.videoElement, 'timeupdate', function(event) { //当播放位置改变时（比如当用户快进到媒介中一个不同的位置时）运行的脚本
		videoController.progressBar.setProgress(this.currentTime / videoController.videoPlayer.videoDuration);
		videoController.aloneProgressBar.setProgress(this.currentTime / videoController.videoPlayer.videoDuration);
		videoController.currentTime.innerHTML = videoController._formatTime(videoController.videoElement.currentTime);
	});
	EventUtils.addEvent(this.videoElement, 'progress', function() { //客户端正在请求数据
		if(this.duration && this.buffered.length>0) {
			var ratio = this.buffered.end(this.buffered.length - 1) / videoController.videoPlayer.videoDuration;
			videoController.progressBar.setSecondProgress(ratio);
			videoController.aloneProgressBar.setSecondProgress(ratio);
		}
	});
	EventUtils.addEvent(this.videoElement, 'pause', function() { //暂停
		videoController._setPlayButtonStyle();
		if(window.top.cancelKeepScreenOn) {
			window.top.cancelKeepScreenOn();
		}
	});
	EventUtils.addEvent(this.videoElement, 'playing', function() { //开始播放
		videoController._setPlayButtonStyle();
		videoController._showControllBar();
		if(window.top.keepScreenOn) {
			window.top.keepScreenOn();
		}
	});
	EventUtils.addEvent(this.videoElement, 'seeking', function() { //定位中
		window.setTimeout(function() {
			if(videoController.videoElement.seeking) {
				videoController._showLoading();
			}
		}, 1000);
	});
	EventUtils.addEvent(this.videoElement, 'seeked', function() { //定位完毕
		videoController.hasError = false;
		videoController._hidePrompt();
	});
	EventUtils.addEvent(this.videoElement, 'ended', function() { //播放结束
		videoController._setPlayButtonStyle();
		videoController.videoElement.pause();
		videoController._showControllBar();
		if(window.top.cancelKeepScreenOn) {
			window.top.cancelKeepScreenOn();
		}
	});
	EventUtils.addEvent(this.videoElement, 'error', function() { //请求数据时遇到错误
		videoController.hasError = true;
		videoController._showPrompt('warningIcon', '视频加载失败', false);
	});
	EventUtils.addEvent(this.videoElement, 'stalled', function() { //在浏览器不论何种原因未能取回媒介数据时运行的脚本
		
	});
};
VideoController.prototype._processPlayButtonEvents = function() { //处理播放按钮事件
	var videoController = this;
	this.playButton.onmouseover = this.playButton.ontouchstart = function(event) {
		videoController.mouseOverPlayButton = true;
		videoController._setPlayButtonStyle();
	};
	this.playButton.onmouseout = this.playButton.ontouchend = function(event) {
		videoController.mouseOverPlayButton = false;
		videoController._setPlayButtonStyle();
	};
	this.playButton.onclick = function() {
		if(videoController.videoElement.readyState==0) {
			return;
		}
		if(videoController.videoElement.paused) {
			videoController.videoElement.play();
		}
		else {
			videoController.videoElement.pause();
		}
	};
};
VideoController.prototype._setPlayButtonStyle = function() { //处理播放按钮事件
	this.playButton.className = 'playButton' + (this.videoElement.paused ? '' : ' playButtonPaused') + (this.mouseOverPlayButton ? (this.videoElement.paused ? ' playButtonOver' : ' playButtonPausedOver') : '');
	this.playButton.title = this.videoElement.paused ? '播放' : '暂停';
};
VideoController.prototype._processRewindForwardButtonEvents = function(button, isForward) { //处理快退、快进事件
	var videoController = this;
	button.onmouseover = function(event) {
		button.className = (isForward ? 'forwardButton forwardButtonOver' : 'rewindButton rewindButtonOver');
	};
	button.onmouseout = function(event) {
		button.className = (isForward ? 'forwardButton' : 'rewindButton');
	};
	button.onclick = function() {
		videoController.videoElement.currentTime = Math.max(0, Math.min(videoController.videoElement.currentTime + (isForward ? 1 : -1) * Math.min(10, videoController.videoPlayer.videoDuration / 30), videoController.videoPlayer.videoDuration));
	};
};
VideoController.prototype._processFullScreenButtonEvents = function(fullScreenButton) { //处理全屏按钮事件
	var videoController = this;
	fullScreenButton.onmouseover = fullScreenButton.ontouchstart = function(event) {
		videoController.mouseOverFullScreenButton = true;
		videoController._setFullScreenButtonStyle(fullScreenButton);
	};
	fullScreenButton.onmouseout = fullScreenButton.ontouchend = function(event) {
		videoController.mouseOverFullScreenButton = false;
		videoController._setFullScreenButtonStyle(fullScreenButton);
	};
	fullScreenButton.onclick = function() {
		if(window.top.client) {
			var func = function(event) {
				window.frameElement.style.width = document.body.style.width = window.top.document.documentElement.clientWidth + 'px';
				window.frameElement.style.height = document.body.style.height = window.top.document.documentElement.clientHeight + 'px';
				videoController._doFullScreen(fullScreenButton);
				EventUtils.removeEvent(window.top, 'resize', func);
				window.top.document.body.style.visibility = 'visible';
			};
			EventUtils.addEvent(window.top, 'resize', func);
			window.top.document.body.style.visibility = 'hidden';
			if(videoController.videoPlayer.isFullScreen()) {
				window.top.exitFullScreen();
			}
			else {
				window.top.requestFullScreen(videoController.videoPlayer.width>videoController.videoPlayer.height);
			}
		}
		else {
			if(videoController.videoPlayer.isFullScreen()) { //当前是全屏
				PageUtils.exitFullScreen();
				videoController.videoPlayer.exitFullScreen(); //退出全屏
				EventUtils.removeEvent(window.top, 'resize', videoController.func);
				return;
			}
			videoController.func = function() {
				if(PageUtils.isFullScreen()) {
					videoController.videoPlayer.fullScreen(); //全屏
				}
				else {
					videoController.videoPlayer.exitFullScreen(); //退出全屏
					EventUtils.removeEvent(window.top, 'resize', videoController.func);
				}
			};
			EventUtils.addEvent(window.top, 'resize', videoController.func);
			PageUtils.requestFullScreen();
		}
	};
};
VideoController.prototype._setFullScreenButtonStyle = function(fullScreenButton) {
	var fullScreen = this.videoPlayer.isFullScreen();
	fullScreenButton.className = 'fullScreenButton' + (fullScreen ? ' fullScreenButtonShrink' : '') + (this.mouseOverFullScreenButton ? (fullScreen ? ' fullScreenButtonShrinkOver' : ' fullScreenButtonOver') : '');
	fullScreenButton.title = fullScreen ? '退出全屏' : '全屏';
};
VideoController.prototype._doFullScreen = function(fullScreenButton) {
	if(this.videoPlayer.isFullScreen()) {
		this.videoPlayer.exitFullScreen();
	}
	else {
		this.videoPlayer.fullScreen();
	}
	this.progressBar.setProgress(this.videoElement.currentTime / this.videoPlayer.videoDuration);
	this._setFullScreenButtonStyle(fullScreenButton);
};
VideoController.prototype._processProgressBarEvents = function() { //处理进度条事件
	var videoController = this;
	this.progressBar.onSeeked = function(ratio) {
		videoController.videoElement.currentTime = ratio * videoController.videoPlayer.videoDuration; //视频定位
	};
	this.controllBar.onmousemove = function(event) {
		videoController.mouseOverControllBar = true;
	};
	this.controllBar.onmouseout= function(event) {
		videoController.mouseOverControllBar = false;
	};
};
VideoController.prototype._processSoundEvents = function(soundButton) { //处理声音事件
	var videoController = this;
	var setSoundButtonStyle = function() {
		soundButton.className = 'soundButton' + (videoController.videoElement.muted ? ' soundButtonMute' : '') + (videoController.mouseOverSoundButton ? (videoController.videoElement.muted ? ' soundButtonMuteOver' : ' soundButtonOver') : '');
	};
	this.volumeBar.onSeeking = function(ratio) {
		videoController.videoElement.volume = ratio;
		videoController.videoElement.muted = false;
		setSoundButtonStyle();
	};
	soundButton.onmouseover = function(event) {
		videoController.mouseOverSoundButton = true;
		setSoundButtonStyle();
	};
	soundButton.onmouseout = function(event) {
		videoController.mouseOverSoundButton = false;
		setSoundButtonStyle();
	};
	soundButton.onclick = function() {
		videoController.videoElement.muted = !videoController.videoElement.muted;
		setSoundButtonStyle();
		videoController.volumeBar.setProgress(videoController.videoElement.muted ? 0 : videoController.videoElement.volume);
	};
};
VideoController.prototype._processVideoAreaEvents = function(videoArea) { //处理视频区域事件
	if(!window.top.setVolume || !window.top.setBrightness) { //有设置音量和亮度的方法
		return;
	}
	var videoController = this;
	videoArea.ontouchstart = function(event) {
		this.startX = event.touches[0].pageX;
		this.startY = event.touches[0].pageY;
		videoController.status = null;
		this.volume = -1;
		this.brightness = -1;
	};
	videoArea.ontouchmove = function(event) {
		var x = event.touches[0].pageX;
		var y = event.touches[0].pageY;
		if(videoController.status==null) {
			if(Math.abs(this.startX - x) > 10) {
				videoController.status = 'forwardRewind';
				this.startTime = videoController.videoElement.currentTime;
			}
			else if(Math.abs(this.startY - y) > 10) {
				if(this.startX > DomUtils.getAbsolutePosition(this, null, false).left + this.offsetWidth / 2) {
					videoController.status = 'volume';
					window.top.getVolume(function(volume) {
						videoArea.volume = volume;
					});
				}
				else {
					videoController.status = 'brightness';
					window.top.getBrightness(function(brightness) {
						videoArea.brightness = brightness;
					});
				}
			}
		}
		if(videoController.status=='forwardRewind') { //快进或快退
			this.time = Math.max(0, Math.min(videoController.videoPlayer.videoDuration, this.startTime + (x - this.startX) / this.offsetWidth / 2 * videoController.videoPlayer.videoDuration));
			videoController._showPrompt((x > this.startX ? 'forwardIcon' : 'rewindIcon'), videoController._formatTime(this.time) + ' / ' + videoController._formatTime(videoController.videoPlayer.videoDuration), false);
		}
		else if(Math.abs(this.startY - y) > 3 && videoController.status=='volume' && this.volume!=-1) { //音量
			var volume = Math.max(0, Math.min(1, this.volume + (this.startY - y + (this.startY > y ? -3 : 3)) / this.offsetHeight));
			videoController._showPrompt('volumeIcon', Math.floor(volume * 100) + '%', false);
			if(volume==0 || volume==1) {
				this.startY = y;
				this.volume = volume;
			}
			window.top.setVolume(volume);
		}
		else if(Math.abs(this.startY - y) > 3 && videoController.status=='brightness' && this.brightness!=-1) { //亮度
			var brightness = Math.max(0, Math.min(1, this.brightness + (this.startY - y + (this.startY > y ? -3 : 3)) / this.offsetHeight));
			videoController._showPrompt('brightnessIcon', Math.floor(brightness * 100) + '%', false);
			if(brightness==0 || brightness==1) {
				this.startY = y;
				this.brightness = brightness;
			}
			window.top.setBrightness(brightness);
		}
	};
	videoArea.ontouchend = function(event) {
		if(videoController.status=='forwardRewind') { //快进或快退
			videoController.videoElement.currentTime = this.time;
		}
		videoController.status = null;
		videoController._hidePrompt();
	};
	videoArea.onclick = function(event) {
		if(videoController.videoElement.paused) {
			videoController.videoElement.play();
		}
	};
};
VideoController.prototype._showPrompt = function(promptIconClassName, promptText, rotate) { //显示提示信息
	this.promptIcon.className = 'promptIcon ' + promptIconClassName;
	this.promptText.innerHTML = promptText;
	if(rotate) {
		CssUtils.rotate(this.promptIcon, 20, 80, -1);
	}
	else {
		CssUtils.stopRotate(this.promptIcon);
	}
	this.prompt.style.display = '';
};
VideoController.prototype._hidePrompt = function() { //隐藏提示信息
	this.prompt.style.display = 'none';
	CssUtils.stopRotate(this.promptIcon);
};
VideoController.prototype._showLoading = function() {
	if(this.status!=null) {
		return;
	}
	this._showPrompt('loadingIcon', '正在加载...', true);
};
VideoController.prototype._formatTime = function(seconds) {
	var hours = Math.floor(seconds / 3600);
	var minutes = Math.floor(seconds % 3600 / 60);
	seconds = Math.floor(seconds % 60);
	return (this.videoElement.duration<3600 ? '' : (hours<10 ? '0' : '') + hours + ':') + (minutes<10 ? '0' : '') + minutes + ':' + (seconds<10 ? '0' : '') + seconds;
};