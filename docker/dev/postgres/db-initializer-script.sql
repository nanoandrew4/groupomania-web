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