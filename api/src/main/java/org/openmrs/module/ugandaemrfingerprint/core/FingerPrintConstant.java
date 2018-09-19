package org.openmrs.module.ugandaemrfingerprint.core;

/**
 * Created by lubwamasamuel on 23/02/2017.
 */
public class FingerPrintConstant {
    public static String FINGER_PRINT_ATTRIBUTE_NAME = "FingerPrint";
    public static String FINGER_PRINT_ATTRIBUTE_DESCRIPTION = "This attribute is the type for person attributed which stores the ugandaemrfingerprint template";
    public static String FINGER_PRINT_ATTRIBUTE_UUID = "a41339f9-5014-45f4-91d6-bab84c6c62f1";


    public static final String CONNECTION_SERVER_IP_GLOBALPROPERTY = "ugandaemrsync.serverIP";

    public static final String CONNECTION_PROTOCOL = "http://";

    public static final int CONNECTION_SUCCESS = 200;

    public static final String XML_CONTENT_TYPE = "application/xml";

    public static final String NORMAL_CONTENT_TYPE = "application/x-wwww-form-urlencoded";

    public static final String JSON_CONTENT_TYPE = "application/json";

    public static final String SEARCH_URL = "/api/query";

    public static final String FACILITY_NAME = "facility.name";
    public static final String FACILITY_ID = "facility.id";
    public static final String PATIENT_ID = "patient.id";

    public static final String PATIENT_UUID_SEARCH_STRING = "{patient(patients:[\"%s\"]){uuid birthdate gender dead patientFacility{ uuid name} names{ familyName middleName givenName voided} summaryPage{obs {uuid concept encounterDate encounterType valueCoded valueText valueNumeric valueDatetime valueDrug valueBoolean valueComplex voided }} mostRecentEncounter{obs { uuid concept encounterDate encounterType valueCoded valueText valueNumeric valueDatetime valueDrug valueBoolean valueComplex voided}}}}";
    public static final String PATIENT_NATIONAL_ID_SEARCH_STRING = "{patient(identifier:{t:\"f0c16a6d-dc5f-4118-a803-616d0075d282\",v:\"%s\"}){uuid birthdate gender dead patientFacility{ uuid name} names{ familyName middleName givenName voided} summaryPage{obs {uuid concept encounterDate encounterType valueCoded valueText valueNumeric valueDatetime valueDrug valueBoolean valueComplex voided }} mostRecentEncounter{obs { uuid concept encounterDate encounterType valueCoded valueText valueNumeric valueDatetime valueDrug valueBoolean valueComplex voided}}}}";
    public static final String PATIENT_ONLINE_ID = "identifiers";
    public static final String PATIENT_NOT_FOUND = "Patient Not Found";
    public static final String CONNECTION_TEST_IP = "google.com/";
    public static final String DEVICE_SOCKET_IP = "ugandaemrfingerprint.socketIPAddress";
    public static final String DEVICE_SOCKET_IP_PLACE_HOLDER = "http://localhost:8084";

    public static final String ONLINE_SEARCH_ENABLE_DISABLE = "ugandaemrfingerprint.searchOnline";
    public static final String ONLINE_SEARCH_ENABLE_DISABLE_PLACE_HOLDER = "false";

    public static final String SHOW_PATIENT_CLINIC_SUMMARY = "ugandaemrfingerprint.showOnlinePatientSummary";
    public static final String SHOW_PATIENT_CLINIC_SUMMARY_PLACE_HOLDER = "false";

    public static final String SHOW_PATIENT_LAST_TREATMENT_ENCOUNTER = "ugandaemrfingerprint.showOnlinePatientLastTreatmentEncounter";
    public static final String SHOW_PATIENT_LAST_TREATMENT_ENCOUNTER_PLACE_HOLDER = "false";


    public static final int LEFT_PINKY = 0;
    public static final int LEFT_RING = 1;
    public static final int LEFT_MIDDLE = 2;
    public static final int LEFT_INDEX = 3;
    public static final int LEFT_THUMB = 4;
    public static final int RIGHT_THUMB = 5;
    public static final int RIGHT_INDEX = 6;
    public static final int RIGHT_MIDDLE = 7;
    public static final int RIGHT_RING = 8;
    public static final int RIGHT_PINKY = 9;







    public static final String RIGHT_PINKY_NAME ="Right Pinky"  ;
    public static final String RIGHT_RING_NAME = "Right Ring";
    public static final String RIGHT_MIDDLE_NAME="Right Middle";
    public static final String RIGHT_INDEX_NAME ="Right Index";
    public static final String RIGHT_THUMB_NAME ="Right Thumb";
    public static final String LEFT_PINKY_NAME = "Left Pinky";
    public static final String LEFT_RING_NAME = "Left Ring";
    public static final String LEFT_MIDDLE_NAME ="Left Middle";
    public static final String LEFT_INDEX_NAME = "Left Index"; 
    public static final String LEFT_THUMB_NAME = "Left Thumb";



    public  static final String PATIENT_NAMES="names";
    public  static final String PATIENT_FAMILY_NAME="familyName";
    public  static final String PATIENT_MIDDLE_NAME="middleName";
    public  static final String PATIENT_GIVEN_NAME="givenName";

    public  static final String PATIENT_GENDER="gender";
    public  static final String PATIENT_BIRTH_DATE="birthdate";
    public  static final String PATIENT_FACILITY_NAME="patientFacility";
    public  static final String PATIENT_SUMMARY="summaryPage";
    public  static final String PATIENT_LAST_ENCOUNTER="mostRecentEncounter";

    public  static final String NAME="name";
    public  static final String DATE_OF_BIRTH="birthdate";
    public  static final String GENDER="gender";
    public  static final String UUID_STRING ="uuid";
    public  static final String CONCEPT ="concept";
    public  static final String ENCOUNTER_DATE="encounterDate";
    public  static final String ENCOUNTER_TYPE="encounterType";
    public  static final String VALUE_CODED="valueCoded";
    public  static final String VALUE_TEXT="valueText";
    public  static final String VALUE_NUMERIC="valueNumeric";
    public  static final String VALUE_DATE_TIME="valueDatetime";
    public  static final String VALUE_DRUG="valueDrug";
    public  static final String VALUE_BOOLEAN="valueBoolean";
    public  static final String VALUE_COMPLEX="valueComplex";
    public  static final String VOIDED="voided";
    public  static final String OBS="obs";
}
