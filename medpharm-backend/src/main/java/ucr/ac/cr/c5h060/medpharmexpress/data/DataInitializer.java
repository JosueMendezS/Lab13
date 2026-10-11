package ucr.ac.cr.c5h060.medpharmexpress.data;

import ucr.ac.cr.c5h060.medpharmexpress.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner fixPasswords(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> repo.findAll().forEach(u -> {
            u.setPassword(encoder.encode("password123"));
            repo.save(u);
        });
    }
}