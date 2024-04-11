package propensi.c06.sipp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import propensi.c06.sipp.security.jwt.JwtTokenFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;


    @Bean
    @Order(1)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {

        http.securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/user/add").hasAuthority("Admin")
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/rencana/view-all").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/user")).hasAuthority("Admin")
                        .requestMatchers(new AntPathRequestMatcher("/user/**")).hasAuthority("Admin")
                        .requestMatchers(new AntPathRequestMatcher("/dashboard")).hasAnyAuthority("Manajer", "Operasional", "Keuangan")
                        .requestMatchers(new AntPathRequestMatcher("/api/rencana/view-all")).hasAnyAuthority("Manajer", "Operasional", "Keuangan") 
                        .requestMatchers(new AntPathRequestMatcher("/rencana/detail/**")).hasAnyAuthority("Manajer", "Operasional", "Keuangan") 
                        .requestMatchers(new AntPathRequestMatcher("/rencana/")).hasAnyAuthority("Manajer", "Operasional", "Keuangan")
                        .requestMatchers(new AntPathRequestMatcher("/rencana/create")).hasAnyAuthority("Manajer", "Operasional")
                        .requestMatchers(new AntPathRequestMatcher("/barang/detail/**")).hasAnyAuthority("Manajer", "Operasional")
                        .requestMatchers(new AntPathRequestMatcher("/barang/tambah")).hasAnyAuthority("Manajer", "Operasional")
                        .requestMatchers(new AntPathRequestMatcher("/barang")).hasAnyAuthority("Manajer", "Operasional", "Keuangan")
                        .requestMatchers(new AntPathRequestMatcher("/vendor")).hasAnyAuthority("Manajer", "Operasional", "Keuangan")
                        .requestMatchers(new AntPathRequestMatcher("/vendor/tambah")).hasAnyAuthority("Manajer", "Operasional")
                        .requestMatchers(new AntPathRequestMatcher("/vendor/detail/**")).hasAnyAuthority("Manajer", "Operasional", "Keuangan")
                        .requestMatchers(new AntPathRequestMatcher("/pengadaan")).hasAnyAuthority("Manajer", "Operasional", "Keuangan")
                        .requestMatchers(new AntPathRequestMatcher("/pengadaan/tambah")).hasAnyAuthority("Manajer", "Keuangan")
                        .requestMatchers(new AntPathRequestMatcher("/pengadaan/detail/**")).hasAnyAuthority("Manajer", "Operasional", "Keuangan")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/")
                )
                .logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                )
                // .exceptionHandling((error) -> error.accessDeniedPage("/error"))
                
        ;
        return http.build();
    }
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
}
