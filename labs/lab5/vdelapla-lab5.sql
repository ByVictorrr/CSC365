-- Lab 5
-- vdelapla
-- Feb 19, 2020

USE `AIRLINES`;
-- AIRLINES-1
-- Find all airports with exactly 17 outgoing flights. Report airport code and the full name of the airport sorted in alphabetical order by the code.
SELECT 
	ap.Code, ap.Name
FROM
	flights f, airports ap
WHERE 
	f.Source=ap.Code
GROUP BY 
	f.Source
HAVING
	COUNT(*)=17
ORDER BY 
    f.Source
;


USE `AIRLINES`;
-- AIRLINES-2
-- Find the number of airports from which airport ANP can be reached with exactly one transfer. Make sure to exclude ANP itself from the count. Report just the number.
SELECT COUNT(DISTINCT T1.Source)
FROM 
    (SELECT 
	    *
    FROM 
	    flights f
    WHERE 
	    f.Source NOT IN ('ANP') AND f.Destination NOT IN ('ANP')
    ) as T1
INNER JOIN 
	flights T2
ON 
    T2.Source=T1.Destination AND T2.Destination="ANP"

;


USE `AIRLINES`;
-- AIRLINES-3
-- Find the number of airports from which airport ATE can be reached with at most one transfer. Make sure to exclude ATE itself from the count. Report just the number.
SELECT 
	COUNT(*)
FROM 
    ((SELECT 
	    f1.Source as Source
	FROM 
		flights f1, flights f2
	WHERE 
		f1.Source NOT IN ('ATE') AND 
		f2.Source NOT IN ('ATE') AND
		f1.Destination NOT IN ('ATE') AND
		f1.Destination=f2.Source AND	     
		f2.Destination="ATE" AND
		f1.FlightNo <> f2.FlightNo)
	UNION DISTINCT 
	(SELECT 
		f.Source as Source
	FROM
		flights f 
	WHERE 
		f.Source NOT IN ('ATE') AND 
		f.Destination="ATE"
	)) as A;


USE `AIRLINES`;
-- AIRLINES-4
-- For each airline, report the total number of airports from which it has at least one outgoing flight. Report the full name of the airline and the number of airports computed. Report the results sorted by the number of airports in descending order. In case of tie, sort by airline name A-Z.
SELECT 
	Name, COUNT(DISTINCT Source)
    FROM
	    flights f, airlines a
    WHERE 
	    f.Source<>f.Destination
	    AND a.Id=f.Airline
GROUP BY
	Name
ORDER BY
	COUNT(DISTINCT Source) DESC,Name;


USE `BAKERY`;
-- BAKERY-1
-- For each flavor which is found in more than three types of items sold by the bakery, report the average price (rounded to the nearest penny) of an item of this flavor and the total number of different items of this flavor on the menu. Sort the output in ascending order by the average price.
SELECT
	Flavor, ROUND(AVG(PRICE),2), COUNT(Food)
FROM
	goods g
GROUP BY
	Flavor
HAVING
	COUNT(Food) > 3
ORDER BY
	AVG(PRICE);


USE `BAKERY`;
-- BAKERY-2
-- Find the total amount of money the bakery earned in October 2007 from selling eclairs. Report just the amount.
SELECT
	SUM(PRICE)
FROM
	goods g, items i, receipts r
WHERE
	g.GId=i.Item AND
	r.RNumber=i.Receipt AND
	SaleDate BETWEEN '2007-10-01' AND '2007-10-31' AND
	g.Food='Eclair';


USE `BAKERY`;
-- BAKERY-3
-- For each visit by NATACHA STENZ output the receipt number, date of purchase, total number of items purchased and amount paid, rounded to the nearest penny. Sort by the amount paid, most to least.
SELECT
	r.RNumber, r.SaleDate, COUNT(*), ROUND(SUM(g.PRICE),2)
FROM
	receipts r, items i, customers c, goods g
WHERE
	i.Receipt=r.RNumber AND
	c.CId=r.Customer AND
	g.GId=i.Item AND
	c.LastName="STENZ" AND
	c.FirstName="NATACHA"
GROUP BY
	r.RNumber
ORDER BY
	SUM(g.PRICE) DESC;


USE `BAKERY`;
-- BAKERY-4
-- For each day of the week of October 8 (Monday through Sunday) report the total number of purchases (receipts), the total number of pastries purchased and the overall daily revenue. Report results in chronological order and include both the day of the week and the date.
SELECT
	DAYNAME(SaleDate), SaleDate, COUNT(DISTINCT r.RNumber), 
	COUNT(i.Item), ROUND(SUM(g.PRICE), 2)
FROM
	items i, goods g, receipts r
WHERE
	i.Receipt=r.RNumber AND
	i.Item=g.GId AND
	SaleDate BETWEEN '2007-10-08' AND '2007-10-14'	
