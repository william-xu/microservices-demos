package org.xwl.task.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {
	
	@Bean
	@Primary
	public DataSourceProperties defaultDsProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@QuartzDataSource  //指示Quartz使用此数据源进行数据库操作
	public DataSource getDataSource() {
		return defaultDsProperties().initializeDataSourceBuilder().build();
	}
	
}
