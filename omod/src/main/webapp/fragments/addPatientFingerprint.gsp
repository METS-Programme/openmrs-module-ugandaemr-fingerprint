<script type="text/javascript">

    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {
            label: "${ ui.escapeJs(ui.message("ugandaemrfingerprint.app.addfingerprint.label")) }",
            link: '${ ui.urlBind("/" + contextPath + "coreapps/clinicianfacing/patient.page?patientId="+patientId, [ patientId: patient ] ) }'
        }
    ];

    var stompClient = null;

    var socket = new SockJS('${fingerSocketPrintIpAddress}/add');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/showResult', function (calResult) {
            showResult(JSON.parse(calResult.body));
        });
    });

    function connect() {

    }

    function sendNum(finger) {
        document.getElementById('calResponse').innerHTML = "";
        document.getElementById('images').innerHTML = "";
        stompClient.send("/calcApp/add", {}, JSON.stringify({'finger': finger, patient: "${patientId}"}));
    }

    function deleteFingerPrints() {
        jq.post('${ ui.actionLink("deleteFingerprint") }', {
            patient: "${patientId}"
        }, function (response) {
            if (response.search("Patient fingerprint Deleted") > -1) {
                var message;
                message = '{"result":"Patient fingerprint Deleted"}';

                showResult(JSON.parse(message));
            }
        });
    }

    function search(finger) {
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
        } else if (message.type === "template") {
            jq.post('${ ui.actionLink("saveFingerprint") }', {
                patient: message.patient,
                finger: message.finger,
                fingerprint: message.result
            }, function (response) {
                console.log("Response: " + response);
            });
        } else {
            response.innerHTML = message.result;
        }
    }
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
</style>
${ui.includeFragment("coreapps", "patientHeader", [patient: patient])}
<h3>${ui.message("ugandaemrfingerprint.app.addfingerprint.label")}</h3>

<div id="calculationDiv">
    <% fingers.each { k, v -> %>
    <button id="thumb" onclick="sendNum(${k});">Scan ${v}</button>
    <% } %>
    <% if (fingerPrintEnrolled > 0) { %>
    <button id="delete" onclick="deleteFingerPrints()">Delete Existing Finger Prints</button>
    <% } %>
    <p id="calResponse"></p>

    <p id="deleteMessage"></p>

    <div id="images"></div>
</div>