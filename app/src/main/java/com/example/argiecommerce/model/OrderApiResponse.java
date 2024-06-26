package com.example.argiecommerce.model;

import java.util.ArrayList;

public class OrderApiResponse {
    private ArrayList<OrderResponse> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPage;
    private boolean last;

    public OrderApiResponse() {
    }

    public ArrayList<OrderResponse> getContent() {
        return content;
    }

    public void setContent(ArrayList<OrderResponse> content) {
        this.content = content;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
