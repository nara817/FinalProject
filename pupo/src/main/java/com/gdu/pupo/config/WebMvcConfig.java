package com.gdu.pupo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gdu.pupo.intercept.AdminCheckInterceptor;
import com.gdu.pupo.intercept.LoginCheckInterceptor;
import com.gdu.pupo.util.MyFileUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  // field
  private final LoginCheckInterceptor loginCheckInterceptor;
  private final MyFileUtil myFileUtil;
  private final AdminCheckInterceptor adminCheckInterceptor;
  
  
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    
    registry.addInterceptor(loginCheckInterceptor)
      .addPathPatterns("/regular/regularPayPage.do", "/regular/regularMyOrder.html" ,"/regular/regularPayDetail.html", "/regular/regWriteReview.html")
      .addPathPatterns("/user/logout.do");
    registry.addInterceptor(adminCheckInterceptor)
    .addPathPatterns("/regular/regularAddPage.html", "/regular/adminRegOrder.do");
    
    
    //registry.addInterceptor(loginCheckInterceptor)
    //  .addPathPatterns("/**")                  // 모든 요청
    //  .excludePathPatterns("/user/leave.do");  // 제외할 요청
      
  }
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/imageLoad/**")
      .addResourceLocations("file:" + myFileUtil.getSummernoteImagePath() + "/");
  }
  
}