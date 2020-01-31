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


-- Trigger: to prevent reservatoins from overlapping on the same room
-- NOTE: its valid for a checkin to occur on the same day as checkout 
