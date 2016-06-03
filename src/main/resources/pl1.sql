--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.2
-- Dumped by pg_dump version 9.5.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: genomeslinks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE genomeslinks (
    from_id integer NOT NULL,
    to_id integer NOT NULL,
    genome character varying(20) NOT NULL
);


ALTER TABLE genomeslinks OWNER TO postgres;

--
-- Name: links; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE links (
    from_id integer NOT NULL,
    to_id integer NOT NULL,
    threshold integer NOT NULL,
    id integer NOT NULL
);


ALTER TABLE links OWNER TO postgres;

--
-- Name: links_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE links_id_seq OWNER TO postgres;

--
-- Name: links_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE links_id_seq OWNED BY links.id;


--
-- Name: segments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE segments (
    id integer NOT NULL,
    data text,
    x integer NOT NULL,
    y integer NOT NULL,
    isbubble boolean NOT NULL
);


ALTER TABLE segments OWNER TO postgres;

--
-- Name: specimen; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE specimen (
    specimen_id text NOT NULL,
    age character varying(7) NOT NULL,
    sex character varying(7) NOT NULL,
    hiv_status character varying(8) NOT NULL,
    cohort character varying(6) NOT NULL,
    date_of_collection character varying(10) NOT NULL,
    study_geographic_district character varying(13) NOT NULL,
    specimen_type character varying(13) NOT NULL,
    microscopy_smear_status character varying(8) NOT NULL,
    dna_isolation_single_colony_or_nonsingle_colony character varying(17) NOT NULL,
    phenotypic_dst_pattern character varying(11) NOT NULL,
    capreomycin_10ugml character varying(1) NOT NULL,
    ethambutol_75ugml character varying(1) NOT NULL,
    ethionamide_10ugml character varying(1) NOT NULL,
    isoniazid_02ugml_or_1ugml character varying(1) NOT NULL,
    kanamycin_6ugml character varying(1) NOT NULL,
    pyrazinamide_nicotinamide_5000ugml_or_pzamgit character varying(1) NOT NULL,
    ofloxacin_2ugml character varying(1) NOT NULL,
    rifampin_1ugml character varying(1) NOT NULL,
    streptomycin_2ugml character varying(1) NOT NULL,
    digital_spoligotype character varying(18) NOT NULL,
    lineage character varying(7) NOT NULL,
    genotypic_dst_pattern character varying(22) NOT NULL,
    tugela_ferry_vs_nontugela_ferry_xdr character varying(20) NOT NULL
);


ALTER TABLE specimen OWNER TO postgres;

--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY links ALTER COLUMN id SET DEFAULT nextval('links_id_seq'::regclass);


--
-- Name: genomeslinks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY genomeslinks
    ADD CONSTRAINT genomeslinks_pkey PRIMARY KEY (from_id, to_id, genome);


--
-- Name: links_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY links
    ADD CONSTRAINT links_pkey PRIMARY KEY (id);


--
-- Name: mytable_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY segments
    ADD CONSTRAINT mytable_pkey PRIMARY KEY (id);


--
-- Name: specimen_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY specimen
    ADD CONSTRAINT specimen_pkey PRIMARY KEY (specimen_id);


--
-- Name: unique_link; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY links
    ADD CONSTRAINT unique_link UNIQUE (from_id, to_id, threshold);


--
-- Name: links_from_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY links
    ADD CONSTRAINT links_from_id_fkey FOREIGN KEY (from_id) REFERENCES segments(id);


--
-- Name: links_to_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY links
    ADD CONSTRAINT links_to_id_fkey FOREIGN KEY (to_id) REFERENCES segments(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: genomeslinks; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE genomeslinks FROM PUBLIC;
REVOKE ALL ON TABLE genomeslinks FROM postgres;
GRANT ALL ON TABLE genomeslinks TO postgres;
GRANT ALL ON TABLE genomeslinks TO pl;


--
-- Name: links; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE links FROM PUBLIC;
REVOKE ALL ON TABLE links FROM postgres;
GRANT ALL ON TABLE links TO postgres;
GRANT ALL ON TABLE links TO pl;


--
-- Name: links_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE links_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE links_id_seq FROM postgres;
GRANT ALL ON SEQUENCE links_id_seq TO postgres;
GRANT ALL ON SEQUENCE links_id_seq TO pl;


--
-- Name: segments; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE segments FROM PUBLIC;
REVOKE ALL ON TABLE segments FROM postgres;
GRANT ALL ON TABLE segments TO postgres;
GRANT ALL ON TABLE segments TO pl;


--
-- Name: specimen; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE specimen FROM PUBLIC;
REVOKE ALL ON TABLE specimen FROM postgres;
GRANT ALL ON TABLE specimen TO postgres;
GRANT ALL ON TABLE specimen TO pl;


--
-- PostgreSQL database dump complete
--

