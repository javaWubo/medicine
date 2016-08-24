package com.wuch.medicine.dao.common;

/**
 * Created with IntelliJ IDEA.
 * User: zhanglu7
 * Date: 14-11-25
 * Time: 上午9:48
 * To change this template use File | Settings | File Templates.
 */
public class Pagination {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NO = 1;
    public static final String DEFAULT_PAGE_SIZE_STRING = "10";
    public static final String DEFAULT_PAGE_NO_STRING = "1";
    private PageHref pageHref;
    private int pageSize;
    private int currentPage;
    private int totalPage;
    private int totalCount;
    private String sort; //排序字段
    private String order; //排序规则

    public Pagination() {
        this.currentPage = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     *
     * @param currentPage
     * @param pageSize
     */
    public Pagination(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
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
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
    *
    * getter of totalpage
    *
    **/
    public int getTotalPage() {
        return totalPage;
    }

    /**
    *
    * setter of totalpage
    * @param totalPage
    *
    **/
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
    *
    * getter of totalcount
    *
    **/
    public int getTotalCount() {
        return totalCount;
    }

    /**
    *
    * setter of totalcount
    * @param totalCount
    *
    **/
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
    *
    * getter of sort
    *
    **/
    public String getSort() {
        return sort;
    }

    /**
    *
    * setter of sort
    * @param sort
    *
    **/
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
    *
    * getter of order
    *
    **/
    public String getOrder() {
        return order;
    }

    /**
    *
    * setter of order
    * @param order
    *
    **/
    public void setOrder(String order) {
        this.order = order;
    }

    /**
    *
    * getter of pagehref
    *
    **/
    public PageHref getPageHref() {
        return pageHref;
    }

    /**
    *
    * setter of pagehref
    * @param pageHref
    *
    **/
    public void setPageHref(PageHref pageHref) {
        this.pageHref = pageHref;
    }
}
