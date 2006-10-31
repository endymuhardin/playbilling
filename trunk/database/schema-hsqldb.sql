DROP TABLE billing_session IF EXISTS;
DROP TABLE billing_member IF EXISTS;
DROP TABLE billing_simple_tariff IF EXISTS;
DROP TABLE billing_sequencer IF EXISTS;


CREATE TABLE billing_sequencer (
			id INTEGER IDENTITY, 
			name VARCHAR(200), 
			next_value INTEGER, 
			UNIQUE(name)
);


CREATE TABLE billing_simple_tariff (
			id INTEGER PRIMARY KEY, 
			name VARCHAR(200), 
			rate DECIMAL, 
			charge_unit INTEGER, 
			time_unit VARCHAR, 
			grace_period INTEGER, 
			grace_period_charge DECIMAL, 
			UNIQUE(name)
);


CREATE TABLE billing_member (
			id INTEGER PRIMARY KEY, 
			username VARCHAR, 
			password VARCHAR, 
			fullname VARCHAR, 
			email VARCHAR, 
			address VARCHAR, 
			tariff_id INTEGER, 
			autoconnect BOOLEAN,
			credit DECIMAL,
			session_expiration_limit BIGINT,
			member_status VARCHAR,
			member_type VARCHAR, 
			register_date DATETIME, 
			expire_date DATETIME,
			UNIQUE(username), 
			FOREIGN KEY (tariff_id) REFERENCES billing_simple_tariff(id)
);


CREATE TABLE billing_session (
			id INTEGER IDENTITY, 
			member_id INTEGER, 
			ip_address VARCHAR, 
			start_time DATETIME, 
			end_time DATETIME, 
			last_visit DATETIME, 
			expiration_limit INTEGER, 
			charge DECIMAL, 
			session_status VARCHAR,
			FOREIGN KEY (member_id) REFERENCES billing_member(id)
);