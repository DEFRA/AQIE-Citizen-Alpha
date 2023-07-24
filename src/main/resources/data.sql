-- Only used when pointing to H2.
INSERT INTO site (id, name, uka_site_id, usage_type, easting, northing) VALUES ('1000002', 'DUKINFIELD 2', 'UKA10609', 'primary', 394000, 397900);
INSERT INTO site (id, name, uka_site_id, usage_type, easting, northing) VALUES ('1000003', 'DUKINFIELD 3', 'UKA11785', 'primary', 395400, 397000);

INSERT INTO pollutants (id, code, name, description) VALUES (1, 'OZONE', 'Ozone', 'Desc for ozone');
INSERT INTO pollutants (id, code, name, description) VALUES (2, 'NITROGEN_DIOXIDE', 'NO2', 'Desc for nitrogen dioxide');
INSERT INTO pollutants (id, code, name, description) VALUES (3, 'SULPHUR_DIOXIDE', 'SO2', 'Desc for sulphur dioxide');
INSERT INTO pollutants (id, code, name, description) VALUES (4, 'PM2DOT5', 'PM2.5', 'Particles that are 2.5 microns or less in diameter');
INSERT INTO pollutants (id, code, name, description) VALUES (5, 'PM10', 'PM10', 'Particles that are 10 microns or less in diameter');
