package com.wuch.medicine.controller.back.door;

import com.mongodb.util.JSON;
import com.wuch.medicine.controller.back.door.domain.ServiceDescription;
import com.wuch.medicine.controller.back.door.service.BackDoorInvoker;
import com.wuch.medicine.controller.back.door.utils.AopTargetUtil;
import com.wuch.medicine.controller.back.door.utils.SpringContext;
import com.wuch.medicine.domain.Medicine;
import com.wuch.medicine.service.MedicineService;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.objectweb.asm.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;

/**
 * Created by wubo5 on 2017/11/8.
 */
@Controller
@RequestMapping("/back")
public class BackController {
    private static Logger logger = LoggerFactory.getLogger(BackController.class);
    @RequestMapping("/test")
    public void test(String beanNameR,String serviceName) throws Exception {
//        MedicineService medicineService =  (MedicineService) SpringContext.getStringBean(beanNameR);
        String beanName = "com.wuch.medicine.service.impl.medicineService";
        Class clazz = SpringContext.getStringBeanType(beanNameR);
//        medicineService.queryMedicineService();
        Method method = getMethod(clazz,serviceName);
        //得到参数名
        String [] filedName = getMethodParamNames(clazz.getName(),serviceName);
        System.out.println("filedName is "+Arrays.toString(filedName));
        //TODO WB MESS 有时间研究begin--------------------------------
//        String [] filedName1 = getMethodParameterNamesByAsm4(clazz,method);
//        System.out.println("filedName1 is "+Arrays.toString(filedName1));
        /**
         * 从字节码文件中得到方法名
         */
//         Object clazzRelay =  (Object) AopTargetUtil.getTarget(medicineService);
//        System.out.println("clazzRelay  is ");
        /**
         * TODO WB  方法1 得到class
         */
//        SpringContext.getAllAnnotationIsService();
//        Class clazz = SpringContext.getStringBeanType1(beanNameR);
        //TODO WB MESS 有时间研究 end--------------------------------
    }


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

    Method getMethod(Class clazz,String method){
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

    public  String[] getMethodParameterNamesByAsm4(Class<?> clazz, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return null;
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];

        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(lastDotIndex + 1) + ".class";
        InputStream is = clazz.getResourceAsStream(className);
        try {
            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM4) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return null;
                    }
                    return new MethodVisitor(Opcodes.ASM4) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                            // 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
                            if (Modifier.isStatic(method.getModifiers())) {
                                parameterNames[index] = name;
                            }
                            else if (index > 0) {
                                parameterNames[index - 1] = name;
                            }
                        }
                    };

                }
            }, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameterNames;
    }

    public static void main(String[] args) {
        String beanName = "com.wuch.medicine.domain.Medicine";
        try {
            Class clazz =  Class.forName(beanName);
            Field[] fileds =  clazz.getDeclaredFields();
            System.out.println("--------------- fileds is "+Arrays.toString(fileds));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //----------------------------------------- 华丽的分界线 --------------------------------------------------------//
    @RequestMapping("/toShowClassDetail")
    public String toQueryClass(@ModelAttribute("service") ServiceDescription serviceDescription){
        serviceDescription.clearDomainParamList();
        BackDoorInvoker backDoorInvoker = new BackDoorInvoker(serviceDescription);
        try {
            backDoorInvoker.makeServiceDescription();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "showClassDetail";
    }

    @RequestMapping("/toShowClassDetail1")
    public String toQueryClass1(HttpServletRequest request, Medicine medicine){

        return "showClassDetail";
    }


}
