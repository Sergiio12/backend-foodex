

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
(NEXT VALUE FOR USERS_SEQ, 'sasensior', '$2a$10$yh4xQHbb8nS/8VIp0zCr8.nWnX1lw/OTo8o4fTVGzss7Lyewt3SoW', 'sasensior03@educarex.es', 'Asensio', 'Rodríguez', 'Sergio', true, '2025-02-19'),
(NEXT VALUE FOR USERS_SEQ, 'jlopez', '$2a$10$abcd1234abcd1234abcd12abcd1234abcd1234abcd1234abcd12', 'jlopez@example.com', 'Juan', 'López', 'Juan López', true, '2025-01-15'),
(NEXT VALUE FOR USERS_SEQ, 'mgarcia', '$2a$10$abcd5678abcd5678abcd56abcd5678abcd5678abcd5678abcd56', 'mgarcia@example.com', 'María', 'García', 'María García', true, '2025-02-01'),
(NEXT VALUE FOR USERS_SEQ, 'cmartinez', '$2a$10$xyz98765xyz98765xyz98xyz98765xyz98765xyz98765xyz98', 'cmartinez@example.com', 'Carlos', 'Martínez', 'Carlos Martínez', false, '2025-02-10'),
(NEXT VALUE FOR USERS_SEQ, 'lfernandez', '$2a$10$mnw65432mnw65432mnw65mnw65432mnw65432mnw65432mnw65', 'lfernandez@example.com', 'Laura', 'Fernández', 'Laura Fernández', true, '2025-02-15'),
(NEXT VALUE FOR USERS_SEQ, 'psanchez', '$2a$10$qwe4321qwe4321qwe432qwe4321qwe4321qwe4321qwe432', 'psanchez@example.com', 'Pedro', 'Sánchez', 'Pedro Sánchez', true, '2025-02-18'),
(NEXT VALUE FOR USERS_SEQ, 'adominguez', '$2a$10$rty5678rty5678rty567rty5678rty5678rty5678rty567', 'adominguez@example.com', 'Ana', 'Domínguez', 'Ana Domínguez', false, '2025-01-28'),
(NEXT VALUE FOR USERS_SEQ, 'ldiaz', '$2a$10$zxc9876zxc9876zxc987zxc9876zxc9876zxc9876zxc987', 'ldiaz@example.com', 'Luis', 'Díaz', 'Luis Díaz', true, '2025-02-05'),
(NEXT VALUE FOR USERS_SEQ, 'scastro', '$2a$10$vbn1234vbn1234vbn123vbn1234vbn1234vbn1234vbn123', 'scastro@example.com', 'Sofía', 'Castro', 'Sofía Castro', true, '2025-02-12'),
(NEXT VALUE FOR USERS_SEQ, 'mortega', '$2a$10$asd4567asd4567asd456asd4567asd4567asd4567asd456', 'mortega@example.com', 'Manuel', 'Ortega', 'Manuel Ortega', false, '2025-02-14'),
(NEXT VALUE FOR USERS_SEQ, 'ejimenez', '$2a$10$fgh6789fgh6789fgh678fgh6789fgh6789fgh6789fgh678', 'ejimenez@example.com', 'Elena', 'Jiménez', 'Elena Jiménez', true, '2025-02-19');


INSERT INTO USER_ROLES (ID_USER, ID_ROL) VALUES
(1, 2),
(1, 1);


/*
* CATEGORÍAS
*/


