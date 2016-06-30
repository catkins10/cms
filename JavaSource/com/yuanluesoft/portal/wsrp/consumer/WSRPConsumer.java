package com.yuanluesoft.portal.wsrp.consumer;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import oasis.names.tc.wsrp.v1.intf.WSRP_v1_Markup_PortType;
import oasis.names.tc.wsrp.v1.intf.WSRP_v1_PortletManagement_PortType;
import oasis.names.tc.wsrp.v1.intf.WSRP_v1_Registration_PortType;
import oasis.names.tc.wsrp.v1.intf.WSRP_v1_ServiceDescription_PortType;
import oasis.names.tc.wsrp.v1.types.BlockingInteractionResponse;
import oasis.names.tc.wsrp.v1.types.ClientData;
import oasis.names.tc.wsrp.v1.types.Contact;
import oasis.names.tc.wsrp.v1.types.EmployerInfo;
import oasis.names.tc.wsrp.v1.types.Extension;
import oasis.names.tc.wsrp.v1.types.GetMarkup;
import oasis.names.tc.wsrp.v1.types.GetPortletDescription;
import oasis.names.tc.wsrp.v1.types.GetServiceDescription;
import oasis.names.tc.wsrp.v1.types.InitCookie;
import oasis.names.tc.wsrp.v1.types.InteractionParams;
import oasis.names.tc.wsrp.v1.types.MarkupParams;
import oasis.names.tc.wsrp.v1.types.MarkupResponse;
import oasis.names.tc.wsrp.v1.types.ModifyRegistration;
import oasis.names.tc.wsrp.v1.types.NamedString;
import oasis.names.tc.wsrp.v1.types.PerformBlockingInteraction;
import oasis.names.tc.wsrp.v1.types.PersonName;
import oasis.names.tc.wsrp.v1.types.PortletContext;
import oasis.names.tc.wsrp.v1.types.PortletDescription;
import oasis.names.tc.wsrp.v1.types.PortletDescriptionResponse;
import oasis.names.tc.wsrp.v1.types.Property;
import oasis.names.tc.wsrp.v1.types.RegistrationContext;
import oasis.names.tc.wsrp.v1.types.RegistrationData;
import oasis.names.tc.wsrp.v1.types.ReleaseSessions;
import oasis.names.tc.wsrp.v1.types.RuntimeContext;
import oasis.names.tc.wsrp.v1.types.ServiceDescription;
import oasis.names.tc.wsrp.v1.types.StateChange;
import oasis.names.tc.wsrp.v1.types.Templates;
import oasis.names.tc.wsrp.v1.types.UserContext;
import oasis.names.tc.wsrp.v1.types.UserProfile;
import oasis.names.tc.wsrp.v1.wsdl.WSRPServiceLocator;

import org.apache.axis.message.MessageElement;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.portal.container.internal.ActionResponseImpl;
import com.yuanluesoft.portal.container.internal.PortletWindow;
import com.yuanluesoft.portal.wsrp.Constants;

/**
 * 
 * @author linchuan
 *
 */
public class WSRPConsumer {
	private String wsrpProducerId; //WSRP生产者ID
	private String wsrpMarkupURL; //标记接口URL
	private String wsrpPortletManagementURL; //Portlet管理接口URL
	private String wsrpRegistrationURL; //注册接口URL
	private String wsrpServiceDescriptionURL; //服务描述接口URL
	
	//私有属性
	private WSRP_v1_Markup_PortType markupPort; //标记接口
	private WSRP_v1_PortletManagement_PortType portletManagementPort; //Portlet管理接口
	private WSRP_v1_Registration_PortType registrationPort; //注册接口
	private WSRP_v1_ServiceDescription_PortType serviceDescriptionPort; //服务描述接口
	private RegistrationContext registrationContext;
	
