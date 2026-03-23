package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.infra.mappers.AppointmentMapper;
import br.com.gabezy.easydoorapi.resources.dto.appointment.CreateAppointmentRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
public class AppointmentService {

    private final AppointmentMapper mapper = Mappers.getMapper(AppointmentMapper.class);

    @Transactional
    public Appontiment createAppointment(CreateAppointmentRequest request) {
        var appointment = mapper.request2Entity(request);
        appointment.persist();
        return appointment;
    }

}
