DROP TABLE IF EXISTS Accounts;

CREATE TABLE Accounts (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime NOT NULL,
  updated datetime NOT NULL,
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
  created datetime NOT NULL,
  updated datetime NOT NULL,
  username varchar(128) NOT NULL,
  password varchar(255) NOT NULL,
  access_token_validity_seconds int(11) unsigned,
  refresh_token_validity_seconds int(11) unsigned,
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_CLIENT_USERNAME (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime NOT NULL,
  updated datetime NOT NULL,
  name varchar(128) NOT NULL,
  description varchar(255), 
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_ROLE_NAME (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS Authorities;

CREATE TABLE Authorities (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime NOT NULL,
  updated datetime NOT NULL,
  name varchar(128) NOT NULL,
  description varchar(255),
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_AUTHORITY_NAME (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS Scopes;

CREATE TABLE Scopes (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime NOT NULL,
  updated datetime NOT NULL,
  name varchar(128) NOT NULL,
  description varchar(255),
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_SCOPE_NAME (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS GrantTypes;

CREATE TABLE GrantTypes (
  id varchar(64) NOT NULL,
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  created datetime NOT NULL,
  updated datetime NOT NULL,
  name varchar(128) NOT NULL,
  description varchar(255),
  KEY(_id),
  PRIMARY KEY(id),
  UNIQUE KEY UK_GRANT_TYPE_NAME (name)
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
  UNIQUE KEY UK_CA_CLIENT_AUTHORITY (client_id,authority_id),
  CONSTRAINT FK_CA_CLIENT FOREIGN KEY (client_id) REFERENCES Clients (id),
  CONSTRAINT FK_CA_AUTHORTIY FOREIGN KEY (authority_id) REFERENCES Authorities (id)
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



DROP TABLE IF EXISTS HeartBeat;

CREATE TABLE HeartBeat (
  _id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO GrantTypes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','gnt_x8ir9yj0gdb8839pkyuk4z9b4eeb3i','password');
INSERT INTO GrantTypes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','gnt_qlb2cfhvcwo2hqxadretxbfcgl30vj','client_credentials');
INSERT INTO GrantTypes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','gnt_dzp0fousdl0um2ppa2mpfvyhtww7ld','refresh_token');
INSERT INTO GrantTypes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','gnt_vrjms7yh0kv0o5mb37xlabq69gaxz1','authorization_code');

INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_gws0jo8mqqhzdewbhqvyfyig9vtmri','accounts:create');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_kzor8xpzoxex8iy9v8ukup4x4tsh1d','accounts:update');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_tjs5q1gmu4z9zhpy08cnoosmxaamr5','accounts:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_hkgtskyizkixzzimnew9w4o1gwxmqb','accounts:read');

INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_v7t5ngcnwibl1ykba53vlyqpgepvag','clients:create');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_nlbl8q2gsfundb6bwrhy88a7zacbi0','clients:update');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_ibg9lhwzjwioe4n9xsrhmbwvczs4hw','clients:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_ltb5zev462vkl6wezayzgq7zpzccy9','clients:read');

INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_fvjtuvymnai9yxmp2kdoh4smf3og3i','roles:create');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_pavligj10fmejikdxqvm6c3jbra2tr','roles:update');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_ga8mlm7l4mfg9pf2ib3xwxfzkkllbn','roles:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_s7bhhhut0qkgip9jqwnjhmtvdng1ei','roles:read');

INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_hkianhtqnek96w7draklsofnfewbmh','authorities:create');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_kig121ofnoj0nbabv2jqyirlunb67m','authorities:update');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_azz7sndajdloudjkyfzwic3izka3kw','authorities:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_zuofgp0oocouglenmqniobejsyc7m0','authorities:read');

INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_qwayiuoajqxqwfcypdyj8hmhcvzyic','scopes:create');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_zbueqkzvvp9zuoq37g3trrvwl5auhh','scopes:update');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_ty8l6srhwlp9si5mpmd9y4bqbeuvle','scopes:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_cd28ieewxlb3gtde3tzafzkf5iciny','scopes:read');

INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_c5ltnjhqocyjnismd5valaehizkcax','grant_types:create');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_jrwvab24bgglcyiadup4tgm92uc6t5','grant_types:update');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_vkssmillu617wo4exldvlwpd8p5ins','grant_types:delete');
INSERT INTO Scopes (created, updated, id, name) VALUES ('2017-01-01 12:00:00','2017-01-01 12:00:00','scp_edgdg5kb7bjsb9cyh55xvexty0j0zb','grant_types:read');
