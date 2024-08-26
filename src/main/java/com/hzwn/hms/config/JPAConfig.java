package com.hzwn.hms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.hzwn.hms")
@EnableTransactionManagement
public class JPAConfig {

    @Bean
    public DataSource dataSource() {
    	 HikariConfig hikariConfig = new HikariConfig();
         hikariConfig.setJdbcUrl(System.getProperty("database.url"));
         hikariConfig.setUsername(System.getProperty("database.username"));
         hikariConfig.setPassword(System.getProperty("database.password"));
         hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver"); // Ensure MySQL JDBC Driver is on the classpath

         // Additional HikariCP configurations can be set here
         hikariConfig.setMaximumPoolSize(10); // Example: Set maximum pool size

         return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.hzwn.hms");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");

        em.setJpaProperties(properties);
        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
