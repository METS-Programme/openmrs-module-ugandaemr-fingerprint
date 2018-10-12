package org.openmrs.module.ugandaemrfingerprint.fragment.controller.patientsearch;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.ugandaemrfingerprint.core.*;
import org.openmrs.Patient;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.coreapps.CoreAppsConstants;
import org.openmrs.module.emrapi.utils.GeneralUtils;
import org.openmrs.module.ugandaemrfingerprint.remoteserver.FingerPrintGlobalProperties;
import org.openmrs.ui.framework.UiFrameworkConstants;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.util.OpenmrsConstants;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static org.openmrs.module.ugandaemrfingerprint.core.FingerPrintConstant.*;

/**
 * Fragment controller for patient search widget; sets the min # of search characters based on global property,
 * and loads last viewed patients for current user if "showLastViewedPatients" fragment config param=true
 */
public class PatientSearchWidgetFragmentController {


    public void controller(FragmentModel model, UiSessionContext sessionContext, HttpServletRequest request, @SpringBean("adminService") AdministrationService administrationService, @FragmentParam(value = "showLastViewedPatients", required = false) Boolean showLastViewedPatients, @FragmentParam(value = "initialSearchFromParameter", required = false) String searchByParam) {

        showLastViewedPatients = showLastViewedPatients != null ? showLastViewedPatients : false;

        model.addAttribute("minSearchCharacters", administrationService.getGlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_MIN_SEARCH_CHARACTERS, "1"));

        model.addAttribute("searchDelayShort", administrationService.getGlobalProperty(CoreAppsConstants.GP_SEARCH_DELAY_SHORT, "300"));

        model.addAttribute("searchDelayLong", administrationService.getGlobalProperty(CoreAppsConstants.GP_SEARCH_DELAY_LONG, "1000"));

        model.addAttribute("dateFormatJS", "DD MMM YYYY");
        model.addAttribute("locale", Context.getLocale().getLanguage());
        model.addAttribute("defaultLocale", new Locale(administrationService.getGlobalProperty((OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_LOCALE), "en")).getLanguage());
        model.addAttribute("dateFormatter", new SimpleDateFormat(administrationService.getGlobalProperty(UiFrameworkConstants.GP_FORMATTER_DATE_FORMAT), Context.getLocale()));
        model.addAttribute("showLastViewedPatients", showLastViewedPatients);

        String doInitialSearch = null;
        if (searchByParam != null && StringUtils.isNotEmpty(request.getParameter(searchByParam))) {
            doInitialSearch = request.getParameter(searchByParam);
        }
        model.addAttribute("doInitialSearch", doInitialSearch);

        FingerPrintGlobalProperties fingerPrintGlobalProperties = new FingerPrintGlobalProperties();

        model.addAttribute("searchOnline", fingerPrintGlobalProperties.getGlobalProperty(FingerPrintConstant.ONLINE_SEARCH_ENABLE_DISABLE));
        model.addAttribute("simpleNationalIdString", PATIENT_NATIONAL_ID_SIMPLE_SEARCH_STRING);
        model.addAttribute("onlineIpAddress", fingerPrintGlobalProperties.getGlobalProperty(FingerPrintConstant.CONNECTION_SERVER_IP_GLOBALPROPERTY));
        model.addAttribute("connectionProtocol", CONNECTION_PROTOCOL);
        model.addAttribute("queryURL", FingerPrintConstant.SEARCH_URL);

        if (showLastViewedPatients) {
            List<Patient> patients = GeneralUtils.getLastViewedPatients(sessionContext.getCurrentUser());
            model.addAttribute("lastViewedPatients", patients);
        }

    }
}