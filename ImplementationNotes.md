**Travix - BusyFlights**

**Notes:**

1.- Although not necessary for the junit tests, Controllers for the two external API s have been mocked to produce the result.

2.- The requirements related to formats (dates and money) are validated at DateAndPriceResponseTests.

3.- A requirement not asked for has been added: If any of the external services is unavailable the service does not fail, but return the flights found on the other available service. This requirement is tested at BusyFlightApplicationTest, along with the general case.

The JSON produced is as follows:

```
[
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
    },
    {
        "airline": "CumbersomeJet",
        "supplier": "MongerAir",
        "fare": "95.00",
        "departureAirportCode": "LGW",
        "destinationAirportCode": "MAD",
        "departureDate": "2019-09-22T17:57:45.132+01:00[Europe/London]",
        "arrivalDate": "2019-09-23T17:57:45.132+01:00[Europe/London]"
    }
]
```
