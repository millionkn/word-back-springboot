package com.example.demo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import org.mybatis.spring.annotation.MapperScan;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

@SpringBootApplication
@Configuration
@MapperScan("com.example.demo.mapper")
@ControllerAdvice
public class DemoApplication implements WebMvcConfigurer {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  public ExpiringMap<String, byte[]> expiringMap() {
    return ExpiringMap.builder().expiration(10, TimeUnit.MINUTES).expirationPolicy(ExpirationPolicy.CREATED).build();
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
        .allowCredentials(false).maxAge(3600);
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoHandlerFoundException.class)
  public String notFound() {
    return "/";
  }

  @ExceptionHandler(UnauthenticatedException.class)
  @ResponseBody
  @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
  public JSONObject notLogin() {
    JSONObject json = new JSONObject();
    json.put("err", "尚未登录");
    return json;
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public JSONObject error() {
    JSONObject json = new JSONObject();
    json.put("err", "服务器错误");
    return json;
  }

  @ExceptionHandler(JSONErrorMessage.class)
  @ResponseBody
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public JSONObject error(JSONErrorMessage th) {
    JSONObject json = new JSONObject();
    json.put("err", th.getMessage());
    return json;
  }

  @ExceptionHandler(UnauthorizedException.class)
  @ResponseBody
  @ResponseStatus(code = HttpStatus.FORBIDDEN)
  public JSONObject permissionDenied() {
    JSONObject json = new JSONObject();
    json.put("err", "权限不足");
    return json;
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    // 序列化配置
    FastJsonConfig config = new FastJsonConfig();
    config.setSerializerFeatures(SerializerFeature.QuoteFieldNames, // 输出key时是否使用双引号
        SerializerFeature.WriteMapNullValue, // 是否输出值为null的字段
        SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null,输出为0,而非null
        SerializerFeature.WriteNullListAsEmpty, // List字段如果为null,输出为[],而非null
        SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null,输出为"",而非null
        SerializerFeature.WriteNullBooleanAsFalse// Boolean字段如果为null,输出为false,而非null
    );
    converter.setFastJsonConfig(config);
    converters.add(converter);
  }
}
