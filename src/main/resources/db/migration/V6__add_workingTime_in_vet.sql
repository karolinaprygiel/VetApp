alter table vets
    add column working_time interval;
update vets
set working_time = shift_end - vets.shift_start ;
