/*
 * Created on 2006-6-13
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class ReverseExit extends BaseExit {
    private List reverseActivityInstances; //可回退的环节实例列表
    
    /**
     * @return Returns the reverseActivityInstances.
     */
    public List getReverseActivityInstances() {
        return reverseActivityInstances;
    }
    /**
     * @param reverseActivityInstances The reverseActivityInstances to set.
     */
    public void setReverseActivityInstances(List reverseActivityInstances) {
        this.reverseActivityInstances = reverseActivityInstances;
    }
}
