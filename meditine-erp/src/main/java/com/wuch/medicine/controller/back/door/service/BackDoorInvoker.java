package com.wuch.medicine.controller.back.door.service;

import com.wuch.medicine.controller.back.door.domain.DomainParam;
import com.wuch.medicine.controller.back.door.domain.ServiceDescription;
import com.wuch.medicine.controller.back.door.utils.SpringContext;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wubo5 on 2017/11/9.
 */

public class BackDoorInvoker {
    private static Logger logger = LoggerFactory.getLogger(BackDoorInvoker.class);
    private ServiceDescription serviceDescription;

    public BackDoorInvoker(ServiceDescription serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
    //包装serviceDescription
    public void makeServiceDescription() throws IllegalAccessException {
        checkServiceDescriptionParam(serviceDescription);
        String beanName = serviceDescription.getBeanName();
        String serviceName = serviceDescription.getServiceName();
        // 实例
        Object obj =  SpringContext.getStringBean(beanName);
        Class clazz = SpringContext.getStringBeanType(beanName);
        String clazzName = clazz.getName();
        //得到参数
        String param[] = getMethodParamNames(clazzName,serviceName);
        if(null == serviceDescription.getDomainParamList()){
            serviceDescription.initDomainParamList();
            //补充param元素
            for (int i = 0; i < param.length; i++) {
                DomainParam domainParam = new DomainParam();
                serviceDescription.getDomainParamList().add(domainParam);
            }

        }
        Method method = getMethod(clazz,serviceName);
        //得到方法-参数类
        Class[] paramTypes =  getMethodParamTypes(method);
        if(null != param && null!=paramTypes && param.length!=paramTypes.length){
            throw new IllegalAccessException("param and paramTypes checkError");
        }

        for (int i = 0; i < param.length; i++) {
            Class cls = paramTypes[i];
            serviceDescription.getDomainParamList().get(i).setName(param[i]);
            serviceDescription.getDomainParamList().get(i).setType(cls);
            //设置参数属性(不是基本类型)
            if (!cls.getName().startsWith("java")) {
                List<DomainParam> list = makeFields(cls);
                serviceDescription.getDomainParamList().get(i).setSubDomainParam(list);
            }
        }
    }

    private List<DomainParam> makeFields(Class clazz) {
        List<DomainParam> domainParamList =new ArrayList<DomainParam>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if(Modifier.isStatic(field.getModifiers())) continue;
            DomainParam domainParam = new DomainParam();
            domainParam.setType(field.getType());
            domainParam.setName(field.getName());
            domainParamList.add(domainParam);
        }
        return domainParamList;
    }


    private Class[] getMethodParamTypes(Method method) {
                Class[] paramTypes =  method.getParameterTypes();
           return paramTypes;
    }

    public void checkServiceDescriptionParam(ServiceDescription serviceDescription) throws IllegalArgumentException {
        if(StringUtils.isBlank(serviceDescription.getBeanName())){
            throw new IllegalArgumentException("ERROR: serviceDescription.beanName is null");
        }
        if(StringUtils.isBlank(serviceDescription.getServiceName())){
            throw new IllegalArgumentException("ERROR: serviceDescription.servicseName is null");
        }
    }

//    public static void main(String[] args) {
//        BackDoorInvoker backDoorInvoker = new BackDoorInvoker(new ServiceDescription());
//        try {
//            backDoorInvoker.makeServiceDescription();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取：方法-参数名称
     * @param clazzName
     * @param methodName
     * @return
     */
    private String[] getMethodParamNames(String clazzName, String methodName) {
        logger.error("getMethodParamNames clazzName is {},serviceName is {}",clazzName,methodName);
        String[] paramNames = null;
        try {
            //获取字节
            ClassPool pool = ClassPool.getDefault();
            /**
             * begin
             * 由静态方法ClassPool.getDefault（）返回的默认ClassPool搜索底层JVM（Java虚拟机）具有的相同路径。
             * 如果程序在JBoss和Tomcat等Web应用程序服务器上运行，则ClassPool对象可能无法找到用户类，
             * 因为这样的Web应用程序服务器使用多个类加载器以及系统类加载器。 在这种情况下，
             * 一个额外的类路径必须注册到ClassPool
             */
            ClassClassPath classPath = new ClassClassPath(this.getClass());
            pool.insertClassPath(classPath);
            //end
            String orgClassName = getOrgClassName(clazzName);
            logger.error("getMethodParamNames orgClassName is {}",orgClassName);

            CtClass cc = pool.get(orgClassName);
            CtMethod cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute =  methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute)  codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                logger.error("getMethodParameterNames attr error, className=" + clazzName + "method=" + methodName);
            }
            paramNames = new String[cm.getParameterTypes().length];
//			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            int begin = 0;
            int count = 0;
            for (int i = 0; i < attr.tableLength(); i++) {
                // 为什么 加这个判断，发现在windows 跟linux执行时，参数顺序不一致，通过观察，实际的参数是从this后面开始的
                if (attr.variableName(i).equals("this")) {
                    begin = i;
                    break;
                }
            }
            for (int i = begin + 1; i <= begin + paramNames.length; i++) {
                paramNames[count] = attr.variableName(i);
                count++;
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return paramNames;
    }

    private String getOrgClassName(String clazzName) {
        int index$ = clazzName.indexOf('$');
        if(-1 == index$){
            return clazzName;
        }else{
            return clazzName.substring(0,index$);
        }
    }

    Method getMethod(Class clazz, String method){
        Method selectMethod = null;
        Method[] methods =   clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            String currentMethodName = methods[i].getName();
            if(!currentMethodName.equals(method)){
                continue;
            }
            selectMethod = methods[i];
        }
        return selectMethod;
    }

}


