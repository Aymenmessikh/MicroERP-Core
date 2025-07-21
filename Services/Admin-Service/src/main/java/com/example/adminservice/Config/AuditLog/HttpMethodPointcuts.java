package com.example.adminservice.Config.AuditLog;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class HttpMethodPointcuts {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMethods() {
    }
}
