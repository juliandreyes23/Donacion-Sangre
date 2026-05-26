package co.sena.cimm.adso.Sistema_Donacion_Sangre.repository;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonanteRepository extends JpaRepository<Donante, Long> {
    Optional<Donante> findByDocumento(String documento);
    Optional<Donante> findByCorreo(String correo);
    boolean existsByDocumento(String documento);
    boolean existsByCorreo(String correo);
    List<Donante> findByTiposangre(TipoSangre tipoSangre);
}