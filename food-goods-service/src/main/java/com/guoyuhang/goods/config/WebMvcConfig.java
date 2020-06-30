//package com.guoyuhang.goods.config;
//
//
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * 描述:
// *
// * @author 国宇航
// * @create 2020-04-06 22:06
// */
//public class WebMvcConfig implements WebMvcConfigurer {
////    @Override
////    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        registry.addResourceHandler("/**").
////                addResourceLocations("classpath:/META-INF/resources").addResourceLocations();
////    }
//
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder){
//        return builder.build();
//    }
//
////    @Bean
////    public UserTokenInterceptor userTokenInterceptor(){
////        return new UserTokenInterceptor();
////    }
//
////    //注册拦截器
////    @Override
////    public void addInterceptors(InterceptorRegistry registry) {
////             registry.addInterceptor(userTokenInterceptor())
////                     .addPathPatterns("/hello");
////             WebMvcConfigurer.super.addInterceptors(registry);
////    }
//}
