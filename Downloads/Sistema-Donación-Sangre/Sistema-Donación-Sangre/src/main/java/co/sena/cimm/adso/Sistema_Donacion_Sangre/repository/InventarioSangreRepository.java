package co.sena.cimm.adso.Sistema_Donacion_Sangre.repository;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.InventarioSangre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioSangreRepository extends JpaRepository<InventarioSangre, Long> {
    Optional<InventarioSangre> findByTipoSangre(TipoSangre tipoSangre);

    boolean existsByTipoSangre(TipoSangre tipoSangre);

}
