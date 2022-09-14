create table secrets(
   secret_id INT NOT NULL AUTO_INCREMENT,
   secret_domain VARCHAR(100) NOT NULL,
   secret_user VARCHAR(100) NOT NULL,
   secret_value VARCHAR(200) NOT NULL,
   iv VARCHAR(120) NOT NULL,
   submission_date DATE,
   who VARCHAR(25),
   PRIMARY KEY ( secret_id )
);

CREATE TRIGGER secrets_audit BEFORE INSERT ON secrets
FOR EACH ROW
SET
    NEW.submission_date = NOW(),
    NEW.who = USER();





