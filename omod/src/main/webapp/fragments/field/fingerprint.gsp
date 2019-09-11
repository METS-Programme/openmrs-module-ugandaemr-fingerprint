
<script type="text/javascript">


            function writeToFingerPrintTextbox(fingerPrintTemplate) {
           
            var fingerPrintTextbox=document.getElementById('fingerPrintTextbox');
            fingerPrintTextbox.value = fingerPrintTemplate;
        }

</script>
<p>
    <label>
        ${config.label}
    </label>
   <applet width="600" height="300" archive="finger-print-applet.jar" code="org.openmrs.module.ugandaemrfingerprint.applet.PatientEnrollmentApplet.class" codebase="/openmrs" name="fingerApplet">
	    <param value="16" name="patientId"></param>
	    <param value="false" name="codebase_lookup"></param>
    </applet>
    <span class="field-error"></span>
    <textarea rows="4" cols="50" id="fingerPrintTextbox" name="fingerPrintTextbox" style="display:none;"></textarea>
</p>