package org.openmrs.module.ugandaemrfingerprint.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.ugandaemrfingerprint.core.FingerPrintConstant;
import org.openmrs.module.ugandaemrfingerprint.remoteserver.FingerPrintGlobalProperties;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import static org.openmrs.module.ugandaemrfingerprint.core.FingerPrintConstant.*;

/**
 * Marking a patient as dead
 */
public class PatientInOtherFacilityPageController {
    protected final Log log = LogFactory.getLog(this.getClass());

    public void controller(UiSessionContext sessionContext, PageModel model) {
    }

    public void get(@SpringBean PageModel pageModel, @RequestParam(value = "breadcrumbOverride", required = false) String breadcrumbOverride, @RequestParam(value = "patientId", required = false) String patientId) {
        FingerPrintGlobalProperties fingerPrintGlobalProperties = new FingerPrintGlobalProperties();
        pageModel.put("familyName", "");
        pageModel.put("middleName", "");
        pageModel.put("givenName", "");
        pageModel.put("birthdate", false);
        pageModel.put("gender", false);
        pageModel.put("dead", false);
        pageModel.put("summaryPage", fingerPrintGlobalProperties.getGlobalProperty("ugandaemrfingerprint.showOnlinePatientSummary"));
        pageModel.put("displaySummaryPage", fingerPrintGlobalProperties.getGlobalProperty("ugandaemrfingerprint.showOnlinePatientSummary"));
        pageModel.put("mostRecentEncounter", fingerPrintGlobalProperties.getGlobalProperty("ugandaemrfingerprint.showOnlinePatientLastTreatmentEncounter"));
        pageModel.put("displayMostRecentEncounter", fingerPrintGlobalProperties.getGlobalProperty("ugandaemrfingerprint.showOnlinePatientLastTreatmentEncounter"));
        pageModel.put("facilityName", "");
        pageModel.put("facilityId", "");
        pageModel.put("patientId", patientId);
        pageModel.put("patientSummary", "");
        pageModel.put("patientFound", false);
        pageModel.put("patientNotFound", "");
        pageModel.put("connectionFailed", "");
        pageModel.put("searched", false);
        pageModel.put("breadcrumbOverride", breadcrumbOverride);
        pageModel.put("onlineIpAddress", fingerPrintGlobalProperties.getGlobalProperty(FingerPrintConstant.CONNECTION_SERVER_IP_GLOBALPROPERTY));
        pageModel.put("showOnlinePatientSummary", fingerPrintGlobalProperties.getGlobalProperty(FingerPrintConstant.SHOW_PATIENT_CLINIC_SUMMARY));
        pageModel.put("showOnlinePatientLastTreatmentEncounter", fingerPrintGlobalProperties.getGlobalProperty(FingerPrintConstant.SHOW_PATIENT_LAST_TREATMENT_ENCOUNTER));
        pageModel.put("queryURL", FingerPrintConstant.SEARCH_URL);
        pageModel.put("searchString", PATIENT_UUID_SEARCH_STRING);
        pageModel.put("nationalIdString", PATIENT_NATIONAL_ID_SEARCH_STRING);
        pageModel.put("connectionProtocol", CONNECTION_PROTOCOL);
        pageModel.put("patientDetails", PATIENT_DETAILS);
        pageModel.put("patientSummarySearchQuery", SUMMARY_ENCOUNTER);
        pageModel.put("patientMostRecentEncounterSearchQuery", MOST_RECENT_ENCOUNTER);
    }
}
