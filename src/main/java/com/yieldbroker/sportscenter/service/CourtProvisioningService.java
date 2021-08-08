package com.yieldbroker.sportscenter.service;

import com.yieldbroker.sportscenter.domain.CourtOccupancyDetails;
import com.yieldbroker.sportscenter.exception.CourtAlreadyAssignedException;
import com.yieldbroker.sportscenter.exception.CourtNotAvailableException;
import com.yieldbroker.sportscenter.persistence.CourtOccupancyDetailsEntity;
import com.yieldbroker.sportscenter.persistence.CourtOccupancyDetailsRepository;
import com.yieldbroker.sportscenter.persistence.TennisCourtDetailsEntity;
import com.yieldbroker.sportscenter.persistence.TennisCourtDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CourtProvisioningService {

    public static final int COURT_MAXIMUM_OCCUPANCY = 4;
    public static final String RESOURCE_UNAVAILABLE_MESSAGE = "Courts are allocated for the given day";
    public static final String RESOURCE_ALREADY_ASSIGNED = "You have already been assigned Court : ";
    public static final String COURT_READY_MESSAGE = "You are ready to join the court for play :";

    @Autowired
    private TennisCourtDetailsRepository tennisCourtDetailsRepository;

    @Autowired
    private CourtOccupancyDetailsRepository courtOccupancyDetailsRepository;

    @Autowired
    private Map<Integer, Integer> courtOccupancy;

    @Autowired
    private HashSet<Integer> userOccupancy;

    @Transactional
    public CourtOccupancyDetails assignCourt(Integer userId) throws Exception {
        validate(userId);
        TennisCourtDetailsEntity tennisCourtDetailsEntity = updateSlotAvailability();
        CourtOccupancyDetails courtOccupancyDetails = assignCourt(userId, tennisCourtDetailsEntity);
        updateCache(userId, tennisCourtDetailsEntity.getCourtId());
        return courtOccupancyDetails;
    }

    private CourtOccupancyDetails assignCourt(Integer userId, TennisCourtDetailsEntity tennisCourtDetailsEntity) {
        CourtOccupancyDetailsEntity courtOccupancyDetailsEntity = CourtOccupancyDetailsEntity
                .builder().courtId(tennisCourtDetailsEntity.getCourtId())
                .userId(userId).build();
        courtOccupancyDetailsRepository.saveAndFlush(courtOccupancyDetailsEntity);
        CourtOccupancyDetails courtOccupancyDetails = CourtOccupancyDetails.builder()
                .courtName("You have been assigned Court :" + tennisCourtDetailsEntity.getCourtName())
                .userId(userId).build();
        return courtOccupancyDetails;
    }

    private TennisCourtDetailsEntity updateSlotAvailability() {
        TennisCourtDetailsEntity tennisCourtDetailsEntity = getImmediateAvailableCourt();
        tennisCourtDetailsEntity.setSlotAvailable(tennisCourtDetailsEntity.getSlotAvailable() - 1);
        tennisCourtDetailsRepository.saveAndFlush(tennisCourtDetailsEntity);
        return tennisCourtDetailsEntity;
    }

    private void updateCache(Integer userId, Integer courtId) {
        if (courtOccupancy.get(courtId) != null) {
            courtOccupancy.put(courtId, courtOccupancy.get(courtId) + 1);
        } else {
            courtOccupancy.put(courtId, 1);
        }
        checkForNotificationSend(courtId);
        userOccupancy.add(userId);
    }

    private void checkForNotificationSend(Integer courtId) {
        if(courtOccupancy.get(courtId) == COURT_MAXIMUM_OCCUPANCY){
            CompletableFuture.runAsync(() -> {
                courtOccupancyDetailsRepository.findByCourtId(courtId)
                        .stream()
                        .forEach(courtOccupancyDetailsEntity ->
                                System.out.println(COURT_READY_MESSAGE
                                        + courtOccupancyDetailsEntity.getUserId()));

            });
        }
    }

    private void validate(Integer userId) throws Exception {
        courtOccupancyValidation();
        userOccupancyValidation(userId);
    }

    private TennisCourtDetailsEntity getImmediateAvailableCourt() {
        List<TennisCourtDetailsEntity> tennisCourtDetailsEntities = tennisCourtDetailsRepository.findByOrderByCourtIdAsc();
        TennisCourtDetailsEntity tennisCourtDetailsEntity = tennisCourtDetailsEntities.stream()
                .filter(courtDetails -> courtDetails.getSlotAvailable() > 0).findFirst().get();
        return tennisCourtDetailsEntity;
    }

    private void courtOccupancyValidation() throws CourtNotAvailableException {
        Set<Integer> courtOccupancySet = courtOccupancy.values().stream().collect(Collectors.toSet());
        if (courtOccupancy.size() == 3 &&
                courtOccupancySet.size() == 1 && courtOccupancySet.iterator().next() == 4) {
            throw new CourtNotAvailableException(RESOURCE_UNAVAILABLE_MESSAGE);
        }
    }

    private void userOccupancyValidation(Integer userId) throws CourtAlreadyAssignedException {
        if (userOccupancy.contains(userId)) {
            String courtName = courtOccupancyDetailsRepository.findByUserId(userId)
                    .getTennisCourtDetailsEntity().getCourtName();
            throw new CourtAlreadyAssignedException(RESOURCE_ALREADY_ASSIGNED + courtName);
        }
    }
}
