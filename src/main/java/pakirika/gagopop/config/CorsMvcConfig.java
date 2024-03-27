package pakirika.gagopop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**") //cors 셋팅
                .exposedHeaders("Set-Cookie");
                //.allowedOrigins("http://localhost:3000"); //웹앱이 동작할 곳의 주소
    }
}