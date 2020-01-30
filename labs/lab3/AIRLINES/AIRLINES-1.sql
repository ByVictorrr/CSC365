SELECT * FROM Flights
WHERE  NOT(SourceAirport = "AKI" OR 
            DestAirport = "AKI"
        );