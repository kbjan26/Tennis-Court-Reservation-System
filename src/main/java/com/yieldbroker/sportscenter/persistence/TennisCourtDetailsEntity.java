package com.yieldbroker.sportscenter.persistence;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TENNIS_COURT_DETAILS")
public class TennisCourtDetailsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COURT_ID")
    private Integer courtId;

    @Column(name = "COURT_NAME")
    private String courtName;

    @Column(name = "SLOT_AVAILABLE")
    private Integer slotAvailable;

    @Version
    @Column(name = "VERSION")
    private long version;


}
