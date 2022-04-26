insert into animals
values (default, 'Peppa Pig', 'PIG', 29, 67),
       (default, 'Baloo', 'BEAR', 29, 67),
       (default, 'Scar the Hero', 'LION', 999, -9),
       (default, 'Shere Khan', 'TIGER', 86, 60),
       (default, 'Timon', 'MEERKAT', 37, 3);

insert into authorities
values (default, 'ADMIN'),
       (default, 'USER'),
       (default, 'TRAINEE');



insert into users
values (default, 'administrator',
        '$2a$10$mJSDNY7ZmiAaZzbHfgosfOeoK56je/opq34Z1ff7KxHJs/Gtk8mra', 'true'),
       -- password = 'super_secret'
       (default, 'regular_user',
        '$2a$12$xJo98SXGhxI390SiVP4tcePqh1Ga4qJnssjIT9J6UHkSMEuTDL2Yi', 'true'),
       -- password = 'best_password'
       (default, 'useless_trainee',
        '$2a$12$iA055Rpzx9shrJGBp92qYeFe9mlP2XVdB1dlrOXPC9GcNlXp8o1NW', 'true');
       -- password = 'how_to_make_password'

insert into users_authorities
values ((select id from users where username = 'administrator'),
        (select id from authorities where name = 'ADMIN')),
       ((select id from users where username = 'regular_user'),
        (select id from authorities where name = 'USER')),
       ((select id from users where username = 'useless_trainee'),
        (select id from authorities where name = 'TRAINEE'))

