spring.jpa.properties.hibernate.default_batch_fetch_size: 300


:::


SELECT lnequest0_.id               AS id1_0_,
       :::
FROM   korbit.lne_quests lnequest0_
WHERE  lnequest0_.type = 'quiz'
       AND '2019-03-01T00:00:00.000+0000' < lnequest0_.created_at
       AND lnequest0_.created_at < '2020-07-01T00:00:00.000+0000'
LIMIT  3, 3;


SELECT quizlist0_.quest_id       AS quest_i10_1_0_,
       :::
FROM   korbit.lne_quizzes quizlist0_
WHERE  quizlist0_.quest_id in (4,5,6)

















CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_hash` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `encrypted_password` varchar(64) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `encrypted_password_set_at` datetime DEFAULT NULL,
  `reset_password_token` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
  `reset_password_sent_at` datetime DEFAULT NULL,
  `remember_created_at` datetime DEFAULT NULL,
  `sign_in_count` int(11) DEFAULT '0',
  `current_sign_in_at` datetime DEFAULT NULL,
  `last_sign_in_at` datetime DEFAULT NULL,
  `current_sign_in_ip` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `last_sign_in_ip` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `confirmation_token` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `confirmed_at` datetime DEFAULT NULL,
  `confirmation_sent_at` datetime DEFAULT NULL,
  `unconfirmed_email` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `failed_attempts` int(11) NOT NULL DEFAULT 0,
  `unlock_token` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
  `unlock_sent_at` datetime DEFAULT NULL,
  `locked_at` datetime DEFAULT NULL,
  `authentication_token` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `nick` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `phone` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
  `permit` int(11) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `nationality` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country_code` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `namechecked_at` datetime DEFAULT NULL,
  `terms` tinyint(1) DEFAULT '0',
  `entity` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `registration_number` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `change_confirmation_token` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
  `change_confirmed_at` datetime DEFAULT NULL,
  `change_confirmation_sent_at` datetime DEFAULT NULL,
  `namecheck_status` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `namecheck_status_code` varchar(40) CHARACTER SET utf8mb4 DEFAULT NULL,
  `namechecked_by` bigint(20) DEFAULT NULL,
  `captcha_at` datetime DEFAULT NULL,
  `no_captcha_until` datetime DEFAULT NULL,
  `join_reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sign_up_platform_id` bigint(20) DEFAULT NULL,
  `is_identified_for_coins` tinyint(1) NOT NULL DEFAULT 0,
  `is_identified_for_fiats` tinyint(1) NOT NULL DEFAULT 0,
  `is_corporation` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_users_on_email` (`email`),
  UNIQUE KEY `index_users_on_reset_password_token` (`reset_password_token`),
  UNIQUE KEY `index_users_on_confirmation_token` (`confirmation_token`),
  UNIQUE KEY `index_users_on_unlock_token` (`unlock_token`),
  UNIQUE KEY `index_users_on_authentication_token` (`authentication_token`),
  UNIQUE KEY `index_users_on_user_hash` (`user_hash`),
  UNIQUE KEY `index_users_on_uuid` (`uuid`),
  KEY `index_users_on_permit` (`permit`),
  KEY `index_users_on_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci


select
  uuid,
  email,
  user_hash,
  encrypted_password,
  encrypted_password_set_at,
  reset_password_token,
  reset_password_sent_at,
  remember_created_at,
  sign_in_count,
  current_sign_in_at,
  last_sign_in_at,
  current_sign_in_ip,
  last_sign_in_ip,
  confirmation_token,
  confirmed_at,
  confirmation_sent_at,
  unconfirmed_email,
  failed_attempts,
  unlock_token,
  unlock_sent_at,
  locked_at,
  authentication_token,
  created_at,
  updated_at,
  name,
  nick,
  phone,
  permit,
  deleted_at,
  dob,
  gender,
  nationality,
  country_code,
  namechecked_at,
  terms,
  entity,
  registration_number,
  change_confirmation_token,
  change_confirmed_at,
  change_confirmation_sent_at,
  namecheck_status,
  namecheck_status_code,
  namechecked_by,
  captcha_at,
  no_captcha_until,
  join_reason,
  status,
  sign_up_platform_id,
  is_identified_for_coins,
  is_identified_for_fiats,
  is_corporation
from users ;