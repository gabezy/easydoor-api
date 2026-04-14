package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.domain.building.repositories.BuildingRepository;
import br.com.gabezy.easydoorapi.infra.mappers.BuildingMapper;
import br.com.gabezy.easydoorapi.resources.dto.building.CreateBuildingRequest;
import br.com.gabezy.easydoorapi.resources.dto.building.FilterBuildingDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.mapstruct.factory.Mappers;

import java.util.List;

@ApplicationScoped
public class BuildingService {

    private final BuildingMapper mapper = Mappers.getMapper(BuildingMapper.class);
    private final BuildingRepository repository;

    public BuildingService(BuildingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Building create(@Valid CreateBuildingRequest request) {
        var building = mapper.request2Entity(request);
        repository.persist(building);
        return building;
    }

    public List<Building> findByFilter(@Valid FilterBuildingDTO filter) {
        return repository.findAllByFilter(filter);
    }

}
