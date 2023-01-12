delete from sensor;

alter sequence sensor_id_seq restart with 1;

insert into sensor(name) values ('Test sensor');