INSERT INTO CATEGORIAS (ID, NOMBRE, DESCRIPCION, IMG_URL, IMG_ORIGEN) VALUES
(NEXT VALUE FOR CATEGORIAS_SEQ, 'Frutas', 'Frutas frescas y naturales, cultivadas con esmero para ofrecerte lo mejor de la naturaleza. Desde jugosas manzanas hasta dulces naranjas, nuestras frutas son una fuente perfecta de vitaminas y frescura, ideales para disfrutar en cualquier momento del día.', 
'c_frutas.jpg', 'STATIC'),
(NEXT VALUE FOR CATEGORIAS_SEQ, 'Lácteos', 'Productos derivados de la leche de alta calidad, que aportan un delicioso sabor y una nutrición excepcional. Desde quesos curados hasta yogures cremosos, cada producto está cuidadosamente elaborado para garantizarte una experiencia inigualable.', 
'c_lacteos.jpg', 'STATIC'),
(NEXT VALUE FOR CATEGORIAS_SEQ, 'Carnes', 'Carnes frescas, tiernas y sabrosas, provenientes de los mejores proveedores. Cada corte ha sido seleccionado para asegurar la máxima calidad y sabor en cada preparación. Ya sea para un asado en familia o un guiso casero, nuestras carnes son siempre la mejor opción.', 
'c_carnes.jpg', 'STATIC'),
(NEXT VALUE FOR CATEGORIAS_SEQ, 'Bebidas', 'Refrescos y bebidas variadas que van desde las más refrescantes hasta las más exóticas. Ya sea que prefieras una bebida energética, un refresco con gas o una bebida saludable, tenemos opciones para cada gusto y ocasión.', 
'c_bebidas.jpg', 'STATIC'),
(NEXT VALUE FOR CATEGORIAS_SEQ, 'Verduras', 'Verduras frescas y orgánicas, llenas de nutrientes y sabor. Desde lechugas crujientes hasta zanahorias dulces y frescas, nuestras verduras son cultivadas sin químicos ni pesticidas, brindándote lo más puro de la tierra para una alimentación sana y deliciosa.', 
'c_verduras.jpg', 'STATIC'),
(NEXT VALUE FOR CATEGORIAS_SEQ, 'Panadería', 'Panes y productos de pastelería elaborados con ingredientes frescos y de la más alta calidad. Desde baguettes crujientes hasta pasteles esponjosos, cada bocado es una explosión de sabor, ideal para acompañar tus desayunos, meriendas o cualquier momento especial.', 
'c_panaderia.jpg', 'STATIC');

/*
* PRODUCTOS
*/


INSERT INTO PRODUCTOS (ID, ID_CATEGORIA, NOMBRE, DESCRIPCION, PRECIO, STOCK, DESCATALOGADO, IMG_URL, IMG_ORIGEN, FECHA_ALTA) VALUES
(NEXT VALUE FOR PRODUCTOS_SEQ, 1, 'Manzana Verde', 'Manzana verde crujiente', 1.40, 95, false, 'manzana_verde.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 1, 'Mandarina', 'Mandarina dulce y jugosa', 1.50, 110, false, 'mandarina.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 1, 'Cereza', 'Cereza roja fresca', 3.20, 70, false, 'cereza.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 1, 'Melocotón', 'Melocotón maduro y suave', 2.30, 85, false, 'melocoton.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 1, 'Papaya', 'Papaya tropical y dulce', 3.50, 60, false, 'papaya.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 1, 'Aguacate', 'Aguacate maduro y cremoso', 2.80, 80, false, 'aguacate.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 1, 'Arándano', 'Arándano fresco y antioxidante', 4.00, 50, false, 'arandano.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 1, 'Granada', 'Granada con granos rojos', 2.60, 65, false, 'granada.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 2, 'Leche Desnatada', 'Leche desnatada baja en grasa', 1.60, 90, false, 'leche_desnatada.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 2, 'Yogur Griego', 'Yogur griego natural', 2.20, 75, false, 'yogur_griego.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 2, 'Cuajada', 'Cuajada de leche fresca', 1.80, 60, false, 'cuajada.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 2, 'Leche de Almendra', 'Leche de almendra sin azúcar', 2.50, 50, false, 'leche_almendra.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 2, 'Crema Pastelera', 'Crema pastelera para repostería', 2.00, 65, false, 'crema_pastelera.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 2, 'Leche de Coco', 'Leche de coco sin conservantes', 3.10, 40, false, 'leche_coco.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 2, 'Ricotta', 'Ricotta fresca de vaca', 3.80, 50, false, 'ricotta.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 2, 'Manteca de Cerdo', 'Manteca de cerdo para cocinar', 2.10, 80, false, 'manteca_cerdo.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 3, 'Costillas de Cerdo', 'Costillas de cerdo a la barbacoa', 10.50, 55, false, 'costillas_cerdo.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 3, 'Pavo', 'Pavo entero fresco', 15.20, 30, false, 'pavo.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 3, 'Chuletas de Cordero', 'Chuletas de cordero tiernas', 13.80, 25, false, 'chuletas_cordero.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 3, 'Pechuga de Pollo Rellenada', 'Pechuga de pollo rellena de espinacas', 8.50, 60, false, 'pechuga_pollo_rellenada.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 3, 'Salchichón', 'Salchichón ibérico de bellota', 4.30, 90, false, 'salchichon.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 3, 'Chistorra', 'Chistorra fresca', 3.80, 100, false, 'chistorra.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 3, 'Hamburguesa de Res', 'Hamburguesa de carne de res', 5.90, 120, false, 'hamburguesa_res.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 3, 'Alitas de Pollo', 'Alitas de pollo marinadas', 6.00, 75, false, 'alitas_pollo.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 3, 'Chorizo', 'Chorizo curado de cerdo', 4.50, 85, false, 'chorizo.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 4, 'Agua con Gas', 'Agua con gas 1.5L', 1.40, 150, false, 'agua_con_gas.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 4, 'Té Helado', 'Té helado de durazno', 2.00, 100, false, 'te_helado.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 4, 'Limonada', 'Limonada casera 1.5L', 2.30, 80, false, 'limonada.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 4, 'Vino Blanco', 'Vino blanco 750ml', 5.60, 70, false, 'vino_blanco.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 4, 'Tetra Pack de Jugo', 'Jugo de piña en tetra pack', 1.90, 95, false, 'jugo_pina.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 4, 'Cerveza Negra', 'Cerveza negra 500ml', 2.20, 60, false, 'cerveza_negra.jpg', 'STATIC', CURRENT_TIMESTAMP),
(NEXT VALUE FOR PRODUCTOS_SEQ, 4, 'Vino Rosado', 'Vino rosado 750ml', 7.20, 50, false, 'vino_rosado.jpg', 'STATIC', CURRENT_TIMESTAMP);

