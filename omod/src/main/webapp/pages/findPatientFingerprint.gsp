<%
    ui.decorateWith("appui", "standardEmrPage")
%>
<script type="text/javascript">
    <% if (breadcrumbs) { %>
    var breadcrumbs = ${ breadcrumbs };
    <% } else { %>
    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {label: "${ ui.message(label)}"}
    ];
    <% } %>
    jq(function () {
        jq('#patient-search').focus();
    });
</script>

<div class="border border-dark">

    <h2>
        ${ui.message(heading)}
    </h2>
    <% if (breadcrumbs) { %>
    ${ui.includeFragment("ugandaemrfingerprint", "patientsearch/patientSearchWidget",
            [afterSelectedUrl      : afterSelectedUrl,
             showLastViewedPatients: showLastViewedPatients,
             breadcrumbOverride    : breadcrumbs])}
    <% } else { %>
    ${ui.includeFragment("ugandaemrfingerprint", "patientsearch/patientSearchWidget",
            [afterSelectedUrl      : afterSelectedUrl,
             showLastViewedPatients: showLastViewedPatients])}
    <% } %>
</div>