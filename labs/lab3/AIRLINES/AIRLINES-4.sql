-- allow for 3 new attributesin each row in the airports table
-- latitude, longitude, altitude

-- invoke constraints (data type, NOT NULL)
-- Populate as follows: 
--          A-M(inclusive): lat=39deg 50' N, long =98deg 35W', alt=42
--          N-Z(inclusive): lat=36deg 50' N, long= 174deg 45E', alt=70

-- ADD NEW COLUMNS
ALTER TABLE Airports 
ADD COLUMN latitude FLOAT NOT NULL,
ADD COLUMN longitude FLOAT NOT NULL,
ADD COLUMN altitude INTEGER NOT NULL;

-- Populate based above
UPDATE Airports
    SET latitude=39+(1/60)*50,  -- + is N
        longitude=98+(35/60), -- + is W
        altitude=42
WHERE AirportCode >= "AAA" AND 
      AirportCode <= "MZZ";

-- Populate based above
UPDATE Airports
    SET latitude=-36+(1/60)*50; -- - is S
        longitude=-174+(45/60), -- E is -
        altitude=70
WHERE AirportCode >= "NAA" AND 
      AirportCode <= "ZZZ";





