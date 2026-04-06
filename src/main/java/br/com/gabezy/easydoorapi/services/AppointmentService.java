package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.domain.appointment.repositories.AppointmentRepository;
import br.com.gabezy.easydoorapi.infra.exceptions.ResourceNotFoundException;
import br.com.gabezy.easydoorapi.infra.mappers.AppointmentMapper;
import br.com.gabezy.easydoorapi.resources.dto.appointment.CreateAppointmentRequest;
import br.com.gabezy.easydoorapi.resources.dto.appointment.FilterAppointmentDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.mapstruct.factory.Mappers;

import java.util.List;

@ApplicationScoped
public class AppointmentService {

    private final AppointmentMapper mapper = Mappers.getMapper(AppointmentMapper.class);
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public Appontiment createAppointment(@Valid CreateAppointmentRequest request) {
        var appointment = mapper.request2Entity(request);
        appointmentRepository.persist(appointment);
        return appointment;
    }

    public List<Appontiment> findByFilter(@Valid FilterAppointmentDTO filter) {
        return appointmentRepository.findAllByFilter(filter);
    }

    public Appontiment findById(Long id) {
        return appointmentRepository.findByIdOptional(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

}
