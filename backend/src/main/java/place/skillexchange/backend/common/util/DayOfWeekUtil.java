package place.skillexchange.backend.common.util;

import place.skillexchange.backend.talent.entity.DayOfWeek;

import java.util.*;

public class DayOfWeekUtil {

    public static Set<DayOfWeek> convertSelectedDaysToEnum(Set<String> selectedDays) {
        Set<DayOfWeek> convertedDays = new TreeSet<>();
        for (String day : selectedDays) {
            convertedDays.add(DayOfWeek.fromString(day));
        }
        return convertedDays;
    }

    public static Set<String> convertSelectedDaysToString(Set<DayOfWeek> selectedDays) {
        // 요일을 순서대로 정렬하기 위한 배열
        String[] daysOfWeekInOrder = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

        // 정렬된 문자열 집합 생성
        Set<String> sortedDays = new LinkedHashSet<>();
        for (String  day : daysOfWeekInOrder) {
            if (selectedDays.contains(DayOfWeek.valueOf(day))) {
                sortedDays.add(day);
            }
        }
        return sortedDays;
    }
}
