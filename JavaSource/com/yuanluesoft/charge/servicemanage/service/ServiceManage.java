package com.yuanluesoft.charge.servicemanage.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.charge.servicemanage.model.CardTopUp;
import com.yuanluesoft.charge.servicemanage.model.ServiceDeducting;
import com.yuanluesoft.charge.servicemanage.pojo.Service;
import com.yuanluesoft.charge.servicemanage.pojo.ServicePrice;

/**
 * 
 * @author linchuan
 *
 */
public interface ServiceManage extends BusinessService {
	//服务扣款策略
	public static final String SERVICE_DEDUCT_POLICY_DAY = "day"; //按实际天数扣除
	public static final String SERVICE_DEDUCT_POLICY_CUSTOM = "custom"; //按自定义策略扣除
	
	//服务订购方式
	public static final String SERVICE_ORDER_MODE_CARRIER = "carrier"; //运营商
	public static final String SERVICE_ORDER_MODE_CARD = "card"; //充值卡
	public static final String SERVICE_ORDER_MODE_WEB = "web"; //网上订购
	
	//服务策略
	public static final int SERVICE_POLICY_FREE = 1; //免费
	public static final int SERVICE_POLICY_TRY_PERIOD = 2; //在指定时间段试用
	public static final int SERVICE_POLICY_TRY_DAYS = 3; //试用指定的天数
	
	/**
	 * 获取服务
	 * @param serviceId
	 * @return
	 * @throws ServiceException
	 */
	public Service getService(long serviceId) throws ServiceException;
	
	/**
	 * 获取服务条目列表,以逗号分隔
	 * @return
	 */
	public String getServiceItems();
	
	/**
	 * 获取服务报价列表
	 * @param serviceIds
	 * @return
	 * @throws ServiceException
	 */
	public List listServicePrices(String servicePriceIds) throws ServiceException;
	
	/**
	 * 删除服务报价
	 * @param servicePrice
	 * @throws ServiceException
	 */
	public void deleteServicePrice(ServicePrice servicePrice) throws ServiceException;
	
	/**
	 * 从服务报价ID列表中,过滤出用户可以使用的服务报价列表
	 * @param servicePriceIds
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public List listOrderEnabledServicePrices(String servicePriceIds, long personId) throws ServiceException;
	
	/**
	 * 获取允许用户订购的服务报价列表
	 * @param sessionInfo
	 * @param orderMode 订购方式
	 * @return
	 * @throws ServiceException
	 */
	public List listOrderEnabledServicePrices(SessionInfo sessionInfo, String orderMode) throws ServiceException;

	/**
	 * 判断用户是否为服务项目付费
	 * @param sessionInfo
	 * @param serviceItemName
	 * @return
	 * @throws ServiceException
	 */
	public boolean isServiceItemPaied(long personId, String serviceItemName) throws ServiceException;
	
	/**
	 * 判断订单是否有支付过,任何条目支付过都算做是
	 * @param orderId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isOrderPaied(long orderId) throws ServiceException;
	
	/**
	 * 判断用户是否有服务项目的试用权限
	 * @param sessionInfo
	 * @param serviceItemName
	 * @return
	 * @throws ServiceException
	 */
	public boolean isServiceItemTriable(SessionInfo sessionInfo, String serviceItemName) throws ServiceException;
	
	/**
	 * 用户使用了服务项目
	 * @param sessionInfo
	 * @param serviceItemName
	 * @throws ServiceException
	 */
	public void useServiceItem(SessionInfo sessionInfo, String serviceItemName) throws ServiceException;
	
	/**
	 * 获取服务的有效使用时间
	 * @param sessionInfo
	 * @param serviceItemName
	 * @return
	 * @throws ServiceException
	 */
	public ServiceDeducting getLastServiceDeducting(SessionInfo sessionInfo, String serviceItemName) throws ServiceException;
	
	/**
	 * 获取最后一次冲值记录
	 * @param sessionInfo
	 * @param serviceItemName
	 * @return
	 * @throws ServiceException
	 */
	public CardTopUp getLastCardTopUp(SessionInfo sessionInfo, String serviceItemName) throws ServiceException;
	
	/**
	 * 获取服务试用信息
	 * @param sessionInfo
	 * @param serviceItemName 服务项目名称,如：学习评测、英语学习器
	 * @return
	 * @throws ServiceException
	 */
	public Timestamp getServiceTryEnd(SessionInfo sessionInfo, String serviceItemName) throws ServiceException;
}