/* 
 * COMPRAS
 */

INSERT INTO COMPRAS (ID, ID_USUARIO, ESTADO, FECHA_HORA, COMENTARIO, TELEFONO, EMAIL, COD_POSTAL, PROVINCIA, CALLE, BLOQUE, PORTAL, MONTO) VALUES
(NEXT VALUE FOR COMPRAS_SEQ, 1, 'PAGADA', '2025-03-01 10:30:00', 'Entrega rápida solicitada', '123456789', 'sasensior03@educarex.es', '28001', 'Madrid', 'Calle Gran Vía', 'B1', 'P3', 15.20),
(NEXT VALUE FOR COMPRAS_SEQ, 2, 'PENDIENTE', '2025-03-02 14:45:00', NULL, '987654321', 'jlopez@example.com', '08002', 'Barcelona', 'Calle Aragón', 'B2', 'P1', 23.50),
(NEXT VALUE FOR COMPRAS_SEQ, 3, 'CANCELADA', '2025-03-03 18:10:00', 'Pedido cancelado por el usuario', '654321987', 'mgarcia@example.com', '41003', 'Sevilla', 'Avenida de la Constitución', 'B3', 'P2', 30.75),
(NEXT VALUE FOR COMPRAS_SEQ, 4, 'PAGADA', '2025-03-04 09:20:00', 'Entrega en mano', '741852963', 'cmartinez@example.com', '29015', 'Málaga', 'Calle Larios', 'B4', 'P5', 12.99),
(NEXT VALUE FOR COMPRAS_SEQ, 5, 'PENDIENTE', '2025-03-05 16:00:00', NULL, '963852741', 'lfernandez@example.com', '46001', 'Valencia', 'Plaza del Ayuntamiento', 'B5', 'P3', 45.00),
(NEXT VALUE FOR COMPRAS_SEQ, 6, 'PAGADA', '2025-03-06 12:10:00', 'Requiere cambio de dirección', '159753486', 'psanchez@example.com', '15003', 'A Coruña', 'Calle Real', 'B6', 'P2', 22.80),
(NEXT VALUE FOR COMPRAS_SEQ, 7, 'CANCELADA', '2025-03-07 19:30:00', 'Producto no disponible', '357159486', 'adominguez@example.com', '03002', 'Alicante', 'Avenida Maisonnave', 'B7', 'P4', 18.60),
(NEXT VALUE FOR COMPRAS_SEQ, 8, 'PAGADA', '2025-03-08 11:45:00', NULL, '753951468', 'ldiaz@example.com', '50001', 'Zaragoza', 'Paseo Independencia', 'B8', 'P1', 36.90),
(NEXT VALUE FOR COMPRAS_SEQ, 9, 'PENDIENTE', '2025-03-09 13:15:00', 'Entregar antes de las 14:00', '852369741', 'scastro@example.com', '20004', 'San Sebastián', 'Calle Mayor', 'B9', 'P6', 27.50),
(NEXT VALUE FOR COMPRAS_SEQ, 10, 'PAGADA', '2025-03-10 17:00:00', 'Pedido urgente', '147258369', 'mortega@example.com', '33002', 'Oviedo', 'Calle Uría', 'B10', 'P3', 42.75);


/* 
 * CARRITOS DE COMPRA
 */


INSERT INTO CARRITO_COMPRA (ID, ID_USUARIO) VALUES
(NEXT VALUE FOR CARRITO_COMPRA_SEQ, 1),
(NEXT VALUE FOR CARRITO_COMPRA_SEQ, 2),
(NEXT VALUE FOR CARRITO_COMPRA_SEQ, 3);


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


