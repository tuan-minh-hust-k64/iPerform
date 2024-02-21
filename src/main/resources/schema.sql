DROP SCHEMA IF EXISTS "iperform" CASCADE;

CREATE SCHEMA "iperform";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DROP TYPE IF EXISTS expectation_type;
DROP TYPE IF EXISTS eks_status;
DROP TYPE IF EXISTS comment_status;
DROP TYPE IF EXISTS comment_type;
DROP TYPE IF EXISTS check_in_status;
DROP TYPE IF EXISTS question_status;
DROP TYPE IF EXISTS check_point_status;
DROP TYPE IF EXISTS collaboration_feedback_status;
DROP TYPE IF EXISTS ranking_type;
drop type if exists category_eks;
drop type if exists category_checkpoint;


CREATE TYPE expectation_type as ENUM ('GROW_YOURSELF', 'GROW_YOUR_TEAM', 'GROW_YOUR_COMPANY');
CREATE TYPE eks_status as ENUM ('ARCHIVED', 'COMPLETED', 'ACTIVE', 'INACTIVE', 'DRAFT');
CREATE TYPE comment_status as ENUM ('DELETED', 'INIT');
CREATE TYPE comment_type as ENUM ('COMMENT', 'FEEDBACK');
CREATE TYPE check_in_progress as ENUM ('PENDING', 'COMPLETED', 'ATTACK', 'RISK', 'ON_TRACK', 'OFF_TRACK', 'ACHIEVED', 'PARTIAL', 'MISSED', 'DROPPED', 'CHALLENGING');
CREATE TYPE check_in_status as ENUM ('INIT', 'PENDING', 'COMPLETED');
CREATE TYPE question_status as ENUM ('DISABLE', 'ENABLE');
CREATE TYPE check_point_status as ENUM ('INIT', 'COMPLETED', 'PENDING', 'FINISHED');
CREATE TYPE collaboration_feedback_status as ENUM ('INIT', 'COMPLETED', 'DELETED');
CREATE TYPE ranking_type as ENUM ('BELOW_EXPECTATIONS', 'NEAR_EXPECTATIONS_NEEDS_IMPROVEMENT', 'MEETS_EXPECTATIONS', 'EXCEEDS_EXPECTATIONS', 'OUTSTANDING');
create type category_eks as enum('NORMAL', 'ONBOARDING');
create type category_checkpoint as enum('NORMAL', 'ONBOARDING');

DROP TABLE IF EXISTS "iperform".key_step CASCADE;
DROP TABLE IF EXISTS "iperform".expectation CASCADE;
DROP TABLE IF EXISTS "iperform".check_point CASCADE;
DROP TABLE IF EXISTS "iperform".check_point_item CASCADE;
DROP TABLE IF EXISTS "iperform".check_in CASCADE;
DROP TABLE IF EXISTS "iperform".comment CASCADE;
DROP TABLE IF EXISTS "iperform".collaboration_feedback CASCADE;
DROP TABLE IF EXISTS "iperform".question CASCADE;
DROP TABLE IF EXISTS "iperform".config CASCADE;





CREATE TABLE "iperform".expectation
(
    id uuid NOT NULL,
    type expectation_type NOT NULL,
    content character varying COLLATE pg_catalog."default" NOT NULL,
    process numeric(10, 2),
    description character varying COLLATE pg_catalog."default",
    status eks_status NOT NULL,
    user_id uuid NOT NULL,
    time_period character varying COLLATE pg_catalog."default",
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ordinal_number numeric(10, 2),
    CONSTRAINT expectation_key PRIMARY KEY (id)
);
alter table "iperform".expectation add column
    category category_eks not null default 'NORMAL';


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
    ranking ranking_type,
    CONSTRAINT check_point_key PRIMARY KEY (id)
);
alter table "iperform".check_point add column
    category category_checkpoint not null default 'NORMAL';


CREATE TABLE "iperform".check_point_item
(
    id uuid NOT NULL,
    title character varying COLLATE pg_catalog."default" NOT NULL,
    subtitle character varying COLLATE pg_catalog."default" NOT NULL,
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
    progress check_in_progress NOT NULL,
    type character varying COLLATE pg_catalog."default",
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT check_in_key PRIMARY KEY (id)
);


CREATE TABLE "iperform".comment
(
    id uuid NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
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
CREATE TABLE "iperform".collaboration_feedback
(
    id uuid NOT NULL,
    target_id uuid NOT NULL,
    reviewer_id uuid NOT NULL,
    strengths character varying COLLATE pg_catalog."default" NOT NULL,
    weaknesses character varying COLLATE pg_catalog."default" NOT NULL,
    status collaboration_feedback_status NOT NULL,
    time_period character varying COLLATE pg_catalog."default" NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT collaboration_feedback_key PRIMARY KEY (id)
);

CREATE TABLE "iperform".config
(
    id uuid NOT NULL,
    check_point boolean,
    check_in boolean,
    guid_check_in character varying COLLATE pg_catalog."default",
    guid_check_point character varying COLLATE pg_catalog."default",
    guid_eks character varying COLLATE pg_catalog."default",
    due_date_check_point TIMESTAMP WITH TIME ZONE
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
