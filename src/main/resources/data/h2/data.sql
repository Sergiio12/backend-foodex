

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
* CATEGORÍAS
*/


INSERT INTO CATEGORIAS (ID, NOMBRE, DESCRIPCION, IMG_URL) VALUES
(1, 'Frutas', 'Frutas frescas y naturales', 'img/frutas.jpg'),
(2, 'Lácteos', 'Productos derivados de la leche', 'img/lacteos.jpg'),
(3, 'Carnes', 'Carnes frescas de gran calidad', 'img/carnes.jpg'),
(4, 'Bebidas', 'Refrescos y bebidas variadas', 'img/bebidas.jpg'),
(5, 'Verduras', 'Verduras frescas y orgánicas', 'img/verduras.jpg'),
(6, 'Congelados', 'Productos congelados de todo tipo', 'img/congelados.jpg'),
(7, 'Panadería', 'Panes y productos de pastelería', 'img/panaderia.jpg'),
(8, 'Snacks', 'Comida rápida y snacks variados', 'img/snacks.jpg');


/*
* PRODUCTOS
*/


INSERT INTO PRODUCTOS (ID, ID_CATEGORIA, NOMBRE, DESCRIPCION, PRECIO, STOCK, DESCATALOGADO, IMG_URL, FECHA_ALTA) VALUES
(1, 1, 'Manzana Verde', 'Manzana verde crujiente', 1.40, 95, false, 'img/manzana_verde.jpg', CURRENT_TIMESTAMP),
(2, 1, 'Mandarina', 'Mandarina dulce y jugosa', 1.50, 110, false, 'img/mandarina.jpg', CURRENT_TIMESTAMP),
(3, 1, 'Cereza', 'Cereza roja fresca', 3.20, 70, false, 'img/cereza.jpg', CURRENT_TIMESTAMP),
(4, 1, 'Melocotón', 'Melocotón maduro y suave', 2.30, 85, false, 'img/melocoton.jpg', CURRENT_TIMESTAMP),
(5, 1, 'Papaya', 'Papaya tropical y dulce', 3.50, 60, false, 'img/papaya.jpg', CURRENT_TIMESTAMP),
(6, 1, 'Aguacate', 'Aguacate maduro y cremoso', 2.80, 80, false, 'img/aguacate.jpg', CURRENT_TIMESTAMP),
(7, 1, 'Arándano', 'Arándano fresco y antioxidante', 4.00, 50, false, 'img/arandano.jpg', CURRENT_TIMESTAMP),
(8, 1, 'Granada', 'Granada con granos rojos', 2.60, 65, false, 'img/granada.jpg', CURRENT_TIMESTAMP),
(9, 2, 'Leche Desnatada', 'Leche desnatada baja en grasa', 1.60, 90, false, 'img/leche_desnatada.jpg', CURRENT_TIMESTAMP),
(10, 2, 'Yogur Griego', 'Yogur griego natural', 2.20, 75, false, 'img/yogur_griego.jpg', CURRENT_TIMESTAMP),
(11, 2, 'Cuajada', 'Cuajada de leche fresca', 1.80, 60, false, 'img/cuajada.jpg', CURRENT_TIMESTAMP),
(12, 2, 'Leche de Almendra', 'Leche de almendra sin azúcar', 2.50, 50, false, 'img/leche_almendra.jpg', CURRENT_TIMESTAMP),
(13, 2, 'Crema Pastelera', 'Crema pastelera para repostería', 2.00, 65, false, 'img/crema_pastelera.jpg', CURRENT_TIMESTAMP),
(14, 2, 'Leche de Coco', 'Leche de coco sin conservantes', 3.10, 40, false, 'img/leche_coco.jpg', CURRENT_TIMESTAMP),
(15, 2, 'Ricotta', 'Ricotta fresca de vaca', 3.80, 50, false, 'img/ricotta.jpg', CURRENT_TIMESTAMP),
(16, 2, 'Manteca de Cerdo', 'Manteca de cerdo para cocinar', 2.10, 80, false, 'img/manteca_cerdo.jpg', CURRENT_TIMESTAMP),
(17, 3, 'Costillas de Cerdo', 'Costillas de cerdo a la barbacoa', 10.50, 55, false, 'img/costillas_cerdo.jpg', CURRENT_TIMESTAMP),
(18, 3, 'Pavo', 'Pavo entero fresco', 15.20, 30, false, 'img/pavo.jpg', CURRENT_TIMESTAMP),
(19, 3, 'Chuletas de Cordero', 'Chuletas de cordero tiernas', 13.80, 25, false, 'img/chuletas_cordero.jpg', CURRENT_TIMESTAMP),
(20, 3, 'Pechuga de Pollo Rellenada', 'Pechuga de pollo rellena de espinacas', 8.50, 60, false, 'img/pechuga_pollo_rellenada.jpg', CURRENT_TIMESTAMP),
(21, 3, 'Salchichón', 'Salchichón ibérico de bellota', 4.30, 90, false, 'img/salchichon.jpg', CURRENT_TIMESTAMP),
(22, 3, 'Chistorra', 'Chistorra fresca', 3.80, 100, false, 'img/chistorra.jpg', CURRENT_TIMESTAMP),
(23, 3, 'Hamburguesa de Res', 'Hamburguesa de carne de res', 5.90, 120, false, 'img/hamburguesa_res.jpg', CURRENT_TIMESTAMP),
(24, 3, 'Alitas de Pollo', 'Alitas de pollo marinadas', 6.00, 75, false, 'img/alitas_pollo.jpg', CURRENT_TIMESTAMP),
(25, 3, 'Chorizo', 'Chorizo curado de cerdo', 4.50, 85, false, 'img/chorizo.jpg', CURRENT_TIMESTAMP),
(26, 4, 'Refresco de Limón', 'Refresco de limón 2L', 1.50, 120, false, 'img/refresco_limon.jpg', CURRENT_TIMESTAMP),
(27, 4, 'Agua con Gas', 'Agua con gas 1.5L', 1.40, 150, false, 'img/agua_con_gas.jpg', CURRENT_TIMESTAMP),
(28, 4, 'Té Helado', 'Té helado de durazno', 2.00, 100, false, 'img/te_helado.jpg', CURRENT_TIMESTAMP),
(29, 4, 'Limonada', 'Limonada casera 1.5L', 2.30, 80, false, 'img/limonada.jpg', CURRENT_TIMESTAMP),
(30, 4, 'Gaseosa', 'Gaseosa de limón 2L', 1.80, 110, false, 'img/gaseosa.jpg', CURRENT_TIMESTAMP),
(31, 4, 'Vino Blanco', 'Vino blanco 750ml', 5.60, 70, false, 'img/vino_blanco.jpg', CURRENT_TIMESTAMP),
(32, 4, 'Tetra Pack de Jugo', 'Jugo de piña en tetra pack', 1.90, 95, false, 'img/jugo_pina.jpg', CURRENT_TIMESTAMP),
(33, 4, 'Cerveza Negra', 'Cerveza negra 500ml', 2.20, 60, false, 'img/cerveza_negra.jpg', CURRENT_TIMESTAMP),
(34, 4, 'Vino Rosado', 'Vino rosado 750ml', 7.20, 50, false, 'img/vino_rosado.jpg', CURRENT_TIMESTAMP);


/* 
 * CARRITOS DE COMPRA
 */


INSERT INTO CARRITO_COMPRA (ID, ID_USUARIO) VALUES
(1, 1),
(2, 2),
(3, 3);


/* 
 * ITEMS DE CARRITO
 */


INSERT INTO ITEMS_CARRITO (ID_CARRITO, ID_PRODUCTO, CANTIDAD) VALUES
(1, 1, 2), -- Usuario 1 tiene 2 Manzanas en su carrito
(1, 3, 3), -- Usuario 1 tiene 3 Leches en su carrito
(2, 5, 4), -- Usuario 2 tiene 4 Pollos en su carrito
(2, 7, 5), -- Usuario 2 tiene 5 Coca Colas en su carrito
(3, 2, 6), -- Usuario 3 tiene 6 Plátanos en su carrito
(3, 6, 4); -- Usuario 3 tiene 4 Terneras en su carrito


