package br.com.gabezy.easydoorapi.domain.building.repositories;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.resources.dto.building.FilterBuildingDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends PanacheRepository<Building> {

    Optional<Building> findByLocker(Long lockerId);

    List<Building> findAllByFilter(FilterBuildingDTO filter);

}
