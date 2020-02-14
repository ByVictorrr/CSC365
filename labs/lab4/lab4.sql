-- Lab 4: JOIN and WHERE
-- vdelapla
-- Feb 14, 2020

USE `STUDENTS`;
-- STUDENTS-1
-- Find all students who study in classroom 111. For each student list first and last name. Sort the output by the last name of the student.
select FirstName, LastName from list where classroom=111 ORDER BY LastName;


USE `STUDENTS`;
-- STUDENTS-2
-- For each classroom report the grade that is taught in it. Report just the classroom number and the grade number. Sort output by classroom in descending order.
select DISTINCT classroom, grade from list ORDER BY classroom DESC;


USE `STUDENTS`;
-- STUDENTS-3
-- Find all teachers who teach fifth grade. Report first and last name of the teachers and the room number. Sort the output by room number.
select DISTINCT First,Last,T.classroom from 
list as l INNER JOIN teachers as T ON T.classroom = l.classroom AND grade=5;


USE `STUDENTS`;
-- STUDENTS-4
-- Find all students taught by OTHA MOYER. Output first and last names of students sorted in alphabetical order by their last name.
select FirstName, Lastname from list NATURAL JOIN teachers 
WHERE First = "OTHA" AND LAST="MOYER"
ORDER BY LastName;


USE `STUDENTS`;
-- STUDENTS-5
-- For each teacher teaching grades K through 3, report the grade (s)he teaches. Each name has to be reported exactly once. Sort the output by grade and alphabetically by teacher’s last name for each grade.
select DISTINCT first,last, grade from list NATURAL JOIN teachers
WHERE grade BETWEEN 0 AND 3 
ORDER BY grade, last;


USE `BAKERY`;
-- BAKERY-1
-- Find all chocolate-flavored items on the menu whose price is under $5.00. For each item output the flavor, the name (food type) of the item, and the price. Sort your output in descending order by price.
select Flavor, Food, Price from goods where Flavor = "Chocolate" AND Price < 5.00
ORDER BY Price DESC;


USE `BAKERY`;
-- BAKERY-2
-- Report the prices of the following items (a) any cookie priced above $1.10, (b) any lemon-flavored items, or (c) any apple-flavored item except for the pie. Output the flavor, the name (food type) and the price of each pastry. Sort the output in alphabetical order by the flavor and then pastry name.
select Flavor, Food, Price from goods where 
(Price > 1.1 AND Food = "Cookie") OR
(Flavor="Lemon") OR 
(Flavor="Apple" AND Food <> "Pie")
ORDER BY Flavor, Food
;


USE `BAKERY`;
-- BAKERY-3
-- Find all customers who made a purchase on October 3, 2007. Report the name of the customer (last, first). Sort the output in alphabetical order by the customer’s last name. Each customer name must appear at most once.
Select DISTINCT LastName, FirstName from customers inner join receipts ON 
CId=Customer AND SaleDate = STR_TO_DATE("10/3/2007", "%m/%d/%Y")
ORDER BY LastName
;


USE `BAKERY`;
-- BAKERY-4
-- Find all different cakes purchased on October 4, 2007. Each cake (flavor, food) is to be listed once. Sort output in alphabetical order by the cake flavor.
select DISTINCT Flavor, Food from (goods inner join items ON Item = GId)
inner join receipts ON RNumber = Receipt
WHERE SaleDate = STR_TO_DATE("10/04/2007","%m/%d/%Y") AND Food="Cake"
;


USE `BAKERY`;
-- BAKERY-5
-- List all pastries purchased by ARIANE CRUZEN on October 25, 2007. For each pastry, specify its flavor and type, as well as the price. Output the pastries in the order in which they appear on the receipt (each pastry needs to appear the number of times it was purchased).
select DISTINCT Flavor, Food, Price from 
goods INNER JOIN items ON GId=Item
INNER JOIN receipts ON RNumber=Receipt
INNER JOIN customers ON CId=Customer 
WHERE (FirstName="ARIANE" AND LastName="CRUZEN"
  AND SaleDate=STR_TO_DATE("10/25/2007","%m/%d/%Y"))
        ;


