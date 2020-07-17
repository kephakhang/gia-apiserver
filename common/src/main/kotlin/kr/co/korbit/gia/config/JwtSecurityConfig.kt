package kr.co.korbit.gia.config

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.service.admin.AdminUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class JwtSecurityConfig() : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var adminUserService: AdminUserService

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/public").permitAll()
            .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**",
                "/webjars/**", "/swagger-ui.html", "/v2/api-docs",
                "/swagger-resources",  "/swagger-resources/configuration/ui", "/swagger-resources/**",
                "/swagger/**", "/swagger-*/**", "/swagger.json",
                "/**/*.html", "/**/*.css", "/**/*.js",
                "/**/*.jpg", "/**/*.jpeg", "/**/*.gif", "/**/*.png",
                "/**/*.ico", "/**/*.svg", "/error").permitAll()
            .antMatchers("/internal/**").permitAll()
            .antMatchers("/admin/**").hasRole(Role.ADMIN.name)
            .antMatchers("/member/**").hasRole(Role.MEMBER.name)
            .antMatchers("/public/**").hasRole(Role.PUBLIC.name)
            .anyRequest().authenticated()
            .and()
            .addFilter(JwtAuthenticationFilter(authenticationManager()))
            .addFilter(JwtAuthorizationFilter(authenticationManager()))
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        web.ignoring().antMatchers(
            "/css/**", "/js/**", "/img/**", "/lib/**",
            "/webjars/**", "/swagger-ui.html", "/v2/api-docs",
            "/swagger-resources", "/swagger-resources/configuration/ui", "/swagger-resources/**",
            "/swagger/**", "/swagger-*/**", "/swagger.json",
            "/**/*.html", "/**/*.css", "/**/*.js",
            "/**/*.html", "/**/*.css", "/**/*.js",
            "/**/*.jpg", "/**/*.jpeg", "/**/*.gif", "/**/*.png",
            "/**/*.ico", "/**/*.svg", "/error"
        )
    }

    @Throws(Exception::class)
    public override fun configure(auth: AuthenticationManagerBuilder) {

        auth.userDetailsService(adminUserService).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }
}