GROUP BY
	DAYNAME(SaleDate), SaleDate
ORDER BY
	SaleDate;


USE `BAKERY`;
-- BAKERY-5
-- Report all days on which more than ten tarts were purchased, sorted in chronological order.
SELECT
	SaleDate	
FROM
	goods g, items i, receipts r
WHERE
	g.GId=i.Item AND
	i.Receipt=r.RNumber AND
	g.Food="Tart"	
GROUP BY
	SaleDate
HAVING
	COUNT(*) > 10;


USE `CSU`;
-- CSU-1
-- For each campus that averaged more than $2,500 in fees between the years 2000 and 2005 (inclusive), report the total cost of fees for this six year period. Sort in ascending order by fee.
SELECT
	c.Campus, SUM(f.fee)
FROM
	campuses c, fees f
WHERE
	c.Id=f.CampusId AND
	f.Year BETWEEN '2000' AND '2005'
GROUP BY
	c.Campus
HAVING
	AVG(f.fee) > 2500
ORDER BY
    SUM(f.fee)
;


USE `CSU`;
-- CSU-2
-- For each campus for which data exists for more than 60 years, report the average, the maximum and the minimum enrollment (for all years). Sort your output by average enrollment.
SELECT
	C.Campus, MIN(E.Enrolled), AVG(E.Enrolled), MAX(E.Enrolled)
FROM
	 enrollments E,
	 (SELECT DISTINCT 
	        c.Campus, c.Id 
	  FROM 
	    campuses c, enrollments e 
	  WHERE 
	    c.Id=e.CampusId AND 2004 - e.Year  > 60
	  ) AS C 
WHERE 
	E.CampusId=C.Id
	
GROUP BY
	C.Campus
ORDER BY
    AVG(E.Enrolled);


USE `CSU`;
-- CSU-3
-- For each campus in LA and Orange counties report the total number of degrees granted between 1998 and 2002 (inclusive). Sort the output in descending order by the number of degrees.

SELECT
	c.Campus, SUM(d.degrees)
FROM
	campuses c, degrees d
WHERE
	c.Id=d.CampusId AND
	c.County IN ("Los Angeles", "Orange") AND
	d.Year BETWEEN 1998 AND 2002 
GROUP BY
	c.Campus
ORDER BY
	SUM(d.degrees) DESC;


USE `CSU`;
-- CSU-4
-- For each campus that had more than 20,000 enrolled students in 2004, report the number of disciplines for which the campus had non-zero graduate enrollment. Sort the output in alphabetical order by the name of the campus. (Exclude campuses that had no graduate enrollment at all.)
SELECT
	c.Campus, COUNT(dE.Discipline)
FROM
	discEnr dE, campuses c, enrollments e
WHERE
	dE.Gr > 0 AND
	c.Id=dE.CampusId AND
	c.Id=e.CampusId AND
	e.Year = 2004 AND
	e.Enrolled > 20000
GROUP BY
	c.Campus


ORDER BY
	c.Campus;


USE `INN`;
-- INN-1
-- For each room, report the total revenue for all stays and the average revenue per stay generated by stays in the room that began in the months of September, October and November. Sort output in descending order by total revenue. Output full room names.
SELECT
	rm.RoomName, SUM(res.Rate*DATEDIFF(res.CheckOut,res.CheckIn)) 
	as tot, ROUND(AVG(res.Rate*DATEDIFF(res.CheckOut,res.CheckIn)),2)
FROM
	rooms rm, reservations res
WHERE
	rm.RoomCode=res.Room AND
	MONTH(res.CheckIn) IN (9,10,11)
GROUP BY
	rm.RoomName
ORDER BY
	tot DESC;


USE `INN`;
-- INN-2
-- Report the total number of reservations that began on Fridays, and the total revenue they brought in.
SELECT 
	COUNT(*), SUM(DATEDIFF(res.CheckOut,res.CheckIn)*res.rate)
FROM
	reservations res, rooms rm
WHERE
	DAYNAME(res.CheckIn) = "Friday" AND
	res.Room = rm.RoomCode;


USE `INN`;
-- INN-3
-- For each day of the week, report the total number of reservations that began on that day and the total revenue for these reservations. Report days of week as Monday, Tuesday, etc. Order days from Sunday to Saturday.
SELECT
	DAYNAME(res.CheckIn), COUNT(*), 
	SUM(DATEDIFF(res.CheckOut, res.CheckIn)*res.Rate)
FROM
	reservations res
GROUP BY
	DAYNAME(res.CheckIn), DAYOFWEEK(res.CheckIn)
ORDER BY
	DAYOFWEEK(res.CheckIn);


