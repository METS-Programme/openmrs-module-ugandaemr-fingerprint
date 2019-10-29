/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ugandaemrfingerprint;

import org.json.JSONObject;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.ugandaemrfingerprint.core.Facility;
import org.openmrs.module.ugandaemrfingerprint.core.PatientInOtherFacility;
import org.openmrs.module.ugandaemrfingerprint.core.PatientName;
import org.springframework.transaction.annotation.Transactional;
import org.openmrs.module.ugandaemrfingerprint.model.Fingerprint;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The main service of this module, which is exposed for other modules. See
 * moduleApplicationContext.xml on how it is wired up.
 */
@Transactional
public interface UgandaEMRFingerprintService extends OpenmrsService{

    @Authorized(UgandaEMRFingerprintConfig.MODULE_PRIVILEGE)

    public List<Fingerprint> getPatientFingerprint(String patientId);

    public List<Fingerprint> getPatientFingerprints();

    public void deletePatientFingerPrint(String patientId);

    public void savePatientFingerprint(Fingerprint fingerprint);

    public boolean isUUID(String uuid);

    public PatientInOtherFacility getPatientInOtherFacility(JSONObject jsonObject);

}
