SELECT DISTINCT rm.RoomName,
    ROUND((COUNT(*) OVER (PARTITION BY rs.ROOM))*100/180,2) rm_pop_percent,
    mr.most_recent_day as next_avail_chkin,
    DATEDIFF(most_recent_day, most_recent_ckin) as recent_duration
FROM lab7_reservations rs, lab7_rooms rm,
    -- returns the Room, most_recent day
	(SELECT ROOM, MAX(CheckOut) as most_recent_day, MAX(CheckIn) as most_recent_ckin
	FROM lab7_reservations rs GROUP BY ROOM) AS mr
WHERE rs.ROOM=mr.ROOM AND rm.RoomCode=rs.ROOM AND DATEDIFF(mr.most_recent_day, rs.CheckIn) <= 180
ORDER BY rm_pop_percent DESC;