package com.cgs.authservice.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class CommonInformation {

    public static boolean isPasswordExpired(String lastPasswordChangedDate) {
        Date today = new Date(System.currentTimeMillis());
        System.out.println("Current Day " + today);

        try {
            Date lastPasswordChanged = new SimpleDateFormat("yyyy-MM-dd").parse(lastPasswordChangedDate);
            System.out.println("Last password changed Date " + lastPasswordChanged);

            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(lastPasswordChanged);
            calendar.add(Calendar.DAY_OF_MONTH, 90); // Config expire date 90 days
            // 07-06-2021 10:10:11 after 10-06-2021 10:10:10  // 3 days
            System.out.println("Password Expire Date " + calendar.getTime());
            if (today.after(calendar.getTime())) {
                System.out.println("password expired true");
                return true;
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("password expired false");
        return false;
    }

    public String getCustomDate(String dateString) {
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date convertedDate;
        try {
            convertedDate = parser.parse(dateString);
        } catch (Exception e) {
            System.out.println("==> Exception while converting date : " + e.getMessage());
            return dateString;
        }
        String output = formatter.format(convertedDate);
        System.out.println(dateString + "=> " + output);
        return output.toUpperCase();
    }

    public String generateToken(Integer length) {
        String token;
        String allowedChars = "1234567890";
        StringBuilder salt = new StringBuilder();
        SecureRandom rnd = new SecureRandom();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * allowedChars.length());
            salt.append(allowedChars.charAt(index));
        }
        token = salt.toString();

        System.out.println("Generate token: " + token);
        return token;
    }

    public String generateReferenceId() {
        String pass;
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        SecureRandom rnd = new SecureRandom();
        while (salt.length() < 10) { // length of the SecureRandom string.
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        pass = salt.toString();

        System.out.println("Generate Pass: " + pass);
        return pass;
    }

    public static int calculatePer(int limit, int per) {
        int result = (limit * per) / 100;
        long num = 100 * Math.round(result / 100.0);
        return Long.valueOf(num).intValue();
    }

}
