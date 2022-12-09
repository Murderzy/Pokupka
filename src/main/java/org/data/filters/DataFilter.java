package org.data.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.data.services.DataInterface;

import javax.servlet.*;
import java.io.IOException;

@Singleton
public class DataFilter  implements Filter {
    private final DataInterface dataInterface ;
    private FilterConfig filterConfig ;
    @Inject
    public DataFilter( DataInterface dataInterface ) {
        this.dataInterface = dataInterface ;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig ;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if( dataInterface.getConnection() == null ) {
            servletRequest.getRequestDispatcher( "WEB-INF/static.jsp" )
                    .forward( servletRequest, servletResponse ) ;
        }
        else {
            servletRequest.setAttribute( "DataService", dataInterface ) ;
            filterChain.doFilter( servletRequest, servletResponse ) ;
            System.out.println("Data");
        }
    }

    public void destroy() {
        this.filterConfig = null ;
    }
}
