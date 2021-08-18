/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package jakarta.mail.internet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test MailDateFormat: formatting and parsing of dates as specified by
 * <a href="http://www.ietf.org/rfc/rfc2822.txt">RFC 2822</a>.
 *
 * @author	Anthony Vanelverdinghe
 */
public class MailDateFormatTest {

    /*
     * Formatting + parsing
     */
    @Test
    public void mustSucceedRoundTrip() throws ParseException {
        Date date = new Date(1341100798000L); // milliseconds must be 0
        SimpleDateFormat fmt = getDefault();
        assertThat(fmt.parse(fmt.format(date)), is(date));
    }

    /*
     * Formatting
     */
    @Test()
    public void formatMustThrowNpeForNullArgs() {
        for (int mask = 0; mask < 7; mask++) {
            try {
                Date date = (mask & 1) == 1
                        ? new Date() : null;
                StringBuffer toAppendTo = (mask & 2) == 2
                        ? new StringBuffer() : null;
                FieldPosition pos = (mask & 4) == 4
                        ? new FieldPosition(0) : null;
                getDefault().format(date, toAppendTo, pos);
                fail("No NullPointerException thrown for mask = " + mask);
            } catch (NullPointerException e) {
            }
        }
    }

    @Test
    public void mustFormatRfc2822WithOptionalCfws() {
        Date date = mustPass(getDefault(), "1 Jan 2015 00:00 +0000");
        assertThatDate(date, "Thu, 1 Jan 2015 00:00:00 +0000 (UTC)");
    }

    @Test
    public void mustUseTimeZoneForFormatting() {
        String input = "1 Jan 2015 00:00 +0100";
        SimpleDateFormat fmt = getDefault();
        Date date = mustPass(fmt, input);
        fmt.setTimeZone(TimeZone.getTimeZone("Etc/GMT+8"));
        assertThat(fmt.format(date),
                is("Wed, 31 Dec 2014 15:00:00 -0800 (GMT-08:00)"));
    }

    /*
     * Parsing
     */
    @Test
    public void parseMustThrowNpeForNullArgs() {
        for (int mask = 0; mask < 3; mask++) {
            try {
                String source = (mask & 1) == 1
                        ? "1 Jan 2015 00:00 +0100" : null;
                ParsePosition pos = (mask & 2) == 2
                        ? new ParsePosition(0) : null;
                getDefault().parse(source, pos);
                fail("No NullPointerException thrown for mask = " + mask);
            } catch (NullPointerException e) {
            }
        }
    }

    @Test
    public void mustReturnOnInvalidParsePosition() {
        assertNull(getDefault().parse("", new ParsePosition(-1)));
        assertNull(getDefault().parse("", new ParsePosition(0)));
        assertNull(getDefault().parse("", new ParsePosition(1)));
    }

    @Test
    public void mustParseRfc2822() {
        mustPass(getDefault(), "Thu, 1 Jan 2015 00:00:00 +0000");
        mustFail(getStrict(), "foo", 0);
        mustFail(getLenient(), "foo", 3);
    }

    @Test
    public void mustRetainTimeZoneForBackwardCompatibility() {
        // though java.text.DateFormat::parse(String, ParsePosition) specifies:
        // "This parsing operation uses the calendar to produce a Date. As a
        // result, the calendar's date-time fields and the TimeZone value
        // may have been overwritten, depending on subclass implementations.
        // Any TimeZone value that has previously been set by a call to
        // setTimeZone may need to be restored for further operations."
        // we must retain the TimeZone value for backward compatibility reasons
        SimpleDateFormat fmt = getDefault();
        fmt.setTimeZone(TimeZone.getTimeZone("Europe/Brussels"));
        mustPass(fmt, "1 Jan 2015 00:00 +0000");
        assertThat(fmt.getTimeZone(),
                is(TimeZone.getTimeZone("Europe/Brussels")));
    }

