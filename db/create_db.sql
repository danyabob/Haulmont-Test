-- patient table

create table T_PATIENT
(
    ID           bigint generated by default as identity
        (start with 1, increment by 1) not null primary key,
    NAME         nvarchar(32) not null,
    SURNAME      nvarchar(32) not null,
    PATRONYMIC   nvarchar(32) not null,
    PHONE_NUMBER nvarchar(32) not null
);

-- doctor table

create table T_DOCTOR
(
    ID             bigint generated by default as identity
        (start with 1, increment by 1) not null primary key,
    NAME           nvarchar(32) not null,
    SURNAME        nvarchar(32) not null,
    PATRONYMIC     nvarchar(32) not null,
    SPECIALIZATION nvarchar(32) not null
);

-- receipt table

create table T_RECEIPT
(
    ID            bigint generated by default as identity
        (start with 1, increment by 1) not null primary key,
    DESCRIPTION   nvarchar(160) not null,
    PATIENT_ID    bigint               not null,
    DOCTOR_ID     bigint               not null,
    CREATION_DATE date                 not null,
    EXPIRE_DATE   date                 not null,
    PRIORITY      nvarchar(16) not null,
        --
    constraint FK_PATIENT_RECEIPT foreign key (PATIENT_ID) references T_PATIENT(ID) on delete restrict,
    constraint FK_DOCTOR_RECEIPT foreign key (DOCTOR_ID) references T_DOCTOR (ID) on delete restrict
);