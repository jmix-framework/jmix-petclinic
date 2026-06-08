package io.jmix.petclinic.security;

import io.jmix.core.JmixSecurityFilterChainOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class PetclinicSecurityConfiguration {

    @Bean
    @Order(JmixSecurityFilterChainOrder.CUSTOM)
    SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/images/**", "/healthcheck")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/healthcheck").permitAll()
                        .requestMatchers("/images/**").authenticated()
                );

        return http.build();
    }
}
