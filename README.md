# FlightsAggregator

**Background:**

Interview test about aggregating responses from different JSON APIs 

Flight Aggregator is a flights search solution which aggregates flight results initially from 2 different suppliers  A future iteration (not part of the test) may add more suppliers.

**What is required:**

Implement the Aggregator Flights service that should produce an aggregated result from both MongerAir and HardJet.
The result should be a JSON response which contains a list of flights ordered by fare which has the following attributes:

**Known errors (NOT FIXED YET):**

* The use of promises is not correct. ==> **Should be fixed, but in depth testing is yet to be done
* Investigate if Async annotation is needed. The interviewer told me otherwise
* It needs separation of concerns; use of SOLID principles
* The configuration should be taken out of the spring boot main application

**Flights Aggregator API**

**Request**

| Name | Description |
| ------ | ------ |
| origin | 3 letter IATA code(eg. LHR, AMS) |
| destination | 3 letter IATA code(eg. LHR, AMS) |
| departureDate | ISO_LOCAL_DATE format |
| returnDate | ISO_LOCAL_DATE format |
| numberOfPassengers | Maximum 4 passengers |

**Response**

| Name | Description |
| ------ | ------ |
| airline | Name of Airline |
| supplier | Eg: MongerAir or HardJet |
| fare | Total price rounded to 2 decimals |
| departureAirportCode | 3 letter IATA code(eg. LHR, AMS) |
| destinationAirportCode | 3 letter IATA code(eg. LHR, AMS) |
| departureDate | ISO_DATE_TIME format |
| arrivalDate | ISO_DATE_TIME format |

The service should connect to the both the suppliers using HTTP.

**MongerAir API**

**Request**

| Name | Description |
| ------ | ------ |
| origin | 3 letter IATA code(eg. LHR, AMS) |
| destination | 3 letter IATA code(eg. LHR, AMS) |
| departureDate | ISO_LOCAL_DATE format |
| returnDate | ISO_LOCAL_DATE format |
| passengerCount | Number of passengers |

**Response**


| Name | Description |
| ------ | ------ |
| airline | Name of the airline |
| price | Total price |
| cabinclass | E for Economy and B for Business |
| departureAirportCode | Eg: LHR |
| destinationAirportCode | Eg: LHR |
| departureDate | ISO_LOCAL_DATE_TIME format |
| arrivalDate | ISO_LOCAL_DATE_TIME format |

**HardJet API**

**Request**

| Name | Description |
| ------ | ------ |
| from | 3 letter IATA code(eg. LHR, AMS) |
| to | 3 letter IATA code(eg. LHR, AMS) |
| outboundDate |ISO_LOCAL_DATE format |
| inboundDate | ISO_LOCAL_DATE format |
| numberOfAdults | Number of passengers |

**Response**

| Name | Description |
| ------ | ------ |
| carrier | Name of the Airline |
| basePrice | Price without tax(doesn't include discount) |
| tax | Tax which needs to be charged along with the price |
| discount | Discount which needs to be applied on the price(in percentage) |
| departureAirportName | 3 letter IATA code(eg. LHR, AMS) |
| arrivalAirportName | 3 letter IATA code(eg. LHR, AMS) |
| outboundDateTime | ISO_INSTANT format |
| inboundDateTime | ISO_INSTANT format |

**What you need to provide:**

- A solution that meets the above requirements.
- The implementation should be made as close to 'production ready' as possible within the time constraints.

**Request example:**
- ```http://localhost:8080/search?origin=LHR&%20&destination=MAD&departureDate=2023-10-31&returnDate=2023-10-31&numberOfPassengers=2```
**Response example:**
  - ```
    [
     {
      "airline": "CumbersomeJet",
      "supplier": "MongerAir",
      "fare": "95.00",
      "departureAirportCode": "LGW",
      "destinationAirportCode": "MAD",
      "departureDate": "2019-09-22T17:57:45.132+01:00[Europe/London]",
      "arrivalDate": "2019-09-23T17:57:45.132+01:00[Europe/London]"
    },
    {
      "airline": "RyanSky",
      "supplier": "MongerAir",
      "fare": "50.00",
      "departureAirportCode": "LGW",
      "destinationAirportCode": "MAD",
      "departureDate": "2019-09-22T17:57:45.132+01:00[Europe/London]",
      "arrivalDate": "2019-09-23T17:57:45.132+01:00[Europe/London]"
    },
    {
      "airline": "CumbersomeJet",
      "supplier": "HardJet",
      "fare": "75.00",
      "departureAirportCode": "LGW",
      "destinationAirportCode": "MAD",
      "departureDate": "2019-09-22T17:57:45.132+01:00[Europe/London]",
      "arrivalDate": "2019-09-23T17:57:45.132+01:00[Europe/London]"
    },
    {
      "airline": "British AirPaths",
      "supplier": "HardJet",
      "fare": "83.30",
      "departureAirportCode": "LGW",
      "destinationAirportCode": "MAD",
      "departureDate": "2019-09-22T17:57:45.132+01:00[Europe/London]",
      "arrivalDate": "2019-09-23T17:57:45.132+01:00[Europe/London]"
    }
    ]
    ```

**Requests and responses to and from "external" providers:**
- ```http://localhost:8080/find?origin=LGW&destination=MAD&departureDate=2019-09-22&returnDate=2019-09-23&passengerCount=1```

yields:

```[{"airline":"CumbersomeJet","price":95.0,"cabinclass":"Tourist","departureAirportCode":"LGW","destinationAirportCode":"MAD","departureDate":"2019-09-22T17:57:45.132","arrivalDate":"2019-09-23T17:57:45.132"},{"airline":"RyanSky","price":50.0,"cabinclass":"Tourist","departureAirportCode":"LGW","destinationAirportCode":"MAD","departureDate":"2019-09-22T17:57:45.132","arrivalDate":"2019-09-23T17:57:45.132"}]```

- ```http://localhost:8080/lookfor?from=LGW&to=MAD&outboundDate=2019-09-22&inboundDate=2019-09-23&numberOfAdults=1```

yields:

```[{"carrier":"CumbersomeJet","basePrice":70.0,"tax":5.0,"discount":0.0,"departureAirportName":"LGW","arrivalAirportName":"MAD","outboundDateTime":"2019-09-22T16:57:45.132Z","inboundDateTime":"2019-09-23T16:57:45.132Z"},{"carrier":"British AirPaths","basePrice":80.0,"tax":5.0,"discount":2.0,"departureAirportName":"LGW","arrivalAirportName":"MAD","outboundDateTime":"2019-09-22T16:57:45.132Z","inboundDateTime":"2019-09-23T16:57:45.132Z"}]```