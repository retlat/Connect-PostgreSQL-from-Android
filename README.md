# Connect PostgreSQL from Android

This is sample application for demonstrating Android can connect to PostgreSQL with JDBC driver.
Only tested on Android Emulator and Docker Desktop for Mac.

## Requirements
- Docker
- Android Emulator
  * Android Nougat (API level 24) or later system image

## Usage
1. Launch emulator
1. Check gateway IP address in `Settings` app
    1. Open `Settings` app
    1. Tap `Network & Internet` => `Wifi` => `Android Wifi` => `Advanced`
    1. Check `Gateway`
1. Run PostgreSQL on Docker
    ```
    $ docker run --rm -e POSTGRES_PASSWORD=password -p 5432:5432 postgres:12.1
    ```
1. Run this app on emulator
1. Fill connection info
    - Use gateway ip address checked above to fill host
    - Use `postgres` to user and database name if not add other environment variable to `docker run`
    - Use `password` for password field
1. Tap `connect` button
    - Connection result is shown by toast
1. Tap `Create sample table` button first, then tap `Select from sample table` button
1. Result will be displayed


## Compatibility of PostgreSQL JDBC Driver and Android
| JDBC        | API level   | comment |
| ----------- | ----------- | ------- |
| 42.2.9      | 26 or later | JDBC for Java 8 or newer uses java.time.Duration but Nougat doesn't support it |
| 42.2.9.jre7 | 24 or later | |
