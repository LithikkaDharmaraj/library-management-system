package com.library.dto;

public class IssueRequest {
    private Long bookId;
    private Long userId;
    private Integer allowedDays;

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getAllowedDays() { return allowedDays; }
    public void setAllowedDays(Integer allowedDays) { this.allowedDays = allowedDays; }
}
