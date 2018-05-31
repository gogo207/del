package com.delex.CardValidation;

import android.support.annotation.Nullable;
import android.view.KeyEvent;

/**
 * Created by ${Ali} on 8/18/2017.
 */

public class CardTextUtils
{
    private static int[] NON_DELETE_KEYS = {
            KeyEvent.KEYCODE_0,
            KeyEvent.KEYCODE_1,
            KeyEvent.KEYCODE_2,
            KeyEvent.KEYCODE_3,
            KeyEvent.KEYCODE_4,
            KeyEvent.KEYCODE_5,
            KeyEvent.KEYCODE_6,
            KeyEvent.KEYCODE_7,
            KeyEvent.KEYCODE_8,
            KeyEvent.KEYCODE_9,
            KeyEvent.KEYCODE_SLASH
    };

    /**
     * Util Array for converting bytes to a hex string.
     * {@url http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java}
     */
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Check to see if the input number has any of the given prefixes.
     *
     * @param number the number to test
     * @param prefixes the prefixes to test against
     * @return {@code true} if number begins with any of the input prefixes
     */
    public static boolean hasAnyPrefix(String number, String... prefixes) {
        if (number == null) {
            return false;
        }

        for (String prefix : prefixes) {
            if (number.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check to see whether the input string is a whole, positive number.
     *
     * @param value the input string to test
     * @return {@code true} if the input value consists entirely of integers
     */
    public static boolean isWholePositiveNumber(String value) {
        if (value == null) {
            return false;
        }

        // Refraining from using android's TextUtils in order to avoid
        // depending on another package.
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Swap {@code null} for blank text values.
     *
     * @param value an input string that may or may not be entirely whitespace
     * @return {@code null} if the string is entirely whitespace, or the original value if not
     */
    public static String nullIfBlank(String value) {
        if (isBlank(value)) {
            return null;
        }
        return value;
    }

    /**
     * A checker for whether or not the input value is entirely whitespace. This is slightly more
     * aggressive than the android TextUtils#isEmpty method, which only returns true for
     * {@code null} or {@code ""}.
     *
     * @param value a possibly blank input string value
     * @return {@code true} if and only if the value is all whitespace, {@code null}, or empty
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Converts a card number that may have spaces between the numbers into one without any spaces.
     * Note: method does not check that all characters are digits or spaces.
     *
     * @param cardNumberWithSpaces a card number, for instance "4242 4242 4242 4242"
     * @return the input number minus any spaces, for instance "4242424242424242".
     * Returns {@code null} if the input was {@code null} or all spaces.
     */
    @Nullable
    public static String removeSpacesAndHyphens(@Nullable String cardNumberWithSpaces) {
        if (isBlank(cardNumberWithSpaces)) {
            return null;
        }
        return cardNumberWithSpaces.replaceAll("\\s|-", "");
    }

    /**

     *
     * @param possibleCardType a String that might match a or be empty.
     * @return {@code null} if the input is blank, else the appropriate .
     */
    @Nullable
    public static String asCardBrand(@Nullable String possibleCardType) {
        if (isBlank(possibleCardType)) {
            return null;
        }

        if (CardSample.AMERICAN_EXPRESS.equalsIgnoreCase(possibleCardType)) {
            return CardSample.AMERICAN_EXPRESS;
        } else if (CardSample.MASTERCARD.equalsIgnoreCase(possibleCardType)) {
            return CardSample.MASTERCARD;
        } else if (CardSample.DINERS_CLUB.equalsIgnoreCase(possibleCardType)) {
            return CardSample.DINERS_CLUB;
        } else if (CardSample.DISCOVER.equalsIgnoreCase(possibleCardType)) {
            return CardSample.DISCOVER;
        } else if (CardSample.JCB.equalsIgnoreCase(possibleCardType)) {
            return CardSample.JCB;
        } else if (CardSample.VISA.equalsIgnoreCase(possibleCardType)) {
            return CardSample.VISA;
        } else {
            return CardSample.UNKNOWN;
        }
    }


}
