package com.yieldbroker.sportscenter.scheduler;

import com.yieldbroker.sportscenter.persistence.TennisCourtDetailsEntity;
import com.yieldbroker.sportscenter.persistence.TennisCourtDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourtResetScheduler {

    public static final String COURT_RESET_CONSTANT = "0 22 * * * *";
    @Autowired
    private TennisCourtDetailsRepository tennisCourtDetailsRepository;

    @Scheduled(cron = COURT_RESET_CONSTANT)
    public void resetCourtAvailability() {
        List<TennisCourtDetailsEntity> tennisCourtDetailsEntities = tennisCourtDetailsRepository.findByOrderByCourtIdAsc();
        tennisCourtDetailsEntities.stream()
                .forEach(tennisCourtDetailsEntity -> tennisCourtDetailsEntity.setSlotAvailable(4));
        tennisCourtDetailsRepository.saveAllAndFlush(tennisCourtDetailsEntities);
    }
}