USE `BAKERY`;
-- BAKERY-6
-- Find all types of cookies purchased by KIP ARNN during the month of October of 2007. Report each cookie type (flavor, food type) exactly once in alphabetical order by flavor.

select distinct Flavor, Food
from (goods g inner join items i ON Item = GId AND Food="cookie")
inner join receipts r ON RNumber = Receipt AND SaleDate BETWEEN STR_TO_DATE("10/01/2007","%m/%d/%Y") AND STR_TO_DATE("10/31/2007","%m/%d/%Y") 
inner join customers c ON CId=r.Customer AND LastName = "ARNN" AND FirstName="KIP"
ORDER BY Flavor;


USE `CSU`;
-- CSU-1
-- Report all campuses from Los Angeles county. Output the full name of campus in alphabetical order.
select Campus from campuses
WHERE County = "Los Angeles"
ORDER BY Campus;


USE `CSU`;
-- CSU-2
-- For each year between 1994 and 2000 (inclusive) report the number of students who graduated from California Maritime Academy Output the year and the number of degrees granted. Sort output by year.
select DISTINCT d.year, d.degrees 
from degrees as d inner join campuses as c ON 
CampusId=Id AND Campus = "California Maritime Academy" AND d.year BETWEEN 1994 AND 2000
ORDER BY d.year;


USE `CSU`;
-- CSU-3
-- Report undergraduate and graduate enrollments (as two numbers) in ’Mathematics’, ’Engineering’ and ’Computer and Info. Sciences’ disciplines for both Polytechnic universities of the CSU system in 2004. Output the name of the campus, the discipline and the number of graduate and the number of undergraduate students enrolled. Sort output by campus name, and by discipline for each campus.
select Campus, d.Name, de.Gr, de.Ug
From discEnr as de inner join disciplines as d ON de.Discipline=d.Id 
AND de.Year = 2004 AND (d.Name="Math" OR d.Name="Mathematics" OR d.Name="Engineering" OR d.Name ="Computer and Info. Sciences")
inner join campuses as c ON c.Id=de.CampusId AND c.Campus LIKE "%Polytechnic%"
ORDER BY Campus, d.Name;


USE `CSU`;
-- CSU-4
-- Report graduate enrollments in 2004 in ’Agriculture’ and ’Biological Sciences’ for any university that offers graduate studies in both disciplines. Report one line per university (with the two grad. enrollment numbers in separate columns), sort universities in descending order by the number of ’Agriculture’ graduate students.
select T1.Campus, T1.Gr as Agriculture, T2.Gr as Biology
from
(select Campus, Gr
from discEnr as de inner join disciplines as d ON
    de.Discipline=d.Id AND de.year = 2004 AND de.Gr > 0 
    AND (d.Name = "Agriculture")
    inner join campuses as c ON c.Id=CampusId) as T1
INNER JOIN
(select Campus, Gr
from discEnr as de inner join disciplines as d ON
    de.Discipline=d.Id AND de.year = 2004 AND de.Gr > 0 
    AND (d.Name="Biological Sciences")
    inner join campuses as c ON c.Id=CampusId) as T2
ON T1.Campus=T2.Campus AND T1.Gr<>T2.Gr
ORDER BY Agriculture DESC
;


USE `CSU`;
-- CSU-5
-- Find all disciplines and campuses where graduate enrollment in 2004 was at least three times higher than undergraduate enrollment. Report campus names and discipline names. Sort output by campus name, then by discipline name in alphabetical order.
select Campus, d.Name, de.Ug, de.Gr
from discEnr as de inner join campuses as c ON de.CampusId=c.Id AND  
de.Year = 2004 AND 3*Ug <= Gr
inner join disciplines as d ON d.Id=de.Discipline
ORDER BY Campus, d.Name;


