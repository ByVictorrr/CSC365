--AIRLINES-1
SELECT * FROM Flights
WHERE  NOT(SourceAirport = "AKI" OR 
            DestAirport = "AKI"
        );
--AIRLINES-2

UPDATE Flights
    SET FlightNo = FlightNo + 2000
WHERE NOT(Airline = 7 OR Airline = 10 OR Airline = 12) 

--AIRLINES3

DELIMITER //
CREATE TRIGGER no_Dest_Source BEFORE INSERT ON Flights
FOR EACH ROW 
BEGIN
    IF (NEW.SourceAirport = NEW.DestAirport) THEN
        signal SQLSTATE '45000'
        SET MESSAGE_TEXT="Source airport and Destination Aiport must not be the same";
    END IF;
END //
DELIMITER ;



--AIRLINES4
-- allow for 3 new attributesin each row in the airports table
-- latitude, longitude, altitude

-- invoke constraints (data type, NOT NULL)
-- Populate as follows: 
--          A-M(inclusive): lat=39deg 50' N, long =98deg 35W', alt=42
--          N-Z(inclusive): lat=36deg 50' N, long= 174deg 45E', alt=70

-- ADD NEW COLUMNS
ALTER TABLE Airports 
ADD COLUMN latitude VARCHAR(10) NOT NULL,
ADD COLUMN longitude VARCHAR(10) NOT NULL,
ADD COLUMN altitude INTEGER NOT NULL;

-- Populate based above
UPDATE Airports
    SET latitude="39◦ 50’ N", 
        longitude="98◦ 35’ W",
        altitude=42
WHERE AirportCode >= "AAA" AND 
      AirportCode <= "MZZ";

-- Populate based above
UPDATE Airports
    SET latitude="36◦ 50’ S",
        longitude="174◦ 45’ E",
        altitude=70
WHERE AirportCode >= "NAA" AND 
      AirportCode <= "ZZZ";

-- INN1 

UPDATE Rooms
    SET basePrice = basePrice*.15+basePrice
WHERE (bedType = "King"  AND maxOccupancy = 4)
        OR basePrice <= 100);
        
--INN2
-- inn has decided to record room servce orders
--

CREATE TABLE `RoomService`(
    serviceNumber INTEGER PRIMARY KEY AUTO_INCREMENT,
    reservation CHAR(6) NOT NULL,
    orderDateTime TIMESTAMP NOT NULL,
    deliverDateTime TIMESTAMP NOT NULL,
    totalBill FLOAT,
    gratuity FLOAT,
    reservationFirstName VARCHAR(100) NOT NULL,
    FOREIGN KEY (reservation) REFERENCES Reservations(Code)
    ON DELETE RESTRICT
 );




INSERT INTO `RoomService`(reservation, orderDateTime, deliverDateTime,totalBill,gratuity,reservationFirstName) 
            VALUES 
                   ('98805', TIMESTAMP('2009-05-18 22:11:11'), TIMESTAMP('2009-05-18 22:19:11'), 65.12, 12.00, "Bill"),
                   ('98805', TIMESTAMP('2009-06-18 22:11:11'), TIMESTAMP('2009-07-18 11:18:11'), 12.00, 2.00, "Mike"),
                   ('98805', TIMESTAMP('2009-06-18 22:11:11'), TIMESTAMP('2009-07-18 22:11:11'), 65.12, 12.00, "Marry"),
                   ('10574', TIMESTAMP('2009-06-18 22:12:11'), TIMESTAMP('2009-07-18 22:21:13'), 59.14, .01, "Brandi"),
                   ('11645', TIMESTAMP('2009-07-18 22:12:11'), TIMESTAMP('2009-08-18 22:21:13'), 2.00, .12, "Victor");


                
-- INN-3
DELETE FROM Reservations WHERE 
    LastName = 'ENA' AND firstName = 'KOLB'
    -- gives result in days (182.5days = 6months)
    AND DATEDIFF(CURRENT_DATE(), STR_TO_DATE(CheckOut, '%d-%M-%y')) >= 182.5; 

-- IN-4
DELIMITER //
CREATE TRIGGER chk_rooms BEFORE INSERT ON Reservations
FOR EACH ROW
BEGIN
    DECLARE c_in DATE;
    DECLARE c_out DATE;
    DECLARE new_in DATE;
    DECLARE new_out DATE;

    SET new_in = STR_TO_DATE(NEW.CheckIn, '%d-%M-%y');
    SET new_out = STR_TO_DATE(NEW.CheckOut, '%d-%M-%y');
    -- Case 0 - new_in is greater than new-out
    -- Case 1 - new_in is before current in (or equal)
    -- Case 2 - new_in is between range of c_in and c_out (exclusive)
    if( new_in > new_out OR EXISTS(SELECT CheckIn FROM Reservations Where new_in <= STR_TO_DATE(CheckIn,'%d-%M-%y') 
                OR STR_TO_DATE(CheckIn,'%d-%M-%y') < new_in AND new_in < STR_TO_DATE(CheckOut,'%d-%M-%y')))THEN
            signal SQLSTATE '45002'
            SET MESSAGE_TEXT="Invalid reservations time - guest is still in their room";
    END IF;
END //
DELIMITER ;

