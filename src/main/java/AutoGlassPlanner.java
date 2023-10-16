import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AutoGlassPlanner {

    public static void main(String[] args) {
        System.out.println(DayOfWeek.valueOf("FRIDAY"));
        long remainingTimeInSeconds = Duration.between(LocalTime.parse("16:00"),
                LocalTime.parse("18:00")).getSeconds();
        System.out.println(LocalTime.parse("09:00").plusSeconds(remainingTimeInSeconds).toString());
        BusinessHourCalculator businessHourCalculator = new BusinessHourCalculator("09:00", "15:00");
        businessHourCalculator.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHourCalculator.setOpeningHours("2013-12-24", "08:00", "13:00");
        businessHourCalculator.setClosed(DayOfWeek.SUNDAY);
        businessHourCalculator.setClosed(DayOfWeek.WEDNESDAY);
        businessHourCalculator.setClosed("2013-12-26");

        LocalDateTime deliveryDate = businessHourCalculator.calculateDeadline(2*60*60,
                "2013-09-12 09:10");
        System.out.println("Delivery date: " + deliveryDate);

        deliveryDate = businessHourCalculator.calculateDeadline(15*60, "2013-09-10 14:48");
        System.out.println("Delivery date: " + deliveryDate);

        deliveryDate = businessHourCalculator.calculateDeadline(7*60*60, "2013-12-24 06:45");
        System.out.println("Delivery date: " + deliveryDate);

        deliveryDate = businessHourCalculator.calculateDeadline(7*60*60, "2013-12-24 14:45");
        System.out.println("Delivery date: " + deliveryDate);
    }
}
