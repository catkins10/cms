package com.yuanluesoft.cms.photocollect.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.photocollect.pojo.PhotoCollect;
import com.yuanluesoft.cms.photocollect.pojo.PhotoCollectCategory;
import com.yuanluesoft.cms.photocollect.service.PhotoCollectService;
import com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PhotoCollectServiceImpl extends PublicServiceImpl implements PhotoCollectService {
	private SiteResourceService siteResourceService; //站点资源服务
	private ImageService imageService; //图片服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof PhotoCollectCategory) {
			getExchangeClient().synchUpdate(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof PhotoCollectCategory) {
			getExchangeClient().synchUpdate(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof PhotoCollectCategory) {
			getExchangeClient().synchDelete(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
		super.delete(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.photocollect.service.PhotoCollectService#listCategories(long)
	 */
	public List listCategories(long siteId) throws ServiceException {
		String hql = "from PhotoCollectCategory PhotoCollectCategory" +
					 " where PhotoCollectCategory.siteId=" + siteId + 
					 " order By PhotoCollectCategory.category";
		List types = getDatabaseService().findRecordsByHql(hql);
		if(siteId==0 || (types!=null && !types.isEmpty())) { //配置不为空
			return types;
		}
		//配置为空,获取上级站点的配置
		List parentDirectories = getSiteService().listParentDirectories(siteId, null);
		if(parentDirectories!=null && !parentDirectories.isEmpty()) {
			for(int i=parentDirectories.size() - 1; i>=0; i--) {
				WebDirectory site = (WebDirectory)parentDirectories.get(i);
				if(!"site".equals(site.getDirectoryType())) {
					continue;
				}
				hql = "from PhotoCollectCategory PhotoCollectCategory" +
					  " where PhotoCollectCategory.siteId=" + site.getId() + 
					  " order By PhotoCollectCategory.category";
				types = getDatabaseService().findRecordsByHql(hql);
				if(types!=null && !types.isEmpty()) { //配置不为空
					return types;
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.photocollect.service.PhotoCollectService#synchSiteColumns(com.yuanluesoft.cms.photocollect.pojo.PhotoCollect, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void synchSiteColumns(PhotoCollect photoCollect, SessionInfo sessionInfo) throws ServiceException {
		if(photoCollect.getPublicPass()!='1') { //不发布
			siteResourceService.deleteResourceBySourceRecordId("" + photoCollect.getId());
			return;
		}
		//获取分类配置
		String hql = "from PhotoCollectCategory PhotoCollectCategory where PhotoCollectCategory.category='" + JdbcUtils.resetQuot(photoCollect.getCategory()) + "'";
		PhotoCollectCategory photoCollectCategory = (PhotoCollectCategory)getDatabaseService().findRecordByHql(hql);
		if(photoCollectCategory==null || photoCollectCategory.getColumnIds()==null || photoCollectCategory.getColumnIds().isEmpty()) {
			return;
		}
		List images = imageService.list("cms/photocollect", "image", photoCollect.getId(), false, 0, null);
		String body = "<p><center>";
		
		for(int i=0; i<(images==null ? 0 : images.size()); i++) {
			Image image = (Image)images.get(i);
			if(i>0) {
				body += "<br/><br/>";
			}
			body += "<img" + (image.getWidth()<=800 ? "" : " onclick=\"window.open(src)\" width=\"800\"") + " src=\"" + image.getUrlInline() + "\" />";
		}
		body += "</center></p>";
		siteResourceService.addResource(
				photoCollectCategory.getColumnIds(),
				SiteResourceService.RESOURCE_TYPE_ARTICLE, 
				photoCollect.getPublicSubject(), //标题
				null, //副标题
				null,
				null,
				null,
				null,
				SiteResourceService.ANONYMOUS_LEVEL_ALL,
				null,
				photoCollect.getCreated(), //创建时间
				DateTimeUtils.now(), //发布时间
				true, //是否发布
				body, //正文
				"" + photoCollect.getId(), //源记录ID
				PhotoCollect.class.getName(),
				Environment.getContextPath() + "/cms/photocollect/admin/photoCollect.shtml?id=" + photoCollect.getId(),
				sessionInfo.getUserId(),
				sessionInfo.getUserName(),
				sessionInfo.getDepartmentId(),
				sessionInfo.getDepartmentName(),
				sessionInfo.getUnitId(),
				sessionInfo.getUnitName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		WebSite parentSite = (WebSite)request.getAttribute("parentSite");
		List types = listCategories(parentSite==null ? 0 : parentSite.getId());
		if(types==null || types.isEmpty()) {
			return types;
		}
		//转换为Object[]列表
		for(int i=0; i<types.size(); i++) {
			PhotoCollectCategory category = (PhotoCollectCategory)types.get(i);
			types.set(i, new Object[]{category.getCategory(), category.getCategory()});
		}
		return types;
	}

	/**
	 * @return the siteResourceService
	 */
	public SiteResourceService getSiteResourceService() {
		return siteResourceService;
	}

	/**
	 * @param siteResourceService the siteResourceService to set
	 */
	public void setSiteResourceService(SiteResourceService siteResourceService) {
		this.siteResourceService = siteResourceService;
	}

	/**
	 * @return the imageService
	 */
	public ImageService getImageService() {
		return imageService;
	}

	/**
	 * @param imageService the imageService to set
	 */
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}
}