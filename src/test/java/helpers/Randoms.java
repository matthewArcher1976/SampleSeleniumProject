package helpers;

import java.util.Random;

public class Randoms {
	//return a random string of the given length
	public static String getRandomString(int i) {
        String RANDCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder randomString = new StringBuilder();
        Random rnd = new Random();
        while (randomString.length() < i) { // length of the random string.
            int index = (int) (rnd.nextFloat() * RANDCHARS.length());
            randomString.append(RANDCHARS.charAt(index));
        }
		return randomString.toString();
    }
	
	//Get a day of the month
	public static String getRandomDay() {
	    Random rnd = new Random();
	    int randomNum = rnd.nextInt(10, 28) + 1;
	    return Integer.toString(randomNum);
	}
	//convert to 01, 02 format
	public static String formatDay(String day) {
	    int dayNum = Integer.parseInt(day);
	    return String.format("%02d", dayNum);
	}
	//Pick a month
	public static String getRandomMonth() {
	    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	    Random rnd = new Random();
	    int index = rnd.nextInt(months.length);
	    return months[index];
	}
	//Pick a year
	public static String getRandomYear() {
	    Random rnd = new Random();
	    int year = rnd.nextInt(105) + 1901;
	    return Integer.toString(year);
	}

	
}
