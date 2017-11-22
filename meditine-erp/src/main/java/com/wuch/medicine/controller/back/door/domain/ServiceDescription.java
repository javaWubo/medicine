package com.wuch.medicine.controller.back.door.domain;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wubo5 on 2017/11/9.
 */
public class ServiceDescription {
    private String beanName;
    private String serviceName;
    private List<DomainParam> domainParamList ;
    private String jsonResult = "data is null";

    public ServiceDescription() {
    }

    public ServiceDescription(String beanName, String serviceName) {
        this.beanName = beanName;
        this.serviceName = serviceName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<DomainParam> getDomainParamList() {
        return domainParamList;
    }

    public void setDomainParamList(List<DomainParam> domainParamList) {
        this.domainParamList = domainParamList;
    }

    public List<DomainParam> initDomainParamList() {
        if(null == domainParamList){
            domainParamList = new ArrayList<DomainParam>();
        }
        return domainParamList;
    }

    public void clearDomainParamList() {
        domainParamList = null;
    }
}

