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
    foreign key (companyId) references company (id)
)

create table BlastingTemplate
(
    id         bigint primary key identity (1, 1),
    name       varchar(100) not null,
    channel    varchar(100) not null,
    templateId varchar(100) not null,
    phone      varchar(100) not null,
    fields     varchar(100) not null,
    sendCount  int          not null
)

create table Ticket
(
    id           bigint primary key identity (1, 1),
    number       varchar(100) unique not null,
    parentId     bigint,
    level        int                 not null,
    description  varchar(500)        not null,
    attachmentId bigint,
    foreign key (attachmentId) references FileRecord (id),
    foreign key (parentId) references Ticket (id)
)

CREATE TABLE FileRecord
(
    id            INT PRIMARY KEY identity (1,1),
    fileName      NVARCHAR(255),
    generatedName NVARCHAR(255) UNIQUE,
    extension     VARCHAR(10),
    fileData      VARBINARY(MAX)
);

select *
from Company;

select *
from UserTable;

select *
from FileRecord;