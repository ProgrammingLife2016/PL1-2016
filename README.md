# PL1-2016

[![Build Status](https://travis-ci.org/ProgrammingLife2016/PL1-2016.png)](https://travis-ci.org/ProgrammingLife2016/PL1-2016.png)

## Group Information

Group name: Byzantine Generals

Group members: Ravi Shivam Autar, Adam el Khalki, Ali Smesseim, Samuel D. Sital,
Kamran Tadzjibov.

## Build information

### Required packages

Git, Maven and JDK8 are required for building and running this project.

#### Arch Linux

    pacman -S git maven jdk8-openjdk

#### Ubuntu

    apt-get update
    apt-get install git maven openjdk-8-jdk

#### Windows

Not officially supported.

### First time running

    git clone https://github.com/ProgrammingLife2016/PL1-2016.git
    cd PL1-2016
    mvn package
    java -cp target/byzantinegenerals-0.1.0.jar io.github.programminglife2016.pl1_2016.Launcher

### Subsequent runs

Go to the `PL1-2016` directory.

    git pull
    mvn package
    java -cp target/byzantinegenerals-0.1.0.jar io.github.programminglife2016.pl1_2016.Launcher

## Usage

The server is running on `http://localhost:8081`.

Open the web app at `http://localhost:8081/static/index.html`.

If you are a developer, you might be interested in interacting with the server
directly. In that case, consult the API documentation below.

## API documentation

- `/api/nodes` returns all nodes of a dataset, without collapsion.
- `/api/nodes/<xmin>/<xmax>/<ymin>/<ymax>/<totalx>/<totaly> returns all nodes of
  a dataset with coordinates between `<xmin>`, `<xmax>`, `<ymin>`, `<ymax>`.
  `<totalx>` and `<totaly>` are the dimensions of the viewport of the client.
  The server may or may not base the node coordinates on the dimensions of the
  viewport.
- `/api/dataset/<dataset>` returns all nodes of the dataset named `<dataset>`,
  without collapsion.
- `/api/dataset/<dataset>/<xmin>/<xmax>/<ymin>/<ymax>/<totalx>/<totaly>` returns
  all nodes of a dataset with coordinates between `<xmin>`, `<xmax>`, `<ymin>`,
  `<ymax>`. `<totalx>` and `<totaly>` are the dimensions of the viewport of the
  client. The server may or may not base the node coordinates on the dimensions
  of the viewport.

