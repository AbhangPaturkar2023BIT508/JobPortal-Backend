// package com.jobportal;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.jobportal.jwt.JwtAuthenticationEntryPoint;
// import com.jobportal.jwt.JwtAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

//     @Autowired
//     private JwtAuthenticationEntryPoint point;

//     @Autowired
//     private JwtAuthenticationFilter filter;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(csrf -> csrf.disable())
//                 .cors() // ✅ Enable CORS here
//                 .and()
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/auth/login", "/users/register", "/users/verifyOtp/**", "/users/sendOtp/**")
//                         .permitAll()
//                         .anyRequest().authenticated())
//                 .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//         http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//         return http.build();
//     }

// }

package com.jobportal;

import com.jobportal.jwt.JwtAuthenticationEntryPoint;
import com.jobportal.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Use custom CORS config
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/users/register", "/users/verifyOtp/**", "/users/sendOtp/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000", // ✅ Add your frontend URL(s) here
                "https://jobportal-frontend-dhw0.onrender.com"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Include OPTIONS for preflight
        config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // ✅ Required for auth and JSON
        config.setAllowCredentials(true); // ✅ Needed if you send cookies or auth tokens

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
