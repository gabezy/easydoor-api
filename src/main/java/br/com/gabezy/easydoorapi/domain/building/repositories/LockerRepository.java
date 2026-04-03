package br.com.gabezy.easydoorapi.domain.building.repositories;

import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

public interface LockerRepository extends PanacheRepository<Locker> {

    Optional<Locker> findBySerialNumber(String serialNumber);

}
