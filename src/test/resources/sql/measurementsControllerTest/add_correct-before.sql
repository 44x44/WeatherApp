delete from sensor;
delete from measurement;

alter sequence sensor_id_seq restart with 1;
alter sequence measurement_id_seq restart with 1;

insert into sensor(name) values ('Test sensor1');
insert into sensor(name) values ('Test sensor2');
insert into sensor(name) values ('Test sensor3');