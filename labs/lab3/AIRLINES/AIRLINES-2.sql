UPDATE Flights
    SET FlightNo = FlightNo + 2000
WHERE NOT(Airline = 7 OR Airline = 10 OR Airline = 12) ;