USE `CSU`;
-- CSU-6
-- Report the amount of money collected from student fees (use the full-time equivalent enrollment for computations) at ’Fresno State University’ for each year between 2002 and 2004 inclusively, and the amount of money (rounded to the nearest penny) collected from student fees per each full-time equivalent faculty. Output the year, the two computed numbers sorted chronologically by year.
select e.Year as year, e.FTE * fee as COLLECTED, ROUND((e.FTE*fee)/fa.FTE,2) as PER_FACULTY 
from campuses as c inner join enrollments e ON c.Id=e.CampusId 
AND c.Campus = "Fresno State University" AND e.Year BETWEEN 2002 AND 2004
inner join fees f ON f.CampusID = e.CampusId AND f.year = e.Year
inner join faculty fa ON fa.CampusID=f.CampusID AND fa.year = f.year;


USE `CSU`;
-- CSU-7
-- Find all campuses where enrollment in 2003 (use the FTE numbers), was higher than the 2003 enrollment in ’San Jose State University’. Report the name of campus, the 2003 enrollment number, the number of faculty teaching that year, and the student-to-faculty ratio, rounded to one decimal place. Sort output in ascending order by student-to-faculty ratio.
select Campus, T.FTE, f.FTE, ROUND(T.FTE/f.FTE,1) as RATIO from 
    (select Campus, R2.FTE, Enrolled, CampusId from
        (select FTE
        from enrollments e inner join campuses c ON
        e.CampusId = c.Id AND e.Year=2003 AND c.Campus="San Jose State University") as R1
    inner join
    (select Campus, FTE, Enrolled, CampusId, e.Year
        from enrollments e inner join campuses c ON
        e.CampusId = c.Id AND e.Year=2003) as R2
    ON R1.FTE < R2.FTE) as T
inner join faculty f ON f.CampusId = T.CampusId AND f.Year=2003
ORDER BY RATIO;


USE `INN`;
-- INN-1
-- Find all modern rooms with a base price below $160 and two beds. Report room names and codes in alphabetical order by the code.
select RoomCode, RoomName from rooms
WHERE basePrice < 160 AND Beds = 2 AND decor="modern"
ORDER BY RoomCode;


USE `INN`;
-- INN-2
-- Find all July 2010 reservations (a.k.a., all reservations that both start AND end during July 2010) for the ’Convoke and sanguine’ room. For each reservation report the last name of the person who reserved it, checkin and checkout dates, the total number of people staying and the daily rate. Output reservations in chronological order.
select res.LastName, res.CheckIn, res.CheckOut, res.Adults+res.Kids as staying, res.Rate
from 
(select *
from reservations 
WHERE CheckIn BETWEEN "2010-07-01" AND "2010-07-31" AND
CheckOut BETWEEN "2010-07-01" AND "2010-07-31") as res
inner join rooms ON RoomCode=res.Room AND RoomName="Convoke and sanguine"
ORDER BY CheckIn;


USE `INN`;
-- INN-3
-- Find all rooms occupied on February 6, 2010. Report full name of the room, the check-in and checkout dates of the reservation. Sort output in alphabetical order by room name.
select RoomName, CheckIn, CheckOut
from reservations inner join rooms ON RoomCode=Room 
AND CheckIn <= "2010-02-06" AND CheckOut > "2010-02-06"  
ORDER BY RoomName;


USE `INN`;
-- INN-4
-- For each stay by GRANT KNERIEN in the hotel, calculate the total amount of money, he paid. Report reservation code, checkin and checkout dates, room name (full) and the total stay cost. Sort output in chronological order by the day of arrival.

select CODE, RoomName, CheckIn, CheckOut, Rate*(DateDiff(CheckOut, CheckIn))  from 
reservations inner join rooms ON
LastName="KNERIEN" AND Room=RoomCode
ORDER BY CheckIn
;


USE `INN`;
-- INN-5
-- For each reservation that starts on December 31, 2010 report the room name, nightly rate, number of nights spent and the total amount of money paid. Sort output in descending order by the number of nights stayed.
select RoomName, Rate, DATEDIFF(CheckOut, CheckIn) AS NIGHTS_SPENT, DATEDIFF(CheckOut, CheckIn)*Rate as TOT_MON
from reservations inner join rooms
ON Room=RoomCode AND CheckIn="2010-12-31"
ORDER BY NIGHTS_SPENT DESC;


