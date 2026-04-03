package br.com.gabezy.easydoorapi.infra.mappers;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.resources.dto.building.CreateBuildingRequest;
import org.mapstruct.Mapper;

@Mapper
public interface BuildingMapper {

    Building request2Entity(CreateBuildingRequest request);

}
