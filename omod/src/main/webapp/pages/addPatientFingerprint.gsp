<%
    ui.decorateWith("appui", "standardEmrPage", [title: ui.message("ugandaemrfingerprint.app.addfingerprint.label")])
    ui.includeJavascript("ugandaemrfingerprint", "patientsearch/sockjs-0.3.4.js")
    ui.includeJavascript("ugandaemrfingerprint", "patientsearch/stomp.js")
%>

${ ui.includeFragment("ugandaemrfingerprint", "addPatientFingerprint") }