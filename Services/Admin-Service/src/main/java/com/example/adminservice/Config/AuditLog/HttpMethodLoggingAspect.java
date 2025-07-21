package com.example.adminservice.Config.AuditLog;

import com.example.adminservice.Config.Kafka.AuditLogProducer;
import com.example.adminservice.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
@Aspect
public class HttpMethodLoggingAspect {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(HttpMethodLoggingAspect.class);
    @Value("${module_name}")
    private String MODULE_NAME;
    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;
    private final UserService userService;
    @Autowired
    private JwtDecoder jwtDecoder;
    private final AuditLogProducer auditLogProducer;

    // Log après l'exécution réussie des méthodes POST
    @AfterReturning(pointcut = "com.example.adminservice.Config.AuditLog.HttpMethodPointcuts.postMethods())", returning = "result")
    public void logAfterPost(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        Object responseBody = extractResponseBody(result);
        Long entityId = extractEntityId(responseBody);
        AuditEvent auditEvent = AuditEvent.builder()
                .moduleName(MODULE_NAME)
                .url(getRequest().getRequestURI())
                .httpMethod(getRequest().getMethod())
                .entityName(getEntityName(joinPoint))
                .objectId(entityId)
                .methodName(joinPoint.getSignature().getName())
                .ipAddress(getRequest().getRemoteAddr())
                .timestamp(LocalDateTime.now().toString())
                .username(extractUserNameFromToken())
                .status("SUCCESS")
                .errorMessage(null)
                .oldEntity(null)
                .newEntity(responseBody.toString())
                .build();
        auditLogProducer.sendAuditEvent(auditEvent);

    }

    @AfterThrowing(pointcut = "com.example.adminservice.Config.AuditLog.HttpMethodPointcuts.postMethods()", throwing = "ex")
    public void logAfterPostError(JoinPoint joinPoint, Exception ex) throws JsonProcessingException {
        AuditEvent auditEvent = AuditEvent.builder()
                .moduleName(MODULE_NAME)
                .url(getRequest().getRequestURI())
                .httpMethod(getRequest().getMethod())
                .entityName(getEntityName(joinPoint))
                .methodName(joinPoint.getSignature().getName())
                .ipAddress(getRequest().getRemoteAddr())
                .timestamp(LocalDateTime.now().toString())
                .objectId(null)
                .username(extractUserNameFromToken())
                .status("FAILED") // Indicating failure
                .errorMessage(ex.getMessage()) // Capture exception message
                .oldEntity(null)
                .newEntity(null) // No new entity since the operation failed
                .build();
        auditLogProducer.sendAuditEvent(auditEvent);

        System.out.println("[POST] Erreur: " + ex.getMessage());
        logger.error("Audit Event (Error): {}", auditEvent);
    }

    @AfterReturning(pointcut = "com.example.adminservice.Config.AuditLog.HttpMethodPointcuts.getMethods())", returning = "result")
    public void logAfterGet(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        AuditEvent auditEvent = AuditEvent.builder()
                .moduleName(MODULE_NAME)
                .url(getRequest().getRequestURI())
                .httpMethod(getRequest().getMethod())
                .entityName(getEntityName(joinPoint))
                .methodName(joinPoint.getSignature().getName())
                .ipAddress(getRequest().getRemoteAddr())
                .objectId(null)
                .timestamp(LocalDateTime.now().toString())
                .username(extractUserNameFromToken())
                .status("SUCCESS")
                .errorMessage(null)
                .oldEntity(null)
                .newEntity(null)
                .build();
        auditLogProducer.sendAuditEvent(auditEvent);

        logger.info("Audit Event (Success): {}", auditEvent);
    }


    @AfterThrowing(pointcut = "com.example.adminservice.Config.AuditLog.HttpMethodPointcuts.getMethods()", throwing = "ex")
    public void logAfterGetError(JoinPoint joinPoint, Exception ex) throws JsonProcessingException {
        AuditEvent auditEvent = AuditEvent.builder()
                .moduleName(MODULE_NAME)
                .url(getRequest().getRequestURI())
                .httpMethod(getRequest().getMethod())
                .entityName(getEntityName(joinPoint))
                .methodName(joinPoint.getSignature().getName())
                .ipAddress(getRequest().getRemoteAddr())
                .timestamp(LocalDateTime.now().toString())
                .objectId(null)
                .username(extractUserNameFromToken())
                .status("FAILED") // Indicating failure
                .errorMessage(ex.getMessage()) // Capture exception message
                .oldEntity(null)
                .newEntity(null) // No new entity since the operation failed
                .build();
        auditLogProducer.sendAuditEvent(auditEvent);

        System.out.println("[GET] Erreur: " + ex.getMessage());
        logger.error("Audit Event (Error): {}", auditEvent);
    }


