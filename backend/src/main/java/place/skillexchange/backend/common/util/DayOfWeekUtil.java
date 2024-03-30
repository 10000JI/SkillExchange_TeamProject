package place.skillexchange.backend.common.util;

import place.skillexchange.backend.talent.entity.DayOfWeek;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DayOfWeekUtil {

    public static Set<DayOfWeek> convertSelectedDaysToEnum(Set<String> selectedDays) {
        Set<DayOfWeek> convertedDays = new HashSet<>();
        for (String day : selectedDays) {
            convertedDays.add(DayOfWeek.fromString(day));
        }
        return convertedDays;
    }

    public static Set<String> convertSelectedDaysToString(Set<DayOfWeek> selectedDays) {
        Set<String> convertedDays = new HashSet<>();
        for (DayOfWeek day : selectedDays) {
            convertedDays.add(day.toString());
        }
        return convertedDays;
    }
}
