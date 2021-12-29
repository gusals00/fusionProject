drop schema `enrolment`;
create schema `enrolment`;
use enrolment;
CREATE TABLE `enrolment`.`admin` (
  `admin_id` VARCHAR(20) NOT NULL,
  `admin_pw` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`admin_id`));

-- DEPARTMENT 테이블
CREATE TABLE `enrolment`.`department` (
  `department_number` INT NOT NULL,
  `department_name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`department_number`));

-- PROFESSOR 테이블
CREATE TABLE `enrolment`.`professor` (
  `professor_id` VARCHAR(20) NOT NULL,
  `professor_pw` VARCHAR(20) NOT NULL,
  `professor_name` VARCHAR(20) NOT NULL,
  `SSN` VARCHAR(14) NOT NULL,
  `e_mail` VARCHAR(30) NOT NULL,
  `professor_phone_number` VARCHAR(15) NOT NULL,
  `department_number` INT NULL,
  PRIMARY KEY (`professor_id`),
  CONSTRAINT `professor_department_number`
    FOREIGN KEY (`department_number`)
    REFERENCES `enrolment`.`department` (`department_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- STUDENT 테이블
CREATE TABLE `enrolment`.`student` (
  `student_id` VARCHAR(20) NOT NULL,
  `student_pw` VARCHAR(20) NOT NULL,
  `student_name` VARCHAR(20) NOT NULL,
  `ssn` VARCHAR(14) NOT NULL,
  `student_level` INT NOT NULL,
  `department_number` INT NOT NULL,
  PRIMARY KEY (`student_id`),
  CONSTRAINT `student_department_number`
    FOREIGN KEY (`department_number`)
    REFERENCES `enrolment`.`department` (`department_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- PERIOD 테이블
CREATE TABLE `enrolment`.`period` (
  `period_id` VARCHAR(20) NOT NULL,
  `period_name` VARCHAR(15) NOT NULL,
  `open_time` DATETIME NOT NULL,
  `close_time` DATETIME NOT NULL,
  PRIMARY KEY (`period_id`));

-- LECTURE 테이블
CREATE TABLE `enrolment`.`lecture` (
  `lecture_code` VARCHAR(10) NOT NULL,
  `lecture_name` VARCHAR(20) NOT NULL,
  `lecture_level` INT NOT NULL,
  `lecture_credit` INT NOT NULL,
  PRIMARY KEY (`lecture_code`));

-- OPEN_LECTURE 테이블
CREATE TABLE `enrolment`.`open_lecture` (
  `open_lecture_id` INT NOT NULL AUTO_INCREMENT,
  `seperated_number` INT NOT NULL,
  `lecture_code` VARCHAR(10) NOT NULL,
  `professor_id` VARCHAR(20) NOT NULL,
  `max_student_number` INT NOT NULL,
  `cur_student_number` INT NOT NULL,
  PRIMARY KEY (`open_lecture_id`),
  CONSTRAINT `lecture_code`
    FOREIGN KEY (`lecture_code`)
    REFERENCES `enrolment`.`lecture` (`lecture_code`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `professor_id`
    FOREIGN KEY (`professor_id`)
    REFERENCES `enrolment`.`professor` (`professor_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- LECTURE_ROOM 테이블
CREATE TABLE `enrolment`.`lecture_room` (
  `lecture_room_id` INT NOT NULL AUTO_INCREMENT,
  `building_name` VARCHAR(10) NOT NULL,
  `lectureroom_number` INT NOT NULL,
  PRIMARY KEY (`lecture_room_id`));

-- LECTURE_TIME 테이블
CREATE TABLE `enrolment`.`lecture_time` (
  `lecturetime_id` INT NOT NULL AUTO_INCREMENT,
  `lecture_day` VARCHAR(10) NOT NULL,
  `lecture_period` INT NOT NULL,
  PRIMARY KEY (`lecturetime_id`));

-- SYLLABUS 테이블
CREATE TABLE `enrolment`.`syllabus` (
  `syllabus_id` INT NOT NULL AUTO_INCREMENT,
  `bookname` VARCHAR(30) NULL,
  `lecture_goal` VARCHAR(30) NULL,
  `open_lecture_id` INT NOT NULL,
  PRIMARY KEY (`syllabus_id`),
  CONSTRAINT `syllabus_open_lecture_id`
    FOREIGN KEY (`open_lecture_id`)
    REFERENCES `enrolment`.`open_lecture` (`open_lecture_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- SYLLABUS_WEEK_INFO 테이블
CREATE TABLE `enrolment`.`syllabus_week_info` (
  `syllabus_info_id` INT NOT NULL AUTO_INCREMENT,
  `syllabus_week` INT NOT NULL,
  `syllabus_subject` VARCHAR(20) NOT NULL,
  `syllabus_content` VARCHAR(50) NOT NULL,
  `syllabus_assignment` VARCHAR(30) NULL,
  `syllabus_evaluation` VARCHAR(30) NULL,
  `syllabus_id` INT NOT NULL,
  PRIMARY KEY (`syllabus_info_id`),
  CONSTRAINT `syllabus_id`
    FOREIGN KEY (`syllabus_id`)
    REFERENCES `enrolment`.`syllabus` (`syllabus_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

-- LECTURE_HISTORY 테이블
CREATE TABLE `enrolment`.`lecture_history` (
  `lecture_register_id` INT NOT NULL AUTO_INCREMENT,
  `student_id` VARCHAR(20) NOT NULL,
  `open_lecture_id` INT NOT NULL,
  PRIMARY KEY (`lecture_register_id`),
  CONSTRAINT `student_id`
    FOREIGN KEY (`student_id`)
    REFERENCES `enrolment`.`student` (`student_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `open_lecture_id`
    FOREIGN KEY (`open_lecture_id`)
    REFERENCES `enrolment`.`open_lecture` (`open_lecture_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

-- LECTURE_ROOM_BY_TIME 테이블
CREATE TABLE `enrolment`.`lecture_room_by_time` (
  `using_lecture_room_id` INT NOT NULL AUTO_INCREMENT,
  `lecture_room_id` INT NOT NULL,
  `lecturetime_id` INT NOT NULL,
  `open_lecture_id` INT NOT NULL,
  PRIMARY KEY (`using_lecture_room_id`),
  CONSTRAINT `lecture_id_room_by_time`
    FOREIGN KEY (`lecture_room_id`)
    REFERENCES `enrolment`.`lecture_room` (`lecture_room_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `lecturetime_id_room_by_time`
    FOREIGN KEY (`lecturetime_id`)
    REFERENCES `enrolment`.`lecture_time` (`lecturetime_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `open_lecture_id_room_by_time`
    FOREIGN KEY (`open_lecture_id`)
    REFERENCES `enrolment`.`open_lecture` (`open_lecture_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('월', '1');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('월', '2');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('월', '3');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('월', '4');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('월', '5');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('월', '6');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('월', '7');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('화', '1');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('화', '2');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('화', '3');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('화', '4');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('화', '5');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('화', '6');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('화', '7');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('수', '1');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('수', '2');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('수', '3');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('수', '4');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('수', '5');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('수', '6');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('수', '7');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('목', '1');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('목', '2');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('목', '3');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('목', '4');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('목', '5');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('목', '6');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('목', '7');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('금', '1');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('금', '2');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('금', '3');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('금', '4');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('금', '5');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('금', '6');
INSERT INTO `enrolment`.`lecture_time` (`lecture_day`, `lecture_period`) VALUES ('금', '7');

insert into period values(1,"1학년 수강신청 기간",date_sub(NOW(),INTERVAL 10 day),date_sub(NOW(),INTERVAL 5 day));
insert into period values(2,"2학년 수강신청 기간",date_sub(NOW(),INTERVAL 10 day),date_sub(NOW(),INTERVAL 5 day));
insert into period values(3,"3학년 수강신청 기간",date_sub(NOW(),INTERVAL 10 day),date_sub(NOW(),INTERVAL 5 day));
insert into period values(4,"4학년 수강신청 기간",date_sub(NOW(),INTERVAL 10 day),date_sub(NOW(),INTERVAL 5 day));
insert into period values(5,"전체학년 수강신청 기간",date_sub(NOW(),INTERVAL 10 day),date_sub(NOW(),INTERVAL 5 day));
insert into period values(6,"강의 계획서 작성 기간",date_sub(NOW(),INTERVAL 10 day),date_sub(NOW(),INTERVAL 5 day));
