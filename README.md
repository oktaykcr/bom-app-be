<p align="center">
  <img width="256" height="256" src="https://github.com/oktaykcr/bom-app-be/blob/master/bom-app-logo.png"  alt="logo"/>
</p>

<h3 align="center">BOM App Backend</h3>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/oktaykcr/bom-app-be.svg)](https://github.com/oktaykcr/bom-app-be/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/oktaykcr/bom-app-be.svg)](https://github.com/oktaykcr/bom-app-be/pulls)

</div>

---

<p align="center"> Manage your components easily.
    <br> 
</p>

## 📝 Table of Contents

- [About](#about)
- [Getting Started](#getting_started)
- [Deployment](#deployment)
- [Usage](#usage)
- [Built Using](#built_using)
- [Authors](#authors)
- [Acknowledgments](#acknowledgement)

## 🧐 About <a name = "about"></a>

- A bill of materials (BOM) is a comprehensive list of parts, items, assemblies, subassemblies, intermediate assemblies,
  documents, drawings, and other materials required to create a product.
- **BOM App** is web application that developed for material tracking and management. In the app you have an inventory
  for your components. It uses [Mouser Electronics API](https://eu.mouser.com/) to fetch necessary information about
  component. For managing components of your project, you need to create a *BOM*. This process is like creating a new
  project. You can create components into created BOM from your inventory. It automatically adjusts quantity on hand
  according to look at inventory.

## 🏁 Getting Started <a name = "getting_started"></a>

The project consist of two side. The backend side is developed with Java Spring Boot,
the [frontend](https://github.com/oktaykcr/bom-app-fe) side is developed with ReactJs.

### Prerequisites

- Install *Docker* to execute `docker-compose.yml` file.

### Installing

1. To create jar file, type `mvn clean package -Dmaven.test.skip=true` in project root directory.
2. At project root directory, execute `docker-compose build` to create *postgres* and *bomapp-backend* images for
   docker.
3. To start containers, execute `docker-compose up`
4. The container will start after these commands, and you can reach to be server from http://locahost:8081. You can
   change port from Dockerfile (`src/main/docker/Dockerfile`).

docker-compose up output:

```
c9b4153b2f85 bom-app "/bin/sh -c 'java -D…" 5 days ago Up 4 seconds 0.0.0.0:8081->8081/tcp bomapp-backend
76154570fc1f postgres "docker-entrypoint.s…" 5 days ago Up 5 seconds 0.0.0.0:5432->5432/tcp bomapp-db
```
### Rest Routes

- You can get api doc from `http://localhost:8081/api/v1/swagger-ui/`. 

## 🎈 Usage <a name="usage"></a>

- Create an account with username, email and password.
- Click `Components` section then click `+` button to add new components into your inventory.
- In the [Mouser](https://eu.mouser.com/) app search any component to get mouser part number.
- Enter *quantity on hand*, *supplier link* and *mouser part number* to create component into your inventory. (Example
  mouser part number of capacitor: `80-C1210C331KDGAUTO`)
- Click BOMs then click `+` button to add a BOM. BOMs are like a new projects.
- Enter *title* and *description* of the BOM.
- Click show button of newly created BOM.
- Click `+` button to add component to use for BOM.
- Choose already created component from your inventory list.
- Enter *quantity*, *cost* and *lead time* to create component to use.

Repeat these steps and manage your components easily.

## 🚀 Deployment <a name = "deployment"></a>

TODO

## ⛏️ Built Using <a name = "built_using"></a>

`bom-app-be` uses following technologies and frameworks:

- Spring Boot: 2.5.4
    - data-jpa
    - web
    - security
    - test
    - actuator
- jsonwebtoken
- postgreSQL
- docker
- lombok
- flywaydb
- swagger

## ✍️ Authors <a name = "authors"></a>

- [@oktaykcr](https://github.com/oktaykcr) - Design and implement
- [@okankocer](https://linkedin.com/in/okan-koçer-b3327615b) - Idea

See also the list of [contributors](https://github.com/oktaykcr/bom-app-be/contributors) who
participated in this project.

## 🎉 Acknowledgements <a name = "acknowledgement"></a>

- [Mouser Electronics](https://eu.mouser.com/)