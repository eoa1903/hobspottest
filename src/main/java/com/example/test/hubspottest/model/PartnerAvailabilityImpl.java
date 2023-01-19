package com.example.test.hubspottest.model;
import com.example.test.hubspottest.entity.Partner;
import com.example.test.hubspottest.entity.PartnersAvailability;
import com.example.test.hubspottest.entity.Invitation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.test.hubspottest.utils.HubspotConstants.COUNTRIES;

@Repository
@Slf4j
public class PartnerAvailabilityImpl implements PartnersAvailability {
        @Value("${api.get.partners.url}")
        private String getPartnersUrl;
        @Value("${api.post.invitation.url}")
        private String postInvitationUrl;
        @Override
        public List<Partner> getPartnerAvailability() {
            RestTemplate restTemplate = new RestTemplate();
            PartnerWrapper result = restTemplate.getForObject(getPartnersUrl, PartnerWrapper.class);
            assert result != null;
            return result.getPartners();
        }

    @Override
        public String sendInvitations(List<Invitation> invitationList) {
            String response;
            try {
                Map<String, List<Invitation>> list = new HashMap<>();
                list.put(COUNTRIES, invitationList);

                HttpEntity<Map<String, List<Invitation>>> request = new HttpEntity<>(list);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Invitation> result = restTemplate.postForEntity(postInvitationUrl, request, Invitation.class);
                response = result.getStatusCode().toString();

            } catch (HttpClientErrorException ex) {
                log.info("Exception status code: {}", ex.getStatusCode());
                log.info("Exception response body: {}", ex.getResponseBodyAsString());
                log.info("Exception during send invitations post request: {}",ex.getMessage());
                response = ex.getResponseBodyAsString();
            }
            return response;
        }

}
