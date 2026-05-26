package co.sena.cimm.adso.Sistema_Donacion_Sangre.repository;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
}