package org.openmrs.module.ugandaemrfingerprint.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.ugandaemrfingerprint.UgandaEMRFingerprintService;
import org.openmrs.module.ugandaemrfingerprint.model.Fingerprint;
import org.openmrs.module.ugandaemrfingerprint.remoteserver.FingerPrintGlobalProperties;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.ugandaemrfingerprint.core.FingerPrintConstant.*;

/**
 * Fragment controller for patient search widget; sets the min # of search characters based on global property,
 * and loads last viewed patients for current user if "showLastViewedPatients" fragment config param=true
 */
public class AddPatientFingerprintFragmentController implements Serializable {

    protected final Log log = LogFactory.getLog(this.getClass());

    public void controller(FragmentModel model, UiSessionContext sessionContext, HttpServletRequest request, @SpringBean("adminService") AdministrationService administrationService, @RequestParam(value = "patientId", required = false) String patient, @FragmentParam(value = "showLastViewedPatients", required = false) Boolean showLastViewedPatients, @FragmentParam(value = "initialSearchFromParameter", required = false) String searchByParam) {
        UgandaEMRFingerprintService ugandaEMRFingerprintService = Context.getService(UgandaEMRFingerprintService.class);

        if (!ugandaEMRFingerprintService.isUUID(patient)) {
            PatientService patientService = Context.getPatientService();
            patient = patientService.getPatient(Integer.parseInt(patient)).getUuid();
        }

        List<Fingerprint> fingerprints = ugandaEMRFingerprintService.getPatientFingerprint(patient);

        List<GlobalProperty> globalProperties = Context.getAdministrationService().getGlobalPropertiesByPrefix("ugandaemrfingerprint.enable");

        Map map = new HashMap();

        for (GlobalProperty globalProperty : globalProperties) {
            if (Boolean.parseBoolean(globalProperty.getPropertyValue())) {
                switch (globalProperty.getProperty()) {
                    case "ugandaemrfingerprint.enableRightPinky":
                        map.put(RIGHT_PINKY, RIGHT_PINKY_NAME);
                        break;
                    case "ugandaemrfingerprint.enableRightRing":
                        map.put(RIGHT_RING, RIGHT_RING_NAME);
                        break;
                    case "ugandaemrfingerprint.enableRightMiddle":
                        map.put(RIGHT_MIDDLE, RIGHT_MIDDLE_NAME);
                        break;
                    case "ugandaemrfingerprint.enableRightIndex":
                        map.put(RIGHT_INDEX, RIGHT_INDEX_NAME);
                        break;
                    case "ugandaemrfingerprint.enableRightThumb":
                        map.put(RIGHT_THUMB, RIGHT_THUMB_NAME);
                        break;
                    case "ugandaemrfingerprint.enableLeftPinky":
                        map.put(LEFT_PINKY, LEFT_PINKY_NAME);
                        break;
                    case "ugandaemrfingerprint.enableLeftRing":
                        map.put(LEFT_RING, LEFT_RING_NAME);
                        break;
                    case "ugandaemrfingerprint.enableLeftMiddle":
                        map.put(LEFT_MIDDLE, LEFT_MIDDLE_NAME);
                        break;
                    case "ugandaemrfingerprint.enableLeftIndex":
                        map.put(LEFT_INDEX, LEFT_INDEX_NAME);
                        break;
                    case "ugandaemrfingerprint.enableLeftThumb":
                        map.put(LEFT_THUMB, LEFT_THUMB_NAME);
                        break;
                    default: ;
                        break;
                }
            }
        }

        for (Fingerprint fingerprint : fingerprints) {
            map.remove(fingerprint.getFinger());
        }

        model.addAttribute("fingers", map);

        model.addAttribute("noOFFingerprint", fingerprints.size());

        model.addAttribute("fingerPrintEnrolled", fingerprints.size());
    }

    public SimpleObject saveFingerprint(FragmentModel model, @RequestParam(value = "patient", required = false) String patient, @RequestParam(value = "finger", required = false) Integer finger, @RequestParam(value = "fingerprint", required = false) String fingerprint, UiUtils ui) {


        SimpleObject obj = new SimpleObject();
        UgandaEMRFingerprintService ugandaEMRFingerprintService = Context.getService(UgandaEMRFingerprintService.class);

        Date dateCreated = new Date();

        Fingerprint fingerprint1 = new Fingerprint();
        fingerprint1.setFinger(finger);
        fingerprint1.setPatient(patient);
        fingerprint1.setFingerprint(fingerprint);
        fingerprint1.setDateCreated(dateCreated);

        ugandaEMRFingerprintService.savePatientFingerprint(fingerprint1);

        obj.put("message", "Patient fingerprint saved");

        return obj;
    }

    public SimpleObject deleteFingerprint(@RequestParam(value = "patient", required = false) String patient, UiUtils ui) {
        SimpleObject obj = new SimpleObject();
        UgandaEMRFingerprintService ugandaEMRFingerprintService = Context.getService(UgandaEMRFingerprintService.class);
        ugandaEMRFingerprintService.deletePatientFingerPrint(patient);
        obj.put("result", "Patient fingerprint Deleted");
        return obj;
    }
}
