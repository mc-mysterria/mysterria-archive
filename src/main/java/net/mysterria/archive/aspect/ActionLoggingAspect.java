package net.mysterria.archive.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mysterria.archive.annotation.LogAction;
import net.mysterria.archive.database.service.ActionService;
import net.mysterria.archive.database.service.ResearcherService;
import net.mysterria.archive.security.UserPrincipal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ActionLoggingAspect {

    private final ActionService actionService;
    private final ResearcherService researcherService;

    @Around("@annotation(logAction)")
    public Object logAction(ProceedingJoinPoint joinPoint, LogAction logAction) throws Throwable {
        Object result = joinPoint.proceed();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                UUID backendUserId = userPrincipal.getId();

                var researcher = researcherService.findOrCreateByBackendUserId(
                    backendUserId,
                    "User_" + backendUserId.toString().substring(0, 8)
                );

                actionService.recordAction(researcher.getId(), logAction.value());

                log.debug("Logged action {} for user {}", logAction.value(), researcher.getNickname());
            } else {
                log.warn("Could not log action {} - no authenticated user found", logAction.value());
            }
        } catch (Exception e) {
            log.error("Failed to log action {}: {}", logAction.value(), e.getMessage(), e);
        }

        return result;
    }
}