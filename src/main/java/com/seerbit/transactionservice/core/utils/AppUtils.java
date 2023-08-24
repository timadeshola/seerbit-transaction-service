package com.seerbit.transactionservice.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.seerbit.transactionservice.core.constants.AppConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import static com.seerbit.transactionservice.core.constants.AppConstants.dateTimeFormatter;

/**
 * Project title: transaction-service
 *
 * @author johnadeshola
 * Date: 8/23/23
 * Time: 5:59 PM
 */
@Slf4j
public class AppUtils {

    private static ObjectMapper mapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }

    public static <T> String toJson(T t) {
        try {
            return mapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error occurred serializing object to json string, error => " + e.getMessage());
        }
    }

    public static boolean isTimeDifferenceWithinThreshold(Timestamp timestamp1, Timestamp timestamp2, int thresholdSeconds) {
        long time1 = timestamp1.getTime();
        long time2 = timestamp2.getTime();
        long timeDifferenceInMillis = time1 - time2;
        long timeDifferenceInSeconds = timeDifferenceInMillis / 1000;
        log.info("timeDifferenceInSeconds: {}", timeDifferenceInSeconds);
        return timeDifferenceInSeconds <= thresholdSeconds;
    }

    @SneakyThrows
    public static Timestamp parseDateUtil(String dateStr) {
        Date parsedDate = AppConstants.formatter.parse(dateStr);
        return new Timestamp(parsedDate.getTime());
    }

    public static String parseTimestamp(Timestamp timestamp) {
        return AppConstants.formatter.format(timestamp);
    }

    public static boolean isValidDate(String dateTime) {
        boolean valid = true;
        try {
            TemporalAccessor parse = dateTimeFormatter.parse(dateTime);
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }
}
