package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.domain.building.repositories.BuildingRepository;
import br.com.gabezy.easydoorapi.resources.dto.building.FilterBuildingDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class BuildingRepositoryImpl implements BuildingRepository {

    @Inject
    EntityManager em;

    @Override
    public Optional<Building> findByLocker(Long lockerId) {
        return find("locker.id = ?1", lockerId).singleResultOptional();
    }


    @Override
    public List<Building> findAllByFilter(FilterBuildingDTO filter) {
        if (Objects.isNull(filter)) {
            return em.createQuery("SELECT b FROM Building b", Building.class).getResultList();
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Building> query = cb.createQuery(Building.class);
        Root<Building> building = query.from(Building.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.name() != null && !filter.name().isBlank()) {
            predicates.add(cb.like(
                    cb.upper(building.get("name")),
                    "%" + filter.name().toUpperCase() + "%"
            ));
        }

        if (filter.description() != null && !filter.description().isBlank()) {
            predicates.add(cb.like(
                    cb.upper(building.get("description")),
                    "%" + filter.description().toUpperCase() + "%"
            ));
        }

        if (filter.area() != null) {
            predicates.add(cb.equal(building.get("area"), filter.area()));
        }

        if (filter.lockerId() != null) {
            predicates.add(cb.equal(building.get("lockerId"), filter.lockerId()));
        }

        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return em.createQuery(query).getResultList();
    }

}
