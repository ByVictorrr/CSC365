-- Lab 6
-- vdelapla
-- Mar 2, 2020

USE `BAKERY`;
-- BAKERY-1
-- Find all customers who did not make a purchase between October 5 and October 11 (inclusive) of 2007. Output first and last name in alphabetical order by last name.
SELECT FirstName, LastName 
FROM customers c
WHERE NOT EXISTS(
	-- all customers that made purchases from 5-11	
	SELECT SaleDate
	FROM receipts r
	WHERE r.SaleDate BETWEEN "2007-10-05" AND "2007-10-11"
		AND c.CId=r.Customer
	
)
ORDER BY LastName;


USE `BAKERY`;
-- BAKERY-2
-- Find the customer(s) who spent the most money at the bakery during October of 2007. Report first, last name and total amount spent (rounded to two decimal places). Sort by last name.
WITH cust_tot_oct_2007 AS (
	-- each customers full purchases in oct 2007
	SELECT FirstName, LastName, SUM(PRICE) as MoneySpent, c.CId
	FROM goods g, items i, receipts r, customers c
	WHERE i.Item = g.GId AND i.Receipt = r.RNumber AND 
	c.CId = r.Customer AND YEAR(SaleDate) = 2007 AND 
	MONTHNAME(SaleDate)="October"
GROUP BY FirstName, LastName, c.CId
) 
SELECT t1.FirstName, t1.LastName, ROUND(MoneySpent,2)
FROM cust_tot_oct_2007 t1
WHERE t1.MoneySpent >= ALL (
    SELECT t2.MoneySpent
    FROM cust_tot_oct_2007 t2
    WHERE t1.CId <> t2.CId
);


USE `BAKERY`;
-- BAKERY-3
-- Find all customers who never purchased a twist ('Twist') during October 2007. Report first and last name in alphabetical order by last name.

SELECT FirstName, LastName
FROM customers co 
WHERE NOT EXISTS(
	-- all the customers who purchased a twist in oct 2007
	SELECT *
	FROM goods g, items i, receipts r
	WHERE g.GId=i.Item AND  i.receipt=r.RNumber AND 
	    MONTHNAME(SaleDate) = "October" AND g.Food="Twist" AND
		YEAR(SaleDate) = 2007 AND r.Customer=co.CId
)
ORDER BY LastName;


USE `BAKERY`;
-- BAKERY-4
-- Find the type of baked good (food type & flavor) responsible for the most total revenue.
WITH ffp AS(
    -- flavor food price
    SELECT Food, Flavor, SUM(PRICE) as tot_price, GId
    FROM goods g, items i
    WHERE g.GId = i.Item
    GROUP BY Food, Flavor, GId
)
SELECT Flavor, Food 
FROM ffp fo
WHERE fo.tot_price > ALL(
		SELECT tot_price
		FROM ffp fi
		WHERE  fi.GId <> fo.GId
);


USE `BAKERY`;
-- BAKERY-5
-- Find the most popular item, based on number of pastries sold. Report the item (food, flavor) and total quantity sold.
WITH item_count AS
(	-- gets the item, count of how many bought
	SELECT Item, COUNT(*) as totalQty
	FROM items i
	GROUP BY Item
)
SELECT Flavor, Food, totalQty
FROM item_count co, goods g
WHERE co.totalQty > ALL(
	SELECT totalQty FROM item_count ci
	WHERE ci.Item <> co.Item
) AND g.GId = co.Item;


USE `BAKERY`;
-- BAKERY-6
-- Find the date of highest revenue during the month of October, 2007.
WITH saledate_price AS
(SELECT DISTINCT SaleDate, 
	SUM(g.PRICE) OVER (PARTITION BY SaleDate) as day_revenue
FROM receipts r, items i, goods g
WHERE r.RNumber = i.Receipt AND g.GId=i.Item)
SELECT SaleDate
FROM saledate_price p
WHERE p.day_revenue > ALL (
	SELECT pi.day_revenue
	FROM saledate_price pi
	WHERE pi.SaleDate <> p.SaleDate
) AND MONTHNAME(p.SaleDate)="October" AND YEAR(p.SaleDate);


