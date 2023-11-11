# SecretService

## Setup

### Prerequisites
Docker desktop should be installed https://www.docker.com/products/docker-desktop/

### Steps to run
I. First go to the src/main/resources/application.yaml file
  1. Comment the datasource config for local db configuration
  2. Uncomment the datasource config for docker
![image](https://github.com/SecretKeepers/secret-keeper-service/assets/146482903/1c14c0ee-0c09-4f96-a662-006ea3c8467c)


II. In the project root run the command- `docker compose up` and wait for it to complete

III. Secret service should be running at http://localhost:8080

### API Info
- Only /signin and /register APIs are public
- For accessing any other API you must be logged in first
- Find the list of APIs available in handlers package
