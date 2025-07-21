-- insert profile values


insert into profile(libelle, actif, user_id)
values ('admin_profile', 'true', 1);

-- update user values


update _user
set actif_profile_id = 1
where id = 1;


--insert into profile_module values


insert into profile_module(profile_id, module_id)
values (1,1)