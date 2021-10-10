package com.explosion204.wclookup.service;

import com.explosion204.wclookup.exception.EntityNotFoundException;
import com.explosion204.wclookup.model.entity.Toilet;
import com.explosion204.wclookup.model.repository.ToiletRepository;
import com.explosion204.wclookup.service.dto.ToiletDto;
import com.explosion204.wclookup.service.pagination.PaginationModel;
import com.explosion204.wclookup.service.validation.DtoValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

@Service
public class ToiletService {
    private final ToiletRepository toiletRepository;

    public ToiletService(ToiletRepository toiletRepository) {
        this.toiletRepository = toiletRepository;
    }

    public PaginationModel<ToiletDto> findAll(@PageableDefault Pageable pageable) {
        Page<ToiletDto> page = toiletRepository.findAll(pageable)
                .map(ToiletDto::fromToilet);
        return PaginationModel.fromPage(page);
    }

    public ToiletDto find(long id) {
        Toilet toilet = toiletRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ToiletDto.fromToilet(toilet);
    }

    @DtoValidation
    public ToiletDto create(ToiletDto toiletDto) {
        Toilet toilet = toiletDto.toToilet();
        Toilet savedToilet = toiletRepository.save(toilet);

        return ToiletDto.fromToilet(savedToilet);
    }

    @DtoValidation
    public ToiletDto update(ToiletDto toiletDto) {
        Toilet toilet = toiletRepository.findById(toiletDto.getId())
                .orElseThrow(EntityNotFoundException::new);

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

        Toilet updatedToilet = toiletRepository.save(toilet);
        return ToiletDto.fromToilet(updatedToilet);
    }

    public void delete(long id) {
        Toilet toilet = toiletRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        toiletRepository.delete(toilet);
    }
}
