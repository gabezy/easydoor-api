package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import br.com.gabezy.easydoorapi.domain.building.repositories.LockerRepository;
import br.com.gabezy.easydoorapi.infra.mappers.LockerMapper;
import br.com.gabezy.easydoorapi.resources.dto.building.CreateLockerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
public class LockerService {

    private final LockerMapper mapper = Mappers.getMapper(LockerMapper.class);
    private final LockerRepository lockerRepository;

    public LockerService(LockerRepository lockerRepository) {
        this.lockerRepository = lockerRepository;
    }

    @Transactional
    public Locker create(@Valid CreateLockerRequest request) {
        var locker = mapper.request2Entity(request);
        lockerRepository.persist(locker);
        return locker;
    }


}
