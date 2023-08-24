package com.seerbit.transactionservice.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.seerbit.transactionservice.core.constants.AppConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.Duration;
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

    public static String toJson(Type type) {
        try {
            return mapper().writeValueAsString(type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error occurred serializing object to json string, error => " + e.getMessage());
        }
    }

    public static <T> String toJson(T t) {
        try {
            return mapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error occurred serializing object to json string, error => " + e.getMessage());
        }
    }

    public static <T> String fromJson(T t) {
        try {
            return mapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error occurred serializing object to json string, error => " + e.getMessage());
        }
    }


    @SneakyThrows
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return mapper().readValue(jsonString, clazz);
    }

    public static boolean timestampCheck(Timestamp timestamp1, Timestamp timestamp2, long seconds) {
        Duration duration = Duration.ofMillis(timestamp1.getTime()).minus(Duration.ofMillis(timestamp2.getTime()));
        if (duration.getSeconds() <= seconds) {
            return true;
        }
        return false;
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
