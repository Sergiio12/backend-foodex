/* 
 * ROLES
 */

INSERT INTO ROLES (ID, NAME) VALUES 
(1, 'USUARIO'),
(2, 'ADMIN');

/* 
 * USUARIOS
 */

INSERT INTO USERS (ID, USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, NAME, ENABLED, FECHA_REGISTRO) VALUES
(NEXT VALUE FOR GENERAL_SEQ, 'sasensior', '$2a$10$yh4xQHbb8nS/8VIp0zCr8.nWnX1lw/OTo8o4fTVGzss7Lyewt3SoW', 'sasensior03@educarex.es', 'Asensio', 'Rodríguez', 'Sergio', true, '2025-02-19'),
(NEXT VALUE FOR GENERAL_SEQ, 'jlopez', '$2a$10$abcd1234abcd1234abcd12abcd1234abcd1234abcd1234abcd12', 'jlopez@example.com', 'Juan', 'López', 'Juan López', true, '2025-01-15'),
(NEXT VALUE FOR GENERAL_SEQ, 'mgarcia', '$2a$10$abcd5678abcd5678abcd56abcd5678abcd5678abcd5678abcd56', 'mgarcia@example.com', 'María', 'García', 'María García', true, '2025-02-01'),
(NEXT VALUE FOR GENERAL_SEQ, 'cmartinez', '$2a$10$xyz98765xyz98765xyz98xyz98765xyz98765xyz98765xyz98', 'cmartinez@example.com', 'Carlos', 'Martínez', 'Carlos Martínez', false, '2025-02-10'),
(NEXT VALUE FOR GENERAL_SEQ, 'lfernandez', '$2a$10$mnw65432mnw65432mnw65mnw65432mnw65432mnw65432mnw65', 'lfernandez@example.com', 'Laura', 'Fernández', 'Laura Fernández', true, '2025-02-15'),
(NEXT VALUE FOR GENERAL_SEQ, 'psanchez', '$2a$10$qwe4321qwe4321qwe432qwe4321qwe4321qwe4321qwe432', 'psanchez@example.com', 'Pedro', 'Sánchez', 'Pedro Sánchez', true, '2025-02-18'),
(NEXT VALUE FOR GENERAL_SEQ, 'adominguez', '$2a$10$rty5678rty5678rty567rty5678rty5678rty5678rty567', 'adominguez@example.com', 'Ana', 'Domínguez', 'Ana Domínguez', false, '2025-01-28'),
(NEXT VALUE FOR GENERAL_SEQ, 'ldiaz', '$2a$10$zxc9876zxc9876zxc987zxc9876zxc9876zxc9876zxc987', 'ldiaz@example.com', 'Luis', 'Díaz', 'Luis Díaz', true, '2025-02-05'),
(NEXT VALUE FOR GENERAL_SEQ, 'scastro', '$2a$10$vbn1234vbn1234vbn123vbn1234vbn1234vbn1234vbn123', 'scastro@example.com', 'Sofía', 'Castro', 'Sofía Castro', true, '2025-02-12'),
(NEXT VALUE FOR GENERAL_SEQ, 'mortega', '$2a$10$asd4567asd4567asd456asd4567asd4567asd4567asd456', 'mortega@example.com', 'Manuel', 'Ortega', 'Manuel Ortega', false, '2025-02-14'),
(NEXT VALUE FOR GENERAL_SEQ, 'ejimenez', '$2a$10$fgh6789fgh6789fgh678fgh6789fgh6789fgh6789fgh678', 'ejimenez@example.com', 'Elena', 'Jiménez', 'Elena Jiménez', true, '2025-02-19');

INSERT INTO USER_ROLES (ID_USER, ID_ROL) VALUES
(1, 2),
(1, 1);

/* 
 * CLIENTES
 */

