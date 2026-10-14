package com.api.alquilaya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.api.alquilaya.repositories.IUsuariosRepository;

@Configuration 
public class SecurityConfig {

    // Bean para el codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para el servicio de detalles de usuario
    @Bean
    public UserDetailsService userDetailsService(
            IUsuariosRepository usuariosRepository) {

        return email -> usuariosRepository.findByEmail(email)
                .map(usuario -> User.withUsername(usuario.getEmail())
                        .password(usuario.getPassword())
                        .roles(usuario.getRol())
                        .disabled(!usuario.isActivo())
                        .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado"));
    }

    // Bean para la configuración de la cadena de filtros de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // Se desactiva CSRF para poder hacer POST, PUT y DELETE desde Postman
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                // La vista (HTML, CSS y JS) se puede abrir sin iniciar sesión
                .requestMatchers("/", "/index.html", "/css/**", "/js/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").hasAnyRole("ADMIN", "OPERADOR")
                .anyRequest().authenticated()
            )
            // Basic Auth para probar la API desde Postman (usuario y contraseña)
            .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