    @Test
    public void mustUseLeniencyForParsing() {
        String lenientInput = "1-Jan-2015 00:00 +0000";
        String strictInput = "1 Jan 2015 00:00 +0000";

        SimpleDateFormat fmt = getStrict();
        mustFail(fmt, lenientInput, 1);
        mustPass(fmt, strictInput);

        fmt.setLenient(true);
        mustPass(fmt, lenientInput);
        mustPass(fmt, strictInput);

        fmt.setLenient(false);
        mustFail(fmt, lenientInput, 1);
        mustPass(fmt, strictInput);
    }

    @Test
    public void mustReportCorrectErrorIndex() {
        mustFail(getDefault(), "1 Juk 2015 00:00 +0000", 2);
        mustFail(getStrict(), "1\r\nJan 2015 00:00 +0000", 1);
        mustFail(getStrict(), "1 Jan 201 00:00 +0000", 6);
    }

    @Test
    public void lenientMustBeBackwardCompatible() {
        mustPass(getLenient(), "Thu, 1 Jan 2015 12:00 +0000");
        mustPass(getLenient(), "32 dEC 2015 12:00 +0099");
        mustPass(getLenient(), "fooFri,999-Jan-99999999 99:99:99+2399");
        mustPass(getLenient(), "fooFri,999-Jan-99999999 99:99:99+2399");
        mustPass(getLenient(), "fooFri ,999Jan99999999 99:99:99+2399");
        mustPass(getLenient(), "foo31-mAY-1\r3:3:3+");
    }

    @Test
    public void strictMustRejectInvalidBegin() {
        mustFail(getStrict(), "foo1 Jan 2015 00:00 +0000", 0);
    }

    @Test
    public void strictMustRejectSemanticallyInvalidDate() {
        mustFail(getStrict(), "Fri, 1 Jan 2015 00:00 +0000", 27);
    }

    /*
     * Parsing - range constraints
     */
    @Test
    public void mustRejectFieldsWithTooManyDigits() {
        mustFail(getDefault(), "1234 Jan 2015 00:00 +0000", 0);
        mustFail(getDefault(), "1 Jan 123456789 00:00 +0000", 6);
        mustFail(getDefault(), "1 Jan 2015 123:00 +0000", 11);
        mustFail(getDefault(), "1 Jan 2015 00:123 +0000", 14);
        mustFail(getDefault(), "1 Jan 2015 00:00:123 +0000", 17);
    }

    @Test
    public void mustCountLeadingZeroesForTooManyDigits() {
        mustFail(getDefault(), "0000000000001 Jan 2015 00:00 +0000", 0);
    }

    /*
     * Parsing - overflow
     */
    @Test
    public void mustFailFastIfDayContainsTooManyDigits() {
        mustFail(getLenient(),
		"100000000000000000000000000000001 Jan 2015 00:00 +0000", 0);
    }

    @Test
    public void mustFailFastIfYearContainsTooManyDigits() {
        mustFail(getLenient(),
		"1 Jan 100000000000000000000000000000002015 00:00 +0000", 6);
    }

    @Test
    public void mustFailIfYearIsTooBigForGregorianCalendar() {
        int tooBig = 999999999;
        assertTrue(tooBig > new GregorianCalendar().getMaximum(Calendar.YEAR));
        mustFail(getLenient(), "1 Jan " + tooBig + " 00:00 +0000", 6);
    }

    @Test
    public void mustFailOnIntegerOverflow() {
        mustFail(getLenient(), "456378941961 Jan 2015 00:00 +0000", 0);
    }

    /*
     * Parsing - FWS (folding white space)
     */
    @Test
    public void mustFailOrSkipCfws() {
        String input = "(3 Jan 2015 00:00 +0000) 1 Jan 2015 00:00 +0000";
        try {
            // NOTE this test fails with getLenient(),
            // since lenient parsing must remain backward compatible
            Date date = getStrict().parse(input);
            assertThatDate(date, "Thu, 1 Jan 2015 00:00:00 +0000 (UTC)");
        } catch (ParseException ignored) {
            assertTrue("Not supporting CFWS is allowed", true);
        }
    }

    @Test
    public void lenientMustAcceptEmptyFwsIffUnambiguous() {
        mustPass(getLenient(), "Thu,1Jan2015 12:00+0000");
        mustFail(getLenient(), "1 Jan 1015123456:00 +0000", 6);
    }

