BEGIN;

INSERT INTO player (login, email, password,role,  sign_up_time, last_sign_in_time, rating, photo, enabled)
VALUES ('kirill','k@mail.ru','$2a$10$M3.Ts0gg0Ldft6fB7N1tiusvTs5eR03inetM.uOQBMHux8BBRMSs2',
        'ROLE_USER',TIMESTAMP '2011-05-16 15:36:38',TIMESTAMP '2011-05-16 15:36:38',100,NULL,TRUE);

INSERT INTO player (login, email, password,  role, sign_up_time, last_sign_in_time, rating, photo, enabled)
VALUES ('liza','liza@mail.ru','$2a$10$M3.Ts0gg0Ldft6fB7N1tiusvTs5eR03inetM.uOQBMHux8BBRMSs2',
        'ROLE_ADMIN',TIMESTAMP '2011-05-16 15:36:38',TIMESTAMP '2011-05-16 15:36:38',100,NULL,TRUE);



COMMIT ;