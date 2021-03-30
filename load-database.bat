@echo off
echo The mysql container will be started
docker run -p 6603:3306 --name JMakowska-db -e MYSQL_ROOT_PASSWORD=D@tab@seP@ssWOrd -d mysql:latest --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
timeout 15
echo The database is loaded into the container
docker exec -i JMakowska-db sh -c "exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"" < ./all-databases.sql