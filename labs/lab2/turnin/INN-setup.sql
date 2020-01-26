-- Rooms.csv
-- Rooms.csv
CREATE TABLE `Rooms`(
	`RoomId` CHAR(3) PRIMARY KEY,
	`roomName` VARCHAR(100) NOT NULL,
	`beds` INTEGER NOT NULL,
	`bedType` VARCHAR(10) NOT NULL,
	`maxOccupancy` INTEGER NOT NULL,
	`basePrice` INTEGER NOT NULL,
	`decor` VARCHAR(20) NOT NULL
);
-- Reservations.csv
CREATE TABLE `Reservations`(
	`Code` CHAR(6) PRIMARY KEY,
	`Room` CHAR(3) NOT NULL,
	`CheckIn` CHAR(9) NOT NULL,
	`CheckOut` CHAR(9) NOT NULL,  -- not sure if need to change to date time
	`Rate` FLOAT NOT NULL,
	`LastName` VARCHAR(20) NOT NULL,
	`firstName` VARCHAR(20) NOT NULL,
	`Adults` INTEGER NOT NULL,
	`Kids` INTEGER NOT NULL,
	FOREIGN KEY (Room) REFERENCES Rooms(RoomId)
);

