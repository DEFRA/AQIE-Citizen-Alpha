# Test vs Local with PostgreSQL
Start the app with:
java -jar -Dspring.profiles.active=localwithpostgre -DOS_MAPPING_KEY=THE_KEY target/airqualitypoc-0.0.1-SNAPSHOT.jar

## App entry point
http://localhost:8080

## Endpoints

### Sites endpoint
To list all sites: curl http://localhost:8080/api/sites

To list a specific site's details: curl http://localhost:8080/api/sites/1000002
```
{
  "id" : "1000002",
  "name" : "DUKINFIELD 2",
  "uka_site_id" : "UKA10609",
  "usage_type" : "primary",
  "easting" : 394000.0,
  "northing" : 397900.0,
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/sites/1000002"
    },
    "sites" : {
      "href" : "http://localhost:8080/api/sites"
    }
  }
}
```

To find the nearest site: curl "http://localhost:8080/api/sites/nearest?postcode=SK164TH"
```
{
  "_embedded" : {
    "siteDtoes" : [ {
      "id" : "1000002",
      "name" : "DUKINFIELD 2",
      "uka_site_id" : "UKA10609",
      "usage_type" : "primary",
      "easting" : 394000.0,
      "northing" : 397900.0,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/api/sites/1000002"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/sites/nearest?postcode=SK164TH"
    }
  }
}
```

### Measurement endpoint
All quantities are given in µgm-3 as shown at https://uk-air.defra.gov.uk/latest/currentlevels.

To save a measurement for a given site:  
curl -X POST -H "Content-Type: application/json" --json '{"siteId" : "1000002", "pollutantCodeQuantityMap" : {"OZONE" : 45, "NITROGEN_DIOXIDE" : 10, "SULPHUR_DIOXIDE" : 2, "PM2DOT5" : 4, "PM10" : 6}, "sampledTime":"2023-05-10T10:30:00.000"}' http://localhost:8080/api/measurements

```
{
  "id" : 1,
  "siteId" : "1000002",
  "pollutantCodeQuantityMap" : {
    "OZONE" : 45,
    "NITROGEN_DIOXIDE" : 10,
    "SULPHUR_DIOXIDE" : 2,
    "PM2DOT5" : 4,
    "PM10" : 6
  },
  "sampledTime" : "2023-05-10T10:30:00",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/measurements/1"
    },
    "measurements" : {
      "href" : "http://localhost:8080/api/measurements{?siteId,from,to}",
      "templated" : true
    }
  }
}
```

To list details about a measurement:  
curl http://localhost:8080/api/measurements/1

To list all measurements for a given site:
- Without providing from/to:  
  curl "http://localhost:8080/api/measurements?siteId=1000002"
```
{
  "_embedded" : {
    "measurementDtoes" : [ {
      "id" : 1,
      "siteId" : "1000002",
      "pollutantCodeQuantityMap" : {
        "NITROGEN_DIOXIDE" : 10,
        "OZONE" : 45,
        "PM2DOT5" : 4,
        "PM10" : 6,
        "SULPHUR_DIOXIDE" : 2
      },
      "sampledTime" : "2023-05-10T10:30:00",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/api/measurements/1"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/measurements?siteId=1000002{&from,to}",
      "templated" : true
    }
  }
}
```

- Providing only from:  
  curl "http://localhost:8080/api/measurements?siteId=1000002&from=2023-05-10T09:16:12"

- Providing from & to:  
  curl "http://localhost:8080/api/measurements?siteId=1000002&from=2023-05-10T09:16:12&to=2023-05-10T10:16:12"

To get the highest measurement for a given pollutant for a given site:
- Without providing from/to:  
  curl "http://localhost:8080/api/measurements/highest?siteId=1000002&pollutantCode=OZONE"
```
{
  "_embedded" : {
    "measurementDtoes" : [ {
      "id" : 1,
      "siteId" : "1000002",
      "pollutantCodeQuantityMap" : {
        "NITROGEN_DIOXIDE" : 10,
        "OZONE" : 45,
        "PM2DOT5" : 4,
        "PM10" : 6,
        "SULPHUR_DIOXIDE" : 2
      },
      "sampledTime" : "2023-05-10T10:30:00",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/api/measurements/1"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/measurements/highest?siteId=1000002&pollutantCode=OZONE{&from,to}",
      "templated" : true
    }
  }
}
```

- Providing only from:  
  curl "http://localhost:8080/api/measurements/highest?siteId=1000002&pollutantCode=OZONE&from=2023-05-10T09:16:12"

- Providing from & to:  
  curl "http://localhost:8080/api/measurements/highest?siteId=1000002&pollutantCode=OZONE&from=2023-05-10T09:16:12&to=2023-05-10T10:16:12"

### Air Quality endpoint
To find the nearest air quality: curl "http://localhost:8080/api/aq/nearest?postcode=SO160AU"
```
{
  "_embedded" : {
    "airQualityDtoes" : [ {
      "id" : 0,
      "index" : 3,
      "polygon" : "POLYGON((-59000 1221000,-59000 1223000,-57000 1223000,-57000 1221000,-59000 1221000))",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/api/aq/0"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/aq/nearest?postcode=SO160AU"
    }
  }
}
```

To list a specific air quality's details: curl "http://localhost:8080/api/aq/0" -> TODO as we get a 500 ATM.
