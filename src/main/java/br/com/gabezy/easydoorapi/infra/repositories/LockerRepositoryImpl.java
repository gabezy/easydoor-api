package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import br.com.gabezy.easydoorapi.domain.building.repositories.LockerRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class LockerRepositoryImpl implements LockerRepository {

    @Override
    public Optional<Locker> findBySerialNumber(String serialNumber) {
        return find("serialNumber", serialNumber).singleResultOptional();
    }
}
