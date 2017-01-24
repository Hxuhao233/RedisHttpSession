package com.hxuhao.session;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class RedisHttpSessionFilter
 */
@WebFilter("/RedisHttpSessionFilter")
public class RedisHttpSessionFilter implements Filter {

	
	 private static final String TOKEN_HEADER_NAME = "x-auth-token";

    private RedisHttpSessionRepository repository;

    public RedisHttpSessionFilter(){
        repository = RedisHttpSessionRepository.getInstance();
    }


	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		RedisSessionRequestWrapper  requestWrapper = new RedisSessionRequestWrapper((HttpServletRequest)request);
		RedisSessionResponseWrapper responseWrapper = new RedisSessionResponseWrapper((HttpServletResponse)response, requestWrapper);
		// pass the request along the filter chain
		chain.doFilter(requestWrapper, responseWrapper);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	
	private final class RedisSessionRequestWrapper extends HttpServletRequestWrapper{

        private HttpServletRequest request;

        private String token;
        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request
         * @throws IllegalArgumentException if the request is null
         */
        public RedisSessionRequestWrapper(HttpServletRequest request) {
            super(request);
            this.request = request;
            this.token = request.getHeader(TOKEN_HEADER_NAME);
        }

        @Override
        public HttpSession getSession(boolean create) {
            if (token != null) {
                return repository.getSession(token, request.getServletContext());
            } else if (create){
                HttpSession session = repository.newSession(request.getServletContext());
                token = session.getId();
                return session;
            } else {
                return null;
            }
        }

        @Override
        public HttpSession getSession() {
            return getSession(true);
        }

        @Override
        public String getRequestedSessionId() {
            return token;
        }
    }

    private final class RedisSessionResponseWrapper extends HttpServletResponseWrapper {
        /**
         * Constructs a response adaptor wrapping the given response.
         *
         * @param response
         * @throws IllegalArgumentException if the response is null
         */
        public RedisSessionResponseWrapper(HttpServletResponse response, RedisSessionRequestWrapper request) {
            super(response);
            //if session associate with token is not existed, create one for the response
            response.setHeader(TOKEN_HEADER_NAME, request.getSession(true).getId());
        }
    }

}
