--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.20
-- Dumped by pg_dump version 9.5.20

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP INDEX IF EXISTS public.la2015_geom_idx;
DROP INDEX IF EXISTS public.fcast_lookup_geom_gist;
DROP INDEX IF EXISTS public.day5_geom_gist;
DROP INDEX IF EXISTS public.day4_geom_gist;
DROP INDEX IF EXISTS public.day3_geom_gist;
DROP INDEX IF EXISTS public.day2_geom_gist;
DROP INDEX IF EXISTS public.day1_geom_gist;
ALTER TABLE IF EXISTS ONLY public.la2015 DROP CONSTRAINT IF EXISTS la2015_pkey;
ALTER TABLE IF EXISTS ONLY public.fcast_lookup DROP CONSTRAINT IF EXISTS fcast_lookup_pkey;
ALTER TABLE IF EXISTS ONLY public.day5 DROP CONSTRAINT IF EXISTS day5_pkey;
ALTER TABLE IF EXISTS ONLY public.day4 DROP CONSTRAINT IF EXISTS day4_pkey;
ALTER TABLE IF EXISTS ONLY public.day3 DROP CONSTRAINT IF EXISTS day3_pkey;
ALTER TABLE IF EXISTS ONLY public.day2 DROP CONSTRAINT IF EXISTS day2_pkey;
ALTER TABLE IF EXISTS ONLY public.day1 DROP CONSTRAINT IF EXISTS day1_pkey;
ALTER TABLE IF EXISTS public.la2015 ALTER COLUMN gid DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.la2015_gid_seq;
DROP TABLE IF EXISTS public.la2015;
DROP TABLE IF EXISTS public.fcast_lookup;
DROP TABLE IF EXISTS public.day5;
DROP TABLE IF EXISTS public.day4;
DROP TABLE IF EXISTS public.day3;
DROP TABLE IF EXISTS public.day2;
DROP TABLE IF EXISTS public.day1;
DROP EXTENSION IF EXISTS postgis_topology;
-- zzz - Next line commented out to get rid of the error when starting the image locally: cannot drop extension postgis because other objects depend on it
-- DROP EXTENSION IF EXISTS postgis;
-- zzz - Next line commented out to get rid of the error when starting the image locally: cannot drop extension plpgsql because other objects depend on it
-- DROP EXTENSION IF EXISTS plpgsql;
DROP SCHEMA IF EXISTS topology;
-- zzz - Next line commented out to get rid of the error when starting the image locally: cannot drop schema public because other objects depend on it
-- DROP SCHEMA IF EXISTS public;
--
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

-- zzz - I added IF NOT EXISTS in the line below
CREATE SCHEMA IF NOT EXISTS public;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: topology; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA topology;


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- Name: postgis_topology; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis_topology WITH SCHEMA topology;


--
-- Name: EXTENSION postgis_topology; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION postgis_topology IS 'PostGIS topology spatial types and functions';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: day1; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.day1 (
    xcen integer NOT NULL,
    ycen integer NOT NULL,
    aq_index integer,
    geom public.geometry(Polygon,27700)
);


--
-- Name: day2; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.day2 (
    xcen integer NOT NULL,
    ycen integer NOT NULL,
    aq_index integer,
    geom public.geometry(Polygon,27700)
);


--
-- Name: day3; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.day3 (
    xcen integer NOT NULL,
    ycen integer NOT NULL,
    aq_index integer,
    geom public.geometry(Polygon,27700)
);


--
-- Name: day4; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.day4 (
    xcen integer NOT NULL,
    ycen integer NOT NULL,
    aq_index integer,
    geom public.geometry(Polygon,27700)
);


--
-- Name: day5; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.day5 (
    xcen integer NOT NULL,
    ycen integer NOT NULL,
    aq_index integer,
    geom public.geometry(Polygon,27700)
);


--
-- Name: fcast_lookup; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fcast_lookup (
    xcen integer NOT NULL,
    ycen integer NOT NULL,
    uwe_2010 integer,
    geom public.geometry(Point,27700)
);


--
-- Name: la2015; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.la2015 (
    gid integer NOT NULL,
    objectid integer,
    uwe_2010 numeric,
    olduwecode numeric,
    aka character varying(254),
    name character varying(254),
    full_name character varying(254),
    formal_nam character varying(254),
    area_code character varying(3),
    descriptio character varying(50),
    laa_code numeric,
    secondtier character varying(254),
    type character varying(254),
    gor_code integer,
    goregion character varying(254),
    da character varying(50),
    la_number double precision,
    geom public.geometry(MultiPolygon,27700)
);


--
-- Name: la2015_gid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.la2015_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: la2015_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.la2015_gid_seq OWNED BY public.la2015.gid;


