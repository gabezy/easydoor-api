package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.domain.appointment.repositories.AppointmentRepository;
import br.com.gabezy.easydoorapi.resources.dto.appointment.FilterAppointmentDTO;
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

@ApplicationScoped
public class AppointmentRepositoryImpl implements AppointmentRepository {

    @Inject
    EntityManager em;

    @Override
    public List<Appontiment> findAllByFilter(FilterAppointmentDTO filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Appontiment> query = cb.createQuery(Appontiment.class);
        Root<Appontiment> appointment = query.from(Appontiment.class);

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(filter.clientId())) {
            predicates.add(cb.equal(appointment.get("clientId"), filter.clientId()));
        }

        if (Objects.nonNull(filter.realEstateAgentId())) {
            predicates.add(cb.equal(appointment.get("realEstateAgentId"), filter.realEstateAgentId()));
        }

        if (Objects.nonNull(filter.buildingId())) {
            predicates.add(cb.equal(appointment.get("buildingId"), filter.buildingId()));
        }

        if (Objects.nonNull(filter.dateFrom())) {
            predicates.add(cb.greaterThanOrEqualTo(appointment.get("time"), filter.dateFrom().atStartOfDay()));
        }

        if (Objects.nonNull(filter.dateTo())) {
            predicates.add(cb.lessThanOrEqualTo(appointment.get("time"), filter.dateTo().atTime(23, 59, 59)));
        }

        if (filter.canceled()) {
            predicates.add(cb.isNotNull(appointment.get("canceledAt")));
        }

        if (!filter.canceled()) {
            predicates.add(cb.isNull(appointment.get("canceledAt")));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return em.createQuery(query).getResultList();
    }
}