USE `BAKERY`;
-- BAKERY-7
-- Find the best-selling item (by number of purchases) on the day of the highest revenue in October of 2007.
WITH day_rev AS(
    SELECT Flavor, Food, COUNT(i.Receipt) AS Qty
    FROM goods g, items i, receipts r
    WHERE g.GId=i.Item AND i.Receipt=r.RNumber
    AND r.SaleDate =
    (
        WITH saledate_price AS
        (SELECT DISTINCT SaleDate, 
	        SUM(g.PRICE) OVER (PARTITION BY SaleDate) as day_revenue
        FROM receipts r, items i, goods g
        WHERE r.RNumber = i.Receipt AND g.GId=i.Item)
        SELECT SaleDate
        FROM saledate_price p
        WHERE p.day_revenue > ALL (
	        SELECT pi.day_revenue
	        FROM saledate_price pi
	        WHERE pi.SaleDate <> p.SaleDate
        ) AND MONTHNAME(p.SaleDate)="October" AND YEAR(p.SaleDate)

    )   
    GROUP BY Flavor, Food
)
SELECT Flavor, Food, Qty
FROM day_rev ro
WHERE ro.Qty > ALL (
	SELECT ri.Qty
	FROM day_rev ri
	WHERE ri.Flavor <> ro.Flavor AND 
		ri.Food <> ro.Food
);


USE `BAKERY`;
-- BAKERY-8
-- For every type of Cake report the customer(s) who purchased it the largest number of times during the month of October 2007. Report the name of the pastry (flavor, food type), the name of the customer (first, last), and the number of purchases made. Sort output in descending order on the number of purchases, then in alphabetical order by last name of the customer, then by flavor.
WITH cake_qty_cust AS(
    SELECT Flavor, Food, Customer, COUNT(*) as Qty
    FROM goods g, items i, receipts r
    WHERE g.GId=i.Item AND r.RNumber=i.Receipt AND
	    g.Food="Cake" AND MONTHNAME(r.SaleDate)="October"
	    AND YEAR(r.SaleDate)=2007
    GROUP BY Customer, Flavor, Food
)
SELECT Flavor, Food, FirstName, LastName, Qty
FROM customers c, cake_qty_cust cqc
WHERE c.CId=cqc.Customer AND Qty >= ALL (
	SELECT Qty
	FROM cake_qty_cust ci
	WHERE ci.Flavor=cqc.Flavor AND cqc.Customer<>ci.Customer
)
ORDER BY Qty DESC, LastName, Flavor;


USE `BAKERY`;
-- BAKERY-9
-- Output the names of all customers who made multiple purchases (more than one receipt) on the latest day in October on which they made a purchase. Report names (first, last) of the customers and the earliest day in October on which they made a purchase, sorted in chronological order, then by last name.

WITH mult_on_last_day AS(
	-- returns customers that have mult purchases on last day
	SELECT c.Customer
	FROM (SELECT r.Customer, MAX(r.SaleDate) AS max_date FROM receipts r GROUP BY Customer) AS c
	WHERE EXISTS(
		SELECT 1
		FROM receipts r	
		WHERE r.SaleDate=c.max_date AND c.Customer = r.Customer
		GROUP BY r.Customer, SaleDate
		HAVING COUNT(DISTINCT r.RNumber) > 1
	)
)
SELECT LastName, FirstName, MIN(SaleDate)
FROM mult_on_last_day m, customers c, receipts r
WHERE m.Customer=c.CId AND m.Customer=r.Customer
GROUP BY LastName, FirstName
ORDER BY MIN(SaleDate), LastName;


USE `BAKERY`;
-- BAKERY-10
-- Find out if sales (in terms of revenue) of Chocolate-flavored items or sales of Croissants (of all flavors) were higher in October of 2007. Output the word 'Chocolate' if sales of Chocolate-flavored items had higher revenue, or the word 'Croissant' if sales of Croissants brought in more revenue.

