-- inn has decided to record room servce orders
--

CREATE TABLE `RoomService`(
    serviceNumber INTEGER PRIMARY KEY AUTO_INCREMENT,
    reservation CHAR(6) NOT NULL,
    orderDateTime TIMESTAMP NOT NULL,
    deliverDateTime TIMESTAMP NOT NULL,
    totalBill FLOAT,
    gratuity FLOAT,
    reservationFirstName VARCHAR(100) NOT NULL
 );
    --FOREIGN KEY (reservation) REFERENCES Reservations(Code)



--CREATE TRIGGER chk_reservation_time BEFORE INSERT(
-- checks the orderDateTime and deliverDatetime to see if it matches Reservation cHECKIN and out time
-- also deliver time > order time


INSERT INTO `RoomService`(reservation, orderDateTime, deliverDateTime,totalBill,gratuity,reservationFirstName) 
            VALUES 
                   ('98805', TIMESTAMP('2009-05-18 22:11:11'), TIMESTAMP('2009-05-18 22:19:11'), 65.12, 12.00, "Bill"),
                   ('98805', TIMESTAMP('2009-06-18 22:11:11'), TIMESTAMP('2009-07-18 11:18:11'), 12.00, 2.00, "Mike"),
                   ('98805', TIMESTAMP('2009-06-18 22:11:11'), TIMESTAMP('2009-07-18 22:11:11'), 65.12, 12.00, "Marry"),
                   ('10574', TIMESTAMP('2009-06-18 22:12:11'), TIMESTAMP('2009-07-18 22:21:13'), 59.14, .01, "Brandi"),
                   ('11645', TIMESTAMP('2009-07-18 22:12:11'), TIMESTAMP('2009-08-18 22:21:13'), 2.00, .12, "Victor");