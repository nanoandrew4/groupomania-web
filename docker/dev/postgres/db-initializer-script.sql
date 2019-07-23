create schema groupomania_schema;

create table if not exists groupomania_schema.oauth_client_details (
      client_id varchar(256) primary key,
      resource_ids varchar(256),
      client_secret varchar(256),
      scope varchar(256),
      authorized_grant_types varchar(256),
      web_server_redirect_uri varchar(256),
      authorities varchar(256),
      access_token_validity integer,
      refresh_token_validity integer,
      additional_information varchar(4096),
      autoapprove varchar(256)
);

create table if not exists groupomania_schema.oauth_client_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table if not exists groupomania_schema.oauth_access_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication bytea,
  refresh_token VARCHAR(256)
);

create table if not exists groupomania_schema.oauth_refresh_token (
  token_id VARCHAR(256),
  token bytea,
  authentication bytea
);

create table if not exists groupomania_schema.oauth_code (
  code VARCHAR(256), authentication bytea
);

create table if not exists groupomania_schema.oauth_approvals (
	userId VARCHAR(256),
	clientId VARCHAR(256),
	scope VARCHAR(256),
	status VARCHAR(10),
	expiresAt TIMESTAMP,
	lastModifiedAt TIMESTAMP
);

create table groupomania_schema.authority (id  bigserial not null, authority varchar(255), primary key (id))
create table groupomania_schema.campaign (id  bigserial not null, campaign_image_file_path varchar(255), description varchar(255), discounted_price float8, end_date date, original_price float8 not null, percent_discount float8, quantity float8 not null check (quantity>=1), show_after_expiration boolean not null, start_date date, state int4 not null, title varchar(255), type int4 not null, owner_id int8, primary key (id))
create table groupomania_schema.campaign_manager (id  bigserial not null, password varchar(255), password_change_required boolean not null, username varchar(255), primary key (id))
create table groupomania_schema.campaign_manager_profile (address varchar(255), email varchar(255), name varchar(255), profile_image_file_path varchar(255), campaign_manager_id int8 not null, primary key (campaign_manager_id))
create table groupomania_schema.coupon_campaign (campaign_manager_address varchar(255), campaign_manager_email varchar(255), campaign_manager_name varchar(255), coupon_description varchar(255), coupon_end_date date, coupon_start_date date, id int8 not null, primary key (id))
create table groupomania_schema.offer_campaign (id int8 not null, primary key (id))
create table groupomania_schema.users_authorities (user_id int8 not null, authority_id int8 not null, primary key (user_id, authority_id))
alter table if exists groupomania_schema.campaign add constraint FK592oyyseywn7ttnjom60hfu8p foreign key (owner_id) references groupomania_schema.campaign_manager
alter table if exists groupomania_schema.campaign_manager_profile add constraint FKenqoahv6lf2uw0jn2vojrfbkm foreign key (campaign_manager_id) references groupomania_schema.campaign_manager
alter table if exists groupomania_schema.coupon_campaign add constraint FKtnc395tnc78nspuqt983h876d foreign key (id) references groupomania_schema.campaign
alter table if exists groupomania_schema.offer_campaign add constraint FKlb2n84e45vxt8l95tbhxgsgf6 foreign key (id) references groupomania_schema.campaign
alter table if exists groupomania_schema.users_authorities add constraint FKac1qasdciwqra319h2pa72gh6 foreign key (authority_id) references groupomania_schema.authority
alter table if exists groupomania_schema.users_authorities add constraint FKdhomx34wl25mwoym314pmlag3 foreign key (user_id) references groupomania_schema.campaign_manager

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

insert into groupomania_schema.oauth_client_details (client_id, client_secret, scope, authorized_grant_types,
   authorities, access_token_validity, refresh_token_validity) values
('public_campaign_manager_client', '$c0808$xMZL8woVJtKRPJcXrZHGKiRlNl90nCJgRkZZf0xnqRM=$yJj/lgUQy8MJJxPSB0J5H1fCuyfYISTJ7bkynwPOaAw=', 'read,write', 'password,refresh_token', null, 36000, 2592000);