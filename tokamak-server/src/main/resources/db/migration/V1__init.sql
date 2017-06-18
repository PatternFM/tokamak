DROP TABLE IF EXISTS Accounts;

CREATE TABLE Accounts (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime(3) NOT NULL,
  updated datetime(3) NOT NULL,
  username varchar(128) NOT NULL,
  password varchar(255) NOT NULL,
  locked tinyint(1) NOT NULL DEFAULT 0,
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_ACCOUNT_USERNAME (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS Clients;

CREATE TABLE Clients (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime(3) NOT NULL,
  updated datetime(3) NOT NULL,
  client_id varchar(128) NOT NULL,
  client_secret varchar(255) NOT NULL,
  name varchar(50),
  access_token_validity_seconds int(11) unsigned,
  refresh_token_validity_seconds int(11) unsigned,
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_CLIENT_ID (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS SerializedClients;

CREATE TABLE SerializedClients (
  id varchar(64) NOT NULL,
  client_id varchar(128) NOT NULL,
  created datetime(3) NOT NULL,
  payload TEXT NOT NULL,
  PRIMARY KEY(id),
  UNIQUE KEY UK_SERIALIZED_CLIENT_ID (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime(3) NOT NULL,
  updated datetime(3) NOT NULL,
  name varchar(128) NOT NULL,
  description varchar(255), 
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_ROLE_NAME (name),
  INDEX roles_created_idx (created)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS Audiences;

CREATE TABLE Audiences (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime(3) NOT NULL,
  updated datetime(3) NOT NULL,
  name varchar(128) NOT NULL,
  description varchar(255), 
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_AUDIENCE_NAME (name),
  INDEX audiences_created_idx (created)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS Authorities;

CREATE TABLE Authorities (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime(3) NOT NULL,
  updated datetime(3) NOT NULL,
  name varchar(128) NOT NULL,
  description varchar(255),
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_AUTHORITY_NAME (name),
  INDEX authorities_created_idx (created)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS Scopes;

CREATE TABLE Scopes (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime(3) NOT NULL,
  updated datetime(3) NOT NULL,
  name varchar(128) NOT NULL,
  description varchar(255),
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_SCOPE_NAME (name),
  INDEX scopes_created_idx (created)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS GrantTypes;

CREATE TABLE GrantTypes (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime(3) NOT NULL,
  updated datetime(3) NOT NULL,
  name varchar(128) NOT NULL,
  description varchar(255),
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_GRANT_TYPE_NAME (name),
  INDEX granttypes_created_idx (created)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS AccountRoles;

CREATE TABLE AccountRoles (
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  account_id varchar(64) NOT NULL,
  role_id varchar(64) NOT NULL,
  PRIMARY KEY (_id),
  UNIQUE KEY UK_AR_ACCOUNT_ROLE (account_id,role_id),
  CONSTRAINT FK_AR_ACCOUNT FOREIGN KEY (account_id) REFERENCES Accounts (id),
  CONSTRAINT FK_AR_ROLE FOREIGN KEY (role_id) REFERENCES Roles (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS ClientAuthorities;

CREATE TABLE ClientAuthorities (
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  client_id varchar(64) NOT NULL,
  authority_id varchar(64) NOT NULL,
  PRIMARY KEY (_id),
  UNIQUE KEY UK_CLIAUT_CLIENT_AUTHORITY (client_id,authority_id),
  CONSTRAINT FK_CLIAUT_CLIENT FOREIGN KEY (client_id) REFERENCES Clients (id),
  CONSTRAINT FK_CLIAUT_AUTHORTIY FOREIGN KEY (authority_id) REFERENCES Authorities (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS ClientAudiences;

CREATE TABLE ClientAudiences (
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  client_id varchar(64) NOT NULL,
  audience_id varchar(64) NOT NULL,
  PRIMARY KEY (_id),
  UNIQUE KEY UK_CLIAUD_CLIENT_AUDIENCE (client_id,audience_id),
  CONSTRAINT FK_CLIAUD_CLIENT FOREIGN KEY (client_id) REFERENCES Clients (id),
  CONSTRAINT FK_CLIAUD_AUDIENCE FOREIGN KEY (audience_id) REFERENCES Audiences (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS ClientGrantTypes;

CREATE TABLE ClientGrantTypes (
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  client_id varchar(64) NOT NULL,
  grant_type_id varchar(64) NOT NULL,
  PRIMARY KEY (_id),
  UNIQUE KEY UK_GT_CLIENT_GRANT_TYPE (client_id,grant_type_id),
  CONSTRAINT FK_GT_CLIENT FOREIGN KEY (client_id) REFERENCES Clients (id),
  CONSTRAINT FK_GT_GRANT_TYPE FOREIGN KEY (grant_type_id) REFERENCES GrantTypes (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS ClientScopes;

CREATE TABLE ClientScopes (
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  client_id varchar(64) NOT NULL,
  scope_id varchar(64) DEFAULT NULL,
  PRIMARY KEY (_id),
  UNIQUE KEY UK_CS_CLIENT_SCOPE (client_id,scope_id),
  CONSTRAINT FK_CS_CLIENT FOREIGN KEY (client_id) REFERENCES Clients (id),
  CONSTRAINT FK_CS_SCOPE FOREIGN KEY (scope_id) REFERENCES Scopes (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS oauth_code;

CREATE TABLE oauth_code (
  code varchar(256) DEFAULT NULL,
  authentication blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS HeartBeat;

CREATE TABLE HeartBeat (
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO GrantTypes (created, updated, id, name) VALUES (NOW(),NOW(),'gnt_x8ir9yj0gdb8839pkyuk','password');
INSERT INTO GrantTypes (created, updated, id, name) VALUES (NOW(),NOW(),'gnt_qlb2cfhvcwo2hqxadret','client_credentials');
INSERT INTO GrantTypes (created, updated, id, name) VALUES (NOW(),NOW(),'gnt_dzp0fousdl0um2ppa2mp','refresh_token');
INSERT INTO GrantTypes (created, updated, id, name) VALUES (NOW(),NOW(),'gnt_vrjms7yh0kv0o5mb37xl','authorization_code');
INSERT INTO GrantTypes (created, updated, id, name) VALUES (NOW(),NOW(),'gnt_stjzs6y708vt75mx17xl','implicit');


INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_gws0jo8mqqhzdewbhqvy','accounts:create');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_kzor8xpzoxex8iy9v8uk','accounts:update');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_tjs5q1gmu4z9zhpy08cn','accounts:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_hkgtskyizkixzzimnew9','accounts:read');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_v7t5ngcnwibl1ykba53v','clients:create');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_nlbl8q2gsfundb6bwrhy','clients:update');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_ibg9lhwzjwioe4n9xsrh','clients:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_ltb5zev462vkl6wezayz','clients:read');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_fvjtuvymnai9yxmp2kdo','roles:create');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_pavligj10fmejikdxqvm','roles:update');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_ga8mlm7l4mfg9pf2ib3x','roles:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_s7bhhhut0qkgip9jqwnj','roles:read');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_hkianhtqnek96w7drakl','authorities:create');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_kig121ofnoj0nbabv2jq','authorities:update');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_azz7sndajdloudjkyfzw','authorities:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_zuofgp0oocouglenmqni','authorities:read');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_hzi6nhtqnek4667drakg','audiences:create');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_krg821ofnojenb7bv2jd','audiences:update');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_arz7sndajdldudj6yfzg','audiences:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_z4ofgp0oocoggle5mqnh','audiences:read');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_qwayiuoajqxqwfcypdyj','scopes:create');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_zbueqkzvvp9zuoq37g3t','scopes:update');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_ty8l6srhwlp9si5mpmd9','scopes:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_cd28ieewxlb3gtde3tza','scopes:read');
INSERT INTO Scopes (created, updated, id, name) VALUES (NOW(),NOW(),'scp_edgdg5kb7bjsb9cyh55x','grant_types:read');


INSERT INTO Roles (created, updated, id, name) VALUES (NOW(),NOW(),'rol_edgda5k47bjsbtcyh45x','role:user');
INSERT INTO Roles (created, updated, id, name) VALUES (NOW(),NOW(),'rol_ibg9lhwzjwiot4n9xsrh','role:admin');


INSERT INTO Authorities (created, updated, id, name) VALUES (NOW(),NOW(),'ath_wb0LcDMl5oDGAfQE8Kfa','authority:admin');