USE `INN`;
-- INN-6
-- Report all reservations in rooms with double beds that contained four adults. For each reservation report its code, the full name and the code of the room, check-in and check out dates. Report reservations in chronological order, then sorted by the three-letter room code (in alphabetical order) for any reservations that began on the same day.
select CODE, RoomCode, RoomName, CheckIn, CheckOut
from reservations inner join rooms ON RoomCode=Room 
AND bedType="Double" AND Adults=4
ORDER BY RoomCode;


USE `MARATHON`;
-- MARATHON-1
-- Report the time, pace and the overall place of TEDDY BRASEL.
select Place, RunTime, Pace
from marathon
Where FirstName="TEDDY" AND LastName="BRASEL";


USE `MARATHON`;
-- MARATHON-2
-- Report names (first, last), times, overall places as well as places in their gender-age group for all female runners from QUNICY, MA. Sort output by overall place in the race.
select FirstName, LastName, Place, RunTime, GroupPlace
from marathon
where Sex="F" AND Town="QUNICY" AND State="MA"
ORDER BY Place;


USE `MARATHON`;
-- MARATHON-3
-- Find the results for all 34-year old female runners from Connecticut (CT). For each runner, output name (first, last), town and the running time. Sort by time.
select FirstName, LastName, Town, RunTime
from marathon
WHERE Age=34 AND State="CT"
ORDER BY RunTime;


USE `MARATHON`;
-- MARATHON-4
-- Find all duplicate bibs in the race. Report just the bib numbers. Sort in ascending order of the bib number. Each duplicate bib number must be reported exactly once.
select DISTINCT m1.BibNumber
from marathon as m1 inner join marathon m2 ON 
m1.Place <> m2.Place AND m1.BibNumber=m2.BibNumber
ORDER BY m1.BibNumber
;


USE `MARATHON`;
-- MARATHON-5
-- List all runners who took first place and second place in their respective age/gender groups. For age group, output name (first, last) and age for both the winner and the runner up (in a single row). Order the output by gender, then by age group.
select m1.Sex, m1.AgeGroup, m1.FirstName, m1.LastName, m1.Age, m2.FirstName, m2.LastName, m2.Age
from marathon as m1 inner join marathon as m2 ON
m1.AgeGroup = m2.AgeGroup AND m1.Place <> m2.Place AND m1.GroupPlace=1 AND m2.GroupPlace=2 AND m1.Sex=m2.Sex
ORDER BY m1.Sex, m1.AgeGroup
;


USE `AIRLINES`;
-- AIRLINES-1
-- Find all airlines that have at least one flight out of AXX airport. Report the full name and the abbreviation of each airline. Report each name only once. Sort the airlines in alphabetical order.
select distinct Name, Abbr
from airlines inner join flights ON
Airline = Id and Source="AXX"
ORDER BY Name;


USE `AIRLINES`;
-- AIRLINES-2
-- Find all destinations served from the AXX airport by Northwest. Re- port flight number, airport code and the full name of the airport. Sort in ascending order by flight number.

select R1.FlightNo, R1.Destination, R2.Name
from
(select * from flights inner join airlines ON
Source="AXX" AND Airline=Id AND Name="Northwest Airlines") as R1
inner join airports as R2 ON R2.Code=R1.Destination
ORDER BY R1.FlightNo
;


USE `AIRLINES`;
-- AIRLINES-3
-- Find all *other* destinations that are accessible from AXX on only Northwest flights with exactly one change-over. Report pairs of flight numbers, airport codes for the final destinations, and full names of the airports sorted in alphabetical order by the airport code.
Select T.FlightNo, f.FlightNo, a.Code, a.Name
from
(select R1.FlightNo, R1.Destination, R2.Name, Airline
from
(select * from flights inner join airlines ON
Source="AXX" AND Airline=Id AND Name="Northwest Airlines") as R1
inner join airports as R2 ON R2.Code=R1.Destination
ORDER BY R1.FlightNo) as T 
inner join flights as f ON
T.Destination = f.source AND f.Destination<>"AXX" AND T.Airline=f.Airline
inner join airports a ON f.Destination = a.Code ;


