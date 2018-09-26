<%
    ui.includeJavascript("ugandaemrfingerprint", "patientsearch/sockjs-0.3.4.js")
    ui.includeJavascript("ugandaemrfingerprint", "patientsearch/stomp.js")
    ui.decorateWith("appui", "standardEmrPage", [title: ui.message("Patients From Other Health Centers")])

    def htmlSafeId = { extension ->
        "${extension.id.replace(".", "-")}-${extension.id.replace(".", "-")}-extension"
    }

    def breadcrumbMiddle = breadcrumbOverride ?: '';
%>
<script type="text/javascript">
    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {
            label: "${ ui.escapeJs(ui.message("Find Patient From other Facility.")) }",
        }
    ];

    if (jQuery) {
        jq(document).ready(function () {
            jq("#tabs").tabs();

            hideContainer("#art_summary_header");
            hideContainer("#art_latest_encounter_header");
            if ("${patientId}" != "") {
                remoteSearch('fingerprint', "${patientId}");
            }

        });
    }

    function remoteSearch(search_type, search_params) {

        jq("patient_found").hide();

        if (!search_params) {
            search_params = jq("#onlinesearch").val();
        }

        var searchQuery = '${searchString}'.replace('%s', search_params);
        var patientDetailssearchQuery = '${patientDetails}'.replace('%s', search_params);
        var patientMostRecentEncounterSearchQuery = '${patientMostRecentEncounterSearchQuery}'.replace('%s', search_params);
        var patientSummarySearchQuery = '${patientSummarySearchQuery}'.replace('%s', search_params);

        if (search_type !== 'fingerprint') {
            searchQuery = '${nationalIdString}'.replace('%s', search_params);
        }

        jq("#status_message").html("");
        if (search_params != "") {
            jq("#patient_found").addClass('hidden');
            if (navigator.onLine) {
                jq.post("${connectionProtocol+onlineIpAddress+queryURL}", {query: patientDetailssearchQuery},
                        function (response) {

                            if (response && response.data.patient !== null) {
                                displayData(response);
                                jq().toastmessage('showSuccessToast', "Patient Found");
                            }
                            else if (response.errors) {
                                jq().toastmessage('showErrorToast', "Internal Server Error");
                            }
                            else {
                                jq().toastmessage('showErrorToast', "Patient Not Found");
                                jq("#patient_found").attr("display", "none");
                            }
                        });

                if(${displayMostRecentEncounter}){
                jq.post("${connectionProtocol+onlineIpAddress+queryURL}", {query: patientMostRecentEncounterSearchQuery},
                    function (response) {
                        if (response && response.data.patient !== null) {
                            jq.post('${ ui.actionLink("processPatientEncounters") }', {
                                patientData: JSON.stringify(response.data.patient)
                            }, function (response) {
                                var responseData = JSON.parse(response.replace("patientData=", "\"patientData\":").trim());
                                patientClinicInfo(responseData);
                            });
                        }
                    });
                }

                if(${displaySummaryPage}){
                jq.post("${connectionProtocol+onlineIpAddress+queryURL}", {query: patientSummarySearchQuery},
                    function (response) {
                        if (response && response.data.patient !== null) {
                            jq.post('${ ui.actionLink("processPatientEncounters") }', {
                                patientData: JSON.stringify(response.data.patient)
                            }, function (response) {
                                var responseData = JSON.parse(response.replace("patientData=", "\"patientData\":").trim());
                                patientClinicInfo(responseData);
                            });
                        }
                    });
                }
            } else {
                jq().toastmessage('showErrorToast', "No internet Connection");
            }
        }
        else {
            jq().toastmessage('showWarningToast', "No Search Parameters typed in the Search box");
        }
    }

    function formatDate(date) {
        var monthNames = [
            "January", "February", "March",
            "April", "May", "June", "July",
            "August", "September", "October",
            "November", "December"
        ];

        var day = date.getDate();
        var monthIndex = date.getMonth();
        var year = date.getFullYear();

        return day + ' ' + monthNames[monthIndex] + ' ' + year;
    }

    function displayData(response) {
        jq("#patient_found").removeClass('hidden');
        jq("patient_found").show();
        var patientNames = "" + response.data.patient.names[0].familyName + " " + response.data.patient.names[0].middleName + " " + response.data.patient.names[0].givenName;
        patientNames = patientNames.replace("null", "");
        jq("#patientNames").html(patientNames);
        jq("#patientId").html(response.data.patient.uuid);
        jq("#age").html(response.data.patient.age);
        jq("#gender").html(patientNames);
        jq("#facilityName").html(response.data.patient.patientFacility.name);
        jq("#facilityNameHeader").html("At " + response.data.patient.patientFacility.name);
        jq("#facilityId").html(response.data.patient.patientFacility.uuid);
        var dateOfBirth = formatDate(new Date(response.data.patient.birthdate));
        jq("#birthDate").html(dateOfBirth + "");
        jq("#gender").html(response.data.patient.gender);

        "${patientFound=true}";
        "${searched=true}";
        "${connectionStatus=""}";
    }

    function patientClinicInfo(response) {
        if (response.patientData != "") {
            if (${showOnlinePatientSummary} === true){
                if (response.patientData.obsSummaryPageList !== null) {
                    showContainer("#art_summary_header");
                    jq().toastmessage('showSuccessToast', "Patient Summary Loaded Successful");
                    var txt1 = "";
                    txt1 += "<div id=\"tabs-1\" style=\"background: #B6D6E6;\" class=\"encounter-summary-container\">";
                    txt1 += "<table>";
                    for (x in response.patientData.obsSummaryPageList) {
                        txt1 += "<tr><td>" + response.patientData.obsSummaryPageList[x].concept + "</td><td>" + response.patientData.obsSummaryPageList[x].answerSummary + "</td></tr>";
                    }
                    txt1 += "</table>";
                    txt1 += "</div>";
                    jq("#tabs").append(txt1);
                }
                else {
                    var txt1 = "";
                    txt1 += "<div id=\"tabs-1\" style=\"background: #B6D6E6;\" class=\"encounter-summary-container\">";
                    txt1 += "</div>";
                    jq("#tabs").append(txt1);
                }
            }

            if (${showOnlinePatientLastTreatmentEncounter} === true){
                if (response.patientData.obsLastEncounterPageList != null) {
                    showContainer("#art_latest_encounter_header");
                    jq().toastmessage('showSuccessToast', "Last Patient Encounter Loaded Successful");
                    var txt2 = "";
                    txt2 += "<div id=\"tabs-2\" style=\"background: #B6D6E6;\" class=\"encounter-summary-container\">";
                    txt2 += "<table>";
                    for (x in response.patientData.obsLastEncounterPageList) {
                        txt2 += "<tr><td>" + response.patientData.obsLastEncounterPageList[x].concept + "</td><td>" + response.patientData.obsLastEncounterPageList[x].answerSummary + "</td></tr>";
                    }
                    txt2 += "</table>";
                    txt2 += "</div>";
                    jq("#tabs").append(txt2);
                }
                else {
                    var txt2 = "";
                    txt2 += "<div id=\"tabs-2\" style=\"background: #B6D6E6;\" class=\"encounter-summary-container\">";
                    txt2 += "</div>";
                    jq("#tabs").append(txt2);
                }
            }
        }
    }

    jq(function () {
        jq("#tabs").tabs();
    });
