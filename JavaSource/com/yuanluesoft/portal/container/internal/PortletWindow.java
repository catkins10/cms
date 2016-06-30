/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuanluesoft.portal.container.internal;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

/**
 * 
 * @author linchuan
 *
 */
public class PortletWindow {
	private String portletInstanceId; //PORTLET实例ID
	private String wsrpProducerId; //WSRP生产者ID
	private String portletHandle; //PORTLET句柄
	private WindowState windowState; //窗口状态
	private PortletMode portletMode; //模式
	private long userId; //用户ID
	private long siteId; //站点ID
	private PortletURLGenerator portletURLGenerator; //portletURL生成器,wsrp生产者调用时,需要指定自己的生成器

	//窗口标题
	private String portletTitle; //允许由RenderResponse动态设置Portlet标题

	public PortletWindow(String portletInstanceId, String wsrpProducerId, String portletHandle, WindowState windowState, PortletMode portletMode, long userId, long siteId, PortletURLGenerator portletURLGenerator) {
		super();
		this.portletInstanceId = portletInstanceId;
		this.wsrpProducerId = wsrpProducerId;
		this.portletHandle = portletHandle;
		this.windowState = windowState;
		this.portletMode = portletMode;
		this.userId = userId;
		this.siteId = siteId;
		this.portletURLGenerator = portletURLGenerator;
	}

	/**
	 * @return the portletHandle
	 */
	public String getPortletHandle() {
		return portletHandle;
	}

	/**
	 * @param portletHandle the portletHandle to set
	 */
	public void setPortletHandle(String portletHandle) {
		this.portletHandle = portletHandle;
	}

	/**
	 * @return the portletInstanceId
	 */
	public String getPortletInstanceId() {
		return portletInstanceId;
	}

	/**
	 * @param portletInstanceId the portletInstanceId to set
	 */
	public void setPortletInstanceId(String portletInstanceId) {
		this.portletInstanceId = portletInstanceId;
	}

	/**
	 * @return the portletMode
	 */
	public PortletMode getPortletMode() {
		return portletMode;
	}

	/**
	 * @param portletMode the portletMode to set
	 */
	public void setPortletMode(PortletMode portletMode) {
		this.portletMode = portletMode;
	}

	/**
	 * @return the portletTitle
	 */
	public String getPortletTitle() {
		return portletTitle;
	}

	/**
	 * @param portletTitle the portletTitle to set
	 */
	public void setPortletTitle(String portletTitle) {
		this.portletTitle = portletTitle;
	}

	/**
	 * @return the windowState
	 */
	public WindowState getWindowState() {
		return windowState;
	}

	/**
	 * @param windowState the windowState to set
	 */
	public void setWindowState(WindowState windowState) {
		this.windowState = windowState;
	}

	/**
	 * @return the wsrpProducerId
	 */
	public String getWsrpProducerId() {
		return wsrpProducerId;
	}

	/**
	 * @param wsrpProducerId the wsrpProducerId to set
	 */
	public void setWsrpProducerId(String wsrpProducerId) {
		this.wsrpProducerId = wsrpProducerId;
	}

	/**
	 * @return the portletURLGenerator
	 */
	public PortletURLGenerator getPortletURLGenerator() {
		return portletURLGenerator;
	}

	/**
	 * @param portletURLGenerator the portletURLGenerator to set
	 */
	public void setPortletURLGenerator(PortletURLGenerator portletURLGenerator) {
		this.portletURLGenerator = portletURLGenerator;
	}

	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
}