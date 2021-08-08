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
@Table(name = "COURT_OCCUPANCY_DETAILS")
public class CourtOccupancyDetailsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "COURT_ID")
    private Integer courtId;

    @ManyToOne()
    @JoinColumn(name = "COURT_ID", referencedColumnName = "COURT_ID", insertable = false, updatable = false)
    private TennisCourtDetailsEntity tennisCourtDetailsEntity;

    @Version
    @Column(name="VERSION")
    private long version;
}
