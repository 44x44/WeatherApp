insert into sensor_sample select * from sensor;
insert into measurement_sample select * from measurement;

delete from sensor;
delete from measurement;