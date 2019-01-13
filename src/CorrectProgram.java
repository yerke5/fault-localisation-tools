import java.util.Calendar;
import java.util.HashMap;

public class CorrectProgram {
    public static HashMap<String, Boolean> getInfo(int year) {
        if(year < 0) {
            System.out.println("Invalid year");
            System.exit(1);
        }
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("Leap Year", false);
        map.put("In the Future", false);
        map.put("Winter olympics", false);
        map.put("Summer olympics", false);
        map.put("World cup", false);
        // Leap Year
        if(year % 4 == 0) {
            if( year % 100 == 0) {
                // year is divisible by 400, hence the year is a leap year
                if (year % 400 == 0)
                    map.replace("Leap Year", true);
                else
                    map.replace("Leap Year", false);
            } else
                map.replace("Leap Year", true);
        } else
            map.replace("Leap Year", false);

        // In the Future
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        if(year <= currYear) {
            map.replace("In the Future", false);
        } else {
            map.replace("In the Future", true);
        }

        // Winter olympics
        int sampleWOlympYear = 2022;
        if(Math.abs(year - sampleWOlympYear) % 4 == 0) {
            map.replace("Winter olympics", true);
        } else {
            map.replace("Winter olympics", false);
        }

        // Summer olympics
        int sampleSOlympYear = 2020;
        if(Math.abs(year - sampleSOlympYear) % 4 == 0) {
            map.replace("Summer olympics", true);
        } else {
            map.replace("Summer olympics", false);
        }

        // World cup
        if(!map.get("In the Future")) {
            if(year < 1930) {
                map.replace("World cup", false);
            } else {
                if(year == 1938 || year == 1950) {
                    map.replace("World cup", true);
                } else if((year - 1930) % 4 == 0) {
                    map.replace("World cup", true);
                } else {
                    map.replace("World cup", false);
                }
            }
        } else {
            if(map.get("Leap Year")) {
                map.replace("World cup", true);
            } else {
                map.replace("World cup", false);
            }
        }
        //System.out.println(map);
        return map;
    }
}
