package com.voltunity.evplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.voltunity.evplatform.model.User;

public interface UserRepository extends JpaRepository<User, Long> {}