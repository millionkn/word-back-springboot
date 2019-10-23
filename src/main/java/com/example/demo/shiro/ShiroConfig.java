package com.example.demo.shiro;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
  @Bean
  public Realm realm() {
    return new MyRealm();
  }

  @Bean
  public ShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
    chain.addPathDefinition("/**", "anon");
    return chain;
  }

  @Bean
  public MapperScannerConfigurer mapperScan() {
    MapperScannerConfigurer config = new MapperScannerConfigurer();
    config.setBasePackage("com.example.demo.shiro");
    return config;
  }
}