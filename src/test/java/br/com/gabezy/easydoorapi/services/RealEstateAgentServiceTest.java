package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.role.entities.Role;
import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.exceptions.NegocioException;
import br.com.gabezy.easydoorapi.infra.repositories.RealEstateAgentRepositoryImpl;
import br.com.gabezy.easydoorapi.infra.repositories.RoleRepositoryImpl;
import br.com.gabezy.easydoorapi.infra.repositories.UserRepositoryImpl;
import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import br.com.gabezy.easydoorapi.resources.dto.realestateagent.CreateRealEstateAgentRequest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RealEstateAgentServiceTest {

    @Inject
    RealEstateAgentService service;

    @InjectMock
    RealEstateAgentRepositoryImpl realEstateAgentRepository;

    @InjectMock
    UserRepositoryImpl userRepository;

    @InjectMock
    RoleRepositoryImpl roleRepository;

    @Test
    void shouldCreateRealEstateAgentAndUser() {
        var request = new CreateRealEstateAgentRequest(
                "Imobiliaria Teste",
                "12345678000199",
                "123456",
                "11999999999",
                "agent.test",
                "agent@test.com",
                "123456",
                new AddressDTO("Rua Teste", "Sao Paulo", "SP", "Brasil", "01234-567")
        );

        var agentRole = new Role("AGENT", "Real estate agent");

        Mockito.when(userRepository.findByEmail("agent@test.com")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername("agent.test")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName("AGENT")).thenReturn(Optional.of(agentRole));

        Mockito.doAnswer(invocation -> {
            var user = invocation.getArgument(0, User.class);
            user.id = 10L;
            return null;
        }).when(userRepository).persist(Mockito.any(User.class));

        Mockito.doAnswer(invocation -> {
            var realEstateAgent = invocation.getArgument(0, RealEstateAgent.class);
            realEstateAgent.id = 20L;
            return null;
        }).when(realEstateAgentRepository).persist(Mockito.any(RealEstateAgent.class));

        var realEstateAgent = service.create(request);

        assertNotNull(realEstateAgent.id);
        assertEquals(10L, realEstateAgent.userId);
        assertEquals("Imobiliaria Teste", realEstateAgent.name);
        assertEquals("12345678000199", realEstateAgent.cnpj);
        assertEquals("123456", realEstateAgent.creci);
        assertEquals("11999999999", realEstateAgent.phone);
        assertNotNull(realEstateAgent.address);
        assertEquals("Rua Teste", realEstateAgent.address.address);
        assertEquals("Sao Paulo", realEstateAgent.address.city);
        assertEquals("SP", realEstateAgent.address.state);
        assertEquals("Brasil", realEstateAgent.address.country);
        assertEquals("01234567", realEstateAgent.address.zipCode);

        Mockito.verify(userRepository).persist(Mockito.any(User.class));
        Mockito.verify(realEstateAgentRepository).persist(Mockito.any(RealEstateAgent.class));
    }

    @Test
    void shouldNotCreateWhenEmailAlreadyExists() {
        var request = new CreateRealEstateAgentRequest(
                "Imobiliaria Teste",
                "12345678000199",
                "123456",
                "11999999999",
                "agent.test",
                "agent@test.com",
                "123456",
                new AddressDTO("Rua Teste", "Sao Paulo", "SP", "Brasil", "01234-567")
        );

        Mockito.when(userRepository.findByEmail("agent@test.com"))
                .thenReturn(Optional.of(new User("existing", "agent@test.com", "hash")));

        assertThrowsExactly(NegocioException.class, () -> service.create(request));
        Mockito.verify(userRepository, Mockito.never()).persist(Mockito.any(User.class));
        Mockito.verify(realEstateAgentRepository, Mockito.never()).persist(Mockito.any(RealEstateAgent.class));
    }

    @Test
    void shouldNotCreateWhenUsernameAlreadyExists() {
        var request = new CreateRealEstateAgentRequest(
                "Imobiliaria Teste",
                "12345678000199",
                "123456",
                "11999999999",
                "agent.test",
                "agent@test.com",
                "123456",
                new AddressDTO("Rua Teste", "Sao Paulo", "SP", "Brasil", "01234-567")
        );

        Mockito.when(userRepository.findByEmail("agent@test.com")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername("agent.test"))
                .thenReturn(Optional.of(new User("agent.test", "other@test.com", "hash")));

        assertThrowsExactly(NegocioException.class, () -> service.create(request));
        Mockito.verify(userRepository, Mockito.never()).persist(Mockito.any(User.class));
        Mockito.verify(realEstateAgentRepository, Mockito.never()).persist(Mockito.any(RealEstateAgent.class));
    }

    @Test
    void shouldValidateRequiredFields() {
        var request = new CreateRealEstateAgentRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThrows(ConstraintViolationException.class, () -> service.create(request));
    }

    @Test
    void shouldListAllRealEstateAgents() {
        var user = new User("agent.test", "agent@test.com", "hash");
        user.id = 10L;

        var realEstateAgent = new RealEstateAgent();
        realEstateAgent.id = 20L;
        realEstateAgent.name = "Imobiliaria Teste";
        realEstateAgent.cnpj = "12345678000199";
        realEstateAgent.creci = "123456";
        realEstateAgent.phone = "11999999999";
        realEstateAgent.userId = 10L;
        realEstateAgent.user = user;
        realEstateAgent.address = new Address("Rua Teste", "Sao Paulo", "SP", "Brasil", "01234-567");

        Mockito.when(realEstateAgentRepository.listAll()).thenReturn(java.util.List.of(realEstateAgent));

        var result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(20L, result.getFirst().id());
        assertEquals("Imobiliaria Teste", result.getFirst().name());
        assertEquals("agent.test", result.getFirst().username());
        assertEquals("agent@test.com", result.getFirst().email());
        assertEquals("01234567", result.getFirst().zipCode());
    }
}
