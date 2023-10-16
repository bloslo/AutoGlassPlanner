import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class WorkTime {
    private DayOfWeek weekDay;
    private LocalDate date;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public WorkTime(DayOfWeek weekDay, String openingTime, String closingTime) {
        this.weekDay = weekDay;
        this.date = null;
        this.openingTime = LocalTime.parse(openingTime);
        this.closingTime = LocalTime.parse(closingTime);
    }

    public WorkTime(String date, String openingTime, String closingTime) {
        this.date = LocalDate.parse(date);
        this.weekDay = null;
        this.openingTime = LocalTime.parse(openingTime);
        this.closingTime = LocalTime.parse(closingTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkTime workTime = (WorkTime) o;
        return weekDay == workTime.weekDay && Objects.equals(date, workTime.date) && Objects.equals(openingTime, workTime.openingTime) && Objects.equals(closingTime, workTime.closingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekDay, date, openingTime, closingTime);
    }

    public DayOfWeek getWeekDay() {
        return weekDay;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }
}
