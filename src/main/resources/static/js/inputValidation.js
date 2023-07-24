// TODO Using the same regEx than in PostcodeRegexMatcher on the Java side. Is there a way to have this constant in a single place.
var postcodeRegEx = /([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\s?[0-9][A-Za-z]{2})/i;

function onPostcodeInput(postcode) {
    var btnSubmit = document.getElementById("submit");
    if (postcodeRegEx.test(postcode.toUpperCase())) {
        btnSubmit.disabled = false;
    } else {
        btnSubmit.disabled = true;
    }
}

function validatePostcode() {
    var postcode = document.getElementById("postcode").value;
    if (postcodeRegEx.test(postcode.toUpperCase())) {
        return true;
    } else {
        alert("The postcode is invalid. Not submitting to the server.");
        return false;
    }
}

function validateInput() {
    var pollutantCode = document.getElementById("pollutantCode").value;
    if (pollutantCode === '0') {
        alert("You must choose a pollutant. Not submitting to the server.");
        return false;
    }
    return validateTimes();
}

function validateTimes() {
    var fromTime = document.getElementById("fromTime").value;
    var toTime = document.getElementById("toTime").value;
    if (fromTime < toTime) {
        return true;
    } else {
        alert("From must be before To. Not submitting to the server.");
        return false;
    }
}