WITH choc_cross_rev AS
(SELECT Flavor, SUM(rev) as rev
FROM
	(SELECT Flavor, COUNT(*) *g.PRICE as rev
	FROM goods g, items i
	WHERE g.GId=i.Item AND g.Flavor="Chocolate"
	GROUP BY Flavor, g.PRICE
	UNION ALL
	SELECT Food, COUNT(*) *g.PRICE as rev
	FROM goods g, items i
	WHERE g.GId=i.Item AND g.Food="Croissant"
	GROUP BY Flavor, g.PRICE) t0
GROUP BY Flavor)
SELECT flavor
FROM choc_cross_rev o
WHERE o.rev > ALL (
	SELECT rev
	FROM choc_cross_rev i
	WHERE i.Flavor<>o.Flavor
);


USE `INN`;
-- INN-1
-- Find the most popular room (based on the number of reservations) in the hotel  (Note: if there is a tie for the most popular room status, report all such rooms). Report the full name of the room, the room code and the number of reservations.

WITH room_qty AS(
    SELECT RoomName, RoomCode, COUNT(*) as NumberRes
    FROM reservations rs, rooms rm
    WHERE rm.RoomCode=rs.Room
    GROUP BY RoomName, RoomCode
)

SELECT rqo.*
FROM room_qty rqo
WHERE rqo.NumberRes >= ALL (
	SELECT NumberRes
	FROM room_qty rqi
	WHERE rqi.RoomCode <> rqo.RoomCode
);


USE `INN`;
-- INN-2
-- Find the room(s) that have been occupied the largest number of days based on all reservations in the database. Report the room name(s), room code(s) and the number of days occupied. Sort by room name.
WITH rm_occ AS(
    SELECT RoomName, RoomCode, 
    SUM(DATEDIFF(CheckOut, CheckIn)) AS Occ
    FROM reservations rs, rooms rm
    WHERE rs.Room=rm.RoomCode
    GROUP BY RoomName, RoomCode
)
SELECT ro.*
FROM rm_occ ro
WHERE ro.Occ >= ALL (
	SELECT Occ
	FROM rm_occ ri
	WHERE ro.RoomCode <> ri.RoomCode
);


USE `INN`;
-- INN-3
-- For each room, report the most expensive reservation. Report the full room name, dates of stay, last name of the person who made the reservation, daily rate and the total amount paid. Sort the output in descending order by total amount paid.
-- for each room, report={most expensive reservation}
WITH res_total AS(
    SELECT DISTINCT Room,
    MAX(DATEDIFF(CheckOut,CheckIn) * rs.rate) as max
    FROM reservations rs
    GROUP BY Room
)
SELECT RoomName, CheckIn, CheckOut, LastName, rate, rto.max
FROM res_total rto, reservations rs, rooms rm
WHERE rto.Room = rm.RoomCode AND rm.RoomCode=rs.Room AND
	DATEDIFF(rs.CheckOut, rs.CheckIn)*rs.rate = rto.max AND
	rs.Room = rto.Room
ORDER BY max DESC;


USE `INN`;
-- INN-4
-- For each room, report whether it is occupied or unoccupied on July 4, 2010. Report the full name of the room, the room code, and either 'Occupied' or 'Empty' depending on whether the room is occupied on that day. (the room is occupied if there is someone staying the night of July 4, 2010. It is NOT occupied if there is a checkout on this day, but no checkin). Output in alphabetical order by room code. 
-- for each room report if its occupied or unoccupied on july 4, 2010
SELECT RoomName, u.RoomCode, status
FROM 
((WITH OCC AS(
    WITH July4thRes AS(
	    SELECT *
	    FROM reservations rs
    	WHERE rs.CheckIn <= "2010-07-04" AND rs.CheckOut >= "2010-07-04" 
    )
    SELECT DISTINCT jo.Room, "Occupied" as status
    FROM July4thRes jo
    WHERE jo.CheckOut > "2010-07-04" OR EXISTS(
	SELECT *
	FROM July4thRes ji
	WHERE jo.Room=ji.room AND jo.CODE<>ji.CODE AND
		ji.CheckOut=jo.CheckIn
    )
)
SELECT RoomCode, "Empty" as status
FROM rooms rm
WHERE NOT EXISTS(
	SELECT *
	FROM OCC o
	WHERE rm.RoomCode = o.Room
)) 
UNION ALL
    (   WITH July4thRes AS(
	    SELECT *
	    FROM reservations rs
    	WHERE rs.CheckIn <= "2010-07-04" AND rs.CheckOut >= "2010-07-04" 
    )
    SELECT DISTINCT jo.Room, "Occupied" as status
    FROM July4thRes jo
    WHERE jo.CheckOut > "2010-07-04" OR EXISTS(
	SELECT *
	FROM July4thRes ji
	WHERE jo.Room=ji.room AND jo.CODE<>ji.CODE AND
		ji.CheckOut=jo.CheckIn
    )
)) AS u, rooms r
WHERE r.RoomCode=u.RoomCode
ORDER BY RoomCode;


