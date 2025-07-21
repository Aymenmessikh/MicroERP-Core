-- insert prfoile_authrity value


INSERT INTO profile_authority (granted, profile_id, authority_id)
SELECT true, 1, id
FROM authority;
