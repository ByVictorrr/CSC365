WITH interval_table AS(
SELECT *,
        (SELECT MIN(DATEDIFF(CheckOut, curr.CheckIn)) FROM lab7_reservations
		WHERE curr.Room=Room AND curr.CheckIn<= CheckOut AND curr.CODE <> CODE
		) AS diff
    FROM lab7_reservations curr
),
input_table AS(
	SELECT CAST( ? AS DATETIME) AS `in`,
	CAST( ? AS DATETIME) AS `out`,
    ? AS `adults`,
	? AS `kids`,
	? AS `bed_type`,
	? AS `room_type`
)
SELECT *
FROM interval_table t, lab7_rooms rm
WHERE rm.RoomCode = t.Room
AND
EXISTS(
	SELECT 1
	FROM input_table i
	WHERE i.in NOT BETWEEN t.CheckIn AND t.CheckOut
		AND i.out NOT BETWEEN t.CheckIn AND t.CheckOut
		-- so enough space
		AND DATEDIFF(i.out, i.in) < t.diff
		-- future or close date
		AND i.in <= t.CheckOut
		-- need options
		AND i.adults <= t.adults
		AND i.kids <= t.kids
		-- bed type and room type
		AND i.bed_type = rm.bedType
		AND i.room_type = rm.RoomCode
)
ORDER BY t.CheckOut DESC
LIMIT 5