INSERT INTO CLIENTES (ID, NOMBRE, APELLIDO1, APELLIDO2, TELEFONO, EMAIL, COD_POSTAL, PROVINCIA, CALLE, BLOQUE, PORTAL) 
VALUES 
(1, 'Juan', 'Pérez', 'García', '600123456', 'juan.perez@example.com', '28001', 'Madrid', 'Gran Vía', 'A', '1'),
(2, 'María', 'López', 'Fernández', '611987654', 'maria.lopez@example.com', '41010', 'Sevilla', 'Avda. Constitución', 'B', '2'),
(3, 'Carlos', 'Sánchez', NULL, '622456789', 'carlos.sanchez@example.com', '08012', 'Barcelona', 'Passeig de Gràcia', 'C', '3'),
(4, 'Ana', 'Gómez', 'Ruiz', '633789123', NULL, '29015', 'Málaga', 'Calle Larios', 'D', '4'),
(5, 'Luis', 'Martín', 'Torres', '644321987', 'luis.martin@example.com', '03001', 'Alicante', 'Rambla Méndez Núñez', 'E', '5'),
(6, 'Laura', 'Díaz', NULL, '655987321', 'laura.diaz@example.com', '46002', 'Valencia', 'Calle Colón', 'F', '6'),
(7, 'Pedro', 'Hernández', 'Moreno', '666123789', 'pedro.hernandez@example.com', '15003', 'A Coruña', 'Ronda de Nelle', 'G', '7'),
(8, 'Elena', 'Alonso', NULL, '677456987', 'elena.alonso@example.com', '50004', 'Zaragoza', 'Paseo Independencia', 'H', '8'),
(9, 'Miguel', 'Romero', 'Serrano', '688789654', 'miguel.romero@example.com', '20006', 'San Sebastián', 'Calle Mayor', 'I', '9'),
(10, 'Carmen', 'Navarro', 'Iglesias', '699321654', 'carmen.navarro@example.com', '35007', 'Las Palmas', 'Avenida Mesa y López', 'J', '10');

/*
* CATEGORÍAS
*/

INSERT INTO CATEGORIAS (ID, NOMBRE, DESCRIPCION, IMG_URL) VALUES
(1, 'Frutas', 'Frutas frescas y naturales', 'img/frutas.jpg'),
(2, 'Lácteos', 'Productos derivados de la leche', 'img/lacteos.jpg'),
(3, 'Carnes', 'Carnes frescas de gran calidad', 'img/carnes.jpg'),
(4, 'Bebidas', 'Refrescos y bebidas variadas', 'img/bebidas.jpg');

/*
* PRODUCTOS
*/

INSERT INTO PRODUCTOS (ID, ID_CATEGORIA, NOMBRE, DESCRIPCION, PRECIO, STOCK, DESCATALOGADO, IMG_URL, FECHA_ALTA) VALUES
(1, 1, 'Manzana', 'Manzana roja deliciosa', 1.20, 100, false, 'img/manzana.jpg', CURRENT_TIMESTAMP),
(2, 1, 'Plátano', 'Plátano maduro y dulce', 0.80, 120, false, 'img/platano.jpg', CURRENT_TIMESTAMP),
(3, 2, 'Leche', 'Leche entera pasteurizada', 1.50, 80, false, 'img/leche.jpg', CURRENT_TIMESTAMP),
(4, 2, 'Queso', 'Queso curado de oveja', 3.75, 50, false, 'img/queso.jpg', CURRENT_TIMESTAMP),
(5, 3, 'Pollo', 'Pechuga de pollo fresca', 6.40, 60, false, 'img/pollo.jpg', CURRENT_TIMESTAMP),
(6, 3, 'Ternera', 'Filete de ternera', 12.99, 40, false, 'img/ternera.jpg', CURRENT_TIMESTAMP),
(7, 4, 'Coca Cola', 'Refresco de cola 2L', 2.20, 150, false, 'img/coca_cola.jpg', CURRENT_TIMESTAMP),
(8, 4, 'Agua Mineral', 'Botella de agua 1.5L', 1.00, 200, false, 'img/agua.jpg', CURRENT_TIMESTAMP);

/* 
 * CARRITOS DE COMPRA
 */

INSERT INTO CARRITO_COMPRA (ID, ID_USUARIO) VALUES
--(1, 1),
(2, 2),
(3, 3);

/* 
 * ITEMS DE CARRITO
 */

INSERT INTO ITEMS_CARRITO (ID_CARRITO, ID_PRODUCTO, CANTIDAD) VALUES
(1, 1, 2), -- Usuario 1 tiene Manzana en su carrito
(1, 3, 3), -- Usuario 1 tiene Leche en su carrito
(2, 5, 4), -- Usuario 2 tiene Pollo en su carrito
(2, 7, 5), -- Usuario 2 tiene Coca Cola en su carrito
(3, 2, 6), -- Usuario 3 tiene Plátano en su carrito
(3, 6, 4); -- Usuario 3 tiene Ternera en su carrito

