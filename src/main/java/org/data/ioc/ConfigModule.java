package org.data.ioc;

import com.google.inject.AbstractModule;
import org.data.services.DataInterface;
import org.data.services.MySqlDataService;
import org.data.services.hash.HashService;
import org.data.services.hash.SHA1HashService;

public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        // Конфигурация служб-поставщиков
        bind( HashService.class ).to( SHA1HashService.class ) ;
        bind( DataInterface.class ).to( MySqlDataService.class ) ;
        
    }
}
