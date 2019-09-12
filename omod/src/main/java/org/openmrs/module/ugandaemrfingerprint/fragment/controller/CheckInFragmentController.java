package org.openmrs.module.ugandaemrfingerprint.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.VisitType;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.coreapps.fragment.controller.visit.QuickVisitFragmentController;
import org.openmrs.module.emrapi.adt.AdtService;
import org.openmrs.module.patientqueueing.api.PatientQueueingService;
import org.openmrs.module.patientqueueing.mapper.PatientQueueMapper;
import org.openmrs.module.patientqueueing.model.PatientQueue;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

public class CheckInFragmentController {

    protected final Log log = LogFactory.getLog(getClass());

    public CheckInFragmentController() {
    }

    public void controller(@SpringBean FragmentModel pageModel, @SpringBean("patientService") PatientService patientService, @SpringBean("locationService") LocationService locationService, @RequestParam(value = "patientId", required = false) Patient patient, UiSessionContext uiSessionContext) {
        if (patient != null) {
            pageModel.put("birthDate", patient.getBirthdate());
            pageModel.put("patient", patient);
            pageModel.put("patientId", patient.getPatientId());
        }
        pageModel.put("locationList", ((Location) locationService.getRootLocations(false).get(0)).getChildLocations());
        pageModel.put("providerList", Context.getProviderService().getAllProviders(false));
    }

    public SimpleObject post(@SpringBean("patientService") PatientService patientService, @RequestParam(value = "patientId") Patient patient, @RequestParam(value = "providerId", required = false) Provider provider, UiUtils ui, @RequestParam("locationId") Location location, @RequestParam(value = "returnUrl", required = false) String returnUrl, UiSessionContext uiSessionContext, UiUtils uiUtils, HttpServletRequest request) throws IOException {
        PatientQueue patientQueue = new PatientQueue();
        PatientQueueingService patientQueueingService = Context.getService(PatientQueueingService.class);
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleObject simpleObject=new SimpleObject();

        patientQueue.setLocationFrom(uiSessionContext.getSessionLocation());
        patientQueue.setPatient(patient);
        patientQueue.setLocationTo(location);
        patientQueue.setProvider(provider);
        patientQueue.setStatus("pending");
        patientQueue.setCreator(uiSessionContext.getCurrentUser());
        patientQueue.setDateCreated(new Date());
        patientQueueingService.savePatientQue(patientQueue);

        if (Context.getVisitService().getActiveVisitsByPatient(patient).size() <= 0) {
            QuickVisitFragmentController quickVisitFragmentController = new QuickVisitFragmentController();
            quickVisitFragmentController.create((AdtService) Context.getService(AdtService.class), Context.getVisitService(), patient, location, uiUtils, getFacilityVisitType(), uiSessionContext, request);
        }
        simpleObject.put("patientQueue",objectMapper.writeValueAsString(mapPatientQueueToMapper(patientQueue)));
        return simpleObject;

    }

    private VisitType getFacilityVisitType() {
        String visitTypeUUID = Context.getAdministrationService().getGlobalProperty("patientqueueing.defaultFacilityVisitTypeUUID");
        return Context.getVisitService().getVisitTypeByUuid(visitTypeUUID);
    }

    private PatientQueueMapper mapPatientQueueToMapper(PatientQueue patientQueue) {
        PatientQueueMapper patientQueueMapper = new PatientQueueMapper();
        if (patientQueue != null) {
            String names = patientQueue.getPatient().getFamilyName() + " " + patientQueue.getPatient().getGivenName() + " " + patientQueue.getPatient().getMiddleName();

            patientQueueMapper.setId(patientQueue.getId());
            patientQueueMapper.setPatientNames(names.replace("null", ""));
            patientQueueMapper.setPatientId(patientQueue.getPatient().getPatientId());
            patientQueueMapper.setLocationFrom(patientQueue.getLocationFrom().getName());
            patientQueueMapper.setLocationTo(patientQueue.getLocationTo().getName());
            if(patientQueue.getProvider()!=null) {
                patientQueueMapper.setProviderNames(patientQueue.getProvider().getName());
            }
            patientQueueMapper.setStatus(patientQueue.getStatus());
            patientQueueMapper.setAge(patientQueue.getPatient().getAge().toString());
            patientQueueMapper.setDateCreated(patientQueue.getDateCreated().toString());
        }
        return patientQueueMapper;
    }
}