USE `INN`;
-- INN-5
-- Find the highest-grossing month (or months, in case of a tie). Report the month, the total number of reservations and the revenue. For the purposes of the query, count the entire revenue of a stay that commenced in one month and ended in another towards the earlier month. (e.g., a September 29 - October 3 stay is counted as September stay for the purpose of revenue computation). In case of a tie, months should be sorted in chronological order.
WITH month_rev AS(
    SELECT MONTHNAME(CheckIn) as _month, 
    COUNT(*) N_revs,
	SUM(rate*DATEDIFF(CheckOut,CheckIn)) as month_rev
    FROM reservations r
    GROUP BY MONTHNAME(CheckIn)
)
SELECT *
FROM month_rev r0
WHERE r0.month_rev >= ALL (
	SELECT month_rev
	FROM month_rev r1
	WHERE r0._month <> r1._month
)
ORDER BY N_revs;


USE `STUDENTS`;
-- STUDENTS-1
-- Find the teacher(s) who teach(es) the largest number of students. Report the name of the teacher(s) (first and last) and the number of students in their class.

WITH teacher_num_std AS (
	SELECT t.Last, t.First, COUNT(*) AS NStudents
	FROM teachers t, list l
	WHERE t.classroom = l.classroom
	GROUP BY t.Last,t.First
)
SELECT Last, First, NStudents
FROM teacher_num_std t0
WHERE t0.NStudents > ALL (
	SELECT t1.NStudents
	FROM teacher_num_std t1
	WHERE t1.Last <> t0.Last AND t1.First <> t0.First
);


USE `STUDENTS`;
-- STUDENTS-2
-- Find the grade(s) with the largest number of students whose last names start with letters 'A', 'B' or 'C' Report the grade and the number of students
WITH abc_count AS(
	SELECT Grade, COUNT(*) AS abcCount
	FROM list l
	WHERE l.LastName REGEXP "^(A|B|C)" 
	GROUP BY Grade
)
SELECT *
FROM abc_count a0
WHERE a0.abcCount > ALL (
	SELECT a1.abcCount
	FROM abc_count a1
	WHERE a1.Grade <> a0.Grade
);


USE `STUDENTS`;
-- STUDENTS-3
-- Find all classrooms which have fewer students in them than the average number of students in a classroom in the school. Report the classroom numbers in ascending order. Report the number of student in each classroom.
With num_stds AS(
	SELECT
	COUNT(*) as N_stds
	FROM list
),
num_rooms AS(
	SELECT	
	COUNT(DISTINCT classroom) as N_rooms
	FROM teachers
),
avg_class_size AS(
	SELECT N_stds/N_rooms as avg
	FROM num_rooms, num_stds
)
SELECT classroom, COUNT(*) as ns
FROM list l
GROUP BY classroom
HAVING COUNT(*) < (
	SELECT *
	FROM avg_class_size
)
ORDER BY classroom;


