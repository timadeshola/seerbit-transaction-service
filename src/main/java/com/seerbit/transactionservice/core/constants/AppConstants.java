package com.seerbit.transactionservice.core.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * Project title: transaction-service
 *
 * @author johnadeshola
 * Date: 8/23/23
 * Time: 6:35 PM
 */
public interface AppConstants {
    public static DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    public static final DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public interface RestMessage {
        String success = "Success";
        String failed = "Failed";
        String created = "Created";
    }
}
