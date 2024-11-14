package com.boruomi.common.event;
import com.boruomi.business.model.entity.SysUserEntity;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CustomEventListener {
    @Async("taskExecutor")
    @Order
    @EventListener
    public void handleCustomEvent(SysUserEntity event) {
        System.out.println(Thread.currentThread().getName());
        System.out.println("Received event with message: " + event.getUserName());
    }
}
