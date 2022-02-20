package com.example.repository;

import com.example.entity.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends PagingAndSortingRepository<Example, String> {

    Page<Example> findAllByDeletedFalse(Pageable pageable);

    Example findByIdAndDeletedFalse(String id);

}
