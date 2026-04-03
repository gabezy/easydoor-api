package br.com.gabezy.easydoorapi.infra.mappers;

import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import br.com.gabezy.easydoorapi.resources.dto.building.CreateLockerRequest;
import org.mapstruct.Mapper;

@Mapper
public interface LockerMapper {

    Locker request2Entity(CreateLockerRequest request);

}
