/*
 * Copyright (c) 2007,2008, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendar.field;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceArray;

import javax.time.calendar.CalendricalProvider;
import javax.time.calendar.Calendrical;
import javax.time.calendar.ISOChronology;
import javax.time.calendar.IllegalCalendarFieldValueException;
import javax.time.calendar.DateProvider;
import javax.time.calendar.DateTimeFieldRule;

/**
 * A representation of a week of week-based year in the ISO-8601 calendar system.
 * <p>
 * WeekOfWeekyear is an immutable time field that can only store a week of week-based year.
 * It is a type-safe way of representing a week of week-based year in an application.
 * <p>
 * The week of week-based year is a field that should be used in combination with
 * the Weekyear field. Together they represent the ISO-8601 week based date
 * calculation described in {@link Weekyear}.
 * <p>
 * Static factory methods allow you to construct instances.
 * The week of week-based year may be queried using getValue().
 * <p>
 * WeekOfWeekyear is thread-safe and immutable.
 *
 * @author Michael Nascimento Santos
 * @author Stephen Colebourne
 */
public final class WeekOfWeekyear implements CalendricalProvider, Comparable<WeekOfWeekyear>, Serializable {

    /**
     * A serialization identifier for this instance.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Cache of singleton instances.
     */
    private static final AtomicReferenceArray<WeekOfWeekyear> cache = new AtomicReferenceArray<WeekOfWeekyear>(53);

    /**
     * The week of week-based year being represented.
     */
    private final int weekOfWeekyear;

    //-----------------------------------------------------------------------
    /**
     * Gets the rule that defines how the week of week-based-year field operates.
     * <p>
     * The rule provides access to the minimum and maximum values, and a
     * generic way to access values within a calendrical.
     *
     * @return the week of week-based-year rule, never null
     */
    public static DateTimeFieldRule rule() {
        return ISOChronology.weekOfWeekyearRule();
    }

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>WeekOfWeekyear</code> from a value.
     * <p>
     * A week of week-based year object represents one of the 53 weeks of the year,
     * from 1 to 53. These are cached internally and returned as singletons,
     * so they can be compared using ==.
     *
     * @param weekOfWeekyear  the week of week-based year to represent, from 1 to 53
     * @return the WeekOfWeekyear singleton, never null
     * @throws IllegalCalendarFieldValueException if the weekOfWeekyear is invalid
     */
    public static WeekOfWeekyear weekOfWeekyear(int weekOfWeekyear) {
        try {
            WeekOfWeekyear result = cache.get(--weekOfWeekyear);
            if (result == null) {
                WeekOfWeekyear temp = new WeekOfWeekyear(weekOfWeekyear + 1);
                cache.compareAndSet(weekOfWeekyear, null, temp);
                result = cache.get(weekOfWeekyear);
            }
            return result;
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalCalendarFieldValueException(rule(), weekOfWeekyear, 1, 53);
        }
    }

    /**
     * Obtains an instance of <code>WeekOfWeekyear</code> from a date provider.
     * <p>
     * This can be used extract a week of week-based year object directly from
     * any implementation of DateProvider, including those in other calendar systems.
     *
     * @param dateProvider  the date provider to use, not null
     * @return the WeekOfWeekyear singleton, never null
     */
    public static WeekOfWeekyear weekOfWeekyear(DateProvider dateProvider) {
        return new WeekOfWeekyear(1);  // TODO
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance with the specified week of week-based year.
     *
     * @param weekOfWeekyear  the week of week-based year to represent
     */
    private WeekOfWeekyear(int weekOfWeekyear) {
        this.weekOfWeekyear = weekOfWeekyear;
    }

    /**
     * Resolve the singleton.
     *
     * @return the singleton, never null
     */
    private Object readResolve() {
        return weekOfWeekyear(weekOfWeekyear);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the week of week-based year value.
     *
     * @return the week of week-based year, from 1 to 53
     */
    public int getValue() {
        return weekOfWeekyear;
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this field to a <code>Calendrical</code>.
     *
     * @return the calendrical representation for this instance, never null
     */
    public Calendrical toCalendrical() {
        return Calendrical.calendrical(rule(), getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if this week of weekyear is valid for the specified week-based year.
     *
     * @param weekyear  the weekyear to validate against, not null
     * @return true if this week of weekyear is valid for the week-based year
     */
    public boolean isValid(Weekyear weekyear) {
        return (weekOfWeekyear < 53 || weekyear.lengthInWeeks() == 53);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this week of week-based year instance to another.
     *
     * @param otherWeekOfWeekyear  the other week of week-based year instance, not null
     * @return the comparator value, negative if less, postive if greater
     * @throws NullPointerException if otherWeekOfWeekyear is null
     */
    public int compareTo(WeekOfWeekyear otherWeekOfWeekyear) {
        int thisValue = this.weekOfWeekyear;
        int otherValue = otherWeekOfWeekyear.weekOfWeekyear;
        return (thisValue < otherValue ? -1 : (thisValue == otherValue ? 0 : 1));
    }

    //-----------------------------------------------------------------------
    /**
     * Is this instance equal to that specified, evaluating the week of week-based year.
     *
     * @param otherWeekOfWeekyear  the other week of week-based year instance, null returns false
     * @return true if the week of week-based year is the same
     */
    @Override
    public boolean equals(Object otherWeekOfWeekyear) {
        if (this == otherWeekOfWeekyear) {
            return true;
        }
        if (otherWeekOfWeekyear instanceof WeekOfWeekyear) {
            return weekOfWeekyear == ((WeekOfWeekyear) otherWeekOfWeekyear).weekOfWeekyear;
        }
        return false;
    }

    /**
     * A hashcode for the week of week-based year object.
     *
     * @return a suitable hashcode
     */
    @Override
    public int hashCode() {
        return weekOfWeekyear;
    }

    /**
     * A string describing the week of week-based year object.
     *
     * @return a string describing this object
     */
    @Override
    public String toString() {
        return "WeekOfWeekyear=" + getValue();
    }

}