USE `AIRLINES`;
-- AIRLINES-4
-- Report all pairs of airports served by both Frontier and JetBlue. Each airport pair must be reported exactly once (if a pair X,Y is reported, then a pair Y,X is redundant and should not be reported).
Select DISTINCT LEAST(T1.Source, T1.Destination), GREATEST(T1.Source, T1.Destination)
from
(select * from airlines a inner join flights f on 
    f.Airline=a.Id AND a.Abbr="JetBlue") as T1
    inner join 
(select * from airlines a inner join flights f on 
    f.Airline=a.Id AND a.Abbr="Frontier") as T2
ON T1.Source=T2.Source AND T1.Destination = T2.Destination;
;


USE `AIRLINES`;
-- AIRLINES-5
-- Find all airports served by ALL five of the airlines listed below: Delta, Frontier, USAir, UAL and Southwest. Report just the airport codes, sorted in alphabetical order.
select Distinct T5.Source from 
(select Distinct T3.Source from 
    (select Distinct T1.Source from 
        (select Source from flights f INNER JOIN airlines al ON Airline=Id AND al.abbr="Frontier" inner join airports ap ON Code=Source) as T1
        inner join
        (select Source from flights f INNER JOIN airlines al ON Airline=Id AND al.abbr="Delta" inner join airports ap ON Code=Source) as T2
        ON T1.Source = T2.Source
    ) as T3 inner join
      (select Distinct T1.Source from 
        (select Source from flights f INNER JOIN airlines al ON Airline=Id AND al.abbr="USAir" inner join airports ap ON Code=Source) as T1
        inner join
        (select Source from flights f INNER JOIN airlines al ON Airline=Id AND al.abbr="UAL" inner join airports ap ON Code=Source) as T2
        ON T1.Source = T2.Source
    ) as T4 
    ON T3.Source=T4.source) as T5
    inner join (select Source from flights f INNER JOIN airlines al ON Airline=Id AND al.abbr="Southwest" inner join airports ap ON Code=Source) as T6
    ON T5.Source=T6.Source
    ORDER BY T5.Source
    ;


USE `AIRLINES`;
-- AIRLINES-6
-- Find all airports that are served by at least three Southwest flights. Report just the three-letter codes of the airports — each code exactly once, in alphabetical order.
select distinct T4.Source from 
    (select T2.Source, T1.FlightNo AS FNO1, T2.FlightNo As FNO2 from 
        (select * from flights f inner join airlines al ON f.Airline = al.Id and al.Abbr="Southwest") as T1 inner join 
        (select * from flights f inner join airlines al ON f.Airline = al.Id and al.Abbr="Southwest") as T2 
        ON T1.Source= T2.Source AND T1.FlightNo<>T2.FlightNo) as T3 inner join 
    (select * from flights f inner join airlines al ON f.Airline = al.Id and al.Abbr="Southwest") as T4 ON T4.Source= T3.Source AND T3.FNO1<>T4.FlightNo AND T3.FNO2 <> T4.FlightNo
    ORDER BY T4.Source;
    
;


USE `KATZENJAMMER`;
-- KATZENJAMMER-1
-- Report, in order, the tracklist for ’Le Pop’. Output just the names of the songs in the order in which they occur on the album.
select Songs.Title from 
Albums as A inner join Tracklists as T 
ON A.Title="Le Pop" AND AId=Album inner join Songs ON Song=SongId;


USE `KATZENJAMMER`;
-- KATZENJAMMER-2
-- List the instruments each performer plays on ’Mother Superior’. Output the first name of each performer and the instrument, sort alphabetically by the first name.
select FirstName, Instrument from
Songs s inner join Instruments i ON s.SongId=i.Song AND s.Title="Mother Superior"
inner join Band b On Bandmate=b.Id
ORDER BY FirstName;


USE `KATZENJAMMER`;
-- KATZENJAMMER-3
-- List all instruments played by Anne-Marit at least once during the performances. Report the instruments in alphabetical order (each instrument needs to be reported exactly once).
select distinct Instrument
from Band b inner join Performance p ON b.Id=p.Bandmate
inner join Instruments i ON i.Song=p.Song AND p.Bandmate=i.Bandmate
WHERE FirstName="Anne-Marit"
ORDER BY Instrument;


