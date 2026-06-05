package com.EducCore.EduCore.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Autenticação
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // Banners
                        .requestMatchers("/api/banners", "/api/banners/**").permitAll()

                        // Cursos e Páginas
                        .requestMatchers("/api/courses", "/api/courses/**").permitAll()
                        .requestMatchers("/api/pages", "/api/pages/**").permitAll()

                        .requestMatchers("/api/products", "/api/products/**").permitAll()

                        // Grupos e Usuários
                        .requestMatchers("/api/groups", "/api/groups/**").permitAll()
                        .requestMatchers("/api/users", "/api/users/**").permitAll()

                        // Outros Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/icons").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/banner-register").permitAll()
                        .requestMatchers("/professor", "/professor/**").permitAll()
                        .requestMatchers("/api/about", "/api/about/**").permitAll()
                        .requestMatchers("/empresa", "/empresa/**").permitAll()
                        .requestMatchers("/api/faqs", "/api/faqs/**").permitAll()

                        // Conteúdo (Módulos e Aulas)
                        .requestMatchers("/api/modules", "/api/modules/**").permitAll()
                        .requestMatchers("/api/classes", "/api/classes/**").permitAll()
                        .requestMatchers("/api/module-class", "/api/module-class/**").permitAll()
                        .requestMatchers("/api/module-course", "/api/module-course/**").permitAll()

                        // Documentação e Console
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // Qualquer outra rota exige autenticação
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}