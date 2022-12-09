INSERT INTO public.users (id, login, password)
VALUES (1, 'test', '$2a$10$8owJMVTTglp4cbEKvxFq7.qQMl0xdcFiC/IUMBiINsOXgrwAuBg5K');

INSERT INTO public.roles (id, title)
VALUES (1, 'user');
INSERT INTO public.roles (id, title)
VALUES (2, 'admin');

INSERT INTO public.user_roles (id, role_id, user_id)
VALUES (1, 1, 1);
