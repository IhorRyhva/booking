ALTER TABLE hotel DROP CONSTRAINT hotel_star_check;
ALTER TABLE room DROP CONSTRAINT room_category_check;

ALTER TABLE hotel
    ALTER COLUMN star TYPE varchar(10) USING CASE star
        WHEN 0 THEN 'ONE'
        WHEN 1 THEN 'TWO'
        WHEN 2 THEN 'THREE'
        WHEN 3 THEN 'FOUR'
        WHEN 4 THEN 'FIVE'
        ELSE 'ERROR'
END;
ALTER TABLE room ALTER COLUMN category TYPE varchar(10) USING CASE category
    WHEN 0 THEN 'LUX'
    WHEN 1 THEN 'FAMILY'
    WHEN 2 THEN 'ECONOMY'
    WHEN 3 THEN 'BASIC'
    WHEN 4 THEN 'DELUXE'
    ELSE 'ERROR'
END;
ALTER TABLE hotel ALTER COLUMN star SET NOT NULL;
ALTER TABLE room ALTER COLUMN category SET NOT NULL;
ALTER TABLE hotel ADD CONSTRAINT hotel_star_check check (star IN ('ONE',  'TWO',  'THREE',  'FOUR',  'FIVE'));
ALTER TABLE room ADD CONSTRAINT room_category_check check (category IN ('LUX', 'FAMILY', 'ECONOMY', 'BASIC', 'DELUXE'));
