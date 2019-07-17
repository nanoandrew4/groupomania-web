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

-- As generated by Hibernate, copied into this file
-- For why constraints look odd: https://stackoverflow.com/questions/35789480/add-constraint-in-hibernate-tutorial
--create table groupomania_schema.authority (id  bigserial not null, authority varchar(255), primary key (id))
--create table groupomania_schema.campaign (id  bigserial not null, campaign_image_file_path varchar(255), description varchar(255), discounted_price float8, end_date date, original_price float8, percent_discount float8, quantity float8, show_after_expiration boolean not null, start_date date, state int4, title varchar(255), type int4, owner_id int8, primary key (id))
--create table groupomania_schema.campaign_manager (id int8 not null, primary key (id))
--create table groupomania_schema.campaign_manager_profile (address varchar(255), email varchar(255), name varchar(255), profile_image_file_path varchar(255), campaign_manager_id int8 not null, primary key (campaign_manager_id))
--create table groupomania_schema.coupon_campaign (campaign_manager_address varchar(255), campaign_manager_email varchar(255), campaign_manager_name varchar(255), coupon_description varchar(255), coupon_end_date date, coupon_start_date date, id int8 not null, primary key (id))
--create table groupomania_schema.offer_campaign (id int8 not null, primary key (id))
--create table groupomania_schema.users (id  bigserial not null, password varchar(255), password_change_required boolean not null, username varchar(255), primary key (id))
--create table groupomania_schema.users_authorities (user_id int8 not null, authority_id int8 not null, primary key (user_id, authority_id))
--alter table if exists groupomania_schema.campaign add constraint FK592oyyseywn7ttnjom60hfu8p foreign key (owner_id) references groupomania_schema.campaign_manager
--alter table if exists groupomania_schema.campaign_manager add constraint FKhr9xno3yr53dhcwt6ttq44qc foreign key (id) references groupomania_schema.users
--alter table if exists groupomania_schema.campaign_manager_profile add constraint FKduqcne1egr0sjeen2fi57x51i foreign key (campaign_manager_id) references groupomania_schema.users
--alter table if exists groupomania_schema.coupon_campaign add constraint FKtnc395tnc78nspuqt983h876d foreign key (id) references groupomania_schema.campaign
--alter table if exists groupomania_schema.offer_campaign add constraint FKlb2n84e45vxt8l95tbhxgsgf6 foreign key (id) references groupomania_schema.campaign
--alter table if exists groupomania_schema.users_authorities add constraint FKac1qasdciwqra319h2pa72gh6 foreign key (authority_id) references groupomania_schema.authority
--alter table if exists groupomania_schema.users_authorities add constraint FKq3lq694rr66e6kpo2h84ad92q foreign key (user_id) references groupomania_schema.users