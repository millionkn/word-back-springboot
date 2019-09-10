package com.example.demo;

import java.util.List;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Configuration
@MapperScan("com.example.demo.mapper")
@ControllerAdvice
public class DemoApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@ExceptionHandler(NoHandlerFoundException.class)
	public String notFound(){
		return "/";
	}
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        //序列化配置
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
			SerializerFeature.QuoteFieldNames,// 输出key时是否使用双引号
            SerializerFeature.WriteMapNullValue,// 是否输出值为null的字段
            SerializerFeature.WriteNullNumberAsZero,//数值字段如果为null,输出为0,而非null
            SerializerFeature.WriteNullListAsEmpty,//List字段如果为null,输出为[],而非null
            SerializerFeature.WriteNullStringAsEmpty,//字符类型字段如果为null,输出为"",而非null
            SerializerFeature.WriteNullBooleanAsFalse//Boolean字段如果为null,输出为false,而非null
        );
        converter.setFastJsonConfig(config);
        converters.add(converter);
    }
}
