-- ******************************************************
-- 
--                    SECUENCIADORES
-- 
-- ******************************************************

CREATE SEQUENCE "USUARIO_SEQ"
	MINVALUE 1
	MAXVALUE 999999999
	INCREMENT BY 1
	START WITH 1
	NOCACHE
	NOCYCLE;
	
-- ******************************************************
-- 
--                  SPRING SECURITY 
-- 
-- ******************************************************

CREATE TABLE ROLES (
    ID 					      BIGINT			    		,
    NAME                      VARCHAR(50) 			NOT NULL,
    
    PRIMARY KEY (ID)
);

CREATE TABLE USERS (
    ID 						  BIGINT		        	   		   		,
    USERNAME 				  VARCHAR(50) 		UNIQUE	   		NOT NULL,
    PASSWORD 				  VARCHAR(100) 			   	   		NOT NULL,
    EMAIL 					  VARCHAR(150) 	    UNIQUE	   		NOT NULL,
    TELEFONO				  VARCHAR(50)					        	,
    NAME					  VARCHAR(50)				   		NOT NULL,
    FIRST_NAME 				  VARCHAR(50) 				   		NOT NULL,
    LAST_NAME 				  VARCHAR(50) 				  		NOT NULL,
    FECHA_REGISTRO			  DATE							    NOT NULL,
    ULTIMO_LOGIN			  TIMESTAMP									,
    ENABLED 				  BOOLEAN		 				 	NOT NULL,
    
    PRIMARY KEY (ID)
);

CREATE TABLE USER_ROLES (
    ID_USER                   BIGINT              NOT NULL,
    ID_ROL                    BIGINT              NOT NULL,
    
    PRIMARY KEY (ID_USER, ID_ROL)						  ,
    
    FOREIGN KEY (ID_USER) REFERENCES USERS (ID) 		  ,
    FOREIGN KEY (ID_ROL)  REFERENCES ROLES (ID)
);
	
-- ******************************************************
-- 
--                  	   MODEL 
-- 
-- ******************************************************

CREATE TABLE CATEGORIAS (
	ID					BIGINT							,
	NOMBRE				VARCHAR(50)				NOT NULL,
	DESCRIPCION 		VARCHAR(150)					,
	IMG_URL				VARCHAR(255)					,
	
	PRIMARY KEY (ID)
	
);
	
CREATE TABLE PRODUCTOS (
	ID					BIGINT								,
	NOMBRE				VARCHAR(50)					NOT NULL,
	DESCRIPCION			VARCHAR(150)						,
  	PRECIO				DOUBLE						NOT NULL,
  	ID_CATEGORIA		BIGINT								,
  	DESCATALOGADO		BOOLEAN		  				NOT NULL,
  	IMG_URL				VARCHAR(255)						,
  	FECHA_ALTA			TIMESTAMP	 						,

  	FOREIGN KEY (ID_CATEGORIA) REFERENCES CATEGORIAS (ID)   ,
  	PRIMARY KEY(ID)
  	
);

CREATE TABLE CLIENTES (
	ID				   BIGINT					NOT NULL,
	ID_USUARIO		   BIGINT					NOT NULL,
	NOMBRE			   VARCHAR(50)				NOT NULL,
	APELLIDO1		   VARCHAR(50)						,
	APELLIDO2		   VARCHAR(50)						,
	TELEFONO		   VARCHAR(15)				NOT NULL,
	EMAIL	 		   VARCHAR(50)						,
	COD_POSTAL		   VARCHAR(10)						,
	PROVINCIA		   VARCHAR(25)				NOT NULL,						
	CALLE			   VARCHAR(50)						,
	BLOQUE			   VARCHAR(5)						,
	PORTAL			   VARCHAR(5)						,
		
	PRIMARY KEY (ID)									,
	FOREIGN KEY (ID_USUARIO) REFERENCES USERS (ID)
	
);

CREATE TABLE COMPRA (

);

