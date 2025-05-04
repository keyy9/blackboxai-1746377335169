package web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Allow requests from any origin
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        
        // Allow specific HTTP methods
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        
        // Allow specific headers
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        // Allow credentials
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        
        // Cache preflight response for 3600 seconds
        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
