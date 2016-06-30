package com.yuanluesoft.microblog.platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.microblog.pojo.MicroblogAccount;

/**
 * 微博平台
 * @author linchuan
 *
 */
public abstract class MicroblogPlatform {
	private int imageSizeLimit; //图片尺寸上限,如果不支持发图片返回0
	private int imageCountLimit; //图片数量上限
	private int shortUrlMaxLength; //短链接的最大长度
	
	protected List accessTokens = new ArrayList(); //令牌
	//私有属性
	private Map expressions = new HashMap(); //表情列表
	private long lastUpdateExpressions = 0; //最后更新表情列表的时间

	/**
	 * 获取名称,如新浪微博
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * 获取参数定义(MicroblogPlatformParameterDefine)列表
	 * @return
	 */
	public abstract List getParameterDefines();
	
	/**
	 * 发布微博,返回微博ID列表
	 * @param account
	 * @param range
	 * @param groupIds
	 * @param content
	 * @param images
	 * @return
	 */
	public abstract String issueMicroblog(MicroblogAccount account, String range, String[] groupIds, String content, List images) throws ServiceException;
	
	/**
	 * 删除微博
	 * @param account
	 * @param microblogId
	 * @throws ServiceException
	 */
	public abstract void deleteMicroblog(MicroblogAccount account, String microblogId) throws ServiceException;
	
	/**
	 * 读取微博(Microblog)列表
	 * @param account
	 * @param pageIndex
	 * @param pageRows
	 * @return
	 * @throws ServiceException
	 */
	public abstract List readMicroblogs(MicroblogAccount account, int pageIndex, int pageRows) throws ServiceException;
	
	/**
	 * 答复私信
	 * @param account
	 * @param privateMssageSenderId
	 * @param content
	 * @param images
	 */
	public abstract void replyPrivateMessage(MicroblogAccount account, String privateMessageSenderId, String content, List images) throws ServiceException;
	
	/**
	 * 创建令牌
	 * @param account
	 * @return
	 */
	protected abstract AccessToken createAccessToken(MicroblogAccount account) throws ServiceException;
	
	/**
	 * 读取官方表情
	 * @param account
	 * @param expressions
	 * @return
	 * @throws ServiceException
	 */
	protected abstract void readExpressions(MicroblogAccount account, Map expressions) throws ServiceException;
	
	/**
	 * 处理接收到的消息
	 * @param account
	 * @param databaseService
	 * @param request
	 * @param response
	 */
	public abstract void processReceivedMessage(MicroblogAccount account, DatabaseService databaseService, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
		
	/**
	 * 获取微博接口令牌
	 * @return
	 **/
	protected String getAccessToken(MicroblogAccount account) throws ServiceException {
		//从列表中获取
		AccessToken accessToken = null;
		for(Iterator iterator = accessTokens.iterator(); iterator.hasNext();) {
			AccessToken token = (AccessToken)iterator.next();
			if(System.currentTimeMillis()>token.getExpires()) {
				iterator.remove();
			}
			else if(token.getUserName().equals(account.getUserName())) {
				accessToken = token;
			}
		}
		if(accessToken!=null) {
			return accessToken.getToken();
		}
		accessToken = createAccessToken(account);
		if(accessToken==null) {
			throw new ServiceException("get access token failed");
		}
		accessTokens.add(accessToken);
		return accessToken.getToken();
	}
	
	/**
	 * 重置微博内容
	 * @param account
	 * @param content
	 * @return
	 * @throws ServiceException
	 */
	protected String resetContent(MicroblogAccount account, String content) throws ServiceException {
		content = StringUtils.escape(content)
							 .replaceAll("&nbsp;", " ")
							 .replaceAll("(?i)((http|https)://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?)", "<a target=\"_blank\" href=\"$1\">$1</a>");
		//替换表情
		String newContent = "";
		int beginIndex = 0, endIndex = 0;
		while((endIndex = content.indexOf('[', beginIndex))!=-1) {
			newContent += content.substring(beginIndex, endIndex);
			beginIndex = endIndex;
			endIndex = content.indexOf(']', beginIndex + 1);
			if(endIndex==-1) {
				break;
			}
			endIndex++;
			String text = content.substring(beginIndex, endIndex);
			beginIndex = endIndex;
			String url = getExpression(account, text);
			if(url==null || url.isEmpty()) {
				newContent += text;
			}
			else {
				newContent += "<img title=\"" + text + "\" border=\"0\" src=\"" + url + "\"/>";
			}
		}
		newContent += content.substring(beginIndex);
		return newContent;
	}
	
	/**
	 * 获取表情
	 * @param account
	 * @param text
	 * @return
	 */
	protected String getExpression(MicroblogAccount account, String text) throws ServiceException {
		synchronized(expressions) {
			if(expressions.isEmpty() || System.currentTimeMillis() - lastUpdateExpressions > 72 * 3600 * 1000) { //没有表情列表,或者已经超过72小时
				readExpressions(account, expressions);
				lastUpdateExpressions = System.currentTimeMillis();
			}
		}
		return (String)expressions.get(text);
	}
	
	/**
	 * 微博令牌
	 * @author linchuan
	 *
	 */
	protected class AccessToken {
		private String userName; //微博用户名 
		private String token; //令牌
		private long expires; //有效期截止时间
		
		public AccessToken(String userName, String token, long expiresTimeMillis) {
			super();
			this.userName = userName;
			this.token = token;
			this.expires = System.currentTimeMillis() + expiresTimeMillis - 60 * 1000;
		}
		/**
		 * @return the expires
		 */
		public long getExpires() {
			return expires;
		}
		/**
		 * @param expires the expires to set
		 */
		public void setExpires(long expires) {
			this.expires = expires;
		}
		/**
		 * @return the token
		 */
		public String getToken() {
			return token;
		}
		/**
		 * @param token the token to set
		 */
		public void setToken(String token) {
			this.token = token;
		}
		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}
		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}
	}

	/**
	 * @return the imageCountLimit
	 */
	public int getImageCountLimit() {
		return imageCountLimit;
	}

	/**
	 * @param imageCountLimit the imageCountLimit to set
	 */
	public void setImageCountLimit(int imageCountLimit) {
		this.imageCountLimit = imageCountLimit;
	}

	/**
	 * @return the imageSizeLimit
	 */
	public int getImageSizeLimit() {
		return imageSizeLimit;
	}

	/**
	 * @param imageSizeLimit the imageSizeLimit to set
	 */
	public void setImageSizeLimit(int imageSizeLimit) {
		this.imageSizeLimit = imageSizeLimit;
	}

	/**
	 * @return the shortUrlMaxLength
	 */
	public int getShortUrlMaxLength() {
		return shortUrlMaxLength;
	}

	/**
	 * @param shortUrlMaxLength the shortUrlMaxLength to set
	 */
	public void setShortUrlMaxLength(int shortUrlMaxLength) {
		this.shortUrlMaxLength = shortUrlMaxLength;
	}
}