package com.yuanluesoft.portal.wsrp;

/**
 * 
 * @author linchuan
 *
 */
public final class Constants {
    // parameter names
    public static final String URL_TYPE = "wsrp-urlType";
    public static final String NAVIGATIONAL_STATE = "wsrp-navigationalState"; //base64编码后的render参数
    public static final String INTERACTION_STATE = "wsrp-interactionState"; //base64编码后的action参数
    public static final String WINDOW_STATE = "wsrp-windowState";
    public static final String PORTLET_MODE = "wsrp-mode";
    public static final String URL = "wsrp-url";
    public static final String FRAGMENT_ID = "wsrp-fragmentID";
    public static final String SECURE_URL = "wsrp-secureURL";
    public static final String REWRITE_RESOURCE = "wsrp-requiresRewrite";
    public static final String FORM_PARAMETERS = "wsrp-formParameters";
    public static final String PORTLET_HANDLE = "wsrp-portletHandle";
    public static final String USER_CONTEXT_KEY = "wsrp-userContextKey";
    public static final String PORTLET_INSTANCE_KEY = "wsrp-portletInstanceKey";
    public static final String SESSION_ID = "wsrp-sessionID";
    
    //url类型
    public static final String URL_TYPE_BLOCKINGACTION = "blockingAction";
    public static final String URL_TYPE_RENDER = "render";
    public static final String URL_TYPE_RESOURCE = "resource";
    
    //身份认证
    public static final String WSRP_AUTHENTICATE_NONE = "wsrp:none";
    public static final String WSRP_AUTHENTICATE_PASSWD = "wsrp:password";
    public static final String WSRP_AUTHENTICATE_CERTIFICATE = "wsrp:certificate";
}