package place.skillexchange.backend.common.util;

import place.skillexchange.backend.talent.entity.DayOfWeek;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeekUtil {

    public static List<DayOfWeek> convertSelectedDaysToEnum(List<String> selectedDays) {
        List<DayOfWeek> convertedDays = new ArrayList<>();
        for (String day : selectedDays) {
            convertedDays.add(DayOfWeek.fromString(day));
        }
        return convertedDays;
    }

    public static List<String> convertSelectedDaysToString(List<DayOfWeek> selectedDays) {
        List<String> convertedDays = new ArrayList<>();
        for (DayOfWeek day : selectedDays) {
            convertedDays.add(day.toString());
        }
        return convertedDays;
    }
}
