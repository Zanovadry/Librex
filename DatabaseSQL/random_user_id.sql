UPDATE public.review
SET user_id = floor(random() * (36 - 7 + 1) + 7)::int;