USE `KATZENJAMMER`;
-- KATZENJAMMER-4
-- Find all songs that featured ukalele playing (by any of the performers). Report song titles in alphabetical order.
Select distinct Title from
Instruments i inner join Songs s ON i.Song=s.SongId AND Instrument="ukalele"
ORDER BY Title;


USE `KATZENJAMMER`;
-- KATZENJAMMER-5
-- Find all instruments Turid ever played on the songs where she sang lead vocals. Report the names of instruments in alphabetical order (each instrument needs to be reported exactly once).
select distinct Instrument from 
Band b inner join Vocals v ON Firstname="Turid" AND b.Id=v.Bandmate AND v.VocalType="lead"
inner join Songs s ON v.Song=s.SongId 
inner join Instruments i ON v.Song=i.Song AND v.Bandmate=i.Bandmate
ORDER BY Instrument
;


USE `KATZENJAMMER`;
-- KATZENJAMMER-6
-- Find all songs where the lead vocalist is not positioned center stage. For each song, report the name, the name of the lead vocalist (first name) and her position on the stage. Output results in alphabetical order by the song, then name of band member. (Note: if a song had more than one lead vocalist, you may see multiple rows returned for that song. This is the expected behavior).
select s.Title, b.FirstName, p.StagePosition
from Vocals v inner join Songs s ON v.VocalType="lead" AND s.SongId=v.Song
INNER JOIN Performance p ON v.Bandmate=p.Bandmate AND v.Song=p.Song AND p.StagePosition<>"center"
INNER JOIN Band b ON v.Bandmate=b.Id
ORDER BY s.Title, b.FirstName;


USE `KATZENJAMMER`;
-- KATZENJAMMER-7
-- Find a song on which Anne-Marit played three different instruments. Report the name of the song. (The name of the song shall be reported exactly once)
select rs.Title from
	(select distinct T4.Song from 
		(select T1.Song as Song, T1.Instrument as i1, T2.Instrument as i2 from 
		    (select * from Band b inner join Instruments i ON Firstname="Anne-Marit" AND i.Bandmate=b.Id) as T1
			    inner join 
		    (select * from Band b inner join Instruments i ON Firstname="Anne-Marit" AND i.Bandmate=b.Id) as T2
			    ON T1.Song=T2.Song AND T1.Instrument <> T2.Instrument ) as T3
			inner join	
		(select * from Band b inner join Instruments i ON Firstname="Anne-Marit" AND i.Bandmate=b.Id) as T4
			ON T3.Song=T4.Song AND i2<>T4.Instrument AND i1<>T4.Instrument) as R 
		inner join Songs as rs
		ON rs.SongId=R.Song;


USE `KATZENJAMMER`;
-- KATZENJAMMER-8
-- Report the positioning of the band during ’A Bar In Amsterdam’. (just one record needs to be returned with four columns (right, center, back, left) containing the first names of the performers who were staged at the specific positions during the song).
select T1.FN, T2.FN, T3.FN, T4.FN from
	(select StagePosition as sp, FirstName as FN from Songs s inner join Performance p ON s.Title="A Bar In Amsterdam" AND s.SongId=p.Song inner join Band b ON b.Id=p.Bandmate WHERE StagePosition="right") as T1 	
	inner join 
	(select StagePosition as sp, FirstName as FN from Songs s inner join Performance p ON s.Title="A Bar In Amsterdam" AND s.SongId=p.Song inner join Band b ON b.Id=p.Bandmate WHERE StagePosition="center") as T2
	ON T1.sp<>T2.sp
	inner join 	
	(select StagePosition as sp, FirstName as FN from Songs s inner join Performance p ON s.Title="A Bar In Amsterdam" AND s.SongId=p.Song inner join Band b ON b.Id=p.Bandmate WHERE StagePosition="back") as T3
	ON T3.sp <> T2.sp AND T3.sp <> T1.sp 
	inner join	
	(select StagePosition as sp, FirstName as FN from Songs s inner join Performance p ON s.Title="A Bar In Amsterdam" AND s.SongId=p.Song inner join Band b ON b.Id=p.Bandmate WHERE StagePosition="left") as T4
	ON T4.sp<> T3.sp AND T4.sp<> T2.sp AND T4.sp <> T1.sp;


