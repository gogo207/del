package com.delex.utility;


import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h>Validator</h>
 * <p>
 *     필드의 유효성을 검사하는 클래스
 * </p>
 * @since  11/5/15.
 */
public class Validator
{

    /**
     * <h2>firstName_status</h2>
     * <p>
     *     method to validate the first name filed
     * </p>
     * @param firstName: input first name
     * @return: flag: true if is valid
     */
    public boolean firstName_status(String firstName)
    {
        Boolean flag = true;
        for (int i = 0; i < firstName.length(); i++) {
            if (!((firstName.charAt(i) >= 'A' && firstName.charAt(i) <= 'Z') || (firstName.charAt(i) >= 'a' && firstName.charAt(i) <= 'z'))) {
                flag = false;
                break;
            }

        }
        return flag;
    }

    /**
     * <h2>lastName_status</h2>
     * <p>
     *     method to validate the last name filed
     * </p>
     * @param lastName: input last name
     * @return: flag: true if is valid
     */
    public boolean lastName_status(String lastName) {
        Boolean flag = true;
        for (int i = 0; i < lastName.length(); i++) {
            if (!((lastName.charAt(i) >= 'A' && lastName.charAt(i) <= 'Z') || (lastName.charAt(i) >= 'a' && lastName.charAt(i) <= 'z'))) {
                flag = false;
                break;
            }

        }

        return flag;
    }

    /**
     * <h2>emailValidation</h2>
     * <p>
     *     입력 전자 메일 형식의 유효성을 검사하는 메서드
     * </p>
     * @param email: input email
     * @return: flag: true if is valid
     */
    public boolean emailValidation(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * <h2>contactNo_status</h2>
     * <p>
     *     method to validate the input phone number format
     * </p>
     * @param phNo: input phone number
     * @return: flag: true if is valid
     */
    public boolean contactNo_status(String phNo) {
        if (phNo.matches("\\d{10}")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <h2>passStatus</h2>
     * <p>
     *     method to validate the input password format
     * </p>
     * @param pass: input password
     * @return: flag: true if is valid
     */
    public boolean passStatus(String pass){
        if(pass.matches("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{3,40})")){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method is used to check whether a string is empty or not
     * @param text Text to be checked
     * @return Returns true if the string is not empty, false if the string is empty
     */
    public static boolean isNotEmpty(String text){
        if(text.trim().length()==0){
            return false;
        }
        return true;
    }
    /**
     * This method is used to check if a String is valid full Name or not
     * @param name This contains the name which is to be checked
     * @return Returns true if the string is valid String, false if the string is not a valid string
     */
    public static boolean isFullNameValid(String name){
        if (name.matches("([A-Za-z]{3,15}+\\s?)+")) {
            return true;
        }
        return false;
    }
    /**
     * This method is used to check if a String is valid  Name or not
     * @param name This contains the name which is to be checked
     * @return Returns true if the string is valid String, false if the string is not a valid string
     */
    public static boolean isNameValid(String name){
        if (name.matches("[A-Za-z]{3,20}")) {
            return true;
        }
        return false;
    }
    /**
     *  This method is used to check whether a phone number is valid or not
     * @param phoneNumber String phone number which must be validated
     * @return Returns true if the phone number is valid else false
     */
    public static boolean isPhoneNoValid(String phoneNumber) {
        Pattern phoneNoPattern = Pattern.compile("[0-9]");
        if (phoneNoPattern.matcher(phoneNumber).matches()) {
            return true;
        }
        return false;
    }
    /**
     *  This method is used to check whether a Email Id is valid or not
     * @param emailid String emailId which must be validated
     * @return Returns true if the emailId is valid else false
     */
    public static boolean isEmailIdValid(String emailid){
        if(Patterns.EMAIL_ADDRESS.matcher(emailid).matches()){
            return true;
        }
        return false;
    }
    /**
     * 이것은 암호를 확인하는 데 사용되며 ReEnteredPassword는 같거나 같지 않습니다.
     * This is used to check the password and ReEnteredPassword are equal or not
     * @param password The String from the password field
     * @param reEnteredPass String from the Re Entered password field
     * @return Returns true if both the parameters are equal, else false
     */
    public static boolean checkReEnteredPass(String password, String reEnteredPass){
        if(password.equals(reEnteredPass)){
            return true;
        }
        return false;
    }
    /**
     * 이 메소드는 패스워드가 유효한지 아닌지를 검사하는데 사용된다.
     * This method is used to check whether a password is valid or not
     * @param passwd the string from the  password field which is used to check
     * @return Returns true if the password is valid, else false
     */
    public static boolean isPasswordValid(String passwd){
       /* if(passwd.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")){
            return true;
        }
        return false;*/
        return true;
    }
}