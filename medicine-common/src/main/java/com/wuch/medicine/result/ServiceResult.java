package com.wuch.medicine.result;

/**
 * @author: wubo5
 * @create: 2016-08-24 17:07
 * @desc:
 */
public class ServiceResult<T> {
    public static final int SUCCESS = 1;
    public static final String SUCCESS_Msg="SUCCESS";
    public static final int FAIL = 0;
    public static final String FAIL_Msg="FAIL";

    private int code=FAIL;
    private String ErrorMsg=FAIL_Msg;
    private T data;

    public ServiceResult() {
    }

    public ServiceResult(int code, String errorMsg) {
        this.code = code;
        ErrorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {

        return "ServiceResult{" +
                "code=" + code +
                ", ErrorMsg='" + ErrorMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public static void main(String[] args) {
        ServiceResult<String> sr = new ServiceResult<String>();
        System.out.println(sr);
    }
}
