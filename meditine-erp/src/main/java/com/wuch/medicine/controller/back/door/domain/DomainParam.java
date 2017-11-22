package com.wuch.medicine.controller.back.door.domain;

import java.util.List;

/**
 * Created by wubo5 on 2017/11/9.
 */
public class DomainParam {
    private Class<?> type;
    private String name;
    private String value;
    private List<DomainParam> subDomainParam;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<DomainParam> getSubDomainParam() {
        return subDomainParam;
    }

    public void setSubDomainParam(List<DomainParam> subDomainParam) {
        this.subDomainParam = subDomainParam;
    }

    @Override
    public String toString() {
        return "DomainParam{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", subDomainParam=" + subDomainParam +
                '}';
    }
}
