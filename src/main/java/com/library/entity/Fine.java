package com.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fine")
public class Fine {

    @Id
    @Column(name = "issued_id")
    private Long issuedId;

    @Column(name = "allowed_days")
    private Integer allowedDays;

    @Column(name = "fine_per_day")
    private Double finePerDay;

    @Column(name = "fine_amount")
    private Double fineAmount;

    public Fine() {}

    public Fine(Long issuedId, Integer allowedDays, Double finePerDay, Double fineAmount) {
        this.issuedId = issuedId;
        this.allowedDays = allowedDays;
        this.finePerDay = finePerDay;
        this.fineAmount = fineAmount;
    }

    public Long getIssuedId() { return issuedId; }
    public void setIssuedId(Long issuedId) { this.issuedId = issuedId; }

    public Integer getAllowedDays() { return allowedDays; }
    public void setAllowedDays(Integer allowedDays) { this.allowedDays = allowedDays; }

    public Double getFinePerDay() { return finePerDay; }
    public void setFinePerDay(Double finePerDay) { this.finePerDay = finePerDay; }

    public Double getFineAmount() { return fineAmount; }
    public void setFineAmount(Double fineAmount) { this.fineAmount = fineAmount; }
}
