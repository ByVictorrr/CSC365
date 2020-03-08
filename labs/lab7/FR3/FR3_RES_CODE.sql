WITH interval_table AS(
SELECT *,
        (SELECT MIN(DATEDIFF(CheckOut, curr.CheckIn)) FROM lab7_reservations
		WHERE curr.Room=Room AND curr.CheckIn<= CheckOut AND curr.CODE <> CODE
		) AS diff
    FROM lab7_reservations curr
)
SELECT diff, CheckOut, Adults, Kids
FROM interval_table t, lab7_rooms rm
WHERE rm.RoomCode=t.Room AND CODE = ?
