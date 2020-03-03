WITH interval_table AS(
SELECT *,
        (SELECT MIN(DATEDIFF(CheckOut, curr.CheckIn)) FROM lab7_reservations
		WHERE curr.Room=Room AND curr.CheckIn<= CheckOut AND curr.CODE <> CODE
		) AS diff
    FROM lab7_reservations curr
),
input_table AS(
	SELECT @in="2020-07-01", @out="2020-07-03",
		@type = "bed type", @adults=1 , @kids=1
)
SELECT *
FROM interval_table t
WHERE EXISTS(
	SELECT 1
	FROM input_table
	WHERE NOT (@in BETWEEN t.CheckIn AND t.CheckOut)
		AND NOT (@out BETWEEN t.CheckIn AND t.CheckOut)
		-- so enough space
		AND DATEDIFF(@out, @in) < DATEDIFF(t.CheckOut, t.CheckIn)
		-- future or close date
		AND @in >= t.CheckOut


)