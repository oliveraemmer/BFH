INSERT INTO person (firstname, lastname, address, sex, birthdate, username, password, role)
VALUES ('Isaac','Instructor', 'Rootstreet 2, 3007 Bern', 'M', to_date('13/03/1965','DD/MM/YYYY'), 'isaac', 'f468a3bbd9b6232589b4622a8461d56ab1828e145d892fbcf626459e1db8defe231d57ae7bb620f2afde6ab47fa90bf8325e72b95399875e0cd15e2ab1ff1bc5','teacher');
INSERT INTO person (firstname, lastname, address, sex, birthdate, username, password, role)
VALUES ('Thomas','Teacher', 'Bildungstrasse 55, 3616 Oberlangenegg', 'M', to_date('13/03/1965','DD/MM/YYYY'), 'thomas', 'cad618d77e1c7e64f9069e67fd2782cc040a33559e4cd948efb81cf94630e106eb754fce1a7720d1da29dd6b5df2603ad8c7c4df3dd89077ef7e0d7b09c50109','teacher');
INSERT INTO person (firstname, lastname, address, sex, birthdate, username, password, role)
VALUES ('Sandro','Student', 'Hoffnungslosweg 22, 3625 Heiligenschwendi', 'M', to_date('12/10/1999','DD/MM/YYYY'), 'sandro', '66330f63d544886f983e2139f22ae29c50e44003fbd02f2317f2dcfda7441bd511c16d7ab79319a1f1313d76e0f8297396b1ed0dc9344330b4b133778b7b7b97','student');
INSERT INTO person (firstname, lastname, address, sex, birthdate, username, password, role)
VALUES ('Philipp','Professor', 'Bachmattstrasse 142, 3628 Kienersrütti', 'M', to_date('01/08/1931','DD/MM/YYYY'), 'philipp', 'a7141723944297a7922dc5c2340e6413d8f86724fcc84fac853b1dc8aa56826ab89e91b108be6850fdbd7d1da290dab59dcf36efbded48409d41b209ec207611','teacher');
INSERT INTO person (firstname, lastname, address, sex, birthdate, username, password, role)
VALUES ('Sebastian','Schueler', 'Lindenstrasse 5, 3176 Neuenegg', 'M', to_date('15/12/2002','DD/MM/YYYY'), 'sebastian', '5d8b9691f431fd819900013721c64999ea0b8717b386e38aa4c9c0a931590507b32b9fa8b029a013ac4def554806055d1009bac3874d6df69d4016a82cea5d98','student');


INSERT INTO module (mid, name, description, pid)
VALUES ('BTI1001', 'Programming with Java 1', 'Java Essentials',2);
INSERT INTO module (mid, name, description, pid)
VALUES ('BTI1011', 'Programming with Java 2', 'Java Advanced',2);
INSERT INTO module (mid, name, description, pid)
VALUES ('BTI1121', 'Software Engineering', 'Agile Project Management',4);
INSERT INTO module (mid, name, description, pid)
VALUES ('BTI1301', 'Web Programming', 'JAVASCRIPT',4);
INSERT INTO module (mid, name, description, pid)
VALUES ('BTI1311', 'Databases', 'Relational Databases with SQL',2);
INSERT INTO module (mid, name, description, pid)
VALUES ('BTI1222', 'TestModule', 'Module for unit Tests',2);

INSERT INTO run (mid, year, semester)
VALUES ('BTI1001', 2022, 'FS');
INSERT INTO run (mid, year, semester)
VALUES ('BTI1011', 2022, 'HS');
INSERT INTO run (mid, year, semester)
VALUES ('BTI1121', 2021, 'FS');
INSERT INTO run (mid, year, semester)
VALUES ('BTI1121', 2022, 'HS');
INSERT INTO run (mid, year, semester)
VALUES ('BTI1301', 2022, 'FS');
INSERT INTO run (mid, year, semester)
VALUES ('BTI1311', 2022, 'HS');


INSERT INTO teacher_run (pid, mrid)
VALUES (2,1);
INSERT INTO teacher_run (pid, mrid)
VALUES (4,1);
INSERT INTO teacher_run (pid, mrid)
VALUES (2,2);
INSERT INTO teacher_run (pid, mrid)
VALUES (2,3);
INSERT INTO teacher_run (pid, mrid)
VALUES (4,4);
INSERT INTO teacher_run (pid, mrid)
VALUES (2,5);


INSERT INTO enroll (mrid, pid)
VALUES (1,3);
INSERT INTO enroll (mrid, pid)
VALUES (2,3);
INSERT INTO enroll (mrid, pid)
VALUES (3,3);
INSERT INTO enroll (mrid, pid, grade)
VALUES (4,3,'a');
INSERT INTO enroll (mrid, pid)
VALUES (5,3);
INSERT INTO enroll (mrid, pid)
VALUES (3,5);
INSERT INTO enroll (mrid, pid, grade)
VALUES (4,5,'f');
INSERT INTO enroll (mrid, pid)
VALUES (5,5);