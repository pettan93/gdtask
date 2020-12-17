CREATE TABLE public.sentence
(
    id      bigint NOT NULL,
    created timestamp without time zone
);

CREATE TABLE public.sentence_stats
(
    id            bigint NOT NULL,
    display_count bigint,
    sentence_id   bigint
);

CREATE SEQUENCE public.sentence_seq
    START WITH 1
    INCREMENT BY 100000
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.word
(
    id            bigint NOT NULL,
    forbidden     boolean,
    text          character varying(100),
    word_category integer
);


CREATE TABLE public.word_sentence_usage
(
    id          bigint NOT NULL,
    place       integer,
    id_sentence bigint,
    id_word     bigint
);


CREATE SEQUENCE public.sentence_stats_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.word_seq
    START WITH 1
    INCREMENT BY 100000
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.word_usage_seq
    START WITH 1
    INCREMENT BY 100000
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;