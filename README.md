# google-places-api-java

**Notice:** Before using this library, you must [register an API key for Google Places API](https://developers.google.com/places/documentation/#Authentication).

## Contents

* [Creating the client](#creating-the-client)
* [Place Searches](#place-searches)
    * [Nearby Search](#nearby-search-requests)
    * [Text Search](#text-search-requests)
    * [Radar Search](#radar-search-requests)
    * [Adding extra URL parameters](#additional-url-parameters)
* [Place Details](#place-details)
* [Modifying a Place](#place-actions)
* [Place Photos](#place-photos)
* [Autocomplete](#autocomplete)
    * [Place prediction](#place-prediction)
    * [Query prediction](#query-prediction)

## Creating the client

```java
GooglePlaces client = new GooglePlaces("yourApiKey");
```

You may optionally provide your own HttpClient to delegate HTTP traffic.

```java
GooglePlace client = new GooglePlaces("yourApiKey", new DefaultHttpClient());
```

## Place Searches

### Nearby Search Requests

You can search for places near specific latitude-longitude coordinates with a radius (in meters):

```java
List<Place> places = client.getNearbyPlaces(lat, lng, radius, GooglePlaces.MAXIMUM_RESULTS);
```

You can retrieve at most 60 results. Every 20 results a new HTTP GET request will have to be made and also has a delay of 3 seconds
because of API restrictions. You can omit the 'limit' parameter and it will default to 20 which will only ever require one HTTP GET request.

### Text Search Requests

You can also search for locations by search query. This is the same backend system that Google Maps uses.

```java
List<Place> places = client.getPlacesByQuery("Empire+State+Building", GooglePlaces.MAXIMUM_RESULTS);
```

### Radar Search Requests

You can also use the ["radar"](https://developers.google.com/places/documentation/search#RadarSearchRequests) method of finding locations.

```java
List<Place> places = client.getPlacesByRadar(lat, lng, radius, GooglePlaces.MAXIMUM_RESULTS);
```

### Additional Url Parameters

If you need to add additional URL parameters to the request URL you can append as many `Param` objects as you want to any request method.

```java
List<Place> places = client.getPlacesByQuery("Empire+State+Building", GooglePlaces.MAXIMUM_RESULTS, Param.name("language").value("en"), Param.name("opennow").value(true));
```

## Place Details

Any of the above getters will only get you limited information about the returned Place. You can get a much more in-depth Place object with `Place#getDetails(Param...)`:

Here's one way I can get detailed information about the Empire State Building.

```java
List<Place> places = client.getPlacesByQuery("Empire+State+Building", GooglePlaces.MAXIMUM_RESULTS);
Place empireStateBuilding = null;
for (Place place : places) {
    if (place.getName().equals("Empire State Building")) {
        empireStateBuilding = place;
        break;
    }
}

if (empireStateBuilding != null) {
    Place detailedEmpireStateBuilding = empireStateBuilding.getDetails(); // sends a GET request for more details
    // Just an example of the amount of information at your disposal:
    System.out.println("ID: " + detailedEmpireStateBuilding.getId());
    System.out.println("Name: " + detailedEmpireStateBuilding.getName());
    System.out.println("Phone: " + detailedEmpireStateBuilding.getPhoneNumber());
    System.out.println("International Phone: " + empireStateBuilding.getInternationalPhoneNumber());
    System.out.println("Website: " + detailedEmpireStateBuilding.getWebsite());
    System.out.println("Always Opened: " + detailedEmpireStateBuilding.isAlwaysOpened());
    System.out.println("Status: " + detailedEmpireStateBuilding.getStatus());
    System.out.println("Google Place URL: " + detailedEmpireStateBuilding.getGoogleUrl());
    System.out.println("Price: " + detailedEmpireStateBuilding.getPrice());
    System.out.println("Address: " + detailedEmpireStateBuilding.getAddress());
    System.out.println("Vicinity: " + detailedEmpireStateBuilding.getVicinity());
    System.out.println("Reviews: " + detailedEmpireStateBuilding.getReviews().size());
    System.out.println("Hours:\n " + detailedEmpireStateBuilding.getHours());
}
```

This will print something like:

```
ID: bc232d2422e7068b2a2ffb314f02e3733dd47796
Name: Empire State Building
Phone: (212) 736-3100
International Phone: null
Website: http://www.esbnyc.com/
Always Opened: false
Status: OPENED
Google Place URL: https://plus.google.com/110101791098901696787/about?hl=en-US
Price: NONE
Address: 350 5th Ave, New York, NY, United States
Vicinity: 350 5th Ave, New York
Reviews: 5
Hours:
SUNDAY 08:00 -- MONDAY 02:00
MONDAY 08:00 -- TUESDAY 02:00
TUESDAY 08:00 -- WEDNESDAY 02:00
WEDNESDAY 08:00 -- THURSDAY 02:00
THURSDAY 08:00 -- FRIDAY 02:00
FRIDAY 08:00 -- SATURDAY 02:00
SATURDAY 08:00 -- SUNDAY 02:00
```

## Place Actions

Coming soon...

## Place Photos

Coming soon...

## Autocomplete

# Place prediction

Coming soon...

# Query prediction

Coming soon...





