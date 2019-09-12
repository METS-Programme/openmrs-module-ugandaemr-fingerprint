<%
    ui.includeJavascript("patientqueueing", "patientqueue.js")
%>

<script type="text/javascript">
    jq(document).ready(function () {
    });
</script>
<style>
.modal-header {
    background: #000;
    color: #ffffff;
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


