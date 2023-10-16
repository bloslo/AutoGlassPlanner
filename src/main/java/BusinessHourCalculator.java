import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BusinessHourCalculator {
    private LocalTime defaultOpeningTime;
    private LocalTime defaultClosingTime;
    private Set<String> closedDays;
    private Set<WorkTime> businessHours;
    DateTimeFormatter formatter;

    public BusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime) {
        this.defaultOpeningTime = LocalTime.parse(defaultOpeningTime);
        this.defaultClosingTime = LocalTime.parse(defaultClosingTime);
        closedDays = new HashSet<>();
        businessHours = new HashSet<>();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    public boolean setOpeningHours(DayOfWeek weekDay, String openingTime, String closingTime) {
        boolean weekDayExists = businessHours.stream()
                .anyMatch(workTime -> workTime.getWeekDay() == weekDay);
        if (!weekDayExists) {
            return businessHours.add(new WorkTime(weekDay, openingTime, closingTime));
        }
        return false;
    }

    public boolean setOpeningHours(String date, String openingTime, String closingTime) {
        boolean dateExists = businessHours.stream()
                .anyMatch(workTime -> workTime.getDate() != null && workTime.getDate().equals(LocalDate.parse(date)));
        if (!dateExists) {
            return businessHours.add(new WorkTime(date, openingTime, closingTime));
        }

        return false;
    }

    public boolean setClosed(DayOfWeek weekDay) {
        return closedDays.add(weekDay.name());
    }

    public boolean setClosed(String date) {
        return closedDays.add(date);
    }

    public LocalDateTime calculateDeadline(long repairTimeInSeconds, String dateAcceptedForRepair) {
        LocalDateTime deliveryTime;
        LocalDateTime dateAccepted = LocalDateTime.parse(dateAcceptedForRepair, formatter);

        // Check if there are business hours for the specific date.
        WorkTime workTime = findNextWorkTime(dateAccepted.toLocalDate());

        if (dateAccepted.toLocalTime().isBefore(workTime.getOpeningTime())) {
            deliveryTime = LocalDateTime.of(dateAccepted.toLocalDate(),
                    workTime.getOpeningTime().plusSeconds(repairTimeInSeconds));
        } else {
            deliveryTime = dateAccepted.plusSeconds(repairTimeInSeconds);
        }

        LocalDate currentWorkDate = workTime.getDate() != null ? workTime.getDate() : dateAccepted.toLocalDate();
        while (deliveryTime.isAfter(LocalDateTime.of(currentWorkDate, workTime.getClosingTime()))) {
            LocalDate nextWorkingDate = findNextWorkingDay(deliveryTime.toLocalDate());
            WorkTime nextWorkTime = findNextWorkTime(nextWorkingDate);
            Duration remainingTime = Duration.between(workTime.getClosingTime(), deliveryTime.toLocalTime());

            workTime = nextWorkTime;
            currentWorkDate = nextWorkingDate;
            deliveryTime = LocalDateTime.of(nextWorkingDate,
                    nextWorkTime.getOpeningTime().plusSeconds(remainingTime.getSeconds()));
        }

        return deliveryTime;
    }

    public LocalDate findNextWorkingDay(LocalDate date) {
        LocalDate nextDate = date;
        do {
            nextDate = nextDate.plusDays(1);
            System.out.println(nextDate);
        } while (closedDays.contains(nextDate.toString()) || closedDays.contains(nextDate.getDayOfWeek().name()));

        return nextDate;
    }

    public WorkTime findNextWorkTime(LocalDate date) {
        return businessHours.stream()
                .filter(bh -> bh.getDate() != null && bh.getDate().equals(date))
                .findFirst()
                .orElseGet(() -> businessHours.stream()
                        .filter(bh -> bh.getWeekDay() == date.getDayOfWeek())
                        .findFirst()
                        .orElse(new WorkTime(date.getDayOfWeek(), defaultOpeningTime.toString(), defaultClosingTime.toString())));
    }
}
