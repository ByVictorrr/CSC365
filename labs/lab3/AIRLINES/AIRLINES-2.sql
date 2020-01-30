UPDATE Flights
    SET FlightNo = FlightNo + 2000
WHERE NOT(Airline = 7 OR Airline = 10 OR Airline = 12) ;

/*
SELECT F.Airline, F.FlightNo+2000, F.SourceAirport, F.DestAirport 
FROM (Flights AS F INNER JOIN Airlines AS A ON A.ID = F.Airline)
    WHERE NOT(A.Airline = "Contential" OR 
          A.Airline = "AirTran" OR 
          A.Airline = "Virgin");

*/
