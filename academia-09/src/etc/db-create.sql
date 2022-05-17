drop table if exists person cascade;
drop table if exists module cascade;
drop table if exists run cascade;
drop table if exists enroll cascade;
drop table if exists teacher_run cascade;

CREATE TABLE person
(
    pid             SERIAL,
    firstname       varchar(255) NOT NULL,
    lastname        varchar(255) NOT NULL,
    address         varchar(255) NOT NULL,
    sex             char(1) NOT NULL,
    birthdate       date NOT NULL,
    username        varchar(255) NOT NULL,
    password        varchar(256) NOT NULL,
    role            varchar(10) NOT NULL,
    PRIMARY KEY (pid)
);

CREATE TABLE module
(
    mid             varchar(255) NOT NULL,
    name            varchar(255) NOT NULL,
    description     varchar(1000),
    pid             int NOT NULL,
    version         int NOT NULL DEFAULT 0,
    PRIMARY KEY (mid),
    FOREIGN KEY (pid) REFERENCES person(pid)
);

CREATE TABLE run
(
    mrid        SERIAL,
    mid         varchar(255) NOT NULL,
    year        smallint NOT NULL,
    semester    varchar(2) NOT NULL,
    PRIMARY KEY (mrid),
    FOREIGN KEY (mid) REFERENCES module(mid)

);

CREATE TABLE teacher_run
(
    pid     int NOT NULL,
    mrid    int NOT NULL,
    PRIMARY KEY (pid,mrid),
    FOREIGN KEY (pid) REFERENCES person(pid),
    FOREIGN Key (mrid) REFERENCES run(mrid)
);

CREATE TABLE enroll
(
    mrid        int NOT NULL,
    pid         int NOT NULL,
    grade       char(1),
    version     int NOT NULL DEFAULT 0,
    PRIMARY KEY (pid,mrid),
    FOREIGN KEY (pid) REFERENCES person(pid),
    FOREIGN Key (mrid) REFERENCES run(mrid)
);