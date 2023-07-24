The frontend code was started with resources/templates/index.html and greeting.html using the tutorial at https://spring.io/guides/gs/serving-web-content/.

# Gov.UK header and footer
In fragments/govuk.html, I defined 3 fragments:
- headerfiles: link to the .css files. Note that we only need to specify the path from styles onwards. It is assumed that the root is resources/static.
- header: I copied into it the content of Header.js from project airqualitypoc.
- footer: I copied into it the content of Footer.js from project airqualitypoc.

In styles/main.css, reference govukstyle.css.

Manual operations: For now, these operations are kept manual in an effort not to pollute this project with Node.js. For full details on the origin of govukstyle.css & assets files, see section 'Gov.UK components' in ToBuildThisProject.md in project airqualitypoc.
- Create file static/styles/govukstyle.css. Inside, copy content from govukstyle.css in project airqualitypoc.
- Copy under static/assets all files from frontend/assets in project airqualitypoc.

# Add mapping to the app

## Google Maps
You first need to set up a Google Cloud project: https://developers.google.com/maps/documentation/javascript/cloud-setup
- I did not create a new project. I reused the project I had created for the reCAPTCHA: https://console.cloud.google.com/welcome?project=airqualitypoc.
- I set up Billing using my First Direct debit card. 90-day trial started on 30/06/2023. See the dashboard at https://console.cloud.google.com/billing/0136A5-1C12FB-AF1F6F?project=airqualitypoc.
    - I created a budget of £10 per month and alerts when my actual spending goes above £5, £9 and £10 per month.
- I enabled the Maps Javascript API. Apply API key restrictions following https://cloud.google.com/docs/authentication/api-keys?visit_id=638237161151260569-3303928981&rd=1#api_key_restrictions.

Restricting the Maps API Key:
- Go to https://console.cloud.google.com/google/maps-apis/credentials?project=airqualitypoc
- Click on 'Maps API Key' under API Keys.
- Edit the restricted IP. Replace it with the IPv6 value given at https://whatismyipaddress.com/.
- Click on Save.

### TODO
The current poc still relies on the key being pasted in google.html. If we go ahead with this mapping API in production, we need to do more reading to verify if it is OK to expose the key. The key can be restricted to a URL but still feeling uncomfortable to make it public.

In google.html, replace the hardcoded Uluru's location with the lat, lng of our site. They will need to be derived from easting, northing.

## Ordnance Survey Maps

### Raster mapping
The code in osRaster.html was taken from https://labs.os.uk/public/os-data-hub-examples/os-maps-api/wmts-3857-basic-map#maplibre-gl-js

I adapted it not to expose the key. See 'http://localhost:8080/proxy_os_raster_mapping' which is the proxy controller where we keep the key server-side.

### Vector mapping
The code in osVector.html was taken from https://labs.os.uk/public/os-data-hub-examples/os-vector-tile-api/vts-3857-basic-map#maplibre-gl-js

Note that the key is hardcoded at the moment. TODO Work out a mechanism so we do not expose the key.
