CREATE TABLE public.book (
                             end_date date,
                             number integer,
                             price integer,
                             start_date date,
                             id bigint NOT NULL,
                             room_id bigint NOT NULL,
                             user_id bigint NOT NULL,
                             hotel_name character varying(255)
);


--
-- Name: book_seq_name; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.book_seq_name
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: hotel; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hotel (
                              star smallint,
                              id bigint NOT NULL,
                              country character varying(255),
                              name_of_hotel character varying(255),
                              city character varying(255),
                              CONSTRAINT hotel_star_check CHECK (((star >= 0) AND (star <= 5)))
);


--
-- Name: hotel_seq_name; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hotel_seq_name
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: room; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.room (
                             category smallint,
                             number integer NOT NULL,
                             price integer NOT NULL,
                             hotel_id bigint NOT NULL,
                             id bigint NOT NULL,
                             CONSTRAINT room_category_check CHECK (((category >= 0) AND (category <= 5)))
);




--
-- Name: room_seq_name; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.room_seq_name
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_seq_name; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_seq_name
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
                              is_banned boolean NOT NULL,
                              id bigint NOT NULL,
                              email character varying(255),
                              user_name character varying(255)
);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- Name: hotel hotel_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hotel
    ADD CONSTRAINT hotel_pkey PRIMARY KEY (id);


--
-- Name: room room_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: book fk9cv1tt952k857xoia51k1vj12; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT fk9cv1tt952k857xoia51k1vj12 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: room fkdosq3ww4h9m2osim6o0lugng8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT fkdosq3ww4h9m2osim6o0lugng8 FOREIGN KEY (hotel_id) REFERENCES public.hotel(id);


--
-- Name: book fkghd3cf6cmkv33q26vm3rt1v8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT fkghd3cf6cmkv33q26vm3rt1v8 FOREIGN KEY (room_id) REFERENCES public.room(id);