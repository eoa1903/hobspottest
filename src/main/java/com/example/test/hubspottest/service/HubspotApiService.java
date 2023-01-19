package com.example.test.hubspottest.service;

import com.example.test.hubspottest.entity.Invitation;
import com.example.test.hubspottest.entity.Partner;

import java.util.List;

public interface HubspotApiService {
    List<Partner> getPartnersAvailability();
    List<Invitation> getInvitationsList(List<Partner> partnersList);
    String sendInvitations(List<Invitation> invitations);

}
