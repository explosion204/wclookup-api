package com.explosion204.wclookup.service.dto;

import com.explosion204.wclookup.model.entity.Toilet;
import com.explosion204.wclookup.service.validation.constraint.RestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@RestDto
public class ToiletDto extends IdentifiableDto {
    @Size(min = 1, max = 1024)
    private String address;

    @Size(min = 1, max = 1024)
    private String schedule;

    @DecimalMin("0.0")
    @DecimalMax("90.0")
    private Double latitude;

    @DecimalMin("0.0")
    @DecimalMax("180.0")
    private Double longitude;

    public Toilet toToilet() {
        Toilet toilet = new Toilet();

        toilet.setId(getId());
        toilet.setAddress(address);
        toilet.setSchedule(schedule);
        toilet.setLatitude(latitude);
        toilet.setLongitude(longitude);

        return toilet;
    }

    public static ToiletDto fromToilet(Toilet toilet) {
        ToiletDto toiletDto = new ToiletDto();

        toiletDto.setId(toilet.getId());
        toiletDto.setAddress(toilet.getAddress());
        toiletDto.setSchedule(toilet.getSchedule());
        toiletDto.setLatitude(toilet.getLatitude());
        toiletDto.setLongitude(toilet.getLongitude());

        return toiletDto;
    }
}
