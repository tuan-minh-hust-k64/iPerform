DROP SCHEMA IF EXISTS "iperform" CASCADE;

CREATE SCHEMA "iperform";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DROP TYPE IF EXISTS expectation_type;
DROP TYPE IF EXISTS eks_status;
DROP TYPE IF EXISTS comment_status;
DROP TYPE IF EXISTS comment_type;
DROP TYPE IF EXISTS check_in_status;
DROP TYPE IF EXISTS question_status;
DROP TYPE IF EXISTS check_point_status


CREATE TYPE expectation_type as ENUM ('IKAME_WHAT', 'IKAME_HOW', 'IKAME_LEVELUP');
CREATE TYPE eks_status as ENUM ('ACHIEVED', 'COMPLETED', 'ACTIVE');
CREATE TYPE comment_status as ENUM ('DELETED', 'INIT');
CREATE TYPE comment_type as ENUM ('COMMENT', 'FEEDBACK');
CREATE TYPE check_in_status as ENUM ('PENDING', 'COMPLETED');
CREATE TYPE question_status as ENUM ('DISABLE', 'ENABLE');
CREATE TYPE check_point_status as ENUM ('INIT', 'COMPLETED');

DROP TABLE IF EXISTS "iperform".key_step CASCADE;
DROP TABLE IF EXISTS "iperform".expectation CASCADE;
DROP TABLE IF EXISTS "iperform".check_point CASCADE;
DROP TABLE IF EXISTS "iperform".check_point_item CASCADE;
DROP TABLE IF EXISTS "iperform".check_in CASCADE;
DROP TABLE IF EXISTS "iperform".comment CASCADE;
DROP TABLE IF EXISTS "iperform".question CASCADE;
DROP TABLE IF EXISTS "iperform".config CASCADE;




CREATE TABLE "iperform".expectation
(
    id uuid NOT NULL,
    type expectation_type NOT NULL,
    content character varying COLLATE pg_catalog."default" NOT NULL,
    process numeric(10, 2),
    status eks_status,
    user_id uuid NOT NULL,
    time_period character varying COLLATE pg_catalog."default",
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ordinal_number numeric(10, 2),
    CONSTRAINT expectation_key PRIMARY KEY (id)
);
CREATE TABLE "iperform".key_step
(
    id uuid NOT NULL,
    e_id uuid NOT NULL,
    content character varying COLLATE pg_catalog."default" NOT NULL,
    status eks_status,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ordinal_number numeric(10, 2),
    CONSTRAINT key_step_key PRIMARY KEY (id, e_id)
);


CREATE TABLE "iperform".check_point
(
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    title character varying COLLATE pg_catalog."default" NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status check_point_status NOT NULL,
    CONSTRAINT check_point_key PRIMARY KEY (id)
);

CREATE TABLE "iperform".check_point_item
(
    id uuid NOT NULL,
    title character varying COLLATE pg_catalog."default" NOT NULL,
    content character varying COLLATE pg_catalog."default" NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    check_point_id uuid NOT NULL,
    CONSTRAINT check_point_item_key PRIMARY KEY (id)
);


CREATE TABLE "iperform".check_in
(
    id uuid NOT NULL,
    e_id uuid NOT NULL,
    content character varying COLLATE pg_catalog."default" NOT NULL,
    status check_in_status NOT NULL,
    type character varying COLLATE pg_catalog."default",
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT check_in_key PRIMARY KEY (id)
);


CREATE TABLE "iperform".comment
(
    id uuid NOT NULL,
    parent_id uuid NOT NULL,
    user_id uuid NOT NULL,
    type comment_type NOT NULL,
    content character varying COLLATE pg_catalog."default" NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status comment_status NOT NULL,
    question_id uuid,
    CONSTRAINT comment_key PRIMARY KEY (id)
);
CREATE TABLE "iperform".config
(
    check_point boolean NOT NULL,
    check_in boolean NOT NULL,
    guid_check_in character varying COLLATE pg_catalog."default" NOT NULL,
    guid_check_point character varying COLLATE pg_catalog."default" NOT NULL,
    guid_eks character varying COLLATE pg_catalog."default" NOT NULL
);
CREATE TABLE "iperform".question
(
    id uuid NOT NULL,
    content character varying COLLATE pg_catalog."default" NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status question_status NOT NULL,
    CONSTRAINT feedback_key PRIMARY KEY (id)
);
ALTER TABLE "iperform".comment
    ADD CONSTRAINT "comment_fk_e" FOREIGN KEY (parent_id)
    REFERENCES "iperform".expectation (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;
ALTER TABLE "iperform".comment
    ADD CONSTRAINT "comment_fk_check_point" FOREIGN KEY (parent_id)
    REFERENCES "iperform".check_point (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;
ALTER TABLE "iperform".comment
    ADD CONSTRAINT "comment_fk_check_point_item" FOREIGN KEY (parent_id)
    REFERENCES "iperform".check_point_item (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;
ALTER TABLE "iperform".comment
    ADD CONSTRAINT "comment_fk_question" FOREIGN KEY (question_id)
    REFERENCES "iperform".question (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;



ALTER TABLE "iperform".key_step
    ADD CONSTRAINT "fk_key_step" FOREIGN KEY (e_id)
    REFERENCES "iperform".expectation (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;
ALTER TABLE "iperform".check_in
    ADD CONSTRAINT "check_in_fk" FOREIGN KEY (e_id)
    REFERENCES "iperform".expectation (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;
ALTER TABLE "iperform".check_point_item
    ADD CONSTRAINT "check_point_item_fk" FOREIGN KEY (check_point_id)
    REFERENCES "iperform".check_point (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;