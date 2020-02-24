/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.ugandaemrfingerprint;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.dataexchange.DataImporter;
import org.openmrs.module.ugandaemrfingerprint.core.FingerPrintMetadataBundle;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.ugandaemrfingerprint.remoteserver.FingerPrintGlobalProperties;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class UgandaEMRFingerprintActivator implements ModuleActivator {

    protected Log log = LogFactory.getLog(getClass());

    /**
     * @see ModuleActivator#willRefreshContext()
     */
    public void willRefreshContext() {
        log.info("Refreshing UgandaEMR FingerPrint Module");
    }

    /**
     * @see ModuleActivator#contextRefreshed()
     */
    public void contextRefreshed() {
        log.info("UgandaEMR FingerPrint Module refreshed");
    }

    /**
     * @see ModuleActivator#willStart()
     */
    public void willStart() {
        log.info("Starting UgandaEMR FingerPrint Module");
    }

    /**
     * @see ModuleActivator#started()
     */
    public void started() {

        MetadataDeployService deployService = Context.getService(MetadataDeployService.class);
        FingerPrintGlobalProperties fingerPrintGlobalProperties = new FingerPrintGlobalProperties();
        try {
            fingerPrintGlobalProperties.setFingerPrintGlobalProperties();
            installCommonMetadata(deployService);
            log.info("UgandaEMR FingerPrint Module started");
        } catch (Exception e) {
            log.error(e);
        }


    }

    private void installCommonMetadata(MetadataDeployService deployService) {
        try {
            log.info("Installing metadata");
            log.info("Installing commonly used metadata");
            deployService.installBundle(Context.getRegisteredComponents(FingerPrintMetadataBundle.class).get(0));
            log.info("Finished installing commonly used metadata");
            log.info("Setting Global Properties For Sync Module");


        } catch (Exception e) {
            Module mod = ModuleFactory.getModuleById("ugandaemrfingerprint");
            ModuleFactory.stopModule(mod);
            throw new RuntimeException("failed to install the common metadata ", e);
        }
    }


    /**
     * @see ModuleActivator#willStop()
     */
    public void willStop() {
        log.info("Stopping UgandaEMR FingerPrint Module");
    }

    /**
     * @see ModuleActivator#stopped()
     */
    public void stopped() {
        log.info("UgandaEMR FingerPrint Module stopped");
    }

}
