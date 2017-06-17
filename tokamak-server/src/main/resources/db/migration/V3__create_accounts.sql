
INSERT INTO `Accounts` (`id`,`created`,`updated`,`username`,`password`,`locked`) VALUES ('acc_451e5ooylPyHXs2VX7lG',NOW(),NOW(),'user','$2a$10$u3we0orWoM41z1v8gA3xd.yPpZOWiXY.anUvSa8mXYFTY8nYu7sti',0);
INSERT INTO AccountRoles (account_id, role_id) VALUES('acc_451e5ooylPyHXs2VX7lG', 'rol_edgda5k47bjsbtcyh45x');

INSERT INTO `Accounts` (`id`,`created`,`updated`,`username`,`password`,`locked`) VALUES ('acc_iYptwy15G2gUtVkoEFjg',NOW(),NOW(),'admin','$2a$10$VX1W9s1G1QEuF6sqPWN8ieSm21BaDb3Ga9TMXvMuiO.EWLY0XRh2S',0);
INSERT INTO AccountRoles (account_id, role_id) VALUES('acc_iYptwy15G2gUtVkoEFjg', 'rol_ibg9lhwzjwiot4n9xsrh');
