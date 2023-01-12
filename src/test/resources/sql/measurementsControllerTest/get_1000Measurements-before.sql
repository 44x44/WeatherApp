delete from sensor;
delete from measurement;

alter sequence sensor_id_seq restart with 1;
alter sequence measurement_id_seq restart with 1;

insert into sensor select * from sensor_sample;
insert into measurement select * from measurement_sample;