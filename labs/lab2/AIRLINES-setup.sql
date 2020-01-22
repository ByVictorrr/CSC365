-- Airlines.csv -- 
CREATE table `airlines`(
	`Id` INTEGER PRIMARY KEY,
	`Airline` VARCHAR(200) NOT NULL,
	`Abbreviation` VARCHAR(100) NOT NULL,
	Unique (Airline)
);

-- flights.csv --
CREATE table `flights`(
	`Airline` INTEGER NOT NULL,
	`FlightNo` INTEGER PRIMARY KEY,
	`SourceAirport` CHAR(3) NOT NULL,
	`DestAirport` CHAR(3) NOT NULL
);


-- airports.csv --
CREATE table `airports`(
	`City` VARCHAR(200) NOT NULL,
	`AirportCode` CHAR(3) PRIMARY KEY,
	`AirportName` VARCHAR(200) NOT NULL,
	`Country` VARCHAR(200) NOT NULL,
	`CountryAbbrev` CHAR(2) NOT NULL


);
	
