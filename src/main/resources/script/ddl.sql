create table if not exists `delivery_state`
(
    `delivery_state_code` char(2) primary key comment '배송 상태 코드',
    `name`                varchar(20) not null comment '이름'
) comment '배송 상태';

create table if not exists `delivery_driver`
(
    `delivery_driver_id` bigint primary key auto_increment comment '배송 기사 식별번호',
    `name`               varchar(20) not null comment '기사 이름'
) comment '배송 기사';

create table if not exists `delivery_info`
(
    `delivery_info_id`   bigint primary key auto_increment comment '배송 정보 식별번호',
    `delivery_driver_id` bigint       null comment '배송 기사 식별번호',
    `delivery_address`   varchar(255) not null comment '배송 주소',
    `price`              int unsigned not null comment '배달료',
    `req_memo`           varchar(255) null comment '요청 메모',
    `req_store`          varchar(255) not null comment '요청사',
    foreign key (delivery_driver_id) references delivery_driver (delivery_driver_id)
) comment '배송 정보';

create table if not exists `delivery_state_history`
(
    `delivery_state_history_id` bigint primary key auto_increment comment '배송 상태 변경 이력 식별번호',
    `delivery_state_code`       char(2)                not null comment '배송 상태 코드',
    `delivery_info_id`          bigint                 not null comment '배송 정보 식별번호',
    `create_datetime`           datetime default now() not null comment '생성일시',
    foreign key (delivery_state_code) references delivery_state (delivery_state_code),
    foreign key (delivery_info_id) references delivery_info (delivery_info_id)
) comment '배송 상태 변경 이력';