USE `INN`;
-- INN-4
-- For each room report the highest markup against the base price and the largest markdown (discount). Report markups and markdowns as the signed difference between the base price and the rate. Sort output in descending order beginning with the largest markup. In case of identical markup/down sort by room name A-Z. Report full room names.
SELECT 
	rm.RoomName, MAX(res.Rate-rm.basePrice) as markup, MIN(res.Rate-rm.basePrice) as markdown
FROM
	reservations res, rooms rm
WHERE
	res.Room=rm.RoomCode	
GROUP BY
	rm.RoomName
ORDER BY
    markup DESC,markdown ,rm.RoomName;


USE `INN`;
-- INN-5
-- For each room report how many nights in calendar year 2010 the room was occupied. Report the room code, the full name of the room and the number of occupied nights. Sort in descending order by occupied nights. (Note: it has to be number of nights in 2010. The last reservation in each room can go beyond December 31, 2010, so the ”extra” nights in 2011 need to be deducted).
SELECT 
	T.RoomCode, T.RoomName, SUM(DATEDIFF(T.CheckOut,T.CheckIn)) as daysOc
FROM 
    ((SELECT rm.RoomName,rm.RoomCode, res.Checkin, res.CheckOut
    FROM
	    rooms rm, reservations res	
    WHERE
	    rm.RoomCode=res.Room AND
	    YEAR(res.CheckIn) = 2010 AND
	    YEAR(res.CheckOut) = 2010
    ) 
    UNION 
    (SELECT rm.RoomName, rm.RoomCode, res.Checkin, DATE_SUB(res.CheckOut,INTERVAL DATEDIFF(res.CheckOut,'2010-12-31') DAY) as CheckOut
    FROM
	    rooms rm, reservations res	
    WHERE
	    rm.RoomCode=res.Room AND
	    YEAR(res.CheckIn) = 2010 AND
	    YEAR(res.CheckOut) > 2010
    )) T	
GROUP BY
	T.RoomCode, T.RoomName
ORDER BY
	daysOc DESC;


USE `KATZENJAMMER`;
-- KATZENJAMMER-1
-- For each performer (by first name) report how many times she sang lead vocals on a song. Sort output in descending order by the number of leads.
SELECT
	b.FirstName, COUNT(DISTINCT Song)
FROM
	Band b, Vocals v
WHERE
	b.Id=v.Bandmate AND
	v.VocalType = "lead"
GROUP BY
	b.FirstName

ORDER BY
	COUNT(*) DESC;


USE `KATZENJAMMER`;
-- KATZENJAMMER-2
-- Report how many different instruments each performer plays on songs from the album 'Le Pop'. Sort the output by the first name of the performers.
SELECT
	b.FirstName ,COUNT(DISTINCT i.Instrument)
FROM
	Band b, Albums a, Instruments i, Tracklists t
WHERE
	b.Id=i.Bandmate AND
	i.Song=t.Song AND
	a.AId = t.Album AND
	a.Title="Le Pop"
GROUP BY
    b.FirstName
ORDER BY	
	b.FirstName;


USE `KATZENJAMMER`;
-- KATZENJAMMER-3
-- Report the number of times Turid stood at each stage position when performing live. Sort output in ascending order of the number of times she performed in each position.

SELECT
	p.StagePosition, COUNT(*)
FROM
	Band b, Performance p
WHERE
	b.Id=p.Bandmate AND
	b.FirstName="Turid"
GROUP BY
	p.StagePosition
ORDER BY
	COUNT(*);


USE `KATZENJAMMER`;
-- KATZENJAMMER-4
-- Report how many times each performer (other than Anne-Marit) played bass balalaika on the songs where Anne-Marit was positioned on the left side of the stage. Sort output alphabetically by the name of the performer.

SELECT
	b.FirstName, COUNT(*) as bass
FROM
	Band b, Instruments i,
	(Select 
		s.SongId
	FROM
		Songs s, Band b, Performance p
	WHERE
		s.SongId=p.Song AND
		b.Id=p.Bandmate AND
		b.FirstName="Anne-Marit" AND
		p.StagePosition in ('left')
	) as S
WHERE
	b.Id=i.Bandmate AND
	i.Instrument="bass balalaika" AND
	S.SongId=i.Song AND
	b.FirstName <> "Anne-Marit"
GROUP BY
	b.FirstName
	
ORDER BY
	b.FirstName;


USE `KATZENJAMMER`;
-- KATZENJAMMER-5
-- Report all instruments (in alphabetical order) that were played by three or more people.
SELECT
	i.Instrument
FROM
	Instruments i, Performance p
WHERE
	i.Song=p.Song AND
	p.Bandmate=i.Bandmate
GROUP BY
	i.Instrument
HAVING
	COUNT(DISTINCT p.Bandmate) >= 3
ORDER BY
    i.Instrument;


