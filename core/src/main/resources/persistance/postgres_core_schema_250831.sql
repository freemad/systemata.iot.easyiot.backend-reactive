CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;
-- select * from pg_extension;

CREATE SCHEMA IF NOT EXISTS "public";

CREATE TABLE IF NOT EXISTS "owner"
(
    id                 uuid default uuid_generate_v4() not null,
    created_by         varchar(255),
    last_modified_by   varchar(255),
    created_date       timestamp without time zone,
    last_modified_date timestamp without time zone,
    first_name         varchar(255),
    last_name          varchar(255),
    date_of_birth      bigint,
    gender             char,
    CONSTRAINT pk_owner PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "device_type"
(
    id                 uuid default uuid_generate_v4() not null,
    created_by         varchar(255),
    last_modified_by   varchar(255),
    created_date       timestamp without time zone,
    last_modified_date timestamp without time zone,
    name               varchar(255),
    model              varchar(255),
    description        varchar(255),
    CONSTRAINT pk_device_type PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "gate_type"
(
    id                 uuid default uuid_generate_v4() not null,
    created_by         varchar(255),
    last_modified_by   varchar(255),
    created_date       timestamp without time zone,
    last_modified_date timestamp without time zone,
    name               varchar(255),
    is_input           bool,
    is_extended        bool,
    max_value          int4,
    base_value         int4,
    pin_count          int2,
    CONSTRAINT pk_gate_type PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "asset"
(
    id                 uuid default uuid_generate_v4() not null,
    created_by         varchar(255),
    last_modified_by   varchar(255),
    created_date       timestamp without time zone,
    last_modified_date timestamp without time zone,
    name               varchar(255),
    label              varchar(255),
    owner_id           uuid,
    CONSTRAINT pk_asset PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "device"
(
    id                 uuid default uuid_generate_v4() not null,
    created_by         varchar(255),
    last_modified_by   varchar(255),
    created_date       timestamp without time zone,
    last_modified_date timestamp without time zone,
    name               varchar(255),
    label              varchar(255),
    hardware_id        uuid,
    asset_id           uuid,
    device_type_id     uuid,
    CONSTRAINT pk_device PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "device_type_gate"
(
    id                 uuid default uuid_generate_v4() not null,
    created_by         varchar(255),
    last_modified_by   varchar(255),
    created_date       timestamp without time zone,
    last_modified_date timestamp without time zone,
    label              varchar(255),
    gate_idx           int2,
    device_type_id     uuid,
    gate_type_id       uuid,
    CONSTRAINT pk_device_type_gate PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "gate"
(
    id                  uuid DEFAULT uuid_generate_v4() NOT NULL,
    created_by          varchar(255),
    last_modified_by    varchar(255),
    created_date        timestamp without time zone default now(),
    last_modified_date  timestamp without time zone default now(),
    name                varchar(255) not null,
    label               varchar(255),
    gate_idx            int2 not null,
    value               double precision,
    base_value          double precision not null,
    max_value           double precision not null,
    device_id           uuid not null,
    device_type_gate_id uuid not null,
    CONSTRAINT pk_gate PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "gate_data" (
    gate_id             uuid not null, -- REFERENCES gate(id),
    ts                  timestamptz not null default now(),
    value               double precision not null,
    CONSTRAINT pk_gate_data PRIMARY KEY (gate_id, ts)
);
SELECT create_hypertable('gate_data', 'ts');
CREATE INDEX ON gate_data (gate_id, ts DESC);


-------------------------
-- BASE DATA INSERTION --
-------------------------

-- OWNER
insert into owner (id, created_by, last_modified_by, created_date, last_modified_date, first_name, last_name,
                   date_of_birth, gender)
values ('12345678-1234-1234-1234-123456789abc', 'admin', 'admin', now(), now(), 'Admin', 'Admin', 0, 1);
-- values ('d950b2d2-7dce-494f-b952-565e208b0605', 'admin', 'admin', now(), now(), 'Admin', 'Admin', 0, 1);

-- DEVICE_TYPE
insert into device_type (id, created_by, last_modified_by, created_date, last_modified_date, name, model, description)
values ('12345678-1234-1234-1234-123456789abd', 'admin', 'admin', now(), now(), 'Test Module 1.0',
        'TM1.0', 'the 1st test module of devices');

-- GATE_TYPE
insert into gate_type (id, created_by, last_modified_by, created_date, last_modified_date, name, is_input, is_extended,
                       max_value, base_value, pin_count)
values ('12345678-1234-1234-1234-123456789abd', 'admin', 'admin', now(), now(), 'bin. in', true, false, 1, 0, 1);
insert into gate_type (id, created_by, last_modified_by, created_date, last_modified_date, name, is_input, is_extended,
                       max_value, base_value, pin_count)
values ('12345678-1234-1234-1234-123456789abe', 'admin', 'admin', now(), now(), 'bin. out', false, false, 1, 0, 1);

-- DEVICE_TYPE_GATE :: INPUT
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ab0', 'admin', 'admin', now(), now(),
        'TM1.0: in 1', 0, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abd');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ab1', 'admin', 'admin', now(), now(),
        'TM1.0: in 2', 1, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abd');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ab2', 'admin', 'admin', now(), now(),
        'TM1.0: in 3', 2, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abd');

-- DEVICE_TYPE_GATE :: out
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ac0', 'admin', 'admin', now(), now(),
        'TM1.0: out 1', 0, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abe');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ac1', 'admin', 'admin', now(), now(),
        'TM1.0: out 2', 1, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abe');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ac2', 'admin', 'admin', now(), now(),
        'TM1.0: out 3', 2, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abe');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ac3', 'admin', 'admin', now(), now(),
        'TM1.0: out 4', 3, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abe');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ac4', 'admin', 'admin', now(), now(),
        'TM1.0: out 5', 4, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abe');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ac5', 'admin', 'admin', now(), now(),
        'TM1.0: out 6', 5, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abe');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ac6', 'admin', 'admin', now(), now(),
        'TM1.0: out 7', 6, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abe');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ac7', 'admin', 'admin', now(), now(),
        'TM1.0: out 8', 7, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abe');
insert into device_type_gate (id, created_by, last_modified_by, created_date, last_modified_date, label, gate_idx,
                              device_type_id, gate_type_id)
values ('12345678-1234-1234-1234-123456789ac8', 'admin', 'admin', now(), now(),
        'TM1.0: out 9', 8, '12345678-1234-1234-1234-123456789abd', '12345678-1234-1234-1234-123456789abe');
