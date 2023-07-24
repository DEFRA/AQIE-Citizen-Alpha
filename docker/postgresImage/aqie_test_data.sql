-- I picked up the first 3 lines for table day1 in the dump provided by Ian on 22/05/2023  (see aq_forecast_data.sql).
INSERT INTO public.day1 VALUES (-58000, 1222000, 3, '0103000020346C000001000000050000000000000000CFECC00000000088A132410000000000CFECC00000000058A932410000000000D5EBC00000000058A932410000000000D5EBC00000000088A132410000000000CFECC00000000088A13241');
INSERT INTO public.day1 VALUES (-56000, 1222000, 3, '0103000020346C000001000000050000000000000000D5EBC00000000088A132410000000000D5EBC00000000058A932410000000000DBEAC00000000058A932410000000000DBEAC00000000088A132410000000000D5EBC00000000088A13241');
INSERT INTO public.day1 VALUES (-54000, 1222000, 3, '0103000020346C000001000000050000000000000000DBEAC00000000088A132410000000000DBEAC00000000058A932410000000000E1E9C00000000058A932410000000000E1E9C00000000088A132410000000000DBEAC00000000088A13241');

-- I picked up the first 3 lines for table site in the dump provided by Ian on 05/06/2023 (see ukair_now_dump.sql).
-- I replaced '0000-00-00' with NOW().
-- Nearest postcode = SK16 4TH according to https://gridreferencefinder.com/
INSERT INTO public.site VALUES ('1000002','UKA10609','primary',20,'DUKINFIELD 2',NULL,NULL,'3940','3979',0,406,0,NULL,NOW(),NOW(),'','',331,NULL,4,80,6,170,10,3,NULL,394000.00,397900.00,ST_Point(394000.00, 397900.00, 27700),1,315,NULL,NULL,NULL,'Approved','','','','',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL);
-- Nearest postcode = SK16 5BY according to https://gridreferencefinder.com/
INSERT INTO public.site VALUES ('1000003','UKA11785','primary',20,'DUKINFIELD 3',NULL,NULL,'3954','3970',0,406,0,NULL,NOW(),NOW(),'','',331,NULL,4,80,6,170,10,3,NULL,395400.00,397000.00,ST_Point(395400.00, 397000.00, 27700),1,315,NULL,NULL,NULL,'Approved','','','','',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL);
-- Nearest postcode = SK16 5HE according to https://gridreferencefinder.com/
INSERT INTO public.site VALUES ('1000004','UKA12081','primary',20,'DUKINFIELD 4',NULL,NULL,'3956','3974',0,406,0,NULL,NOW(),NOW(),'','',331,NULL,4,80,6,170,10,3,NULL,395600.00,397400.00,ST_Point(395600.00, 397400.00, 27700),1,315,NULL,NULL,NULL,'Approved','','','','',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL);

-- The data below mirrors what has been done in H2. No link to UK Air.
INSERT INTO public.pollutants (id, code, name, description) VALUES (1, 'OZONE', 'Ozone', 'Desc for ozone');
INSERT INTO public.pollutants (id, code, name, description) VALUES (2, 'NITROGEN_DIOXIDE', 'NO2', 'Desc for nitrogen dioxide');
INSERT INTO public.pollutants (id, code, name, description) VALUES (3, 'SULPHUR_DIOXIDE', 'SO2', 'Desc for sulphur dioxide');
INSERT INTO public.pollutants (id, code, name, description) VALUES (4, 'PM2DOT5', 'PM2.5', 'Particles that are 2.5 microns or less in diameter');
INSERT INTO public.pollutants (id, code, name, description) VALUES (5, 'PM10', 'PM10', 'Particles that are 10 microns or less in diameter');