-- Drop tables in correct order (child tables first)
DROP TABLE IF EXISTS password_reset_token;
DROP TABLE IF EXISTS secret;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    id int NOT NULL AUTO_INCREMENT,
    first_name varchar(30) NOT NULL,
    last_name varchar(30) NOT NULL,
    email varchar(30) NOT NULL,
    password longtext NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Initial user data
INSERT INTO `user` (`first_name`, `last_name`, `email`, `password`) VALUES
('Hans', 'Muster', 'hans.muster@bbw.ch', 'abcd'),
('Paula', 'Kuster', 'paula.kuster@bbw.ch', 'efgh'),
('Andrea', 'Oester', 'andrea.oester@bbw.ch', 'ijkl');

-- Table: secret
CREATE TABLE secret (
    id int NOT NULL AUTO_INCREMENT,
    user_id int NOT NULL,
    content LONGTEXT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Table: Password Reset Token
-- Used to store the token for password reset if requested by user
CREATE TABLE password_reset_token (
    token VARCHAR(255) PRIMARY KEY,
    user_id int NOT NULL,
    expiration TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Initial secret data
INSERT INTO secret (`user_id`, `content`) VALUES
    (1, '{"kindid":1,"kind":"credential","userName":"muster","password":"1234","url":"www.bbw.ch"}'),
    (1, '{"kindid":2,"kind":"creditcard","cardtype":"Visa","cardnumber":"4242 4242 4242 4241","expiration":"12/27","cvv":"789"}'),
    (1, '{"kindid":3,"kind":"note","title":"Eragon","content":"Und Eragon ging auf den Drachen zu."}');