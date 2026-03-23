package br.com.gabezy.easydoorapi.domain.appointment.repositories;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface AppointmentRepository extends PanacheRepository<Appontiment> {
}