--
-- Name: gid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.la2015 ALTER COLUMN gid SET DEFAULT nextval('public.la2015_gid_seq'::regclass);

--
-- Name: la2015_gid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.la2015_gid_seq', 1, false);


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: topology; Type: TABLE DATA; Schema: topology; Owner: -
--



--
-- Data for Name: layer; Type: TABLE DATA; Schema: topology; Owner: -
--



--
-- Name: day1_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.day1
    ADD CONSTRAINT day1_pkey PRIMARY KEY (xcen, ycen);


--
-- Name: day2_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.day2
    ADD CONSTRAINT day2_pkey PRIMARY KEY (xcen, ycen);


--
-- Name: day3_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.day3
    ADD CONSTRAINT day3_pkey PRIMARY KEY (xcen, ycen);


--
-- Name: day4_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.day4
    ADD CONSTRAINT day4_pkey PRIMARY KEY (xcen, ycen);


--
-- Name: day5_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.day5
    ADD CONSTRAINT day5_pkey PRIMARY KEY (xcen, ycen);


--
-- Name: fcast_lookup_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fcast_lookup
    ADD CONSTRAINT fcast_lookup_pkey PRIMARY KEY (xcen, ycen);


--
-- Name: la2015_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.la2015
    ADD CONSTRAINT la2015_pkey PRIMARY KEY (gid);


--
-- Name: day1_geom_gist; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX day1_geom_gist ON public.day1 USING gist (geom);


--
-- Name: day2_geom_gist; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX day2_geom_gist ON public.day2 USING gist (geom);


--
-- Name: day3_geom_gist; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX day3_geom_gist ON public.day3 USING gist (geom);


--
-- Name: day4_geom_gist; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX day4_geom_gist ON public.day4 USING gist (geom);


--
-- Name: day5_geom_gist; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX day5_geom_gist ON public.day5 USING gist (geom);


--
-- Name: fcast_lookup_geom_gist; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fcast_lookup_geom_gist ON public.fcast_lookup USING gist (geom);


--
-- Name: la2015_geom_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX la2015_geom_idx ON public.la2015 USING gist (geom);


--
-- PostgreSQL database dump complete
--

--
-- Start of manual addition using ukair_now_dump.sql provided by Ian on 05/06/2023.
--

--
-- For table site:
--      - I removed quotes on the table name and column names.
--      - For column types, I changed:
--          - all int(x) into int, all smallint(x) into smallint, all tinyint(x) into SMALLINT.
--          - enum('primary','duplicate','unused') into the creation of type public.usageType and its usage on column usage_type.
--          - easting & northing into geom public.geometry(Point,27700): required to use PostGIS to identify the nearest station to a point.
--      - For keys, I changed:
--          - PRIMARY KEY (site_id) --> ALTER TABLE ONLY public.site ADD CONSTRAINT site_pkey PRIMARY KEY (site_id);
--          - la_district_region --> index site_la_district_region
--          - idx_02_site --> index site_uka_site_id
--      - For default values:
--          - '0000-00-00' -> NOW()
--
DROP TYPE IF EXISTS public.usageType;
CREATE TYPE public.usageType AS ENUM ('primary','duplicate','unused');

DROP INDEX IF EXISTS public.site_uka_site_id;
DROP INDEX IF EXISTS public.site_la_district_region;
ALTER TABLE IF EXISTS ONLY public.site DROP CONSTRAINT IF EXISTS site_pkey;
DROP TABLE IF EXISTS public.site;

