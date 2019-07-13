insert into groupomania_schema.campaign_manager (username, password, password_change_required) values
('admin', '$e0808$ziN95PXDH3QmOV+kymcEWBtv4r25q8cUuW1WFi2y3LFoYy44Ic5IFVCvpBL/nmRUhn5LECQqwmAD0P0mMFC8zQ==$f/CqvRzECwDJmryDehkY8r1MlL7N+Gx+FslgeLcn6DGm77AhgZ2iYppMWe9axp9cJEor8UlLhZYa76zjAiAKRg==', 'true'),
('david', '$e0808$ziN95PXDH3QmOV+kymcEWBtv4r25q8cUuW1WFi2y3LFoYy44Ic5IFVCvpBL/nmRUhn5LECQqwmAD0P0mMFC8zQ==$f/CqvRzECwDJmryDehkY8r1MlL7N+Gx+FslgeLcn6DGm77AhgZ2iYppMWe9axp9cJEor8UlLhZYa76zjAiAKRg==', 'true'),
('andres', '$e0808$ziN95PXDH3QmOV+kymcEWBtv4r25q8cUuW1WFi2y3LFoYy44Ic5IFVCvpBL/nmRUhn5LECQqwmAD0P0mMFC8zQ==$f/CqvRzECwDJmryDehkY8r1MlL7N+Gx+FslgeLcn6DGm77AhgZ2iYppMWe9axp9cJEor8UlLhZYa76zjAiAKRg==', 'true');

insert into groupomania_schema.campaign (title, description, type, state, start_date, end_date, show_after_expiration, quantity, original_price, percent_discount, owner_id) values
('Offer campaign', 'Test offer campaign', 1, 0, '2030-07-02', '2030-07-04', 'true', 'Infinity', 20, 33, 3),
('Coupon campaign', 'Sample description', 0, 0, '2030-07-01', '2030-07-05', 'true', 100, 10, 33, 3),
('Expired campaign', 'Expired and visible campaign', 1, 0, '2019-06-25', '2019-06-28', 'true', 'Infinity', 20, 33, 3);

insert into groupomania_schema.campaign_manager_profile(campaign_manager_id, name, email, address) values
(1, 'admin profile', 'admin@test.com', 'admin address'),
(2, 'davids profile', 'david@test.com', 'davids address'),
(3, 'andres profile', 'andres@test.com', 'andres address');

insert into groupomania_schema.offer_Campaign (id) values
(1),
(3);

insert into groupomania_schema.coupon_Campaign (id, coupon_start_date, coupon_end_date, campaign_manager_name, campaign_manager_email) values
(2, '2030-07-01', '2030-07-04', 'SampleManagerName', 'samplemanager@email.com');

insert into groupomania_schema.authority(authority) values
('ROLE_CAMPAIGN_MANAGER');

insert into groupomania_schema.users_authorities (user_id, authority_id) values
(1, 1),
(2, 1),
(3, 1);