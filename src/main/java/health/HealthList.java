package health;

import storage.LogFile;
import utility.CustomExceptions;
import utility.HealthConstant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents the list of BMI objects stored.
 */
public class HealthList extends ArrayList<Health> {
    static LogFile logFile = LogFile.getInstance();
    private static final ArrayList<Bmi> bmis = new ArrayList<>();
    private static final ArrayList<Period> periods = new ArrayList<>();
    private static final ArrayList<Appointment> appointments = new ArrayList<>();

    //@@author j013n3
    /**
     * Adds a Bmi to the list of Bmis whenever addBmi is called.
     *
     * @param bmi Bmi object.
     * @throws AssertionError If Bmi object is null.
     */
    public static void addBmi(Bmi bmi) {
        assert bmi != null : HealthConstant.BMI_CANNOT_BE_NULL;
        bmis.add(bmi);
    }

    //@@author syj02
    /**
     * Prints the most recently added Bmi object from bmis list.
     *
     * @throws AssertionError If bmis list is empty.
     */
    public static void showCurrentBmi() {
        assert !bmis.isEmpty() : HealthConstant.BMI_LIST_EMPTY;
        int currentIndex = bmis.size();
        System.out.println(bmis.get(currentIndex - 1));
    }

    /**
     * Prints all the BMI entries recorded.
     *
     * @throws AssertionError If bmis list is empty
     */
    public static void showBmiHistory() {
        assert !bmis.isEmpty() : HealthConstant.BMI_LIST_EMPTY;
        for (Bmi bmi : bmis) {
            System.out.println(bmi);
        }
    }

    /**
     * Adds a period to the ArrayList of periods.
     *
     * @param period Period object to be added
     * @throws AssertionError If period object is null
     */
    public static void addPeriod(Period period) {
        assert period != null : HealthConstant.PERIOD_CANNOT_BE_NULL;
        if (!periods.isEmpty()) {
            Period previousPeriod = periods.get(periods.size() - 1);
            previousPeriod.setCycleLength(period.getStartDate());
        }
        periods.add(period);
    }

    /**
     * Prints the latest period object added.
     * @throws AssertionError If periods list is empty
     */
    public static void showLatestPeriod() {
        assert !periods.isEmpty() : HealthConstant.PERIOD_LIST_EMPTY;
        int currentIndex = periods.size();
        System.out.println(periods.get(currentIndex - 1));
    }

    //@@author j013n3
    /**
     * Prints all Period entries tracked.
     * @throws AssertionError If periods list is empty
     */
    public static void showPeriodHistory() {
        assert !periods.isEmpty() : HealthConstant.PERIOD_LIST_EMPTY;
        for (Period period : periods) {
            System.out.println(period);
        }
    }

    /**
     * Retrieves the latest Period object from the periods list.
     *
     * @return The latest Period object, or null if the list is empty.
     */
    public static Period getLatestCycle() {
        if (periods.isEmpty()) {
            return null;
        }
        return periods.get(periods.size() - 1);
    }


    /**
     * Retrieves the number of periods recorded.
     *
     * @return The number of periods recorded.
     */
    public static int getPeriodSize() {
        return periods.size();
    }

    /**
     * Gets the Period object at the specified index.
     *
     * @param index The index of the Period object.
     * @return The Period object at the specified index, or null if the index is out of bounds.
     */
    public static Period getPeriod(int index) {
        if (index < 0 || index >= periods.size()) {
            return null;
        }
        return periods.get(index);
    }

    /**
     * Predicts the start date of the next period based on the average cycle length of the last three cycles.
     *
     * @return The predicted start date of the next period, or null if there are no periods recorded.
     */
    public static LocalDate predictNextPeriodStartDate() {
        if (periods.isEmpty()) {
            return null;
        }

        Period latestPeriod = periods.get(periods.size() - 1);
        return latestPeriod.nextCyclePrediction();
    }

