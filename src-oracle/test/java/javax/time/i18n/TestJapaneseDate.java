package javax.time.i18n;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.time.CalendricalException;
import javax.time.calendar.Calendrical;
import javax.time.calendar.CalendricalProvider;
import javax.time.calendar.DateProvider;
import javax.time.calendar.DateTimeFieldRule;
import javax.time.calendar.IllegalCalendarFieldValueException;
import javax.time.calendar.LocalDate;
import javax.time.calendar.UnsupportedCalendarFieldException;
import javax.time.calendar.field.HourOfDay;
import javax.time.i18n.JapaneseChronology;
import javax.time.i18n.JapaneseDate;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestJapaneseDate {
    
    private JapaneseEra testEra = JapaneseEra.HEISEI;
    private int testYear = 21;
    private int testGregorianYear = 2009;
    private int testMonthOfYear = 3;
    private int testDayOfMonth = 3;
    private int testDayOfYear = 62;
    private boolean testLeapYear = false;
    private JapaneseDate testDate;

    @BeforeTest
    public void setUp() throws Exception {
        testDate = JapaneseDate.japaneseDate(testEra, testYear, testMonthOfYear, testDayOfMonth);
    }
    
    @Test
    public void testInterfaces() {
        assertTrue(testDate instanceof CalendricalProvider);
        assertTrue(testDate instanceof DateProvider);
        assertTrue(testDate instanceof Serializable);
        assertTrue(testDate instanceof Comparable);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(testDate);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
        assertEquals(ois.readObject(), testDate);
    }
    
    @Test
    public void testImmutable() {
        Class<JapaneseDate> cls = JapaneseDate.class;
        assertTrue(Modifier.isPublic(cls.getModifiers()));
        assertTrue(Modifier.isFinal(cls.getModifiers()));
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) == false) {
                assertTrue(Modifier.isPrivate(field.getModifiers()));
                assertTrue(Modifier.isFinal(field.getModifiers()));
            }
        }
    }

    @Test
    public void testJapaneseDateDateProvider() throws Exception {
        assertEquals(JapaneseDate.japaneseDate(testDate), testDate);
        assertJapaneseDate(testDate, testEra, testYear, testMonthOfYear, testDayOfMonth);
    }
    
    @Test(expectedExceptions=NullPointerException.class)
    public void testJapaneseDateDateProviderNull() throws Exception {
        JapaneseDate.japaneseDate(null);
    }

    @Test
    public void testJapaneseDateIntIntInt() throws Exception{
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth), testDate);
        assertJapaneseDate(testDate, testEra, testYear, testMonthOfYear, testDayOfMonth);
    }
    
    @Test
    public void testJapaneseDateIntIntIntInt() throws Exception{
        assertEquals(JapaneseDate.japaneseDate(testEra, testYear, testMonthOfYear, testDayOfMonth), testDate);
        assertJapaneseDate(testDate, testEra, testYear, testMonthOfYear, testDayOfMonth);
    }
    
    @Test
    public void testJapaneseDateInvalidYear() throws Exception{
        try {
            JapaneseDate.japaneseDate(10000, testMonthOfYear, testDayOfMonth);// Invalid year.
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }
    
    @Test
    public void testJapaneseDateInvalidMonth() throws Exception{
        try {
            JapaneseDate.japaneseDate(testYear, 13, testDayOfMonth);// Invalid month of year
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.monthOfYear());
        }
    }
    
    @Test
    public void testJapaneseDateInvalidDay() throws Exception{
        try {
            JapaneseDate.japaneseDate(testYear, testMonthOfYear, 40);// Invalid day of month.
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.dayOfMonth());
        }
    }

    @Test
    public void testGetChronology() {
        assertEquals(testDate.getChronology(), JapaneseChronology.INSTANCE);
    }
    
    @Test
    public void testGet() throws Exception {
        assertEquals(testDate.get(JapaneseChronology.INSTANCE.era()), testDate.getEra().getValue());
        assertEquals(testDate.get(JapaneseChronology.INSTANCE.yearOfEra()), testDate.getYearOfEra());
        assertEquals(testDate.get(JapaneseChronology.INSTANCE.monthOfYear()), testDate.getMonthOfYear());
        assertEquals(testDate.get(JapaneseChronology.INSTANCE.dayOfMonth()), testDate.getDayOfMonth());
        assertEquals(testDate.get(JapaneseChronology.INSTANCE.dayOfYear()), testDate.getDayOfYear());
        assertEquals(testDate.get(JapaneseChronology.INSTANCE.dayOfWeek()), testDate.getDayOfWeek());
    }
    
    @Test(expectedExceptions=UnsupportedCalendarFieldException.class)
    public void testGetUnsupported() throws Exception {
        testDate.get(HourOfDay.rule());
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void testGetNull() throws Exception {
        testDate.get((DateTimeFieldRule) null);
    }

    @Test
    public void testGetEra() {
        assertEquals(testDate.getEra(), testEra);
    }

    @Test
    public void testGetYearOfEra() {
        assertEquals(testDate.getYearOfEra(), testYear);
    }

    @Test
    public void testGetMonthOfYear() {
        assertEquals(testDate.getMonthOfYear(), testMonthOfYear);
    }

    @Test
    public void testGetDayOfMonth() {
        assertEquals(testDate.getDayOfMonth(), testDayOfMonth);
    }

    @Test
    public void testGetDayOfYear() {
        assertEquals(testDate.getDayOfYear(), testDayOfYear);
    }

    //-----------------------------------------------------------------------
    // getDayOfWeek()
    //-----------------------------------------------------------------------
    @Test
    public void testGetDayOfWeek() {
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, 2).getDayOfWeek(), 1);
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, 3).getDayOfWeek(), 2);
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, 4).getDayOfWeek(), 3);
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, 5).getDayOfWeek(), 4);
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, 6).getDayOfWeek(), 5);
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, 7).getDayOfWeek(), 6);
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, 8).getDayOfWeek(), 7);
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, 9).getDayOfWeek(), 1);
    }
    
    @Test
    public void testGetDayOfWeekCrossCheck() throws Exception {
        JapaneseDate date = JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth);
        assertEquals(date.getDayOfWeek(), date.toLocalDate().getDayOfWeek().getValue());
    }

    @Test
    public void testIsLeapYear() {
        assertEquals(testDate.isLeapYear(), testLeapYear);
    }

    //-----------------------------------------------------------------------
    // withYearOfEra()
    //-----------------------------------------------------------------------
    @Test
    public void testWithYearOfEra() {
        JapaneseDate date = testDate.withYearOfEra(2010);
        assertEquals(date, JapaneseDate.japaneseDate(2010, testMonthOfYear, testDayOfMonth));
    }
    
    @Test
    public void testWithYearOfEraInvalidTooSmall() throws Exception {
        try {
            testDate.withYearOfEra(-1);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }
    
    @Test
    public void testWithYearOfEraInvalidTooBig() throws Exception {
        try {
            testDate.withYearOfEra(10000);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // withYear()
    //-----------------------------------------------------------------------

    @Test
    public void testWithYear() {
        JapaneseDate date = testDate.withYear(JapaneseEra.SHOWA, 48);
        assertEquals(date, JapaneseDate.japaneseDate(JapaneseEra.SHOWA, 48, testMonthOfYear, testDayOfMonth));
    }
    
    @Test
    public void testWithYearInvalidTooSmall() throws Exception {
        try {
            testDate.withYear(testEra, -1);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }
    
    @Test
    public void testWithYearInvalidTooBig() throws Exception {
        try {
            testDate.withYear(testEra, 10000);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // withMonthOfYear()
    //-----------------------------------------------------------------------
    @Test
    public void testWithMonthOfYear() {
        JapaneseDate date = testDate.withMonthOfYear(4);
        assertEquals(date, JapaneseDate.japaneseDate(testYear, 4, testDayOfMonth));
    }
    
    @Test
    public void testWithMonthOfYearInvalidTooSmall() throws Exception {
        try {
            testDate.withMonthOfYear(-1);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.monthOfYear());
        }
    }
    
    @Test
    public void testWithMonthOfYearInvalidTooBig() throws Exception {
        try {
            testDate.withMonthOfYear(13);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.monthOfYear());
        }
    }

    //-----------------------------------------------------------------------
    // withDayOfMonth()
    //-----------------------------------------------------------------------
    @Test
    public void testWithDayOfMonth() {
        JapaneseDate date = testDate.withDayOfMonth(4);
        assertEquals(date, JapaneseDate.japaneseDate(testYear, testMonthOfYear, 4));
    }
    
    @Test
    public void testWithDayOfMonthInvalidTooSmall() throws Exception {
        try {
            testDate.withDayOfMonth(0);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.dayOfMonth());
        }
    }
    
    @Test
    public void testWithDayOfMonthInvalidTooBig() throws Exception {
        try {
            testDate.withDayOfMonth(32);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.dayOfMonth());
        }
    }
    
    //-----------------------------------------------------------------------
    // withDayOfYear()
    //-----------------------------------------------------------------------
    @Test
    public void testWithDayOfYear() {
        JapaneseDate date = testDate.withDayOfYear(15);
        assertEquals(date, JapaneseDate.japaneseDate(testYear, 1, 15));
    }
    
    @Test
    public void testWithDayOfYearInvalidTooSmall() throws Exception {
        try {
            testDate.withDayOfYear(0);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.dayOfYear());
        }
    }
    
    @Test
    public void testWithDayOfYearInvalidTooBig() throws Exception {
        try {
            testDate.withDayOfYear(367);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.dayOfYear());
        }
    }
    
    //-----------------------------------------------------------------------
    // plusYears()
    //-----------------------------------------------------------------------
    @Test
    public void testPlusYears() {
        assertEquals(testDate.plusYears(10), JapaneseDate.japaneseDate(testYear+10, testMonthOfYear, testDayOfMonth));
    }
    
    @Test (expectedExceptions=CalendricalException.class)
    public void testPlusYearsInvalidTooBig() {
        testDate.plusYears(9999);
    }

    //-----------------------------------------------------------------------
    // plusMonths()
    //-----------------------------------------------------------------------
    @Test
    public void testPlusMonths() {
        assertEquals(testDate.plusMonths(5), JapaneseDate.japaneseDate(testYear, testMonthOfYear+5, testDayOfMonth));
    }
    
    @Test
    public void testPlusMonthsOverflow() throws Exception {
        try {
            testDate.plusMonths(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // plusDays()
    //-----------------------------------------------------------------------
    @Test
    public void testPlusDays() {
        assertEquals(testDate.plusDays(2), JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth+2));
    }
    
    @Test
    public void testPlusDaysOverflow() throws Exception {
        try {
            testDate.plusDays(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // plusWeeks()
    //-----------------------------------------------------------------------
    @Test
    public void testPlusWeeks() {
        assertEquals(testDate.plusWeeks(2), JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth+(2*7)));
    }
    
    @Test
    public void testPlusWeeksOverflow() throws Exception {
        try {
            testDate.plusWeeks(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // minusYears()
    //-----------------------------------------------------------------------
    @Test
    public void testMinusYears() {
        assertEquals(testDate.minusYears(10), JapaneseDate.japaneseDate(testYear-10, testMonthOfYear, testDayOfMonth));
    }
    
    @Test (expectedExceptions=CalendricalException.class)
    public void testMinusYearsInvalidTooSmall() {
        testDate.minusYears(20000);
    }

    //-----------------------------------------------------------------------
    // minusMonths()
    //-----------------------------------------------------------------------
    @Test
    public void testMinusMonths() {
        assertEquals(testDate.minusMonths(1), JapaneseDate.japaneseDate(testYear, testMonthOfYear-1, testDayOfMonth));
    }
    
    @Test
    public void testMinusMonthsOverflow() throws Exception {
        try {
            testDate.minusMonths(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // minusDays()
    //-----------------------------------------------------------------------
    @Test
    public void testMinusDays() {
        assertEquals(testDate.minusDays(2), JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth-2));
    }
    
    @Test
    public void testMinusDaysOverflow() throws Exception {
        try {
            testDate.minusDays(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }
    
    //-----------------------------------------------------------------------
    // minusWeeks()
    //-----------------------------------------------------------------------
    @Test
    public void testMinusWeeks() {
        assertEquals(testDate.minusWeeks(2), JapaneseDate.japaneseDate(testYear, testMonthOfYear-1, 28+testDayOfMonth-(2*7)));
    }
    
    @Test
    public void testMinusWeeksOverflow() throws Exception {
        try {
            testDate.minusWeeks(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), JapaneseChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // toLocalDate()
    //-----------------------------------------------------------------------
    @Test
    public void testToLocalDate() {
        assertEquals(JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth).toLocalDate(),
                LocalDate.date(testGregorianYear, testMonthOfYear, testDayOfMonth));
    }

    //-----------------------------------------------------------------------
    // toCalendrical()
    //-----------------------------------------------------------------------
    @Test
    public void testToCalendrical() {
        Calendrical test = JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth).toCalendrical();
        assertEquals(test, new Calendrical(LocalDate.date(testGregorianYear, testMonthOfYear, testDayOfMonth), null, null, null));
    }

    //-----------------------------------------------------------------------
    // compareTo(), isAfter(), isBefore(), and equals()
    //-----------------------------------------------------------------------   
    @Test
    public void testCompareTo() throws Exception {
        doTestComparisons(
            JapaneseDate.japaneseDate(1, 1, 1),
            JapaneseDate.japaneseDate(1, 1, 2),
            JapaneseDate.japaneseDate(1, 1, 31),
            JapaneseDate.japaneseDate(1, 2, 1),
            JapaneseDate.japaneseDate(1, 2, 28),
            JapaneseDate.japaneseDate(1, 12, 31),
            JapaneseDate.japaneseDate(2, 1, 1),
            JapaneseDate.japaneseDate(2, 12, 31),
            JapaneseDate.japaneseDate(3, 1, 1),
            JapaneseDate.japaneseDate(3, 12, 31),
            JapaneseDate.japaneseDate(9999, 1, 1),
            JapaneseDate.japaneseDate(9999, 12, 31)
        );
    }

    void doTestComparisons(JapaneseDate... dates) {
        for (int i = 0; i < dates.length; i++) {
            JapaneseDate a = dates[i];
            for (int j = 0; j < dates.length; j++) {
                JapaneseDate b = dates[j];
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0, a + " <=> " + b);
                    assertEquals(a.isBefore(b), true, a + " <=> " + b);
                    assertEquals(a.isAfter(b), false, a + " <=> " + b);
                    assertEquals(a.equals(b), false, a + " <=> " + b);
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0, a + " <=> " + b);
                    assertEquals(a.isBefore(b), false, a + " <=> " + b);
                    assertEquals(a.isAfter(b), true, a + " <=> " + b);
                    assertEquals(a.equals(b), false, a + " <=> " + b);
                } else {
                    assertEquals(a.compareTo(b), 0, a + " <=> " + b);
                    assertEquals(a.isBefore(b), false, a + " <=> " + b);
                    assertEquals(a.isAfter(b), false, a + " <=> " + b);
                    assertEquals(a.equals(b), true, a + " <=> " + b);
                }
            }
        }
    }
    
    @Test(expectedExceptions=NullPointerException.class)
    public void testCompareToObjectNull() throws Exception {
        testDate.compareTo(null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void testIsBeforeObjectNull() throws Exception {
        testDate.isBefore(null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void testIsAfterObjectNull() throws Exception {
        testDate.isAfter(null);
    }

    @Test(expectedExceptions=ClassCastException.class)
    public void testCompareToNonDate() throws Exception {
       Comparable c = testDate;
       c.compareTo(new Object());
    }

    //-----------------------------------------------------------------------
    // equals()
    //-----------------------------------------------------------------------
    @Test
    public void testEqualsEaual() throws Exception {
        JapaneseDate a = JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth);
        JapaneseDate b = JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth);
        assertEquals(a.equals(b), true);
        assertEquals(b.equals(a), true);
        assertEquals(a.equals(a), true);
        assertEquals(b.equals(b), true);
    }
    
    @Test
    public void testEqualsNotEqualDay() throws Exception {
        JapaneseDate a = JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth);
        JapaneseDate b = JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth+1);
        assertEquals(a.equals(b), false);
        assertEquals(b.equals(a), false);
        assertEquals(a.equals(a), true);
        assertEquals(b.equals(b), true);
    }

    @Test
    public void testEqualsNotEqualMonth() throws Exception {
        JapaneseDate a = JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth);
        JapaneseDate b = JapaneseDate.japaneseDate(testYear, testMonthOfYear+1, testDayOfMonth);
        assertEquals(a.equals(b), false);
        assertEquals(b.equals(a), false);
        assertEquals(a.equals(a), true);
        assertEquals(b.equals(b), true);
    }

    @Test
    public void testEqualsNotEqualYear() throws Exception {
        JapaneseDate a = JapaneseDate.japaneseDate(testYear, testMonthOfYear, testDayOfMonth);
        JapaneseDate b = JapaneseDate.japaneseDate(testYear+1, testMonthOfYear, testDayOfMonth);
        assertEquals(a.equals(b), false);
        assertEquals(b.equals(a), false);
        assertEquals(a.equals(a), true);
        assertEquals(b.equals(b), true);
    }

    @Test
    public void testEqualsItselfTrue() throws Exception {
        assertEquals(testDate.equals(testDate), true);
    }

    @Test
    public void testEqualsStringFalse() throws Exception {
        assertEquals(testDate.equals("22-07-15"), false);
    }
    
    //-----------------------------------------------------------------------
    // hashCode()
    //-----------------------------------------------------------------------

    @Test
    public void testHashCode() throws Exception {
        JapaneseDate a = JapaneseDate.japaneseDate(1, 1, 1);
        JapaneseDate b = JapaneseDate.japaneseDate(1, 1, 1);
        assertEquals(a.hashCode(), a.hashCode());
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(b.hashCode(), b.hashCode());
    }
    
    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @Test
    public void testToString() {
        String expected = "HEISEI 21-03-03 (Japanese)";
        String actual = testDate.toString();
        assertEquals(expected, actual);
    }
    
    private void assertJapaneseDate(JapaneseDate test, JapaneseEra era, int year, int month, int day) throws Exception {
        assertEquals(test.getEra(), era);
        assertEquals(test.getYearOfEra(), year);
        assertEquals(test.getMonthOfYear(), month);
        assertEquals(test.getDayOfMonth(), day);
        assertEquals(test.isLeapYear(), isLeapYear(era.getValue(), year));
    }
    
    private boolean isLeapYear(int era, int year) {
        int[] gEraYear = getGregorianEraYearFromLocalEraYear(era, year);
        era = gEraYear[0];
        year = gEraYear[1];
        
        if (era == 0) {
            year = 1 - year;
        } 
        return ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)));
    }
    
    private int[] getGregorianEraYearFromLocalEraYear(int era, int year) {

        int eraInfo[] = new int[2];

        if (era == 0) {
            eraInfo[0] = 0;
            eraInfo[1] = year;
            return eraInfo;
        }

        int offSet = 0;
        int eraCnt = 0;

        for (int i = 0; i < ERA_DATA.length; i = i + ERA_ARRAY_SIZE) {
            int targetEra = TOTAL_ERA_SIZE - eraCnt - 1;
            if (targetEra == era) {
                offSet = ERA_DATA[i + PISITION_YEAR] - 1;
                year += offSet;
                eraInfo[0] = 1;
                eraInfo[1] = year;
                return eraInfo;
            }
            eraCnt++;
        }
        eraInfo[0] = 1;
        eraInfo[1] = year;
        return eraInfo;
    }
    
    private static final int[] ERA_DATA = {
        1989, 1, 8, 6, // HEISEI
        1926, 12, 25, 5, // SHOWA
        1912, 7, 30, 4, // TAISHO
        1868, 9, 8, 3, // MEIJI
        1865, 4, 7, 2, // KEIO
        // ignore other eras.
        1, 1, 1, 1, // AD
        0, 1, 1, 0, // BC
        };

    private static final int ERA_ARRAY_SIZE = 4;
    private static final int PISITION_YEAR = 0;
    private static final int TOTAL_ERA_SIZE = ERA_DATA.length / ERA_ARRAY_SIZE;

}