CREATE TABLE public.site (
  id varchar(50) NOT NULL DEFAULT '0',
  uka_site_id varchar(30) NOT NULL,
  usage_type usageType NOT NULL DEFAULT 'primary',
  county_id int NOT NULL DEFAULT '0',
  name varchar(255) DEFAULT NULL,
  latitude decimal(12,6) DEFAULT NULL,
  longitude decimal(12,6) DEFAULT NULL,
  location_x varchar(30) DEFAULT NULL,
  location_y varchar(30) DEFAULT NULL,
  location_type_id int DEFAULT NULL,
  DEM_location_type_id int DEFAULT '406',
  environment_id int DEFAULT NULL,
  altitude varchar(30) DEFAULT NULL,
  date_started date DEFAULT NULL,
  date_ended date NOT NULL DEFAULT NOW(),
  height varchar(30) DEFAULT NULL,
  kerb_distance varchar(255) DEFAULT NULL,
  la_region int DEFAULT NULL,
  new_la_region_id smallint DEFAULT NULL,
  ea_region int DEFAULT NULL,
  borough_region int DEFAULT NULL,
  gov_region int DEFAULT NULL,
  la_district_region int DEFAULT '9999',
  zone_region int DEFAULT '2',
  agglomeration_region int DEFAULT NULL,
  la_type_region int DEFAULT NULL,
  easting decimal(12,6) DEFAULT NULL,
  northing decimal(12,6) DEFAULT NULL,
  geom public.geometry(Point,27700) DEFAULT NULL,
  country_region int DEFAULT NULL,
  ua_id int DEFAULT NULL,
  analytical_lab_id int DEFAULT NULL,
  created_on date DEFAULT NULL,
  created_by int DEFAULT NULL,
  record_status varchar(10) NOT NULL DEFAULT 'Approved',
  address_line_1 varchar(50) DEFAULT NULL,
  address_line_2 varchar(50) DEFAULT NULL,
  address_line_3 varchar(50) DEFAULT NULL,
  postcode varchar(10) DEFAULT '',
  sub_office_id int DEFAULT NULL,
  EU_site_id varchar(30) DEFAULT NULL,
  la_reference varchar(15) DEFAULT NULL,
  EMEP_site_id varchar(10) DEFAULT NULL,
  altitude_erep int DEFAULT NULL,
  TfGM SMALLINT NOT NULL DEFAULT '0',
  wrf_10_id int DEFAULT NULL,
  la_site_id varchar(100) DEFAULT NULL,
  api_config varchar(255) DEFAULT NULL
);

ALTER TABLE ONLY public.site ADD CONSTRAINT site_pkey PRIMARY KEY (id);
CREATE INDEX site_uka_site_id ON public.site USING btree(uka_site_id);
CREATE INDEX site_la_district_region ON public.site USING btree(la_district_region);
--
-- End of manual addition using ukair_now_dump.sql provided by Ian on 05/06/2023.
--

--
-- Start of manual addition for the measurements path: required so it matches the Java code put together when pointing to H2.
--
ALTER TABLE IF EXISTS ONLY public.pollutants DROP CONSTRAINT IF EXISTS pollutants_pkey;
DROP TABLE IF EXISTS public.pollutants;
CREATE TABLE public.pollutants (
  id int NOT NULL,
  code varchar(30) NOT NULL,
  name varchar(20),
  description varchar(100)
);
ALTER TABLE ONLY public.pollutants ADD CONSTRAINT pollutants_pkey PRIMARY KEY (id);


ALTER TABLE IF EXISTS ONLY public.measurements DROP CONSTRAINT IF EXISTS measurements_pkey;
ALTER TABLE IF EXISTS ONLY public.measurements DROP CONSTRAINT IF EXISTS measurements_site_fkey;
ALTER TABLE IF EXISTS ONLY public.measurements DROP CONSTRAINT IF EXISTS measurements_CK1;
DROP TABLE IF EXISTS public.measurements;
CREATE TABLE public.measurements (
  id SERIAL NOT NULL,
  sampled_time timestamp NOT NULL,
  site_id varchar(50) NOT NULL
);
ALTER TABLE ONLY public.measurements ADD CONSTRAINT measurements_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.measurements ADD CONSTRAINT measurements_site_fkey FOREIGN KEY (site_id) REFERENCES site (id);
-- measurements_CK1 has been defined so for a given site, we can only have one measurement at a given time.
ALTER TABLE ONLY public.measurements ADD CONSTRAINT measurements_CK1 UNIQUE (sampled_time, site_id);


ALTER TABLE IF EXISTS ONLY public.MEASUREMENT_POLLUTANT_MAPPING DROP CONSTRAINT IF EXISTS MEASUREMENT_POLLUTANT_MAPPING_pkey;
ALTER TABLE IF EXISTS ONLY public.MEASUREMENT_POLLUTANT_MAPPING DROP CONSTRAINT IF EXISTS MEASUREMENT_POLLUTANT_MAPPING_measurements_fkey;
DROP TABLE IF EXISTS public.MEASUREMENT_POLLUTANT_MAPPING;
CREATE TABLE public.MEASUREMENT_POLLUTANT_MAPPING (
  MEASUREMENT_ID int NOT NULL,
  POLLUTANT_CODE varchar(30) NOT NULL,
  QUANTITY int
);
ALTER TABLE ONLY public.MEASUREMENT_POLLUTANT_MAPPING ADD CONSTRAINT MEASUREMENT_POLLUTANT_MAPPING_pkey PRIMARY KEY (MEASUREMENT_ID, POLLUTANT_CODE);
ALTER TABLE ONLY public.MEASUREMENT_POLLUTANT_MAPPING ADD CONSTRAINT MEASUREMENT_POLLUTANT_MAPPING_measurements_fkey FOREIGN KEY (MEASUREMENT_ID) REFERENCES measurements (id);
--
-- End of manual addition for the measurements path: required so it matches the Java code put together when pointing to H2.
--