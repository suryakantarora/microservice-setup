package com.cgs.authservice.security;

import com.cgs.authservice.util.RequestEncryptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

	@Autowired
	RequestEncryptor requestEncryptor;

	@Autowired
	private Environment env;

	@Bean(name = "dsDatasource")
	@Primary
	public DataSource dsDatasource() {
		HikariConfig config = new HikariConfig();
//		config.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		config.setJdbcUrl(requestEncryptor.propertyDecrypt(env.getProperty("spring.datasource.url")));
		config.setUsername(requestEncryptor.propertyDecrypt(env.getProperty("spring.datasource.username")));
		config.setPassword(requestEncryptor.propertyDecrypt(env.getProperty("spring.datasource.password")));

//		config.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.datasource.hikari.maximum-pool-size")));
//		config.setConnectionTimeout(Integer.parseInt(env.getProperty("spring.datasource.hikari.connection-timeout")));
//		config.setMinimumIdle(Integer.parseInt(env.getProperty("spring.datasource.hikari.minimum-idle")));
//		config.setIdleTimeout(Integer.parseInt(env.getProperty("spring.datasource.hikari.idle-timeout")));
//		config.setMaxLifetime(Integer.parseInt(env.getProperty("spring.datasource.hikari.max-lifetime")));

		HikariDataSource dataSource = new HikariDataSource(config);

		logger.debug("[] ==> New ds1verifyDatasource object created successfully");

		return dataSource;
	}
}
