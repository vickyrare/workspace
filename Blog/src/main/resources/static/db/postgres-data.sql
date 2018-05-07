INSERT INTO role( role_id, role ) VALUES ( 1, 'ADMIN' );
INSERT INTO role( role_id, role ) VALUES ( 2, 'USER' );

INSERT INTO users( user_id, active, creation_date, email, first_name, last_name, password, profile_picture ) VALUES ( '79a5bd3f-1ec2-46cf-94b6-8ac23df3f3c9', TRUE , '2018-05-03 21:52:00.937', 'vickyrare@gmail.com', 'Admin', 'User', '$2a$10$HM9usyc3rr.G.9KoNqyOJeRun6Fw5tZDvFC85edoLa2BZYE8NKiaC', 'avatar.png' );
INSERT INTO users( user_id, active, creation_date, email, first_name, last_name, password, profile_picture ) VALUES ( 'f9d98297-9db9-41a3-86e6-25ab0480fcd8', TRUE, '2018-05-03 21:52:01.24', 'vickyrare@yahoo.com', 'Waqqas', 'Sharif', '$2a$10$4zIuOPVOZlx.h8SzjchyUebMN3FCl/ZKgWkAeHEApiQA960l.1QNm', 'avatar.png' );
INSERT INTO users( user_id, active, creation_date, email, first_name, last_name, password, profile_picture ) VALUES ( 'a9d98297-9db9-41a3-86e6-25ab0480fcd8', FALSE, '2018-05-03 21:52:01.24', 'anaya@yahoo.com', 'Anaya', 'Waqqas', '$2a$10$4zIuOPVOZlx.h8SzjchyUebMN3FCl/ZKgWkAeHEApiQA960l.1QNm', 'avatar.png' );

INSERT INTO post( post_id, creation_date, description, last_modified, title, user_id ) VALUES ( 'd63d1cf0-b70e-43f1-bf4c-5f562d1c5a59', '2018-05-03 21:52:01.408', 'I am wondering whether anyone can help me hack my 3DS. My 3DS is currently running 1.5 firmware version.', '2018-05-03 21:52:01.408', 'How to hack 3DS', '79a5bd3f-1ec2-46cf-94b6-8ac23df3f3c9' );
INSERT INTO post( post_id, creation_date, description, last_modified, title, user_id ) VALUES ( 'f63d1cf0-b70e-43f1-bf4c-5f562d1c5a59', '2018-05-03 21:52:01.408', 'I am wondering whether anyone can help me hack my PS3. My PS3 is currently running 1.5 firmware version.', '2018-05-03 21:52:01.408', 'How to hack PS3', '79a5bd3f-1ec2-46cf-94b6-8ac23df3f3c9' );
INSERT INTO post( post_id, creation_date, description, last_modified, title, user_id ) VALUES ( 'e63d1cf0-b70e-43f1-bf4c-5f562d1c5a59', '2018-05-03 21:52:01.408', 'I am wondering whether anyone can help me hack my Wii U. My Wii U is currently running 1.5 firmware version.', '2018-05-03 21:52:01.408', 'How to hack Wii U', 'f9d98297-9db9-41a3-86e6-25ab0480fcd8' );
INSERT INTO post( post_id, creation_date, description, last_modified, title, user_id ) VALUES ( 'e73d1cf0-b70e-43f1-bf4c-5f562d1c5a59', '2018-05-03 21:52:01.408', 'I am wondering whether anyone can help me hack my PS4. My PS4 is currently running 1.5 firmware version.', '2018-05-03 21:52:01.408', 'How to hack PS4', 'f9d98297-9db9-41a3-86e6-25ab0480fcd8' );

INSERT INTO post_comment( comment_id, content, post_date, post_id, user_id ) VALUES ( 'dfdaf9b9-75f9-4ebe-ab6d-307f315cef65', 'Try IGN', '2018-05-03 21:52:01.412', 'd63d1cf0-b70e-43f1-bf4c-5f562d1c5a59', '79a5bd3f-1ec2-46cf-94b6-8ac23df3f3c9' );
INSERT INTO post_comment( comment_id, content, post_date, post_id, user_id ) VALUES ( 'efdaf9b9-75f9-4ebe-ab6d-307f315cef65', 'Try Gamespot', '2018-05-03 21:52:01.412', 'f63d1cf0-b70e-43f1-bf4c-5f562d1c5a59', '79a5bd3f-1ec2-46cf-94b6-8ac23df3f3c9' );
INSERT INTO post_comment( comment_id, content, post_date, post_id, user_id ) VALUES ( 'cfdaf9b9-75f9-4ebe-ab6d-307f315cef65', 'Visit http://www.wiiu.guide.org', '2018-05-03 21:52:01.412', 'e63d1cf0-b70e-43f1-bf4c-5f562d1c5a59', 'f9d98297-9db9-41a3-86e6-25ab0480fcd8' );
INSERT INTO post_comment( comment_id, content, post_date, post_id, user_id ) VALUES ( 'ffdaf9b9-75f9-4ebe-ab6d-307f315cef65', 'Try Gamespot', '2018-05-03 21:52:01.412', 'e73d1cf0-b70e-43f1-bf4c-5f562d1c5a59', 'f9d98297-9db9-41a3-86e6-25ab0480fcd8' );

INSERT INTO user_role( user_id, role_id ) VALUES ( '79a5bd3f-1ec2-46cf-94b6-8ac23df3f3c9', 1 );
INSERT INTO user_role( user_id, role_id ) VALUES ( 'f9d98297-9db9-41a3-86e6-25ab0480fcd8', 2 );
INSERT INTO user_role( user_id, role_id ) VALUES ( 'a9d98297-9db9-41a3-86e6-25ab0480fcd8', 2 );
