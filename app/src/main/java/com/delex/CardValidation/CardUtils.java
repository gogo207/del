package com.delex.CardValidation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <h>CardUtils</h>
 * Created by ${Ali} on 8/18/2017.
 */

public class CardUtils
{
    public static final int LENGTH_COMMON_CARD = 16;
    public static final int LENGTH_AMERICAN_EXPRESS = 15;
    public static final int LENGTH_DINERS_CLUB = 14;

    public static final int CVC_LENGTH_COMMON = 3;
    public static final int CVC_LENGTH_AMEX = 4;

    public static boolean isValidCardNumber(@Nullable String cardNumber) {
        String normalizedNumber = CardTextUtils.removeSpacesAndHyphens(cardNumber);
        return isValidLuhnNumber(normalizedNumber) && isValidCardLength(normalizedNumber);
    }

    /**
     * Checks the input string to see whether or not it is a valid Luhn number.
     *
     * @param cardNumber a String that may or may not represent a valid Luhn number
     * @return {@code true} if and only if the input value is a valid Luhn number
     */
    private static boolean isValidLuhnNumber(@Nullable String cardNumber) {
        if (cardNumber == null) {
            return false;
        }

        boolean isOdd = true;
        int sum = 0;

        for (int index = cardNumber.length() - 1; index >= 0; index--) {
            char c = cardNumber.charAt(index);
            if (!Character.isDigit(c)) {
                return false;
            }

            int digitInteger = Character.getNumericValue(c);
            isOdd = !isOdd;

            if (isOdd) {
                digitInteger *= 2;
            }

            if (digitInteger > 9) {
                digitInteger -= 9;
            }

            sum += digitInteger;
        }

        return sum % 10 == 0;
    }

    /**
     * Checks to see whether the input number is of the correct length, after determining its brand.
     * This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @return {@code true} if the card number is of known type and the correct length
     */
    private static boolean isValidCardLength(@Nullable String cardNumber) {
        if (cardNumber == null) {
            return false;
        }

        return isValidCardLength(cardNumber, getPossibleCardType(cardNumber, false));
    }

    /**
     * Checks to see whether the input number is of the correct length, given the assumed brand of
     * the card. This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @return {@code true} if the card number is the correct length for the assumed brand
     */
    private static boolean isValidCardLength(
            @Nullable String cardNumber, String cardBrand) {
        if(cardNumber == null || CardSample.UNKNOWN.equals(cardBrand)) {
            return false;
        }

        int length = cardNumber.length();
        if (cardBrand.equals(CardSample.AMERICAN_EXPRESS)) {
            return length == LENGTH_AMERICAN_EXPRESS;
        } else if (cardBrand.equals(CardSample.DINERS_CLUB)) {
            return length == LENGTH_DINERS_CLUB;
        } else {
            return length == LENGTH_COMMON_CARD;
        }
    }



    @NonNull

    public static String getPossibleCardType(@Nullable String cardNumber,
                                              boolean shouldNormalize) {
        if (CardTextUtils.isBlank(cardNumber)) {
            return CardSample.UNKNOWN;
        }

        String spacelessCardNumber = cardNumber;
        if (shouldNormalize) {
            spacelessCardNumber = CardTextUtils.removeSpacesAndHyphens(cardNumber);
        }

        if (CardTextUtils.hasAnyPrefix(spacelessCardNumber, CardSample.PREFIXES_AMERICAN_EXPRESS)) {
            return CardSample.AMERICAN_EXPRESS;
        } else if (CardTextUtils.hasAnyPrefix(spacelessCardNumber, CardSample.PREFIXES_DISCOVER)) {
            return CardSample.DISCOVER;
        } else if (CardTextUtils.hasAnyPrefix(spacelessCardNumber, CardSample.PREFIXES_JCB)) {
            return CardSample.JCB;
        } else if (CardTextUtils.hasAnyPrefix(spacelessCardNumber, CardSample.PREFIXES_DINERS_CLUB)) {
            return CardSample.DINERS_CLUB;
        } else if (CardTextUtils.hasAnyPrefix(spacelessCardNumber, CardSample.PREFIXES_VISA)) {
            return CardSample.VISA;
        } else if (CardTextUtils.hasAnyPrefix(spacelessCardNumber, CardSample.PREFIXES_MASTERCARD)) {
            return CardSample.MASTERCARD;
        } else {
            return CardSample.UNKNOWN;
        }
    }

}
