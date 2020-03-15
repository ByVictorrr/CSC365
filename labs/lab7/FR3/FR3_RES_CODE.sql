WITH interval_table AS(
SELECT *,
        IFNULL((SELECT MIN(DATEDIFF(curr.CheckIn, CheckOut)) FROM lab7_reservations
		WHERE curr.Room=Room AND curr.CheckIn>= CheckOut AND curr.CODE <> CODE
		),0) AS days_open_prior,
		  IFNULL((SELECT MIN(DATEDIFF(CheckIn, curr.CheckOut)) FROM lab7_reservations
		WHERE curr.Room=Room AND curr.Checkout<= Checkin AND curr.CODE <> CODE
		),0) AS days_open_after
    FROM lab7_reservations curr
    ORDER BY Room, CheckIn
)
SELECT RoomName, CheckIn, CheckOut, Rate, LastName, FirstName, Adults, Kids, days_open_prior, days_open_after, basePrice, maxOcc
FROM interval_table t, lab7_rooms rm
WHERE rm.RoomCode = t.Room AND CODE=?
ORDER BY CheckIn
