package com.explosion204.wclookup.service.validation.constraint;

import com.explosion204.wclookup.service.dto.IdentifiableDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Objects;

@Component
public class RestDtoValidator implements ConstraintValidator<RestDto, IdentifiableDto> {
    private static final String ID_FIELD = "id";

    @SneakyThrows
    @Override
    public boolean isValid(IdentifiableDto dto, ConstraintValidatorContext context) {
        if (dto.getId() == null) {
            // check if all fields are not null except for id
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mappedObject = objectMapper.convertValue(dto, new TypeReference<>() {});
            mappedObject.remove(ID_FIELD);

            return mappedObject.values()
                    .stream()
                    .allMatch(Objects::nonNull);
        }

        return true;
    }
}
