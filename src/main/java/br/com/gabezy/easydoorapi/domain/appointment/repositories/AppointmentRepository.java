package br.com.gabezy.easydoorapi.domain.appointment.repositories;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.resources.dto.appointment.FilterAppointmentDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface AppointmentRepository extends PanacheRepository<Appontiment>{

    List<Appontiment> findAllByFilter(FilterAppointmentDTO filter);

}
