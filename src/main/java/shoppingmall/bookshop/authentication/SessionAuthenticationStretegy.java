package shoppingmall.bookshop.authentication;

import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

public class SessionAuthenticationStretegy extends CompositeSessionAuthenticationStrategy {

    private final List<SessionAuthenticationStretegy> delecateStrategies;

    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        int currentPosition = 0;
        int size = this.delecateStrategies.size();

        SessionAuthenticationStretegy delegate;

        for (Iterator var6 = this.delegateStrategies.iterator(); var6.hasNext(); delegate.onAuthentication(authentication, request, response)) {
            delegate = (SessionAuthenticationStretegy) var6.next();
            if (this.logger.isTraceEnabled()) {
                Log var10000 = this.logger;
                String var10002 = delegate.getClass().getSimpleName();
                ++currentPosition;
                var10000.trace(LogMessage.format("Preparing session with %s (%d/%d)", var10002, currentPosition, size));
            }
        }


    }
}

