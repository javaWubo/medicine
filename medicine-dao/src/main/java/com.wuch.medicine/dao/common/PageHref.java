package com.wuch.medicine.dao.common;

/**
 * Created with IntelliJ IDEA.
 * User: zhanglu7
 * Date: 14-11-26
 * Time: 下午2:45
 * To change this template use File | Settings | File Templates.
 */
public class PageHref {
    private String hrefUrl;
    private int currentPage;
    private int pageSize;
    public PageHref() {
    }
    /**
    *
    * @param hrefUrl
    *
    **/
    public PageHref(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }

    /**
    *
    * getter of hrefurl
    *
    **/
    public String getHrefUrl() {
        return hrefUrl;
    }

    /**
    *
    * setter of hrefurl
    * @param hrefUrl
    *
    **/
    public PageHref setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
        return this;
    }

    /**
    *
    * getter of currentpage
    *
    **/
    public int getCurrentPage() {
        return currentPage;
    }

    /**
    *
    * setter of currentpage
    * @param currentPage
    *
    **/
    public PageHref setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    /**
    *
    * getter of pagesize
    *
    **/
    public int getPageSize() {
        return pageSize;
    }

    /**
    *
    * setter of pagesize
    * @param pageSize
    *
    **/
    public PageHref setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
    *
    *
    **/
    public String toString(){
        String url = "";
        url += hrefUrl;

        if(currentPage>0){
            if(url.indexOf("?")>-1){
                url += "&";
            }else{
                url += "?";
            }
            url += "currentPage="+currentPage ;
        }

        if(pageSize >0){
            if(url.indexOf("?")>-1){
                url += "&";
            }else{
                url += "?";
            }
            url += "pageSize="+pageSize ;
        }
        return url;
    }

}
