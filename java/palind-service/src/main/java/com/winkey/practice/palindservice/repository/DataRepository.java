package com.winkey.practice.palindservice.repository;

import com.winkey.practice.palindservice.model.PalindromeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Venkatesh Rajendran
 */

public interface DataRepository extends PagingAndSortingRepository<PalindromeEntity, Integer> {
    PalindromeEntity findByData(String data);

    List<PalindromeEntity> findByDataStartsWithIgnoreCase(String data, Sort sort);
}
