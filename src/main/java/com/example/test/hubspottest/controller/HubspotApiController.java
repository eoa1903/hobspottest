package com.example.test.hubspottest.controller;


import com.example.test.hubspottest.entity.Invitation;
import com.example.test.hubspottest.entity.Partner;
import com.example.test.hubspottest.service.HubspotApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hubspot/api/")
@Slf4j
public class HubspotApiController {
    @Autowired
    private HubspotApiService hubspotApiService;
    private List<Partner> partnersList;
    private List<Invitation> invitationsList;

    @RequestMapping(value = "/" + "partners", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getPartnersAndSendInvitations() {
        partnersList = hubspotApiService.getPartnersAvailability();

        if (CollectionUtils.isEmpty(partnersList)) {
            return "Unable to get partners list.";
        }

        invitationsList = hubspotApiService.getInvitationsList(partnersList);
        invitationsList.forEach(val ->{
            log.info(val.toString());
        });
        return hubspotApiService.sendInvitations(invitationsList);
    }
}
