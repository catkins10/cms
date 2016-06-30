package com.yuanluesoft.cms.infopublic.soap;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.jeaf.soap.BaseSoapService;

/**
 * 
 * @author linchuan
 *
 */
public class PublicInfoSoapService  extends BaseSoapService {

	/**
	 * 获取子目录列表,返回格式:[目录名称]|[目录类型,其中"info"类型用来存放信息]|[目录ID]
	 * @param parentDirectoryId 根目录ID为0
	 * @return
	 * @throws Exception
	 */
	public String listChildDirectories(long parentDirectoryId) throws Exception {
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getSpringService("publicDirectoryService");
		List childDirectories = publicDirectoryService.listChildDirectories(parentDirectoryId, null, null, null, false, false, null, 0, 0);
		if(childDirectories==null || childDirectories.isEmpty()) {
			return null;
		}
		String result = null;
		for(Iterator iterator = childDirectories.iterator(); iterator.hasNext();) {
			PublicDirectory publicDirectory = (PublicDirectory)iterator.next();
			result = (result==null ? "" : result + ",") + publicDirectory.getDirectoryName() + "|" + publicDirectory.getDirectoryType() + "|" + publicDirectory.getId();
		}
		return result;
	}
} 