USE `STUDENTS`;
-- STUDENTS-4
--  Find all pairs of classrooms with the same number of students in them. Report each pair only once. Report both classrooms and the number of students. Sort output in ascending order by the number of students in the classroom.
WITH std_count AS(
	SELECT classroom, COUNT(*) as ns
	FROM list l
	GROUP BY classroom
)
SELECT l1.classroom, l2.classroom, l1.ns
FROM std_count l1, std_count l2
WHERE l1.ns = l2.ns AND l1.classroom <> l2.classroom 
	AND l1.classroom < l2.classroom
ORDER BY l1.ns;


USE `STUDENTS`;
-- STUDENTS-5
-- For each grade with more than one classroom, report the last name of the teacher who teachers the classroom with the largest number of students in the grade. Output results in ascending order by grade.
WITH grade_with_mul_classrooms AS(
	SELECT grade
	FROM list l
	GROUP BY grade
	HAVING COUNT(DISTINCT classroom) > 1
),
teachers_most_stds AS(
	SELECT l.grade, Last, First, l.classroom, COUNT(*) as ns
	FROM list l , teachers t, grade_with_mul_classrooms gmc
	WHERE t.classroom=l.classroom AND gmc.grade=l.grade
	GROUP BY l.grade, Last, First, l.classroom
)
SELECT grade, Last
FROM teachers_most_stds  t0
WHERE t0.ns > ALL (
	-- in the specific grade
	SELECT ns
	FROM teachers_most_stds t1
	WHERE t0.grade=t1.grade AND t0.Last <> t1.Last
		AND t0.First <> t1.First
	
)
ORDER BY grade;


USE `CSU`;
-- CSU-1
-- Find the campus with the largest enrollment in 2000. Output the name of the campus and the enrollment.

WITH campus_2000 AS (
	SELECT Campus, Enrolled
	FROM enrollments em, campuses c
	WHERE c.Id=em.CampusId AND em.Year=2000
)
SELECT Campus, Enrolled
FROM campus_2000 c0
WHERE c0.Enrolled > ALL (
	SELECT c1.Enrolled
	FROM campus_2000 c1
	WHERE c0.Campus <> c1.Campus	
);


USE `CSU`;
-- CSU-2
-- Find the university that granted the highest average number of degrees per year over its entire recorded history. Report the name of the university.

WITH campus_avg_e AS(
	SELECT DISTINCT
		d.CampusId,
		AVG(d.degrees) OVER (PARTITION BY d.CampusId) as avg_e
	FROM degrees d
)
SELECT Campus
FROM campuses c, campus_avg_e e0
WHERE c.Id=e0.CampusId AND e0.avg_e > ALL(
	SELECT avg_e
	FROM campus_avg_e e1
	WHERE e1.CampusId <> e0.CampusId	
);


USE `CSU`;
-- CSU-3
-- Find the university with the lowest student-to-faculty ratio in 2003. Report the name of the campus and the student-to-faculty ratio, rounded to one decimal place. Use FTE numbers for enrollment.
WITH 2003_std_enrollments AS(
	SELECT CampusId, FTE
	FROM enrollments e
	WHERE e.Year=2003
),
2003_fac_enrollments AS(
	SELECT CampusId, FTE
	FROM faculty f
	WHERE f.Year = 2003
),
2003_ratio_std_to_fac AS(
	SELECT f.CampusId, ROUND(s.FTE/f.FTE,1) AS ratio
	FROM 2003_fac_enrollments f, 2003_std_enrollments s
	WHERE f.CampusId=s.CampusId
	GROUP BY CampusId
)
SELECT Campus, ratio
FROM 2003_ratio_std_to_fac r0, campuses c
WHERE c.Id=r0.CampusId AND
r0.ratio < ALL (
	SELECT ratio
	FROM 2003_ratio_std_to_fac r1
	WHERE r1.CampusId <> r0.CampusId
);


