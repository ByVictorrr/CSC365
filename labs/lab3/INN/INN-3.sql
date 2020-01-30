-- Schedule a purge of "" data from previous 6 months in table of Reservations
DELETE FROM Reservations WHERE 
    LastName = 'ENA' AND firstName = 'KOLB'
    -- gives result in days (182.5days = 6months)
    AND DATEDIFF(CURRENT_DATE(), STR_TO_DATE(CheckOut, '%d-%M-%y')) >= 182.5; 

