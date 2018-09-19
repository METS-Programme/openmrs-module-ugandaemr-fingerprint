<style type="text/css">
img {
    width: 100px;
    height: auto;
}
</style>

<div class="info-section patientsummary">
    <div class="info-header">
        <i class="icon-hand-up"></i>

        <h3>${ui.message("Fingerprint Details".toUpperCase())}</h3>
    </div>

    <div id="images" class="info-body">

        <% if (fingerPrintNo <= 0) {

        %>
        <div>
            <strong>${ui.message("No Fingerprint Captured.")}</strong>
        </div>
        <%
            } else {
                fingerPrint.each { k, v -> %>
        <div>
            <strong>${k}: </strong>Enrolled on ${Date.parse("yyyy-M-d H:m:s", v).format("d MMM yyyy")}
        </div>

        <% }} %>
    </div>
</div>