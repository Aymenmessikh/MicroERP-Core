package com.example.adminservice.Config.filter;

import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.CritiriaParamsArgumentResolver;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SearchValueParamsArgumentResolver;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SortParamsArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CommonsWebServiceConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SortParamsArgumentResolver());
        resolvers.add(new CritiriaParamsArgumentResolver());
        resolvers.add(new SearchValueParamsArgumentResolver());
    }
}
