package co.sena.cimm.adso.Sistema_Donacion_Sangre;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.Rol;  // <- agregar este import
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Usuario;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SistemaDonacionSangreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDonacionSangreApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UsuarioRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.findByUsername("admin").isEmpty()) {
				Usuario user = new Usuario();
				user.setUsername("admin");
				user.setPassword(encoder.encode("123456"));
				user.setRol(Rol.ADMIN);
				repo.save(user);
				System.out.println("Usuario admin creado");
			}
		};
	}
}