package com.explosion204.wclookup.service;

import com.explosion204.wclookup.exception.EntityNotFoundException;
import com.explosion204.wclookup.model.entity.User;
import com.explosion204.wclookup.model.repository.UserRepository;
import com.explosion204.wclookup.service.dto.UserDto;
import com.explosion204.wclookup.service.pagination.PaginationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PaginationModel<UserDto> findAll(Pageable pageable) {
        Page<UserDto> page = userRepository.findAll(pageable)
                .map(UserDto::fromUser);
        return PaginationModel.fromPage(page);
    }

    public UserDto find(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return UserDto.fromUser(user);
    }
}
