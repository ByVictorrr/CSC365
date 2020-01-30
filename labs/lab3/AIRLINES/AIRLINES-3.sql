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

