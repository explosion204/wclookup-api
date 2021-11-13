package com.explosion204.wclookup.service;

import com.explosion204.wclookup.exception.EntityNotFoundException;
import com.explosion204.wclookup.model.entity.Toilet;
import com.explosion204.wclookup.model.repository.ToiletRepository;
import com.explosion204.wclookup.service.dto.ToiletFilterDto;
import com.explosion204.wclookup.service.dto.identifiable.ToiletDto;
import com.explosion204.wclookup.service.pagination.PageContext;
import com.explosion204.wclookup.service.pagination.PaginationModel;
import com.explosion204.wclookup.service.validation.annotation.ValidateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ToiletService {
    private final ToiletRepository toiletRepository;

    public ToiletService(ToiletRepository toiletRepository) {
        this.toiletRepository = toiletRepository;
    }

    public PaginationModel<ToiletDto> findAll(PageContext pageContext) {
        PageRequest pageRequest = pageContext.toPageRequest();
        Page<ToiletDto> page = toiletRepository.findAll(pageRequest)
                .map(ToiletDto::fromToilet);
        return PaginationModel.fromPage(page);
    }

    @ValidateDto
    public PaginationModel<ToiletDto> findByParams(ToiletFilterDto toiletFilterDto, PageContext pageContext) {
        Page<Toilet> toiletPage;
        PageRequest pageRequest = pageContext.toPageRequest();

        if (toiletFilterDto.hasNoNullAttributes()) {
            double latitude = toiletFilterDto.getLatitude();
            double longitude = toiletFilterDto.getLongitude();
            int radius = toiletFilterDto.getRadius();

            toiletPage = toiletRepository.findByRadius(latitude, longitude, radius, pageRequest);
        } else {
            toiletPage = toiletRepository.findAllConfirmed(pageRequest);
        }

        Page<ToiletDto> dtoPage = toiletPage.map(ToiletDto::fromToilet);
        return PaginationModel.fromPage(dtoPage);
    }

    public ToiletDto findById(long id, boolean requireConfirmed) {
        Optional<Toilet> result = requireConfirmed
                ? toiletRepository.findByIdConfirmed(id)
                : toiletRepository.findById(id);
        Toilet toilet = result.orElseThrow(() -> new EntityNotFoundException(Toilet.class));
        return ToiletDto.fromToilet(toilet);
    }

    @ValidateDto
    public ToiletDto create(ToiletDto toiletDto, boolean isProposal) {
        if (isProposal) {
            toiletDto.setConfirmed(false);
        }

        Toilet toilet = toiletDto.toToilet();
        Toilet savedToilet = toiletRepository.save(toilet);

        return ToiletDto.fromToilet(savedToilet);
    }

    @ValidateDto
    public ToiletDto update(ToiletDto toiletDto) {
        Toilet toilet = toiletRepository.findById(toiletDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(Toilet.class));

        if (toiletDto.getAddress() != null) {
            toilet.setAddress(toiletDto.getAddress());
        }

        if (toiletDto.getSchedule() != null) {
            toilet.setSchedule(toiletDto.getSchedule());
        }

        if (toiletDto.getLatitude() != null) {
            toilet.setLatitude(toiletDto.getLatitude());
        }

        if (toiletDto.getLongitude() != null) {
            toilet.setLatitude(toiletDto.getLongitude());
        }

        if (toiletDto.getConfirmed() != null) {
            toilet.setConfirmed(toiletDto.getConfirmed());
        }

        Toilet updatedToilet = toiletRepository.save(toilet);
        return ToiletDto.fromToilet(updatedToilet);
    }

    public void delete(long id) {
        Toilet toilet = toiletRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Toilet.class));
        toiletRepository.delete(toilet);
    }
}
