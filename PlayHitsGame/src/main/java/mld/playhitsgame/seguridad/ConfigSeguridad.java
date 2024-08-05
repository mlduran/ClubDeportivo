/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.seguridad;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfigSeguridad {
    
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    JwtUtils jwtUtils;
    
    @Autowired
    FilterAutorizacion filterAutorizacion;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity
            ) throws Exception{
        
        SecurityFilterChain filtro;
        filtro = httpSecurity
                
                .authorizeHttpRequests(auth -> {                  
                    auth.anyRequest().permitAll();
                })
                
                .headers(headers -> headers
			.contentTypeOptions(contentTypeOptions -> contentTypeOptions.disable())
		)
                .build();
        
        return filtro;   

    }
    
    @Bean
    public SecurityFilterChain securityFilterChain_(HttpSecurity httpSecurity
            ) throws Exception{
        
        SecurityFilterChain filtro;
        filtro = httpSecurity
                
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/favicon.ico").permitAll();
                    auth.requestMatchers("/images/**").permitAll();
                    auth.requestMatchers("/javascript/**").permitAll();
                    auth.requestMatchers("/").permitAll();                    
                    auth.requestMatchers("/login").permitAll();
                    auth.requestMatchers("/altaUsuario").permitAll();
                    auth.requestMatchers("/panel").permitAll();
                    auth.requestMatchers("/crearPartida").permitAll();
                    auth.requestMatchers("/partidaMaster").permitAll();
                    //auth.requestMatchers("/panel").hasAnyRole("USER","COLABORADOR");
                    //auth.requestMatchers("/crearUsuario").hasAnyRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session ->{
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .formLogin(formLogin ->
                    formLogin
                            .loginPage("/login"))
                .headers(headers -> headers
			.contentTypeOptions(contentTypeOptions -> contentTypeOptions.disable())
		)
                .build();
        
        return filtro;   

    }
    
    @Bean
    public SecurityFilterChain securityFilterChain_completo(HttpSecurity httpSecurity
            ) throws Exception{
        
        FilterSeguridad filterSeguridad = new FilterSeguridad(jwtUtils);
        filterSeguridad.setAuthenticationManager(authenticationManager());
        filterSeguridad.setFilterProcessesUrl("/login");
        
        SecurityFilterChain filtro;
        filtro = httpSecurity
                
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/favicon.ico").permitAll();
                    auth.requestMatchers("/images/**").permitAll();
                    auth.requestMatchers("/javascript/**").permitAll();
                    auth.requestMatchers("/").permitAll();                    
                    auth.requestMatchers("/login").permitAll();
                    auth.requestMatchers("/altaUsuario").permitAll();
                    auth.requestMatchers("/panel").authenticated();
                    //auth.requestMatchers("/panel").hasAnyRole("USER","COLABORADOR");
                    //auth.requestMatchers("/crearUsuario").hasAnyRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session ->{
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                
                
                .addFilter(filterSeguridad)
                .addFilterBefore(filterAutorizacion, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
			.contentTypeOptions(contentTypeOptions -> contentTypeOptions.disable())
		)
                .build();
        
        return filtro;   

    }
    
    public AuthenticationSuccessHandler accesoOK(){
        
        return ((request, response, authentication) -> {
            response.sendRedirect("/panel");
        });
        
    }
    
    /*
    @Bean
    public UserDetailsService userDetailsService() {
        
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(User.withUsername("miguel")
                    .password("1234")
                    .roles("USER")
                    .build());

            return manager;
    }
*/
    
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    

    @Bean
    public AuthenticationManager authenticationManager() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService);
            authenticationProvider.setPasswordEncoder(passwordEncoder());

            return new ProviderManager(authenticationProvider);
    }

    
    
}