    @Test
    public void lenientMustRejectInconsistentMonthFws() {
        mustFail(getLenient(), "1-Jan 2015 00:00 +0000", 5);
    }

    @Test
    public void strictMustRejectInvalidFws() {
        mustFail(getStrict(), "\r\n\r\n 1 Jan 2015 00:00 +0000", 0);
        mustFail(getStrict(), "\r\n1 Jan 2015 00:00 +0000", 0);
        mustFail(getStrict(), "\n 1 Jan 2015 00:00 +0000", 0);
        mustFail(getStrict(), " \n 1 Jan 2015 00:00 +0000", 1);
        mustFail(getStrict(), " \r\n1 Jan 2015 00:00 +0000", 0);
    }

    /*
     * Parsing - day-name and month-name
     */
    @Test
    public void mustAcceptValidDayMonthNames() {
        SimpleDateFormat fmt = getDefault();
        for (String dayMonth : new String[]{
            "Mon, 5 Jan", "Tue, 3 Feb", "Wed, 4 Mar", "Thu, 2 Apr",
            "Fri, 1 May", "Sat, 6 Jun", "Sun, 5 Jul", "Sat, 1 Aug",
            "Tue, 1 Sep", "Thu, 1 Oct", "Sun, 1 Nov", "Tue, 1 Dec"
        }) {
            mustPass(fmt, dayMonth + " 2015 00:00:00 +0000");
        }
    }

    @Test
    public void strictMustMatchDayNameCaseSensitive() {
        mustFail(getStrict(), "tHU, 1 Jan 2015 00:00 +0000", 0);
    }

    @Test
    public void strictMustMatchMonthNameCaseSensitive() {
        mustFail(getStrict(), "1 jAN 2015 00:00 +0000", 2);
    }

    /*
     * Parsing - year
     */
    @Test
    public void lenientMustAcceptSingleDigitYears() {
        mustPass(getLenient(), "1 Jan 1 00:00 +0000");
    }

    @Test
    public void lenientMustAcceptYearsBetween1000and1899() {
        mustPass(getLenient(), "1 Jan 999 00:00 +0000");
        mustPass(getLenient(), "1 Jan 1000 00:00 +0000");
        mustPass(getLenient(), "1 Jan 1899 00:00 +0000");
        mustPass(getStrict(), "1 Jan 1900 00:00 +0000");
    }

    @Test
    public void lenientMustAdd1900To3DigitYear() {
        Date date = mustPass(getLenient(), "1 Jul 900 00:00 +0000");
        assertThatDate(date, "Sat, 1 Jul 2800 00:00:00 +0000 (UTC)");
    }

    @Test
    public void strictMustRejectAllYearsLt1900() {
        mustFail(getStrict(), "1 Jan 000001 00:00 +0000", 6);
        mustFail(getStrict(), "1 Jan 999 00:00 +0000", 6);
        mustFail(getStrict(), "1 Jan 1000 00:00 +0000", 6);
        mustFail(getStrict(), "1 Jan 1899 00:00 +0000", 6);
        mustPass(getStrict(), "1 Jan 1900 00:00 +0000");
    }

    /*
     * Parsing - second
     */
    @Test
    public void mustParseLeapSecondAsJsr310() {
        // JSR-310 replaces 60 with 59
        Date date = mustPass(getDefault(), "30 Jun 2012 23:59:60 +0000");
        // Date.from(ISO_INSTANT.parse("2012-06-30T23:59:60Z", Instant::from))
        assertThat(date, is(new Date(1341100799000L)));
    }

    /*
     * Parsing - zone
     */
    @Test
    public void mustParseNegativeZeroesZoneAsUtc() {
        Date date = mustPass(getDefault(), "1 Jan 2015 00:00 -0000");
        assertThatDate(date, "Thu, 1 Jan 2015 00:00:00 +0000 (UTC)");
    }

    @Test
    public void mustCorrectlyParseInputWithTrailingDigits() {
        Date date = mustPass(getStrict(), "1 Jan 2015 00:00 -001530");
        assertThatDate(date, "Thu, 1 Jan 2015 00:15:00 +0000 (UTC)");
    }

