ALTER table room ADD COLUMN bed_number integer not null default 1;
ALTER table room ADD COLUMN description varchar(500) not null default 'empty';