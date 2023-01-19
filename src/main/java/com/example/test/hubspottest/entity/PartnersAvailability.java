package com.example.test.hubspottest.entity;

import java.util.List;

public interface PartnersAvailability {
    List<Partner> getPartnerAvailability();
    String sendInvitations (List<Invitation> invitationList);
}
