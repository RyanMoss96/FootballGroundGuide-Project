package uk.co.ryanmoss.footballgroundguide_android;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by ryanmoss on 16/04/2017.
 */

public class EmailValidator {
    public final static boolean isValidEmail(String target) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(target).matches();
    }
}
