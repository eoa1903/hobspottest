package com.example.test.hubspottest.service;

import com.example.test.hubspottest.entity.PartnersAvailability;
import com.example.test.hubspottest.entity.Invitation;
import com.example.test.hubspottest.entity.Partner;
import com.example.test.hubspottest.utils.HubspotHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HubspotApiServiceImpl implements HubspotApiService{
    @Autowired
    private PartnersAvailability partnerAvailability;
    @Override
    public List<Partner> getPartnersAvailability() {
        List<Partner> partnersList =partnerAvailability.getPartnerAvailability();
        return partnersList;
    }
    @Override
    public List<Invitation> getInvitationsList(List<Partner> partnersList) {
        return HubspotHelper.checkAvailableDatesAndGetInvitations(partnersList);
    }
    @Override
    public String sendInvitations(List<Invitation> invitations) {
        return partnerAvailability.sendInvitations(invitations);
    }
}
