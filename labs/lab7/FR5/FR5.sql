SELECT rm.*, res.*
FROM lab7_reservations res, lab7_rooms rm
WHERE res.FirstName LIKE ? AND res.LastName LIKE ? AND res.CheckIn >= ?  AND res.CheckOut <= ? AND res.Room LIKE ? AND res.CODE LIKE ?