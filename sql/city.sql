/* Province表建表语句.*/
create table Province (
	id integer primary key autoincrement,
	province_name text, 
	province_code text
)

/* City表建表语句.*/
create table City (
	id integer primary key autoincrement,
	city_name text,
	city_code text,
	province_id integer
)

/* County表建表语句.*/
create table County (
	id integer primary key autoincrement, 
	county_name text,
	county_code text,
	city_id integer
)