USE `CSU`;
-- CSU-4
-- Find the university with the largest percentage of the undergraduate student body in the discipline 'Computer and Info. Sciences' in 2004. Output the name of the campus and the percent of these undergraduate students on campus.
WITH campus_n_comp_2004 AS(
	SELECT Name, c.Campus, dE.CampusId, 100*ROUND(dE.Ug/e.Enrolled,3) AS percent
	FROM campuses c, discEnr dE, disciplines dp, enrollments e
	WHERE c.Id=dE.CampusId AND Name="Computer and Info. Sciences" AND
		dE.Year=2004 AND e.Year=dE.Year AND e.CampusId=c.Id AND dE.Discipline=dp.Id
)
SELECT Campus, percent
FROM campus_n_comp_2004 c0
WHERE
c0.percent > ALL(
	SELECT percent
	FROM campus_n_comp_2004 c1
	WHERE c1.CampusId<>c0.CampusId
);


USE `CSU`;
-- CSU-5
-- For each year between 1997 and 2003 (inclusive) find the university with the highest ratio of total degrees granted to total enrollment (use enrollment numbers). Report the years, the names of the campuses and the ratios. List in chronological order.
WITH ratio_1997_to_2003 AS(
	SELECT e.Year,c.Campus, e.CampusId, d.degrees, e.Enrolled
	FROM enrollments e, degrees d, campuses c
	WHERE e.CampusId=d.CampusId AND e.Year = d.Year 
		AND c.Id=e.CampusId AND e.Year BETWEEN 1997 AND 2003
)
SELECT r0.Year, Campus, (r0.degrees/r0.Enrolled) as DPE 
FROM ratio_1997_to_2003 r0
WHERE (r0.degrees/r0.Enrolled) > ALL (
	SELECT (r1.degrees/r1.Enrolled) as ratio
	FROM ratio_1997_to_2003 r1
	WHERE r0.Year=r1.Year AND r0.CampusId <> r1.CampusId
)
ORDER BY Year;


USE `CSU`;
-- CSU-6
-- For each campus report the year of the best student-to-faculty ratio, together with the ratio itself. Sort output in alphabetical order by campus name. Use FTE numbers to compute ratios.
WITH campuses_ratio AS(
	SELECT e.CampusId, e.Year, ROUND(e.FTE/f.FTE, 2) as ratio
	FROM enrollments e, faculty f
	WHERE e.CampusId=f.CampusId AND e.Year=f.Year
)
SELECT Campus, c0.Year, ratio
FROM campuses_ratio c0, campuses c
WHERE c.Id=c0.CampusId AND c0.ratio > ALL(
	SELECT ratio
	FROM campuses_ratio c1
	WHERE c0.CampusId = c1.CampusId AND c0.Year <> c1.Year
)
ORDER BY Campus;


USE `CSU`;
-- CSU-7
-- For each year (for which the data is available) report the total number of campuses in which student-to-faculty ratio became worse (i.e. more students per faculty) as compared to the previous year. Report in chronological order.

-- step 1 - return a table of student faculty ratio for each year
WITH std_to_fac AS ( 
    SELECT e.CampusId, e.Year, ROUND(e.FTE/f.FTE,4) as ratio,
	SUM(ROUND(e.FTE/f.FTE,4)) OVER 
	(PARTITION BY CampusId ORDER BY Year
	ROWS BETWEEN 1 PRECEDING AND CURRENT ROW) as indicator
	
    FROM enrollments e, faculty f
    WHERE f.Year=e.Year AND e.CampusId = f.CampusId
    ORDER BY e.CampusId, e.Year
)
SELECT Year, COUNT(*) as schools
FROM std_to_fac sf
WHERE 2*sf.ratio > sf.indicator AND ratio <> sf.indicator
GROUP BY Year
ORDER BY Year;


USE `MARATHON`;
-- MARATHON-1
-- Find the state(s) with the largest number of participants. List state code(s) sorted alphabetically.

WITH states AS(
	SELECT State, COUNT(*) as cnt
	FROM marathon m
	GROUP BY State
)
SELECT State
FROM states s0
WHERE s0.cnt >= ALL(
    SELECT cnt
    FROM states s1
    WHERE s1.State <> s0.State
)
ORDER BY State;


USE `MARATHON`;
-- MARATHON-2
--  Find all towns in Rhode Island (RI) which fielded more female runners than male runners for the race. Report the names of towns, sorted alphabetically.

