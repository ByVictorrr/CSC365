SELECT rm.*, res.*
FROM lab7_reservations res, lab7_rooms rm
WHERE res.LastName LIKE ? AND res.CheckIn >= ?  AND res.CheckOut <= ? AND res.Room LIKE ? AND res.CODE LIKE ?