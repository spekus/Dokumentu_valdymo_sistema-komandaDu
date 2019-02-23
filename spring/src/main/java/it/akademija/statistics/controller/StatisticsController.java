package it.akademija.statistics.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.statistics.repository.Statistics;
import it.akademija.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;


@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public StatisticsService getStatisticsService() {
        return statisticsService;
    }

    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @RequestMapping(value = "/approved-docs", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "statistics", notes = "Returns statistics by date interval")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Collection<Statistics> getApprovedDocsStatistics(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "startdate", required = true, defaultValue = "2018-02-21T15:39:07.936")
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam("endDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate) {
        return statisticsService.getApprovedDocsStatistics(authentication.getName(),startDate, endDate);

    }

    @RequestMapping(value = "/approved-docs", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "statistics", notes = "Returns statistics by date interval")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Collection<Statistics> getКуоусеувDocsStatistics(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "startdate", required = true, defaultValue = "2018-02-21T15:39:07.936")
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam("endDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate) {
        return statisticsService.getRejectedDocsStatistics(authentication.getName(),startDate, endDate);

    }

}



