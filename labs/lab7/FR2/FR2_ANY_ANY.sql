WITH interval_table AS(
SELECT *,
        (SELECT MIN(DATEDIFF(CheckIn, curr.CheckOut)) FROM lab7_reservations
		WHERE curr.Room=Room AND curr.CheckOut<= Checkin AND curr.CODE <> CODE
		) AS diff
    FROM lab7_reservations curr
),
input_table AS(
	SELECT CAST( ? AS DATETIME) AS `in`,
	CAST( ? AS DATETIME) AS `out`,
    ? AS `adults`,
	? AS `kids`
)
SELECT rm.RoomName, RoomCode, t.Checkout,diff, basePrice, Adults, decor, bedType, basePrice
FROM interval_table t, lab7_rooms rm
WHERE rm.RoomCode=t.Room
AND
EXISTS(
	SELECT 1
	FROM input_table i
	WHERE DATEDIFF(i.out, i.in) <= t.diff
		-- future or close date
		AND i.in <= t.CheckOut
		-- need options
		AND rm.maxOcc >= i.adults+ i.kids
)
ORDER BY t.CheckIn
LIMIT 5