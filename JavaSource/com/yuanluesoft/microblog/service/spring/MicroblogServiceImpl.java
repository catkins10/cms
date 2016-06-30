package com.yuanluesoft.microblog.service.spring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.microblog.model.MicroblogPlatformParameterDefine;
import com.yuanluesoft.microblog.platform.MicroblogPlatform;
import com.yuanluesoft.microblog.pojo.Microblog;
import com.yuanluesoft.microblog.pojo.MicroblogAccount;
import com.yuanluesoft.microblog.pojo.MicroblogAccountParameter;
import com.yuanluesoft.microblog.pojo.MicroblogPrivateMessage;
import com.yuanluesoft.microblog.pojo.MicroblogWorkflowConfig;
import com.yuanluesoft.microblog.service.MicroblogService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 * 
 * @author linchuan
 *
 */
public class MicroblogServiceImpl extends BusinessServiceImpl implements MicroblogService {
	private OrgService orgService; //组织机构服务
	private CryptService cryptService; //加密服务
	private List platforms; //微博平台列表
	private Cache cache; //微博列表缓存
	private int cacheSeconds = 120; //缓存的时长,默认120秒

	/**
	 * 初始化
	 *
	 */
	public void init() {
		try {
			orgService.appendDirectoryPopedomType("microblogMessageManager", "微博消息管理", "unit", DirectoryPopedomType.INHERIT_FROM_PARENT_NO, false, true);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof MicroblogAccount) {
			MicroblogAccount account = (MicroblogAccount)record;
			account.setPassword(cryptService.encrypt(account.getPassword(), "" + account.getId(), true));
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof MicroblogAccount) {
			MicroblogAccount account = (MicroblogAccount)record;
			account.setPassword(cryptService.encrypt(account.getPassword(), "" + account.getId(), true));
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(com.yuanluesoft.jeaf.database.Record, boolean)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		if(record instanceof Microblog) { //微博
			Microblog microblog = (Microblog)record;
			//获取微博平台
			String hql = "select distinct MicroblogAccount.platform" +
						 " from MicroblogAccount MicroblogAccount" +
						 " where MicroblogAccount.id in (" + microblog.getAccountIds() + ")";
			List platformNames = getDatabaseService().findRecordsByHql(hql);
			//检查图片数量和大小
			List images;
			try {
				images = (List)FieldUtils.getFieldValue(record, "image", null);
			}
			catch (Exception e) {
				throw new ServiceException(e);
			}
			List errors = new ArrayList();
			for(Iterator iterator = platformNames.iterator(); iterator.hasNext();) {
				String platformName = (String)iterator.next();
				MicroblogPlatform microblogPlatform = getMicroblogPlatform(platformName);
				if(images!=null && images.size()>microblogPlatform.getImageCountLimit()) { //超出图片数量限制
					errors.add(microblogPlatform.getName() + "图片数量不允许超过" + microblogPlatform.getImageCountLimit() + "张");
				}
				for(Iterator iteratorImage = images==null ? null : images.iterator(); iteratorImage!=null && iteratorImage.hasNext();) {
					Image image = (Image)iteratorImage.next();
					if(image.getSize()>microblogPlatform.getImageSizeLimit()) {
						errors.add(microblogPlatform.getName() + "图片大小不允许超过" + StringUtils.getFileSize(microblogPlatform.getImageSizeLimit()));
						break;
					}
				}
			}
			return errors.isEmpty() ? null : errors;
		}
		return super.validateBusiness(record, isNew);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.service.MicroblogService#listMicroblogAccountParameters(java.lang.String, com.yuanluesoft.microblog.pojo.MicroblogAccount)
	 */
	public Set listMicroblogAccountParameters(String platformName, MicroblogAccount account) throws ServiceException {
		if(platformName==null || platformName.isEmpty()) {
			return null;
		}
		//获取微博平台
		MicroblogPlatform microblogPlatform = getMicroblogPlatform(platformName);
		if(microblogPlatform.getParameterDefines()==null) {
			return null;
		}
		Set parameters = new LinkedHashSet();
		for(Iterator iterator = microblogPlatform.getParameterDefines().iterator(); iterator.hasNext();) {
			MicroblogPlatformParameterDefine parameterDefine = (MicroblogPlatformParameterDefine)iterator.next();
			MicroblogAccountParameter accountParameter = (account==null ? null : (MicroblogAccountParameter)ListUtils.findObjectByProperty(account.getParameters(), "parameterName", parameterDefine.getName()));
			if(accountParameter==null) {
				accountParameter = new MicroblogAccountParameter();
			}
			accountParameter.setParameterDefine(parameterDefine); //参数定义
			accountParameter.setParameterName(parameterDefine.getName()); //名称
			if(account==null) {
				accountParameter.setParameterValue(parameterDefine.getDefaultValue());
			}
			parameters.add(accountParameter);
		}
		return parameters;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.service.MicroblogService#saveMicroblogAccountParameters(com.yuanluesoft.microblog.pojo.MicroblogAccount, java.lang.String[])
	 */
	public void saveMicroblogAccountParameters(MicroblogAccount account, String[] parameterValues) throws ServiceException {
		//删除原来的参数
		getDatabaseService().deleteRecordsByHql("from MicroblogAccountParameter MicroblogAccountParameter where MicroblogAccountParameter.accountId=" + account.getId());
		//获取微博平台
		MicroblogPlatform microblogPlatform = getMicroblogPlatform(account.getPlatform());
		List parameterDefines = microblogPlatform.getParameterDefines();
		if(microblogPlatform.getParameterDefines()==null) {
			return;
		}
		for(int i=0; i<parameterDefines.size(); i++) {
			MicroblogPlatformParameterDefine parameterDefine = (MicroblogPlatformParameterDefine)parameterDefines.get(i);
			MicroblogAccountParameter accountParameter = new MicroblogAccountParameter();
			accountParameter.setId(UUIDLongGenerator.generateId()); //ID
			accountParameter.setAccountId(account.getId()); //帐号ID
			accountParameter.setParameterName(parameterDefine.getName()); //参数名称
			if(!parameterDefine.isPassword()) { //不是密码
				accountParameter.setParameterValue(parameterValues[i]); //参数值
			}
			else { //密码
				accountParameter.setParameterValue(cryptService.encrypt(parameterValues[i], "" + account.getId(), true)); //参数值
			}
			getDatabaseService().saveRecord(accountParameter);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.service.MicroblogService#issue(com.yuanluesoft.microblog.pojo.Microblog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void issue(Microblog microblog, SessionInfo sessionInfo) throws ServiceException {
		MicroblogPrivateMessage privateMessage = null;
		if(microblog.getPrivateMessageId()>0) { //私信
			privateMessage = (MicroblogPrivateMessage)load(MicroblogPrivateMessage.class, microblog.getPrivateMessageId());
			if(privateMessage==null) { //私信已经不存在
				return;
			}
		}
		//获取图片
		List images;
		try {
			images = (List)FieldUtils.getFieldValue(microblog, "image", null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		//获取微薄帐号
		String hql = "from MicroblogAccount MicroblogAccount" +
					 " where MicroblogAccount.id in (" + (microblog.getPrivateMessageId()>0 ? privateMessage.getAccountId() + "" : microblog.getAccountIds()) + ")";
		List accounts = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("parameters"));
		for(Iterator iterator = accounts==null ? null : accounts.iterator(); iterator!=null && iterator.hasNext();) {
			MicroblogAccount account = (MicroblogAccount)iterator.next();
			//解密
			decryptMicroblogAccount(account);
			//获取微博平台
			MicroblogPlatform microblogPlatform = getMicroblogPlatform(account.getPlatform());
			if(microblog.getPrivateMessageId()>0) { //私信
				microblogPlatform.replyPrivateMessage(account, privateMessage.getSenderId(), microblog.getContent(), images);
			}
			else if(microblog.getBlogIds()==null || (";" + microblog.getBlogIds()).indexOf(";" + account.getId() + "|")==-1) { //之前没有发送过或者发送失败
				try {
					//TODO 获取分组在当前账户中的ID
					String ids = microblogPlatform.issueMicroblog(account, microblog.getRange(), null, microblog.getContent(), images);
					if(ids==null || ids.isEmpty()) {
						throw new ServiceException();
					}
					microblog.setBlogIds((microblog.getBlogIds()==null ? "" : microblog.getBlogIds() + ";") + account.getId() + "|" + ids);
				}
				catch(Exception e) {
					Logger.exception(e);
					unissue(microblog); //撤销发布
					throw new ValidateException("发布到" + microblogPlatform.getName() + "时失败");
				}
			}
		}
		if(privateMessage!=null) { //私信
			privateMessage.setReplyTime(DateTimeUtils.now());
			privateMessage.setReplier(sessionInfo.getUserName());
			privateMessage.setReplierId(sessionInfo.getUserId());
			update(privateMessage);
		}
		microblog.setSendTime(DateTimeUtils.now());
		update(microblog);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.microblog.service.MicroblogService#unissue(com.yuanluesoft.microblog.pojo.Microblog)
	 */
	public void unissue(Microblog microblog) throws ServiceException {
		if(microblog.getPrivateMessageId()>0) { //私信
			return;
		}
		if(microblog.getBlogIds()==null || microblog.getBlogIds().isEmpty()) {
			return;
		}
		//获取微薄帐号
		String hql = "from MicroblogAccount MicroblogAccount" +
					 " where MicroblogAccount.id in (" + microblog.getAccountIds() + ")";
		List accounts = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("parameters"));
		List blogIds = ListUtils.generateList(microblog.getBlogIds(), ";");
		for(Iterator iterator = blogIds.iterator(); iterator.hasNext();) {
			String blogId = (String)iterator.next();
			String[] values = blogId.split("\\|");
			MicroblogAccount account = (MicroblogAccount)ListUtils.findObjectByProperty(accounts, "id", new Long(values[0]));
			if(account==null) {
				continue;
			}
			//解密
			decryptMicroblogAccount(account);
			//获取微博平台
			MicroblogPlatform microblogPlatform = getMicroblogPlatform(account.getPlatform());
			try {
				String[] ids = values[1].split(",");
				for(int i=0; i<ids.length; i++) {
					microblogPlatform.deleteMicroblog(account, ids[i]);
				}
				iterator.remove();
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		microblog.setBlogIds(ListUtils.join(blogIds, ";", false));
		update(microblog);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.service.MicroblogService#readMicroblogs(long, int)
	 */
	public List readMicroblogs(long microblogAccountId, int count) throws ServiceException {
		//从缓存中获取
		CachedMicroblogs cachedMicroblogs = null;
		try {
			cachedMicroblogs = (CachedMicroblogs)cache.get(new Long(microblogAccountId));
		}
		catch (CacheException e) {
			Logger.exception(e);
		}
		if(cachedMicroblogs!=null &&
		   (System.currentTimeMillis() - cachedMicroblogs.getReadTime()) < cacheSeconds * 1000 && //未超时
		   count<=cachedMicroblogs.readCount) { //希望读取的记录数<=缓存的记录数
			List microblogs = cachedMicroblogs.getMicroblogs();
			return microblogs==null ? null : (microblogs.size()>count ? microblogs.subList(0, count) : microblogs);
		}
		MicroblogAccount account = (MicroblogAccount)load(MicroblogAccount.class, microblogAccountId);
		if(account==null) {
			return null;
		}
		//解密
		decryptMicroblogAccount(account);
		//获取微博平台
		MicroblogPlatform microblogPlatform = getMicroblogPlatform(account.getPlatform());
		List newestMicroblogs = new ArrayList();
		for(int i=0; i<100 && newestMicroblogs.size()<count; i++) {
			List microblogs = microblogPlatform.readMicroblogs(account, i, count);
			for(Iterator iterator = microblogs==null ? null : microblogs.iterator(); iterator!=null && iterator.hasNext();) {
				com.yuanluesoft.microblog.model.Microblog microblog = (com.yuanluesoft.microblog.model.Microblog)iterator.next();
				if("all".equals(microblog.getVisible())) { //筛选出所有人都可以看的微博
					newestMicroblogs.add(microblog);
				}
			}
		}
		if(cachedMicroblogs==null) {
			cachedMicroblogs = new CachedMicroblogs();
		}
		if(newestMicroblogs.isEmpty()) {
			newestMicroblogs = null;
		}
		cachedMicroblogs.setMicroblogs(newestMicroblogs);
		cachedMicroblogs.setReadCount(count);
		cachedMicroblogs.setReadTime(System.currentTimeMillis());
		try {
			cache.put(new Long(microblogAccountId), cachedMicroblogs);
		}
		catch (CacheException e) {
			Logger.exception(e);
		}
		return newestMicroblogs;
	}
	
	/**
	 * 解密微博帐号
	 * @param account
	 */
	private void decryptMicroblogAccount(MicroblogAccount account) throws ServiceException {
		//解密密码
		account.setPassword(cryptService.decrypt(account.getPassword(), "" + account.getId(), true));
		//获取微博平台
		MicroblogPlatform microblogPlatform = getMicroblogPlatform(account.getPlatform());
		List parameterDefines = microblogPlatform.getParameterDefines();
		if(microblogPlatform.getParameterDefines()==null) {
			return;
		}
		for(int i=0; i<parameterDefines.size(); i++) {
			MicroblogPlatformParameterDefine parameterDefine = (MicroblogPlatformParameterDefine)parameterDefines.get(i);
			if(!parameterDefine.isPassword()) { //不是密码
				continue;
			}
			MicroblogAccountParameter accountParameter = (MicroblogAccountParameter)ListUtils.findObjectByProperty(account.getParameters(), "parameterName", parameterDefine.getName());
			accountParameter.setParameterValue(cryptService.decrypt(accountParameter.getParameterValue(), "" + account.getId(), true)); //解密
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.service.MicroblogService#getWorkflowConfig(long, boolean)
	 */
	public MicroblogWorkflowConfig getWorkflowConfig(long orgId, boolean inheritParentEnabled) throws ServiceException {
		String hql;
		if(!inheritParentEnabled || orgId==0) {
			hql = "from MicroblogWorkflowConfig MicroblogWorkflowConfig" +
				  " where MicroblogWorkflowConfig.orgId=" + orgId;
		}
		else {
			//查找当前机构以及上级机构的流程配置
			hql = "select MicroblogWorkflowConfig" +
				  " from MicroblogWorkflowConfig MicroblogWorkflowConfig, OrgSubjection OrgSubjection" +
				  " where MicroblogWorkflowConfig.orgId=OrgSubjection.parentDirectoryId" +
				  " and OrgSubjection.directoryId=" + orgId +
				  " order by OrgSubjection.id";
		}
		return (MicroblogWorkflowConfig)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.service.MicroblogService#listAccounts(long)
	 */
	public List listAccounts(long unitId) throws ServiceException {
		String hql = "from MicroblogAccount MicroblogAccount" +
					 " where MicroblogAccount.unitId=" + unitId +
					 " order by MicroblogAccount.id";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.service.MicroblogService#getShortUrlMaxLength()
	 */
	public int getShortUrlMaxLength() throws ServiceException {
		int len = 0;
		for(Iterator iterator = platforms.iterator(); iterator.hasNext();) {
			MicroblogPlatform platform = (MicroblogPlatform)iterator.next();
			if(platform.getShortUrlMaxLength()>len) {
				len = platform.getShortUrlMaxLength();
			}
		}
		return len;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.microblog.service.MicroblogService#processReceivedMessage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void processReceivedMessage(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		MicroblogAccount account = (MicroblogAccount)load(MicroblogAccount.class, RequestUtils.getParameterLongValue(request, "accountId"));
		if(account==null) {
			return;
		}
		//解密
		decryptMicroblogAccount(account);
		MicroblogPlatform microblogPlatform = getMicroblogPlatform(account.getPlatform());
		microblogPlatform.processReceivedMessage(account, getDatabaseService(), request, response);
	}

	/**
	 * 获取微博平台
	 * @param platformName
	 * @return
	 */
	private MicroblogPlatform getMicroblogPlatform(String platformName) {
		return (MicroblogPlatform)ListUtils.findObjectByProperty(platforms, "name", platformName);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("platform".equals(itemsName)) { //微博平台
			List items = new ArrayList();
			for(Iterator iterator = platforms.iterator(); iterator.hasNext();) {
				MicroblogPlatform platform = (MicroblogPlatform)iterator.next();
				items.add(new Object[]{platform.getName(), platform.getName()});
			}
			return items;
		}
		else if("account".equals(itemsName)) { //帐号
			Number unitId = null;
			try {
				unitId = (Number)PropertyUtils.getProperty(bean, "unitId");
			}
			catch(Exception e) {
				
			}
			List items = (List)request.getAttribute("accounts" + unitId);
			if(items!=null) {
				return items;
			}
			List accounts = listAccounts(unitId.longValue());
			items = new ArrayList();
			for(Iterator iterator = accounts==null ? null : accounts.iterator(); iterator!=null && iterator.hasNext();) {
				MicroblogAccount account = (MicroblogAccount)iterator.next();
				items.add(new Object[]{account.getName() + "(" + account.getPlatform() + ")", new Long(account.getId())});
			}
			request.setAttribute("accounts" + unitId, items);
			return items;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long orgId = RequestUtils.getParameterLongValue(notifyRequest, "orgId"); //组织机构ID
		MicroblogWorkflowConfig workflowConfig = getWorkflowConfig(orgId, false);
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) { //流程已删除
			getDatabaseService().deleteRecord(workflowConfig);
			return;
		}
		boolean isNew = (workflowConfig==null);
		if(isNew) {
			workflowConfig = new MicroblogWorkflowConfig();
			workflowConfig.setId(UUIDLongGenerator.generateId()); //ID
			workflowConfig.setOrgId(orgId); //机构ID
		}
		workflowConfig.setWorkflowId(workflowId); //流程ID
		workflowConfig.setWorkflowName(workflowPackage.getName()); //流程名称
		if(isNew) {
			getDatabaseService().saveRecord(workflowConfig);
		}
		else {
			getDatabaseService().updateRecord(workflowConfig);
		}
	}
	
	/**
	 * 缓存的微博
	 * @author linchuan
	 *
	 */
	private class CachedMicroblogs implements Serializable, Cloneable {
		private long readTime; //获取时间
		private List microblogs; //微博列表
		private int readCount; //希望读取的记录数
		
		/**
		 * @return the microblogs
		 */
		public List getMicroblogs() {
			return microblogs;
		}
		/**
		 * @param microblogs the microblogs to set
		 */
		public void setMicroblogs(List microblogs) {
			this.microblogs = microblogs;
		}
		/**
		 * @return the readCount
		 */
		public int getReadCount() {
			return readCount;
		}
		/**
		 * @param readCount the readCount to set
		 */
		public void setReadCount(int readCount) {
			this.readCount = readCount;
		}
		/**
		 * @return the readTime
		 */
		public long getReadTime() {
			return readTime;
		}
		/**
		 * @param readTime the readTime to set
		 */
		public void setReadTime(long readTime) {
			this.readTime = readTime;
		}
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @return the platforms
	 */
	public List getPlatforms() {
		return platforms;
	}

	/**
	 * @param platforms the platforms to set
	 */
	public void setPlatforms(List platforms) {
		this.platforms = platforms;
	}

	/**
	 * @return the cryptService
	 */
	public CryptService getCryptService() {
		return cryptService;
	}

	/**
	 * @param cryptService the cryptService to set
	 */
	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	/**
	 * @return the cacheSeconds
	 */
	public int getCacheSeconds() {
		return cacheSeconds;
	}

	/**
	 * @param cacheSeconds the cacheSeconds to set
	 */
	public void setCacheSeconds(int cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}
}