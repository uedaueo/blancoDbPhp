create table blanco (
	id integer auto_increment, 
	name varchar(60), 
	age integer,
	email varchar(255),
  point DECIMAL(10,2),
  updated TIMESTAMP,
	PRIMARY KEY (id)
);

insert into blanco (name, age, email, point) VALUES
  ("TATSUYA UEDA", 51, "tueda@collegium.or.jp", 10.50),
  ("UEDA Ueo", 50, "ueo@ueo.co.jp", 9.99);
