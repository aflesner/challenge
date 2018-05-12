# **Sample Problem**


------------

#### Build a web application that allows a user to enter a date time range and get back the rate at which they would be charged to park for that time span.

## Requirements

* Publish a repo on Github that i can clone and run on my local machine
* Use Java, Scala, or Go to complete this.
   - For JVM - Don’t use Spring Boot, Dropwizard or Scalatra to do this, but feel free to use the libraries employed by those frameworks (eg JAX-RS, Jersey, Metrics, Jackson, Akka, Jetty, etc)
* API will need documentation and a contract published.
* It should support JSON & XML over HTTP
* I should be able to curl against an API that computes a price for a specified datetime range given a JSON file of rates.

Sample file:
```json
    {
      "rates": [
        {
          "days": "mon,tues,wed,thurs,fri",
          "times": "0600-1800",
          "price": 1500
        },
        {
          "days": "sat,sun",
          "times": "0600-2000",
          "price": 2000
        }
      ]
    }
```



Sample result:
Datetime ranges should be specified in isoformat.  A rate must completely encapsulate a datetime range for it to be available.

Rates will never overlap.

2015-07-01T07:00:00Z to 2015-07-01T12:00:00Z should yield 1500
2015-07-04T07:00:00Z to 2015-07-04T12:00:00Z should yield 2000
2015-07-04T07:00:00Z to 2015-07-04T20:00:00Z should yield unavailable

Sample JSON for testing
```json
{
    "rates": [
        {
            "days": "mon,tues,thurs",
            "times": "0900-2100",
            "price": 1500
        },
        {
            "days": "fri,sat,sun",
            "times": "0900-2100",
            "price": 2000
        },
        {
            "days": "wed",
            "times": "0600-1800",
            "price": 1750
        },
        {
            "days": "mon,wed,sat",
            "times": "0100-0500",
            "price": 1000
        },
        {
            "days": "sun,tues",
            "times": "0100-0700",
            "price": 925
        }
    ]
}

```

## Building
Requires JDK 8.

To build a fatJar with all dependencies included execute:
```text
./gradlew fatJar
```  

## Starting
execute:  
```text
java -jar build/libs/webapp-all-1.0-SNAPSHOT.jar
```

## Usage
App will accept and return JSON or XML based on Content-Type HTTP header.  

Expected JSON request object structure:  
```json
{
  "begin":"2015-07-04T01:00:00Z", 
  "end":"2015-07-04T07:00:00Z"
}
```

Where begin is start of time of parking spot being requested and end is the ending time of parking spot being requested. App will return a JSON message similar to below when parking exists for that time period.  

Response when a valid JSON request is submitted and parking exists:  
```json
{
  "begin":"2015-07-01T07:00:00Z",
  "end":"2015-07-01T12:00:00Z",
  "price":1750
}
```

Response when a valid JSON request is submitted and parking does not exist:
```json
{
  "begin":"2015-07-04T01:00:00Z",
  "end":"2015-07-04T07:00:00Z",
  "price":"unavailable"
}
```

Example requests for JSON: 
```text
curl -d '{"begin":"2015-07-04T01:00:00Z", "end":"2015-07-04T07:00:00Z"}' -H "Content-Type: application/json" -X POST http://localhost:8080  
curl -d '{"begin":"2015-07-04T07:00:00Z", "end":"2015-07-04T12:00:00Z"}' -H "Content-Type: application/json" -X POST http://localhost:8080  
curl -d '{"begin":"2015-07-01T07:00:00Z", "end":"2015-07-01T12:00:00Z"}' -H "Content-Type: application/json" -X POST http://localhost:8080  
curl -d '{"begin":"2015-07-04T07:00:00Z", "end":"2015-07-04T20:00:00Z"}' -H "Content-Type: application/json" -X POST http://localhost:8080  
curl -d '{"begin":"2015-07-04T01:01:00Z", "end":"2015-07-04T06:59:00Z"}' -H "Content-Type: application/json" -X POST http://localhost:8080
``` 

Expected XML request object structure:  

```xml
<?xml version="1.0" encoding="UTF-8"?>
<RateRequest>
    <begin>2015-07-04T01:00:00Z</begin>
    <end>2015-07-04T07:00:00Z</end>
</RateRequest>
```

Where begin is start of time of parking spot being requested and end is the ending time of parking spot being requested. App will return a XML message similar to below when parking exists for that time period.  

Response when a valid XML request is submitted and parking exists:  
```xml
<?xml version='1.0' encoding='UTF-8'?>
<RateResponse>
    <begin>2015-07-01T07:00:00Z</begin>
    <end>2015-07-01T12:00:00Z</end>
    <price>1750</price>
</RateResponse>
```
Response when a valid JSON request is submitted and parking does not exist:  
```xml
<?xml version='1.0' encoding='UTF-8'?>
<RateResponse>
    <begin>2015-07-04T01:00:00Z</begin>
    <end>2015-07-04T07:00:00Z</end>
    <price>unavailable</price>
</RateResponse>
```

Example requests for XML:
```text
curl -d '<?xml version="1.0" encoding="UTF-8"?><RateRequest><begin>2015-07-04T01:00:00Z</begin><end>2015-07-04T07:00:00Z</end></RateRequest>' -H "Content-Type: application/xml" -X POST http://localhost:8080
curl -d '<?xml version="1.0" encoding="UTF-8"?><RateRequest><begin>2015-07-04T07:00:00Z</begin><end>2015-07-04T12:00:00Z</end></RateRequest>' -H "Content-Type: application/xml" -X POST http://localhost:8080
curl -d '<?xml version="1.0" encoding="UTF-8"?><RateRequest><begin>2015-07-01T07:00:00Z</begin><end>2015-07-01T12:00:00Z</end></RateRequest>' -H "Content-Type: application/xml" -X POST http://localhost:8080
```  


