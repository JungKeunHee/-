package com.ohgiraffers.semiproject.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd"; // 날짜 형식

    public static Date parseDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace(); // 예외 처리
            return null; // 변환 실패 시 null 반환
        }
    }

}
