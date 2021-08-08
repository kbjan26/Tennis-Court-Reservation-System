package com.yieldbroker.sportscenter.web;

import com.yieldbroker.sportscenter.domain.CourtOccupancyDetails;
import com.yieldbroker.sportscenter.service.CourtProvisioningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/court")
public class CourtProvisioningController {

    @Autowired
    private CourtProvisioningService courtProvisioningService;

    @PostMapping("/provision/{userId}")
    public CourtOccupancyDetails assignCourt(@PathVariable Integer userId) throws Exception {
        return courtProvisioningService.assignCourt(userId);
    }
}
