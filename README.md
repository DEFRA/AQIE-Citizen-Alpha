# AQIE-Citizen-Alpha
AQIE = Air Quality and Industrial Emissions. This is the prototype for our Citizen service.

## Backend
Controllers returning a View have a name prefixed with Display.

## Frontend
The frontend code does not use a JavaScript framework. This is done on purpose to follow progressive enhancement recommended by GDS. See https://www.gov.uk/service-manual/technology/using-progressive-enhancement.

## References
https://www.thymeleaf.org/

## TODO
Add html pages: 
    - Page 4 = Nearest air quality to a postcode. 1 text field = Postcode and 1 Submit button.
        - Once submitted, add a table listing air qualities. Cols = Index, Area.

Add a reCAPTCHA.

Unit tests for inputValidation.js

Dockerise the app. Mirror what was done with the 1st poc project.

