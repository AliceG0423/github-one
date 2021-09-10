package com.accenture.bars.repository;


import com.accenture.bars.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BillingRepository extends JpaRepository <Billing, Integer> {

    Billing findByBillingCycleAndStartDateAndEndDate
            (@Param("billingCycle") int billingCycle,
             @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate);

}
