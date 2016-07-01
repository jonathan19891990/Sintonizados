package util;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by InnovaCaicedo on 7/4/2016.
 */
public class ValidatorUtil {
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate given email with regular expression.
     *
     * @param email
     *            email for validation
     * @return true valid email, otherwise false
     */
    public static boolean validateEmail(String email) {
        if(Texto(email)){
            // Compiles the given regular expression into a pattern.
            Log.i("textoemail"," email "+email);
            Pattern pattern = Pattern.compile(PATTERN_EMAIL);
            Log.i("validarEmail","validar "+email);
            // Match the given input against this pattern
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } else
            return false;

    }

    public static boolean Texto(String texto)
    {
        Log.i("texto","valor texto "+texto);
        return texto!=null&& texto.trim().length()>=4;
    }

}
