package org.example.github.dto;

import org.kohsuke.github.GHRepositoryCloneTraffic;
import org.kohsuke.github.GHRepositoryTraffic;

import java.util.Date;
import java.util.List;

public class CloneTrafficDTO {

    private int total;

    private int uniques;

    private List<Integer> totalTraffic;

    private List<Integer> uniquesTraffic;

    private List<Date> timestamps;

    public CloneTrafficDTO(GHRepositoryCloneTraffic traffic) {

        total = traffic.getCount();
        uniques = traffic.getUniques();
        totalTraffic = traffic.getClones().stream().map(GHRepositoryTraffic.DailyInfo::getCount).toList();
        uniquesTraffic = traffic.getClones().stream().map(GHRepositoryTraffic.DailyInfo::getUniques).toList();
        timestamps = traffic.getClones().stream().map(GHRepositoryTraffic.DailyInfo::getTimestamp).toList();
    }
}
