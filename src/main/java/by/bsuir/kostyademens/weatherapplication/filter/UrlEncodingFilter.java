package by.bsuir.kostyademens.weatherapplication.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = "/search")
public class UrlEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String locationName =
                URLEncoder.encode(servletRequest.getParameter("locationName"), StandardCharsets.UTF_8);
        servletRequest.setAttribute("locationName", locationName);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
