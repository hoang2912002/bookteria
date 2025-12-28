package com.hamet.post.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class DateTimeFormatter {
    /**
     * @LinkedHashMap
     * để không thay đổi thứ tự map
     * 
     * Map/Dictionary-Based Strategy
     */
    Map<Long, Function<Instant, String>> strategyMap = new LinkedHashMap<>();
    

    public DateTimeFormatter() {
        strategyMap.put(60L, this::formatInSeconds);
        strategyMap.put(3600L, this::formatInMinutes);
        strategyMap.put(86400L, this::formatInHours);
        strategyMap.put(Long.MAX_VALUE, this::formatInDays);
    }
    
    private String formatInDays(Instant instant){
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(dateTimeFormatter);
    }

    public String format(Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());

        // Lọc để lấy map phù hợp theo key
        var stategy = strategyMap.entrySet().stream().filter(e -> {
            return elapseSeconds < e.getKey();
        }).findFirst().get();

        // Lấy value và sau đó gọi function bằng apply truyền vào instant
        return stategy.getValue().apply(instant);
    }

    private String formatInSeconds(Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        return String.format("%dseconds", elapseSeconds);
    }

    private String formatInMinutes(Instant instant){
        long elapseMinus = ChronoUnit.MINUTES.between(instant, Instant.now());
        return String.format("%dminutes", elapseMinus);
    }

    private String formatInHours(Instant instant){
        long elapseHours = ChronoUnit.HOURS.between(instant, Instant.now());
            return String.format("%dhours", elapseHours);
    }
}
