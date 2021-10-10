package com.explosion204.wclookup.controller;

import com.explosion204.wclookup.service.ToiletService;
import com.explosion204.wclookup.service.dto.ToiletDto;
import com.explosion204.wclookup.service.pagination.PaginationModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/toilets")
public class ToiletController {
    private final ToiletService toiletService;

    public ToiletController(ToiletService toiletService) {
        this.toiletService = toiletService;
    }

    @GetMapping
    public ResponseEntity<PaginationModel<ToiletDto>> getToilets(@PageableDefault Pageable pageable) {
        PaginationModel<ToiletDto> toilets = toiletService.findAll(pageable);
        return new ResponseEntity<>(toilets, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToiletDto> getToilet(@PathVariable("id") long id) {
        ToiletDto toiletDto = toiletService.find(id);
        return new ResponseEntity<>(toiletDto, OK);
    }

    @PostMapping
    public ResponseEntity<ToiletDto> createToilet(@RequestBody ToiletDto toiletDto) {
        toiletDto.setId(null); // new entity cannot have id
        ToiletDto createdToiletDto = toiletService.create(toiletDto);

        return new ResponseEntity<>(createdToiletDto, CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ToiletDto> updateToilet(@PathVariable("id") long id, @RequestBody ToiletDto toiletDto) {
        toiletDto.setId(id); // existing entity must have id
        ToiletDto updatedToiletDto = toiletService.update(toiletDto);

        return new ResponseEntity<>(updatedToiletDto, OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToilet(@PathVariable("id") long id) {
        toiletService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
