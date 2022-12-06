package shoppingmall.bookshop.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SessionManagementFilterImpl extends SessionManagementFilter {

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public SessionManagementFilterImpl(SecurityContextRepository securityContextRepository) {
        super(securityContextRepository);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (httpServletRequest.getServletPath().equals("/api/login")) {
            log.info("로그인 전이므로 SessionManagementFilterImpl을 거치지 않습니다.");
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        log.info("SecurityContext에 해당 유저의 인증 정보가 존재하는지 확인합니다.");
        if (securityContextRepository.containsContext(httpServletRequest)) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            log.info("유저의 정보가 존재한다면 이 세션을 저장합니다.");
            saveSession(securityContext, httpServletRequest, httpServletResponse);
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void saveSession(SecurityContext securityContext, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            securityContextRepository.saveContext(securityContext, request, response);
        }
    }
}
