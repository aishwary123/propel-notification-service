package com.notification.propel.controllers.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notification.propel.configs.tenancy.TenantManagement;
import com.notification.propel.interfaces.RestAPIPrefix;
import com.notification.propel.messages.CustomMessages;

@RestController
@RequestMapping(value = ApplicationController.REST_ENDPOINT)
public class ApplicationController {

    public static final String REST_ENDPOINT = RestAPIPrefix.API_BASE_V1_REQUEST_ENDPOINT
                + "/tenant";

    @Autowired
    private TenantManagement tenantManagement;

    @GetMapping(value = "/onboard")
    public ResponseEntity<String> onboardTenant() {

        tenantManagement.createNotificationTables();
        return new ResponseEntity<>(
                    CustomMessages.TENANT_ONBOARDED_SUCCESSFULLY,
                    HttpStatus.OK);
    }
}
