package com.obakeng.cruddemo.repository;

import com.obakeng.cruddemo.model.BmwCrudDemo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BmwCrudRepository extends JpaRepository<BmwCrudDemo, Long> {
  Page<BmwCrudDemo> findBySettled(boolean settled, Pageable pageable);

  Page<BmwCrudDemo> findByCarContaining(String car, Pageable pageable);
}
