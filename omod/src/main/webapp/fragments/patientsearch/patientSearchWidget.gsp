<%
    config.require("afterSelectedUrl")
    def breadcrumbOverride = config.breadcrumbOverride ?: ""
    ui.includeCss("uicommons", "datatables/dataTables_jui.css")
    ui.includeCss("ugandaemrfingerprint", "patientsearch/patientSearchWidget.css")
    ui.includeCss("ugandaemrfingerprint", "patientsearch/fontcustom_findpatient_fingerprint.css")
    ui.includeJavascript("uicommons", "datatables/jquery.dataTables.min.js")
    ui.includeJavascript("ugandaemrfingerprint", "patientsearch/patientSearchWidget.js")
    ui.includeJavascript("ugandaemrfingerprint", "patientsearch/patientFingerPrintSearch.js")
    ui.includeJavascript("uicommons", "moment-with-locales.min.js")
    ui.includeJavascript("ugandaemrfingerprint", "patientsearch/sockjs-0.3.4.js")
    ui.includeJavascript("ugandaemrfingerprint", "patientsearch/stomp.js")
%>
<style type="text/css">
img {
    width: 100px;
    height: auto;
}
</style>
<script type="text/javascript">
    var lastViewedPatients = [];
    function handlePatientRowSelection() {
        this.handle = function (row) {
            var uuid = row.uuid;
            location.href = '/' + OPENMRS_CONTEXT_PATH + emr.applyContextModel('${ ui.escapeJs(config.afterSelectedUrl) }', {
                        patientId: uuid,
                        breadcrumbOverride: '${ ui.escapeJs(breadcrumbOverride) }'
                    });
        }
    }
    var handlePatientRowSelection = new handlePatientRowSelection();
    jq(function () {
        var widgetConfig = {
            initialPatients: lastViewedPatients,
            doInitialSearch: ${ doInitialSearch ? "\"" + ui.escapeJs(doInitialSearch) + "\"" : "null" },
            minSearchCharacters: ${ config.minSearchCharacters ?: 3 },
            afterSelectedUrl: '${ ui.escapeJs(config.afterSelectedUrl) }',
            breadcrumbOverride: '${ ui.escapeJs(breadcrumbOverride) }',
            searchDelayShort: ${ searchDelayShort },
            searchDelayLong: ${ searchDelayLong },
            handleRowSelection: ${ config.rowSelectionHandler ?: "handlePatientRowSelection" },
            dateFormat: '${ dateFormatJS }',
            locale: '${ locale }',
            defaultLocale: '${ defaultLocale }',
            messages: {
                info: '${ ui.message("coreapps.search.info") }',
                first: '${ ui.message("coreapps.search.first") }',
                previous: '${ ui.message("coreapps.search.previous") }',
                next: '${ ui.message("coreapps.search.next") }',
                last: '${ ui.message("coreapps.search.last") }',
                noMatchesFound: 'No matching Patient found in this hospital. Click to start remote search',
                noData: 'Patient file not found in this hospital. Click to start remote search',
                recent: '${ ui.message("coreapps.search.label.recent") }',
                searchError: '${ ui.message("coreapps.search.error") }',
                identifierColHeader: '${ ui.message("coreapps.search.identifier") }',
                nameColHeader: '${ ui.message("coreapps.search.name") }',
                genderColHeader: '${ ui.message("coreapps.gender") }',
                ageColHeader: '${ ui.message("coreapps.age") }',
                birthdateColHeader: '${ ui.message("coreapps.birthdate") }'
            }
        };
        var searchString="${simpleNationalIdString.replace('"', '\\"')}";
        new PatientSearchWidget(widgetConfig,${searchOnline},searchString,"${connectionProtocol+onlineIpAddress+queryURL}");

    });
</script>
<script type="text/javascript">
    var myVar;

    var stompClient = null;

    var socket = new SockJS('${fingerSocketPrintIpAddress}/search');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/showResult', function (calResult) {
            showResult(JSON.parse(calResult.body));
        });
    });


    function search() {
        document.getElementById('calResponse').innerHTML = "";
        document.getElementById('images').innerHTML = "";
        stompClient.send("/calcApp/search", {});
    }

    function showResult(message) {
        var response = document.getElementById('calResponse');
        var imageDiv = document.getElementById('images');
        if (message.type === "image") {
            var imageTag = document.createElement('img');
            imageTag.src = "data:image/png;base64," + message.result;
            imageDiv.appendChild(imageTag);
        } else if (message.type === "local" && message.patient !== "") {
            window.location = "../../coreapps/clinicianfacing/patient.page?patientId=" + message.patient;
        }
        else if (message.type === "online" && message.patient !== "") {
            window.location = "/openmrs/ugandaemrfingerprint/patientInOtherFacility.page?patientId=" + message.patient;
        }
        else if (message.type === null && (message.patient === null || message.patient === "")) {
            var message;
            message = '{"result":"Patient Not Found at Central Server"}';
            showResult(JSON.parse(message));
            jq().toastmessage('showErrorToast', "Patient Not Found");
        }
        else {
            response.innerHTML = message.result;
        }
    }
    function myFunction() {
        document.getElementById("loader").className = "load";
        myVar = setTimeout(showPage, 5000);
    }
    function showPage() {
        document.getElementById("loader").style.display = "none";
        document.getElementById("myDiv").style.display = "block";
    }
</script>

<form method="get" id="patient-search-form" onsubmit="return false">
    <input type="text" id="patient-search" placeholder="${ui.message("coreapps.findPatient.search.placeholder")}"
           autocomplete="off" <% if (doInitialSearch) { %>value="${doInitialSearch}" <% } %>/>
    <i id="patient-search-clear-button" class="small icon-remove-sign"></i>
    <i id="patient-search-finger-print-button" class="small icon-fingerprint"></i>
</form>

<div id="patient-search-finger-print" style="display:none;">
    <br>

    <div id="calculationDiv">
        <button id="search" onclick="search();">Search</button>

        <p id="calResponse"></p>

        <div id="images"></div>
    </div>

</div>

<div id="patient-search-results"></div>

<div id="remote-search">
    <a href="${ui.pageLink('ugandaemrfingerprint', 'patientInOtherFacility')}">remote search</a>
</div>