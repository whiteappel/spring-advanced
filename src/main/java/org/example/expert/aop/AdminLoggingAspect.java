package org.example.expert.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect //aop정의관련
@Component
public class AdminLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(AdminLoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 이것이 실행될때 로그를 남기겟다는 의미
    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object logAdminActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getCurrentHttpRequest();
        //Http 요청 가져옴
        String userId = request.getHeader("User-Id");
        String requestUrl = request.getRequestURI();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String requestBody = getRequestBodyFromArgs(joinPoint);
        //id,url,시간,요청내용 json형변환
        //요청 로그
        logger.info("[어드민 API 요청] 사용자 ID: {}, 시간: {}, URL: {}, 요청 본문: {}",
                userId, timestamp, requestUrl, requestBody);
        // 실행
        Object response = joinPoint.proceed();
        // 응답 json화
        String responseBody = objectMapper.writeValueAsString(response);
        //응답 로그
        logger.info("[어드민 API 응답] 사용자 ID: {}, URL: {}, 응답 본문: {}",
                userId, requestUrl, responseBody);

        return response;
    }

    // 현재 요청 객체가져오는 메소드
    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest();
    }

    //요청 데이터 찾아 json화
    private String getRequestBodyFromArgs(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            //HttpServletRequest,HttpServletResponse 아닌 데이터값 일때
            if (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)) {
                try {
                    return objectMapper.writeValueAsString(arg);
                } catch (JsonProcessingException e) {
                    return "요청 본문 직렬화 실패";
                }
            }
        }
        return "본문 없음";
    }

}
