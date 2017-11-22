package com.wuch.medicine.controller.back.door.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wubo5 on 2017/11/8.
 */
public class SpringContext {


    private static Map<String,Object> classInstances = null;
    private static  WebApplicationContext CONTEXT = null;
    private SpringContext() {
    }
    private static ReentrantLock reentrantLock = new ReentrantLock();

    public static WebApplicationContext getContextSingle(){
        try{
            reentrantLock.lock();
            if(null == CONTEXT){
                CONTEXT = ContextLoader.getCurrentWebApplicationContext();
            }
        }catch (Exception e){

        }finally {
            reentrantLock.unlock();
        }
        return CONTEXT;
    }
    public static Object getStringBean(String beanName){
        if(StringUtils.isBlank(beanName)){
            return null;
        }
        initContextMethod();
        Object obj = CONTEXT.getBean(beanName);
       return  obj;
    }

    public static Class<?> getStringBeanType(String beanName){
        if(StringUtils.isBlank(beanName)){
            return null;
        }
        initContextMethod();
        Class aClass = CONTEXT.getType(beanName);
        return  aClass;
    }

    public static Class<?> getStringBeanType1(String beanName){
        return (Class)classInstances.get(beanName);
    }



    public static void initContextMethod(){
        System.out.println("initmethod ------------- run");
        if(null == CONTEXT){
            CONTEXT = getContextSingle();
        }
    }

    public enum InitContextMethodSingle{
        context;
        private WebApplicationContext contextSingle;
        private InitContextMethodSingle(){
            contextSingle = ContextLoader.getCurrentWebApplicationContext();
        }
        public WebApplicationContext getContextSingle(){
            return contextSingle;
        }
    }

    public static void initContextMethod1(){
        if(null == CONTEXT){
            CONTEXT = InitContextMethodSingle.context.getContextSingle();
        }
    }

    public static void getAllAnnotationIsService(){
        initContextMethod();

       classInstances = new HashMap<String,Object>();
//        if(this.applicationContext == null){
//            return;
//        }
//        if(this.classInstances == null){
//            this.classInstances = new HashMap<String,Object>();
//        }
        //获取有NotAutowired2Service注解的实例
//        Map<String, Object> beansWithNotAutowired2ServiceMap = this.applicationContext.getBeansWithAnnotation(com.lvbey.service.annotation.NotAutowired2Service.class);

        Map<String, Object> beansWithAnnotationMap = CONTEXT.getBeansWithAnnotation(org.springframework.stereotype.Service.class);

        Class<? extends Object> clazz = null;
        for(Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()){
            clazz = entry.getValue().getClass();//获取到实例对象的class信息
            Class<? extends Object>  [] interfaces = clazz.getInterfaces();
            for(Class<? extends Object>  aInterface : interfaces){
                String aInterfaceName = aInterface.getName();//接口的完整名
//                for(String packageName : this.basePackages){
//                    if(aInterfaceName.startsWith(packageName)){//如果这个接口是在指定的包下
//
//                        //接口实例名(只是将接口的首字母换成小写)
                        String aInterfaceSimpleName = getDefaultInstanceName(aInterface);
//
//                        //如果这个接口没有NotServiceBean注解
//                        if(beansWithNotAutowired2ServiceMap.containsValue(entry.getValue())){
//                            System.out.println(entry.getValue() + " has NotAutowired2Service Annotation");
//                        }else{
                            classInstances.put(aInterfaceSimpleName, entry.getValue());
                        }
//                    }
//                }
            }
//        }
    }

    /**
     * 根据这个类来获取默认的实例名（默认：将首字母换成小写）
     *
     * @param clazz
     *            类信息
     * @return 默认的实例名
     */
    public static String getDefaultInstanceName(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        String className = clazz.getSimpleName();
        String firsrLowerChar = className.substring(0, 1).toLowerCase();
        className = firsrLowerChar + className.substring(1);
        return className;
    }


}