USE `KATZENJAMMER`;
-- KATZENJAMMER-6
-- For each performer, report the number of times they played more than one instrument on the same song. Sort output in alphabetical order by first name of the performer
SELECT 
	b.FirstName, COUNT(*)
FROM
	Band b,
	(SELECT DISTINCT
		i1.Bandmate, i1.Song
	FROM 
			Instruments i1, Instruments i2
	WHERE
		i1.Song=i2.Song AND
		i1.Bandmate=i2.Bandmate AND
		i1.Instrument <> i2.Instrument
	GROUP BY
		i1.Song,i1.Bandmate
	) M1
WHERE
    M1.Bandmate=b.Id
GROUP BY
	M1.Bandmate
ORDER BY
    b.FirstName;


USE `MARATHON`;
-- MARATHON-1
-- For each gender/age group, report total number of runners in the group, the overall place of the best runner in the group and the overall place of the slowest runner in the group. Output result sorted by age group and sorted by gender (F followed by M) within each age group.
SELECT
	m.AgeGroup, m.Sex, COUNT(*), MIN(m.Place), MAX(m.Place)
FROM
	marathon m
GROUP BY
	m.AgeGroup, m.Sex
ORDER BY
    m.AgeGroup, m.Sex;


USE `MARATHON`;
-- MARATHON-2
-- Report the total number of gender/age groups for which both the first and the second place runners (within the group) are from the same state.
SELECT
    COUNT(DISTINCT m1.AgeGroup)
FROM
	marathon m1, marathon m2
WHERE
	(m1.GroupPlace=1 OR m1.GroupPlace=2) AND
	(m2.GroupPlace=1 OR m2.GroupPlace=2) AND
	m1.Place <> m2.Place AND
	m1.State = m2.State AND
	m1.AgeGroup=m2.AgeGroup AND
	m1.Sex<>m2.Sex;


USE `MARATHON`;
-- MARATHON-3
-- For each full minute, report the total number of runners whose pace was between that number of minutes and the next. In other words: how many runners ran the marathon at a pace between 5 and 6 mins, how many at a pace between 6 and 7 mins, and so on.
SELECT
	TIME_FORMAT(m.Pace,'%i') as PaceMin, COUNT(*)
FROM
	marathon m
GROUP BY
	PaceMin;


USE `MARATHON`;
-- MARATHON-4
-- For each state with runners in the marathon, report the number of runners from the state who finished in top 10 in their gender-age group. If a state did not have runners in top 10, do not output information for that state. Sort in descending order by the number of top 10 runners.
SELECT
    m.State, COUNT(*)
FROM
	marathon m
WHERE
	m.GroupPlace BETWEEN 1 AND 10
GROUP BY
	m.State
HAVING
	COUNT(*) > 0
ORDER BY
	COUNT(*) DESC;


USE `MARATHON`;
-- MARATHON-5
-- For each Connecticut town with 3 or more participants in the race, report the average time of its runners in the race computed in seconds. Output the results sorted by the average time (lowest average time first).
SELECT
	m.Town, ROUND(AVG(TIME_TO_SEC(m.RunTime)),1) as avg
FROM
	marathon m
WHERE
	m.State="CT"
GROUP BY
	m.Town
HAVING
	COUNT(*) >= 3
ORDER BY
	avg;


USE `STUDENTS`;
-- STUDENTS-1
-- Report the names of teachers who have between seven and eight (inclusive) students in their classrooms. Sort output in alphabetical order by the teacher's last name.
SELECT
	t.Last, t.First
FROM
	list l, teachers t
WHERE
	l.classroom=t.classroom
GROUP BY
	t.Last,t.First
HAVING
	COUNT(*) BETWEEN 7 AND 8
ORDER BY
	t.Last;


USE `STUDENTS`;
-- STUDENTS-2
-- For each grade, report the number of classrooms in which it is taught and the total number of students in the grade. Sort the output by the number of classrooms in descending order, then by grade in ascending order.

SELECT
	l.grade, COUNT(DISTINCT l.classroom) as n_rooms, COUNT(*) as n_std
FROM
	list l
GROUP BY
	l.grade
ORDER BY
	n_rooms DESC, l.grade;


USE `STUDENTS`;
-- STUDENTS-3
-- For each Kindergarten (grade 0) classroom, report the total number of students. Sort output in the descending order by the number of students.
SELECT 
	l.classroom, COUNT(*) as stds
FROM
	list l	
WHERE
	l.grade=0
GROUP BY
	l.classroom
ORDER BY
	stds DESC;


USE `STUDENTS`;
-- STUDENTS-4
-- For each fourth grade classroom, report the student (last name) who is the last (alphabetically) on the class roster. Sort output by classroom.
SELECT 
	l.classroom, MAX(l.LastName)
FROM
	list l
WHERE
	l.grade=4
GROUP BY
	l.classroom
ORDER BY
	l.classroom;


