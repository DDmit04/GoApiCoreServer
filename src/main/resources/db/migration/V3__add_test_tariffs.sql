insert into tariff (id, cost_per_day, tariff_name)
values (1, 100, 'small api'),
       (2, 200, 'med api'),
       (3, 300, 'large api'),
       (4, 100, 'small database'),
       (5, 200, 'med database'),
       (6, 300, 'large database');

insert into user_api_tariff (id, requests_count)
values (1, 10),
       (2, 100),
       (3, 1000);

insert into database_tariff (id, max_size_bytes)
values (4, 1024 * 10),
       (5, 1024 * 100),
       (6, 1024 * 1000);