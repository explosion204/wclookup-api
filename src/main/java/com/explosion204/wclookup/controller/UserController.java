package com.explosion204.wclookup.controller;

import com.explosion204.wclookup.service.UserService;
import com.explosion204.wclookup.service.dto.identifiable.UserDto;
import com.explosion204.wclookup.service.pagination.PageContext;
import com.explosion204.wclookup.service.pagination.PaginationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<PaginationModel<UserDto>> getUsers(
            @RequestParam(required = false) Integer page,
            @RequestBody(required = false) Integer pageSize
    ) {
        PaginationModel<UserDto> users = userService.findAll(PageContext.of(page, pageSize));
        return new ResponseEntity<>(users, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") long id) {
        UserDto userDto = userService.findById(id);
        return new ResponseEntity<>(userDto, OK);
    }
}
