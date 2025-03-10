//package com.mobile.group.tlu_contact_be.configs;
//
//import com.mobile.group.tlu_contact_be.filter.FirebaseAuthFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public FilterRegistrationBean<FirebaseAuthFilter> firebaseAuthFilter() {
//        FilterRegistrationBean<FirebaseAuthFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new FirebaseAuthFilter());
//        registrationBean.addUrlPatterns("/api/*"); // Áp dụng cho các endpoint API
//        return registrationBean;
//    }
//}
