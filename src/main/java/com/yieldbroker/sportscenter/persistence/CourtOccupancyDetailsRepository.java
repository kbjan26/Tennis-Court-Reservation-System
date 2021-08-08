package com.yieldbroker.sportscenter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourtOccupancyDetailsRepository extends JpaRepository<CourtOccupancyDetailsEntity, Integer> {

    CourtOccupancyDetailsEntity findByUserId(Integer userId);

    List<CourtOccupancyDetailsEntity> findByCourtId(Integer courtId);
}
