--------------------------------------------------------
--  File creato - mercoled�-ottobre-13-2021   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table PERSON
--------------------------------------------------------

  CREATE TABLE "ROOT"."PERSON" 
   (	"PERSON_ID" NUMBER(19,0), 
	"FIRST_NAME" VARCHAR2(50 BYTE), 
	"LAST_NAME" VARCHAR2(50 BYTE), 
	"EMAIL" VARCHAR2(50 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
REM INSERTING into ROOT.PERSON
SET DEFINE OFF;
Insert into ROOT.PERSON (PERSON_ID,FIRST_NAME,LAST_NAME,EMAIL) values ('0','Mario','Rossi','mario.rossi@test.it');
Insert into ROOT.PERSON (PERSON_ID,FIRST_NAME,LAST_NAME,EMAIL) values ('1','Luca','Gialli','luca.gialli@test.it');
Insert into ROOT.PERSON (PERSON_ID,FIRST_NAME,LAST_NAME,EMAIL) values ('2','paul','Bianchi','paul.bianco@test.it');
Insert into ROOT.PERSON (PERSON_ID,FIRST_NAME,LAST_NAME,EMAIL) values ('4','Marco','Caccia','marco.caccia@test.it');
Insert into ROOT.PERSON (PERSON_ID,FIRST_NAME,LAST_NAME,EMAIL) values ('5','Fabio','Nollo','fabio.nollo@test.it');
Insert into ROOT.PERSON (PERSON_ID,FIRST_NAME,LAST_NAME,EMAIL) values ('6','Mario','Cacu','mario.cacu@test.it');
--------------------------------------------------------
--  DDL for Index SYS_C007006
--------------------------------------------------------

  CREATE UNIQUE INDEX "ROOT"."SYS_C007006" ON "ROOT"."PERSON" ("PERSON_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  Constraints for Table PERSON
--------------------------------------------------------

  ALTER TABLE "ROOT"."PERSON" ADD PRIMARY KEY ("PERSON_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM"  ENABLE;
  ALTER TABLE "ROOT"."PERSON" MODIFY ("EMAIL" NOT NULL ENABLE);
  ALTER TABLE "ROOT"."PERSON" MODIFY ("LAST_NAME" NOT NULL ENABLE);
  ALTER TABLE "ROOT"."PERSON" MODIFY ("FIRST_NAME" NOT NULL ENABLE);
  ALTER TABLE "ROOT"."PERSON" MODIFY ("PERSON_ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Procedure 
--------------------------------------------------------

create or replace PROCEDURE SAVE_NEW_PERSON(
    myfirstname VARCHAR2,
    mylastname VARCHAR2,
    myemail VARCHAR2
)
AS
    BEGIN
        INSERT INTO PERSON (PERSON_ID, FIRST_NAME, LAST_NAME, EMAIL)
        VALUES (PERSON_ID_SEQ.nextval, myfirstname, mylastname, myemail);
END;



create or replace PROCEDURE UPDATE_PERSON(
    myid VARCHAR2,
    myfirstname VARCHAR2,
    mylastname VARCHAR2,
    myemail VARCHAR2
)
AS
    BEGIN
        UPDATE PERSON p 
        SET p.first_name = myfirstname, p.last_name = mylastname, p.email = myemail 
        WHERE p.person_id = myid;  
END;


create or replace PROCEDURE DELETE_PERSON(
    myemail VARCHAR2
)
AS
    BEGIN
        DELETE PERSON p 
        WHERE p.email = myemail;  
END;