WITH RI_towns AS(
    SELECT DISTINCT Sex, Town,
	COUNT(*) OVER (PARTITION BY Sex, Town) as scnt
    FROM marathon m
    WHERE m.State="RI"
    ORDER BY Town
)
SELECT Town
FROM RI_towns rt0
WHERE rt0.sex = "F" AND EXISTS (
	SELECT 1
	FROM RI_towns rt1
	WHERE 
		rt0.scnt > rt1.scnt
		AND rt1.Town = rt0.Town 
		AND rt0.sex<>rt1.sex	
)
ORDER BY Town;


USE `MARATHON`;
-- MARATHON-3
-- For each state, report the gender-age group with the largest number of participants. Output state, age group, gender, and the number of runners in the group. Report only information for the states where the largest number of participants in a gender-age group is greater than one. Sort in ascending order by state code, age group, then gender.
-- step 1 - get a table with runners count
WITH runner_count AS(
	SELECT state, agegroup, Sex, COUNT(*) as rcnt
	FROM marathon m
	GROUP BY state, agegroup, Sex
	ORDER BY state
)
SELECT state, agegroup, sex, rcnt
FROM runner_count r0
WHERE r0.rcnt > 1 AND r0.rcnt >= ALL (
	SELECT r1.rcnt
	FROM runner_count r1
	WHERE 
		r1.state = r0.state AND
		CONCAT(r1.agegroup, r1.sex) <> CONCAT(r0.agegroup, r0.sex)
)
ORDER BY state, agegroup, sex;


USE `MARATHON`;
-- MARATHON-4
-- Find the 30th fastest female runner. Report her overall place in the race, first name, and last name. This must be done using a single SQL query (which may be nested) that DOES NOT use the LIMIT clause. Think carefully about what it means for a row to represent the 30th fastest (female) runner.
WITH ranker AS(
    SELECT 
	    *,
	    RANK() OVER (PARTITION BY Sex ORDER BY Place) as sex_place
	    
    FROM
	    marathon m
)	
SELECT Place, firstname, lastname
FROM ranker
WHERE sex_place=30 AND sex="F";


USE `MARATHON`;
-- MARATHON-5
-- For each town in Connecticut report the total number of male and the total number of female runners. Both numbers shall be reported on the same line. If no runners of a given gender from the town participated in the marathon, report 0. Sort by number of total runners from each town (in descending order) then by town.

SELECT DISTINCT m.Town, 
	(SELECT COUNT(*) FROM marathon WHERE state="CT" AND Sex="M" AND town=m.town) AS men,
	(SELECT COUNT(*) FROM marathon WHERE state="CT" AND Sex="F" AND town=m.town) AS women
FROM 
	marathon m
WHERE
	m.state="CT"
ORDER BY men+women DESC ,m.Town;


USE `KATZENJAMMER`;
-- KATZENJAMMER-1
-- Report the first name of the performer who never played accordion.

SELECT FirstName
FROM Band b
WHERE NOT EXISTS(
	-- table where muscians play accordion
	SELECT 1
	FROM Instruments i
	WHERE i.Bandmate = b.Id AND i.Instrument="accordion"
);


USE `KATZENJAMMER`;
-- KATZENJAMMER-2
-- Report, in alphabetical order, the titles of all instrumental compositions performed by Katzenjammer ("instrumental composition" means no vocals).

-- the titles of all instruments compositions performed by katzenjammer
SELECT Title
FROM Songs s
WHERE NOT EXISTS(
	-- all songs performed by vocals
	SELECT 1
	FROM Vocals v
	WHERE v.Song = s.SongId 
);


USE `KATZENJAMMER`;
-- KATZENJAMMER-3
-- Report the title(s) of the song(s) that involved the largest number of different instruments played (if multiple songs, report the titles in alphabetical order).
WITH instr_per_song AS(
	SELECT Song, COUNT(*) as num_ins
	FROM Instruments i
	GROUP BY Song
)
SELECT Title
FROM Songs s, instr_per_song ips0
WHERE s.SongId=ips0.Song AND ips0.num_ins >= ALL(
	SELECT num_ins
	FROM instr_per_song ips1
	WHERE ips0.Song <> ips1.Song 
)
ORDER BY Title;


