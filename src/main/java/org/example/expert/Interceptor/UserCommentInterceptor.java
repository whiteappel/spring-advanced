package org.example.expert.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Component//bean등록을 위한것
public class UserCommentInterceptor implements HandlerInterceptor {

    //요청전에 확인할때 실행
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String userRole = request.getHeader("X-USER-ROLE");
        String uri = request.getRequestURI();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"));
        //어드민여부 ,url ,요청시간

        //인증성공시
        if(Objects.equals(userRole, "ADMIN")){
            log.info("확인되었습니다 요청 시간 {} , uri: {}", timestamp, uri);
            return true;
        }

        //인증실패시
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("실패햇습니다.");
        response.getWriter().flush();
        return false;
    }
    //실패를 기본값으로 잡음
}
