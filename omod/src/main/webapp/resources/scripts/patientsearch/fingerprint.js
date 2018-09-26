/*
 * Show the container, and enable all elements in it
 *
 * @param the Id of the container
 */
function showContainer(container) {
    jq(container).removeClass('hidden');
    jq(container + ' :input').attr('disabled', false);
    jq(container + ' :input').prop('checked', false);
}


function hideContainer(container) {
    jq(container).addClass('hidden');
    jq(container + ' :input').attr('disabled', true);
    jq(container + ' :input').prop('checked', false);
}