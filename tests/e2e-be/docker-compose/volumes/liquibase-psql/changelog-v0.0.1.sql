CREATE TABLE recipes (
   "id" text primary key constraint recipes_id_length_ctr check (length("id") < 64),
   "title" text constraint recipes_title_length_ctr check (length(title) < 128),
   "description" text constraint recipes_description_length_ctr check (length(title) < 4096),
   "owner_id" text not null constraint recipes_owner_id_length_ctr check (length(id) < 64),
   "lock" text not null constraint recipes_lock_length_ctr check (length(id) < 64),
   "ingredients" text
);

CREATE INDEX recipes_owner_id_idx on "recipes" using hash ("owner_id");
