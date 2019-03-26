package springboot.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;
import springboot.util.MyUtils;

import javax.annotation.Resource;

/**
 * @author tangj
 * @date 2018/1/22 20:50
 */
@Component
public class WebMvcConfig extends WebMvcConfigurationSupport{
    @Resource
    private BaseInterceptor baseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(baseInterceptor);
        addInterceptor.excludePathPatterns("/static/**");//不拦截静态资源
        addInterceptor.excludePathPatterns("/login");
        addInterceptor.excludePathPatterns("/loginCheck");
        addInterceptor.addPathPatterns("/**");
//        registry.addInterceptor(baseInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ MyUtils.getUploadFilePath()+"upload/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    /**
     * 添加默认主页，访问域名或者端口跳转
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:login.html");
    }

}
