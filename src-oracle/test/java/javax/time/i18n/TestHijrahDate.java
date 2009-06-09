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
import javax.time.i18n.HijrahChronology;
import javax.time.i18n.HijrahDate;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestHijrahDate {
    
    private HijrahEra testEra = HijrahEra.HIJRAH;
    private int testYear = 1430;
    private int testGregorianYear = 2009;
    private int testGregorianMonthOfYear = 3;
    private int testGregorianDayOfMonth = 2;
    private int testMonthOfYear = 3;
    private int testDayOfMonth = 5;
    private int testDayOfYear = 64;
    private boolean testLeapYear = false;
    private HijrahDate testDate;

    @BeforeTest
    public void setUp() throws Exception {
        testDate = HijrahDate.hijrahDate(testEra, testYear, testMonthOfYear, testDayOfMonth);
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
        Class<HijrahDate> cls = HijrahDate.class;
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
    public void testHijrahDateDateProvider() throws Exception {
        assertEquals(HijrahDate.hijrahDate(testDate), testDate);
        assertHijrahDate(testDate, testEra, testYear, testMonthOfYear, testDayOfMonth);
    }
    
    @Test(expectedExceptions=NullPointerException.class)
    public void testHijrahDateDateProviderNull() throws Exception {
        HijrahDate.hijrahDate(null);
    }

    @Test
    public void testHijrahDateIntIntInt() throws Exception{
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth), testDate);
        assertHijrahDate(testDate, testEra, testYear, testMonthOfYear, testDayOfMonth);
    }
    
    @Test
    public void testHijrahDateIntIntIntInt() throws Exception{
        assertEquals(HijrahDate.hijrahDate(testEra, testYear, testMonthOfYear, testDayOfMonth), testDate);
        assertHijrahDate(testDate, testEra, testYear, testMonthOfYear, testDayOfMonth);
    }
    
    @Test
    public void testHijrahDateInvalidYear() throws Exception{
        try {
            HijrahDate.hijrahDate(10000, testMonthOfYear, testDayOfMonth);// Invalid year.
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }
    
    @Test
    public void testHijrahDateInvalidMonth() throws Exception{
        try {
            HijrahDate.hijrahDate(testYear, 13, testDayOfMonth);// Invalid month of year
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.monthOfYear());
        }
    }
    
    @Test
    public void testHijrahDateInvalidDay() throws Exception{
        try {
            HijrahDate.hijrahDate(testYear, testMonthOfYear, 40);// Invalid day of month.
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.dayOfMonth());
        }
    }

    @Test
    public void testGetChronology() {
        assertEquals(testDate.getChronology(), HijrahChronology.INSTANCE);
    }
    
    @Test
    public void testGet() throws Exception {
        assertEquals(testDate.get(HijrahChronology.INSTANCE.era()), testDate.getEra().getValue());
        assertEquals(testDate.get(HijrahChronology.INSTANCE.yearOfEra()), testDate.getYearOfEra());
        assertEquals(testDate.get(HijrahChronology.INSTANCE.monthOfYear()), testDate.getMonthOfYear());
        assertEquals(testDate.get(HijrahChronology.INSTANCE.dayOfMonth()), testDate.getDayOfMonth());
        assertEquals(testDate.get(HijrahChronology.INSTANCE.dayOfYear()), testDate.getDayOfYear());
        assertEquals(testDate.get(HijrahChronology.INSTANCE.dayOfWeek()), testDate.getDayOfWeek());
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
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, 5).getDayOfWeek(), 1);
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, 6).getDayOfWeek(), 2);
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, 7).getDayOfWeek(), 3);
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, 8).getDayOfWeek(), 4);
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, 9).getDayOfWeek(), 5);
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, 10).getDayOfWeek(), 6);
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, 11).getDayOfWeek(), 7);
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, 12).getDayOfWeek(), 1);
    }
    
    @Test
    public void testGetDayOfWeekCrossCheck() throws Exception {
        HijrahDate date = HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth);
        assertEquals(date.getDayOfWeek(), date.toLocalDate().getDayOfWeek().getValue());
    }

    @Test
    public void testIsLeapYear() {
        assertEquals(testDate.isLeapYear(), testLeapYear);
    }

    //-----------------------------------------------------------------------
    // withYear()
    //-----------------------------------------------------------------------
    @Test
    public void testWithYearOfEra() {
        HijrahDate date = testDate.withYearOfEra(1545);
        assertEquals(date, HijrahDate.hijrahDate(1545, testMonthOfYear, testDayOfMonth));
    }
    
    @Test
    public void testWithYearOfEraInvalidTooSmall() throws Exception {
        try {
            testDate.withYearOfEra(-1);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }
    
    @Test
    public void testWithYearOfEraInvalidTooBig() throws Exception {
        try {
            testDate.withYearOfEra(10000);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // withYear()
    //-----------------------------------------------------------------------
    @Test
    public void testWithYear() {
        HijrahDate date = testDate.withYear(HijrahEra.BEFORE_HIJRAH, 1540);
        assertEquals(date, HijrahDate.hijrahDate(HijrahEra.BEFORE_HIJRAH, 1540, testMonthOfYear, testDayOfMonth));
    }
    
    @Test
    public void testWithYearInvalidTooSmall() throws Exception {
        try {
            testDate.withYear(testEra, -1);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }
    
    @Test
    public void testWithYearInvalidTooBig() throws Exception {
        try {
            testDate.withYear(testEra, 10000);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // withMonthOfYear()
    //-----------------------------------------------------------------------
    @Test
    public void testWithMonthOfYear() {
        HijrahDate date = testDate.withMonthOfYear(4);
        assertEquals(date, HijrahDate.hijrahDate(testYear, 4, testDayOfMonth));
    }
    
    @Test
    public void testWithMonthOfYearInvalidTooSmall() throws Exception {
        try {
            testDate.withMonthOfYear(-1);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.monthOfYear());
        }
    }
    
    @Test
    public void testWithMonthOfYearInvalidTooBig() throws Exception {
        try {
            testDate.withMonthOfYear(13);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.monthOfYear());
        }
    }

    //-----------------------------------------------------------------------
    // withDayOfMonth()
    //-----------------------------------------------------------------------
    @Test
    public void testWithDayOfMonth() {
        HijrahDate date = testDate.withDayOfMonth(4);
        assertEquals(date, HijrahDate.hijrahDate(testYear, testMonthOfYear, 4));
    }
    
    @Test
    public void testWithDayOfMonthInvalidTooSmall() throws Exception {
        try {
            testDate.withDayOfMonth(0);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.dayOfMonth());
        }
    }
    
    @Test
    public void testWithDayOfMonthInvalidTooBig() throws Exception {
        try {
            testDate.withDayOfMonth(32);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.dayOfMonth());
        }
    }
    
    //-----------------------------------------------------------------------
    // withDayOfYear()
    //-----------------------------------------------------------------------
    @Test
    public void testWithDayOfYear() {
        HijrahDate date = testDate.withDayOfYear(15);
        assertEquals(date, HijrahDate.hijrahDate(testYear, 1, 15));
    }
    
    @Test
    public void testWithDayOfYearInvalidTooSmall() throws Exception {
        try {
            testDate.withDayOfYear(0);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.dayOfYear());
        }
    }
    
    @Test
    public void testWithDayOfYearInvalidTooBig() throws Exception {
        HijrahDate date = HijrahDate.hijrahDate(2008, 2, 1);
        try {
            date.withDayOfYear(367);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.dayOfYear());
        }
    }
    
    //-----------------------------------------------------------------------
    // plusYears()
    //-----------------------------------------------------------------------
    @Test
    public void testPlusYears() {
        assertEquals(testDate.plusYears(10), HijrahDate.hijrahDate(testYear+10, testMonthOfYear, testDayOfMonth));
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
        assertEquals(testDate.plusMonths(5), HijrahDate.hijrahDate(testYear, testMonthOfYear+5, testDayOfMonth));
    }
    
    @Test
    public void testPlusMonthsOverflow() throws Exception {
        try {
            testDate.plusMonths(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // plusDays()
    //-----------------------------------------------------------------------
    @Test
    public void testPlusDays() {
        assertEquals(testDate.plusDays(2), HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth+2));
    }
    
    @Test
    public void testPlusDaysOverflow() throws Exception {
        try {
            testDate.plusDays(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // plusWeeks()
    //-----------------------------------------------------------------------
    @Test
    public void testPlusWeeks() {
        assertEquals(testDate.plusWeeks(2), HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth+(2*7)));
    }
    
    @Test
    public void testPlusWeeksOverflow() throws Exception {
        try {
            testDate.plusWeeks(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // minusYears()
    //-----------------------------------------------------------------------
    @Test
    public void testMinusYears() {
        assertEquals(testDate.minusYears(10), HijrahDate.hijrahDate(testYear-10, testMonthOfYear, testDayOfMonth));
    }
    
    @Test (expectedExceptions=CalendricalException.class)
    public void testMinusYearsInvalidTooSmall() {
        testDate.minusYears(9999);
    }

    //-----------------------------------------------------------------------
    // minusMonths()
    //-----------------------------------------------------------------------
    @Test
    public void testMinusMonths() {
        assertEquals(testDate.minusMonths(1), HijrahDate.hijrahDate(testYear, testMonthOfYear-1, testDayOfMonth));
    }
    
    @Test
    public void testMinusMonthsOverflow() throws Exception {
        try {
            testDate.minusMonths(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // minusDays()
    //-----------------------------------------------------------------------
    @Test
    public void testMinusDays() {
        assertEquals(testDate.minusDays(2), HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth-2));
    }
    
    @Test
    public void testMinusDaysOverflow() throws Exception {
        try {
            testDate.minusDays(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }
    
    //-----------------------------------------------------------------------
    // minusWeeks()
    //-----------------------------------------------------------------------
    @Test
    public void testMinusWeeks() {
        assertEquals(testDate.minusWeeks(2), HijrahDate.hijrahDate(testYear, testMonthOfYear-1, 29+testDayOfMonth-(2*7)));
    }
    
    @Test
    public void testMinusWeeksOverflow() throws Exception {
        try {
            testDate.minusWeeks(Integer.MAX_VALUE);
            fail();
        } catch (IllegalCalendarFieldValueException ex) {
            assertEquals(ex.getFieldRule(), HijrahChronology.INSTANCE.yearOfEra());
        }
    }

    //-----------------------------------------------------------------------
    // toLocalDate()
    //-----------------------------------------------------------------------
    @Test
    public void testToLocalDate() {
        assertEquals(HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth).toLocalDate(),
                LocalDate.date(testGregorianYear, testGregorianMonthOfYear, testGregorianDayOfMonth));
    }

    //-----------------------------------------------------------------------
    // toCalendrical()
    //-----------------------------------------------------------------------
    @Test
    public void testToCalendrical() {
        Calendrical test = HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth).toCalendrical();
        assertEquals(test, new Calendrical(LocalDate.date(testGregorianYear, testGregorianMonthOfYear, testGregorianDayOfMonth), null, null, null));
    }

    //-----------------------------------------------------------------------
    // compareTo(), isAfter(), isBefore(), and equals()
    //-----------------------------------------------------------------------   
    @Test
    public void testCompareTo() throws Exception {
        doTestComparisons(
            HijrahDate.hijrahDate(1, 1, 1),
            HijrahDate.hijrahDate(1, 1, 2),
            HijrahDate.hijrahDate(1, 1, 30),
            HijrahDate.hijrahDate(1, 2, 1),
            HijrahDate.hijrahDate(1, 2, 29),
            HijrahDate.hijrahDate(1, 12, 29),
            HijrahDate.hijrahDate(2, 1, 1),
            HijrahDate.hijrahDate(2, 12, 29),
            HijrahDate.hijrahDate(3, 1, 1),
            HijrahDate.hijrahDate(3, 12, 29),
            HijrahDate.hijrahDate(4500, 1, 1),
            HijrahDate.hijrahDate(4500, 12, 29)
        );
    }

    void doTestComparisons(HijrahDate... dates) {
        for (int i = 0; i < dates.length; i++) {
            HijrahDate a = dates[i];
            for (int j = 0; j < dates.length; j++) {
                HijrahDate b = dates[j];
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
        HijrahDate a = HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth);
        HijrahDate b = HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth);
        assertEquals(a.equals(b), true);
        assertEquals(b.equals(a), true);
        assertEquals(a.equals(a), true);
        assertEquals(b.equals(b), true);
    }
    
    @Test
    public void testEqualsNotEqualDay() throws Exception {
        HijrahDate a = HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth);
        HijrahDate b = HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth+1);
        assertEquals(a.equals(b), false);
        assertEquals(b.equals(a), false);
        assertEquals(a.equals(a), true);
        assertEquals(b.equals(b), true);
    }

    @Test
    public void testEqualsNotEqualMonth() throws Exception {
        HijrahDate a = HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth);
        HijrahDate b = HijrahDate.hijrahDate(testYear, testMonthOfYear+1, testDayOfMonth);
        assertEquals(a.equals(b), false);
        assertEquals(b.equals(a), false);
        assertEquals(a.equals(a), true);
        assertEquals(b.equals(b), true);
    }

    @Test
    public void testEqualsNotEqualYear() throws Exception {
        HijrahDate a = HijrahDate.hijrahDate(testYear, testMonthOfYear, testDayOfMonth);
        HijrahDate b = HijrahDate.hijrahDate(testYear+1, testMonthOfYear, testDayOfMonth);
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
        assertEquals(testDate.equals("2009-07-15"), false);
    }
    
    //-----------------------------------------------------------------------
    // hashCode()
    //-----------------------------------------------------------------------

    @Test
    public void testHashCode() throws Exception {
        HijrahDate a = HijrahDate.hijrahDate(1, 1, 1);
        HijrahDate b = HijrahDate.hijrahDate(1, 1, 1);
        assertEquals(a.hashCode(), a.hashCode());
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(b.hashCode(), b.hashCode());
    }
    
    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @Test
    public void testToString() {
        String expected = "1430-03-05 (Hijrah)";
        String actual = testDate.toString();
        assertEquals(expected, actual);
    }
    
    private void assertHijrahDate(HijrahDate test, HijrahEra era, int year, int month, int day) throws Exception {
        assertEquals(test.getEra(), era);
        assertEquals(test.getYearOfEra(), year);
        assertEquals(test.getMonthOfYear(), month);
        assertEquals(test.getDayOfMonth(), day);
        assertEquals(test.isLeapYear(), isLeapYear(year));
    }
    
    private  boolean isLeapYear(int year) {
        return (14 + 11 * (year > 0 ? year : -year)) % 30 < 11;
    }
}
