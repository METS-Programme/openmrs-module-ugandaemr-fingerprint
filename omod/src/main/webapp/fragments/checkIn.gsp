<%
    ui.includeJavascript("patientqueueing", "patientqueue.js")
%>

<script type="text/javascript">
    jq(document).ready(function () {
    });

    function printTriageRecord(divIdToPrint, dataToPrint) {
        var divToPrint = document.getElementById(divIdToPrint);
        var newWin = window.open('', 'Print-Window');
        var checkInData = "";
        jq("#check_in_receipt").html("");
        if (dataToPrint.patientTriageQueue !== "") {
            checkInData += "<table>";
            checkInData += "<tr><th width=\"50%\" style=\"text-align: left\">Check In Date:</th><td>%s</td></tr>".replace("%s", dataToPrint.patientTriageQueue.dateCreated);
            checkInData += "<tr><th width=\"50%\" style=\"text-align: left\">Patient Names Date:</th><td>%s</td></tr>".replace("%s", dataToPrint.patientTriageQueue.patientNames);
            checkInData += "<tr><th width=\"50%\" style=\"text-align: left\">Visit No.:</th><td>%s</td></tr>".replace("%s", dataToPrint.patientTriageQueue.id);
            checkInData += "<tr><th width=\"50%\" style=\"text-align: left\">Gender:</th><td>%s</td></tr>".replace("%s", "TBD");
            checkInData += "<tr><th width=\"50%\" style=\"text-align: left\">Entry Point:</th><td>%s</td></tr>".replace("%s", dataToPrint.patientTriageQueue.locationFrom);
            checkInData += "<tr><th width=\"50%\" style=\"text-align: left\">Registration Attendant:</th><td>%s</td></tr>".replace("%s", "TBD");
            checkInData += "</table>";
        }
        jq("#check_in_receipt").append(checkInData);
        newWin.document.open();
        newWin.document.write('<html><body onload="window.print()">' + divToPrint.innerHTML + '</body></html>');
        newWin.document.close();
        setTimeout(function () {
            newWin.close();
        }, 10);
    }
</script>
<style>
.modal-header {
    background: #000;
    color: #ffffff;
}

.print-only {
    display: none;
}

hr.printhr {
    border: 1px solid red;
}
</style>

<script>
    if (jQuery) {
    }
</script>

<div class="modal fade" id="add_patient_to_queue_dialog" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                Check in <span id="checkin_patient_names"></span><i class="icon-check medium"></i>
            </div>

            <div class="modal-body">
                <span id="add_to_queue-container">

                </span>
                <input type="hidden" id="patient_id" name="patient_id" value="">

                <div class="form-group">
                    <label for="location_id">${ui.message("patientqueueing.location.label")}</label>
                    <select class="form-control" id="location_id" name="location_id">
                        <option value="">${ui.message("patientqueueing.location.selectTitle")}</option>
                        <% if (locationList != null) {
                            locationList.each { %>
                        <option value="${it.uuid}">${it.name}</option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <span class="field-error" style="display: none;"></span>
                    <% if (locationList == null) { %>
                    <div><${ui.message("patientqueueing.select.error")}</div>
                    <% } %>
                </div>
            </div>

            <div class="modal-footer form">
                <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                <input type="submit" class="confirm" id="checkin" value="Check In">
            </div>
        </div>
    </div>
</div>

<div id="printSection" class="print-only">
    <center>
        <div style="width: 60%">
            <div><img src="${ui.resourceLink("aijar", "images/moh_logo_large.png")}"/></div>

            <div><h1>HEALTH CENTER NAME</h1></div>
            <hr style="border: 1px solid red;"/>

            <div><h2>Visit Registration Receipt</h2></div>

            <div id="check_in_receipt" align="left">
            </div>
        </div>
    </center>
</div>


