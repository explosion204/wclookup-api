package com.explosion204.wclookup.model.repository;

import com.explosion204.wclookup.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