USE `KATZENJAMMER`;
-- KATZENJAMMER-4
-- Find the favorite instrument of each performer. Report the first name of the performer, the name of the instrument and the number of songs the performer played the instrument on. Sort in alphabetical order by the first name, then instrument.

WITH inst_cnt AS(
    SELECT DISTINCT Bandmate, Instrument,
	    COUNT(*) OVER (PARTITION BY i.Instrument, i.Bandmate) as cnt
    FROM Instruments i
)
SELECT b.FirstName,ic0.Instrument, ic0.cnt
FROM Band b, inst_cnt ic0
WHERE b.Id=ic0.Bandmate AND
ic0.cnt >= ALL(
	SELECT cnt
	FROM inst_cnt ic1
	WHERE ic1.Bandmate = ic0.Bandmate AND ic1.Instrument <> ic0.Instrument
)
ORDER BY FirstName;


USE `KATZENJAMMER`;
-- KATZENJAMMER-5
-- Find all instruments played ONLY by Anne-Marit. Report instruments in alphabetical order.
WITH all_x_anne_instrs AS(
    -- table is contains every instrument used by everyone excluding Anne-Marit
    SELECT DISTINCT i.Instrument
    FROM Instruments i, Band b
    WHERE i.Bandmate=b.Id AND b.FirstName<>"Anne-Marit" 
),
all_anne_instrs AS(
    -- table is contains every instrument used by Anne-Marit
    SELECT DISTINCT i.Instrument
    FROM Instruments i, Band b
    WHERE i.Bandmate=b.Id AND b.FirstName="Anne-Marit" 
)
SELECT a0.Instrument
FROM all_anne_instrs a0
WHERE NOT EXISTS(
	SELECT 1
	FROM all_x_anne_instrs a1
	WHERE a0.Instrument = a1.Instrument
)
ORDER BY Instrument;


USE `KATZENJAMMER`;
-- KATZENJAMMER-6
-- Report the first name of the performer who played the largest number of different instruments. Sort in ascending order.

WITH bandmate_num_inst AS(
	SELECT i.Bandmate, COUNT(DISTINCT i.Instrument) AS n_inst
	FROM Instruments i
	GROUP BY i.Bandmate
)
SELECT b.FirstName
FROM bandmate_num_inst b0, Band b
WHERE b0.BandMate = b.Id AND
	b0.n_inst >= ALL (
		SELECT n_inst
		FROM bandmate_num_inst b1
		WHERE b1.Bandmate<>b0.Bandmate
	)
ORDER BY FirstName;


USE `KATZENJAMMER`;
-- KATZENJAMMER-7
-- Which instrument(s) was/were played on the largest number of songs? Report just the names of the instruments (note, you are counting number of songs on which an instrument was played, make sure to not count two different performers playing same instrument on the same song twice).
WITH inst_per_song AS(
	SELECT s.Instrument, COUNT(*) as Num_songs
	FROM (SELECT DISTINCT Song, Instrument FROM Instruments) AS s
	GROUP BY s.Instrument	
)
SELECT i0.Instrument
FROM inst_per_song i0
WHERE i0.Num_songs >= ALL (
	SELECT Num_songs
	FROM inst_per_song i1
	WHERE i0.Instrument <> i1.Instrument
);


USE `KATZENJAMMER`;
-- KATZENJAMMER-8
-- Who spent the most time performing in the center of the stage (in terms of number of songs on which she was positioned there)? Return just the first name of the performer(s). Sort in ascending order.

WITH mate_center_songs AS(
	SELECT p.Bandmate, COUNT(DISTINCT p.Song) as n_songs
	FROM Performance p
	WHERE p.StagePosition = "center"
	GROUP BY p.Bandmate
)
SELECT FirstName
FROM mate_center_songs m0, Band b
WHERE b.Id=m0.Bandmate AND m0.n_songs >= ALL(
	SELECT n_songs
	FROM mate_center_songs m1
	WHERE m0.Bandmate <> m1.Bandmate 
)
ORDER BY FirstName;


