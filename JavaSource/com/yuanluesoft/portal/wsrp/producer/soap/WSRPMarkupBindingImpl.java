package com.yuanluesoft.portal.wsrp.producer.soap;

import java.rmi.RemoteException;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.portal.wsrp.producer.WSRPProducer;

import oasis.names.tc.wsrp.v1.bind.WSRP_v1_Markup_Binding_SOAPImpl;
import oasis.names.tc.wsrp.v1.types.AccessDeniedFault;
import oasis.names.tc.wsrp.v1.types.BlockingInteractionResponse;
import oasis.names.tc.wsrp.v1.types.Extension;
import oasis.names.tc.wsrp.v1.types.GetMarkup;
import oasis.names.tc.wsrp.v1.types.InconsistentParametersFault;
import oasis.names.tc.wsrp.v1.types.InitCookie;
import oasis.names.tc.wsrp.v1.types.InvalidCookieFault;
import oasis.names.tc.wsrp.v1.types.InvalidHandleFault;
import oasis.names.tc.wsrp.v1.types.InvalidRegistrationFault;
import oasis.names.tc.wsrp.v1.types.InvalidSessionFault;
import oasis.names.tc.wsrp.v1.types.InvalidUserCategoryFault;
import oasis.names.tc.wsrp.v1.types.MarkupResponse;
import oasis.names.tc.wsrp.v1.types.MissingParametersFault;
import oasis.names.tc.wsrp.v1.types.OperationFailedFault;
import oasis.names.tc.wsrp.v1.types.PerformBlockingInteraction;
import oasis.names.tc.wsrp.v1.types.PortletStateChangeRequiredFault;
import oasis.names.tc.wsrp.v1.types.ReleaseSessions;
import oasis.names.tc.wsrp.v1.types.UnsupportedLocaleFault;
import oasis.names.tc.wsrp.v1.types.UnsupportedMimeTypeFault;
import oasis.names.tc.wsrp.v1.types.UnsupportedModeFault;
import oasis.names.tc.wsrp.v1.types.UnsupportedWindowStateFault;

/**
 * 标记接口（必选）： 标记接口允许 WSRP 消费者同 WSRP 生产者的远程运行的 portlet 进行交互。
 * 例如，当用户通过门户页面提供一个表单时需要使用这个接口执行一些交互。
 * 另外，门户可能需要根据 portlet 当前的状态来获取最新的标记（例如当用户点击刷新或者与当前页面的另一个 portlet 进行交互的时候）。
 * @author linchuan
 *
 */
public class WSRPMarkupBindingImpl extends WSRP_v1_Markup_Binding_SOAPImpl {

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Markup_Binding_SOAPImpl#getMarkup(oasis.names.tc.wsrp.v1.types.GetMarkup)
	 */
	public MarkupResponse getMarkup(GetMarkup getMarkup) throws RemoteException, InconsistentParametersFault, InvalidRegistrationFault, MissingParametersFault, OperationFailedFault, UnsupportedMimeTypeFault, UnsupportedModeFault, UnsupportedLocaleFault, InvalidUserCategoryFault, InvalidSessionFault, InvalidCookieFault, AccessDeniedFault, InvalidHandleFault, UnsupportedWindowStateFault {
		try {
			return WSRPProducer.getWSRPProducer().getMarkup(getMarkup);
		}
		catch(RemoteException e) {
			throw e;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Markup_Binding_SOAPImpl#performBlockingInteraction(oasis.names.tc.wsrp.v1.types.PerformBlockingInteraction)
	 */
	public BlockingInteractionResponse performBlockingInteraction(PerformBlockingInteraction performBlockingInteraction) throws RemoteException, InconsistentParametersFault, InvalidRegistrationFault, MissingParametersFault, OperationFailedFault, UnsupportedMimeTypeFault, UnsupportedModeFault, UnsupportedLocaleFault, InvalidUserCategoryFault, InvalidSessionFault, InvalidCookieFault, PortletStateChangeRequiredFault, AccessDeniedFault, InvalidHandleFault, UnsupportedWindowStateFault {
		try {
			return WSRPProducer.getWSRPProducer().performBlockingInteraction(performBlockingInteraction);
		}
		catch(RemoteException e) {
			throw e;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Markup_Binding_SOAPImpl#initCookie(oasis.names.tc.wsrp.v1.types.InitCookie)
	 */
	public Extension[] initCookie(InitCookie initCookie) throws RemoteException, InvalidRegistrationFault, OperationFailedFault, AccessDeniedFault {
		try {
			return WSRPProducer.getWSRPProducer().initCookie(initCookie);
		}
		catch(RemoteException e) {
			throw e;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_Markup_Binding_SOAPImpl#releaseSessions(oasis.names.tc.wsrp.v1.types.ReleaseSessions)
	 */
	public Extension[] releaseSessions(ReleaseSessions releaseSessions) throws RemoteException, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault {
		try {
			return WSRPProducer.getWSRPProducer().releaseSessions(releaseSessions);
		}
		catch(RemoteException e) {
			throw e;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}
}