    @Test
    public void mustAcceptZoneWithExtremeOffset() {
        Date date = mustPass(getStrict(), "1 Jan 2015 00:00 +9959");
        Date equivalentWithoutOffset = mustPass(getStrict(),
                "27 Dec 2014 20:01 +0000");
        assertThat(date, is(equivalentWithoutOffset));

        date = mustPass(getStrict(), "1 Jan 2015 00:00 -9959");
        equivalentWithoutOffset = mustPass(getStrict(),
                "5 Jan 2015 03:59 +0000");
        assertThat(date, is(equivalentWithoutOffset));
    }

    @Test
    public void lenientMustAcceptNonMilitaryZoneNames() {
        SimpleDateFormat fmt = getLenient();
        for (String zoneName : new String[]{"GMT", "UT",
            "EDT", "EST", "CDT", "CST", "MDT", "MST", "PDT", "PST"}) {
            mustPass(fmt, "1 Jan 2015 00:00 " + zoneName);
        }
    }

    @Test
    public void strictMustMatchZone() {
        mustFail(getStrict(), "1 Jan 2015 00:00", 16);
    }

    @Test
    public void strictMustRejectZoneOffsetMinutesGreaterThan60() {
        mustFail(getStrict(), "1 Jan 2015 00:00 +0060", 17);
    }

    /*
     * Unsupported methods. When possible, the test also demonstrates 
     * why invoking the method must be prohibited.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void mustProhibitSetCalendar() {
        getDefault().setCalendar(Calendar.getInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mustProhibitSetNumberFormat() {
        getDefault().setNumberFormat(NumberFormat.getInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mustProhibitApplyLocalizedPattern() {
        SimpleDateFormat fmt = getStrict();
        fmt.applyLocalizedPattern("yyyy");
        Date date = mustPass(fmt, "1 Jan 2015 00:00:00 +0000");
        assertThat(fmt.format(date), is("2015"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mustProhibitApplyPattern() {
        SimpleDateFormat fmt = getStrict();
        fmt.applyPattern("yyyy");
        Date date = mustPass(fmt, "1 Jan 2015 00:00:00 +0000");
        assertThat(fmt.format(date), is("2015"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mustProhibitGet2DigitYearStart() {
        getDefault().get2DigitYearStart();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mustProhibitSet2DigitYearStart() {
        getDefault().set2DigitYearStart(new Date());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mustProhibitSetDateFormatSymbols() {
        SimpleDateFormat fmt = getStrict();
        fmt.setDateFormatSymbols(new DateFormatSymbols(Locale.FRENCH));
        Date date = mustPass(fmt, "1 Jan 2015 00:00:00 +0000");
        assertThatDate(date, "jeu., 1 janv. 2015 00:00:00 +0000 (UTC)");
    }

    /*
     * Helper methods
     */
    private static SimpleDateFormat getDefault() {
        return new MailDateFormat();
    }

    private static SimpleDateFormat getLenient() {
        SimpleDateFormat fmt = getDefault();
        fmt.setLenient(true);
        return fmt;
    }

    private static SimpleDateFormat getStrict() {
        SimpleDateFormat fmt = getDefault();
        fmt.setLenient(false);
        return fmt;
    }

    private void assertThatDate(Date date, String formattedDate) {
        SimpleDateFormat fmt = getDefault();
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertThat(fmt.format(date), is(formattedDate));
    }

    private Date mustPass(DateFormat fmt, String input) {
        try {
            Date date = fmt.parse(input);
            assertNotNull(date);
            return date;
        } catch (ParseException e) {
            fail(String.format("'%s' is a valid date in %s mode", input,
                    fmt.isLenient() ? "lenient" : "strict"));
            throw new AssertionError();
        }
    }

    private void mustFail(DateFormat fmt, String input, int errorOffset) {
        try {
            Date result = fmt.parse(input);
            fail(String.format("'%s' is not a valid date in %s mode", input,
                    fmt.isLenient() ? "lenient" : "strict"));
        } catch (ParseException e) {
            assertThat(e.getErrorOffset(), is(errorOffset));
        }
    }

}
