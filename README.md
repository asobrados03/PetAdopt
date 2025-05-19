# PetAdopt
Aplicación empresarial para la adopción de mascotas, facilitando la comunicación entre los refugios y los clientes.

## Antes de Ejecutar

Lo primero se debe configurar el realm, para ello dejo a continuación unas capturas de pantalla llamadas realm1.png y 
realm2.png aparte dejo los datos de los campos por si acaso:

Realm Name: jdbc-realm-PetAdopt
JAAS Context: jdbcRealm
JNDI: jdbc/petadopt
User Table: users
User Name Column: email
Password Column: password
Group Table: user_groups
Group Table User Name Column: email
Group Name Column: groupname
Digest Algorithm: SHA-256
Encoding: Base64

Dentro del archivo BD.sql, están todas las sentencias para crear las tablas y debajo unas sentencias de inserción para rellenarlas,
si es la primera vez que rellenas la tabla de pets no hay ningún problema, pero si no debes cambiar los petId de la tabla 
adoptionrequests para que vincule las peticiones a dichas mascotas. Se incluye también el nombre de la BD.

También destacar que todos los usuarios cuentan con la misma contraseña "Contra1234" excepto el admin que tiene "Admin1234".
