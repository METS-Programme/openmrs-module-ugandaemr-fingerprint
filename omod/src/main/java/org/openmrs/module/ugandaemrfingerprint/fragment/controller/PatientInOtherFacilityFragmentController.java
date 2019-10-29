package org.openmrs.module.ugandaemrfingerprint.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.ugandaemrfingerprint.UgandaEMRFingerprintService;
import org.openmrs.module.ugandaemrfingerprint.core.PatientInOtherFacility;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Lubwama Samuel on 26/01/2016.
 */
public class PatientInOtherFacilityFragmentController {


    private static final Log log = LogFactory.getLog(PatientInOtherFacilityFragmentController.class);


    public void controller(FragmentModel model, UiSessionContext sessionContext, HttpServletRequest request, @SpringBean("adminService") AdministrationService administrationService, @FragmentParam(value = "showLastViewedPatients", required = false) Boolean showLastViewedPatients, @FragmentParam(value = "initialSearchFromParameter", required = false) java.lang.String searchByParam) {
    }


    public SimpleObject processPatientEncounters(FragmentModel model, @RequestParam(value = "patientData", required = false) java.lang.String patientData, UiUtils ui) throws IOException {

        JSONObject jsonObj = new JSONObject();

        jsonObj = new JSONObject(patientData);

        ObjectMapper objectMapper = new ObjectMapper();

        SimpleObject simpleObject = new SimpleObject();

        UgandaEMRFingerprintService ugandaEMRFingerprintService = Context.getService(UgandaEMRFingerprintService.class);

        PatientInOtherFacility patientInOtherFacility = new PatientInOtherFacility();

        patientInOtherFacility = ugandaEMRFingerprintService.getPatientInOtherFacility(jsonObj);

        simpleObject.put("patientData", objectMapper.writeValueAsString(patientInOtherFacility));

        return simpleObject;
    }
}