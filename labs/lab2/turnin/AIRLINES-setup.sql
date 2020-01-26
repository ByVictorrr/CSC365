-- airlines.csv -- 
CREATE table `Airlines`(
	`Id` INTEGER PRIMARY KEY,
	`Airline` VARCHAR(200) NOT NULL,
	`Abbreviation` VARCHAR(200) NOT NULL,
	`Country` CHAR(3)
);


-- airports.csv --
CREATE table `Airports`(
	`City` VARCHAR(200) NOT NULL,
	`AirportCode` CHAR(3) PRIMARY KEY,
	`AirportName` VARCHAR(200) NOT NULL,
	`Country` VARCHAR(200) NOT NULL,
	`CountryAbbrev` CHAR(2) NOT NULL

);
-- flights.csv --
CREATE table `Flights`(
	`Airline` INTEGER NOT NULL,
	`FlightNo` INTEGER NOT NULL,
	`SourceAirport` CHAR(3) NOT NULL,
	`DestAirport` CHAR(3) NOT NULL,
	PRIMARY KEY (Airline, FlightNo),
	FOREIGN KEY (SourceAirport) REFERENCES Airports(AirportCode),
	FOREIGN KEY (DestAirport) REFERENCES Airports(AirportCode)

);


	
