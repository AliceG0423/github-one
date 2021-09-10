package com.accenture.bars.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Request {

    private int billingCycle;
    private LocalDate startDate;
    private LocalDate endDate;

    public Request() {}

    public Request(int billingCycle, LocalDate startDate, LocalDate endDate) {
        this.billingCycle = billingCycle;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(int billingCycle) {
        this.billingCycle = billingCycle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Request request = (Request) o;
        return billingCycle == request.billingCycle &&
                Objects.equals(startDate, request.startDate) &&
                Objects.equals(endDate, request.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billingCycle, startDate, endDate);
    }
}
