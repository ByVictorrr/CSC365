WITH same_months AS(
	-- This table is all of the entrys where MONTH(in)==MONTH(out)
	SELECT Room, MONTHNAME(CheckIn) AS monthName, MONTH(CheckIn) AS month, SUM(rate) AS revenue
	FROM lab7_reservations same
	WHERE LAST_DAY(CheckIn) = LAST_DAY(CheckOut)
	GROUP BY Room, MONTHNAME(CheckIn), MONTH(CheckIn)
	ORDER BY Room, MONTHNAME(CheckIn)
),
diff_months AS(
	SELECT * FROM lab7_reservations res WHERE LAST_DAY(CheckIn) <> LAST_DAY(CheckOut)
),
diff_union_months AS(
	(SELECT Room, MONTHNAME(CheckIn) AS monthName, MONTH(CheckIn) AS month
	    , SUM(rate/(DATEDIFF(LAST_DAY(CheckIn), CheckIn)+1)) AS revenue FROM diff_months d
		GROUP BY Room, MONTHNAME(CheckIn), MONTH(CheckIn)
	)
	UNION
	(SELECT Room, MONTHNAME(CheckOut) AS monthName, MONTH(CheckOut) AS month,
	SUM(rate/DATEDIFF(CheckOut, DATE_ADD(CheckOut, INTERVAL - DAY(CheckOut)+1 DAY))) AS revenue
	FROM diff_months
		-- take out checkout on the first day of month
	WHERE DATEDIFF(CheckOut, DATE_ADD(CheckOut, INTERVAL - DAY(CheckOut)+1 DAY)) > 0
	GROUP BY Room, MONTHNAME(CheckOut), MONTH(CheckOut)
	)

),
final_table AS(
    (SELECT * FROM diff_union_months) UNION (SELECT * FROM same_months)
)
SELECT RoomName, Room, month, SUM(revenue) as month_rev
FROM final_table f, lab7_rooms rm
WHERE f.Room=rm.RoomCode
GROUP BY RoomName, Room, month
ORDER BY Room, month;
