package com.online.yugaofan.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.online.yugaofan.interceptor.HeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        if(!registry.hasMappingForPattern("/static/**")){
//            registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        }
//    }

    /**
     * 不需要拦截地址
     */
    public static final String[] excludeUrls = {"/api/voice/toMp3", "/ip/district"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getHeaderInterceptor()).addPathPatterns("/**").excludePathPatterns(excludeUrls)
                .order(-10);
    }

    /**
     * 自定义请求头拦截器
     */
    public HeaderInterceptor getHeaderInterceptor() {
        return new HeaderInterceptor();
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOrigin("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * 设置编码默认格式为utf-8
     * 修改StringHttpMessageConverter默认配置 & Json 默认序列化方式 (改这个方法)
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 自定义配置...
//        FastJsonConfig config = new FastJsonConfig();
//        converter.setFastJsonConfig(config);
        // spring boot高版本无需配置，低版本不配置报错：Content-Type cannot contain wildcard type '*'
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(0, converter);
    }
}
