package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.shared.vo.Email;
import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.exceptions.NegocioException;
import br.com.gabezy.easydoorapi.infra.repositories.RealEstateAgentRepositoryImpl;
import br.com.gabezy.easydoorapi.infra.repositories.RoleRepositoryImpl;
import br.com.gabezy.easydoorapi.infra.repositories.UserRepositoryImpl;
import br.com.gabezy.easydoorapi.resources.dto.realestateagent.CreateRealEstateAgentRequest;
import br.com.gabezy.easydoorapi.resources.dto.realestateagent.RealEstateAgentDTO;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;

@ApplicationScoped
public class RealEstateAgentService {

    private final RealEstateAgentRepositoryImpl realEstateAgentRepository;
    private final UserRepositoryImpl userRepository;
    private final RoleRepositoryImpl roleRepository;

    public RealEstateAgentService(
            RealEstateAgentRepositoryImpl realEstateAgentRepository,
            UserRepositoryImpl userRepository,
            RoleRepositoryImpl roleRepository
    ) {
        this.realEstateAgentRepository = realEstateAgentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public RealEstateAgent create(@Valid CreateRealEstateAgentRequest request) {
        var normalizedEmail = new Email(request.email()).value();

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new NegocioException("Email already exists");
        }

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new NegocioException("Username already exists");
        }

        var user = new User(
                request.username(),
                normalizedEmail,
                BcryptUtil.bcryptHash(request.password())
        );

        var agentRole = roleRepository.findByName("AGENT")
                .orElseThrow(() -> new NegocioException("Role AGENT not found"));

        user.addRole(agentRole);

        userRepository.persist(user);

        var realEstateAgent = new RealEstateAgent();
        realEstateAgent.name = request.name();
        realEstateAgent.cnpj = request.cnpj();
        realEstateAgent.creci = request.creci();
        realEstateAgent.phone = request.phone();
        realEstateAgent.userId = user.id;
        realEstateAgent.address = new Address(
                request.address().address(),
                request.address().city(),
                request.address().state(),
                request.address().country(),
                request.address().zipCode()
        );

        realEstateAgentRepository.persist(realEstateAgent);
        return realEstateAgent;
    }

    public List<RealEstateAgentDTO> findAll() {
        return realEstateAgentRepository.listAll().stream()
                .map(RealEstateAgentDTO::new)
                .toList();
    }
}
