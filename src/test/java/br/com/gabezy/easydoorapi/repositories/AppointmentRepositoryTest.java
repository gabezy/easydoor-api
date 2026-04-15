package br.com.gabezy.easydoorapi.repositories;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.domain.appointment.repositories.AppointmentRepository;
import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import br.com.gabezy.easydoorapi.domain.user.entities.Client;
import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.resources.dto.appointment.FilterAppointmentDTO;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestTransaction
public class AppointmentRepositoryTest {

    @Inject
    AppointmentRepository appointmentRepository;

    @Test
    public void shouldCreateAppointment() {
        var client = createClient();
        var realestateAgent = createRealEstateAgent();
        var building = createBuilding();

        Appontiment appontiment = new Appontiment(
                LocalDateTime.now(),
                client.id,
                realestateAgent.id,
                building.id,
                null,
                null,
                null,
                null
        );

        appointmentRepository.persist(appontiment);
        appointmentRepository.flush();

        assertNotNull(appontiment.id);
        assertEquals(client.id, appontiment.clientId);
        assertEquals(realestateAgent.id, appontiment.realEstateAgentId);
        assertEquals(building.id, appontiment.buildingId);
        assertNotNull(appontiment.time);
    }

    @Test
    public void shouldFindAllByFilter() {
        var client = createClient();
        var realestateAgent = createRealEstateAgent();
        var building = createBuilding();

        var filter = new FilterAppointmentDTO(building.id, null, null, null, null, null, false);

        var appontiment = new Appontiment(
                LocalDateTime.now(),
                client.id,
                realestateAgent.id,
                building.id,
                null,
                null,
                null,
                null
        );

        var appointment2 = new Appontiment(
                LocalDateTime.now(),
                client.id,
                realestateAgent.id,
                building.id,
                LocalDateTime.now().plusDays(1),
                null,
                null,
                null
        );

        appointmentRepository.persist(List.of(appontiment, appointment2));
        appointmentRepository.flush();

        var foundAppointments = appointmentRepository.findAllByFilter(filter);

        assertEquals(1, foundAppointments.size());
        assertEquals(appontiment.id, foundAppointments.getFirst().id);
    }

    @Test
    public void shouldNotFindByFilterBuldingId() {
        var filter = new FilterAppointmentDTO(10L, 1L, null, null, null, null, false);
        var client = createClient();
        var realestateAgent = createRealEstateAgent();
        var building = createBuilding();

        var appontiment = new Appontiment(
                LocalDateTime.now(),
                client.id,
                realestateAgent.id,
                building.id,
                null,
                null,
                null,
                null
        );

        var appointment2 = new Appontiment(
                LocalDateTime.now(),
                client.id,
                realestateAgent.id,
                building.id,
                LocalDateTime.now().plusDays(1),
                null,
                null,
                null
        );

        appointmentRepository.persist(List.of(appontiment, appointment2));
        appointmentRepository.flush();

        var foundAppointments = appointmentRepository.findAllByFilter(filter);

        assertEquals(0, foundAppointments.size());
    }

    private Building createBuilding() {
        var address = new Address(
                "Rua Teste, 123",
                "São Paulo",
                "SP",
                "Brasil",
                "01234-567"
        );
        Building building = new Building(
                "building 1",
                createLocker().id,
                new GeographicalCoordinates(BigDecimal.ONE, BigDecimal.ONE),
                100D,
                "building 1 description",
                address
        );

        building.persistAndFlush();
        return building;
    }

    private Locker createLocker() {
        var coordinates = new GeographicalCoordinates(BigDecimal.ONE, BigDecimal.ONE);
        Locker locker = new Locker("teste22", "locker 1", coordinates);
        locker.persistAndFlush();
        return locker;
    }

    private RealEstateAgent createRealEstateAgent() {
        var user = new User("realestateagent1", "somevalidemail@email.com", "passwrod");
        user.persistAndFlush();

        var realEstateAgent = new RealEstateAgent();
        realEstateAgent.name = "Jonh Doe";
        realEstateAgent.cnpj = "12345678000199";
        realEstateAgent.creci = "1234567890";
        realEstateAgent.phone = "1234567890";
        realEstateAgent.userId = user.id;
        realEstateAgent.address = new Address(
                "Rua Teste, 123",
                "São Paulo",
                "SP",
                "Brasil",
                "01234-567"
        );

        realEstateAgent.persistAndFlush();

        return realEstateAgent;
    }

    private Client createClient() {
        var user = new User("client1", "somevalidemai1l@email.com", "passwrod");
        user.persistAndFlush();

        Client client = new Client("Jonh Kenenedy", "13761376073", user.id);
        client.persistAndFlush();
        return client;
    }


}
