package com.example.test.hubspottest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {

    private String name;
    private String startDate;
    private int attendeeCount;
    private List<String> attendees;

}
