package com.yieldbroker.sportscenter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TennisCourtDetailsRepository extends JpaRepository<TennisCourtDetailsEntity, Integer> {

    List<TennisCourtDetailsEntity> findByOrderByCourtIdAsc();

}