    //@@l5_z
    /**
     * Clears the Bmis and Periods array lists.
     * @throws AssertionError If periods and bmis lists are not empty
     */
    public static void clearBmisAndPeriods() {
        periods.clear();
        bmis.clear();
        assert bmis.isEmpty() : ErrorConstant.BMI_LIST_UNCLEARED_ERROR;
        assert periods.isEmpty() : ErrorConstant.PERIOD_LIST_UNCLEARED_ERROR;
    }
    //@@author rouvinerh


    /**
     * Retrieves size of periods list.
     *
     * @return Size of periods list.
     */
    public static int getPeriodsSize() {
        return periods.size();
    }

    /**
     * Retrieves size of bmis list.
     *
     * @return Size of bmis list.
     */
    public static int getBmisSize() {
        return bmis.size();
    }
    /**
     * Deletes Bmi object based on index.
     * @param index Index of the Bmi object to be deleted.
     */
    public static void deleteBmi(int index) throws CustomExceptions.OutOfBounds {
        assert !bmis.isEmpty() : ErrorConstant.EMPTY_BMI_LIST_ERROR;
        if (index < 0 || index >= bmis.size()) {
            throw new CustomExceptions.OutOfBounds(ErrorConstant.INVALID_INDEX_DELETE_ERROR);
        }
        Bmi deletedBmi = bmis.get(index);
        System.out.println(String.format(HealthConstant.LOG_DELETE_BMI_FORMAT,
                        deletedBmi.bmiValue,
                        deletedBmi.date));
        bmis.remove(index);
        LogFile.writeLog(HealthConstant.BMI_REMOVED_MESSAGE_PREFIX + index, false);
    }

    /**
     * Deletes Bmi object based on index.
     * @param index Index of the Bmi object to be deleted.
     */
    public static void deletePeriod(int index) throws CustomExceptions.OutOfBounds {
        assert !periods.isEmpty() : ErrorConstant.EMPTY_PERIOD_LIST_ERROR;
        if (index < 0 || index >= periods.size()) {
            throw new CustomExceptions.OutOfBounds(ErrorConstant.INVALID_INDEX_DELETE_ERROR);
        }
        Period deletedPeriod = periods.get(index);
        System.out.println(String.format(HealthConstant.LOG_DELETE_PERIOD_FORMAT,
                deletedPeriod.getStartDate(),
                deletedPeriod.getEndDate()));
        periods.remove(index);
        LogFile.writeLog(HealthConstant.PERIOD_REMOVED_MESSAGE_PREFIX + index, false);
    }

    //@@author syj_02
    public static void addAppointment(Appointment appointment) {
        assert appointment != null : ErrorConstant.NULL_APPOINTMENT_ERROR;
        appointments.add(appointment);
        appointments.sort(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getTime));
    }

    public static void deleteAppointment(int index) throws CustomExceptions.OutOfBounds {
        assert !appointments.isEmpty() : ErrorConstant.EMPTY_APPOINTMENT_LIST_ERROR;
        if (index < 1 || index > appointments.size()) {
            throw new CustomExceptions.OutOfBounds(ErrorConstant.INVALID_INDEX_DELETE_ERROR);
        }
        index -= 1;
        Appointment deletedAppointment = appointments.get(index);
        System.out.println(String.format(HealthConstant.LOG_DELETE_APPOINTMENT_FORMAT,
                deletedAppointment.date,
                deletedAppointment.time,
                deletedAppointment.description));
        appointments.remove(index);
        LogFile.writeLog(HealthConstant.APPOINTMENT_REMOVED_MESSAGE_PREFIX + index, false);
        showAppointmentList();
    }

    public static void showAppointmentList() {
        assert !appointments.isEmpty() : ErrorConstant.EMPTY_APPOINTMENT_LIST_ERROR;
        int index = 1;
        for (Appointment appointment: appointments) {
            System.out.print(index + ". ");
            System.out.println(appointment);
            index += 1;
        }
    }
    public static void clearAppointments() {
        appointments.clear();
        assert appointments.isEmpty() : ErrorConstant.APPOINTMENT_LIST_UNCLEARED_ERROR;
    }
}
