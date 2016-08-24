package com.wuch.medicine.dao.common;

import java.util.HashMap;

public class DBQueryRequest extends HashMap {

    private Pagination page;

    public Pagination getPage() {
        return page;
    }

    /**
     * setter of page
     *
     * @param page 每页的数量
     **/
    public void setPage(Pagination page) {
        this.page = page;
        this.put("page", page);
    }

}