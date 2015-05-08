# google-places-api-java

**Notice:** Before using this library, you must
[register an API key for Google Places API](https://developers.google.com/places/webservice/intro#Authentication).


**Notice 2:** The release of v2 brings many breaking changes. You will almost certainly need to make adjustments and
recompile after upgrading to the latest version. This is because many deprecations from previous iterations of this
library and the Google Places API specification have been removed for conciseness including the removal of Events.
**Previous iterations of this library will stop working once the deprecations in the API specification are removed and
it is imperative that you update ASAP to ensure your applications continue to work.**


Walker Crouse is an aspiring software developer, open source contributor, and starving college student. If you like my
projects, please consider donating a small amount so that I may continue to devote time to them. Thank you.

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=BR4TEJ4R39SFY&lc=US&item_name=Walker%20Crouse&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted)


## Contents

* [Quickstart](#quickstart)
* [Creating the client](#creating-the-client)
* [Place Searches](#place-searches)
    * [Nearby Search](#nearby-search-requests)
    * [Text Search](#text-search-requests)
    * [Radar Search](#radar-search-requests)
    * [Adding extra URL parameters](#additional-url-parameters)
* [Place Details](#place-details)
    * [Icons](#icons)
* [Modifying a Place](#place-actions)
    * [Add place](#add-place)
    * [Delete place](#delete-place)
* [Place Photos](#place-photos)
* [Autocomplete](#autocomplete)
    * [Place prediction](#place-prediction)
    * [Query prediction](#query-prediction)
* [Android integration](#android-integration)
* [Documentation](#documentation)
* [Build](#build)


## Quickstart

With Maven (make sure you are using the [latest version](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22se.walkercrou%22%20AND%20a%3A%22google-places-api-java%22)):

```xml
<dependencies>
    <dependency>
        <groupId>se.walkercrou</groupId>
        <artifactId>google-places-api-java</artifactId>
        <version>2.1.2</version>
    </dependency>
</dependencies>
```

With Gradle (make sure you are using the [latest version](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22se.walkercrou%22%20AND%20a%3A%22google-places-api-java%22)):

```java
repositories {
    mavenCentral()
}

dependencies {
    compile 'se.walkercrou:google-places-api-java:2.1.2'
    compile 'org.apache.httpcomponents:httpclient-android:4.3.5.1'
}
```

Or just download the JAR with all dependencies included from the
[releases](https://github.com/windy1/google-places-api-java/releases). **Do not use the JAR if you are using Android, it will cause an error.**


## Creating the client

```java
GooglePlaces client = new GooglePlaces("yourApiKey");
```

You may optionally provide your own RequestHandler to delegate HTTP traffic

```java
GooglePlaces client = new GooglePlaces("yourApiKey", new MyRequestHandler());
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
List<Place> places = client.getPlacesByQuery("Empire State Building", GooglePlaces.MAXIMUM_RESULTS);
```

### Radar Search Requests

You can also use the ["radar"](https://developers.google.com/places/documentation/search#RadarSearchRequests) method of finding locations.

```java
List<Place> places = client.getPlacesByRadar(lat, lng, radius, GooglePlaces.MAXIMUM_RESULTS);
```

### Additional Url Parameters

If you need to add additional URL parameters to the request URL you can append as many `Param` objects as you want to any request method.

```java
List<Place> places = client.getPlacesByQuery("Empire State Building", GooglePlaces.MAXIMUM_RESULTS, Param.name("language").value("en"), Param.name("opennow").value(true));
```


## Place Details

Any of the above getters will only get you limited information about the returned Place. You can get a much more in-depth Place object with `Place#getDetails(Param...)`:

Here's one way I can get detailed information about the Empire State Building.

```java
List<Place> places = client.getPlacesByQuery("Empire State Building", GooglePlaces.MAXIMUM_RESULTS);
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

### Icons

Once you have a detailed `Place` object, you can download it's "Icon" with the following.

```java
BufferedImage image = place.downloadIcon().getIconImage();
```

If you are working on Android, javax.imageio is not implemented. You can create a Bitmap from the icon with:

```java
InputStream stream = place.downloadIcon().getIconInputStream();
Bitmap bitmap = BitmapFactory.decodeStream(stream);
```


## Place Actions

### Add Place

Places can be added to Google Places API through this library. Added Places are only visible to your application until
they are approved by Google's moderation process. This can be checked with `Place.getScope()`.

You must begin by building the Place input via the `PlaceBuilder` class.

```java
PlaceBuilder builder = new PlaceBuilder(locationName, latitude, longitude, "type1", "type2"...)
```

The constructor arguments are the only required arguments to add a place to Google Places but filling out the building
more completely will help your Place get approved by the moderation process faster.

```java
builder.accuracy(50)
       .phoneNumber("(000) 000-0000")
       .address("4 John St")
       .website("http://walkercrou.se")
       .locale(Locale.ENGLISH);
```

You must then pass the builder as an argument of `GooglePlaces.addPlace()`.

```java
Place place = client.addPlace(builder, true);
```

### Delete Place

You can delete places with:

```java
client.deletePlace(place);
```

## Place Photos

You can retrieve photos of places from Google as well. For example, here's how I can choose a random photo from a place
and save it to disk.

```java
List<Photo> photos = place.getPhotos();
Photo photo = photos.get(new Random().nextInt(photos.size()));
BufferedImage image = photo.download().getImage();

File file = new File("test.jpg");
file.createNewFile();
ImageIO.write(image, "jpg", file);
```

You can also specify a max width and max height for the image. The aspect ratio of the image will always be maintained.

```java
BufferedImage image = photo.download(100, 100).getImage();
```

To specify one and not the other, just set one of them to -1. If you do not specify them, the max size (1600) will be
passed. **NOTE:** You must pass at least one of the size parameters.

If you are working on Android, javax.imageio is not implemented, so you can create a bitmap from a photo with.

```java
InputStream stream = photo.download().getInputStream();
Bitmap bitmap = BitmapFactory.decodeStream(stream);
```

Remember not to execute this code on the main thread.


## Autocomplete

### Place prediction

You can receive auto-complete predictions for Places with:

```java
List<Prediction> predictions = client.getPlacePredictions("Empire");
```

As you might expect, The Empire State Building is the first result returned here. The prediction object contains a
human-readable description and a Place accessor so you can easily build a UI around it. (Particularly useful for Android
development)

### Query prediction

You can also receive auto-complete predictions for Places with general queries such as "pizza in New York".

```java
List<Prediction> predictions = client.getQueryPredictions("pizza in New York");
```

## Android integration

Just remember that if you are using this library with Android you should never execute network code on the main thread.
Either run it in another thread...

```java
new Thread(new Runnable() {
    public void run() {
        // do something
    }
}).start();
```

...or run it in an [AsyncTask](http://developer.android.com/reference/android/os/AsyncTask.html).


## Documentation

Documentation for this project can be found at [this location](http://windy1.github.io/google-places-api-java/docs).


## Build

This project uses [Apache Maven](http://maven.apache.org/). Create a file called `src/main/resources/places_api.key`
with your Google Places API key before running tests and building with `mvn`.
