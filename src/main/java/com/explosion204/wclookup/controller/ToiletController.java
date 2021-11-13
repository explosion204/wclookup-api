package com.explosion204.wclookup.controller;

import com.explosion204.wclookup.controller.util.AuthUtil;
import com.explosion204.wclookup.service.ToiletService;
import com.explosion204.wclookup.service.dto.SearchDto;
import com.explosion204.wclookup.service.dto.ToiletDto;
import com.explosion204.wclookup.service.pagination.PageContext;
import com.explosion204.wclookup.service.pagination.PaginationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.explosion204.wclookup.security.ApplicationAuthority.ADMIN;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/toilets")
public class ToiletController {
    private final ToiletService toiletService;
    private final AuthUtil authUtil;

    public ToiletController(ToiletService toiletService, AuthUtil authUtil) {
        this.toiletService = toiletService;
        this.authUtil = authUtil;
    }

    @GetMapping
    public ResponseEntity<PaginationModel<ToiletDto>> getToilets(
            @ModelAttribute SearchDto searchDto,
            @RequestParam(required = false) Integer page,
            @RequestBody(required = false) Integer pageSize
    ) {
        PageContext pageContext = PageContext.of(page, pageSize);
        // admin can load get all toilets, even not confirmed
        PaginationModel<ToiletDto> toilets = authUtil.hasAuthority(ADMIN.getAuthority())
                ? toiletService.findAll(pageContext)
                : toiletService.findByParams(searchDto, pageContext);
        return new ResponseEntity<>(toilets, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToiletDto> getToilet(@PathVariable("id") long id) {
        // admin can view not confirmed toilets
        boolean requireConfirmed = !authUtil.hasAuthority(ADMIN.getAuthority());
        ToiletDto toiletDto = toiletService.findById(id, requireConfirmed);

        return new ResponseEntity<>(toiletDto, OK);
    }

    @PostMapping
    public ResponseEntity<ToiletDto> createToilet(@RequestBody ToiletDto toiletDto) {
        toiletDto.setId(null); // new entity cannot have id
        // user can only propose toilet (isConfirmed = false)
        boolean isProposal = !authUtil.hasAuthority(ADMIN.getAuthority());
        ToiletDto createdToiletDto = toiletService.create(toiletDto, isProposal);

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
