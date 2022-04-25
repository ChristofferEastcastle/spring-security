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
       (default, 'regular_user',
        '$2a$12$9/BylAxHWgpLymIECTZQreEIwKktf2/.f39e3pP6Rwcw2ICOUjXRq', 'true'),
       (default, 'useless_trainee',
        '$2a$12$6tfi0tvxpigIoOJuibWbzOoo0DgtjDt9gfF9oMZjdKwKUcx.vNttu', 'true');

insert into users_authorities
values ((select id from users where username = 'administrator'),
        (select id from localdevdb.public.authorities where name = 'ADMIN')),
       ((select id from users where username = 'regular_user'),
        (select id from localdevdb.public.authorities where name = 'USER')),
       ((select id from users where username = 'useless_trainee'),
        (select id from localdevdb.public.authorities where name = 'TRAINEE'))

