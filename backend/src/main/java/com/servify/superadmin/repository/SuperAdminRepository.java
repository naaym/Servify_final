package com.servify.superadmin.repository;

import com.servify.superadmin.model.SuperAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepository  extends JpaRepository<SuperAdminEntity,Integer> {
  boolean existsByEmail(String email);
}
