package pakirika.gagopop.config;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import pakirika.gagopop.jwt.JWTFilter;
import pakirika.gagopop.jwt.JWTUtil;
import pakirika.gagopop.service.CustomOAuth2UserService;
import pakirika.gagopop.oauth2.CustomSuccessHandler;

import java.util.Collections;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    private final JWTUtil jwtUtil;
/*    @Bean
    public WebSecurityCustomizer webSecurityCustomizer( ) {
        return (web) -> web.ignoring().requestMatchers("/popup/**");
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{

        //cors 설정
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.addAllowedOrigin("http://localhost:3000");
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); //프론트 서버 주소
                        configuration.setAllowedMethods(Collections.singletonList("*"));//get,put 다 허용
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));//header도 허용
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));//cookie와 Authorization은 서버 단에서
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        //csrf disable
        http
                .csrf((csrf)->csrf.disable());

        //form login 방식 disable
        http
                .formLogin((login)-> login.disable());
                //.formLogin((f)->f
                       // .loginPage( "/admin/login" )
                        //.defaultSuccessUrl("/admin/dashboard"));


        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((basic) -> basic.disable());



        //JWTFilter 추가
       http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);


        //OAuth2
        http
                .oauth2Login( oauth2-> oauth2
                        .userInfoEndpoint((userInfo) -> userInfo
                                .userService( customOAuth2UserService ))
                        .successHandler( customSuccessHandler )
                        .loginPage("/admin/login"))

                //.formLogin((f)->f.disable())
                .logout((logout)->logout
                        .logoutUrl( "/logout" )
                        //.logoutSuccessUrl( "http://localhost:3000" )
                        .logoutSuccessUrl( "https://gagopop.vercel.app/" )
                        .deleteCookies( "Authorization")
                        .permitAll()); //나중에 추가 셋팅할 것
        //.oauth2Login( Customizer.withDefaults() );

        http
                .authorizeHttpRequests( (auth) -> auth
                        .requestMatchers( "/admin/login","/login", "/" ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll() );
        //경로별 인가 작업

      /*  http
        .exceptionHandling( (ex) -> ex
                .authenticationEntryPoint( new HttpStatusEntryPoint( HttpStatus.NOT_FOUND ) )
                .authenticationEntryPoint( (request, response, authException) //인증되지 않은경우 401 반환
                        -> response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"  ) ));
        //잘못된 경로, 파라미터로 요청시 404 오류 반환하도록*/


        //세션 설정

        http
                .sessionManagement( (session) -> session
                        .sessionCreationPolicy( SessionCreationPolicy.STATELESS ) );

        return  http.build();
    }
}
