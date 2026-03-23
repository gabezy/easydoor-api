package br.com.gabezy.easydoorapi.infra.mappers;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.resources.dto.appointment.CreateAppointmentRequest;
import org.mapstruct.Mapper;

@Mapper
public interface AppointmentMapper {

    Appontiment request2Entity(CreateAppointmentRequest request);

}
