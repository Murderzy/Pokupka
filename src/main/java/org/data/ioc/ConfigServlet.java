package org.data.ioc;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import org.data.filters.DataFilter;
import org.data.servlets.*;


public class ConfigServlet extends ServletModule {
    @Override
    protected void configureServlets() {
        // Программная замена web.xml - конфигурация фильтров ...
        //filter( "/*" ).through( CharsetFilter.class ) ;
        filter( "/*" ).through( DataFilter.class ) ;
        //filter( "/*" ).through( AuthFilter.class ) ;
        //filter( "/*" ).through( DemoFilter.class ) ;

        // ...  и сервлетов
        serve( "/goods/" ).with(GoodsServlet.class ) ;
        serve( "/delete" ).with( DeleteServlet.class ) ;
        serve( "/register/" ).with( RegServlet.class ) ;
        serve( "/edit" ).with( EditServlet.class ) ;
        serve( "/editone" ).with( EditOneServlet.class ) ;
        serve( "/settings" ).with( SettingsServlet.class ) ;
        serve( "/" ).with( HomeServlet.class ) ;
        serve( "/log" ).with( LoginServlet.class ) ;
        serve("/logout").with(LogoutServlet.class);
        serve( "/image/*" ).with( DownloadServlet.class ) ;
    }
}
