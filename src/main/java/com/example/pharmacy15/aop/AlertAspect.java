package com.example.pharmacy15.aop;

import com.example.pharmacy15.service.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
@Slf4j
public class AlertAspect {

    private final DrugService drugService;
    private final AlertMessageStore alertStore;

    @AfterReturning("execution(* com.example.pharmacy15.service.OrderServiceImpl.*(..))")
    public void checkDrugWarnings() {
        var expiring = drugService.getExpiringDrugs();
        var lowStock = drugService.getLowStockDrugs();

        if (!expiring.isEmpty()) {
            String msg = "⏰ 유통기한 임박 약품 " + expiring.size() + "건 존재함";
            log.warn(msg);
            alertStore.add(msg);
        }

        if (!lowStock.isEmpty()) {
            String msg = "📦 재고 부족 약품 " + lowStock.size() + "건 존재함";
            log.warn(msg);
            alertStore.add(msg);
        }
    }
}