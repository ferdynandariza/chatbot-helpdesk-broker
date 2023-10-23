create database chatbotHelpdesk;

use chatbotHelpdesk;

drop table FileRecord;
drop table Ticket;
drop table UserTable;
drop table Company;
drop table BlastingTemplate;

create table Company
(
    id   bigint primary key identity (1, 1),
    name varchar(100) not null
);

create table UserTable
(
    id         bigint primary key identity (1, 1),
    companyId  bigint,
    name       varchar(100) not null,
    phone      varchar(100),
    telegramId varchar(100),
    foreign key (companyId) references company (id),
    unique (phone, telegramId)
);

create table BlastingTemplate
(
    id         bigint primary key identity (1, 1),
    name       varchar(100) not null,
    channel    varchar(100) not null,
    templateId varchar(100) not null,
    phone      varchar(100) not null,
    fields     varchar(100) not null,
    sendCount  int          not null
);

create table Ticket
(
    id          bigint primary key identity (1, 1),
    number      varchar(100) unique not null,
    requestorId bigint              not null,
    description varchar(500)        not null,
    createdAt   bigint              not null,
    foreign key (requestorId) references UserTable (id)
);

CREATE TABLE FileRecord
(
    id            VARCHAR(100) PRIMARY KEY,
    fileName      NVARCHAR(255),
    extension     VARCHAR(10),
    fileData      VARBINARY(MAX),
    ticketId      bigint,
    foreign key (ticketId) references Ticket (id)
);

select *
from Company;

select *
from UserTable;

select *
from Ticket;

select *
from FileRecord;

select *
from BlastingTemplate;

delete from FileRecord where ticketId is null;