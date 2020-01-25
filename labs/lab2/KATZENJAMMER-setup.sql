-- Albums.csv
CREATE TABLE `Albums`(
	`AId` INTEGER PRIMARY KEY,
	`Title` VARCHAR(100) NOT NULL,	
	`Year` INTEGER NOT NULL,
	`Label` VARCHAR(100) NOT NULL,
	`Type` VARCHAR(20) NOT NULL
);

-- Band.csv
CREATE TABLE `Band`(
	`Id` INTEGER PRIMARY KEY,
	`FirstName` VARCHAR(100) NOT NULL,
	`LastName` VARCHAR(100) NOT NULL
);
--PRIMARY KEY STILL
--  Instruments.csv
CREATE TABLE `Instruments`(
	`SongId` INTEGER NOT NULL,
	`BandmateId` INTEGER NOT NULL,
	`Instrument` VARCHAR(100) NOT NULL,
	--PRIMARY KEY (SongId, )
	FOREIGN KEY (SongId) REFERENCES Songs(SongId),
	FOREIGN KEY (BandmateId) REFERENCES Band(Id)
);
--PRIMARY KEY STILL
--  Performance.csv
CREATE TABLE `Performance`(
	`SongId` INTEGER NOT NULL,
	`Bandmate`INTEGER NOT NULL,
	`StagePosition` VARCHAR(100) NOT NULL,
	FOREIGN KEY (SongId) REFERENCES Songs(SongId),
	FOREIGN KEY (Bandmate) REFERENCES Band(Id)
);

--  Songs.csv
CREATE TABLE `Songs`(
	`SongId` INTEGER PRIMARY KEY,
	`Title` VARCHAR(100) NOT NULL,
);



--PRIMARY KEY STILL
--  Tracklists.csv
CREATE TABLE `Tracklists`(
	`AlbumId` INTEGER NOT NULL,
	`Postion` INTEGER NOT NULL,
	`SongId` INTEGER NOT NULL,
	FOREIGN KEY (SongId) REFERENCES Songs(SongId),
	FOREIGN KEY (AlbumId) REFERENCES Albums(AId)
);

--  Vocals.csv
CREATE TABLE `Vocals`(
	`SongId` INTEGER NOT NULL,
	`Bandmate` INTEGER NOT NULL,
	`Type` VARCHAR(100) NOT NULL,
	FOREIGN KEY (SongId) REFERENCES Songs(SongId),
	FOREIGN KEY (Bandmate) REFERENCES Band(Id)
);







