# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table sp_market (
  trade_date                    varchar(255) not null,
  symbol                        varchar(255),
  open                          float,
  end                           float,
  increase                      float,
  increase_per                  float,
  low                           float,
  high                          float,
  volume                        bigint,
  amount                        bigint,
  constraint pk_sp_market primary key (trade_date)
);


# --- !Downs

drop table if exists sp_market;

