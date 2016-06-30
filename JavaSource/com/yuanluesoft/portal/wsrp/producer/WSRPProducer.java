package com.yuanluesoft.portal.wsrp.producer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;

import oasis.names.tc.wsrp.v1.intf.WSRP_v1_Markup_PortType;
import oasis.names.tc.wsrp.v1.intf.WSRP_v1_PortletManagement_PortType;
import oasis.names.tc.wsrp.v1.intf.WSRP_v1_Registration_PortType;
import oasis.names.tc.wsrp.v1.intf.WSRP_v1_ServiceDescription_PortType;
import oasis.names.tc.wsrp.v1.types.AccessDeniedFault;
import oasis.names.tc.wsrp.v1.types.BlockingInteractionResponse;
import oasis.names.tc.wsrp.v1.types.ClonePortlet;
import oasis.names.tc.wsrp.v1.types.DestroyPortlets;
import oasis.names.tc.wsrp.v1.types.DestroyPortletsResponse;
import oasis.names.tc.wsrp.v1.types.Extension;
import oasis.names.tc.wsrp.v1.types.GetMarkup;
import oasis.names.tc.wsrp.v1.types.GetPortletDescription;
import oasis.names.tc.wsrp.v1.types.GetPortletProperties;
import oasis.names.tc.wsrp.v1.types.GetPortletPropertyDescription;
import oasis.names.tc.wsrp.v1.types.GetServiceDescription;
import oasis.names.tc.wsrp.v1.types.InconsistentParametersFault;
import oasis.names.tc.wsrp.v1.types.InitCookie;
import oasis.names.tc.wsrp.v1.types.InvalidCookieFault;
import oasis.names.tc.wsrp.v1.types.InvalidHandleFault;
import oasis.names.tc.wsrp.v1.types.InvalidRegistrationFault;
import oasis.names.tc.wsrp.v1.types.InvalidSessionFault;
import oasis.names.tc.wsrp.v1.types.InvalidUserCategoryFault;
import oasis.names.tc.wsrp.v1.types.MarkupContext;
import oasis.names.tc.wsrp.v1.types.MarkupParams;
import oasis.names.tc.wsrp.v1.types.MarkupResponse;
import oasis.names.tc.wsrp.v1.types.MissingParametersFault;
import oasis.names.tc.wsrp.v1.types.ModifyRegistration;
import oasis.names.tc.wsrp.v1.types.NamedString;
import oasis.names.tc.wsrp.v1.types.OperationFailedFault;
import oasis.names.tc.wsrp.v1.types.PerformBlockingInteraction;
import oasis.names.tc.wsrp.v1.types.PortletContext;
import oasis.names.tc.wsrp.v1.types.PortletDescription;
import oasis.names.tc.wsrp.v1.types.PortletDescriptionResponse;
import oasis.names.tc.wsrp.v1.types.PortletPropertyDescriptionResponse;
import oasis.names.tc.wsrp.v1.types.PortletStateChangeRequiredFault;
import oasis.names.tc.wsrp.v1.types.Property;
import oasis.names.tc.wsrp.v1.types.PropertyList;
import oasis.names.tc.wsrp.v1.types.RegistrationContext;
import oasis.names.tc.wsrp.v1.types.RegistrationData;
import oasis.names.tc.wsrp.v1.types.RegistrationState;
import oasis.names.tc.wsrp.v1.types.ReleaseSessions;
import oasis.names.tc.wsrp.v1.types.RuntimeContext;
import oasis.names.tc.wsrp.v1.types.ServiceDescription;
import oasis.names.tc.wsrp.v1.types.SetPortletProperties;
import oasis.names.tc.wsrp.v1.types.UnsupportedLocaleFault;
import oasis.names.tc.wsrp.v1.types.UnsupportedMimeTypeFault;
import oasis.names.tc.wsrp.v1.types.UnsupportedModeFault;
import oasis.names.tc.wsrp.v1.types.UnsupportedWindowStateFault;
import oasis.names.tc.wsrp.v1.types.UpdateResponse;
import oasis.names.tc.wsrp.v1.types.UserContext;

