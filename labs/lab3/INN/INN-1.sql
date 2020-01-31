-- objective: single sql statemnt inc by 15 percent the base price of king-bed rooms wqith a max occupancy of 4,
--              as well as those rooms with a based price of 100 dollars or less

UPDATE Rooms
    SET basePrice = basePrice*.15+basePrice
WHERE (bedType = "King"  AND maxOccupancy = 4)
        OR basePrice <= 100);
        