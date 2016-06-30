package com.yuanluesoft.cms.templatemanage.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 主题服务
 * @author linchuan
 *
 */
public interface TemplateThemeService extends BusinessService {
	//主题类型
	public static final int THEME_TYPE_COMPUTER = 0; //电脑
	public static final int THEME_TYPE_SMART_PHONE = 1; //智能手机或平板电脑
	public static final int THEME_TYPE_WAP = 2; //WAP
	public static final int THEME_TYPE_CLIENT = 3; //客户端
	public static final int THEME_TYPE_WECHAT = 4; //微信
	
	//主题操作
	public final static int THEME_ACTION_SET_DEFAULT = 1; //设为默认模板
	public final static int THEME_ACTION_TEMPORARY_OPENING = 2; //临时启用
	public final static int THEME_ACTION_CANCEL_TEMPORARY_OPENING = 3; //取消临时启用
	
	/**
	 * 主题是否被使用
	 * @param themeId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isThemeUsed(long themeId) throws ServiceException;
	
	/**
	 * 检查主题是否属于指定的类型
	 * @param themeId
	 * @param themeType
	 * @return
	 * @throws ServiceException
	 */
	public boolean isTypeOf(long themeId, int themeType) throws ServiceException;
}