    @AfterReturning(
            pointcut = "com.example.adminservice.Config.AuditLog.HttpMethodPointcuts.deleteMethods() && args(id)",
            returning = "result"
    )
    public void logAfterDelete(JoinPoint joinPoint, Long id, Object result) throws JsonProcessingException {
        String entityName = getEntityName(joinPoint);

        AuditEvent auditEvent = AuditEvent.builder()
                .moduleName(MODULE_NAME)
                .url(getRequest().getRequestURI())
                .httpMethod(getRequest().getMethod())
                .entityName(entityName)
                .methodName(joinPoint.getSignature().getName())
                .ipAddress(getRequest().getRemoteAddr())
                .timestamp(LocalDateTime.now().toString())
                .objectId(id)
                .username(extractUserNameFromToken())
                .status("SUCCESS")
                .errorMessage(null)
                .oldEntity(null) // L'entité supprimée
                .newEntity(null) // Pas de nouvelle entité
                .build();

        auditLogProducer.sendAuditEvent(auditEvent);
        logger.info("Audit Event (DELETE Success): {}", auditEvent);
        logger.info("Id de objet (DELETE Success): {}", id);
    }

    @AfterThrowing(pointcut = "com.example.adminservice.Config.AuditLog.HttpMethodPointcuts.deleteMethods() && args(id)", throwing = "ex")
    public void logAfterDeleteError(JoinPoint joinPoint, Long id, Exception ex) throws JsonProcessingException {
        String entityName = getEntityName(joinPoint);

        AuditEvent auditEvent = AuditEvent.builder()
                .moduleName(MODULE_NAME)
                .url(getRequest().getRequestURI())
                .httpMethod(getRequest().getMethod())
                .entityName(entityName)
                .objectId(id)
                .methodName(joinPoint.getSignature().getName())
                .ipAddress(getRequest().getRemoteAddr())
                .timestamp(LocalDateTime.now().toString())
                .username(extractUserNameFromToken())
                .status("FAILED")
                .errorMessage(ex.getMessage())
                .oldEntity(null)
                .newEntity(null)
                .build();

        auditLogProducer.sendAuditEvent(auditEvent);
        logger.error("Audit Event (DELETE Error): {}", auditEvent);
    }

    @AfterReturning(pointcut = "com.example.adminservice.Config.AuditLog.HttpMethodPointcuts.putMethods()", returning = "result")
    public void logAfterPut(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        String entityName = getEntityName(joinPoint);
        Object responseBody = extractResponseBody(result);
        Long entityId = extractEntityId(responseBody);
        AuditEvent auditEvent = AuditEvent.builder()
                .moduleName(MODULE_NAME)
                .objectId(entityId)
                .url(getRequest().getRequestURI())
                .httpMethod(getRequest().getMethod())
                .entityName(entityName)
                .methodName(joinPoint.getSignature().getName())
                .ipAddress(getRequest().getRemoteAddr())
                .timestamp(LocalDateTime.now().toString())
                .username(extractUserNameFromToken())
                .status("SUCCESS")
                .errorMessage(null)
                .oldEntity(null)
                .newEntity(responseBody.toString())
                .build();

        auditLogProducer.sendAuditEvent(auditEvent);
        logger.info("Audit Event (PUT Success): {}", auditEvent);
    }


    @AfterThrowing(pointcut = "com.example.adminservice.Config.AuditLog.HttpMethodPointcuts.putMethods() && args(id)", throwing = "ex")
    public void logAfterPutError(JoinPoint joinPoint, Long id, Exception ex) throws JsonProcessingException {
        String entityName = getEntityName(joinPoint);
        AuditEvent auditEvent = AuditEvent.builder()
                .moduleName(MODULE_NAME)
                .url(getRequest().getRequestURI())
                .httpMethod(getRequest().getMethod())
                .entityName(entityName)
                .methodName(joinPoint.getSignature().getName())
                .ipAddress(getRequest().getRemoteAddr())
                .objectId(id)
                .timestamp(LocalDateTime.now().toString())
                .username(extractUserNameFromToken())
                .status("FAILED")
                .errorMessage(ex.getMessage())
                .oldEntity(null)
                .newEntity(null)
                .build();

        auditLogProducer.sendAuditEvent(auditEvent);
        logger.error("Audit Event (PUT Error): {}", auditEvent);
    }

    private Long extractEntityId(Object respnseBody) {
        if (respnseBody == null) return null;

        try {
            // Recherche d'un getter "getId" ou autre identifiant possible
            Method getIdMethod = respnseBody.getClass().getMethod("getId");
            Object idValue = getIdMethod.invoke(respnseBody);
            return idValue != null ? (Long) idValue : null;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null; // Retourne null si l'entité n'a pas de méthode getId()
        }
    }

    private Object extractResponseBody(Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            return responseEntity.getBody(); // Retourne le body si c'est un ResponseEntity
        }
        return result; // Retourne l'objet tel quel s'il n'est pas encapsulé dans ResponseEntity
    }

    public Jwt getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);  // Retirer le préfixe "Bearer "

            // Décoder le JWT et le retourner en tant qu'objet Jwt
            return jwtDecoder.decode(token);
        }

        return null;
    }

    public String getEntityName(JoinPoint joinPoint) {
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String entityName = controllerName.length() > 10
                ? controllerName.substring(0, controllerName.length() - 10)
                : controllerName;
        return entityName;
    }

    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return request;

    }

    private String getPrincipleClaimName() {
        Jwt jwt = getTokenFromRequest();
        String claimName = JwtClaimNames.SUB;
        if (principleAttribute != null) {
            claimName = principleAttribute;
        }
        return jwt.getClaim(claimName);
    }

    public String extractUserNameFromToken() {
        String userName = getPrincipleClaimName();
        return userName;
    }
}