import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;
import org.apache.axis.transport.http.HTTPConstants;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.portal.container.internal.PortletWindow;
import com.yuanluesoft.portal.container.pojo.PortletEntity;
import com.yuanluesoft.portal.container.service.PortletContainer;
import com.yuanluesoft.portal.container.service.PortletDefinitionService;
import com.yuanluesoft.portal.wsrp.producer.internal.PortletURLGenerator;
import com.yuanluesoft.portal.wsrp.producer.internal.ProducerRequest;
import com.yuanluesoft.portal.wsrp.producer.internal.Registration;

/**
 * 
 * @author linchuan
 *
 */
public class WSRPProducer implements WSRP_v1_Registration_PortType, WSRP_v1_Markup_PortType, WSRP_v1_PortletManagement_PortType, WSRP_v1_ServiceDescription_PortType {
	private PortletContainer portletContainer; //portlet容器
	private PortletDefinitionService portletDefinitionService; //portlet定义服务
	private SessionService sessionService; //会话服务
	
	//私有属性
	private Map registrations = new HashMap(); //注册记录
	
	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Registration_Binding_SOAPImpl#register(oasis.names.tc.wsrp.v1.types.RegistrationData)
	 */
	public RegistrationContext register(RegistrationData registrationData) throws RemoteException, OperationFailedFault, MissingParametersFault {
        // create the registration context
        RegistrationContext registrationContext = new RegistrationContext();
        registrationContext.setRegistrationHandle("" + UUIDLongGenerator.generateId()); //句柄
        registrationContext.setRegistrationState(null);
        registrationContext.setExtensions(null);
        // add new registration to hashmap
        registrations.put(registrationContext.getRegistrationHandle(), new Registration(registrationContext, registrationData));
        return registrationContext;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Registration_Binding_SOAPImpl#modifyRegistration(oasis.names.tc.wsrp.v1.types.ModifyRegistration)
	 */
	public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws RemoteException, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault {
		Registration registration = (Registration)registrations.get(modifyRegistration.getRegistrationContext().getRegistrationHandle());
		if(registration==null) {
			return null;
		}
		registration.setRegistrationData(modifyRegistration.getRegistrationData());
		RegistrationState registrationState = new RegistrationState();
		registrationState.setRegistrationState(registration.getRegistrationContext().getRegistrationState());
		return registrationState;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Registration_Binding_SOAPImpl#deregister(oasis.names.tc.wsrp.v1.types.RegistrationContext)
	 */
	public Extension[] deregister(RegistrationContext deregister) throws RemoteException, InvalidRegistrationFault, OperationFailedFault {
		registrations.remove(deregister.getRegistrationHandle());
		return new Extension[0];
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_ServiceDescription_PortType#getServiceDescription(oasis.names.tc.wsrp.v1.types.GetServiceDescription)
	 */
	public ServiceDescription getServiceDescription(GetServiceDescription getServiceDescription) throws RemoteException, InvalidRegistrationFault, OperationFailedFault {
		//获取注册信息
		RegistrationData registrationData = getRegistrationData(getServiceDescription.getRegistrationContext());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setRequiresRegistration(true); //是否需要注册
		if(registrationData==null) {
			return serviceDescription;
		}
        serviceDescription.setUserCategoryDescriptions(null);
        serviceDescription.setCustomUserProfileItemDescriptions(null);
        serviceDescription.setCustomWindowStateDescriptions(null);
        serviceDescription.setCustomModeDescriptions(null);
        serviceDescription.setRequiresInitCookie(null);
        serviceDescription.setRegistrationPropertyDescription(null);
        serviceDescription.setLocales(new String[]{Locale.SIMPLIFIED_CHINESE.getLanguage()});
        serviceDescription.setResourceList(null);
        serviceDescription.setExtensions(null); //扩展
        //获取portlet列表
        String userId = getRegistrationProperty(registrationData, "userId");
        String siteId = getRegistrationProperty(registrationData, "siteId");
        String userLoginName = getRegistrationProperty(registrationData, "userLoginName");
        List portletEntities = null;
		try {
			portletEntities = portletDefinitionService.listPortletEntities(userId==null ? -1 : Long.parseLong(userId),
																		   siteId==null ? -1 : Long.parseLong(siteId),
																		   userLoginName==null ? SessionService.ANONYMOUS_SESSION : sessionService.getSessionInfo(userLoginName));
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		if(portletEntities!=null) {
			List offeredPortlets = new ArrayList();
			for(int i=0; i<portletEntities.size(); i++) {
				PortletEntity portletEntity = (PortletEntity)portletEntities.get(i);
				PortletDescription portletDescription = null;
				try {
					portletDescription = portletContainer.getPortletDescription(null, "" + portletEntity.getId());
				}
				catch (ServiceException e) {
					Logger.exception(e);
				}
				if(portletDescription!=null) {
					offeredPortlets.add(portletDescription);
				}
		    }
			PortletDescription[] portletDescriptions = new PortletDescription[offeredPortlets.size()];
			offeredPortlets.toArray(portletDescriptions);
			serviceDescription.setOfferedPortlets(portletDescriptions);
		}
		return serviceDescription;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_Markup_PortType#getMarkup(oasis.names.tc.wsrp.v1.types.GetMarkup)
	 */
	public MarkupResponse getMarkup(GetMarkup getMarkup) throws RemoteException, InconsistentParametersFault, InvalidRegistrationFault, MissingParametersFault, OperationFailedFault, UnsupportedMimeTypeFault, UnsupportedModeFault, UnsupportedLocaleFault, InvalidUserCategoryFault, InvalidSessionFault, InvalidCookieFault, AccessDeniedFault, InvalidHandleFault, UnsupportedWindowStateFault {
		//构造请求
		ProducerRequest request = getProducerRequest(getMarkup.getUserContext(), getMarkup.getMarkupParams(), null);
		//构造portlet窗口
		PortletWindow portletWindow = getPortletWindow(getMarkup.getRuntimeContext(), getMarkup.getPortletContext(), getMarkup.getMarkupParams());
		//执行portlet render操作
		com.yuanluesoft.portal.container.internal.MarkupResponse response = null;
		try {
			response = portletContainer.doRender(portletWindow, request);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		MarkupResponse markupResponse = new MarkupResponse();
		markupResponse.setMarkupContext(getMarkupContext(response));
		markupResponse.setSessionContext(null);
		markupResponse.setExtensions(null);
		return markupResponse;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_Markup_PortType#performBlockingInteraction(oasis.names.tc.wsrp.v1.types.PerformBlockingInteraction)
	 */
	public BlockingInteractionResponse performBlockingInteraction(PerformBlockingInteraction performBlockingInteraction) throws RemoteException, InconsistentParametersFault, InvalidRegistrationFault, MissingParametersFault, OperationFailedFault, UnsupportedMimeTypeFault, UnsupportedModeFault, UnsupportedLocaleFault, InvalidUserCategoryFault, InvalidSessionFault, InvalidCookieFault, PortletStateChangeRequiredFault, AccessDeniedFault, InvalidHandleFault, UnsupportedWindowStateFault {
		//构造请求
		ProducerRequest request = getProducerRequest(performBlockingInteraction.getUserContext(), performBlockingInteraction.getMarkupParams(), performBlockingInteraction.getInteractionParams().getFormParameters());
		//构造portlet窗口
		PortletWindow portletWindow = getPortletWindow(performBlockingInteraction.getRuntimeContext(), performBlockingInteraction.getPortletContext(), performBlockingInteraction.getMarkupParams());
		//执行portlet render操作
		com.yuanluesoft.portal.container.internal.MarkupResponse response = null;
		try {
			response = portletContainer.doAction(portletWindow, request);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		BlockingInteractionResponse interactionResponse = new BlockingInteractionResponse();
		UpdateResponse updateResponse = new UpdateResponse();
		updateResponse.setMarkupContext(getMarkupContext(response));
		updateResponse.setNavigationalState(null);
		updateResponse.setNewMode(portletWindow.getPortletMode().toString());
		updateResponse.setNewWindowState(portletWindow.getWindowState().toString());
		updateResponse.setPortletContext(null);
		updateResponse.setSessionContext(null);
		interactionResponse.setUpdateResponse(updateResponse);
		interactionResponse.setRedirectURL(null);
		interactionResponse.setExtensions(null);
		return interactionResponse;
	}
	
	/**
	 * 构造portlet窗口
	 * @param runtimeContext
	 * @param portletContext
	 * @param markupParams
	 * @return
	 */
	private PortletWindow getPortletWindow(RuntimeContext runtimeContext, PortletContext portletContext, MarkupParams markupParams) {
		String portletInstanceId = runtimeContext.getPortletInstanceKey();
		String portletHandle = portletContext.getPortletHandle();
		WindowState windowState = new WindowState(markupParams.getWindowState().replaceFirst("wsrp:", ""));
		PortletMode portletMode = new PortletMode(markupParams.getMode().replaceFirst("wsrp:", ""));
		PortletURLGenerator portletURLGenerator = new PortletURLGenerator(runtimeContext);
		String userId = getExtensionValue(markupParams.getExtensions(), "userId");
		String siteId = getExtensionValue(markupParams.getExtensions(), "siteId");
		return new PortletWindow(portletInstanceId, null, portletHandle, windowState, portletMode, (userId==null ? -1 : Long.parseLong(userId)), (siteId==null ? -1 : Long.parseLong(siteId)), portletURLGenerator);
	}
	
	/**
	 * 构造生产者请求
	 * @param userContext
	 * @param markupParams
	 * @return
	 */
	private ProducerRequest getProducerRequest(UserContext userContext, MarkupParams markupParams, NamedString[] formParameters) {
		//获取会话
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(userContext.getProfile().getName().getNickname());
		}
		catch(Exception e) {
			sessionInfo = SessionService.ANONYMOUS_SESSION;
		}
		//解析url参数
		String queryString = getExtensionValue(markupParams.getExtensions(), "queryString");
		if(queryString!=null) {
			queryString = new String(new Base64Decoder().decode(queryString));
		}
		//解析请求参数
		Map parameterMap = null;
		try {
			parameterMap = markupParams.getNavigationalState()==null || markupParams.getNavigationalState().isEmpty() ? null : (Map)ObjectSerializer.deserialize(new Base64Decoder().decode(markupParams.getNavigationalState()));
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		if(formParameters!=null && formParameters.length>0) { //表单参数不为空
			if(parameterMap==null) {
				parameterMap = new HashMap();
			}
			else {
				for(int i=0; i<formParameters.length; i++) {
					parameterMap.remove(formParameters[i].getName());
				}
			}
			for(int i=0; i<formParameters.length; i++) {
				String[] values = (String[])parameterMap.get(formParameters[i].getName());
				if(values==null) {
					values = new String[]{formParameters[i].getValue()};
				}
				else {
					String[] newValues = new String[values.length + 1];
					for(int j=0; j<values.length; j++) {
						newValues[j] = values[j];
					}
					newValues[values.length] = formParameters[i].getValue();
					values = newValues;
				}
				parameterMap.put(formParameters[i].getName(), values);
			}
		}
		//获取HTTP请求
		HttpServletRequest axisRequest = (HttpServletRequest)MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		return new ProducerRequest(axisRequest, queryString, getExtensionValue(markupParams.getExtensions(), "requestMethod"), parameterMap, sessionInfo);
	}
	
	/**
	 * 获取标记上下文
	 * @param response
	 * @return
	 */
	private MarkupContext getMarkupContext(com.yuanluesoft.portal.container.internal.MarkupResponse response) {
		MarkupContext markupContext = new MarkupContext();
		markupContext.setUseCachedMarkup(Boolean.FALSE);
		markupContext.setMimeType(response==null ? null : response.getContentType());
		markupContext.setMarkupString(response==null ? null : response.getMarkup());
		markupContext.setMarkupBinary(null);
		markupContext.setLocale("zh");
		markupContext.setRequiresUrlRewriting(Boolean.FALSE);
		markupContext.setCacheControl(null);
		markupContext.setPreferredTitle(null);
		markupContext.setExtensions(null);
		return markupContext;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_Markup_PortType#initCookie(oasis.names.tc.wsrp.v1.types.InitCookie)
	 */
	public Extension[] initCookie(InitCookie initCookie) throws RemoteException, InvalidRegistrationFault, OperationFailedFault, AccessDeniedFault {
		return null;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_Markup_PortType#releaseSessions(oasis.names.tc.wsrp.v1.types.ReleaseSessions)
	 */
	public Extension[] releaseSessions(ReleaseSessions releaseSessions) throws RemoteException, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault {
		return null;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_PortletManagement_PortType#clonePortlet(oasis.names.tc.wsrp.v1.types.ClonePortlet)
	 */
	public PortletContext clonePortlet(ClonePortlet clonePortlet) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		//管理接口暂不需要
		return null;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_PortletManagement_PortType#destroyPortlets(oasis.names.tc.wsrp.v1.types.DestroyPortlets)
	 */
	public DestroyPortletsResponse destroyPortlets(DestroyPortlets destroyPortlets) throws RemoteException, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault {
		//管理接口暂不需要
		return null;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_PortletManagement_PortType#getPortletDescription(oasis.names.tc.wsrp.v1.types.GetPortletDescription)
	 */
	public PortletDescriptionResponse getPortletDescription(GetPortletDescription getPortletDescription) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		PortletDescription portletDescription = null;
		try {
			portletDescription = portletContainer.getPortletDescription(null, getPortletDescription.getPortletContext().getPortletHandle());
		} 
		catch (ServiceException e) {
			Logger.exception(e);
		}
		PortletDescriptionResponse portletDescriptionResponse = new PortletDescriptionResponse();
		portletDescriptionResponse.setPortletDescription(portletDescription);
		return portletDescriptionResponse;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_PortletManagement_PortType#getPortletProperties(oasis.names.tc.wsrp.v1.types.GetPortletProperties)
	 */
	public PropertyList getPortletProperties(GetPortletProperties getPortletProperties) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		//管理接口暂不需要
		return null;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_PortletManagement_PortType#getPortletPropertyDescription(oasis.names.tc.wsrp.v1.types.GetPortletPropertyDescription)
	 */
	public PortletPropertyDescriptionResponse getPortletPropertyDescription(GetPortletPropertyDescription getPortletPropertyDescription) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		//管理接口暂不需要
		return null;
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.intf.WSRP_v1_PortletManagement_PortType#setPortletProperties(oasis.names.tc.wsrp.v1.types.SetPortletProperties)
	 */
	public PortletContext setPortletProperties(SetPortletProperties setPortletProperties) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		//管理接口暂不需要
		return null;
	}
	
	/**
	 * 获取WSRP生产者
	 *
	 */
	public static WSRPProducer getWSRPProducer() {
		try {
			return (WSRPProducer)Environment.getService("wsrpProducer");
		}
		catch (ServiceException e) {
			Logger.exception(e);
			return null;
		}
	}
	
	/**
	 * 获取注册信息
	 * @param registrationContext
	 * @return
	 */
	private RegistrationData getRegistrationData(RegistrationContext registrationContext) {
		if(registrationContext==null || registrationContext.getRegistrationHandle()==null) {
			return null;
		}
		Registration registration = (Registration)registrations.get(registrationContext.getRegistrationHandle());
		return registration==null ? null : registration.getRegistrationData();
	}
	
	/**
	 * 从注册信息获取属性
	 * @param registrationData
	 * @param propertyName
	 * @return
	 */
	private String getRegistrationProperty(RegistrationData registrationData, String propertyName) {
		Property[] properties = registrationData.getRegistrationProperties();
		if(properties==null) {
			return null;
		}
		for(int i=0; i<properties.length; i++) {
			if(propertyName.equals(properties[i].getName())) {
				return properties[i].getStringValue();
			}
		}
		return null;
	}
	
	/**
	 * 获取扩展参数
	 * @param extensions
	 * @param name
	 * @return
	 */
	protected String getExtensionValue(Extension[] extensions, String name) {
		for(int i=0; i<(extensions==null ? 0 : extensions.length); i++) {
			MessageElement[] elements = extensions[i].get_any();
			for(int j=0; j<(elements==null ? 0 : elements.length); j++) {
				if(name.equals(elements[j].getName())) {
					return elements[j].getValue();
				}
			}
		}
		return null;
	}

	/**
	 * @return the portletContainer
	 */
	public PortletContainer getPortletContainer() {
		return portletContainer;
	}

	/**
	 * @param portletContainer the portletContainer to set
	 */
	public void setPortletContainer(PortletContainer portletContainer) {
		this.portletContainer = portletContainer;
	}

	/**
	 * @return the portletDefinitionService
	 */
	public PortletDefinitionService getPortletDefinitionService() {
		return portletDefinitionService;
	}

	/**
	 * @param portletDefinitionService the portletDefinitionService to set
	 */
	public void setPortletDefinitionService(
			PortletDefinitionService portletDefinitionService) {
		this.portletDefinitionService = portletDefinitionService;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
}