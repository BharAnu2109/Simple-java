#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE order_db;
    CREATE DATABASE customer_db;
    CREATE DATABASE payment_db;
    CREATE DATABASE notification_db;
    
    GRANT ALL PRIVILEGES ON DATABASE restaurant_db TO restaurant_user;
    GRANT ALL PRIVILEGES ON DATABASE order_db TO restaurant_user;
    GRANT ALL PRIVILEGES ON DATABASE customer_db TO restaurant_user;
    GRANT ALL PRIVILEGES ON DATABASE payment_db TO restaurant_user;
    GRANT ALL PRIVILEGES ON DATABASE notification_db TO restaurant_user;
EOSQL