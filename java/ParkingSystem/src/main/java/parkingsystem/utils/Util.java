package parkingsystem.utils;

import java.util.ArrayList;

public class Util {
    public static ArrayList<Integer> createSpotIds(int startId, int numSpots) {
        ArrayList<Integer> spotIds = new ArrayList<>();
        for(Integer i = startId; i <= startId + numSpots; i++) {
            spotIds.add(i);
        }
        return spotIds;
    }
}
