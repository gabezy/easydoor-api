package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.domain.appointment.repositories.AppointmentRepository;
import br.com.gabezy.easydoorapi.domain.user.entities.Client;
import br.com.gabezy.easydoorapi.domain.user.repositories.ClientRepository;
import br.com.gabezy.easydoorapi.infra.exceptions.ResourceNotFoundException;
import br.com.gabezy.easydoorapi.resources.dto.appointment.CreateAppointmentRequest;
import br.com.gabezy.easydoorapi.resources.dto.appointment.FilterAppointmentDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AppointmentServiceTest {

    @Inject
    AppointmentService appointmentService;

    @InjectMock
    AppointmentRepository appointmentRepository;

    @InjectMock
    ClientRepository clientRepository;

    @Test
    public void shouldCreateAppointment() {
        var request = new CreateAppointmentRequest(
                LocalDateTime.now(),
                1L,
                1L,
                1L
        );

        Mockito.when(clientRepository.findByIdOptional(request.userId())).thenReturn(Optional.of(new Client(
                "name", "321321321", 1L
        )));

        Mockito.doAnswer(invocation -> {
            var appointmentToSave = invocation.getArgument(0, Appontiment.class);
            appointmentToSave.id = 1L;
            return null;
        }).when(appointmentRepository).persist(Mockito.any(Appontiment.class));

        var appointment = appointmentService.createAppointment(request);

        assertEquals(request.time(), appointment.time);
        assertEquals(1L, appointment.clientId);
        assertEquals(request.realEstateAgentId(), appointment.realEstateAgentId);
        assertNull(appointment.canceledAt);
        assertNull(appointment.approvedAt);
        assertNull(appointment.finishedAt);

        Mockito.verify(appointmentRepository, Mockito.times(1)).persist(Mockito.any(Appontiment.class));
    }

    @Test
    public void shouldFindAppointById() {
        var appointment = new Appontiment(
                LocalDateTime.now(),
                1L,
                1L,
                1L,
                null,
                null,
                null,
                null
        );

        Mockito.when(appointmentRepository.findByIdOptional(1L)).thenReturn(Optional.of(appointment));

        var foundAppointment = appointmentService.findById(1L);

        assertEquals(appointment, foundAppointment);
        Mockito.verify(appointmentRepository, Mockito.times(1)).findByIdOptional(1L);
    }

    @Test
    public void shouldNotFindAppointById() {
        Mockito.when(appointmentRepository.findByIdOptional(1L)).thenReturn(Optional.empty());
        assertThrowsExactly(ResourceNotFoundException.class, () -> appointmentService.findById(1L));
        Mockito.verify(appointmentRepository, Mockito.times(1)).findByIdOptional(1L);
    }

    @Test
    public void shouldFindByFilter() {
        var appointments = List.of(
                new Appontiment(
                        LocalDateTime.now(),
                        1L,
                        1L,
                        1L,
                        null,
                        null,
                        null,
                        null
                ),
                new Appontiment(
                        LocalDateTime.now().plusDays(1),
                        1L,
                        1L,
                        1L,
                        null,
                        null,
                        null,
                        null
                )
        );

        Mockito.when(appointmentRepository.findAllByFilter(Mockito.any(FilterAppointmentDTO.class))).thenReturn(appointments);
        var foundAppointments = appointmentService.findByFilter(new FilterAppointmentDTO(1L, null, null, null, null, null,  false));

        assertEquals(2, foundAppointments.size());
        assertEquals(appointments, foundAppointments);
        Mockito.verify(appointmentRepository, Mockito.times(1)).findAllByFilter(Mockito.any(FilterAppointmentDTO.class));
    }

}
