insert into tariff (id, cost_per_day, tariff_name)
values (1, 100, 'small'),
       (2, 200, 'med'),
       (3, 300, 'large');

insert into database_tariff (id, max_size_bytes)
values (1, 1024 * 10),
       (2, 1024 * 100),
       (3, 1024 * 1000);

insert into user_api_tariff (id, requests_count)
values (1, 10),
       (2, 100),
       (3, 1000);