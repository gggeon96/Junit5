
INSERT INTO `users` ( `id`,`email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
VALUES (1,'geon1120@gmail.com','geonhyeon','seoul','abcdefgh-abcd-abcd-abcd-abcdefghijklm','ACTIVE',0);
insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values (1, 'helloworld', 1678530673958, 0, 1);