package com.yuanluesoft.microblog.platform.sina;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.Mime;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.HttpResponse;
import com.yuanluesoft.microblog.model.Microblog;
import com.yuanluesoft.microblog.model.MicroblogImage;
import com.yuanluesoft.microblog.model.MicroblogPlatformParameterDefine;
import com.yuanluesoft.microblog.model.MicroblogUser;
import com.yuanluesoft.microblog.platform.MicroblogPlatform;
import com.yuanluesoft.microblog.pojo.MicroblogAccount;
import com.yuanluesoft.microblog.service.MicroblogService;

/**
 * 新浪微博平台
 * @author linchuan
 *
 */
public class MicroblogPlatformSinaImpl extends MicroblogPlatform {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.platform.MicroblogPlatform#getName()
	 */
	public String getName() {
		return "新浪微博";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.platform.MicroblogPlatform#getParameterDefines()
	 */
	public List getParameterDefines() {
		List defines = new ArrayList();
		defines.add(new MicroblogPlatformParameterDefine("App Key", "appKey", null, null, false));
		defines.add(new MicroblogPlatformParameterDefine("App Secret", "appSecret", null, null, false));
		return defines;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.platform.MicroblogPlatform#issueMicroblog(com.yuanluesoft.microblog.pojo.MicroblogAccount, java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	public String issueMicroblog(MicroblogAccount account, String range, String[] groupIds, String content, List images) throws ServiceException {
		String accessToken = getAccessToken(account); //获取令牌
		String[] listIds;
		int visible = 0; //微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0。
		if(MicroblogService.MICROBLOG_ISSUE_RANGE_GROUPS.equals(range)) {
			if(groupIds==null || groupIds.length==0) {
				return null;
			}
			visible = 3;
			listIds = groupIds;
		}
		else {
			listIds = new String[]{""};
			if(MicroblogService.MICROBLOG_ISSUE_RANGE_SELF.equals(range)) {
				visible = 1;
			}
		}
		byte[] imageBytes = null;
		String mime = null;
		if(images!=null && !images.isEmpty()) { //没有图片
			Image image = (Image)images.get(0);
			imageBytes = FileUtils.readFile(image.getFilePath());
			mime = Mime.getMimeType(image.getFilePath());
		}
		String ids = null;
		for(int i=0; i<listIds.length; i++) {
			JSONObject json;
			if(imageBytes==null) { //没有图片
				NameValuePair[] nameValuePairs = new NameValuePair[visible==3 ? 4 : 3]; 
				nameValuePairs[0] = new NameValuePair("access_token", accessToken); //采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得
				nameValuePairs[1] = new NameValuePair("status", content); //要发布的微博文本内容，必须做URLencode，内容不超过140个汉字
				nameValuePairs[2] = new NameValuePair("visible", "" + visible); //微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0。
				if(visible==3) {
					nameValuePairs[3] = new NameValuePair("list_id", "" + listIds[i]); //微博的保护投递指定分组ID，只有当visible参数为3时生效且必选
				}
				json = (JSONObject)openRequest("https://api.weibo.com/2/statuses/update.json", true, nameValuePairs);
			}
			else {
				Part[] parts =  new Part[visible==3 ? 5 : 4];
				parts[0] = new StringPart("access_token", accessToken); //采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得
				parts[1] = new StringPart("status", content, "utf-8"); //要发布的微博文本内容，必须做URLencode，内容不超过140个汉字
				parts[2] = new StringPart("visible", "" + visible); //微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0。 
				if(visible==3) {
					parts[3] = new StringPart("list_id", "" + listIds[i]); //微博的保护投递指定分组ID，只有当visible参数为3时生效且必选
				}
				parts[visible==3 ? 4 : 3] = new HttpUtils.ByteArrayPart(imageBytes, "utf-8", "pic", mime);
				json = (JSONObject)openRequest("https://upload.api.weibo.com/2/statuses/upload.json", true, parts);
			}
			if(json==null) {
				throw new ServiceException();
			}
			String errorCode = (String)json.get("error_code");
			if(errorCode!=null) {
				throw new ServiceException((String)json.get("error"));
			}
			ids = (ids==null ? "" : ids + ",") + json.get("id");
		}
		return ids;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.platform.MicroblogPlatform#deleteMicroblog(com.yuanluesoft.microblog.pojo.MicroblogAccount, java.lang.String)
	 */
	public void deleteMicroblog(MicroblogAccount account, String microblogId) throws ServiceException {
		String accessToken = getAccessToken(account); //获取令牌
		NameValuePair[] nameValuePairs = new NameValuePair[2]; 
		nameValuePairs[0] = new NameValuePair("access_token", accessToken); //采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得
		nameValuePairs[1] = new NameValuePair("id", microblogId); //需要删除的微博ID
		JSONObject json = (JSONObject)openRequest("https://api.weibo.com/2/statuses/destroy.json", true, nameValuePairs);
		if(json==null) {
			throw new ServiceException();
		}
		String errorCode = (String)json.get("error_code");
		if(errorCode!=null) {
			throw new ServiceException((String)json.get("error"));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.platform.MicroblogPlatform#readMicroblogs(com.yuanluesoft.microblog.pojo.MicroblogAccount, int, int)
	 */
	public List readMicroblogs(MicroblogAccount account, int pageIndex, int pageRows) throws ServiceException {
		String accessToken = getAccessToken(account); //获取令牌
		String url = "https://api.weibo.com/2/statuses/user_timeline.json" + 
					 "?access_token=" + accessToken + //采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得
					 "&count=" + Math.min(pageRows, 100) + //单页返回的记录条数，最大不超过100，超过100以100处理，默认为20
					 "&page=" + (pageIndex + 1); //返回结果的页码，默认为1。
		JSONObject json = (JSONObject)openRequest(url, false, null);
		if(json==null) {
			throw new ServiceException();
		}
		String errorCode = (String)json.get("error_code");
		if(errorCode!=null) {
			throw new ServiceException((String)json.get("error"));
		}
		JSONArray statuses = (JSONArray)json.get("statuses");
		if(statuses==null || statuses.isEmpty()) {
			return null;
		}
		List microblogs = new ArrayList();
		for(Iterator iterator = statuses.iterator(); iterator.hasNext();) {
			JSONObject statuse = (JSONObject)iterator.next();
			microblogs.add(generateMicroblog(account, statuse)); //生成微博对象
		}
		return microblogs;
	}
	
	/**
	 * 生成微博对象
	 * @param statuse
	 * @return
	 */
	private Microblog generateMicroblog(MicroblogAccount account, JSONObject statuse) throws ServiceException {
		if(statuse==null) {
			return null;
		}
		Microblog microblog = new Microblog();
		microblog.setId("" + statuse.get("id")); //微博ID
		microblog.setContent(resetContent(account, (String)statuse.get("text"))); //微博信息内容
		microblog.setSource((String)statuse.get("source")); //微博来源
		microblog.setCreated(parseTimestamp((String)statuse.get("created_at"))); //微博创建时间,  微博创建时间, Mon May 26 12:41:16 +0800 2014 
		//microblog.setGeo(geo); //地理信息字段 详细 
		microblog.setUser(generateMicroblogUser(account, (JSONObject)statuse.get("user"))); //用户
		microblog.setRetweetedMicroblog(generateMicroblog(account, (JSONObject)statuse.get("retweeted_status"))); //被转发的原微博信息字段，当该微博为转发微博时返回 
		microblog.setRepostsCount(Integer.parseInt("" + statuse.get("reposts_count"))); //转发数
		microblog.setCommentsCount(Integer.parseInt("" + statuse.get("comments_count"))); //评论数 
		microblog.setAttitudesCount(Integer.parseInt("" + statuse.get("attitudes_count"))); //表态数
		String visible = "" + ((JSONObject)statuse.get("visible")).get("type"); //0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号 
		microblog.setVisible("0".equals(visible) ? "all" : ("3".equals(visible) ? "groups" : "private")); //微博的可见性及指定可见分组信息,all/普通微博，private/私密微博，groups/指定分组微博 
		JSONArray picUrls = (JSONArray)statuse.get("pic_urls");
		List images = new ArrayList();
		for(Iterator iterator = picUrls==null ? null : picUrls.iterator(); iterator!=null && iterator.hasNext();) {
			JSONObject picUrl = (JSONObject)iterator.next();
			MicroblogImage microblogImage = new MicroblogImage();
			microblogImage.setThumbnailUrl((String)picUrl.get("thumbnail_pic")); //缩略图片地址
			microblogImage.setMiddleUrl(microblogImage.getThumbnailUrl().replaceFirst("/thumbnail/", "/bmiddle/")); //中等尺寸图片地址 
			microblogImage.setOriginalUrl(microblogImage.getThumbnailUrl().replaceFirst("/thumbnail/", "/large/")); //原始图片地址 
			if(picUrls.size()>1) { //不只一张图片
				microblogImage.setThumbnailUrl(microblogImage.getThumbnailUrl().replaceFirst("/thumbnail/", "/square/")); //缩略图片地址
			}
			images.add(microblogImage);
		}
		microblog.setImages(images.isEmpty() ? null : images); //微博配图(MicroblogImage)列表
		microblog.setUrl("http://weibo.com/" + microblog.getUser().getId() + "/" + encodeSinaId(microblog.getId())); //链接
		microblog.setRepostUrl(microblog.getUrl() + "?type=repost"); //转发链接 http://weibo.com/1900372975/B61B0FfFQ?type=repost
		microblog.setCommentUrl(microblog.getUrl() + "?type=comment"); //评论链接 http://weibo.com/1900372975/B61B0FfFQ?type=comment
		return microblog;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.platform.MicroblogPlatform#readExpressions(com.yuanluesoft.microblog.pojo.MicroblogAccount, java.util.Map)
	 */
	protected void readExpressions(MicroblogAccount account, Map expressions) throws ServiceException {
		String accessToken = getAccessToken(account); //获取令牌
		readExpressions(accessToken, "ani", expressions);
		readExpressions(accessToken, "cartoon", expressions);
		readExpressions(accessToken, "face", expressions);
	}
	
	/**
	 * 读取表情
	 * @param accessToken
	 * @param type 表情类别，face：普通表情、ani：魔法表情、cartoon：动漫表情，默认为face。
	 * @param expressions
	 * @throws ServiceException
	 */
	private void readExpressions(String accessToken, String type, Map expressions) throws ServiceException {
		//type: 表情类别，face：普通表情、ani：魔法表情、cartoon：动漫表情，默认为face。
		JSONArray jsonArray = (JSONArray)openRequest("https://api.weibo.com/2/emotions.json?access_token=" + accessToken + "&type=" + type, false, null);
		for(Iterator iterator = jsonArray==null ? null : jsonArray.iterator(); iterator!=null && iterator.hasNext();) {
			JSONObject json = (JSONObject)iterator.next();
			try {
				expressions.put(json.get("phrase"), json.get("url"));
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("SinaWeibo: read " + type + " expressions, total " + expressions.size() + "expression.");
		}
	}
	
	/**
	 * 生成微博用户对象
	 * @param account
	 * @param user
	 * @return
	 */
	private MicroblogUser generateMicroblogUser(MicroblogAccount account, JSONObject user) throws ServiceException {
		MicroblogUser microblogUser = new MicroblogUser();
		microblogUser.setId("" + user.get("id")); //用户UID
		microblogUser.setNickname((String)user.get("screen_name")); //用户昵称 
		microblogUser.setName((String)user.get("name")); //友好显示名称 
		microblogUser.setLocation((String)user.get("location")); //用户所在地 
		microblogUser.setDescription((String)user.get("description")); //用户个人描述 
		microblogUser.setUrl((String)user.get("url")); //用户博客地址 
		microblogUser.setProfileImageUrl((String)user.get("profile_image_url")); //用户头像地址（中图），50×50像素 
		microblogUser.setProfileLargeImageUrl((String)user.get("avatar_large")); //用户头像地址（大图），180×180像素 
		microblogUser.setProfileHdImageUrl((String)user.get("avatar_hd")); //用户头像地址（高清），高清头像原图 
		microblogUser.setProfileUrl("http://weibo.com/" + (String)user.get("profile_url")); //用户的微博统一URL地址 
		microblogUser.setDomain((String)user.get("domain")); //用户的个性化域名 
		microblogUser.setSex(((String)user.get("gender")).toUpperCase().charAt(0)); //性别，M：男、F：女、N：未知 
		microblogUser.setFollowersCount(Integer.parseInt("" + user.get("followers_count"))); //粉丝数 
		microblogUser.setFriendsCount(Integer.parseInt("" + user.get("friends_count"))); //关注数 
		microblogUser.setMicroblogCount(Integer.parseInt("" + user.get("statuses_count"))); //微博数 
		microblogUser.setFavouritesCount(Integer.parseInt("" + user.get("favourites_count"))); //收藏数 
		microblogUser.setCreated(parseTimestamp((String)user.get("created_at"))); //用户创建（注册）时间 
		microblogUser.setAnyoneSendPrivateMessage("true".equalsIgnoreCase("" + user.get("allow_all_act_msg"))); //是否允许所有人给我发私信，true：是，false：否 
		microblogUser.setGeoEnabled("true".equalsIgnoreCase("" + user.get("geo_enabled"))); //是否允许标识用户的地理位置，true：是，false：否 
		microblogUser.setVerified("true".equalsIgnoreCase("" + user.get("verified"))); //是否是微博认证用户，即加V用户，true：是，false：否 
		microblogUser.setRemark((String)user.get("remark")); //用户备注信息，只有在查询用户关系时才返回此字段 
		microblogUser.setNewestMicroblog(generateMicroblog(account, (JSONObject)user.get("status"))); //用户的最近一条微博信息字段
		microblogUser.setAnyoneComment("true".equalsIgnoreCase("" + user.get("allow_all_comment"))); //是否允许所有人对我的微博进行评论，true：是，false：否 
		microblogUser.setVerifiedReason((String)user.get("verified_reason")); //认证原因 
		microblogUser.setFollowMe("true".equalsIgnoreCase("" + user.get("allow_all_act_msg"))); //该用户是否关注当前登录用户，true：是，false：否 
		microblogUser.setOnlineStatus(Integer.parseInt("" + user.get("online_status"))); //用户的在线状态，0：不在线、1：在线 
		microblogUser.setEachOtherFollowersCount(Integer.parseInt("" + user.get("bi_followers_count"))); //用户的互粉数 
		microblogUser.setLang((String)user.get("lang")); //用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
		return microblogUser;
	}
	
	/**
	 * 解析时间
	 * @param timeText
	 * @return
	 */
	private Timestamp parseTimestamp(String timeText) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
		try {
			return new Timestamp(formatter.parse(timeText).getTime());
		}
		catch (ParseException e) {
			Logger.exception(e);
			return null;
		} 
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.platform.MicroblogPlatform#replyPrivateMessage(com.yuanluesoft.microblog.pojo.MicroblogAccount, java.lang.String, java.lang.String, java.util.List)
	 */
	public void replyPrivateMessage(MicroblogAccount account, String privateMessageSenderId, String content, List images) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.platform.MicroblogPlatform#processReceivedMessage(com.yuanluesoft.microblog.pojo.MicroblogAccount, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void processReceivedMessage(MicroblogAccount account, DatabaseService databaseService, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		try {
			if(Logger.isTraceEnabled()) {
				Logger.trace("SinaWeibo: process request " + RequestUtils.getRequestURL(request, true));
			}
			if(!checkSignature(account, request)) { //签名验证
				return;
			}
			//读取消息内容
			StringBuffer requestBody = new StringBuffer();
			char[] buffer = new char[2048];
			BufferedReader reader = request.getReader();
			int readLen;
			while((readLen=reader.read(buffer))!=-1) {
				requestBody.append(buffer, 0, readLen);
			}
			if(requestBody.length()==0) { //没有内容
				response.getWriter().write(request.getParameter("echostr")); //输出随机字符串
				return;
			}
			if(Logger.isTraceEnabled()) {
				Logger.trace("SinaWeibo: receive message " + requestBody.toString());
			}
			//解析接收到的信息,JSON格式
			/*String messageType = xmlElement.elementText("type");
			if("text".equalsIgnoreCase(messageType)) {
				//processReceivedTextMessage(xmlElement, wechatAccount, request, response);
			}
			else if("image".equalsIgnoreCase(messageType)) {
				//processReceivedImageMessage(xmlElement, wechatAccount, request, response); 
			}*/
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 签名验证
	 * @param account
	 * @param request
	 * @return
	 **/
	private boolean checkSignature(MicroblogAccount account, HttpServletRequest request) {
		String signature = request.getParameter("signature"); //微博加密签名，signature结合了开发者appsecret参数和请求中的timestamp参数，nonce参数 
		String timestamp = request.getParameter("timestamp"); //时间戳 
		String nonce = request.getParameter("nonce"); //随机数 
		//signature参数的加密规则为：将appsecret参数，timestamp参数，nonce参数进行字典排序后，将三个参数字符串拼接成一个字符串进行sha1加密；开发者收到请求后，首先通过加密后的signature参数来校验GET请求的真实性，如果确认此次GET请求来自微博服务器，原样返回echostr参数内容就可以成功建立首次连接，否则连接失败。
		List list = ListUtils.generateListFromArray(new String[]{account.getParameterValue("appSecret"), timestamp, nonce});
		Collections.sort(list);
		try {
			return signature.equalsIgnoreCase(Encoder.getInstance().sha1(ListUtils.join(list, "", false)));
		}
		catch(Exception e) {
			Logger.exception(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.platform.MicroblogPlatform#createAccessToken(com.yuanluesoft.microblog.pojo.MicroblogAccount)
	 */
	protected AccessToken createAccessToken(MicroblogAccount account) throws ServiceException {
		String authorizeUrl = account.getSiteUrl() + (account.getSiteUrl().endsWith("/") ? "" : "/") + "authorize";
		NameValuePair[] values = {
				new NameValuePair("client_id", account.getParameterValue("appKey")),
				new NameValuePair("response_type", "code"),
				new NameValuePair("redirect_uri", authorizeUrl),
				new NameValuePair("action", "submit"),
				new NameValuePair("userId", account.getUserName()),
				new NameValuePair("passwd", account.getPassword()),
				new NameValuePair("isLoginSina", "0"),
				new NameValuePair("from", ""),
				new NameValuePair("regCallback", ""),
				new NameValuePair("state", ""),
				new NameValuePair("ticket", ""),
				new NameValuePair("withOfficalFlag", "0")
		};
		Map requestHeaders = new HashMap();
		requestHeaders.put("Referer", "https://api.weibo.com/oauth2/authorize");
		requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
		HttpResponse response;
		try {
			response = HttpUtils.doPost("https://api.weibo.com/oauth2/authorize", "utf-8", values, requestHeaders);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("SinaWeibo: authorize response is " + response.getResponseBody());
		}
		String code = StringUtils.getPropertyValue(response.getResponseBody(), "code");
		
		//调用OAuth2的access_token接口
		//client_id=2884940676&client_secret=17b1605ea4f68ec38ce2aebfae116d04&grant_type=authorization_code
		values = new NameValuePair[] {
					new NameValuePair("client_id", account.getParameterValue("appKey")), //申请应用时分配的AppKey。 
					new NameValuePair("client_secret", account.getParameterValue("appSecret")), //申请应用时分配的AppSecret
					new NameValuePair("grant_type", "authorization_code"), //请求的类型，填写authorization_code 
					new NameValuePair("code", code), //调用authorize获得的code值
					new NameValuePair("redirect_uri", authorizeUrl)}; //回调地址，需需与注册应用里的回调地址一致
		JSONObject json = (JSONObject)openRequest("https://api.weibo.com/oauth2/access_token", true, values);
		/*
		 返回数据
		 {
		       "access_token": "ACCESS_TOKEN",
		       "expires_in": 1234,
		       "remind_in":"798114",
		       "uid":"12341234"
		 }
		 access_token: string, 用于调用access_token，接口获取授权后的access token 
		 expires_in: string, access_token的生命周期，单位是秒数 
		 remind_in: string, access_token的生命周期（该参数即将废弃，开发者请使用expires_in）。 
		 uid: string, 当前授权用户的UID
		 */
		String errorCode = (String)json.get("error_code");
		if(errorCode!=null) {
			throw new ServiceException((String)json.get("error"));
		}
		String token = (String)json.get("access_token");
		if(token==null || token.isEmpty()) {
			return null;
		}
		return new AccessToken(account.getUserName(), token, Long.parseLong("" + json.get("expires_in")) * 1000);
	}
	
	/**
	 * 提交,并解析返回的JSON对象
	 * @param url
	 * @param post
	 * @param values
	 * @return
	 * @throws ServiceException
	 */
	private Object openRequest(String url, boolean post, Object[] values) throws ServiceException {
		try {
			if(Logger.isTraceEnabled()) {
				String info = "SinaWeibo: open url " + url;
				for(int i=0; i<(values==null ? 0 : values.length); i++) {
					if(values[i] instanceof NameValuePair) {
						info += "," + ((NameValuePair)values[i]).getName() + "=" + ((NameValuePair)values[i]).getValue();
					}
					else if(values[i] instanceof StringPart) {
						Field field = StringPart.class.getDeclaredField("value");
						field.setAccessible(true);
						info += "," + ((StringPart)values[i]).getName() + "=" + field.get(values[i]);
					}
				}
				Logger.trace(info);
			}
			HttpResponse response;
			if(post) {
				response = HttpUtils.doPost(url, "utf-8", values, null);
			}
			else {
				response = HttpUtils.getHttpContent(url, null, true, null, 10000);
			}
			if(Logger.isTraceEnabled()) {
				Logger.trace("SinaWeibo: response is " + (response.getResponseBody().length()<5000 ? response.getResponseBody() : response.getResponseBody().substring(0, 5000)));
			}
			return new JSONParser().parse(response.getResponseBody());
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 新浪ID解码
	 * @param id
	 * @return
	 */
	protected String decodeSinaId(String id) {
		String decoded = "";
		for(int i = id.length() - 4; i > -4; i = i - 4) { //从最后往前以4字节为一组读取URL字符
			int offset1 = i < 0 ? 0 : i;
			int offset2 = i + 4;
			String str = id.substring(offset1, offset2);
			str = "" + StringUtils.str62to10(str);
			if (offset1 > 0) { //若不是第一组，则不足7位补0
				while (str.length() < 7) {
					str = '0' + str;
				}
			}
			decoded = str + decoded;
		}
		return decoded;
	}
	
	/**
	 * 新浪ID编码
	 * @param id
	 * @return
	 */
	protected String encodeSinaId(String id) {
		String result = "";
		for (int i = id.length() - 7; i > -7; i = i - 7) { //从最后往前以7字节为一组读取id
			int offset1 = i < 0 ? 0 : i;
			int offset2 = i + 7;
			String num = id.substring(offset1, offset2);
			result = StringUtils.int10to62(Long.parseLong(num)) + result;
		}
		return result;
	}
}