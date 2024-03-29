package com.codeforces.graygoose.util;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class GoogleCalendarUtil {
    public static void addEvent(String title, String username, String password) throws GoogleCalendarException {
        CalendarEventEntry entry = new CalendarEventEntry();
        CalendarService service = new CalendarService("GrayGoose-1.0");

        try {
            service.setUserCredentials(username, password);
        } catch (AuthenticationException e) {
            throw new GoogleCalendarException("Illegal credentials.", e);
        }

        entry.setService(service);
        entry.setTitle(TextConstruct.plainText(title));
        entry.setContent(TextConstruct.plainText(""));

        setupWhen(entry);
        try {
            CalendarEventEntry insertedEntry = service.insert(
                    new URL("http://www.google.com/calendar/feeds/default/private/full"), entry
            );
            setupReminder(insertedEntry);
            insertedEntry.update();
        } catch (IOException | ServiceException e) {
            throw new GoogleCalendarException("Can't add calendar event.", e);
        }
    }

    private static void setupReminder(CalendarEventEntry entry) {
        Reminder reminder = new Reminder();
        reminder.setMinutes(1);
        reminder.setMethod(Reminder.Method.SMS);
        entry.getReminder().add(reminder);
    }

    private static void setupWhen(CalendarEventEntry entry) {
        When when = new When();

        Calendar startTime = new GregorianCalendar();
        startTime.add(Calendar.MINUTE, 2);

        Calendar endTime = new GregorianCalendar();
        endTime.add(Calendar.MINUTE, 3);

        when.setStartTime(new DateTime(startTime.getTime(), TimeZone.getDefault()));
        when.setEndTime(new DateTime(endTime.getTime(), TimeZone.getDefault()));

        entry.addTime(when);
    }

    private GoogleCalendarUtil() {
        throw new UnsupportedOperationException();
    }
}
