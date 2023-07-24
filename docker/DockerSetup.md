This has been set up using macOS Ventura 13.3.1 (a).  
I have updated Docker to the latest version. docker -v shows Docker version 23.0.5. 

# To run PostgreSQL locally

## To build the AQIE PostgreSQL image

### Prerequisites
Under AQIE-Citizen-Alpha/docker, create directory postgresImage.

Under postgresImage, create files:
- aqie_groundzero.sql: Inside, I have copied the schema export (from the DB in Azure) that Ian gave me.
    - Note that I had to comment out a few lines. See 'zzz'.
- aqie_test_data.sql: Inside, I have copied some sample SQL that I have commented out for now.
- Dockerfile: Inside, I describe which version of PostgreSQL we want. I went for 14-bullseye which was the latest available one. Note that in Azure, Ian uses 11.19.
- initdb-postgis.sh & update-postgis.sh : I copied them from https://github.com/postgis/docker-postgis/tree/81a0b55135c13f23045e1504b5e78239fe0df3fa/14-3.2.
    - This came after I built an image without these files and the PostGIS entry in Dockerfile. When running the image, I hit the issue: extension "postgis" is not available. I then read the section 'Additional Extensions' at https://hub.docker.com/_/postgres.
    - Note that the POSTGIS_VERSION given at https://github.com/postgis/docker-postgis/blob/81a0b55135c13f23045e1504b5e78239fe0df3fa/14-3.2/Dockerfile was wrong. I figured out the correct version by doing:
        - Download http://apt.postgresql.org/pub/repos/apt/dists/bullseye-pgdg/main/binary-amd64/Packages
        - Open it in Visual Studio Code and search for the 2 lines
      ```
      Package: postgresql-14-postgis-3
      Source: postgis
      ```
        - The following line says: Version: 3.3.3+dfsg-1~exp1.pgdg110+1
        - In the file Dockerfile, update ENV POSTGIS_VERSION with the correct version.

### Build the image
cd AQIE-Citizen-Alpha/docker/postgresImage  
docker build -t aqiepostgresimage --no-cache .

Verify the image now exists: docker image list

Delete redundant images: docker image rm IMAGE_ID
- You may need to delete containers using redundant images with: docker container rm CONTAINER_NAME

### Test the image

#### Prerequisites
Install pgAdmin:
- Go to https://www.pgadmin.org/download/pgadmin-4-macos/
- Select the latest version.
- Download the .dmg file.
- Double-click on it to install it. Note that if pgAdmin is already installed, you can select 'Replace' and it will keep your connection details.

#### Test
docker run -p 6432:5432 aqiepostgresimage

Open pgAdmin.  
Create a server connection with:
- host name = localhost
- port = 6432 because this is the value of ${EX_POSTGRES_PORT} in .env
- maintenance database = postgres
- username = postgres
- password = pocpwd

In the database postgres, I can see 4 schemas: public, tiger, tiger_data, topology
- In schema public, I can see tables day1, day2, etc. which confirms that the script aqie_groundzero.sql was used.

Check that you can execute the script aqie_test_data.sql which is part of the Docker image:
- Using pgAdmin, I logged into the DB postgres and issued the commands:
    - truncate table day1
    - select count(*) from day1 -> 0
- In aqie_test_data.sql, I had (Remember that if you modify its content, you need to rebuild the image!):
  ```
  SET SCHEMA 'public';
  INSERT INTO day1(xcen, ycen, aq_index, geom) values (1, 2, 3, null);
  ```
- Identify the name of your running container:
    - docker ps
    - see column NAMES for image aqiepostgresimage = CONTAINER_NAME
- cd AQIE-Citizen-Alpha/docker/postgresImage
- docker exec CONTAINER_NAME psql -d postgres -U postgres -a -f aqie_test_data.sql
    - The flag d is for database.
    - The flag U is for user.
    - The flag a is to output cmds & results to the console.
- In pgAdmin:
    - select count(*) from day1 should show 1.

### Publish the image to Docker Cloud
docker login with brossierp  
docker tag aqiepostgresimage brossierp/aqiepostgres:0.0.1  
docker push brossierp/aqiepostgres:0.0.1

Verify that the image can be seen at https://hub.docker.com/repositories/brossierp:
- namespace = account = brossierp
- repository = aqiepostgres

## To set up the local dev environment
Under AQIE-Citizen-Alpha/docker, create file .env. It sets various environment variables that are used in docker-compose-dev_env.yml.  
Create file docker-compose-dev_env.yml:
- To figure out which value (in our case "3.9") to use for version, read https://docs.docker.com/compose/compose-file/compose-file-v3/
  To create & start containers in the background: docker-compose -f docker-compose-dev_env.yml up -d

# Cheatsheets
To log into Docker: docker login

To list all images: docker image list
To delete image postgresimage: docker image rm postgresimage

To list all running containers: docker ps  
To list all containers: docker ps -a
To run an Interactive Shell in container named sleepy_shtern: docker exec -it sleepy_shtern sh  
To stop a container: docker stop sleepy_shtern  
To delete a container: docker rm sleepy_shtern