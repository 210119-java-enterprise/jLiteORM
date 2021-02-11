create table app_users (
	user_id		serial,
	username	varchar unique not null,
	password	varchar	not null,
	first_name	varchar not null,
	last_name	varchar not null,


	constraint app_users_pk
	primary key (user_id)

);

create table checking_account (

    checking_id     serial,
    balance         float not null,


    constraint  checking_account_pk
    primary key (checking_id)


);

create table user_checking_account (

    checking_id		int not null,
    user_id         int not null,

    constraint user_checking_acc_pk
    primary key (checking_id, user_id),

    constraint  checking_account_fk
    foreign key (checking_id)
    references checking_account,

    constraint app_user_fk
    foreign key (user_id)
    references app_users

);



commit;