package com.ohgiraffers.semiproject.board.model.dto;

public class PageDTO {
    private int currentPage; // 기존의 page -> currentPage로 변경
    private int pageSize;    // 기존의 size -> pageSize로 변경
    private int totalPages;

    public PageDTO() {}

    public PageDTO(int currentPage, int pageSize, int totalPages) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", totalPages=" + totalPages +
                '}';
    }
}
