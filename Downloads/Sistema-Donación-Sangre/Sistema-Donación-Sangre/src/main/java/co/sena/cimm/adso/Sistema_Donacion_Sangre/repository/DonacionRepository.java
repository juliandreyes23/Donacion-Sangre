package co.sena.cimm.adso.Sistema_Donacion_Sangre.repository;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion,Long> {
    List<Donacion> findByDonanteId(Long donanteId);
    boolean existsByCodigoDonacion(String codigoDonacion);


}
