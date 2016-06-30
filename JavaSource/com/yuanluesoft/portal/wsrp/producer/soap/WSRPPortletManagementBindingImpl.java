package com.yuanluesoft.portal.wsrp.producer.soap;

import java.rmi.RemoteException;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.portal.wsrp.producer.WSRPProducer;

import oasis.names.tc.wsrp.v1.bind.WSRP_v1_PortletManagement_Binding_SOAPImpl;
import oasis.names.tc.wsrp.v1.types.AccessDeniedFault;
import oasis.names.tc.wsrp.v1.types.ClonePortlet;
import oasis.names.tc.wsrp.v1.types.DestroyPortlets;
import oasis.names.tc.wsrp.v1.types.DestroyPortletsResponse;
import oasis.names.tc.wsrp.v1.types.GetPortletDescription;
import oasis.names.tc.wsrp.v1.types.GetPortletProperties;
import oasis.names.tc.wsrp.v1.types.GetPortletPropertyDescription;
import oasis.names.tc.wsrp.v1.types.InconsistentParametersFault;
import oasis.names.tc.wsrp.v1.types.InvalidHandleFault;
import oasis.names.tc.wsrp.v1.types.InvalidRegistrationFault;
import oasis.names.tc.wsrp.v1.types.InvalidUserCategoryFault;
import oasis.names.tc.wsrp.v1.types.MissingParametersFault;
import oasis.names.tc.wsrp.v1.types.OperationFailedFault;
import oasis.names.tc.wsrp.v1.types.PortletContext;
import oasis.names.tc.wsrp.v1.types.PortletDescriptionResponse;
import oasis.names.tc.wsrp.v1.types.PortletPropertyDescriptionResponse;
import oasis.names.tc.wsrp.v1.types.PropertyList;
import oasis.names.tc.wsrp.v1.types.SetPortletProperties;

/**
 * Portlet 管理接口（可选）： Portlet 管理接口使 WSRP 消费者可以访问远程运行的 portlet 的生命周期。
 * 通过这个接口，消费者具备定制 portlet 行为甚至是销毁一个远程运行的 portlet 实例的能力。
 * @author linchuan
 *
 */
public class WSRPPortletManagementBindingImpl extends WSRP_v1_PortletManagement_Binding_SOAPImpl {

	/* (non-Javadoc)
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_PortletManagement_Binding_SOAPImpl#clonePortlet(oasis.names.tc.wsrp.v1.types.ClonePortlet)
	 */
	public PortletContext clonePortlet(ClonePortlet clonePortlet) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		try {
			return WSRPProducer.getWSRPProducer().clonePortlet(clonePortlet);
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
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_PortletManagement_Binding_SOAPImpl#destroyPortlets(oasis.names.tc.wsrp.v1.types.DestroyPortlets)
	 */
	public DestroyPortletsResponse destroyPortlets(DestroyPortlets destroyPortlets) throws RemoteException, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault {
		try {
			return WSRPProducer.getWSRPProducer().destroyPortlets(destroyPortlets);
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
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_PortletManagement_Binding_SOAPImpl#getPortletDescription(oasis.names.tc.wsrp.v1.types.GetPortletDescription)
	 */
	public PortletDescriptionResponse getPortletDescription(GetPortletDescription getPortletDescription) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		try {
			return WSRPProducer.getWSRPProducer().getPortletDescription(getPortletDescription);
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
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_PortletManagement_Binding_SOAPImpl#getPortletProperties(oasis.names.tc.wsrp.v1.types.GetPortletProperties)
	 */
	public PropertyList getPortletProperties(GetPortletProperties getPortletProperties) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		try {
			return WSRPProducer.getWSRPProducer().getPortletProperties(getPortletProperties);
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
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_PortletManagement_Binding_SOAPImpl#getPortletPropertyDescription(oasis.names.tc.wsrp.v1.types.GetPortletPropertyDescription)
	 */
	public PortletPropertyDescriptionResponse getPortletPropertyDescription(GetPortletPropertyDescription getPortletPropertyDescription) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		try {
			return WSRPProducer.getWSRPProducer().getPortletPropertyDescription(getPortletPropertyDescription);
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
	 * @see oasis.names.tc.wsrp.v1.bind.WSRP_v1_PortletManagement_Binding_SOAPImpl#setPortletProperties(oasis.names.tc.wsrp.v1.types.SetPortletProperties)
	 */
	public PortletContext setPortletProperties(SetPortletProperties setPortletProperties) throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, InvalidHandleFault {
		try {
			return WSRPProducer.getWSRPProducer().setPortletProperties(setPortletProperties);
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