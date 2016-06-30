package com.yuanluesoft.bbs.forum.service.spring;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsReply;
import com.yuanluesoft.bbs.article.service.BbsArticleService;
import com.yuanluesoft.bbs.forum.model.Category;
import com.yuanluesoft.bbs.forum.pojo.Bbs;
import com.yuanluesoft.bbs.forum.pojo.BbsDirectory;
import com.yuanluesoft.bbs.forum.pojo.BbsDirectoryPopedom;
import com.yuanluesoft.bbs.forum.pojo.BbsDirectorySubjection;
import com.yuanluesoft.bbs.forum.pojo.Forum;
import com.yuanluesoft.bbs.forum.pojo.ForumCategory;
import com.yuanluesoft.bbs.forum.service.ForumService;
import com.yuanluesoft.bbs.usermanage.pojo.BbsUser;
import com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.applicationtree.ApplicationTreeNavigator;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.usermanage.member.service.MemberService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ForumServiceImpl extends DirectoryServiceImpl implements ForumService {
	private MemberService memberService; //网上用户注册管理
	private BbsArticleService bbsArticleService; //论坛文章服务

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getBaseDirectoryClass()
	 */
	public Class getBaseDirectoryClass() {
		return BbsDirectory.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryLinkClass()
	 */
	public Class getDirectoryLinkClass() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryTypes()
	 */
	public DirectoryType[] getDirectoryTypes() {
		return new DirectoryType[] {
			new DirectoryType("bbs", null, "论坛", Bbs.class, "/bbs/forum/icons/bbs.gif", null, true),
			new DirectoryType("category", "bbs,category", "版块分类", ForumCategory.class, "/bbs/forum/icons/category.gif", null, false), 
			new DirectoryType("forum", "bbs,category,forum", "版块", Forum.class, "/bbs/forum/icons/forum.gif", null, false)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryPopedoms()
	 */
	public DirectoryPopedomType[] getDirectoryPopedomTypes() {
		return new DirectoryPopedomType[] {
			new DirectoryPopedomType("manager", "管理员", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, true, true),
			new DirectoryPopedomType("visitor", "访问者", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_WHEN_EMPTY, false, false)};
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getTreeRootNodeIcon()
	 */
	public String getTreeRootNodeIcon() {
		return "/bbs/forum/icons/root.gif";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#resetCopiedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException {
		return newDirectory;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#deleteDataInDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public void deleteDataInDirectory(Directory directory) throws ServiceException {
		if(!(directory instanceof Forum)) { //不是版块,不需要删除数据
			return;
		}
		//获取隶属当前目录的文章列表
		String hql = "select BbsArticle" +
					 " from BbsArticle BbsArticle, BbsArticleSubjection BbsArticleSubjection" +
					 " where BbsArticleSubjection.forumId=" + directory.getId() +
					 " and BbsArticleSubjection.articleId=BbsArticle.id";
		for(int i=0; ; i+=100) {
			List articles = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections", ","), i, 100);
			if(articles==null || articles.isEmpty()) {
				break;
			}
			for(Iterator iterator = articles.iterator(); iterator.hasNext();) {
				BbsArticle article = (BbsArticle)iterator.next();
				//检查文章是否只隶属于当前版块
				if(article.getSubjections().size()==1) { //只属于当前版块
					//删除文章
					bbsArticleService.delete(article);
				}
				else { //属于多个版块
					//删除隶属关系
					Set subjections = new HashSet(article.getSubjections());
					ListUtils.removeObjectByProperty(subjections, "forumId", new Long(directory.getId()));
					bbsArticleService.updateArticleSubjections(article, false, ListUtils.join(subjections, "forumId", ",", false));
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return "/bbs/article/admin/articleView.shtml?directoryId=" + directoryId;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#authorize(com.yuanluesoft.jeaf.directorymanage.pojo.Directory, java.lang.String, java.lang.String, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean authorize(Directory directory, boolean isNewDirectory, String popedomName, String userIds, String userNames, boolean keepMyselfPopedom, SessionInfo sessionInfo) throws ServiceException {
		//获取昵称
		if(userIds!=null && !userIds.equals("")) {
			List bbsUsers = getDatabaseService().findRecordsByHql("from BbsUser BbsUser where BbsUser.id in (" + JdbcUtils.validateInClauseNumbers(userIds) + ")");
			String[] ids = userIds.split(",");
			String[] names = userNames.split(",");
			for(int i=0; i<ids.length; i++) {
				BbsUser bbsUser = (BbsUser)ListUtils.findObjectByProperty(bbsUsers, "id", new Long(ids[i]));
				if(bbsUser!=null) {
					names[i] = bbsUser.getNickname();
				}
			}
			userNames = ListUtils.join(names, ",", false);
		}
		return super.authorize(directory, isNewDirectory, popedomName, userIds, userNames, keepMyselfPopedom, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getApplicationNavigator(java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ApplicationNavigator getApplicationNavigator(String applicationName, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		ApplicationTreeNavigator applicationTreeNavigator = (ApplicationTreeNavigator)super.getApplicationNavigator(applicationName, request, sessionInfo);
		//添加"论坛用户"
		if("0".equals(applicationTreeNavigator.getTree().getRootNode().getNodeId())) { //根论坛
			TreeNode userNode = applicationTreeNavigator.getTree().appendChildNode("bbsUser", "论坛用户", "bbsUser", Environment.getWebApplicationUrl() + "/bbs/forum/icons/user.gif", false);
			userNode.setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/jeaf/application/applicationView.shtml?applicationName=bbs/usermanage&viewName=admin/bbsuser");
		}
		return applicationTreeNavigator;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.system.services.InitializeService#init(java.lang.String, long, java.lang.String)
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException {
		if(getDirectory(0)!=null) {
			return false;
		}
		
		//创建论坛
		Bbs bbs = new Bbs();
		bbs.setId(0);
		bbs.setDirectoryName(systemName + "论坛");
		bbs.setDirectoryType("bbs");
		getDatabaseService().saveRecord(bbs);
		
		//更新隶属关系
		BbsDirectorySubjection subjection = new BbsDirectorySubjection();
		subjection.setId(UUIDLongGenerator.generateId()); //ID
		subjection.setDirectoryId(0); //目录ID
		subjection.setParentDirectoryId(0); //上级ID
		getDatabaseService().saveRecord(subjection);
		
		//添加管理员
		BbsDirectoryPopedom manager = new BbsDirectoryPopedom();
		manager.setId(UUIDLongGenerator.generateId()); //ID
		manager.setDirectoryId(0); //目录ID
		manager.setUserId(managerId); //管理员ID
		manager.setUserName(managerName); //管理员昵称
		manager.setPopedomName("manager"); //访问级别
		manager.setIsInherit('0'); //不是继承的
		getDatabaseService().saveRecord(manager);
		
		//添加访问者
		BbsDirectoryPopedom visitor = new BbsDirectoryPopedom();
		visitor.setId(UUIDLongGenerator.generateId()); //ID
		visitor.setDirectoryId(0); //目录ID
		visitor.setUserId(0); //访问者ID
		visitor.setUserName("所有用户"); //访问者昵称
		visitor.setPopedomName("visitor"); //访问级别
		visitor.setIsInherit('0'); //不是继承的
		getDatabaseService().saveRecord(visitor);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.forum.service.ForumService#getBbs(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public com.yuanluesoft.bbs.forum.model.Bbs getBbs(long bbsId, SessionInfo sessionInfo) throws ServiceException {
		com.yuanluesoft.bbs.forum.model.Bbs bbs = new com.yuanluesoft.bbs.forum.model.Bbs();
		Bbs pojoBbs = (Bbs)load(Bbs.class, bbsId);
		//论坛ID
		bbs.setId(pojoBbs.getId());
		//论坛名称
		bbs.setName(pojoBbs.getDirectoryName());
		//论坛描述
		bbs.setDescription(pojoBbs.getDescription());
		//总版主
		bbs.setManagers(pojoBbs.getMyVisitors("manager"));
		//文章总数
		bbs.setArticleTotal(totalArticle(bbsId, null, null));
		//回复总数
		bbs.setReplyTotal(totalReply(bbsId, null, null));
		//今天的文章总数
		Timestamp today = new Timestamp(DateTimeUtils.date().getTime());
		bbs.setArticleToday(totalArticle(bbsId, today, null));
		//今天的回复总数
		bbs.setReplyToday(totalReply(bbsId, today, null));
		//今天的文章总数
		Timestamp yesterday = new Timestamp(DateTimeUtils.date().getTime()-24*3600*1000);
		bbs.setArticleYesterday(totalArticle(bbsId, yesterday, today));
		//今天的回复总数
		bbs.setReplyYesterday(totalReply(bbsId, yesterday, today));
		//用户总数
		bbs.setMemberTotal(memberService.totalMember());
		//最后注册的用户
		bbs.setLastMember(memberService.getLastMember());
		//版块分类列表,仅一层,超过一层的自动加入到第一层中
		String hql = "select ForumCategory.id" +
					 " from ForumCategory ForumCategory" +
					 " where ForumCategory.parentDirectoryId=" + bbsId +
					 " order by ForumCategory.priority DESC, ForumCategory.directoryName";
		List categories = getDatabaseService().findRecordsByHql(hql);
		if(categories==null) {
			return bbs;
		}
		for(int i=categories.size()-1; i>=0; i--) {
			long categoryId = ((Long)categories.get(i)).longValue();
			Category category = getCategory(categoryId, sessionInfo);
			categories.set(i, category);
		}
		bbs.setCategories(categories);
		return bbs;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.forum.service.ForumService#getCategory(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Category getCategory(long categoryId, SessionInfo sessionInfo) throws ServiceException {
		Category category = new Category();
		ForumCategory pojoCategory = (ForumCategory)load(ForumCategory.class, categoryId);
		category.setId(pojoCategory.getId()); //分类ID
		category.setName(pojoCategory.getDirectoryName()); //分类名称
		category.setDescription(pojoCategory.getDescription()); //分类描述
		category.setManagers(pojoCategory.getMyVisitors("manager")); //分类管理员
		//获取下级论坛列表
		String hql = "select Forum.id" +
					 " from Forum Forum, BbsDirectorySubjection BbsDirectorySubjection" +
					 " where Forum.id=BbsDirectorySubjection.directoryId" +
					 " and BbsDirectorySubjection.parentDirectoryId=" + categoryId +
					 " order by Forum.priority DESC, Forum.directoryName";
		List forums = getDatabaseService().findRecordsByHql(hql);
		if(forums==null) {
			return category;
		}
		for(int i=0; i<forums.size(); i++) {
			long forumId = ((Long)forums.get(i)).longValue();
			forums.set(i, getForum(forumId, sessionInfo));
		}
		category.setForums(forums);
		return category;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.forum.service.ForumService#getForum(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public com.yuanluesoft.bbs.forum.model.Forum getForum(long forumId, SessionInfo sessionInfo) throws ServiceException {
		Forum pojoForum = (Forum)load(Forum.class, forumId);
		com.yuanluesoft.bbs.forum.model.Forum forum = getForum(pojoForum);
		
		//获取下级论坛列表
		String hql = "from Forum Forum where Forum.parentDirectoryId=" + forumId + " order by Forum.priority DESC, Forum.directoryName";
		List childForums = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("directoryPopedoms", ","));
		if(childForums==null) {
			return forum;
		}
		for(int i=0; i<childForums.size(); i++) {
			pojoForum = (Forum)childForums.get(i);
			childForums.set(i, getForum(pojoForum));
		}
		forum.setChildForums(childForums); //子论坛列表,仅第一级
		return forum;
	}
	
	/**
	 * 转换成Forum模型
	 * @param pojoForum
	 * @return
	 * @throws ServiceException
	 */
	private com.yuanluesoft.bbs.forum.model.Forum getForum(Forum pojoForum) throws ServiceException {
		com.yuanluesoft.bbs.forum.model.Forum forum = new com.yuanluesoft.bbs.forum.model.Forum();
		forum.setId(pojoForum.getId()); //分类ID
		forum.setName(pojoForum.getDirectoryName()); //分类名称
		forum.setDescription(pojoForum.getDescription()); //分类名称
		forum.setManagers(pojoForum.getMyVisitors("manager")); //版主列表
		forum.setPageArticles(pojoForum.getPageArticles()); //论坛每页显示主题数
		forum.setPageReplies(pojoForum.getPageReplies()); //每贴显示的回复数
		forum.setAnonymousEnabled(pojoForum.getAnonymousEnabled()); //是否允许匿名访问
		forum.setVipOnly(pojoForum.getVipOnly()); //是否只允许VIP访问
		forum.setAnonymousDownload(pojoForum.getAnonymousDownload()); //是否允许匿名用户下载附件
		forum.setArticleTotal(totalArticle(pojoForum.getId(), null, null)); //文章总数
		forum.setReplyTotal(totalReply(pojoForum.getId(), null, null)); //回复总数
		Timestamp today = new Timestamp(DateTimeUtils.date().getTime());
		forum.setArticleToday(totalArticle(pojoForum.getId(), today, null)); //今天的文章总数
		forum.setReplyToday(totalReply(pojoForum.getId(), today, null)); //今天的回复总数
		String hql = "select BbsArticle from BbsArticle BbsArticle, BbsArticleSubjection BbsArticleSubjection" +
					 " where not (BbsArticle.created is null)" +
					 " and BbsArticle.id=BbsArticleSubjection.articleId" +
					 " and BbsArticleSubjection.forumId=" + pojoForum.getId() +
					 " and BbsArticle.systemMessage!='1'" + //系统消息不在查询范围内
					 " order by BbsArticle.created DESC";
		forum.setLastArticle((BbsArticle)getDatabaseService().findRecordByHql(hql)); //最后发表的文章
		hql = "select BbsReply from BbsReply BbsReply, BbsArticle BbsArticle, BbsArticleSubjection BbsArticleSubjection" +
			  " where not (BbsReply.created is null)" +
			  " and BbsReply.articleId=BbsArticleSubjection.articleId" +
			  " and BbsReply.articleId=BbsArticle.id" +
			  " and BbsArticle.systemMessage!='1'" + //系统消息不在查询范围内
			  " and BbsArticleSubjection.forumId=" + pojoForum.getId() +
			  " order by BbsReply.created DESC";
		forum.setLastReply((BbsReply)getDatabaseService().findRecordByHql(hql)); //最后发表的回复
		return forum;
	}
	
	/**
	 * 获取文章数量
	 * @param directoryId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ServiceException
	 */
	private int totalArticle(long directoryId, Timestamp beginTime, Timestamp endTime) throws ServiceException {
		String hql = "select Count(distinct BbsArticle)" +
					 " from BbsArticle BbsArticle";
		String where = "not (BbsArticle.created is null)";
		if(beginTime!=null) {
			where += " and BbsArticle.created>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")";
		}
		if(endTime!=null) {
			where += " and BbsArticle.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(endTime, null) + ")";
		}
		if(directoryId>0) {
			hql += ",BbsArticleSubjection BbsArticleSubjection, BbsDirectorySubjection BbsDirectorySubjection";
			where += " and BbsArticle.id=BbsArticleSubjection.articleId" +
					 " and BbsArticleSubjection.forumId=BbsDirectorySubjection.directoryId" +
					 " and BbsDirectorySubjection.parentDirectoryId=" + directoryId;
		}
		hql += " where " + where;
		Object total = getDatabaseService().findRecordByHql(hql);
		return (total==null ? 0 : ((Number)total).intValue());
	}
	
	/**
	 * 获取回复数量
	 * @param directoryId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ServiceException
	 */
	private int totalReply(long directoryId, Timestamp beginTime, Timestamp endTime) throws ServiceException {
		String hql = "select Count(distinct BbsReply)" +
					 " from BbsReply BbsReply";
		String where = "not (BbsReply.created is null)";
		if(beginTime!=null) {
			where += " and BbsReply.created>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")";
		}
		if(endTime!=null) {
			where += " and BbsReply.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(endTime, null) + ")";
		}
		if(directoryId>0) {
			hql += ",BbsArticleSubjection BbsArticleSubjection, BbsDirectorySubjection BbsDirectorySubjection";
			where += " and BbsReply.articleId=BbsArticleSubjection.articleId" +
					 " and BbsArticleSubjection.forumId=BbsDirectorySubjection.directoryId" +
					 " and BbsDirectorySubjection.parentDirectoryId=" + directoryId;
		}
		hql += " where " + where;
		Object total = getDatabaseService().findRecordByHql(hql);
		return (total==null ? 0 : ((Number)total).intValue());
	}

	/**
	 * @return the memberService
	 */
	public MemberService getMemberService() {
		return memberService;
	}

	/**
	 * @param memberService the memberService to set
	 */
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	/**
	 * @return the bbsArticleService
	 */
	public BbsArticleService getBbsArticleService() {
		return bbsArticleService;
	}

	/**
	 * @param bbsArticleService the bbsArticleService to set
	 */
	public void setBbsArticleService(BbsArticleService bbsArticleService) {
		this.bbsArticleService = bbsArticleService;
	}
}
