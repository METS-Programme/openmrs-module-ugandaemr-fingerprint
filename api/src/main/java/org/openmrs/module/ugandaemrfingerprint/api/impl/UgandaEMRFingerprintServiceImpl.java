/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ugandaemrfingerprint.api.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ugandaemrfingerprint.UgandaEMRFingerprintService;
import org.openmrs.module.ugandaemrfingerprint.api.db.UgandaEMRFingerprintDao;
import org.openmrs.module.ugandaemrfingerprint.core.Facility;
import org.openmrs.module.ugandaemrfingerprint.core.FingerPrintConstant;
import org.openmrs.module.ugandaemrfingerprint.core.PatientInOtherFacility;
import org.openmrs.module.ugandaemrfingerprint.core.PatientName;
import org.openmrs.module.ugandaemrfingerprint.model.Fingerprint;
import org.openmrs.module.ugandaemrfingerprint.model.PatientOb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.openmrs.module.ugandaemrfingerprint.core.FingerPrintConstant.*;

public class UgandaEMRFingerprintServiceImpl extends BaseOpenmrsService implements UgandaEMRFingerprintService {


    private UgandaEMRFingerprintDao dao;

    public UgandaEMRFingerprintServiceImpl() {
    }

    private UgandaEMRFingerprintDao getUgandaEMRFingerprintDAO() {
        return dao;
    }

    public void setUgandaEMRFingerprintDAO(UgandaEMRFingerprintDao dao) {
        this.dao = dao;
    }

    private Log log = LogFactory.getLog(this.getClass());

    public void setDao(UgandaEMRFingerprintDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Fingerprint> getPatientFingerprint(String patientId) {
        return dao.getPatientFingerPrint(patientId);
    }

    @Override
    public List<Fingerprint> getPatientFingerprints() {
        return dao.getPatientFingerPrints();
    }

    @Override
    public void deletePatientFingerPrint(String patientId) {
        dao.deletePatientFingerPrint(patientId);
    }

    @Override
    public void savePatientFingerprint(Fingerprint fingerprint) {
        dao.savePatientFingerprint(fingerprint);
    }

    @Override
    public boolean isUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    private List<Facility> getOtherClientHealthCenters(List<Map> healthCenters) {

        List<Facility> otherHealthCenters = new ArrayList<Facility>();

        for (Map healthCenter : healthCenters) {
            Facility otherHealthCenter = new Facility();

            otherHealthCenter.setFacilityId(healthCenter.get("id").toString());
            otherHealthCenter.setName(healthCenter.get("name").toString());
            otherHealthCenters.add(otherHealthCenter);
        }
        return otherHealthCenters;
    }


    public PatientInOtherFacility getPatientInOtherFacility(JsonObject client) {

        List<PatientName> patientNames = new ArrayList<PatientName>();

        patientNames = getPatientNames(client.get(PATIENT_NAMES).getAsJsonArray().get(0).getAsJsonObject());

        Facility facilities = getPatientFacility(client.get(PATIENT_FACILITY_NAME).getAsJsonObject());

        List<PatientOb> patientSummary = null;


        if (client.get(PATIENT_SUMMARY)!=null && client.get(PATIENT_SUMMARY).isJsonObject()) {
            try {
                patientSummary = getEncounter(client.get(PATIENT_SUMMARY).getAsJsonObject().get(OBS).getAsJsonArray());
            } catch (Exception e) {
                log.error(e);
            }
        }

        List<PatientOb> lastEncounter = null;
        if (client.get(PATIENT_LAST_ENCOUNTER)!=null && client.get(PATIENT_LAST_ENCOUNTER).isJsonObject()) {
            try {
                lastEncounter = getEncounter(client.get(PATIENT_LAST_ENCOUNTER).getAsJsonObject().get(OBS).getAsJsonArray());
            } catch (Exception e) {
                log.error(e);
            }

        }

        PatientInOtherFacility patientInOtherFacility = new PatientInOtherFacility(cleanRecord(client.get(DATE_OF_BIRTH).toString()), cleanRecord(client.get(GENDER).toString()), patientNames, facilities, patientSummary, lastEncounter);

        return patientInOtherFacility;
    }


    private java.lang.String getConcept(String s) {
        ConceptService conceptService = Context.getConceptService();
        Concept concept = new Concept();
        String conceptName = "";
        if (s != "" || s != "null" || s != null) {
            concept = conceptService.getConceptByUuid(java.lang.String.valueOf(s));

            if (concept != null) {
                conceptName = concept.getName().getName();
            }
        }
        return conceptName;
    }

    private String getEncounterType(String s) {
        EncounterService encounterService = Context.getEncounterService();
        EncounterType encounterType = new EncounterType();
        encounterType = encounterService.getEncounterTypeByUuid(s);
        String ecounterTypeName = "";
        if (encounterType != null) {
            ecounterTypeName = encounterType.getName();
        }
        return ecounterTypeName;
    }

    private List<PatientName> getPatientNames(JsonObject jsonObject) {
        List<PatientName> patientNames = new ArrayList<PatientName>();
        PatientName patientName = new PatientName();
        patientName.setFamilyName(jsonObject.get(PATIENT_FAMILY_NAME).toString());

        if (jsonObject.get(PATIENT_MIDDLE_NAME) != null) {
            patientName.setMiddleName(jsonObject.get(PATIENT_MIDDLE_NAME).toString());
        }
        patientName.setGivenName(jsonObject.get(PATIENT_GIVEN_NAME).toString());
        patientNames.add(patientName);
        return patientNames;
    }

    private Facility getPatientFacility(JsonObject map) {
        Facility facility = new Facility();
        facility.setName(map.get(NAME).toString());
        facility.setFacilityId(map.get(FingerPrintConstant.UUID_STRING).toString());
        return facility;
    }


    private List<PatientOb> getEncounter(JsonArray jsonObject) {
        List<PatientOb> obsList = new ArrayList<PatientOb>();

        try {
            for (int i = 0; i <= jsonObject.size(); i++) {
                PatientOb patientOb = new PatientOb();
                JsonObject jsonObject1 = jsonObject.get(i).getAsJsonObject();
                patientOb.setEncounterDate(jsonObject1.get(ENCOUNTER_DATE).toString());
                patientOb.setEncounterType((String) getEncounterType(cleanRecord(jsonObject1.get(ENCOUNTER_TYPE).toString().replace("\"", ""))));
                patientOb.setValueBoolean((String) jsonObject1.get(VALUE_BOOLEAN).toString().replace("\"", ""));
                patientOb.setValueText((String) jsonObject1.get(VALUE_TEXT).toString().replace("\"", ""));
                patientOb.setValueDatetime((String) jsonObject1.get(VALUE_DATE_TIME).toString().replace("\"", ""));
                patientOb.setValueNumeric((String) jsonObject1.get(VALUE_NUMERIC).toString().replace("\"", ""));
                patientOb.setValueComplex((String) jsonObject1.get(VALUE_COMPLEX).toString().replace("\"", ""));
                patientOb.setValueCoded((String) getConcept(jsonObject1.get(VALUE_CODED).toString().replace("\"", "")));
                patientOb.setValueDrug((String) getConcept(jsonObject1.get(VALUE_DRUG).toString().replace("\"", "")));
                patientOb.setUuid((String) jsonObject1.get(FingerPrintConstant.UUID_STRING).toString().replace("\"", ""));
                patientOb.setConcept((String) getConcept(jsonObject1.get(FingerPrintConstant.CONCEPT).toString().replace("\"", "")));

                String answerSummary = "" + patientOb.valueCoded + " " + patientOb.valueText + " " + patientOb.valueNumeric + " " + patientOb.valueDatetime + " " + patientOb.valueDrug + " " + patientOb.valueBoolean + " " + patientOb.valueComplex + "";

                patientOb.setAnswerSummary(answerSummary.replace("null", "").trim());
                obsList.add(patientOb);
            }
        } catch (Exception e) {

        }
        return obsList;
    }

    private String cleanRecord(String jsonObject) {
        return (String) jsonObject.replace("\"", "");
    }

}
