package com.example.test.hubspottest.utils;


import com.example.test.hubspottest.entity.Invitation;
import com.example.test.hubspottest.entity.Partner;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static com.example.test.hubspottest.utils.HubspotConstants.DATE_FORMAT;
import static com.example.test.hubspottest.utils.HubspotConstants.DIFF_IN_DAYS;
@Slf4j
public class HubspotHelper {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    public static List<Invitation> checkAvailableDatesAndGetInvitations(List<Partner> partnersList) {
        Map<String, String> countryToDateMap = new HashMap<>();

        Map<String, List<Integer>> countriesToIndexMap = new HashMap<>();
        for (int i = 0; i < partnersList.size(); i++) {
            String country = partnersList.get(i).getCountry();
            List<Integer> indexes = countriesToIndexMap.getOrDefault(country, new ArrayList<>());
            indexes.add(i);
            countriesToIndexMap.put(country, indexes);
        }
        for (String country : countriesToIndexMap.keySet()) {
            List<String> datesList = new ArrayList<>();

            for (int index : countriesToIndexMap.get(country)) {
                List<String> dates = partnersList.get(index).getAvailableDates();

                for (int j = 0; j < dates.size() - 1; j++) {
                    if (getDiffInDates(dates.get(j), dates.get(j + 1)) == DIFF_IN_DAYS) {
                        datesList.add(dates.get(j));
                    }
                }
            }

            HashMap<String, Integer> datesCountMap = new HashMap<>();
            for (String dateStr : datesList) {
                datesCountMap.put(dateStr, datesCountMap.getOrDefault(dateStr, 0) + 1);
            }

            int maxDateCount = Collections.max(datesCountMap.values());
            List<String> similarCountDatesList = new ArrayList<>();
            for (String mapDateStr : datesCountMap.keySet()) {
                if (datesCountMap.get(mapDateStr) == maxDateCount) {
                    similarCountDatesList.add(mapDateStr);
                }
            }

            Collections.sort(similarCountDatesList);

            if (similarCountDatesList.size() > 0) {
                countryToDateMap.put(country, similarCountDatesList.get(0));
            } else {
                countryToDateMap.put(country, null);
            }
        }
        return generateInvitationRequestObj(countryToDateMap, partnersList, countriesToIndexMap);
    }
    public static List<Invitation> generateInvitationRequestObj(Map<String, String> countryToDateMap,
                                                                List<Partner> partnersList, Map<String, List<Integer>>
                                                                        countriesToIndexMap) {

        List<Invitation> invitationsList = new ArrayList<>();
        for (String country : countriesToIndexMap.keySet()) {
            int attendeeCount = 0;
            Set<String> attendeesList = new HashSet<>();

            for (int i : countriesToIndexMap.get(country)) {

                List<String> datesList = partnersList.get(i).getAvailableDates();
                if (datesList.contains(countryToDateMap.get(country))) {
                    Date currDate = null;

                    try {
                        currDate = dateFormat.parse(countryToDateMap.get(country));
                    } catch (ParseException e) {
                       log.info("Unable to parse date in countryToDateMap: {}",e.getMessage());
                    }

                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(currDate);
                    cal1.add(Calendar.DATE, DIFF_IN_DAYS);

                    Date nextDay = cal1.getTime();
                    String nextDayStr = dateFormat.format(nextDay);

                    if (datesList.contains(nextDayStr)) {
                        attendeesList.add(partnersList.get(i).getEmail());
                        attendeeCount++;
                    }
                }
            }
            Invitation invitation = new Invitation();

            invitation.setStartDate(countryToDateMap.get(country));
            invitation.setName(country);
            invitation.setAttendeeCount(attendeeCount);
            List<String> attendeesListArray = new ArrayList<>(attendeesList);
            invitation.setAttendees(attendeesListArray);

            invitationsList.add(invitation);
        }
        return invitationsList;
    }

    private static long getDiffInDates(String startDateStr, String endDateStr) {
        long diff = 0;
        try {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            long diffInMilliSeconds = Math.abs(endDate.getTime() - startDate.getTime());
            diff = TimeUnit.DAYS.convert(diffInMilliSeconds, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            log.info("Exception when comparing dates: {}", ex.getMessage());
        }
        return diff;
    }
}
