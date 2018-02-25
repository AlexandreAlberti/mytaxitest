/**
 * CREATE Script for init of DB
 */

-- Create 3 OFFLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (1, now(), false, 'OFFLINE',
'driver01pw', 'driver01');

insert into driver (id, date_created, deleted, online_status, password, username) values (2, now(), false, 'OFFLINE',
'driver02pw', 'driver02');

insert into driver (id, date_created, deleted, online_status, password, username) values (3, now(), false, 'OFFLINE',
'driver03pw', 'driver03');


-- Create 3 ONLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (4, now(), false, 'ONLINE',
'driver04pw', 'driver04');

insert into driver (id, date_created, deleted, online_status, password, username) values (5, now(), false, 'ONLINE',
'driver05pw', 'driver05');

insert into driver (id, date_created, deleted, online_status, password, username) values (6, now(), false, 'ONLINE',
'driver06pw', 'driver06');

-- Create 1 OFFLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (7,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'OFFLINE',
'driver07pw', 'driver07');

-- Create 1 ONLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (8,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'ONLINE',
'driver08pw', 'driver08');


-- Manufacturer Examples
insert into manufacturer (id, date_created, name, country) values (1,now(),'BMW','Germany');
insert into manufacturer (id, date_created, name, country) values (2,now(),'OPEL','Germany');
insert into manufacturer (id, date_created, name, country) values (3,now(),'MERCEDES','Germany');
insert into manufacturer (id, date_created, name, country) values (4,now(),'FORD','North America');
insert into manufacturer (id, date_created, name, country) values (5,now(),'PEUGEOT','France');
insert into manufacturer (id, date_created, name, country) values (6,now(),'RENAULT','France');
insert into manufacturer (id, date_created, name, country) values (7,now(),'FERRARI','Italy');

-- Car examples
insert into car (id, date_created, LICENSE_PLATE, SEAT_COUNT, convertible, rating, ENGINE_TYPE, MANUFACTURER_ID) values (1, now(), '0000ABC', 2, 1, 3.5, 'GAS', 3);
insert into car (id, date_created, LICENSE_PLATE, SEAT_COUNT, convertible, rating, ENGINE_TYPE, MANUFACTURER_ID) values (2, now(), '0000ABD', 5, 0, 6.5, 'HYBRID', 2);
insert into car (id, date_created, LICENSE_PLATE, SEAT_COUNT, convertible, rating, ENGINE_TYPE, MANUFACTURER_ID) values (3, now(), '0000ABE', 7, 0, 8.4, 'ELECTRIC', 4);