	/**
	 * 销毁
	 *
	 */
	public void destroy() {
		try {
			deregister();
		}
		catch (Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 执行远程portlet的render方法
	 * @param portletWindow
	 * @param renderRequest
	 * @param renderResponse
	 * @throws PortletException
	 * @throws IOException
	 */
	public void renderPortlet(PortletWindow portletWindow, RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
		//构造请求
		GetMarkup request = new GetMarkup();
		//WSRP PORTLET上下文
		request.setPortletContext(getPortletContext(portletWindow));
		//运行时上下文
		request.setRuntimeContext(getRuntimeContext(portletWindow, renderRequest));
		//参数
		request.setMarkupParams(getMarkupParams(portletWindow, renderRequest));
		//注册信息上下文
		request.setRegistrationContext(register(false, -1, -1, null));
		//用户上下文
		request.setUserContext(getUserContext((SessionInfo)renderRequest.getAttribute(PortletRequest.USER_INFO)));
		//远程调用获取标记方法
		MarkupResponse response = getMarkupPort().getMarkup(request);
		//输出到renderResponse
		renderResponse.setContentType(response.getMarkupContext().getMimeType());
		renderResponse.getWriter().write(response.getMarkupContext().getMarkupString());
	}

	/**
	 * 处理portlet操作
	 * @param portletWindow
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortletException
	 * @throws IOException
	 */
	public void processAction(PortletWindow portletWindow, ActionRequest actionRequest, ActionResponseImpl actionResponse) throws PortletException, IOException {
		PerformBlockingInteraction request = new PerformBlockingInteraction();
		//注册信息上下文
		request.setRegistrationContext(register(false, -1, -1, null));
		//WSRP PORTLET上下文
		request.setPortletContext(getPortletContext(portletWindow));
		//运行时上下文
		request.setRuntimeContext(getRuntimeContext(portletWindow, actionRequest));
		//用户上下文
		request.setUserContext(getUserContext((SessionInfo)actionRequest.getAttribute(PortletRequest.USER_INFO)));
		//参数
		request.setMarkupParams(getMarkupParams(portletWindow, actionRequest));
        //互动参数
		request.setInteractionParams(getInteractionParams(portletWindow, actionRequest));
		
		//远程调用
		BlockingInteractionResponse response = markupPort.performBlockingInteraction(request);
		
		//输出到actionResponse
		if(response.getUpdateResponse().getNewMode()!=null) { //模式
			actionResponse.setPortletMode(new PortletMode(response.getUpdateResponse().getNewMode()));
		}
		if(response.getUpdateResponse().getNewWindowState()!=null) { //窗口状态
			actionResponse.setWindowState(new WindowState(response.getUpdateResponse().getNewWindowState()));
		}
		response.setRedirectURL(response.getRedirectURL()); //重定向URL
		if(response.getUpdateResponse().getNavigationalState()!=null) { //参数
			try {
				actionResponse.setRenderParameters((Map)ObjectSerializer.deserialize(new Base64Decoder().decode(response.getUpdateResponse().getNavigationalState())));
			} 
			catch (Exception e) {
				Logger.exception(e);
			}
		}
		//属性
		for(int i=0; i<(response.getExtensions()==null ? 0 : response.getExtensions().length); i++) {
			Extension extension = response.getExtensions()[i];
			MessageElement[] elements = extension.get_any();
			for(int j=0; j<(elements==null ? 0 : elements.length); j++) {
				actionResponse.addProperty(elements[j].getName(), elements[j].getValue());
			}
		}
		//输出内容
		if(response.getUpdateResponse().getMarkupContext()!=null) {
			actionResponse.setContentType(response.getUpdateResponse().getMarkupContext().getMimeType());
			actionResponse.getWriter().write(response.getUpdateResponse().getMarkupContext().getMarkupString());
		}
	}
	
	/**
	 * 获取远程portlet列表
	 * @param siteId
	 * @param userId
	 * @param sessionInfo
	 * @return
	 * @throws RemoteException
	 * @throws PortletException
	 */
	public PortletDescription[] listRemotePortlets(long siteId, long userId, SessionInfo sessionInfo) throws RemoteException, PortletException {
		GetServiceDescription request = new GetServiceDescription();
		request.setRegistrationContext(register(true, siteId, userId, sessionInfo.getLoginName()));
		ServiceDescription serviceDescription = getServiceDescriptionPortPort().getServiceDescription(request);
		return serviceDescription==null ? null : serviceDescription.getOfferedPortlets();
	}
	
	/**
	 * 获取远程portlet定义
	 * @param portletHandle
	 * @return
	 * @throws RemoteException
	 * @throws PortletException
	 */
	public PortletDescription getRemotePortletDescription(String portletHandle) throws RemoteException, PortletException {
		GetPortletDescription request = new GetPortletDescription();
		//设置PORTLET上下文
		PortletContext portletContext = new PortletContext();
		portletContext.setPortletHandle(portletHandle); //portlet句柄
		request.setPortletContext(portletContext);
		//设置注册信息
		request.setRegistrationContext(register(false, -1, -1, null));
		//设置用户上下文
		request.setUserContext(getUserContext(SessionService.ANONYMOUS_SESSION));
		PortletDescriptionResponse reponse = getPortletManagementPort().getPortletDescription(request);
		return reponse==null ? null : reponse.getPortletDescription();
	}
	
	/**
	 * 初始化COOKIE
	 * @throws RemoteException
	 * @throws PortletException
	 */
	protected void initCookie() throws RemoteException, PortletException {
		InitCookie request = new InitCookie();
		request.setRegistrationContext(register(false, -1, -1, null));
		getMarkupPort().initCookie(request);
	}
	
	/**
	 * 是否会话
	 * @param sessionIDs
	 * @throws RemoteException
	 * @throws PortletException
	 */
	protected void releaseSessions(String[] sessionIDs) throws RemoteException, PortletException {
		ReleaseSessions request = new ReleaseSessions();
		request.setRegistrationContext(register(false, -1, -1, null));
		request.setSessionIDs(sessionIDs);
		getMarkupPort().releaseSessions(request);
	}
	
	/**
	 * 注册
	 * @param forceRegister
	 * @param siteId
	 * @param userId
	 * @param userLoginName
	 * @return
	 * @throws RemoteException
	 * @throws PortletException
	 */
	private RegistrationContext register(boolean forceRegister, long siteId, long userId, String userLoginName) throws RemoteException, PortletException {
		if(!forceRegister && registrationContext!=null) {
			return registrationContext;
		}
		RegistrationData registrationData = new RegistrationData();
		registrationData.setConsumerName("Yuanlue Portal Server");
		registrationData.setConsumerAgent("Yuanlue Portal WRSP Consumer");
		registrationData.setMethodGetSupported(false); //A flag that tells the Producer whether the Consumer has implemented portlet URLs (regardless of whether they are written through Consumer URL rewriting or Producer URL writing, see section 10.2) in a manner that supports HTML markup containing forms with method=”get”.
        registrationData.setConsumerModes(new String[]{"wsrp:view", "wsrp:edit"});
        registrationData.setConsumerWindowStates(new String[]{"wsrp:normal", "wsrp:minimized"});
        registrationData.setConsumerUserScopes(new String[] {"wsrp:perUser", "wsrp:forAll"});
        registrationData.setCustomUserProfileData(null);
        Property[] properties = new Property[userLoginName==null ? 2 : 3];
        properties[0] = new Property("" + siteId, null, "siteId", "en");
        properties[1] = new Property("" + userId, null, "userId", "en");
        if(userLoginName!=null) {
        	properties[2] = new Property(userLoginName, null, "userLoginName", "zh");
        }
        registrationData.setRegistrationProperties(properties);
        registrationData.setExtensions(null);
        if(registrationContext==null) { //未注册
        	registrationContext = getRegistrationPort().register(registrationData);
        }
        else { //已经注册过,修改注册
	        ModifyRegistration request = new ModifyRegistration();
			request.setRegistrationContext(registrationContext);
			request.setRegistrationData(registrationData);
			getRegistrationPort().modifyRegistration(request);
        }
        return registrationContext;
	}

	/**
	 * 注销
	 * @throws RemoteException
	 * @throws PortletException
	 */
	private void deregister() throws RemoteException, PortletException {
		if(registrationContext!=null) {
			getRegistrationPort().deregister(registrationContext);
		}
	}
	
	/**
	 * 获取portlet上下文
	 * @param portletWindow
	 * @return
	 */
	private PortletContext getPortletContext(PortletWindow portletWindow)  {
		PortletContext portletContext = new PortletContext();
		portletContext.setPortletHandle(portletWindow.getPortletHandle()); //portlet句柄
		portletContext.setPortletState(portletWindow.getWindowState().toString().getBytes()); //状态
		portletContext.setExtensions(null); //扩展属性
		return portletContext;
	}
	
	/**
	 * 获取运行时上下文
	 * @param portletWindow
	 * @param portletRequest
	 * @return
	 */
	private RuntimeContext getRuntimeContext(PortletWindow portletWindow, PortletRequest portletRequest) {
		RuntimeContext runtimeContext = new RuntimeContext();
		runtimeContext.setUserAuthentication(Constants.WSRP_AUTHENTICATE_NONE); //wsrp:none/wsrp:password/wsrp:certificate, String indicating how end-users were authenticated by the consumer
		runtimeContext.setNamespacePrefix("portlet" + "_" + portletWindow.getPortletInstanceId() + "_");
		runtimeContext.setPortletInstanceKey(portletWindow.getPortletInstanceId());
		runtimeContext.setSessionID(portletRequest.getPortletSession(true).getId());
		//URL模板
		Templates templates = new Templates();
		templates.setBlockingActionTemplate(generateBlockingActionTemplate(portletWindow, false, true, true, true, true));
		templates.setDefaultTemplate(generateDefaultTemplate(portletWindow, false, true, true, true, true));
		templates.setRenderTemplate(generateRenderTemplate(portletWindow, false, true, true, true, true));
		templates.setResourceTemplate(generateResourceTemplate(portletWindow, false, true, true, true, true));
		templates.setSecureBlockingActionTemplate(generateBlockingActionTemplate(portletWindow, true, true, true, true, true));
		templates.setSecureDefaultTemplate(generateDefaultTemplate(portletWindow, true, true, true, true, true));
		templates.setSecureRenderTemplate(generateRenderTemplate(portletWindow, true, true, true, true, true));
		templates.setSecureResourceTemplate(generateResourceTemplate(portletWindow, true, true, true, true, true));
		runtimeContext.setTemplates(templates);
		runtimeContext.setExtensions(null); //扩展属性
		return runtimeContext;
	}
	
	/**
	 * 生成参数
	 * @param portletWindow
	 * @param portletRequest
	 * @return
	 */
	private MarkupParams getMarkupParams(PortletWindow portletWindow, PortletRequest portletRequest) {
		MarkupParams markupParams = new MarkupParams();
		ClientData clientData = new ClientData();
        clientData.setUserAgent("Yuanlue Portal WSRP Portlet");
        markupParams.setClientData(clientData);
        markupParams.setSecureClientCommunication(false);
        markupParams.setLocales(new String[]{Locale.SIMPLIFIED_CHINESE.getLanguage()});
        markupParams.setMimeTypes(new String[] { "text/html" });
        markupParams.setMode("wsrp:" + portletWindow.getPortletMode().toString());
        markupParams.setWindowState("wsrp:" + portletWindow.getWindowState().toString());
        try {
			markupParams.setNavigationalState(new Base64Encoder().encode(ObjectSerializer.serialize((Serializable)portletRequest.getParameterMap())));
		}
        catch (IOException e) {
			Logger.exception(e);
		}
        markupParams.setMarkupCharacterSets(new String[] { "utf-8" });
        markupParams.setValidateTag(null);
        markupParams.setValidNewModes(new String[]{"wsrp:view", "wsrp:edit"}); //wsrp:help
        markupParams.setValidNewWindowStates(new String[]{"wsrp:normal", "wsrp:minimized"}); //wsrp:maximized
        //扩展参数
        List extensions = new ArrayList(); 
        //将queryString加为扩展参数
        String queryString = ((HttpServletRequest)portletRequest).getQueryString();
        if(queryString!=null && !queryString.isEmpty()) {
        	extensions.add(new Extension(new MessageElement[]{new MessageElement(new QName("queryString"), new Base64Encoder().encode(queryString.getBytes()))}));
        }
        //添加request方法
        extensions.add(new Extension(new MessageElement[]{new MessageElement(new QName("requestMethod"), ((HttpServletRequest)portletRequest).getMethod())}));
        //添加用户ID
        if(portletWindow.getUserId()!=-1) {
        	extensions.add(new Extension(new MessageElement[]{new MessageElement(new QName("userId"), "" + portletWindow.getUserId())}));
        }
        //添加站点ID
        if(portletWindow.getSiteId()!=-1) {
        	extensions.add(new Extension(new MessageElement[]{new MessageElement(new QName("siteId"), "" + portletWindow.getSiteId())}));
        }
        if(!extensions.isEmpty()) {
	        Extension[] extensionArray = new Extension[extensions.size()];
	        for(int i=0; i<extensions.size(); i++) {
	        	extensionArray[i] = (Extension)extensions.get(i);
	        }
	        markupParams.setExtensions(extensionArray);
        }
        return markupParams;
	}
	
	/**
	 * 生成互动参数
	 * @param portletWindow
	 * @param actionRequest
	 * @return
	 */
	private InteractionParams getInteractionParams(PortletWindow portletWindow, ActionRequest actionRequest) {
		InteractionParams params = new InteractionParams();
		params.setPortletStateChange(StateChange.cloneBeforeWrite);
		params.setInteractionState(actionRequest.getWindowState().toString());
		//设置表单参数列表
        ArrayList formParams = new ArrayList();
        Enumeration parameterNames = actionRequest.getParameterNames();
        while(parameterNames.hasMoreElements()) {
        	String name = (String)parameterNames.nextElement();
        	String[] values = actionRequest.getParameterValues(name);
        	for(int i=0; i<(values==null ? 0 : values.length); i++) {
	        	NamedString parameter = new NamedString();
	        	parameter.setName(name);
	        	parameter.setValue(values[i]);
	        	formParams.add(parameter);
        	}
        }
        NamedString[] formParamArray = new NamedString[formParams.size()];
        formParams.toArray(formParamArray);
        params.setFormParameters(formParamArray);
		params.setUploadContexts(null);
		params.setExtensions(null);
		return params;
	}
	
	/**
	 * 获取用户上下文
	 * @param sessionInfo
	 * @return
	 */
	private UserContext getUserContext(SessionInfo sessionInfo) {
        UserContext userContext = new UserContext();
        userContext.setUserContextKey(sessionInfo.getLoginName());
        userContext.setExtensions(null);
        userContext.setUserCategories(null);
        
        UserProfile userProfile = new UserProfile();
        userProfile.setBdate(new GregorianCalendar(1970, 1, 1));
        userProfile.setEmployerInfo(new EmployerInfo());
        userProfile.setExtensions(null);
        userProfile.setGender("mmh");
        userProfile.setHomeInfo(new Contact());
        userProfile.setBusinessInfo(new Contact());
        
        PersonName personName = new PersonName();
        personName.setGiven(sessionInfo.getUserName()); //教名
        personName.setFamily(sessionInfo.getUserName()); //姓
        personName.setMiddle(null); //中名
        personName.setNickname(sessionInfo.getLoginName()); //昵称
        userProfile.setName(personName);
        userContext.setProfile(userProfile);
        return userContext;
    }
	
	/**
	 * 获取标记接口
	 * @return
	 * @throws PortletException
	 * @throws IOException
	 */
	private WSRP_v1_Markup_PortType getMarkupPort() throws PortletException {
		if(markupPort!=null) {
			return markupPort;
		}
		try {
			markupPort = new WSRPServiceLocator().getWSRPBaseService(new URL(wsrpMarkupURL));
		}
		catch(Exception e) {
			throw new PortletException(e);
		}
		return markupPort;
	}
	
	/**
	 * 获取portlet管理接口
	 * @return
	 * @throws PortletException
	 * @throws IOException
	 */
	protected WSRP_v1_PortletManagement_PortType getPortletManagementPort() throws PortletException {
		if(portletManagementPort!=null) {
			return portletManagementPort;
		}
		try {
			portletManagementPort = new WSRPServiceLocator().getWSRPPortletManagementService(new URL(wsrpPortletManagementURL));
		}
		catch(Exception e) {
			throw new PortletException(e);
		}
		return portletManagementPort;
	}
	
	/**
	 * 获取注册接口
	 * @return
	 * @throws PortletException
	 * @throws IOException
	 */
	private WSRP_v1_Registration_PortType getRegistrationPort() throws PortletException {
		if(registrationPort!=null) {
			return registrationPort;
		}
		try {
			registrationPort = new WSRPServiceLocator().getWSRPRegistrationService(new URL(wsrpRegistrationURL));
		}
		catch(Exception e) {
			throw new PortletException(e);
		}
		return registrationPort;
	}
	
	/**
	 * 获取服务描述接口
	 * @return
	 * @throws PortletException
	 * @throws IOException
	 */
	private WSRP_v1_ServiceDescription_PortType getServiceDescriptionPortPort() throws PortletException {
		if(serviceDescriptionPort!=null) {
			return serviceDescriptionPort;
		}
		try {
			serviceDescriptionPort = new WSRPServiceLocator().getWSRPServiceDescriptionService(new URL(wsrpServiceDescriptionURL));
		}
		catch(Exception e) {
			throw new PortletException(e);
		}
		return serviceDescriptionPort;
	}
	
	/**
	 * 生成render url模板
	 * @return
	 */
	public String generateRenderTemplate(PortletWindow portletWindow, boolean isSecure, boolean includePortletHandle, boolean includeUserContextKey, boolean includePortletInstanceKey, boolean includeSessionID) {
		return generateTemplate(Constants.URL_TYPE_RENDER, portletWindow, isSecure, includePortletHandle, includeUserContextKey, includePortletInstanceKey, includeSessionID);
	}
	
	/**
	 * 生成render url模板
	 * @return
	 */
	public String generateBlockingActionTemplate(PortletWindow portletWindow, boolean isSecure, boolean includePortletHandle, boolean includeUserContextKey, boolean includePortletInstanceKey, boolean includeSessionID) {
		return generateTemplate(Constants.URL_TYPE_BLOCKINGACTION, portletWindow, isSecure, includePortletHandle, includeUserContextKey, includePortletInstanceKey, includeSessionID);
	}
	
	/**
	 * 生成默认模板
	 * @param isSecure
	 * @return
	 */
	public String generateDefaultTemplate(PortletWindow portletWindow, boolean isSecure, boolean includePortletHandle, boolean includeUserContextKey, boolean includePortletInstanceKey, boolean includeSessionID) {
		return generateTemplate(null, portletWindow, isSecure, includePortletHandle, includeUserContextKey, includePortletInstanceKey, includeSessionID);
	}
	
	/**
	 * 生成资源URL模板
	 * @param isSecure
	 * @return
	 */
	public String generateResourceTemplate(PortletWindow portletWindow, boolean isSecure, boolean includePortletHandle, boolean includeUserContextKey, boolean includePortletInstanceKey, boolean includeSessionID) {
		return generateTemplate(Constants.URL_TYPE_RESOURCE, portletWindow, isSecure, includePortletHandle, includeUserContextKey, includePortletInstanceKey, includeSessionID);
	}
	
	/**
	 * 生成URL模板
	 * @param portletWindow
	 * @param isSecure
	 * @param urlType Constants.URL_TYPE_BLOCKINGACTION/Constants.URL_TYPE_RENDER/Constants.URL_TYPE_RESOURCE
	 * @return
	 */
	private String generateTemplate(String urlType, PortletWindow portletWindow, boolean isSecure, boolean includePortletHandle, boolean includeUserContextKey, boolean includePortletInstanceKey, boolean includeSessionID) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Environment.getContextPath() + "/portal/portlet.shtml");
		//WRSP生产者ID
		buffer.append("?wsrpProducerId=" + portletWindow.getWsrpProducerId());
		//PORTLET实例ID
		if(!includePortletInstanceKey) {
			buffer.append("&portletInstanceKey=" + portletWindow.getPortletInstanceId());
		}
		//PORTLET句柄
		if(!includePortletHandle) {
			buffer.append("&portletHandle=" + portletWindow.getPortletHandle());
		}
	    buffer.append("&urlType={" + Constants.URL_TYPE + "}");
        if(!Constants.URL_TYPE_RESOURCE.equals(urlType)) { //不是资源URL
	        buffer.append("&portletMode={" + Constants.PORTLET_MODE + "}");
	        buffer.append("&windowState={" + Constants.WINDOW_STATE + "}");
	    }
        if(Constants.URL_TYPE_RENDER.equals(urlType)) { //render URL
        	buffer.append("&navigationalState={" + Constants.NAVIGATIONAL_STATE + "}"); //base64编码后的render参数
        }
        else if(Constants.URL_TYPE_BLOCKINGACTION.equals(urlType)) { //render URL //action url
        	buffer.append("&interactionState={" + Constants.INTERACTION_STATE + "}"); //base64编码后的action参数
        }
        if(isSecure) {
        	buffer.append("&secureURL={" + Constants.SECURE_URL + "}");
        }
        if(urlType==null || Constants.URL_TYPE_RESOURCE.equals(urlType)) { //默认URL或者资源URL
        	buffer.append("&resourceUrl={" + Constants.URL + "}");
        	buffer.append("&rewriteResource={" + Constants.REWRITE_RESOURCE + "}");
        }
        if(includePortletHandle) {
        	buffer.append("&portletHandle={" + Constants.PORTLET_HANDLE + "}");
        }
        if(includeUserContextKey) {
        	buffer.append("&userContextKey={" + Constants.USER_CONTEXT_KEY + "}");
        }
        if(includePortletInstanceKey) {
        	buffer.append("&portletInstanceKey={" + Constants.PORTLET_INSTANCE_KEY + "}");
        }
        if(includeSessionID) {
        	buffer.append("&sessionId={" + Constants.SESSION_ID + "}");
        }
		//用户ID
		if(portletWindow.getUserId()!=-1) {
			buffer.append("&portalUserId=" + portletWindow.getUserId());
		}
		//站点ID
		if(portletWindow.getSiteId()!=-1) {
			buffer.append("&portalSiteId=" + portletWindow.getSiteId());
		}
        return buffer.toString();
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
	 * @return the wsrpMarkupURL
	 */
	public String getWsrpMarkupURL() {
		return wsrpMarkupURL;
	}

	/**
	 * @param wsrpMarkupURL the wsrpMarkupURL to set
	 */
	public void setWsrpMarkupURL(String wsrpMarkupURL) {
		this.wsrpMarkupURL = wsrpMarkupURL;
	}

	/**
	 * @return the wsrpPortletManagementURL
	 */
	public String getWsrpPortletManagementURL() {
		return wsrpPortletManagementURL;
	}

	/**
	 * @param wsrpPortletManagementURL the wsrpPortletManagementURL to set
	 */
	public void setWsrpPortletManagementURL(String wsrpPortletManagementURL) {
		this.wsrpPortletManagementURL = wsrpPortletManagementURL;
	}

	/**
	 * @return the wsrpRegistrationURL
	 */
	public String getWsrpRegistrationURL() {
		return wsrpRegistrationURL;
	}

	/**
	 * @param wsrpRegistrationURL the wsrpRegistrationURL to set
	 */
	public void setWsrpRegistrationURL(String wsrpRegistrationURL) {
		this.wsrpRegistrationURL = wsrpRegistrationURL;
	}

	/**
	 * @return the wsrpServiceDescriptionURL
	 */
	public String getWsrpServiceDescriptionURL() {
		return wsrpServiceDescriptionURL;
	}

	/**
	 * @param wsrpServiceDescriptionURL the wsrpServiceDescriptionURL to set
	 */
	public void setWsrpServiceDescriptionURL(String wsrpServiceDescriptionURL) {
		this.wsrpServiceDescriptionURL = wsrpServiceDescriptionURL;
	}
}