</script>
<style type="text/css">
#death-date-display {
    min-width: 35%;
}

span.field-error {
    padding: 1px 6px 1px 6px;
    margin-left: 4px;
    margin-right: 4px;
    vertical-align: middle;
    color: red;
}

img {
    width: 100px;
    height: auto;
}

.online_search_input_box {
    max-width: 94%;
    height: 34px;
}

.search_container {
    width: 65%;
}

.toast-position-top-right {
    position: fixed;
    top: 165px;
    right: 70px;
}

.dialog .dialog-header, .ngdialog.ngdialog-theme-default .ngdialog-content .dialog-header {
    background: #000;
}

.column-style {

}
</style>

<h3>Locate Patient From other Facility</h3>

<div id="status_message"></div>
<% if (patientFound == true && searched == true) { %>
<div id="patient_found" class="dialog" style="width: 100%">
    <div class="dialog-header">
        <i class="icon-globe"></i>

        <h3>Patient Found: <span id="facilityNameHeader"></span></h3>
    </div>

    <div class="dialog-content">
        <div>
            <strong>Patient Names:</strong> <span style="" id="patientNames"></span>
        </div>

        <div>
            <strong>Sex:</strong><span id="gender"></span>
        </div>

        <div>
            <strong>Date of Birth:</strong><span id="birthDate"></span>
        </div>

        <div>
            <strong>Facility Name:</strong><span id="facilityName"></span>
        </div>
    </div>
    <div id="tabs" style="margin-left: auto; margin-right: auto;width: 95%;margin-top: 20px;">
        <ul>
            <li id="art_summary_header"><a href="#tabs-1">Patient Clinic Summary</a></li>
            <li id="art_latest_encounter_header"><a href="#tabs-2">Last Patient Encounter</a></li>
        </ul>
    </div>
</div>
<% } %>