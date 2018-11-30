/* Province表建表语句.*/
create table Province (
	province_id integer,
	province_name text
)

/* City表建表语句.*/
create table City (
	city_id integer,
	city_name text,
	province_id integer
)

/* County表建表语句.*/
create table County (
	county_id integer, 
	county_name text,
	city_id integer,
	weather_id text
)