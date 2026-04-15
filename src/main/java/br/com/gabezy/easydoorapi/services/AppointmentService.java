package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.domain.appointment.repositories.AppointmentRepository;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.domain.user.repositories.ClientRepository;
import br.com.gabezy.easydoorapi.domain.user.repositories.RealEstateAgentRepository;
import br.com.gabezy.easydoorapi.infra.exceptions.ForbiddenException;
import br.com.gabezy.easydoorapi.infra.exceptions.ResourceNotFoundException;
import br.com.gabezy.easydoorapi.infra.mappers.AppointmentMapper;
import br.com.gabezy.easydoorapi.infra.repositories.UserRepositoryImpl;
import br.com.gabezy.easydoorapi.resources.dto.appointment.AppointmentApprovalRequest;
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
    private final ClientRepository clientRepository;
    private final UserRepositoryImpl userRepository;
    private final RealEstateAgentRepository realEstateAgentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, ClientRepository clientRepository, UserRepositoryImpl userRepository, RealEstateAgentRepository realEstateAgentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.realEstateAgentRepository = realEstateAgentRepository;
    }

    @Transactional
    public Appontiment createAppointment(@Valid CreateAppointmentRequest request) {
        var appointment = mapper.request2Entity(request);

        appointment.clientId = clientRepository.findByUserId(request.userId())
                        .map(client -> client.id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client not found for user id: " + request.userId()));
        appointmentRepository.persist(appointment);
        return appointment;
    }

    public List<Appontiment> findByFilter(@Valid FilterAppointmentDTO filter, Long userId) {
        var isAdmin = userRepository.findByIdWithRoles(userId).stream()
                .flatMap(user -> user.getRoles().stream())
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));

        if (isAdmin) {
            return appointmentRepository.findAllByFilter(filter);
        }

        var agentId = realEstateAgentRepository.findByUserId(userId)
                .map(realEstateAgent -> realEstateAgent.id)
                .orElseThrow(() -> new ResourceNotFoundException("Real estate agent not found for user id: " + userId));

        return appointmentRepository.findAllByFilter(filter.WithAgentId(agentId));
    }

    public Appontiment findById(Long id) {
        return appointmentRepository.findByIdOptional(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    @Transactional
    public Appontiment reviewAppointment(Long appointmentId, @Valid AppointmentApprovalRequest request) {
        var appointment = appointmentRepository.findByIdOptional(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        var decisionUser = userRepository.findByIdWithRoles(request.approvedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateDecisionUser(decisionUser, appointment);

        appointment.approvedUserId = decisionUser.id;

        if (Boolean.TRUE.equals(request.approved())) {
            appointment.approvedAt = java.time.LocalDateTime.now();
            appointment.rejectedAt = null;
        } else {
            appointment.rejectedAt = java.time.LocalDateTime.now();
            appointment.approvedAt = null;
        }

        appointmentRepository.persist(appointment);
        return appointment;
    }

    private void validateDecisionUser(User decisionUser, Appontiment appointment) {
        boolean isAdmin = decisionUser.getRoles().stream()
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));

        if (isAdmin) {
            return;
        }

        if (appointment.realEstateAgent == null || !decisionUser.id.equals(appointment.realEstateAgent.userId)) {
            throw new ForbiddenException("User is not allowed to review this appointment");
        }
    }

}
